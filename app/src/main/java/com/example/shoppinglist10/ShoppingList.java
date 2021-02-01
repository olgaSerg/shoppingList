package com.example.shoppinglist10;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "shopping_lists")
public class ShoppingList {
    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo
    public String name;

    @ColumnInfo
    public String date;

}