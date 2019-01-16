package com.group5.android.homework4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetBitmapApi extends AsyncTask<String, Void, Bitmap>{

    BitmapData bitmapData;
    Context context;

    public GetBitmapApi(BitmapData bitmapData, MainActivity context) {
        this.bitmapData = bitmapData;
        this.context = context;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        Bitmap myBitmap = null;
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            myBitmap = BitmapFactory.decodeStream(input);
            if (input == null){
                Log.d("HOLAAA","hola");
            }
            Log.d("INSIDE",strings[0]);
            return myBitmap;
        } catch (MalformedURLException e) {
            Log.d("HOLAAA","hola");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myBitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        bitmapData.bitmapHandledData(bitmap);
    }

    public static interface BitmapData{
        void bitmapHandledData(Bitmap bitmap);
    }

}
