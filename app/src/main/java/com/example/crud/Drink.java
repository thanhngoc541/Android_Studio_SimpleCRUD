package com.example.crud;

import java.io.Serializable;

public class Drink implements Serializable
{
    private int Id;
    private String Name;
    private float Price;
    private boolean Status;
    private String TimeOfCreate;

    public Drink(int id, String name, float price, boolean status, String timeOfCreate) {
        Id = id;
        Name = name;
        Price = price;
        Status = status;
        TimeOfCreate = timeOfCreate;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public float getPrice() {
        return Price;
    }

    public void setPrice(float price) {
        Price = price;
    }

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }

    public String getTimeOfCreate() {
        return TimeOfCreate;
    }

    public void setTimeOfCreate(String timeOfCreate) {
        TimeOfCreate = timeOfCreate;
    }

    @Override
    public String toString() {
        return Id+"-"+Name+"-"+Price+"-"+Status+"-"+TimeOfCreate;
    }
}
