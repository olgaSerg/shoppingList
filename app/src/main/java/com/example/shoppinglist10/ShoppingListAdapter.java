package com.example.shoppinglist10;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ShoppingListAdapter extends ArrayAdapter<ShoppingList> {
    public ShoppingListAdapter(Context context, ArrayList<ShoppingList> shoppingLists) {
        super(context, 0, shoppingLists);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ShoppingList shoppingList = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.shopping_list_item, parent, false);
        }

        TextView tvName = convertView.findViewById(R.id.list_name);
        tvName.setText(shoppingList.name);
        return convertView;
    }
}
