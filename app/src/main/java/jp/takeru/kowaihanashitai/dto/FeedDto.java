package jp.takeru.kowaihanashitai.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

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

    public static class Feed implements Parcelable {
        /** id */
        @SerializedName("id")
        public int id;
        /** サイトid */
        @SerializedName("site_id")
        public int siteId;
        /** タイトル */
        @SerializedName("title")
        public String title;
        /** サイト名 */
        @SerializedName("site_name")
        public String siteName;
        /** URL */
        @SerializedName("url")
        public String url;
        /** 投稿日 */
        @SerializedName("publish_at")
        public String date;


        public static final Parcelable.Creator<Feed> CREATOR
                = new Parcelable.Creator<Feed>() {
            public Feed createFromParcel(Parcel in) {
                return new Feed(in);
            }

            public Feed[] newArray(int size) {
                return new Feed[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            out.writeInt(id);
            out.writeInt(siteId);
            out.writeString(title);
            out.writeString(siteName);
            out.writeString(url);
            out.writeString(date);

        }

        private Feed(Parcel in) {
            id = in.readInt();
            siteId = in.readInt();
            title = in.readString();
            siteName = in.readString();
            url = in.readString();
            date = in.readString();
        }

        public Feed() {
            // empty
        }
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
