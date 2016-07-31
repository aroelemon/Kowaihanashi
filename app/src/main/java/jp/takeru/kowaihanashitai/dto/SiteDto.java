package jp.takeru.kowaihanashitai.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * まとめサイトDTO.
 */
public class SiteDto {

    @SerializedName("sites")
    public List<Site> sites;

    public static class Site {
        /** id */
        @SerializedName("id")
        public int id;
        /** サイト名 */
        @SerializedName("name")
        public String siteName;
        /** ON/OFF */
        public boolean enable;
    }

}
