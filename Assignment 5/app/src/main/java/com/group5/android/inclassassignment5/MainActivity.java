package com.group5.android.inclassassignment5;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements GetNewsBuzzAPI.UrlData {

    private final String API_KEY = "1442c0e4d97c4bceaa07a5e2cc3a79c9";
    private TextView publishedOnValue;
    private TextView headline;
    private ImageView imageView;
    private EditText description;
    private ArrayList<News> news;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        publishedOnValue = findViewById(R.id.textViewPublishedOnValue);
        description = findViewById(R.id.editText);
        headline = findViewById(R.id.textViewHeadline);
        imageView = findViewById(R.id.imageView);
        headline.setVisibility(View.INVISIBLE);
        findViewById(R.id.textViewPublishedOn).setVisibility(View.INVISIBLE);
        publishedOnValue.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.INVISIBLE);
        findViewById(R.id.textViewDescription).setVisibility(View.INVISIBLE);
        description.setVisibility(View.INVISIBLE);
        findViewById(R.id.imageButtonPrevious).setVisibility(View.INVISIBLE);
        findViewById(R.id.imageButtonNext).setVisibility(View.INVISIBLE);
        findViewById(R.id.buttonQuit).setVisibility(View.INVISIBLE);
        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setTitle("Loading").setView(inflater.inflate(R.layout.dialog_bar, null));
        dialog = builder.create();
        if (isConnected()) {
            dialog.show();
            description.setKeyListener(null);
            description.setFocusable(false);
            description.setCursorVisible(false);
            RequestParams params = new RequestParams();
            params.addParameter("sources", "buzzfeed").addParameter("apiKey", API_KEY);
            new GetNewsBuzzAPI(MainActivity.this, params).execute("https://newsapi.org/v2/top-headlines");
        } else {
            Toast.makeText(publishedOnValue.getContext(), "No Internet Connection!", Toast.LENGTH_LONG).show();
        }

        findViewById(R.id.imageButtonNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()) {
                    i++;
                    if (i >= news.size()) {
                        i = news.size() - 1;
                        Toast.makeText(v.getContext(), "You are on the last article!", Toast.LENGTH_SHORT).show();
                    } else {
                        displayContent(news.get(i));
                    }
                } else {
                    Toast.makeText(v.getContext(), "No Internet Connection!", Toast.LENGTH_LONG).show();
                }
            }
        });

        findViewById(R.id.imageButtonPrevious).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()) {
                    i--;
                    if (i < 0) {
                        i = 0;
                        Toast.makeText(v.getContext(), "You are on the First Article!", Toast.LENGTH_SHORT).show();
                    } else {
                        displayContent(news.get(i));
                    }
                } else {
                    Toast.makeText(v.getContext(), "No Internet Connection!", Toast.LENGTH_LONG).show();
                }
            }
        });

        findViewById(R.id.buttonQuit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
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
    public void urlHandleData(ArrayList<News> data) {
        if (data != null && data.size() > 0) {
            this.news = data;
            dialog.dismiss();
            headline.setVisibility(View.VISIBLE);
            findViewById(R.id.textViewPublishedOn).setVisibility(View.VISIBLE);
            publishedOnValue.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
            findViewById(R.id.textViewDescription).setVisibility(View.VISIBLE);
            description.setVisibility(View.VISIBLE);
            findViewById(R.id.imageButtonPrevious).setVisibility(View.VISIBLE);
            findViewById(R.id.imageButtonNext).setVisibility(View.VISIBLE);
            findViewById(R.id.buttonQuit).setVisibility(View.VISIBLE);
            displayContent(news.get(i));
        } else {
            Toast.makeText(publishedOnValue.getContext(), "No Articles Found", Toast.LENGTH_LONG).show();
        }
    }

    private void displayContent(News n) {
        headline.setText(n.getTitle());
        description.setText(n.getDescription());
        publishedOnValue.setText(n.getPublishedAt().substring(0, 10));
        if (isConnected()) {
            dialog.show();
            Picasso.get().load(n.getUrlToImage()).into(imageView);
            dialog.dismiss();
        } else {
            imageView.setImageResource(R.drawable.ic_launcher_background);
            Toast.makeText(publishedOnValue.getContext(), "No Internet Connection!", Toast.LENGTH_LONG).show();
        }
    }
}
