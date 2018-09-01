package com.inosuctechnologies.linkup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class ProductCategoriesActivity extends AppCompatActivity {
    WebView mWebView;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Categories");
        setContentView(R.layout.fragment_category);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mWebView = findViewById(R.id.categoryWeb);
        progress = findViewById(R.id.categoryProgress);
        loadWebView();
    }

    private void loadWebView() {
        mWebView.clearCache(true);
        enableJavascript();
        enableZoom();
        mWebView.loadUrl("http://linkup.ist.co.zw/mobile/categories.php");
    }

    private void enableZoom() {
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDisplayZoomControls(false);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void enableJavascript() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.setWebViewClient(new ProdWebClient());
    }

    private class ProdWebClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView webView, String url, Bitmap favicon) {
            super.onPageStarted(webView, url, favicon);
            webView.setVisibility(View.INVISIBLE);
            progress.setVisibility(View.VISIBLE);
        }

        // This method will be triggered when the Page loading is completed
        @Override
        public void onPageFinished(WebView webView, String url) {
            super.onPageFinished(webView, url);
            progress.setVisibility(View.INVISIBLE);
            webView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}