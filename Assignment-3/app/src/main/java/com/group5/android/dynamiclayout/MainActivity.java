/*
 * Copyright (c)
 *  @Group 5
 *  Kshitij Shah - 801077782
 *  Parth Mehta - 801057625
 */

package com.group5.android.dynamiclayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.Serializable;


public class MainActivity extends AppCompatActivity {
    private Object avatarString;
    private Context context;
    private EditText firstName;
    private EditText lastName;
    private EditText studentId;
    private Button buttonSave;
    private RadioGroup departmentGroup;
    private ImageView avatarImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this.getApplicationContext();
        buttonSave = findViewById(R.id.buttonSave);
        firstName = findViewById(R.id.editTextFirstName);
        lastName = findViewById(R.id.editTextLastName);
        studentId = findViewById(R.id.editTextStudentId);
        departmentGroup = findViewById(R.id.radioGroupDepartment);

        final Intent i1 = new Intent(this, SelectAvatarActivity.class);
        avatarImage = findViewById(R.id.imageView);
        avatarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(i1, 1);
            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Bitmap bmap = ((BitmapDrawable) avatarImage.getDrawable()).getBitmap();
                Drawable myDrawable = getResources().getDrawable(R.drawable.select_image);
                final Bitmap myLogo = ((BitmapDrawable) myDrawable).getBitmap();

                if (bmap.sameAs(myLogo)) {
                    Toast.makeText(departmentGroup.getContext(), "Select a Profile Image", Toast.LENGTH_LONG).show();
                } else if (firstName.getText().toString() == null || firstName.getText().toString().matches("")) {
                    firstName.setError("Enter the First Name");
                } else if (lastName.getText().toString() == null || lastName.getText().toString().matches("")) {
                    lastName.setError("Enter the Last Name");
                } else if (studentId.getText().toString() == null || studentId.getText().toString().matches("")) {
                    studentId.setError("Enter the Student Id");
                } else if (departmentGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(departmentGroup.getContext(), "Select a radio button", Toast.LENGTH_LONG).show();
                } else {
                    int radioButtonID = departmentGroup.getCheckedRadioButtonId();
                    RadioButton radioButton = departmentGroup.findViewById(radioButtonID);
                    Log.d("Radio", "radioButton.getText().toString()" + radioButton.getText().toString());
                    User user = new User(firstName.getText().toString(), lastName.getText().toString(), Integer.parseInt(studentId.getText().toString()), avatarString, radioButton.getText().toString());
                    Intent i2 = new Intent(getApplicationContext(), DisplayActivity.class);
                    Log.d("wppp", user.getFirstName());
                    i2.putExtra("studentData", (Serializable) user);
                    startActivity(i2);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                avatarString = data.getExtras().get("result");
                ImageView r = findViewById(R.id.imageView);
                r.setImageDrawable(getDrawable((Integer) data.getExtras().get("result")));
                Log.d("result", data.getExtras().get("result").toString());
            }
        }
    }

}
