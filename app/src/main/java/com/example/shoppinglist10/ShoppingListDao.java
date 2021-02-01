package com.example.shoppinglist10;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ShoppingListDao {
    @Query("SELECT * FROM shopping_lists where id = :id")
    ShoppingList getById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertShoppingList(ShoppingList shoppingList);

    @Query("SELECT * FROM shopping_lists")
    ShoppingList[] getShoppingLists();

    @Query("SELECT * FROM shopping_lists ORDER BY date DESC")
    ShoppingList[] getSortedShoppingLists();

    @Delete
    void deleteShoppingList(ShoppingList shoppingList);
}