package com.inosuctechnologies.linkup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BusinessTypeActivity extends AppCompatActivity {
    @BindView(R.id.buzzWebView) WebView mWebView;
    @BindView(R.id.buzzProgress) ProgressBar progress;
    public static final String URL_DATA = "http://linkup.ist.co.zw/mobile/buzz.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Business Type");
        setContentView(R.layout.fragment_buzz);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        if (savedInstanceState != null){
            mWebView.restoreState(savedInstanceState);
        } else {
            loadWebView();
        }
    }

    private void loadWebView() {
        mWebView = findViewById(R.id.buzzWebView);
        mWebView.clearCache(true);
        enableJavascript();
        enableZoom();
        mWebView.loadUrl(URL_DATA);
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
        webSettings.setAllowContentAccess(true);
        webSettings.setAppCacheEnabled(true);
        mWebView.setWebViewClient(new BuzzWebClient());
    }

    //    private void loadWebView(){
//        WebSettings webSettings = mWebView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setBuiltInZoomControls(true);
//        webSettings.setAllowContentAccess(true);
//        webSettings.setSupportZoom(true);
//        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
//
//        BuzzWebClient buzzWebClient = new BuzzWebClient();
//        mWebView.setWebViewClient(buzzWebClient);
//        mWebView.loadUrl(URL_DATA);
//    }

//    @SuppressLint("SetJavaScriptEnabled")
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mWebView.getSettings().setSupportZoom(true);
//        mWebView.getSettings().setBuiltInZoomControls(true);
//        mWebView.getSettings().setLoadsImagesAutomatically(true);
//        mWebView.getSettings().setDomStorageEnabled(true);
//        mWebView.getSettings().setAppCacheEnabled(true);
//        mWebView.getSettings().setAppCacheMaxSize(1024*1024*8);
//        mWebView.getSettings().setAllowFileAccess(true);
//        mWebView.getSettings().setJavaScriptEnabled(true);
//        mWebView.loadUrl(URL_DATA);
//        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ONLY);
//    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save the state of the WebView
        mWebView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // restore the state of the WebView
        mWebView.restoreState(savedInstanceState);
    }

    private class BuzzWebClient extends WebViewClient {

//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            return super.shouldOverrideUrlLoading(view, url);
//            if (!url.equals("http://linkup.ist.co.zw/mobile/buzz.php")) {
////                preferences.edit().putString(url,"PREFS_ID").apply();
//            }
//            return true;
//        }

        @Override
        public void onPageStarted(WebView webView, String url, Bitmap favicon) {
            super.onPageStarted(webView, url, favicon);
            webView.setVisibility(View.INVISIBLE);
            progress.setVisibility(View.VISIBLE);
        }

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