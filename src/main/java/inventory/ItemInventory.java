package inventory;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import entities.Beverage;

public class ItemInventory implements Inventory {

    private ConcurrentMap<String, Beverage> itemMap;

    private ConcurrentHashMap<Beverage, Integer> minimumItemQuantityMap;

    public ItemInventory() {
        itemMap = new ConcurrentHashMap<>(5);
        minimumItemQuantityMap = new ConcurrentHashMap<>(5);
    }

    /**
     * Add item with default threshold of zero
     *
     * @param item
     */
    @Override
    public void addItem(Beverage item) {
        addItemWithMinimumQuantityThreshold(item, 0);
    }

    /**
     * Add item with a certain threshold in Inventory
     *
     * @param item
     * @param minimumQty
     */
    @Override
    public void addItemWithMinimumQuantityThreshold(Beverage item, int minimumQty) {
        itemMap.putIfAbsent(item.getName(), item);
        minimumItemQuantityMap.putIfAbsent(item, minimumQty);
    }

    @Override
    public Beverage getItem(String name) {
        return itemMap.get(name);
    }


    /**
     * Remove an item from inventory
     *
     * @param name
     * @return
     */
    @Override
    public Beverage removeItem(String name) {
        return itemMap.remove(name);
    }


    /**
     * To refill given items in inventory
     *
     * @param itemName
     * @param qty
     */
    @Override
    public void refill(String itemName, int qty) {
        if (itemMap.containsKey(itemName)) {
            itemMap.get(itemName).setQuantity(new AtomicInteger(qty));
        }
    }

    /**
     * Use this method to get LowRunningItems in inventory
     *
     * @return
     */
    @Override
    public List<Beverage> getLowRunningIngredients() {
        return itemMap.values().stream().filter(item -> {
            if (item.getQuantity().get() < minimumItemQuantityMap.get(item))
                return true;
            return false;
        }).collect(Collectors.toList());
    }

    @Override
    public Set<String> getAllAvailableItems() {
        return itemMap.keySet();
    }

    @Override
    public boolean itemAvailable(String required_item) {
        return getAllAvailableItems().contains(required_item);
    }
}
