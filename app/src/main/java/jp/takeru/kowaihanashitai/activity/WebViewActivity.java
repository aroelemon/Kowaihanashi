package jp.takeru.kowaihanashitai.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import jp.takeru.kowaihanashitai.db.dao.MyListDao;
import jp.takeru.kowaihanashitai.dto.FeedDto;
import jp.takeru.kowaihanashitai.dto.WebViewParamDto;
import jp.takeru.kowaihanashitai.R;

/**
 * WebViewアクティビティ.
 */
public class WebViewActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, View.OnClickListener {

    /** パラムDTO */
    private FeedDto.Feed paramDto;
    /** WebView */
    private WebView webView;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        paramDto = getIntent().getParcelableExtra(this.getClass().getCanonicalName());

        Toolbar toolbar = (Toolbar) findViewById(R.id.webview_toolbar);
        toolbar.setTitle(paramDto.title);
        toolbar.inflateMenu(R.menu.webview_header_menu);
        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setNavigationOnClickListener(this);
        toolbar.setOnMenuItemClickListener(this);
        setMyListIconState(toolbar.getMenu().findItem(R.id.webview_header_menu_mylist), MyListDao.existFeedById(paramDto.id));


        webView = (WebView) findViewById(R.id.webview_webview);
        webView.setWebViewClient(new MyWebViewClient());
        // JavaScript有効化
        webView.getSettings().setJavaScriptEnabled(true);

        // Progress
        progressBar = (ProgressBar) findViewById(R.id.webview_progressbar);
        WebChromeClient chrome = new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(progress);
            }
        };
        webView.setWebChromeClient(chrome);
        // ページの読み込み
        webView.loadUrl(paramDto.url);


        AdView mAdView = (AdView) findViewById(R.id.webview_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.webview_header_menu_mylist) {    //お気に入り
            if (MyListDao.existFeedById(paramDto.id)) {
                MyListDao.deleteById(paramDto.id);
            } else {
                MyListDao.add(paramDto);
            }
            setMyListIconState(item, MyListDao.existFeedById(paramDto.id));
            return true;
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        } else {
            finish();
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * お気に入りアイコンの設定を行います。
     *
     * @param item  アイテム
     * @param isAdd 登録されているかどうか
     */
    private void setMyListIconState(MenuItem item, boolean isAdd) {
        item.setIcon(isAdd ? R.drawable.ic_favorite_full : R.drawable.ic_favorite_empty);
    }

    @Override
    public void onClick(View v) {
        // Xボタン
        finish();
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            progressBar.setVisibility(View.VISIBLE);
            return false;
        }

        // ページ読み込み開始時の処理
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progressBar.setVisibility(View.VISIBLE);
        }

        // ページ読み込み完了時の処理
        @Override
        public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(View.INVISIBLE);
        }

        // ページ読み込みエラー時の処理
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String url) {
            progressBar.setVisibility(View.INVISIBLE);
        }

    }
}
