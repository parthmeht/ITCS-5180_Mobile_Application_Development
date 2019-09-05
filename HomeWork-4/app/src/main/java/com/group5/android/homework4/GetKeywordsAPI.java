package com.group5.android.homework4;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class GetKeywordsAPI extends AsyncTask<String, Void, HashMap<String,ArrayList<String>>> {

    KeywordData keywordData;

    public GetKeywordsAPI(KeywordData keywordData) {
        this.keywordData = keywordData;
    }

    @Override
    protected HashMap<String,ArrayList<String>> doInBackground(String... strings) {
        HashMap<String,ArrayList<String>> keywordData = new HashMap<String,ArrayList<String>>();
        String string = new String();
        HttpURLConnection connection = null;
        try {
            URL url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            string = IOUtils.toString(connection.getInputStream(), "UTF8");
            String[] s = string.split(";");
            for (int i = 0; i < s.length; i++) {
                keywordData.put(s[i], new ArrayList<String>());
            }
            Log.d("keyword",string);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("Connection", "GetKeywordsAPI MalformedURLException Error");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Connection", "GetKeywordsAPI IOException Error");
        } catch (Exception e){
            e.printStackTrace();
            Log.e("Connection", "GetKeywordsAPI Exception Error");
        } finally {
            if (connection != null){
                connection.disconnect();
            }
        }
        return keywordData;
    }

    @Override
    protected void onPostExecute(HashMap<String,ArrayList<String>> hashMap) {
        keywordData.handleData(hashMap);
    }

    public static interface KeywordData{
        void handleData(HashMap<String,ArrayList<String>> data);
    }
}