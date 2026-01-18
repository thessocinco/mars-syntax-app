package com.marsIT.marsSyntaxApp.InvoiceAdapter;

public class InvoiceProducts {
    public int qty;
    public String description;
    public float unitPrice;
    public float totalAmount;

    public InvoiceProducts(int qty, String description, float unitPrice, float totalAmount) {
        this.qty = qty;
        this.description = description;
        this.unitPrice = unitPrice;
        this.totalAmount = totalAmount;
    }
}
