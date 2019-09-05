package com.group5.android.hw05;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ShowNews extends AppCompatActivity implements HeadlinesApi.HeadLinesData {
    Bundle b;
    private AlertDialog.Builder builder;
    private ArrayList<News> newsList;
    ListView listView;
    private AlertDialog dialog;
    private ArrayAdapter<News> newsArrayAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_news);
        b = getIntent().getExtras();
        setTitle(b.get("name").toString());
        listView = findViewById(R.id.listViewNews);
        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setTitle("Loading Stories..").setView(inflater.inflate(R.layout.dialog_bar, null));
        dialog = builder.create();
        dialog.show();
        RequestParams params = new RequestParams();
        params.addParameter("apiKey", b.get("apiKey").toString());
        params.addParameter("sources", b.get("sources").toString());
        new HeadlinesApi(params, (HeadlinesApi.HeadLinesData) ShowNews.this, ShowNews.this).execute("https://newsapi.org/v2/top-headlines");
    }

    @Override
    public void healineHandledData(ArrayList<News> newsArrayList) {
        Log.d("You have reached", newsArrayList.get(0).getAuthor());
        newsList = newsArrayList;

        newsArrayAdapter = new NewsAdapter(this, R.layout.headline, newsList);
        listView.setAdapter(newsArrayAdapter);
        dialog.dismiss();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bundle bundle = new Bundle();
                bundle.putString("url", newsList.get(position).getUrl());
                Intent i = new Intent(getApplicationContext(), WebBrowserView.class);
                i.putExtras(bundle);
                startActivity(i);

            }
        });
    }
}
