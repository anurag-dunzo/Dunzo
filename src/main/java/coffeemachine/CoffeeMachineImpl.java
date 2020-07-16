package coffeemachine;

import entities.Beverage;
import inventory.Inventory;

import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class CoffeeMachineImpl implements CoffeeMachine {


    private static final String INGREDIENT_NOT_AVAILABLE_IN_INVENTORY_TEMPLATE = "beverage {0} cannot be prepared because ingredient {1} is not available";
    private static final String INGREDIENT_NOT_AVAILABLE_IN_ENOUGH_QUANTITY_TO_PREPARE_BEVERAGE_TEMPLATE = "beverage {0} cannot be prepared because amount of item {1} left is {2}, whereas required quantity is {3}";
    private static final String BEVERAGE_IS_PREPARED_TEMPLATE = "beverage {0} is prepared";
    
    private Inventory inventory;
    Semaphore outlet = null;

    public CoffeeMachineImpl(Inventory inventory, int outlets) {
        this.inventory = inventory;
        this.outlet = new Semaphore(outlets);
    }

    /**
     * This method first checks the inventory and then prepares the input beverage if the ingredients are available in required quantity.
     *
     * @param beverageToPrepare
     */
    @Override
    public void prepare(Beverage beverageToPrepare) {
        boolean wasItemPreperationSuccessful = true;
        
        try {
            outlet.acquire();
            if (allItemsAvailable(beverageToPrepare)) {
                wasItemPreperationSuccessful = updateAndGetItemsFromInventory(beverageToPrepare);
            } else {
                wasItemPreperationSuccessful = false;
            }
        } catch (InterruptedException e) {
            wasItemPreperationSuccessful = false;
            e.printStackTrace();
        } finally {
            outlet.release();
        }
        
        if (wasItemPreperationSuccessful) {
    			printMessage(MessageFormat.format(BEVERAGE_IS_PREPARED_TEMPLATE, beverageToPrepare.getName()));
        }
    }

	private boolean updateAndGetItemsFromInventory(Beverage beverageToPrepare) {
		boolean isItemPrepared = true;
		for(Map.Entry<String, Integer> entry : beverageToPrepare.getRequiredIngredientQuantityMap().entrySet()) {
		    String requiredItem = entry.getKey();
		    Integer requiredQuantity = entry.getValue();
		    Beverage itemFromInventory = inventory.getItem(requiredItem);
		    if (itemFromInventory != null) {
		        int existingQuantity = itemFromInventory.getQuantity().get();
		        //update the remaining quantity if there's enough of the ingredient left.
		        itemFromInventory.getQuantity().updateAndGet(quantity -> (quantity - requiredQuantity) >= 0 ? (quantity - requiredQuantity) : quantity);
		        //if quantity is unchanged then the ingredient was not available in enough quantity.
		        if (itemFromInventory.getQuantity().get() == existingQuantity) {
		            printMessage(MessageFormat.format(INGREDIENT_NOT_AVAILABLE_IN_ENOUGH_QUANTITY_TO_PREPARE_BEVERAGE_TEMPLATE, beverageToPrepare.getName(), requiredItem,
		                    itemFromInventory.getQuantity(), requiredQuantity));
		            isItemPrepared = false;
		            break;
		        }
		    } else {
		    		printMessage(MessageFormat.format(INGREDIENT_NOT_AVAILABLE_IN_INVENTORY_TEMPLATE, beverageToPrepare.getName(), requiredItem));
		        isItemPrepared = false;
		        break;
		    }
		}
		return isItemPrepared;
	}

    /**
     * Check whether all items required to prepare the beverage are available.
     *
     * @param beverageToPrepare
     * @return
     */
    private boolean allItemsAvailable(Beverage beverageToPrepare) {
        AtomicBoolean allItemsAvailable = new AtomicBoolean(true);
        for (String requiredItem : beverageToPrepare.getRequiredIngredientQuantityMap().keySet()) {
            if (!inventory.itemAvailable(requiredItem)) {
            		printMessage(MessageFormat.format(INGREDIENT_NOT_AVAILABLE_IN_INVENTORY_TEMPLATE, beverageToPrepare.getName(), requiredItem));
                allItemsAvailable.set(false);
                break;
            }
        }
        return allItemsAvailable.get();
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
    
    /*
     * this prints to the console now, can be changed to write to a log file as well.
     */
    private static void printMessage(String message) {
    		System.out.println(message);
    }

}
