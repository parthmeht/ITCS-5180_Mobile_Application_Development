
/*
 * Copyright (c)
 * @Group 5
 * Kshitij Shah - 801077782
 * Parth Mehta - 801057625
 */

package com.group5.android.baccalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static Double weight;
    android.support.v7.widget.Toolbar toolbar;
    public static String gender;
    static final Double MEN = 0.68;
    static final Double WOMEN = 0.55;
    public static ArrayList<Drinks> drinksList;
    private EditText editTextWeight;
    private Switch genderSwitch;
    private Button buttonSave;
    private SeekBar alcoholPercentageSlider;
    private TextView alcoholPercentValue;
    private Button buttonAddDrink;
    private RadioGroup drinkSizeRadioGroup;
    private TextView bacLevelValue;
    private ProgressBar bacLevelProgressBar;
    private TextView statusValue;
    private Button buttonReset;
    private RadioButton radioButton1oz;
    public static Double totalBac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drinksList = new ArrayList<Drinks>();
        editTextWeight = findViewById(R.id.editTextWeight);
        genderSwitch = findViewById(R.id.genderSwitch);
        buttonSave = findViewById(R.id.buttonSave);
        alcoholPercentageSlider = findViewById(R.id.alcoholPercentageSlider);
        alcoholPercentValue = findViewById(R.id.alcoholPercentValue);
        buttonAddDrink = findViewById(R.id.buttonAddDrink);
        drinkSizeRadioGroup = findViewById(R.id.drinkSizeRadioGroup);
        bacLevelValue = findViewById(R.id.bacLevelValue);
        bacLevelProgressBar = findViewById(R.id.bacLevelProgressBar);
        statusValue = findViewById(R.id.statusValue);
        buttonReset = findViewById(R.id.buttonReset);
        radioButton1oz = findViewById(R.id.radioButton1oz);
        totalBac = 0.00;
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextWeight.getText().toString().matches("") || Double.parseDouble(editTextWeight.getText().toString()) <= 0) {
                    Toast.makeText(v.getContext(), "Invalid Input", Toast.LENGTH_LONG).show();
                } else {
                    weight = Double.parseDouble(editTextWeight.getText().toString());
                    if (genderSwitch.isChecked()) {
                        gender = genderSwitch.getTextOn().toString();
                    } else {
                        gender = genderSwitch.getTextOff().toString();
                    }
                    recalculateTotalBAC(drinksList, weight, gender);
                    editTextWeight.setError(null);
                }
            }
        });

        alcoholPercentageSlider.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("bac", "Progress: " + (progress+5) + "%");
                alcoholPercentValue.setText((progress+5)+ "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        buttonAddDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (weight == null || weight == 0) {
                    editTextWeight.setError("Enter the weight in lb.");
                } else {
                    int radioButtonID = drinkSizeRadioGroup.getCheckedRadioButtonId();
                    RadioButton radioButton = drinkSizeRadioGroup.findViewById(radioButtonID);
                    //Integer alcoholPercent = Integer.parseInt(alcoholPercentValue.getText().toString().replaceAll("[^\\d.]", ""));
                    Integer alcoholPercent = alcoholPercentageSlider.getProgress();
                    Integer drinkSize = Integer.parseInt(radioButton.getText().toString().replaceAll("[^\\d.]", ""));
                    Log.d("bac", "alcohol percent value : " + alcoholPercent);
                    Log.d("bac", "drink size : " + drinkSize);
                    Drinks drink = new Drinks(drinkSize, alcoholPercent);
                    drinksList.add(drink);
                    calculateBAC(drink, weight, gender);
                }
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSave.setEnabled(true);
                buttonAddDrink.setEnabled(true);
                editTextWeight.setEnabled(true);
                editTextWeight.setText("");
                weight=0.0;
                gender="";
                genderSwitch.setChecked(true);
                bacLevelProgressBar.setProgress(0);
                alcoholPercentageSlider.setProgress(0);
                radioButton1oz.setChecked(true);
                drinksList = new ArrayList<Drinks>();
                recalculateTotalBAC(drinksList,weight,gender);
            }
        });

    }

    public Double bacCalculation(Drinks drink, Double weight, String gender){
        Double bac;
        if (gender.equalsIgnoreCase("M")) {
            bac = ((drink.getAlcoholConsumed() * 6.24) / (weight * MEN)) * 0.01;
        } else if (gender.equalsIgnoreCase("F")) {
            bac = ((drink.getAlcoholConsumed() * 6.24) / (weight * WOMEN)) * 0.01;
        } else {
            bac = 0.00;
        }
        return bac;
    }

    public void calculateBAC(Drinks drink, Double weight, String gender) {
        Double bac = bacCalculation(drink,weight,gender);
        Log.d("calculateBAC", "bac : " + bac);
        totalBac = totalBac + bac;
        Log.d("calculateBAC", "total bac : " + totalBac);
        bacLevelValue.setText(String.format("%.2f",totalBac));
        int progress = (int) ((totalBac / 0.25) * 100);
        bacLevelProgressBar.setProgress(progress);
        decideStatus();
    }

    public void recalculateTotalBAC(ArrayList<Drinks> drinksList, Double weight, String gender) {
        Double bac;
        totalBac = 0.00;
        if (drinksList != null && drinksList.size() != 0) {
            for (Drinks d : drinksList) {
                bac = bacCalculation(d,weight,gender);
                Log.d("calculateBAC", "bac : " + bac);
                totalBac = totalBac + bac;
            }
            Log.d("calculateBAC", "total bac : " + totalBac);
            bacLevelValue.setText(String.format("%.2f",totalBac));
            int progress = (int) ((totalBac / 0.25) * 100);
            Log.d("calculateBAC", "progress : " + progress);
            bacLevelProgressBar.setProgress(progress);
            decideStatus();
        } else {
            bacLevelValue.setText("0.00");
            alcoholPercentageSlider.setProgress(0);
            decideStatus();
        }
    }

    public void decideStatus() {
        if (totalBac >= 0 && totalBac <= 0.08) {
            statusValue.setText(R.string.statusValueSafe);
            statusValue.setBackgroundResource(R.drawable.green_rounded_corner);
        } else if (totalBac > 0.08 && totalBac < 0.2) {
            statusValue.setText(R.string.statusValueCareful);
            statusValue.setBackgroundResource(R.drawable.orange_rounded_corner);
        } else {
            statusValue.setText(R.string.statusValueOver);
            statusValue.setBackgroundResource(R.drawable.red_rounded_corner);
            if (totalBac>0.25){
                buttonSave.setEnabled(false);
                buttonAddDrink.setEnabled(false);
                editTextWeight.setEnabled(false);
                Toast.makeText(buttonAddDrink.getContext(),"No more drinks for you.",Toast.LENGTH_LONG).show();
            }
        }
    }
}
