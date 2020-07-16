package coffeemachine;

import entities.Beverage;
import inventory.Inventory;

public interface CoffeeMachine {

    void prepare(Beverage beverageToPrepare);

    Inventory getInventory();
}
