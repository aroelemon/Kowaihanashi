package jp.takeru.kowaihanashitai.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jp.takeru.kowaihanashitai.R;
import jp.takeru.kowaihanashitai.db.dao.MyListDao;
import jp.takeru.kowaihanashitai.dto.FeedDto;


/**
 * 新着フィードのアダプター.
 */
public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {


    /** インフレータ */
    private LayoutInflater inflater;
    /** コンテキスト */
    private Context context;
    /** リスナー */
    private FeedAdapterListener listener;
    /** 項目 */
    private List<FeedDto.Feed> items;

    /** 閲覧済みのID */
    private List<Integer> historyIds = new ArrayList<>();


    public FeedAdapter(Context context, FeedAdapterListener listener, List<FeedDto.Feed> items) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
        this.items = items;
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FeedViewHolder(inflater.inflate(R.layout.fragment_feed_list_item, parent, false), this);
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {
        FeedDto.Feed item = items.get(position);
        // タイトル
        holder.titleTextView.setText(item.title);
        if (historyIds.contains(item.id)) { // 閲覧済み
            holder.titleTextView.setTextColor(context.getResources().getColor(R.color.color_already_read));
        } else {
            holder.titleTextView.setTextColor(context.getResources().getColor(R.color.color_not_read));
        }
        // お気に入り
        if (MyListDao.existFeedById(item.id)) {   // お気に入り登録済み
            holder.starImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_full));
        } else {
            holder.starImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_empty));
        }
        holder.starImageView.setTag(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    /**
     * 閲覧済みのIDをセットします。
     *
     * @param id 閲覧済みID
     */
    public void setHistoryId(Integer id) {
        historyIds.add(id);
    }

    /**
     * 項目を追加します。
     *
     * @param feeds 項目
     */
    public void addAll(List<FeedDto.Feed> feeds) {
        items.addAll(feeds);
        notifyDataSetChanged();
    }

    /**
     * 項目をすべてクリアしてから項目を追加します。
     *
     * @param feed 項目
     */
    public void swap(List<FeedDto.Feed> feed) {
        items.clear();
        items.addAll(feed);
        notifyDataSetChanged();
    }


    public void onItemClick(int position) {
        listener.onItemClick(items.get(position));
//        if (v instanceof ImageView) {   // スター
//            FeedDto.Feed item = (FeedDto.Feed) v.getTag();
//            if (!MyListDao.existFeedById(item.id)) {   //　未登録
//                // マイリストに追加
//                MyListDao.add(item);
//                ((ImageView) v).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_full));
//            } else {
//                // マイリストから削除
//                MyListDao.deleteById(item.id);
//                ((ImageView) v).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_empty));
//            }
//
//        }
    }


    /**
     * FeedViewHolder
     */
    public static class FeedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private FeedAdapter adapter;
        /** タイトル */
        public TextView titleTextView;
        /** スター */
        private ImageView starImageView;

        public FeedViewHolder(View itemView, FeedAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            itemView.setOnClickListener(this);
            titleTextView = (TextView) itemView.findViewById(R.id.fragment_feed_list_item_title);
            starImageView = (ImageView) itemView.findViewById(R.id.fragment_feed_list_item_favorite_imageview);
        }

        @Override
        public void onClick(View v) {
            adapter.onItemClick(getLayoutPosition());
        }
    }

    /**
     * スクロールした際に呼ばれるリスナー.
     */
    public abstract class OnScrollListener extends RecyclerView.OnScrollListener {

        private int firstVisibleItem, visibleItemCount, totalItemCount;
        private int visibleThreshold = 0;
        private int previousTotal = 0;
        private boolean loading = true;

        private LinearLayoutManager mLinearLayoutManager;

        public OnScrollListener(LinearLayoutManager linearLayoutManager) {
            this.mLinearLayoutManager = linearLayoutManager;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            visibleItemCount = recyclerView.getChildCount();
            totalItemCount = mLinearLayoutManager.getItemCount();
            firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                }
            }

            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                listener.onLoadMore();
                loading = true;
            }
        }
    }

    /**
     * 項目がタップされた時にコールバックするリスナー
     */
    public interface FeedAdapterListener {

        /**
         * 項目がタップされた際に呼び出されます。
         *
         * @param feed タップされた項目
         */
        void onItemClick(FeedDto.Feed feed);

        /**
         * 項目を更に追加できる際に呼び出されます。
         */
        void onLoadMore();
    }
}
