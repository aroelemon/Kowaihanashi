package jp.takeru.kowaihanashitai.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * WebViewアクティビティのParamDto
 */
public class WebViewParamDto implements Parcelable {


    /** id */
    public int id;
    /** サイトid */
    public int siteId;
    /** タイトル */
    public String title;
    /** サイト名 */
    public String siteName;
    /** URL */
    public String url;
    /** 投稿日 */
    public String date;

    public static final Parcelable.Creator<WebViewParamDto> CREATOR
            = new Parcelable.Creator<WebViewParamDto>() {
        public WebViewParamDto createFromParcel(Parcel in) {
            return new WebViewParamDto(in);
        }

        public WebViewParamDto[] newArray(int size) {
            return new WebViewParamDto[size];
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

    private WebViewParamDto(Parcel in) {
        id = in.readInt();
        siteId = in.readInt();
        title = in.readString();
        siteName = in.readString();
        url = in.readString();
        date = in.readString();
    }

    public WebViewParamDto() {
        // empty
    }
}
