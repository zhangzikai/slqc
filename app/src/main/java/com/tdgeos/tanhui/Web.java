package com.tdgeos.tanhui;

import com.tdgeos.yangdi.YangdiMgr;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Web extends Activity {

    private String indexUrl = "file:///android_asset/web/index.html";
    private WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        setTitle(getString(R.string.app_name));

        webView = (WebView) findViewById(R.id.doc_web);
        webView.getSettings().setJavaScriptEnabled(true);//支持JS
        //webView.getSettings().setUseWideViewPort(true); //设置webview推荐使用的窗口
        //webView.getSettings().setLoadWithOverviewMode(true);//根据窗口大小显示页面，可缩放
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl(indexUrl);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}