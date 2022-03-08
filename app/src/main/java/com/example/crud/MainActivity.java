package com.example.crud;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button btnCreate;
    private DrinkDAO dao;
    private DrinkAdapter adapter;
    private GridView gvDrinks;
    private EditText edtFromPrice;
    private EditText edtToPrice;
    private Button btnSearch;
    private List<Drink> lst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dao = new DrinkDAO();
        btnCreate = findViewById(R.id.btnCreate);
        gvDrinks = findViewById(R.id.gvDrinks);
        edtFromPrice = findViewById(R.id.edtFromPrice);
        edtToPrice = findViewById(R.id.edtToPrice);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateActivity.class);
                intent.putExtra("isUpdating", false);
                startActivityForResult(intent, 123);
            }
        });
        loadList();
    }
    private void getFromRaw(){
        InputStream is = getResources().openRawResource(R.raw.data);
        List<Drink> result = null;
        try {
            result = dao.loadFromRAR(is);
            FileOutputStream fos = openFileOutput("drinks.txt", MODE_PRIVATE);
            if (result.size()>0)
            {
                lst.addAll(result);
                dao.saveToInternal(fos, lst);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 123) {
            if(resultCode == RESULT_OK) {
                Toast.makeText(this, "Created drink successfully!", Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode == 234) {
            if(resultCode == RESULT_OK) {
                Toast.makeText(this, "Updated drink successfully", Toast.LENGTH_SHORT).show();
            }
        }
        loadList();
    }

    private void loadList() {
        //load list from file
        try(FileInputStream fis = openFileInput("drinks.txt")) {
            lst = dao.loadFromInternal(fis);
           // getFromRaw();
        } catch(Exception e) {
            lst = new ArrayList<>();
        }
        adapter = new DrinkAdapter(lst, this);
        gvDrinks.setAdapter(adapter);
        gvDrinks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                Drink selectedDrink = (Drink) gvDrinks.getItemAtPosition(i);
                intent.putExtra("drink", selectedDrink);
                startActivityForResult(intent, 234);
            }
        });
    }

    public void clickToSearch(View view) {
        if(edtFromPrice.getText() == null || edtFromPrice.getText().toString().isEmpty()
                || edtToPrice.getText() == null || edtToPrice.getText().toString().isEmpty()) {
            return;
        }
        List<Drink> result = new ArrayList<>();
        for(Drink d : lst) {
            if(!d.isStatus()
                    && d.getPrice() <= Float.parseFloat(edtToPrice.getText().toString())
                    && d.getPrice() >= Float.parseFloat(edtFromPrice.getText().toString())) {
                result.add(d);
            }
        }
        adapter.setList(result);
        gvDrinks.setAdapter(adapter);
    }

    public void clickToViewAll(View view) {
        adapter.setList(lst);
        gvDrinks.setAdapter(adapter);
    }
}