package com.marsIT.marsSyntaxApp.SalesInventoryAdapter;

public class InventoryItem {
    public String description;
    public int beginningInventory;
    public int salesQuantity;
    public float endingInventory;

    public InventoryItem(String description, int beginningInventory, int salesQuantity, float endingInventory) {
        this.description = description;
        this.beginningInventory = beginningInventory;
        this.salesQuantity = salesQuantity;
        this.endingInventory = endingInventory;
    }
}
