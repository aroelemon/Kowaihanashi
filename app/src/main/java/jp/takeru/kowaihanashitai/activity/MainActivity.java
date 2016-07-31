package jp.takeru.kowaihanashitai.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.IOException;

import jp.takeru.kowaihanashitai.R;
import jp.takeru.kowaihanashitai.db.dao.HistoryDao;
import jp.takeru.kowaihanashitai.db.dao.SiteDao;
import jp.takeru.kowaihanashitai.dto.FeedDto;
import jp.takeru.kowaihanashitai.dto.SiteDto;
import jp.takeru.kowaihanashitai.fragment.MyListFragment;
import jp.takeru.kowaihanashitai.fragment.NewArrivalsFragment;
import jp.takeru.kowaihanashitai.parser.SitesParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity implements NewArrivalsFragment.OnItemClickListener, MyListFragment.OnItemClickListener, ViewPager.OnPageChangeListener, NavigationView.OnNavigationItemSelectedListener {


    /** ViewPager のアダプタ */
    private FragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.main_drawerlayout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        drawerToggle.setDrawerIndicatorEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.main_navigationview);
        navigationView.setNavigationItemSelectedListener(this);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tablayout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.main_viewpager);

        adapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(this);
        tabLayout.setupWithViewPager(viewPager);

        AdView mAdView = (AdView) findViewById(R.id.main_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // まとめサイト取得
        requestApi();
    }

    @Override
    public void onItemClick(FeedDto.Feed feed) {
        // 閲覧済みに追加
        HistoryDao.add(feed);

        // WebViewの起動
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(WebViewActivity.class.getCanonicalName(), feed);
        startActivity(intent);
    }

    @Override
    public void onPageSelected(int position) {
        Fragment fragment = adapter.getItem(position);
        if (fragment instanceof MyListFragment) {   // お気に入り
            // お気に入りをリフレッシュ
            ((MyListFragment) fragment).refresh();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // empty
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // empty
    }

    /**
     * まとめサイト取得APIにリクエストします。
     */
    private void requestApi() {
        SitesTask sitesTask = new SitesTask();
        Uri.Builder url = Uri.parse(getString(R.string.sites_api_url)).buildUpon();
        url.appendQueryParameter("token", getString(R.string.sites_api_token));
        sitesTask.execute(url.toString());
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_setting) {  //設定
            // 設定画面起動
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
        }

        return false;
    }


    /**
     * ViewPagerのアダプター.
     */
    private static class MainPagerAdapter extends FragmentPagerAdapter {


        private Fragment[] fragments = {new NewArrivalsFragment(), new MyListFragment()};
        private String[] titles = {"新着", "お気に入り"};

        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public int getItemPosition(Object object) {
            if (object instanceof MyListFragment) {
                ((MyListFragment) object).refresh();
            }
            return super.getItemPosition(object);
        }
    }

    // TODO:通信処理を共通クラスにまとめる
    /**
     * まとめサイト取得タスク
     */
    private class SitesTask extends AsyncTask<String, Void, SiteDto> {

        @Override
        protected SiteDto doInBackground(String... params) {

            OkHttpClient client = new OkHttpClient();
            SiteDto siteDto;
            try {
                Request request = new Request.Builder().url(params[0]).build();
                Response response = client.newCall(request).execute();
                siteDto = SitesParser.parse(response.body().string());
            } catch (IOException e) {
                return null;
            }
            return siteDto;
        }

        @Override
        protected void onPostExecute(SiteDto siteDto) {
            if (siteDto != null) {
                // まとめサイトをDBに格納
                SiteDao.add(siteDto);
            }
        }
    }

}
