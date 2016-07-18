package jp.takeru.kowaihanashitai.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import jp.takeru.kowaihanashitai.db.dao.MyListDao;
import jp.takeru.kowaihanashitai.db.dto.MyListDto;

/**
 * FeedDto.
 */
public class FeedDto {

    /** ステータス */
    @SerializedName("status")
    public String status;
    /** feeds */
    @SerializedName("feeds")
    public List<Feed> feeds;
    /** 情報 */
    @SerializedName("pagenate")
    public Pagenate pagenate;

    public class Feed {
        /** id */
        @SerializedName("id")
        public int id;
        /** サイトid */
        @SerializedName("site_id")
        public int siteId;
        /** タイトル */
        @SerializedName("title")
        public String title;
        /** URL */
        @SerializedName("url")
        public String url;
        /** 投稿日 */
        @SerializedName("publish_at")
        public String date;
    }

    public class Pagenate {
        /** 総項目数 */
        @SerializedName("total_count")
        public int totalCount;
        /** 総ページ数 */
        @SerializedName("total_page")
        public int totalPage;
        /** 現在のページ */
        @SerializedName("current_page")
        public int current_page;
        /** 最後のページかどうか */
        @SerializedName("is_last_page")
        public boolean isLastPage;
        /** 最初のページかどうか */
        @SerializedName("is_first_page")
        public boolean is_first_page;
    }

}
