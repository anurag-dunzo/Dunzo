package entities;

import java.util.Map;

public class Machine {

    private Outlet outlets;

    private Map<String, Integer> total_items_quantity;

    private Map<String, Map<String, Integer>> beverages;

    public Outlet getOutlets() {
        return outlets;
    }

    public void setOutlets(Outlet outlets) {
        this.outlets = outlets;
    }

    public Map<String, Integer> getTotal_items_quantity() {
        return total_items_quantity;
    }

    public void setTotal_items_quantity(Map<String, Integer> total_items_quantity) {
        this.total_items_quantity = total_items_quantity;
    }

    public Map<String, Map<String, Integer>> getBeverages() {
        return beverages;
    }

    public void setBeverages(Map<String, Map<String, Integer>> beverages) {
        this.beverages = beverages;
    }
}
