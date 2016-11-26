package com.example.shize.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.shize.dao.ListButtonClickListener;
import com.example.shize.entity.MP3File;
import com.example.shize.fragment.R;

import java.util.List;

/**
 * 播放列队适配器
 * Created by shize on 2016/11/20.
 */
public class MusicPlayListAdapter extends BaseAdapter {

    private List<MP3File> mp3Files;
    private LayoutInflater mInflater;
    private ListButtonClickListener listButtonClickListener;

    public MusicPlayListAdapter(Context context, List<MP3File> mp3Files) {
        this.mp3Files = mp3Files;
        mInflater = LayoutInflater.from(context);
    }

    /**
     * 删除按钮点击事件接口
     * @param listButtonClickListener 删除按钮单击接口
     */
    public void setListButtonClickListener(ListButtonClickListener listButtonClickListener){
        this.listButtonClickListener = listButtonClickListener;
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
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.activity_play_list_main_item, null);
            viewHolder.musicName = (TextView) convertView.findViewById(R.id.play_list_item_music_name);
            viewHolder.singerName = (TextView) convertView.findViewById(R.id.play_list_item_singer_name);
            viewHolder.deleteMusic = (ImageButton) convertView.findViewById(R.id.play_list_delete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.musicName.setText(mp3Files.get(position).getTitle());
        viewHolder.singerName.setText(mp3Files.get(position).getArtist());
        // 解决按钮拦截父容器的点击事件
        viewHolder.deleteMusic.setFocusable(false);
        viewHolder.deleteMusic.setOnClickListener(new OnDeleteClickListener(position));
        return convertView;
    }

    private final class ViewHolder{
        private TextView musicName;
        private TextView singerName;
        private ImageButton deleteMusic;
    }

    /**
     * 删除按钮点击事件
     */
    private class OnDeleteClickListener implements View.OnClickListener {
        private int position;

        private OnDeleteClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            listButtonClickListener.onDeleteClick(mp3Files.get(position).getUrl(), position);
        }
    }
}
