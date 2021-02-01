package com.example.shoppinglist10;

import android.app.Application;

import androidx.room.Room;

import com.facebook.stetho.Stetho;


public class ProductApplication extends Application {
    ProductDatabase productDatabase;

    @Override
    public void onCreate() {
        super.onCreate();

        // when upgrading versions, kill the original tables by using fallbackToDestructiveMigration()
        productDatabase = Room.databaseBuilder(this, ProductDatabase.class, ProductDatabase.NAME).fallbackToDestructiveMigration().build();
        // чтобы смотреть бд через хром
        Stetho.initializeWithDefaults(this);
    }

    public ProductDatabase getDatabase() {
        return productDatabase;
    }
}
