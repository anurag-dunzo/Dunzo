package entities;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Beverage {

    private final String name;

    private volatile AtomicInteger quantity;

    private Map<String, Integer> requiredIngredientQuantityMap;

    private Beverage(String name, Map<String, Integer> requiredIngredientQuantityMap) {
        this.name = name;
        this.requiredIngredientQuantityMap = requiredIngredientQuantityMap;
    }

    private Beverage(String name, int quantity) {
        this.name = name;
        this.quantity = new AtomicInteger(quantity);
    }

    public static Beverage createInventoryItem(String name, int quantity) {
        return new Beverage(name, quantity);
    }

    public static Beverage createBeveragesItem(String name, Map<String, Integer> requiredIngredientQuantityMap) {
        return new Beverage(name, requiredIngredientQuantityMap);
    }

    public Map<String, Integer> getRequiredIngredientQuantityMap() {
        return requiredIngredientQuantityMap;
    }

    public String getName() {
        return name;
    }

    public AtomicInteger getQuantity() {
        return quantity;
    }

    public void setQuantity(AtomicInteger quantity) {
        this.quantity = quantity;
    }
}
