package com.group5.android.homework4;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements GetKeywordsAPI.KeywordData, GetUrlAPI.UrlData, GetBitmapApi.BitmapData {

    private HashMap<String, ArrayList<String>> keywordData;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> keywords;
    private String selectedKeyword;
    private TextView textView;
    private ImageButton buttonNext, buttonPrev;
    private ImageView imageView;
    private ProgressDialog progressDialog;
    private ArrayList<String> bitmapData;
    private int bmapIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        buttonNext = findViewById(R.id.buttonNext);
        buttonPrev = findViewById(R.id.buttonPrevious);
        imageView = findViewById(R.id.imageView);
        progressDialog = new ProgressDialog(MainActivity.this);

        buttonPrev.setEnabled(false);
        buttonNext.setEnabled(false);

        if (isConnected()) {
            keywordData = new HashMap<>();
            new GetKeywordsAPI(MainActivity.this).execute("http://dev.theappsdr.com/apis/photos/keywords.php");

            findViewById(R.id.buttonGo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (keywordData != null && keywordData.size() > 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        keywords = new ArrayList<String>(keywordData.keySet());
                        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, keywords) {
                            @NonNull
                            @Override
                            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                                TextView text_view = (TextView) super.getView(position, convertView, parent);
                                text_view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                                return text_view;
                            }
                        };
                        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog.show();
                                Log.d("Keyword", keywords.get(which));
                                selectedKeyword = keywords.get(which);
                                textView.setText(selectedKeyword);
                                RequestParams params = new RequestParams();
                                params.addParameter("keyword", selectedKeyword);
                                new GetUrlAPI(MainActivity.this, params).execute("http://dev.theappsdr.com/apis/photos/index.php");
                            }
                        });
                        builder.setTitle("Choose a Keyword");
                        AlertDialog dialog = builder.create();
                        ListView listView = dialog.getListView();
                        listView.setDivider(new ColorDrawable(Color.BLUE));
                        listView.setDividerHeight(5);
                        dialog.show();
                    } else {
                        Toast.makeText(v.getContext(), "No Keyword Present in the database!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText(textView.getContext(), "No Internet Connection!", Toast.LENGTH_LONG).show();
        }

        buttonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()){
                    Log.d("Hello", String.valueOf(bitmapData.size()) + String.valueOf(bmapIndex));
                    progressDialog.show();
                    if (bmapIndex == 0) {
                        bmapIndex = bitmapData.size() - 1;
                        new GetBitmapApi(MainActivity.this, MainActivity.this).execute(bitmapData.get(bmapIndex));
                    } else {
                        bmapIndex = bmapIndex - 1;
                        new GetBitmapApi(MainActivity.this, MainActivity.this).execute(bitmapData.get(bmapIndex));
                    }
                }else{
                    Toast.makeText(textView.getContext(), "No Internet Connection!", Toast.LENGTH_LONG).show();
                }
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isConnected()){
                    progressDialog.show();
                    if (bmapIndex == bitmapData.size() - 1) {
                        bmapIndex = 0;
                        new GetBitmapApi(MainActivity.this, MainActivity.this).execute(bitmapData.get(bmapIndex));
                    } else {
                        bmapIndex = bmapIndex + 1;
                        new GetBitmapApi(MainActivity.this, MainActivity.this).execute(bitmapData.get(bmapIndex));
                    }
                }else{
                    Toast.makeText(textView.getContext(), "No Internet Connection!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()
                || (networkInfo.getType() != ConnectivityManager.TYPE_WIFI && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }

    @Override
    public void handleData(HashMap<String, ArrayList<String>> data) {
        keywordData = data;
    }

    @Override
    public void urlHandleData(final ArrayList<String> data) {
        if (data != null && data.size() > 0) {
            keywordData.put(selectedKeyword, data);
            bitmapData = data;
            if (data.size() == 1){
                buttonNext.setEnabled(false);
                buttonPrev.setEnabled(false);
            }
            else {
                buttonNext.setEnabled(true);
                buttonPrev.setEnabled(true);
            }

            Log.d("DataFetch", data.get(0).toString());
            bmapIndex = 0;
            progressDialog.setMessage("Loading Image");
            new GetBitmapApi(MainActivity.this, MainActivity.this).execute(bitmapData.get(bmapIndex));
        } else {
            progressDialog.dismiss();
            buttonNext.setEnabled(false);
            buttonPrev.setEnabled(false);
            imageView.setImageResource(R.drawable.ic_launcher_background);
            Toast.makeText(this, "No Image Found", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void bitmapHandledData(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
        progressDialog.dismiss();
    }
}
