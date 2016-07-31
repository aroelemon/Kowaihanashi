package jp.takeru.kowaihanashitai.db.dto;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 閲覧履歴DTO.
 */
public class HistoryTableDto extends RealmObject {

    /** id */
    @PrimaryKey
    private int id;
    /** サイトid */
    private int siteId;
    /** タイトル */
    private String title;
    /** URL */
    private String url;
    /** 閲覧日 */
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
