package com.example.shoppinglist10;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import static androidx.appcompat.app.ActionBar.*;

public class NewListActivity extends AppCompatActivity {
    ArrayList<Product> shoppingList = new ArrayList<>();
    ProductAdapter productAdapter;
    int listId;
    ShoppingList listEntity;

    void loadListEntity() {
        final ShoppingListDao shoppingListDao = getShoppingListDao();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                listEntity = shoppingListDao.getById(listId);
                final String listName = listEntity.name;
                Log.v("asdf", listName);
                runOnUiThread(new Runnable() {
                    public void run() {
                        getSupportActionBar().setTitle(listName);
                    }
                });
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_list);

//        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.GREEN));


        Intent intent = getIntent();
        listId = intent.getIntExtra("list-id", -1);
        loadListEntity();

        productAdapter = new ProductAdapter(this, shoppingList);

        ListView listView = findViewById(R.id.list);
        listView.setAdapter(productAdapter);

        registerForContextMenu(listView);

        EditText productEditText = findViewById(R.id.newProduct);
        final Button newProductButton = findViewById(R.id.newProductButton);
        newProductButton.setEnabled(false);
        productEditText.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {
                newProductButton.setEnabled(!s.toString().equals(""));
            }
        });

        loadShoppingList();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_actions, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int itemPosition = info.position;
        final Product curProduct = productAdapter.getItem(itemPosition);
        final ProductDao productDao = getProductDao();
        final NewListActivity context = this;

        switch (item.getItemId()) {
            case R.id.list_action_delete:
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        productDao.deleteProduct(curProduct);
                        context.loadShoppingList();
                    }
                });
                break;
            case R.id.list_action_edit:
                AlertDialog.Builder builder = new AlertDialog.Builder(NewListActivity.this);
                builder.setMessage("Update the product");
                builder.setCancelable(true);

                LayoutInflater inflater = getLayoutInflater();

                final View dialogView = inflater.inflate(R.layout.dialog_change_name, null);
                EditText etName = dialogView.findViewById(R.id.new_name);

                etName.setText(curProduct.name);

                builder.setView(dialogView);


                builder.setPositiveButton(
                        "Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                EditText etName = dialogView.findViewById(R.id.new_name);
                                String newProductName = etName.getText().toString();

                                curProduct.name = newProductName;

                                AsyncTask.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        productDao.insertProduct(curProduct);
                                        context.loadShoppingList();
                                    }
                                });

                                dialog.cancel();
                            }
                        });

                builder.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                builder.create().show();
                break;
        }
        return super.onContextItemSelected(item);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                //Write your logic here
//                this.finish();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        final NewListActivity context = this;

        if (id == android.R.id.home) {
            //Write your logic here
            this.finish();
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.change_name) {
            AlertDialog.Builder builder = new AlertDialog.Builder(NewListActivity.this);
            builder.setCancelable(true);

            LayoutInflater inflater = getLayoutInflater();

            final View dialogView = inflater.inflate(R.layout.dialog_change_name, null);
            EditText etName = dialogView.findViewById(R.id.new_name);


//            etName.setText(shoppingList.name);
            etName.setText(listEntity.name);

            builder.setView(dialogView);
            builder.setPositiveButton(
                    "Save",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            EditText etName = dialogView.findViewById(R.id.new_name);
                            String newShoppingListName = etName.getText().toString();
                            final ShoppingListDao shoppingListDao = getShoppingListDao();

                            listEntity.name = newShoppingListName;


                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                    shoppingListDao.insertShoppingList(listEntity);
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            loadListEntity();
                                        }
                                    });
                                }
                            });

                            dialog.cancel();
                        }
                    });

            builder.setNegativeButton(
                    "Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            builder.create().show();

        }
        return super.onOptionsItemSelected(item);
    }
//        if (id == R.id.delete_all) {
//            final ProductDao ProductDao = getProductDao();
//            AsyncTask.execute(new Runnable() {
//                @Override
//                public void run() {
//                    Product[] products = ProductDao.getAllProducts();
//                    for (int i = 0; i < products.length; ++i) {
//                        Product curProduct = products[i];
//                        ProductDao.deleteProduct(curProduct);
//                    }
//                    context.loadShoppingList();
//                }
//            });
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//


    public ProductDao getProductDao() {
        return ((ProductApplication) getApplicationContext()).getDatabase().productDao();
    }

    ShoppingListDao getShoppingListDao() {
        return ((ProductApplication) getApplicationContext()).getDatabase().shoppingListDao();
    }

    public void addToShoppingList(View view) {
        EditText productEditText = findViewById(R.id.newProduct);
        String product = productEditText.getText().toString();

        if (product.length() == 0) {
            return;
        }

        final ProductDao productDao = getProductDao();
        final Product newProduct = new Product();
        newProduct.name = product;
        newProduct.list_id = listId;
        newProduct.is_checked = false;

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                productDao.insertProduct(newProduct);
                loadShoppingList();
            }
        });

        productEditText.setText("");
    }

    void loadShoppingList() {
        final ProductDao productDao = getProductDao();
        AsyncTask.execute(new Runnable() {
            @Override
            public synchronized void run() {
//                Product[] products = productDao.getListProducts(listId));
//                Collections.addAll(shoppingList, products);
                shoppingList.clear();
                Collections.addAll(shoppingList, productDao.getListProducts(listId));
                runOnUiThread(new Runnable() {
                    public void run() {
                        productAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }
}





