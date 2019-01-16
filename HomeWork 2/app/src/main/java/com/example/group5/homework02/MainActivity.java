package com.example.group5.homework02;
/*
 * Copyright (c)
 * @Group 5
 * Kshitij Shah - 801077782
 * Parth Mehta - 801057625
 */


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static int i = 0;

    final private static double PI = 3.1416;
    final private static double HALF = 0.5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        final TextView inputL2 = findViewById(R.id.lengthText2);
        final TextView shape = findViewById(R.id.shape);
        final TextView answer = findViewById(R.id.answer);
        final EditText length1 = findViewById(R.id.length1);
        final EditText length2 = findViewById(R.id.length2);
        final ImageButton triangle = findViewById(R.id.triangleButton);
        final ImageButton circle = findViewById(R.id.circleButton);
        final ImageButton square = findViewById(R.id.squareButton);
        final Button calculate = findViewById(R.id.calculate);
        final Button clear = findViewById(R.id.clear);

        SpannableString ans = new SpannableString("                                           ");
        ans.setSpan(new UnderlineSpan(), 0, ans.length(), 0);
        answer.setText(ans);

        triangle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shape.setText("Triangle");

                length2.setVisibility(View.VISIBLE);

                if (!inputL2.isShown())
                    inputL2.setVisibility(View.VISIBLE);
                length2.setFocusable(true);
                answer.setText("");
                i = 1;
                SpannableString ans = new SpannableString("                                           ");
                ans.setSpan(new UnderlineSpan(), 0, ans.length(), 0);
                answer.setText(ans);
            }
        });

        circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shape.setText("Circle");
                length2.setVisibility(View.INVISIBLE);
                if (inputL2.isShown())
                    inputL2.setVisibility(View.GONE);
                answer.setText("");
                i = 2;
                SpannableString ans = new SpannableString("                                           ");
                ans.setSpan(new UnderlineSpan(), 0, ans.length(), 0);
                answer.setText(ans);

            }
        });
        square.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shape.setText("Square");
                length2.setVisibility(View.INVISIBLE);

                if (inputL2.isShown())
                    inputL2.setVisibility(View.GONE);
                answer.setText("");
                i = 3;
                SpannableString ans = new SpannableString("                                           ");
                ans.setSpan(new UnderlineSpan(), 0, ans.length(), 0);
                answer.setText(ans);
            }
        });

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (i) {
                    case 0:
                        Toast.makeText(getApplicationContext(), "Please select a shape", Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        if (length1.getText().toString().matches("") | length2.getText().toString().matches(""))
                            Toast.makeText(getApplicationContext(), "Enter Valid Length", Toast.LENGTH_LONG).show();
                        else
                            triangleArea();
                        break;
                    case 2:
                        if (length1.getText().toString().matches(""))
                            Toast.makeText(getApplicationContext(), "Enter Valid Length", Toast.LENGTH_LONG).show();
                        else
                            circleArea();
                        break;
                    case 3:
                        if (length1.getText().toString().matches(""))
                            Toast.makeText(getApplicationContext(), "Enter Valid Length", Toast.LENGTH_LONG).show();
                        else
                            squareArea();
                        break;

                }

            }

            private void triangleArea() {
                SpannableString ans = new SpannableString("             " + String.format("%.2f", Double.parseDouble(length1.getText().toString()) * Double.parseDouble(length2.getText().toString()) * HALF) + "             ");
                ans.setSpan(new UnderlineSpan(), 0, ans.length(), 0);
                answer.setText(ans);

            }

            private void circleArea() {
                SpannableString ans = new SpannableString("             " + String.format("%.2f", Double.parseDouble(length1.getText().toString()) * Double.parseDouble(length1.getText().toString()) * PI) + "             ");
                ans.setSpan(new UnderlineSpan(), 0, ans.length(), 0);
                answer.setText(ans);

            }

            private void squareArea() {
                SpannableString ans = new SpannableString("             " + String.format("%.2f", Double.parseDouble(length1.getText().toString()) * Double.parseDouble(length1.getText().toString())) + "             ");
                ans.setSpan(new UnderlineSpan(), 0, ans.length(), 0);
                answer.setText(ans);
            }

        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                length1.setText("");
                length2.setText("");
                if (!inputL2.isShown())
                    inputL2.setVisibility(View.VISIBLE);
                length2.setVisibility(View.VISIBLE);
                SpannableString ans = new SpannableString("                                           ");
                ans.setSpan(new UnderlineSpan(), 0, ans.length(), 0);
                answer.setText(ans);
                shape.setText(R.string.appStartText);
                i = 0;
            }
        });

    }


}
