package com.example.group5.moviedatabaseapplication;

import android.content.Context;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.Toast;

public class EditTextValidation {
    EditTextValidation(EditText editText, String message) {
        if (editText.getText().toString().matches(""))
            editText.setError(message);
    }

    EditTextValidation(EditText editText, int value, String message, String toast, Context context) {
        if (editText.getText().toString().length() > value) {
            editText.setError(message);
            Toast.makeText(context, toast, Toast.LENGTH_LONG).show();
        }

    }

    EditTextValidation(String message, EditText editText, String toast, Context context) {
        if (!URLUtil.isValidUrl(editText.getText().toString())) {
            editText.setError(message);
            Toast.makeText(context, toast, Toast.LENGTH_LONG).show();

        }
    }

    public EditTextValidation(EditText year, String m1, String m2, int i1, int i2, Context context) {

        if (Integer.parseInt(year.getText().toString()) < i1) {
            year.setError(m1);
            Toast.makeText(context, m1, Toast.LENGTH_LONG).show();
        } else if (Integer.parseInt(year.getText().toString()) > i2) {
            year.setError(m2);
            Toast.makeText(context, m2, Toast.LENGTH_LONG).show();
        }


    }
}
