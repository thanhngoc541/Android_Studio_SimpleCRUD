package com.example.crud;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {
    private TextView txtID;
    private TextView txtName;
    private TextView txtPrice;
    private TextView txtStatus;
    private TextView txtTime;
    private Button btnUpdate;
    private Button btnCancel;
    private Drink drink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //initialize views
        txtID = findViewById(R.id.txtDetailID);
        txtName = findViewById(R.id.txtDetailName);
        txtPrice = findViewById(R.id.txtDetailPrice);
        txtStatus = findViewById(R.id.txtDetailStatus);
        txtTime = findViewById(R.id.txtDetailTime);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnCancel = findViewById(R.id.btnDetailCancel);

        //get selected drink from intent
        Intent intent = this.getIntent();
        drink = (Drink) intent.getSerializableExtra("drink");

        //assign value of drink to views
        txtID.setText("ID: " + String.valueOf(drink.getId()));
        txtName.setText("Name: " + drink.getName());
        txtPrice.setText("Price: " + String.valueOf(drink.getPrice()));
        if (drink.isStatus()) txtStatus.setText("Status: In Progress");
        else txtStatus.setText("Status: Finished");
        txtTime.setText("Time of Creation: " + drink.getTimeOfCreate());

        //set event for btnCancel
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailActivity.this.setResult(RESULT_CANCELED);
                finish();
            }
        });

        //set event for btnUpdate
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, CreateActivity.class);
                intent.putExtra("isUpdating", true);
                intent.putExtra("drink", drink);
                startActivityForResult(intent, 345);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 345) {
            if(resultCode == RESULT_OK) {
                Drink updated = (Drink) data.getSerializableExtra("updated");
                txtID.setText("ID: " + String.valueOf(updated.getId()));
                txtName.setText("Name: " + updated.getName());
                txtPrice.setText("Price: " + String.valueOf(updated.getPrice()));
                if (!updated.isStatus()) txtStatus.setText("Status: In Progress");
                else txtStatus.setText("Status: Finished");
                drink = updated;
                Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }
}