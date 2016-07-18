package jp.takeru.kowaihanashitai.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.takeru.kowaihanashitai.adapter.FeedAdapter;
import jp.takeru.kowaihanashitai.adapter.listener.EndlessRecyclerOnScrollListener;
import jp.takeru.kowaihanashitai.db.dao.HistoryDao;
import jp.takeru.kowaihanashitai.db.dto.HistoryDto;
import jp.takeru.kowaihanashitai.dto.FeedDto;
import jp.takeru.kowaihanashitai.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import jp.takeru.kowaihanashitai.parser.NewArrivalParser;


/**
 * 新着フィードフラグメント
 */
public class NewArrivalsFragment extends Fragment implements FeedAdapter.FeedAdapterListener {

    /** ListView */
    private RecyclerView recyclerView;
    /** ProgressBar */
    private ProgressBar progressBar;
    /** アダプター */
    private FeedAdapter adapter;
    /** リスナー */
    OnItemClickListener listener;

    /** Feed取得タスク */
    private FeedTask feedTask;
    /** ページ */
    private int page = 1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_arrivals, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listener = (OnItemClickListener) getActivity();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = (ProgressBar) view.findViewById(R.id.fragment_new_arrivals_progressbar);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_new_arrivals_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new FeedAdapter(getActivity(), this, new ArrayList<FeedDto.Feed>(0));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener((LinearLayoutManager) recyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore(int current_page) {
                requestApi();
            }
        });
        requestApi();
    }

    @Override
    public void onStart() {
        super.onStart();
        List<HistoryDto> historyDaoList = HistoryDao.findAll();
        for (HistoryDto historyDto : historyDaoList) {
            adapter.setHistoryId(historyDto.getId());
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * Feed取得APIにリクエストします。
     */
    private void requestApi() {
        feedTask = new FeedTask();
        StringBuilder apiUrl = new StringBuilder(getString(R.string.url_new_arrival_api));
        apiUrl.append("?token=aaaaaaaadsafsgeg/efdsofpw42-3r^a.&page=");
        apiUrl.append(String.valueOf(page));
        feedTask.execute(apiUrl.toString());
    }

    /**
     * コンテンツのセットアップをします.
     *
     * @param feedDto FeedDto
     */
    private void setupContent(FeedDto feedDto) {
        adapter.addAll(feedDto.feeds);
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(FeedDto.Feed feed) {
        listener.onItemClick(feed);
    }

    @Override
    public void onLoadMore() {
        requestApi();
    }

    /**
     * Feed取得のタスク
     */
    private class FeedTask extends AsyncTask<String, Void, FeedDto> {

        @Override
        protected FeedDto doInBackground(String... params) {

            OkHttpClient client = new OkHttpClient();
            FeedDto feedDto;
            try {
                Request request = new Request.Builder().url(params[0]).build();
                Response response = client.newCall(request).execute();
                feedDto = NewArrivalParser.parse(response.body().string());
            } catch (IOException e) {
                return null;
            }
            return feedDto;
        }

        @Override
        protected void onPostExecute(FeedDto feedDto) {

            if (feedDto == null) {  // エラー
                // 再試行ボタン表示
                Toast.makeText(getActivity(), "通信エラー", Toast.LENGTH_SHORT);
            } else {
                page++;
                // 項目のセットアップ
                setupContent(feedDto);
            }
        }
    }

    /**
     * 項目がタップされた時にコールバックするリスナー
     */
    public interface OnItemClickListener {

        /**
         * 項目がタップされた際に呼び出されます。
         *
         * @param feed タップされた項目
         */
        void onItemClick(FeedDto.Feed feed);
    }
}
