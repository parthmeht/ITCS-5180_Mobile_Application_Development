package com.groupr4.android.inclassassignment6;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Messages extends AppCompatActivity implements TheardOperations {

    TextView userName;
    EditText newThread;
    ImageButton logOff, add, delete;
    String user_name, token;
    ListView lv;
    ThreadAdapter threadAdapter;
    Threads t;
    ArrayList<Threads> result = null;
    public static String ChatRoomThread_Key = "thread";
    private final OkHttpClient client = new OkHttpClient();
    private User user;
    public static AlertDialog.Builder builder;
    public static AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        Log.d("Messages", "Inside Messages");

        userName = (TextView) findViewById(R.id.textView);
        newThread = (EditText) findViewById(R.id.editText7);
        logOff = (ImageButton) findViewById(R.id.imageButton3);
        add = (ImageButton) findViewById(R.id.imageButton2);
        lv = (ListView) findViewById(R.id.myListView);
        setTitle("Message Threads");
        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setTitle("Loading").setView(inflater.inflate(R.layout.dialog_bar, null));
        dialog = builder.create();
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (getIntent() != null) {
            String userString = preferences.getString(MainActivity.user_key, null);
            Gson gson = new Gson();
            user = gson.fromJson(userString, User.class);
            //user = (User) getIntent().getExtras().getSerializable(MainActivity.user_key);
            user_name = user.firstName + " " + user.lastName;
            userName.setText(user_name);
            token = preferences.getString(MainActivity.token_key, null);
            if (!token.equalsIgnoreCase(""))
                getThreads();
            else {
                finish();
                Intent intent_logOff = new Intent(Messages.this, MainActivity.class);
                startActivity(intent_logOff);
            }
        }
        logOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                token = "";
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(MainActivity.token_key, null);
                editor.putString(MainActivity.user_key, null);
                editor.commit();
                Intent intent_logOff = new Intent(Messages.this, MainActivity.class);
                startActivity(intent_logOff);
                finish();
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = newThread.getText().toString();
                if (!(text.equals("") || text.equals(null))) {
                    dialog.show();
                    t = new Threads();
                    OkHttpClient client = new OkHttpClient();
                    RequestBody formBody = new FormBody.Builder()
                            .add("title", text)
                            .build();
                    Request request = new Request.Builder()
                            .header("Authorization", "BEARER " + token)
                            .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/thread/add")
                            .post(formBody)
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            try (ResponseBody responseBody = response.body()) {
                                if (!response.isSuccessful())
                                    throw new IOException("Unexpected code " + response);
                                JSONObject root = new JSONObject(response.body().string());
                                String status = root.getString("status");
                                if (status.equals("ok")) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            newThread.setText("");
                                        }
                                    });
                                    getThreads();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    });

                }
                else
                {
                    Toast.makeText(Messages.this, "Enter a valid thread title", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        dialog.hide();
    }

    private void setAdapter(ArrayList<Threads> s) {
        MainActivity.dialog.hide();
        if (SignUp.dialog != null)
            SignUp.dialog.hide();
        if (ChatRoomActivity.dialog != null)
            ChatRoomActivity.dialog.hide();
        dialog.hide();
        lv = (ListView) findViewById(R.id.myListView);
        threadAdapter = new ThreadAdapter(user, s, this, this);
        lv.setAdapter(threadAdapter);
    }

    private void getThreads() {
        Request request = new Request.Builder()
                .header("Authorization", "BEARER " + token)
                .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/thread")
                .build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);
                    JSONObject root = new JSONObject(responseBody.string());
                    String status = root.getString("status");
                    if (status.equalsIgnoreCase("ok")) {
                        result = new ArrayList<>();
                        JSONArray source = root.getJSONArray("threads");
                        for (int i = source.length() - 1; i >= 0; i--) {
                            JSONObject sourceJSON = source.getJSONObject(i);
                            Threads threads = new Threads();
                            threads.user_fname = sourceJSON.getString("user_fname");
                            threads.user_lname = sourceJSON.getString("user_lname");
                            threads.user_id = sourceJSON.getInt("user_id");
                            threads.id = sourceJSON.getInt("id");
                            threads.title = sourceJSON.getString("title");
                            threads.created_at = sourceJSON.getString("created_at");
                            result.add(threads);
                        }

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                setAdapter(result);
                                Log.d("addapDemo", "main");
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    @Override
    public void deleteThreads(int id) {
        result.remove(id);
        getThreads();
    }

    @Override
    public void gotoChatroom(int id) {
        Threads selected_thread = (Threads) threadAdapter.getItem(id);
        Intent int_msg = new Intent(Messages.this, ChatRoomActivity.class);
        Bundle bnd = new Bundle();
        bnd.putSerializable(Messages.ChatRoomThread_Key, selected_thread);
        int_msg.putExtras(bnd);
        startActivity(int_msg);
        dialog.show();
    }
}
