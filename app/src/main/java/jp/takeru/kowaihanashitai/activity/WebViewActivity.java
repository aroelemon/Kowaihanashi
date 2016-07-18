package jp.takeru.kowaihanashitai.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

import jp.takeru.kowaihanashitai.dto.WebViewParamDto;
import jp.takeru.kowaihanashitai.R;

/**
 * WebViewアクティビティ.
 */
public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        WebViewParamDto paramDto = getIntent().getParcelableExtra(this.getClass().getCanonicalName());

        Toolbar toolbar = (Toolbar) findViewById(R.id.webview_toolbar);
        toolbar.setTitle(paramDto.title);
        setSupportActionBar(toolbar);

        WebView webView = (WebView) findViewById(R.id.webview_webview);
        // JavaScript有効化
        webView.getSettings().setJavaScriptEnabled(true);
        // ページの読み込み
        webView.loadUrl(paramDto.url);
    }
}
