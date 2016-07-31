package jp.takeru.kowaihanashitai;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * 独自アプリケーションクラス.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(config);
    }

}
