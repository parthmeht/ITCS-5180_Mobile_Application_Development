package com.group5.android.inclassassignment4;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    ExecutorService threadpool;
    Handler handler;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        pb = findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);
        threadpool = Executors.newFixedThreadPool(2);
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {

                Log.d("what","what1");
                if (msg.obj != null){
                    imageView.setImageBitmap((Bitmap) msg.obj);
                    Log.d("what","what");
                    pb.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });


        findViewById(R.id.buttonThread).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb.setVisibility(View.VISIBLE);
                threadpool.execute(new ImageThreadDownload());
            }
        });
        findViewById(R.id.buttonAsync).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb.setVisibility(View.VISIBLE);
                String[] imageURL = {"https://cdn.pixabay.com/photo/2014/12/16/22/25/youth-570881_960_720.jpg"};
                new ImageDownloadAsyncTask().execute(imageURL);
            }
        });
    }
    class ImageThreadDownload implements Runnable{

        @Override
        public void run() {
            Log.d("Inside","Run");
            String imageURL = "https://cdn.pixabay.com/photo/2017/12/31/06/16/boats-3051610_960_720.jpg";
            Bitmap myBitmap = getImageBitmap(imageURL);
            Message message = new Message();
            message.obj = myBitmap;
            handler.sendMessage(message);
        }
    }

    class ImageDownloadAsyncTask extends AsyncTask<String, Integer, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... strings) {
            return getImageBitmap(strings);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb = findViewById(R.id.progressBar);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
                pb.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }

    Bitmap getImageBitmap(String... strings) {
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
