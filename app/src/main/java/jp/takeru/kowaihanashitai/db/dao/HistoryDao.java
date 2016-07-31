package jp.takeru.kowaihanashitai.db.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import jp.takeru.kowaihanashitai.db.dto.HistoryTableDto;
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
        HistoryTableDto historyTableDto = realm.createObject(HistoryTableDto.class);
        historyTableDto.setId(feed.id);
        historyTableDto.setSiteId(feed.siteId);
        historyTableDto.setTitle(feed.title);
        historyTableDto.setUrl(feed.url);
        historyTableDto.setDate(new Date().toString());
        realm.commitTransaction();
        realm.close();
    }

    /**
     * IDを指定してFeedを取得します。
     *
     * @param id ID
     * @return 閲覧履歴DTO
     */
    public static HistoryTableDto getFeedById(int id) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(HistoryTableDto.class).equalTo("id", id).findFirst();
    }

    /**
     * すべての閲覧履歴を取得します。
     *
     * @return 閲覧履歴全件
     */
    public static List<HistoryTableDto> findAll() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<HistoryTableDto> realmResults = realm.where(HistoryTableDto.class).findAll();
        List<HistoryTableDto> historyTableDtoList = new ArrayList<>();
        for (HistoryTableDto historyTableDto : realmResults) {
            historyTableDtoList.add(historyTableDto);
        }
        return historyTableDtoList;
    }
}
