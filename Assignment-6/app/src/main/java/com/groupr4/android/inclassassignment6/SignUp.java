package com.groupr4.android.inclassassignment6;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class SignUp extends AppCompatActivity {

    private EditText firstNameEdit;
    private EditText lastNameEdit;
    private EditText emailEdit;
    private EditText passwordEdit;
    private EditText confirmPasswordEdit;
    private Button cancelButton;
    private Button signUpButton;
    private final OkHttpClient client = new OkHttpClient();
    private User user;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firstNameEdit = findViewById(R.id.editTextFirstName);
        lastNameEdit = findViewById(R.id.editTextLastName);
        emailEdit = findViewById(R.id.editTextEmail);
        passwordEdit = findViewById(R.id.editTextPassword);
        confirmPasswordEdit = findViewById(R.id.editTextConfirmPassword);
        cancelButton = findViewById(R.id.buttonCancel);
        signUpButton = findViewById(R.id.buttonSignUp);
        setTitle("Sign Up");

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()){
                    if (firstNameEdit.getText() == null || firstNameEdit.getText().toString().equalsIgnoreCase("")){
                        firstNameEdit.setError("Enter First Name");
                    }
                    else if (lastNameEdit.getText() == null || lastNameEdit.getText().toString().equalsIgnoreCase("")){
                        lastNameEdit.setError("Enter Last Name");
                    }
                    else if (emailEdit.getText() == null || emailEdit.getText().toString().equalsIgnoreCase("")){
                        emailEdit.setError("Enter Email Id");
                    }
                    else if (passwordEdit.getText() == null || passwordEdit.getText().toString().equalsIgnoreCase("")){
                        passwordEdit.setError("Enter Password");
                    }
                    else if (confirmPasswordEdit.getText() == null || confirmPasswordEdit.getText().toString().equalsIgnoreCase("")){
                        confirmPasswordEdit.setError("Enter Confirm Password");
                    }
                    else if (passwordEdit.getText() != null
                            && !passwordEdit.getText().toString().equalsIgnoreCase("")
                            && confirmPasswordEdit.getText() != null
                            && !confirmPasswordEdit.getText().toString().equalsIgnoreCase("")
                            && !passwordEdit.getText().toString().equalsIgnoreCase(confirmPasswordEdit.getText().toString())){
                        confirmPasswordEdit.setError("Password and Confirm Password does not match");
                    }
                    else if(passwordEdit.getText().length()<6 || confirmPasswordEdit.getText().length()<6)
                    {
                        passwordEdit.setError("Password has to be 6 or more than 6 characters");
                    }
                    else {
                        user = new User(firstNameEdit.getText().toString(),lastNameEdit.getText().toString(),emailEdit.getText().toString(),passwordEdit.getText().toString());
                        Log.d("User",user.toString());
                        MainActivity.dialog.show();
                        signUp();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "No Internet Connection!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void signUp(){

        if (user!=null){
            RequestBody formBody = new FormBody.Builder()
                    .add("email", user.email)
                    .add("password", user.password)
                    .add("fname",user.firstName)
                    .add("lname",user.lastName)
                    .build();
            Request request = new Request.Builder()
                    .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/signup")
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try (ResponseBody responseBody = response.body()) {
                        if (!response.isSuccessful()){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(SignUp.this, "Invalid Inputs", Toast.LENGTH_LONG).show();
                                }
                            });
                            throw new IOException("Unexpected code " + response);
                        }
                        JSONObject root = new JSONObject(response.body().string());
                        String status = root.getString("status");
                        if (status.equalsIgnoreCase("ok")){
                            token = root.getString("token");
                            user.userId = root.getInt("user_id");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("Token",token);
                                    editor.apply();
                                    Toast.makeText(SignUp.this, "User Created Successfully", Toast.LENGTH_LONG).show();
                                }
                            });
                            Intent int_login = new Intent(SignUp.this, Messages.class);
                            Bundle bnd = new Bundle();
                            bnd.putSerializable(MainActivity.user_key, user);
                            //bnd.putString(MainActivity.token_key, token);
                            int_login.putExtras(bnd);
                            startActivity(int_login);
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }
}
