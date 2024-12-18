package com.raulrh.practicaandroid.ui.shoppinglist.data;

public class ShoppingItem {
    private int id;
    private String name;
    private String imagePath;
    private String category;

    public ShoppingItem() {
    }

    public ShoppingItem(String name, String imagePath, String category) {
        this.name = name;
        this.imagePath = imagePath;
        this.category = category;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}