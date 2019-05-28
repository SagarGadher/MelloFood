package com.example.mellofood.model;

public class RestaurantList {
    String tvStoreName, tvDescription;
    private int ivStoreImage;

    public String getTvDescription() {
        return tvDescription;
    }

    public String getTvStoreName() {
        return tvStoreName;
    }

    public int getIvStoreImage() {
        return ivStoreImage;
    }

    public RestaurantList(String tvStoreName, String tvDescription, int ivStoreImage) {
        this.tvStoreName = tvStoreName;
        this.tvDescription = tvDescription;
        this.ivStoreImage = ivStoreImage;
    }
}
