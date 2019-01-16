package com.group5.android.hw05;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GetNewsBuzzAPI.UrlData {

    private final String API_KEY = "1442c0e4d97c4bceaa07a5e2cc3a79c9";
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private ArrayList<Source> sourceList;
    private ListView listView;
    private ArrayAdapter<Source> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Main Activity");
        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setTitle("Loading Sources..").setView(inflater.inflate(R.layout.dialog_bar, null));
        dialog = builder.create();
        listView = findViewById(R.id.listView);
        if (isConnected()) {
            dialog.show();
            RequestParams params = new RequestParams();
            params.addParameter("apiKey", API_KEY);
            new GetNewsBuzzAPI(MainActivity.this, params).execute("https://newsapi.org/v2/sources");
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection!", Toast.LENGTH_LONG).show();
        }

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (isConnected()) {
//                    Source source = adapter.getItem(position);
//                    Log.d("Selected", "Selected source : " + position + " value : " + source);
//                } else {
//                    Toast.makeText(getApplicationContext(), "No Internet Connection!", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
    }

    public boolean isConnected() {
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
    public void urlHandleData(ArrayList<Source> data) {
        if (data != null && data.size() > 0) {
            sourceList = data;
            dialog.dismiss();
            adapter = new ArrayAdapter<Source>(this, android.R.layout.simple_list_item_1, android.R.id.text1, sourceList);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (isConnected()){
                        Log.d("Hey", sourceList.get(position).getId());
                        RequestParams params1 = new RequestParams();
                        Bundle bundle = new Bundle();
                        bundle.putString("apiKey", API_KEY);
                        bundle.putString("sources", sourceList.get(position).getId());
                        bundle.putString("name", sourceList.get(position).getName());
                        Intent i = new Intent(getApplicationContext(), ShowNews.class);
                        i.putExtras(bundle);
                        startActivity(i);
                    }
                    else
                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();

                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Sources Found", Toast.LENGTH_LONG).show();
        }
    }
}
