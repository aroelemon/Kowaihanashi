package jp.takeru.kowaihanashitai.db.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import jp.takeru.kowaihanashitai.db.dto.HistoryDto;
import jp.takeru.kowaihanashitai.dto.FeedDto;

/**
 * 閲覧履歴DAO
 */
public class HistoryDao {

    /**
     * 閲覧履歴に追加します。
     *
     * @param feed フィード
     */
    public static void add(FeedDto.Feed feed) {
        if (getFeedById(feed.id) != null) { // 登録済み
            return;
        }
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        HistoryDto historyDto = realm.createObject(HistoryDto.class);
        historyDto.setId(feed.id);
        historyDto.setSiteId(feed.siteId);
        historyDto.setTitle(feed.title);
        historyDto.setUrl(feed.url);
        historyDto.setDate(new Date().toString());
        realm.commitTransaction();
        realm.close();
    }

    /**
     * IDを指定してFeedを取得します。
     *
     * @param id ID
     * @return 閲覧履歴DTO
     */
    public static HistoryDto getFeedById(int id) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(HistoryDto.class).equalTo("id", id).findFirst();
    }

    /**
     * すべての閲覧履歴を取得します。
     *
     * @return 閲覧履歴全件
     */
    public static List<HistoryDto> findAll() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<HistoryDto> realmResults = realm.where(HistoryDto.class).findAll();
        List<HistoryDto> historyDtoList = new ArrayList<>();
        for (HistoryDto historyDto : realmResults) {
            historyDtoList.add(historyDto);
        }
        return historyDtoList;
    }
}
