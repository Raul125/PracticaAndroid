package com.raulrh.practicaandroid.ui.shoppinglist.data;

public class ShoppingItem {
    private int id; // Añadir un ID
    private String name;
    private int imageResId;

    // Constructor vacío para Gson
    public ShoppingItem() {}

    public ShoppingItem(String name, int imageResId) {
        this.name = name;
        this.imageResId = imageResId;
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

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }
}