/*
 * Copyright (c)
 * @Group 5
 * Kshitij Shah - 801077782
 * Parth Mehta - 801057625
 */

package com.group5.android.baccalculator;

import android.util.Log;

public class Drinks {
    private int drinkSize;
    private int alcoholPercent;
    private int alcoholConsumed;

    public Drinks(int drinkSize, int alcoholPercent) {
        this.drinkSize = drinkSize;
        this.alcoholPercent = alcoholPercent+5;
        this.alcoholConsumed = drinkSize * (alcoholPercent+5);
        Log.d("drinks", "Drinks{" +
                "drinkSize=" + drinkSize +
                ", alcoholPercent=" + alcoholPercent +
                ", alcoholConsumed=" + this.alcoholConsumed +
                '}');
    }

    public int getDrinkSize() {
        return drinkSize;
    }

    public void setDrinkSize(int drinkSize) {
        this.drinkSize = drinkSize;
    }

    public int getAlcoholPercent() {
        return alcoholPercent;
    }

    public void setAlcoholPercent(int alcoholPercent) {
        this.alcoholPercent = alcoholPercent;
    }

    public int getAlcoholConsumed() {
        return alcoholConsumed;
    }

    public void setAlcoholConsumed(int alcoholConsumed) {
        this.alcoholConsumed = alcoholConsumed;
    }

    @Override
    public String toString() {
        return "Drinks{" +
                "drinkSize=" + drinkSize +
                ", alcoholPercent=" + alcoholPercent +
                ", alcoholConsumed=" + alcoholConsumed +
                '}';
    }
}
