package jp.takeru.kowaihanashitai.parser;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import jp.takeru.kowaihanashitai.dto.FeedDto;
import jp.takeru.kowaihanashitai.dto.SiteDto;

/**
 * まとめサイト取得APIのパーサー
 */
public class SitesParser {

    /**
     * まとめサイト取得APIをSiteDtoにパースします。
     *
     * @return FeedDto
     */
    public static SiteDto parse(String response) {

        Gson gson = new Gson();
        SiteDto siteDto;

        try {

            siteDto = gson.fromJson(response.toString(), SiteDto.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }

        return siteDto;
    }
}
