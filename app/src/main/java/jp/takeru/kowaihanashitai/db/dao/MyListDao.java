package jp.takeru.kowaihanashitai.db.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import jp.takeru.kowaihanashitai.db.dto.MyListDto;
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
        MyListDto myListDto = realm.createObject(MyListDto.class);
        myListDto.setId(feed.id);
        myListDto.setSiteId(feed.siteId);
        myListDto.setTitle(feed.title);
        myListDto.setUrl(feed.url);
        myListDto.setDate(new Date().toString());
        realm.commitTransaction();
        realm.close();
    }

    /**
     * IDを指定して対象の項目を削除します。
     *
     * @param id フィード
     */
    public static void deleteById(int id) {
        MyListDto myListDto = findById(id);
        if (myListDto == null) { // 項目なし
            return;
        }
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.where(MyListDto.class).equalTo("id", id).findAll().deleteAllFromRealm();
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
        return realm.where(MyListDto.class).equalTo("id", id).count() > 0;
    }

    /**
     * すべてのマイリストを取得します。
     *
     * @return マイリスト全件
     */
    public static List<FeedDto.Feed> findAll() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<MyListDto> realmResults = realm.where(MyListDto.class).findAll();
        List<FeedDto.Feed> feeds = new ArrayList<>();
        for (MyListDto myListDto : realmResults) {
            feeds.add(myListDto.getFeed());
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
    public static MyListDto findById(int id) {
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<MyListDto> query = realm.where(MyListDto.class).equalTo("id", id);
        realm.close();
        return query.findFirst();
    }
}
