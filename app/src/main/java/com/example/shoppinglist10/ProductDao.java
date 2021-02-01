package com.example.shoppinglist10;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM products where id = :id")
    Product getById(int id);

    @Query("SELECT * FROM products")
    Product[] getAllProducts();

    @Query("SELECT * FROM products where list_id = :listId")
    Product[] getListProducts(int listId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertProduct(Product product);

    @Delete
    void deleteProduct(Product product);

}

