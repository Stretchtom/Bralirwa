package com.example.wendy_guo.j4sp.view;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.wendy_guo.j4sp.Constants;
import com.example.wendy_guo.j4sp.R;


public class ViewWebPageActivity extends Activity {

    private WebView mWebView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_web_page);
        final ActionBar actionBar = getActionBar();
        actionBar.hide();
        mWebView = (WebView) findViewById(R.id.webView);
        String url = getIntent().getStringExtra(Constants.VIEW_URL);
        mWebView.loadUrl(url);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());

    }


}
