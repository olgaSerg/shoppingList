package com.example.shoppinglist10;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ArrayList<ShoppingList> shoppingList = new ArrayList<>();
    ShoppingListAdapter shoppingListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //создание адаптера
        shoppingListAdapter = new ShoppingListAdapter(this, shoppingList);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.GREEN));

        // #835
        ListView listView = findViewById(R.id.listView);

        //??листу установить адаптер (Он еще пустой,просто указывается откуда брать инфу)
        listView.setAdapter(shoppingListAdapter);
        //описываем порядок действий при возникновении события onItemClickListener
        AdapterView.OnItemClickListener itemClickListener =
                new AdapterView.OnItemClickListener(){
                    public void onItemClick(AdapterView<?> listView,
                                            View itemView,
                                            int position,
                                            long id) {
                        final ShoppingList curShoppingList = shoppingListAdapter.getItem(position);
                        Intent intent = new Intent(MainActivity.this,
                                NewListActivity.class);
                        intent.putExtra("list-id", curShoppingList.id);
                        startActivity(intent);
                        Log.v("asddf",String.valueOf(position));
                    }
                };
        //Назначение слушателя для спискового представления(регистрируем обработчик события)
        listView.setOnItemClickListener(itemClickListener);

        registerForContextMenu(listView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadShoppingLists();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete_list, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int itemPosition = info.position;
        // shoppingListAdapter обращается к dataSourсe и получает item по позиции
        final ShoppingList curShoppingList = shoppingListAdapter.getItem(itemPosition);
        final ShoppingListDao shoppingListDao = getShoppingListDao();
        final ProductDao productDao = getProductDao();
        final MainActivity context = this;

        switch (item.getItemId()) {
            case R.id.list_delete:
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        Product[] productsToDelete = productDao.getListProducts(curShoppingList.id);
                        for (int i = 0; i < productsToDelete.length; ++i) {
                            Product curProduct = productsToDelete[i];
                            productDao.deleteProduct(curProduct);
                        }
                        shoppingListDao.deleteShoppingList(curShoppingList);
                        context.loadShoppingLists();
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void loadShoppingLists() {
        final ShoppingListDao shoppingListDao = getShoppingListDao();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                shoppingList.clear();
//                Product[] products = productDao.getListProducts(listId));
//                Collections.addAll(shoppingList, products);
                Collections.addAll(shoppingList, shoppingListDao.getSortedShoppingLists());
                runOnUiThread(new Runnable() {
                    public void run() {
                        shoppingListAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addNewList(View view) {
        final ShoppingListDao shoppingListDao = getShoppingListDao();
        final ShoppingList newShoppingList = new ShoppingList();
        newShoppingList.name = "список";
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        newShoppingList.date = dateFormat.format(currentDate);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final int newListId = (int) shoppingListDao.insertShoppingList(newShoppingList);
                runOnUiThread(new Runnable() {
                    public void run() {
                    Intent intent = new Intent(MainActivity.this,
                        NewListActivity.class);
                    intent.putExtra("list-id", newListId);
                    startActivity(intent);
                    }
                });
            }
        });

    }

    ShoppingListDao getShoppingListDao() {
        return ((ProductApplication) getApplicationContext()).getDatabase().shoppingListDao();
    }
    public ProductDao getProductDao() {
        return ((ProductApplication) getApplicationContext()).getDatabase().productDao();
    }
}
