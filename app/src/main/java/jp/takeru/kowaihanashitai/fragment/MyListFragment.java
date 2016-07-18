package jp.takeru.kowaihanashitai.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import jp.takeru.kowaihanashitai.R;
import jp.takeru.kowaihanashitai.adapter.FeedAdapter;
import jp.takeru.kowaihanashitai.db.dao.MyListDao;
import jp.takeru.kowaihanashitai.dto.FeedDto;


/**
 * お気に入りリストフラグメント
 */
public class MyListFragment extends Fragment implements FeedAdapter.FeedAdapterListener {

    /** アダプター */
    private FeedAdapter adapter;
    /** リスナー */
    OnItemClickListener listener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mylist, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listener = (OnItemClickListener) getActivity();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fragment_mylist_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new FeedAdapter(getActivity(), this, new ArrayList<FeedDto.Feed>(0));
        recyclerView.setAdapter(adapter);
        refresh();
    }

    /**
     * 表示内容を更新します。
     */
    public void refresh() {
        List<FeedDto.Feed> feeds = MyListDao.findAll();
        adapter.swap(feeds);
    }

    @Override
    public void onItemClick(FeedDto.Feed feed) {
        listener.onItemClick(feed);
    }

    @Override
    public void onLoadMore() {
        // empty
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
        public void onItemClick(FeedDto.Feed feed);
    }
}
