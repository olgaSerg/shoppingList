package com.example.shoppinglist10;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities={Product.class, ShoppingList.class}, version=6)
public abstract class ProductDatabase extends RoomDatabase {
    public abstract ProductDao productDao();

    public abstract ShoppingListDao shoppingListDao();

    public static final String NAME = "ProductDataBase";
}
