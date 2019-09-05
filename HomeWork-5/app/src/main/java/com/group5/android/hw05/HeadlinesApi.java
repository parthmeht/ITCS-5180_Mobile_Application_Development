package com.group5.android.hw05;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class HeadlinesApi extends AsyncTask<String, Void, ArrayList<News>> {

    RequestParams params;
    HeadLinesData headLinesData;
    Context context;

    public HeadlinesApi(RequestParams params, HeadLinesData headLinesData, ShowNews mainActivity) {
        this.params = params;
        context = mainActivity;
        this.headLinesData = headLinesData;
    }

    @Override
    protected ArrayList<News> doInBackground(String... strings) {

        HttpURLConnection connection = null;
        ArrayList<News> newsArrayList = new ArrayList<>();
        Log.d("Inside", "doInBackground" + strings[0]);
        try {
            URL url = new URL(params.getEncodedUrl(strings[0]));
            Log.d("The Url Is", url.toString());
            connection = (HttpURLConnection) url.openConnection();
            String s = IOUtils.toString(connection.getInputStream(), "UTF-8");
            JSONObject root = new JSONObject(s);
            JSONArray articles = root.getJSONArray("articles");
            for (int i = 0; i < articles.length(); i++) {
                Log.d("Source", articles.get(i).toString());
                News news = new News();
                news.author = articles.getJSONObject(i).getString("author");
                news.url = articles.getJSONObject(i).getString("url");
                news.utlToImage = articles.getJSONObject(i).getString("urlToImage");
                news.publishedAt = articles.getJSONObject(i).getString("publishedAt");
                news.sourcetitle = articles.getJSONObject(i).getString("title");
                newsArrayList.add(news);
            }
            Log.d("Source Array List", newsArrayList.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newsArrayList;
    }

    @Override
    protected void onPostExecute(ArrayList<News> newsArrayList) {
        headLinesData.healineHandledData(newsArrayList);
    }

    public static interface HeadLinesData {
        void healineHandledData(ArrayList<News> newsArrayList);
    }
}
