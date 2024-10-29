package com.raulrh.practicaandroid.ui.shoppinglist.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.raulrh.practicaandroid.util.Util;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "shoppingList.db";
    private static final int DATABASE_VERSION = 4;

    public static final String TABLE_SHOPPING = "shopping";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_IMAGE_URI = "image_uri";

    public ShoppingListDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SHOPPING_TABLE = "CREATE TABLE " + TABLE_SHOPPING + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_IMAGE_URI + " TEXT" + ")";
        db.execSQL(CREATE_SHOPPING_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPPING);
        onCreate(db);
    }

    public void addShoppingItem(ShoppingItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, item.getName());
        values.put(COLUMN_IMAGE_URI, Util.getBytes(item.getImage()));
        db.insert(TABLE_SHOPPING, null, values);
        db.close();
    }

    public List<ShoppingItem> getAllShoppingItems() {
        List<ShoppingItem> itemList = new ArrayList<>();
        final String[] SELECT = {COLUMN_ID, COLUMN_NAME, COLUMN_IMAGE_URI};
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SHOPPING, SELECT, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                ShoppingItem item = new ShoppingItem();
                item.setId(cursor.getInt(0));
                item.setName(cursor.getString(1));
                item.setImage(Util.getBitmap(cursor.getBlob(2)));
                itemList.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return itemList;
    }

    public void deleteShoppingItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SHOPPING, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}

