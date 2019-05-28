package com.example.mellofood.model;

public class MenuList {
    private String tvMenuItemName, tvMenuItemDescription, tvMenuItemPrice;
    private int ivMenuItemImage;

    public String getTvMenuItemName() {
        return tvMenuItemName;
    }

    public int getIvMenuItemImage() {
        return ivMenuItemImage;
    }

    public String getTvMenuItemDescription() {
        return tvMenuItemDescription;
    }

    public String getTvMenuItemPrice() {
        return tvMenuItemPrice;
    }

    public MenuList(String tvMenuItemName, String tvMenuItemDescription, String tvMenuItemPrice, int ivMenuItemImage) {
        this.tvMenuItemName = tvMenuItemName;
        this.tvMenuItemDescription = tvMenuItemDescription;
        this.tvMenuItemPrice = tvMenuItemPrice;
        this.ivMenuItemImage = ivMenuItemImage;
    }
}
