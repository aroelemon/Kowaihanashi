package jp.takeru.kowaihanashitai.db.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import jp.takeru.kowaihanashitai.db.dto.MyListTableDto;
import jp.takeru.kowaihanashitai.dto.FeedDto;

/**
 * お気に入りDAO
 */
public class MyListDao {

    /**
     * お気に入りに追加します。
     *
     * @param feed フィード
     */
    public static void add(FeedDto.Feed feed) {
        if (existFeedById(feed.id)) { // 登録済み
            return;
        }
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        MyListTableDto myListTableDto = realm.createObject(MyListTableDto.class);
        myListTableDto.setId(feed.id);
        myListTableDto.setSiteId(feed.siteId);
        myListTableDto.setTitle(feed.title);
        myListTableDto.setSiteName(feed.siteName);
        myListTableDto.setUrl(feed.url);
        myListTableDto.setDate(new Date().toString());
        realm.commitTransaction();
        realm.close();
    }

    /**
     * IDを指定して対象の項目を削除します。
     *
     * @param id フィード
     */
    public static void deleteById(int id) {
        MyListTableDto myListTableDto = findById(id);
        if (myListTableDto == null) { // 項目なし
            return;
        }
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.where(MyListTableDto.class).equalTo("id", id).findAll().deleteAllFromRealm();
        realm.commitTransaction();
        realm.close();
    }

    /**
     * IDを指定してFeedを取得します。
     *
     * @param id ID
     * @return 存在する場合は true、それ以外は false
     */
    public static boolean existFeedById(int id) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(MyListTableDto.class).equalTo("id", id).count() > 0;
    }

    /**
     * すべてのマイリストを取得します。
     *
     * @return マイリスト全件
     */
    public static List<FeedDto.Feed> findAll() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<MyListTableDto> realmResults = realm.where(MyListTableDto.class).findAll();
        List<FeedDto.Feed> feeds = new ArrayList<>();
        for (MyListTableDto myListTableDto : realmResults) {
            feeds.add(myListTableDto.getFeed());
        }
        realm.close();
        return feeds;
    }

    /**
     * 指定した ID に一致するフィードを取得します。
     *
     * @param id 　ID
     * @return フィード
     */
    public static MyListTableDto findById(int id) {
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<MyListTableDto> query = realm.where(MyListTableDto.class).equalTo("id", id);
        realm.close();
        return query.findFirst();
    }
}
