package jp.takeru.kowaihanashitai.parser;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import jp.takeru.kowaihanashitai.dto.FeedDto;

/**
 * 新着フィードのパーサー
 */
public class NewArrivalParser {

    /**
     * 新着APIをFeedDtoにパースします。
     *
     * @return FeedDto
     */
    public static FeedDto parse(String response) {

        Gson gson = new Gson();
        FeedDto feedDto;

        try {

            feedDto = gson.fromJson(response.toString(), FeedDto.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }

        return feedDto;
    }
}
