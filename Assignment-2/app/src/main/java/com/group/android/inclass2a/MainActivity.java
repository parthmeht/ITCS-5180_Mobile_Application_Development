/*
    @Group 5
    Kshitij Shah - 801077782 & Parth Mehta - 801057625
    Extra 5 points for constraint view presentation
 */

package com.group.android.inclass2a;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.text.DecimalFormat;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private EditText weightText;
    private EditText feetText;
    private EditText inchesText;
    private Button calculateBMI;
    private TextView bmiResult;
    private TextView bmiStatus;
    private static DecimalFormat df2 = new DecimalFormat(".##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weightText = findViewById(R.id.weightTextField);
        feetText = findViewById(R.id.feetTextField);
        calculateBMI = findViewById(R.id.calculateBMIButton);
        inchesText = findViewById(R.id.inchesTextField);
        bmiResult = findViewById(R.id.bmiResult);
        bmiStatus = findViewById(R.id.bmiStatus);
        calculateBMI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inchesText.getText().toString().matches("")) {
                    inchesText.setText("0");
                }
                if (weightText.getText().toString().matches("") || feetText.getText().toString().matches("")) {
                    Toast.makeText(v.getContext(), "Invalid Input", Toast.LENGTH_LONG).show();
                    bmiResult.setText("");
                    bmiStatus.setText("");
                } else if (Float.parseFloat(weightText.getText().toString()) <= 1 || Float.parseFloat(feetText.getText().toString()) <= 0 || Float.parseFloat(weightText.getText().toString()) < 1) {
                    Toast.makeText(v.getContext(), "Invalid Input", Toast.LENGTH_LONG).show();
                    bmiResult.setText("");
                    bmiStatus.setText("");
                } else if (Float.parseFloat(inchesText.getText().toString()) > 12) {
                    Toast.makeText(v.getContext(), "Invalid Height", Toast.LENGTH_LONG).show();
                    bmiResult.setText("");
                    bmiStatus.setText("");
                } else {
                    Float height = Float.parseFloat(feetText.getText().toString()) * 12 + Float.parseFloat(inchesText.getText().toString());
                    Float bmi = (Float.parseFloat(weightText.getText().toString()) / (height * height)) * 703;
                    bmiResult.setText("Your BMI :    " + df2.format(bmi));
                    if (bmi <= 18.5)
                        bmiStatus.setText("Your BMI Status: Underweight");
                    else if (bmi <= 24.9)
                        bmiStatus.setText("Your BMI Status: Normal Weight");
                    else if (bmi <= 29.9)
                        bmiStatus.setText("Your BMI Status: Over weight");
                    else
                        bmiStatus.setText("Your BMI Status: Obese");
                }
            }
        });
    }
}
