package com.inosuctechnologies.linkup.ui.fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.inosuctechnologies.linkup.R;

public class LoginFragment extends Fragment {
    private ProgressBar progress;

    public LoginFragment() {

    }

    @SuppressLint("SetJavaScriptEnabled")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        setRetainInstance(true);
        progress = view.findViewById(R.id.loginProgress);
        WebView webView;
        webView = view.findViewById(R.id.loginWebView);
        webView.loadUrl("http://linkup.ist.co.zw/mobile/signup.php");

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webView.setWebViewClient(new LoginClient());
        return view;
    }

    private class LoginClient extends WebViewClient {

//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            return false;
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
}