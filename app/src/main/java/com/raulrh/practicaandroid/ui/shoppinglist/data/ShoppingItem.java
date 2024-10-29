package com.raulrh.practicaandroid.ui.shoppinglist.data;

public class ShoppingItem {
    private int id;
    private String name;
    private String imageUri;

    public ShoppingItem() {

    }

    public ShoppingItem(String name, String imageUri) {
        this.name = name;
        this.imageUri = imageUri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}