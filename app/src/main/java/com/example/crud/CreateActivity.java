package com.example.crud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateActivity extends AppCompatActivity {
    private EditText edtID;
    private EditText edtName;
    private EditText edtPrice;
    private CheckBox chkStatus;
    private Button btnSave;
    private DrinkDAO dao;
    private boolean isUpdating;
    private List<Drink> list;
    private Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        dao = new DrinkDAO();
        //initialize views
        edtID = findViewById(R.id.edtID);
        edtName = findViewById(R.id.edtName);
        edtPrice = findViewById(R.id.edtPrice);
        chkStatus = findViewById(R.id.chkStatus);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        Intent intent = getIntent();
        isUpdating = intent.getBooleanExtra("isUpdating", false);
        if (isUpdating) {
            edtID.setFocusable(false);
            Drink drink = (Drink) intent.getSerializableExtra("drink");
            edtID.setText(String.valueOf(drink.getId()));
            edtName.setText(drink.getName());
            edtPrice.setText(String.valueOf(drink.getPrice()));
            chkStatus.setSelected(drink.isStatus());
        }
        //get List from file
        try (FileInputStream fis = openFileInput("drinks.txt")) {
            list = dao.loadFromInternal(fis);
        } catch (Exception e) {
            e.printStackTrace();
            list = new ArrayList<>();
        }

        //set event for btnSave
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtID.getText() == null
                        || edtID.getText().toString().isEmpty()
                        || edtName.getText() == null
                        || edtName.getText().toString().isEmpty()
                        || edtPrice.getText() == null
                        || edtPrice.getText().toString().isEmpty()) {
                    Toast.makeText(CreateActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                int success = 0;
                Drink updatedDrink = null;
                //if isUpdating == false, create new drinks
                if (!isUpdating) {
                    success = addDrink(list);
                } else {
                    updatedDrink = updateDrink(list);
                    if (updatedDrink != null) {
                        success = 1;
                    }
                }
                if (success == 1) {
                    if (updatedDrink != null) {
                        Intent i = new Intent();
                        i.putExtra("updated", updatedDrink);
                        CreateActivity.this.setResult(RESULT_OK, i);
                    } else {
                        CreateActivity.this.setResult(RESULT_OK);
                    }
                    finish();
                } else {
                    CreateActivity.this.setResult(RESULT_CANCELED);
                    Toast.makeText(CreateActivity.this, "ID exists!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //set event for btnCancel
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateActivity.this.setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private int addDrink(List<Drink> lst) {
        int id = Integer.parseInt(edtID.getText().toString());
        for (Drink drink : lst) {
            if (drink.getId() == id) {
                Toast.makeText(CreateActivity.this, "ID exists!", Toast.LENGTH_SHORT).show();
                return 0;
            }
        }
        String name = edtName.getText().toString();
        float price = Float.parseFloat(edtPrice.getText().toString());
        boolean status = chkStatus.isChecked();
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        Drink drink = new Drink(id, name, price, status, formatter.format(date));
        lst.add(drink);
        try {
            FileOutputStream fos = openFileOutput("drinks.txt", MODE_PRIVATE);
            dao.saveToInternal(fos, lst);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    private Drink updateDrink(List<Drink> lst) {
        int id = Integer.parseInt(edtID.getText().toString());
        String name = edtName.getText().toString();
        float price = Float.parseFloat(edtPrice.getText().toString());
        boolean status = chkStatus.isChecked();
        Drink updated = null;
        for (Drink drink : lst) {
            if (drink.getId() == id) {
                drink.setName(name);
                drink.setPrice(price);
                drink.setStatus(status);
                updated = drink;
                break;
            }
        }
        try {
            FileOutputStream fos = openFileOutput("drinks.txt", MODE_PRIVATE);
            dao.saveToInternal(fos, lst);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return updated;
    }
}