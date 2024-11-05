package com.raulrh.practicaandroid.ui.shoppinglist.data;

import android.graphics.Bitmap;

public class ShoppingItem {
    private int id;
    private String name;
    // private Bitmap image;

    public ShoppingItem() {

    }

    public ShoppingItem(String name/*, Bitmap image*/) {
        this.name = name;
        // this.image = image;
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

    /*public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }*/
}