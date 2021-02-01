package com.example.shoppinglist10;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "products", foreignKeys = @ForeignKey(entity=ShoppingList.class, parentColumns="id", childColumns="list_id"))
public class Product {
    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo
    public String name;

    @ColumnInfo
    int list_id;

    @ColumnInfo
    boolean is_checked;


//    @ColumnInfo
//    public String number;
//
//    @ColumnInfo
//    public String group;


}

// [
//   Product(id=10, name="myaso", list_id=5, is_checked=false),
//   Product(id=12, name="luk", list_id=5, is_checked=true),
//   Product(id=15, name="botva", list_id=5, is_checked=false),
//   Product(id=17, name="hren", list_id=5, is_checked=true),
// ]