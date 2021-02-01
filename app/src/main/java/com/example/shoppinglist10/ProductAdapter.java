package com.example.shoppinglist10;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

public class ProductAdapter extends ArrayAdapter<Product> {
    public ProductAdapter(Context context, ArrayList<Product> products) {
        super(context, 0, products);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.v("get view", String.valueOf(position));
        final Product product = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.product_item, parent, false);
        }


        ((NewListActivity)getContext()).registerForContextMenu(convertView);

        TextView tvName = convertView.findViewById(R.id.product_name);

        tvName.setText(product.name);
        int textColor;
        if (product.is_checked) {
            textColor = Color.LTGRAY;
        } else {
            textColor = Color.BLACK;
        }
        tvName.setTextColor(textColor);
        CheckBox checkBox = convertView.findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(null);
        checkBox.setChecked(product.is_checked);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("event","checked " + isChecked);
                final ProductDao productDao = ((NewListActivity)getContext()).getProductDao();
                product.is_checked = isChecked;
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        productDao.insertProduct(product);
                        ((NewListActivity)getContext()).runOnUiThread(new Runnable() {
                            public void run() {
                                ((NewListActivity)getContext()).loadShoppingList();
                            }
                        });
                    }
                });

            }
        });

        final View itemView = convertView;

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = itemView.findViewById(R.id.checkbox);
                if (!checkBox.isChecked()) {
                    checkBox.toggle();
                }
            }
        };
        convertView.setOnClickListener(clickListener);
        return convertView;
    }
}