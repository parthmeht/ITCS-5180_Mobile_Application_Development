package com.group5.android.homework4;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class GetUrlAPI extends AsyncTask<String, Void, ArrayList<String>> {

    GetUrlAPI.UrlData urlData;
    RequestParams mParams;

    public GetUrlAPI(GetUrlAPI.UrlData urlData, RequestParams params) {
        this.urlData = urlData;
        this.mParams = params;
    }

    @Override
    protected ArrayList<String> doInBackground(String... strings) {
        ArrayList<String> urlData = null;
        String string = new String();
        HttpURLConnection connection = null;
        try {
            URL url = new URL(mParams.getEncodedUrl(strings[0]));
            connection = (HttpURLConnection) url.openConnection();
            string = IOUtils.toString(connection.getInputStream(), "UTF8");
            Log.d("url",string);
            if (string!=null && !"".equalsIgnoreCase(string)){
                String[] word = string.split("\n");
                ArrayList<String> s = new ArrayList<String>();
                for (String w : word){
                    s.add(w);
                }
                urlData = s;
                //urlData = (ArrayList<String>) Arrays.asList(string.split("\n"));
                Log.d("url00",urlData.toString());
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
        return urlData;
    }

    @Override
    protected void onPostExecute(ArrayList<String> strings) {
        urlData.urlHandleData(strings);
    }

    public static interface UrlData{
        void urlHandleData(ArrayList<String> data);
    }
}
