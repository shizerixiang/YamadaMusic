package com.example.shize.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shize.entity.MP3File;
import com.example.shize.fragment.R;

import java.util.List;

/**
 * 歌曲列表适配器
 * Created by shize on 2016/11/17.
 */
public class MusicListAdapter extends BaseAdapter {

    private List<MP3File> mp3Files;
    private LayoutInflater mInflater;

    public MusicListAdapter(Context context, List<MP3File> mp3Files) {
        this.mp3Files = mp3Files;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mp3Files.size();
    }

    @Override
    public Object getItem(int position) {
        return mp3Files.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i("SearchMusic", "getView: " + position);
        ViewHolder viewHolder;
        // 缓存机制，判断是否缓存
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.fragment_my_music_page_index_local_item, null);
            viewHolder.itemId = (TextView) convertView.findViewById(R.id.my_music_index_local_item_id);
            viewHolder.title = (TextView) convertView.findViewById(R.id.my_music_index_local_item_title);
            viewHolder.singer = (TextView) convertView.findViewById(R.id.my_music_index_local_item_singer);
            viewHolder.action = (ImageView) convertView.findViewById(R.id.my_music_local_item_action);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (position < 10) {
            viewHolder.itemId.setText("0" + (position + 1));
        } else {
            viewHolder.itemId.setText("" + (position + 1));
        }
        viewHolder.title.setText(mp3Files.get(position).getTitle());
        viewHolder.singer.setText(mp3Files.get(position).getArtist());
        viewHolder.action.setImageResource(R.drawable.music_action_icon);
        return convertView;
    }

    private final class ViewHolder {
        private TextView itemId;
        private TextView title;
        private TextView singer;
        private ImageView action;
    }
}
