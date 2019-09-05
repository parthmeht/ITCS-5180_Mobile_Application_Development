/*
 * Copyright (c)
 *  @Group 5
 *  Kshitij Shah - 801077782
 *  Parth Mehta - 801057625
 */

package com.group5.android.dynamiclayout;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class SelectAvatarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_avatar);
        final Intent returnIntent = new Intent();
        final ImageButton b1 = findViewById(R.id.avatar1);

        final ImageButton b2 = findViewById(R.id.avatar2);
        final ImageButton b3 = findViewById(R.id.avatar3);
        final ImageButton b4 = findViewById(R.id.avatar4);
        final ImageButton b5 = findViewById(R.id.avatar5);
        final ImageButton b6 = findViewById(R.id.avatar6);

        setResult(Activity.RESULT_OK, returnIntent);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Hola", String.valueOf(b1.getId()));
                returnIntent.putExtra("result", R.drawable.avatar_f_1);
                finish();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnIntent.putExtra("result", R.drawable.avatar_f_2);
                finish();
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnIntent.putExtra("result", R.drawable.avatar_f_3);
                finish();
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnIntent.putExtra("result", R.drawable.avatar_m_1);
                finish();
            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                returnIntent.putExtra("result",String.valueOf(b5.getDrawable()));
                returnIntent.putExtra("result", R.drawable.avatar_m_2);
                finish();
            }
        });
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnIntent.putExtra("result", R.drawable.avatar_m_3);
                finish();
            }
        });


    }
}
