/*
 * Copyright (c)
 * Parth Mehta
 * 801057625
 */

package com.parth.android.expensemanager;

public class Expense {
    String id,name;
    float amount;

    public Expense(){

    }

    public Expense(String id, String name, float amount) {
        this.id = id;
        this.name = name;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                '}';
    }
}
