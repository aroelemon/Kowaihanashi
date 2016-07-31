package jp.takeru.kowaihanashitai.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import jp.takeru.kowaihanashitai.R;
import jp.takeru.kowaihanashitai.adapter.SitesAdapter;
import jp.takeru.kowaihanashitai.db.dao.SiteDao;

/**
 * 設定アクティビティ.
 */
public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setNavigationOnClickListener(this);

        ListView listView = (ListView) findViewById(R.id.setting_listview);
        SitesAdapter adapter = new SitesAdapter(this, SiteDao.findAll().sites);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
