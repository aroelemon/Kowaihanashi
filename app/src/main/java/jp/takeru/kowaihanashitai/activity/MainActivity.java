package jp.takeru.kowaihanashitai.activity;

import android.content.Intent;
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

import jp.takeru.kowaihanashitai.R;
import jp.takeru.kowaihanashitai.db.dao.HistoryDao;
import jp.takeru.kowaihanashitai.dto.FeedDto;
import jp.takeru.kowaihanashitai.dto.WebViewParamDto;
import jp.takeru.kowaihanashitai.fragment.MyListFragment;
import jp.takeru.kowaihanashitai.fragment.NewArrivalsFragment;


public class MainActivity extends AppCompatActivity implements NewArrivalsFragment.OnItemClickListener, MyListFragment.OnItemClickListener, ViewPager.OnPageChangeListener {


    /** ViewPager のアダプタ */
    private FragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.main_drawerlayout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tablayout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.main_viewpager);

        adapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(this);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onItemClick(FeedDto.Feed feed) {
        // 閲覧済みに追加
        HistoryDao.add(feed);

        // WebViewの起動
        Intent intent = new Intent(this, WebViewActivity.class);
        WebViewParamDto paramDto = new WebViewParamDto();
        paramDto.title = feed.title;
        paramDto.url = feed.url;
        intent.putExtra(WebViewActivity.class.getCanonicalName(), paramDto);
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
}
