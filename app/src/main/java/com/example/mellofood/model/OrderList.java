package com.example.mellofood.model;

public class OrderList {
    String ItemName,ItemPrice,ItemAmount,ItemDescription;

    public OrderList(String itemName, String itemDescription, String itemPrice, String itemAmount) {
        ItemName = itemName;
        ItemPrice = itemPrice;
        ItemAmount = itemAmount;
        ItemDescription = itemDescription;
    }

    public String getItemName() {
        return ItemName;
    }

    public String getItemPrice() {
        return ItemPrice;
    }

    public String getItemAmount() {
        return ItemAmount;
    }

    public String getItemDescription() {
        return ItemDescription;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public void setItemPrice(String itemPrice) {
        ItemPrice = itemPrice;
    }

    public void setItemAmount(String itemAmount) {
        ItemAmount = itemAmount;
    }

    public void setItemDescription(String itemDescription) {
        ItemDescription = itemDescription;
    }
}
