package com.groupr4.android.inclassassignment6;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ChatRoomActivity extends AppCompatActivity implements ChatOperations {

    private Threads current_thread;
    private ArrayList<Msg> msg_list;
    private OkHttpClient client;
    private ListView msg_listview;
    private String token;
    private TextView ThreadName;
    private ImageButton addMessageButton;
    private User user;
    private EditText editNewMessage;
    MsgAdapter adapter;
    public static AlertDialog.Builder builder;
    public static AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        addMessageButton = findViewById(R.id.addMessageButton);
        editNewMessage = findViewById(R.id.editNewMessage);
        setTitle("Chatroom");
        client = new OkHttpClient();
        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setTitle("Loading").setView(inflater.inflate(R.layout.dialog_bar, null));
        dialog = builder.create();
        if (getIntent() != null && getIntent().getExtras() != null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            //user = (User) getIntent().getExtras().getSerializable(MainActivity.user_key);
            String userString = preferences.getString(MainActivity.user_key, null);
            Gson gson = new Gson();
            user = gson.fromJson(userString, User.class);
            current_thread = (Threads) getIntent().getExtras().getSerializable(Messages.ChatRoomThread_Key);
            token = preferences.getString(MainActivity.token_key, null);
            if (token != null) {
                getMessages();
            }
        }

        findViewById(R.id.homeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                Intent intHome = new Intent(ChatRoomActivity.this, Messages.class);
                /*Bundle bnd = new Bundle();
                bnd.putSerializable(MainActivity.user_key, user);
                intHome.putExtras(bnd);*/
                startActivity(intHome);
            }
        });

        addMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()) {
                    String message;

                    if (editNewMessage.getText().toString() == null || editNewMessage.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(getApplicationContext(), "Enter a message", Toast.LENGTH_LONG).show();
                    } else {
                        dialog.show();
                        message = editNewMessage.getText().toString();
                        //dialog.show();
                        addMessage(message);
                        editNewMessage.setText("");
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection!", Toast.LENGTH_LONG).show();
                }
            }
        });


    }


    public void getMessages() {
        Request request = new Request.Builder()
                .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/messages/" + current_thread.id)
                .header("Authorization", "BEARER " + token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);
                    JSONObject root = new JSONObject(response.body().string().toString());
                    JSONArray msg_arr = root.getJSONArray("messages");
                    msg_list = new ArrayList<>();
                    for (int i = msg_arr.length() - 1; i >= 0; i--) {
                        JSONObject msg_json_obj = msg_arr.getJSONObject(i);
                        Msg current_msg = new Msg();
                        current_msg.user_fname = msg_json_obj.getString("user_fname");
                        current_msg.user_lname = msg_json_obj.getString("user_lname");
                        current_msg.msg_id = msg_json_obj.getString("id");
                        current_msg.user_id = msg_json_obj.getString("user_id");
                        current_msg.msgContent = msg_json_obj.getString("message");
                        current_msg.createdAt = msg_json_obj.getString("created_at");
                        msg_list.add(current_msg);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setListView();

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void addMessage(String message) {
        RequestBody formBody = new FormBody.Builder()
                .add("message", message)
                .add("thread_id", String.valueOf(current_thread.id))
                .build();
        Request request = new Request.Builder()
                .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/message/add")
                .header("Authorization", "BEARER " + token)
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
                    if (status.equalsIgnoreCase("ok")) {
                        getMessages();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setListView() {
        ThreadName = (TextView) findViewById(R.id.txtThreadName);
        ThreadName.setText(current_thread.title);
        msg_listview = (ListView) findViewById(R.id.listMessages);
        adapter = new MsgAdapter(user, msg_list, this, this);
        msg_listview.setAdapter(adapter);
        dialog.hide();
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

    @Override
    public void deleteChat(int id) {
        msg_list.remove(id);
        getMessages();
    }
}

