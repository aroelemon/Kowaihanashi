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

    /**
     * ディスプレイタイプ
     */
    public static class DisplayType {
        /** 新着一覧 */
        public static int NEW_ARRIVAL = 1;
        /** お気に入り一覧 */
        public static int MY_LIST = 2;
    }


    /** インフレータ */
    private LayoutInflater inflater;
    /** コンテキスト */
    private Context context;
    /** リスナー */
    private FeedAdapterListener listener;
    /** 項目 */
    private List<FeedDto.Feed> items;
    /** ディスプレイタイプ */
    private int displayType;
    /** 閲覧済みのID */
    private List<Integer> historyIds = new ArrayList<>();


    /**
     * コンストラクタ
     *
     * @param context     　コンテキスト
     * @param listener    　リスナー
     * @param items       　項目
     * @param displayType 　ディスプレイタイプ
     */
    public FeedAdapter(Context context, FeedAdapterListener listener, List<FeedDto.Feed> items, int displayType) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
        this.items = items;
        this.displayType = displayType;
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FeedViewHolder(inflater.inflate(R.layout.fragment_feed_list_item, parent, false), this, displayType);
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
        // サイト名
        holder.siteNameTextView.setText(item.siteName);
        // 公開日
        holder.dateTextView.setText(item.date);
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


    /**
     * 項目がタップされた時のコールバック.
     *
     * @param feed FeedDTO
     */
    public void onItemClick(FeedDto.Feed feed) {
        listener.onItemClick(feed);
    }

    /**
     * 削除ボタンが押された時のコールバック.
     *
     * @param feed FeedDTO
     */
    public void onDeleteClick(FeedDto.Feed feed) {
        MyListDao.deleteById(feed.id);
        items.remove(feed);
        notifyDataSetChanged();
    }


    /**
     * FeedViewHolder
     */
    public static class FeedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private FeedAdapter adapter;
        /** タイトル */
        public TextView titleTextView;
        /** サイト名 */
        public TextView siteNameTextView;
        /** 日付 */
        public TextView dateTextView;
        /** 削除ボタン（お気に入り） */
        public ImageView deleteButton;

        public FeedViewHolder(View itemView, FeedAdapter adapter, int displayType) {
            super(itemView);
            this.adapter = adapter;
            itemView.setOnClickListener(this);
            titleTextView = (TextView) itemView.findViewById(R.id.fragment_feed_list_item_title_textview);
            siteNameTextView = (TextView) itemView.findViewById(R.id.fragment_feed_list_item_sitename_textview);
            dateTextView = (TextView) itemView.findViewById(R.id.fragment_feed_list_item_date_textview);
            deleteButton = (ImageView) itemView.findViewById(R.id.fragment_feed_list_item_delete_imageview);
            if (DisplayType.MY_LIST == displayType) {
                deleteButton.setVisibility(View.VISIBLE);
                deleteButton.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            if (v.equals(deleteButton)) {   // 削除ボタン
                adapter.onDeleteClick(adapter.items.get(getLayoutPosition()));
            } else {
                adapter.onItemClick(adapter.items.get(getLayoutPosition()));
            }

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
