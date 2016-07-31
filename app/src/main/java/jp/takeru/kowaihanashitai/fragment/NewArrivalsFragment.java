package jp.takeru.kowaihanashitai.fragment;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.takeru.kowaihanashitai.BuildConfig;
import jp.takeru.kowaihanashitai.adapter.FeedAdapter;
import jp.takeru.kowaihanashitai.adapter.decorator.DividerItemDecoration;
import jp.takeru.kowaihanashitai.adapter.listener.EndlessRecyclerOnScrollListener;
import jp.takeru.kowaihanashitai.db.dao.HistoryDao;
import jp.takeru.kowaihanashitai.db.dao.SiteDao;
import jp.takeru.kowaihanashitai.db.dto.HistoryTableDto;
import jp.takeru.kowaihanashitai.dto.FeedDto;
import jp.takeru.kowaihanashitai.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import jp.takeru.kowaihanashitai.parser.NewArrivalParser;


/**
 * 新着フィードフラグメント
 */
public class NewArrivalsFragment extends Fragment implements FeedAdapter.FeedAdapterListener, View.OnClickListener {

    /** ListView */
    private RecyclerView recyclerView;
    /** ProgressBar */
    private ProgressBar progressBar;
    /** アダプター */
    private FeedAdapter adapter;
    /** 再試行レイアウト */
    private ViewGroup retryLayout;
    /** リスナー */
    private OnItemClickListener listener;

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

        // 再試行
        retryLayout = (ViewGroup) view.findViewById(R.id.fragment_new_arrivals_retry_layout);
        view.findViewById(R.id.fragment_new_arrivals_retry_button).setOnClickListener(this);
        // プログレスバー
        progressBar = (ProgressBar) view.findViewById(R.id.fragment_new_arrivals_progressbar);

        // 一覧
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_new_arrivals_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        adapter = new FeedAdapter(getActivity(), this, new ArrayList<FeedDto.Feed>(0), FeedAdapter.DisplayType.NEW_ARRIVAL);
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
        List<HistoryTableDto> historyDaoList = HistoryDao.findAll();
        for (HistoryTableDto historyTableDto : historyDaoList) {
            adapter.setHistoryId(historyTableDto.getId());
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * Feed取得APIにリクエストします。
     */
    private void requestApi() {
        feedTask = new FeedTask();
        Uri.Builder url = Uri.parse(getString(R.string.new_arrival_api_url)).buildUpon();
        url.appendQueryParameter("token", getString(R.string.new_arrival_api_token));
        url.appendQueryParameter("page", String.valueOf(page));
        List<Integer> ids = SiteDao.findEnableSiteIds();
        url.appendQueryParameter("site_id_in", TextUtils.join(",", ids));
        if (BuildConfig.DEBUG) {
            Log.d("#### Request URL ####", url.toString());
        }
        feedTask.execute(url.toString());
    }

    /**
     * コンテンツのセットアップをします.
     *
     * @param feedDto FeedDto
     */
    private void setupContent(FeedDto feedDto) {
        adapter.addAll(feedDto.feeds);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.fragment_new_arrivals_retry_button) {    // 再試行ボタン
            progressBar.setVisibility(View.VISIBLE);
            retryLayout.setVisibility(View.GONE);
            requestApi();
        }
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

            progressBar.setVisibility(View.GONE);

            if (feedDto == null) {  // エラー
                if (page == 1) {
                    // 再試行ボタン表示
                    retryLayout.setVisibility(View.VISIBLE);
                } else {
                    // 接続エラートースト
                    Toast.makeText(getContext(), "通信に失敗しました。", Toast.LENGTH_SHORT).show();
                }
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
