package jp.takeru.kowaihanashitai.db.dao;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import jp.takeru.kowaihanashitai.db.dto.SiteTableDto;
import jp.takeru.kowaihanashitai.dto.SiteDto;

/**
 * まとめサイトDAO
 */
public class SiteDao {

    /**
     * まとめサイトに追加します。
     *
     * @param siteDto まとめサイト
     */
    public static void add(SiteDto siteDto) {
        Realm realm = Realm.getDefaultInstance();
        for (SiteDto.Site site : siteDto.sites) {

            if (existSiteById(site.id)) { // 登録済み
                return;
            }
            realm.beginTransaction();
            SiteTableDto siteTableDto = realm.createObject(SiteTableDto.class);
            siteTableDto.setId(site.id);
            siteTableDto.setSiteName(site.siteName);
            siteTableDto.setEnable(true);
            realm.commitTransaction();
        }
        realm.close();
    }

    /**
     * サイトIDを指定して対象の項目を削除します。
     *
     * @param id サイトid
     */
    public static void deleteById(int id) {
        SiteTableDto siteTableDto = findById(id);
        if (siteTableDto == null) { // 項目なし
            return;
        }
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.where(SiteTableDto.class).equalTo("id", id).findAll().deleteAllFromRealm();
        realm.commitTransaction();
        realm.close();
    }

    /**
     * 指定されてIDのサイトをOFFにします。
     *
     * @param id     ID
     * @param enable ONかどうか
     */
    public static void updateEnable(int id, boolean enable) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        SiteTableDto siteTableDto = realm.where(SiteTableDto.class).equalTo("id", id).findFirst();
        siteTableDto.setEnable(enable);
        realm.commitTransaction();
        realm.close();
    }

    /**
     * IDを指定してサイトが存在するかを返します.
     *
     * @param id ID
     * @return 存在する場合は true、それ以外は false
     */
    public static boolean existSiteById(int id) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(SiteTableDto.class).equalTo("id", id).count() > 0;
    }

    /**
     * すべてのまとめサイトを取得します。
     *
     * @return まとめサイト全件
     */
    public static SiteDto findAll() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<SiteTableDto> realmResults = realm.where(SiteTableDto.class).findAll();
        SiteDto siteDto = new SiteDto();
        siteDto.sites = new ArrayList<>();
        for (SiteTableDto siteTableDto : realmResults) {
            SiteDto.Site site = new SiteDto.Site();
            site.id = siteTableDto.getId();
            site.siteName = siteTableDto.getSiteName();
            site.enable = siteTableDto.isEnable();
            siteDto.sites.add(site);
        }
        realm.close();
        return siteDto;
    }

    /**
     * 購読が有効なサイトIDを取得します。
     *
     * @return 購読が有効なサイトIDリスト
     */
    public static List<Integer> findEnableSiteIds() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<SiteTableDto> query = realm.where(SiteTableDto.class).equalTo("enable", true).findAll();
        List<Integer> ids = new ArrayList<>();
        for (SiteTableDto site : query) {
            ids.add(site.getId());
        }
        realm.close();
        return ids;
    }

    /**
     * 指定した ID に一致するサイトを取得します。
     *
     * @param id ID
     * @return SiteTableDto
     */
    public static SiteTableDto findById(int id) {
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<SiteTableDto> query = realm.where(SiteTableDto.class).equalTo("id", id);
        realm.close();
        return query.findFirst();
    }
}
