package org.example.pojos_bodies.request.adminrequests;

public class Items {
    private String itemId;
    private int quantity;
    public Items(){}
    public Items(String itemId, int quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
    }
    public String getItemId() {
        return itemId;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
