/*
 * Copyright (c)
 * Parth Mehta
 * 801057625
 */

package com.parth.android.expensemanager;

import java.io.Serializable;

public class MonthlyBudget implements Serializable {
    float budget, spent;

    public MonthlyBudget(){

    }

    public MonthlyBudget(float budget) {
        this.budget = budget;
    }

    public float getBudget() {
        return budget;
    }

    public void setBudget(float budget) {
        this.budget = budget;
    }

    public float getSpent() {
        return spent;
    }

    public void setSpent(float spent) {
        this.spent = spent;
    }

    @Override
    public String toString() {
        return "MonthlyBudget{" +
                "budget=" + budget +
                ", spent=" + spent +
                '}';
    }
}
