package com.example.crud;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class DrinkAdapter extends BaseAdapter {

    private List<Drink> list;
    private LayoutInflater inflater;
    private Context context;

    public DrinkAdapter(List<Drink> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return list.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null)  {
            view = inflater.inflate(R.layout.grid_item, viewGroup, false);
        }
        TextView txtID = view.findViewById(R.id.txtId);
        TextView txtName = view.findViewById(R.id.txtName);
        TextView txtPrice = view.findViewById(R.id.txtPrice);
        TextView txtStatus = view.findViewById(R.id.txtStatus);
        TextView txtTimeOfCreate = view.findViewById(R.id.txtTimeOfCreate);

        Drink drink = this.list.get(i);

        txtID.setText("ID: "+String.valueOf(drink.getId()));
        txtName.setText("Name: "+drink.getName());
        txtPrice.setText("Price: "+String.valueOf(drink.getPrice()));
        txtStatus.setText("Status: "+(drink.isStatus()?"Finished":"In progress"));
        txtTimeOfCreate.setText("Time of create: "+drink.getTimeOfCreate());
        return view;
    }

    public void setList(List<Drink> list) {
        this.list = list;
    }
}
