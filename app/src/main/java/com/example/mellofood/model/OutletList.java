package com.example.mellofood.model;

public class OutletList {
    int id;
    String tvStoreName, isLike;
    private int ivStoreImage;

    public OutletList(int id, String tvStoreName, String isLike, int ivStoreImage) {
        this.id = id;
        this.tvStoreName = tvStoreName;
        this.isLike = isLike;
        this.ivStoreImage = ivStoreImage;
    }

    public String getIsLike() {
        return isLike;
    }

    public void setIsLike(String isLike) {
        this.isLike = isLike;
    }

    public String getTvStoreName() {
        return tvStoreName;
    }

    public void setTvStoreName(String tvStoreName) {
        this.tvStoreName = tvStoreName;
    }

    public int getIvStoreImage() {
        return ivStoreImage;
    }

    public void setIvStoreImage(int ivStoreImage) {
        this.ivStoreImage = ivStoreImage;
    }

    public int getId() {
        return id;
    }
}
