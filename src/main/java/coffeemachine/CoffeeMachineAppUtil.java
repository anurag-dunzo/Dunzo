package coffeemachine;

import entities.Beverage;
import entities.Machine;
import inventory.Inventory;
import inventory.ItemInventory;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import java.util.List;
import java.util.Map;

/**
 * utility class that is used by the coffee machine app to parse input.
 */
public class CoffeeMachineAppUtil {
    /**
     * parses the --file arguments and returns the list of files.
     *
     * @param args
     * @return
     */
    public List<String> parseArguments(String[] args) {
        ArgumentParser parser = ArgumentParsers.newFor("CoffeeMachine").build()
                .defaultHelp(true)
                .description("enter input file paths");

        parser.addArgument("--inputFile").nargs("*").required(true)
                .help("input file for testing the vending machine with different quantities and types of beverages.");

        Namespace namespace = null;
        try {
            namespace = parser.parseArgs(args);
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }
        return namespace.<String>getList("inputFile");
    }

    /**
     * Initializes CoffeeMachine based on the input json file.
     *
     * @param inputMachine
     * @return
     */
    public CoffeeMachine initialize(Machine inputMachine) {
        Inventory inventory = initializeInventory(inputMachine);
        return new CoffeeMachineImpl(inventory, inputMachine.getOutlets().getCount_n());
    }

    /**
     * Initializes Inventory for the coffee machine.
     *
     * @param inputMachine
     * @return
     */
    private Inventory initializeInventory(Machine inputMachine) {
        Inventory inventory = new ItemInventory();
        Map<String, Integer> totalItemsQuantity = inputMachine.getTotal_items_quantity();
        totalItemsQuantity.forEach((key, value) ->
                {
                    Beverage inventoryItem = Beverage.createInventoryItem(key, value);
                    // adding a minimum required quantity chosen at random between 10 to 30 to simulate the `running low on` usecase.
                    inventory.addItemWithMinimumQuantityThreshold(inventoryItem, getRandomQuantity(10, 30));
                }
        );
        return inventory;
    }

    /**
     * Random integer between lower and upper bound
     *
     * @param lower
     * @param upper
     * @return
     */
    public int getRandomQuantity(int lower, int upper) {
        return (int) (Math.random() * (upper - lower)) + lower;
    }
}
