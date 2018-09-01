package com.inosuctechnologies.linkup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Parcel;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract;
import android.support.constraint.solver.widgets.Helper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MyProductsActivity extends AppCompatActivity {
    WebView mWebView;
    ProgressBar progress;
//    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("My Products");
        setContentView(R.layout.fragment_products);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progress = findViewById(R.id.productProgress);

        // create instance of cookieSyncManager
        CookieSyncManager.createInstance(MyProductsActivity.this);

        // setting up webview
        mWebView = findViewById(R.id.productswebView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.setWebViewClient(new MyWebClient());
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDisplayZoomControls(true);
        mWebView.getSettings().setUseWideViewPort(true);

        // save history
        Bundle savedWebview = restoreFromFile();
        // if not found load the default url
        if(savedWebview != null) {
            mWebView.restoreState(savedWebview);
        } else {
            mWebView.loadUrl("http://linkup.ist.co.zw/mobile/myprod.php");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // start cookieSyncManager
        CookieSyncManager.getInstance().startSync();
        // for when the user navigate away for a while
        mWebView.onResume();
    }

    @Override
    protected void onPause() {
        // for when the user navigate away for a while
        mWebView.onPause();
//        mWebView.saveState(savedWebView);
        CookieSyncManager.getInstance().stopSync();
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        // create a new fresh bundle
        Bundle in = new Bundle();
        // save webview history in it
        mWebView.saveState(in);
        // Check if a saved file exist and delete
        File webViewSaveFile = new File(getFilesDir().getPath() + "/webviewForServer");
        if (webViewSaveFile.exists()) {
            webViewSaveFile.delete();
        }

        // create new file for the server
        FileOutputStream fos;
        try {
            fos = openFileOutput("webViewHisForServer", Context.MODE_PRIVATE);
            Parcel parcel = Parcel.obtain(); // creating empty parcel object
            in.writeToParcel(parcel, 0); // saving bundle as parcel
            fos.write(parcel.marshall()); // writing parcel to file
            fos.flush();
            fos.close();
//            getSharedPreferences(Helper.pref, MODE_PRIVATE)
//                    .edit()
//                    .putString(Helper.AndroidVersion_pref_Key, Build.FINGERPRINT)
//                    .apply();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bundle restoreFromFile() {
        // check file
        File webViewSaveFile = new File(getFilesDir().getPath() + "/webViewForServer");
        if (!webViewSaveFile.exists())
            return null;
//        if (!getSharedPreferences(SyncStateContract.Helpers.pref, MODE_PRIVATE).getString(
//                Helper.AndroidVersion_pref_Key, "").matches(Build.FINGERPRINT)) {
//            webViewSaveFile.delete();
//            return null;
//        }

        Bundle bundle = null;
        FileInputStream fis;
        try {
            fis = new FileInputStream(webViewSaveFile);
            byte fileContent[] = new byte[(int) webViewSaveFile.length()];
            fis.read(fileContent);
            fis.close();
            Parcel parcel = Parcel.obtain();
            parcel.unmarshall(fileContent, 0, fileContent.length);
            parcel.setDataPosition(0);
            bundle = parcel.readBundle();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bundle;
    }

//    private void loadWebView() {
//        enableJavascript();
//        enableZoom();
//        mWebView.loadUrl("http://linkup.ist.co.zw/mobile/myprod.php");
//    }
//
//    private void enableZoom() {
//        mWebView.getSettings().setBuiltInZoomControls(true);
//        mWebView.getSettings().setDisplayZoomControls(false);
//    }
//
//    @SuppressLint("SetJavaScriptEnabled")
//    private void enableJavascript() {
//        WebSettings webSettings = mWebView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setDomStorageEnabled(true);
//        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//        mWebView.setWebViewClient(new MyWebClient());
//    }

    private class MyWebClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

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
            CookieSyncManager.getInstance().sync();
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