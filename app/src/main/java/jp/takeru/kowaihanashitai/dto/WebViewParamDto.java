package jp.takeru.kowaihanashitai.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * WebViewアクティビティのParamDto
 */
public class WebViewParamDto implements Parcelable {

    /** URL */
    public String url;
    /** タイトル */
    public String title;

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
        out.writeString(url);
        out.writeString(title);
    }

    private WebViewParamDto(Parcel in) {
        url = in.readString();
        title = in.readString();
    }

    public WebViewParamDto() {
        // empty
    }
}
