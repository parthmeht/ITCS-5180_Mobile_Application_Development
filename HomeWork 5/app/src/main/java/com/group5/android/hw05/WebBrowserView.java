package com.group5.android.hw05;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;


public class WebBrowserView extends Activity{
    Bundle b;
    private WebView webView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        b = getIntent().getExtras();
        webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(b.get("url").toString());
    }
}
