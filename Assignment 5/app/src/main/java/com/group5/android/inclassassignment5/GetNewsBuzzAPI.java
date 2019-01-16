package com.group5.android.inclassassignment5;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class GetNewsBuzzAPI extends AsyncTask<String, Void, ArrayList<News>> {

    GetNewsBuzzAPI.UrlData urlData;
    RequestParams mParams;

    public GetNewsBuzzAPI(GetNewsBuzzAPI.UrlData urlData, RequestParams params) {
        this.urlData = urlData;
        this.mParams = params;
    }

    @Override
    protected ArrayList<News> doInBackground(String... strings) {
        ArrayList<News> news = new ArrayList<>();
        String json = new String();
        HttpURLConnection connection = null;
        try {
            URL url = new URL(mParams.getEncodedUrl(strings[0]));
            connection = (HttpURLConnection) url.openConnection();
            json = IOUtils.toString(connection.getInputStream(), "UTF8");
            Log.d("Json : ",json);
            JSONObject rootObj = new JSONObject(json);
            JSONArray articles = rootObj.getJSONArray("articles");
            for (int i = 0; i < articles.length(); i++) {
                JSONObject obj = articles.getJSONObject(i);
                News n = new News(obj.getString("title"),obj.getString("description"),obj.getString("urlToImage"),obj.getString("publishedAt"));
                news.add(n);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("Connection", "GetUrlAPI MalformedURLException Error");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Connection", "GetUrlAPI IOException Error");
        } catch (Exception e){
            e.printStackTrace();
            Log.e("Connection", "GetUrlAPI Exception Error");
        } finally {
            if (connection != null){
                connection.disconnect();
            }
        }
        return news;
    }

    @Override
    protected void onPostExecute(ArrayList<News> news) {
        urlData.urlHandleData(news);
    }

    public static interface UrlData{
        void urlHandleData(ArrayList<News> data);
    }
}
