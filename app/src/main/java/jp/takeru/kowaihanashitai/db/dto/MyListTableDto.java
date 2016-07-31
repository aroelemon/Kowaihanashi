package jp.takeru.kowaihanashitai.db.dto;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import jp.takeru.kowaihanashitai.dto.FeedDto;


/**
 * お気に入りDTO.
 */
public class MyListTableDto extends RealmObject {

    /** id */
    @PrimaryKey
    private int id;
    /** サイトid */
    private int siteId;
    /** タイトル */
    private String title;
    /** サイト名 */
    private String siteName;
    /** URL */
    private String url;
    /** 登録日 */
    private String date;

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

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

    public FeedDto.Feed getFeed() {
        FeedDto.Feed feed = new FeedDto.Feed();
        feed.id = id;
        feed.siteId = siteId;
        feed.title = title;
        feed.url = url;
        feed.siteName = siteName;
        feed.date = date;
        return feed;
    }
}
