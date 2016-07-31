package jp.takeru.kowaihanashitai.adapter;

import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import jp.takeru.kowaihanashitai.R;
import jp.takeru.kowaihanashitai.db.dao.SiteDao;
import jp.takeru.kowaihanashitai.dto.SiteDto;

/**
 * まとめサイトのアダプター.
 */
public class SitesAdapter extends ArrayAdapter<SiteDto.Site> implements View.OnClickListener {


    public SitesAdapter(Context context, List<SiteDto.Site> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(getContext()).inflate(R.layout.settings_site_listitem, null);
            holder.siteNameTextView = (TextView) view.findViewById(R.id.settings_site_listitem_site_name_textview);
            holder.feedSwitch = (SwitchCompat) view.findViewById(R.id.settings_site_listitem_switch);
            holder.feedSwitch.setOnClickListener(this);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        SiteDto.Site item = getItem(position);
        holder.siteNameTextView.setText(item.siteName);
        holder.feedSwitch.setChecked(item.enable);
        holder.feedSwitch.setTag(item);

        return view;
    }

    @Override
    public void onClick(View v) {
        // 購読の設定
        SiteDto.Site site = (SiteDto.Site) v.getTag();
        SiteDao.updateEnable(site.id, !site.enable);
    }

    /**
     * ViewHolder.
     */
    private static class ViewHolder {
        /** サイト名 */
        public TextView siteNameTextView;
        /** 推知 */
        public SwitchCompat feedSwitch;
    }
}
