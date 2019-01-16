/*
 * Copyright (c)
 *  @Group 5
 *  Kshitij Shah - 801077782
 *  Parth Mehta - 801057625
 */

package com.group5.android.dynamiclayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplayActivity extends AppCompatActivity {

    private TextView name;
    private Button buttonEdit;
    private TextView studentId;
    private TextView department;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        name = findViewById(R.id.textViewNameValue);
        buttonEdit = findViewById(R.id.buttonEdit);
        studentId = findViewById(R.id.textViewStudentIdValue);
        department = findViewById(R.id.textViewDepatmentValue);
        ImageView avatarImage = findViewById(R.id.imageViewDisplay);
        User user = (User) getIntent().getExtras().get("studentData");
        name.setText(user.getFirstName() + " " +user.getLastName());
        avatarImage.setImageDrawable(getDrawable((Integer) user.getAvatar()));
        studentId.setText(user.getStudentId().toString());
        department.setText(user.getDepartment());


        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Log.d("AVATAR", String.valueOf(user.getAvatar()));
    }
}
