package jp.takeru.kowaihanashitai.db.dto;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import jp.takeru.kowaihanashitai.dto.FeedDto;
import jp.takeru.kowaihanashitai.dto.SiteDto;

/**
 * まとめサイトテーブルDTO.
 */
public class SiteTableDto extends RealmObject {

    /** id */
    @PrimaryKey
    private int id;
    /** サイト名 */
    private String siteName;
    /** ON/OFF */
    private boolean enable;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

}
