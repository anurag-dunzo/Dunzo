package inventory;

import java.util.List;
import java.util.Set;

import entities.Beverage;

public interface Inventory {

    void addItem(Beverage item);

    void addItemWithMinimumQuantityThreshold(Beverage item, int minimumQuantity);

    Beverage getItem(String name);

    Beverage removeItem(String name);

    public void refill(String itemName, int quantity);

    public List<Beverage> getLowRunningIngredients();

    Set<String> getAllAvailableItems();

    boolean itemAvailable(String requiredItem);
}
