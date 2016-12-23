package com.example.shize.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shize.dao.PlayerDao;
import com.example.shize.entity.MP3File;
import com.example.shize.fragment.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 音乐播放器适配器
 * Created by shize on 2016/12/13.
 */

public class PlayerAdapter {
    /**
     * 播放列队适配器
     * Created by shize on 2016/11/20.
     */
    public static class MusicPlayListAdapter extends BaseAdapter {

        private List<MP3File> mp3Files;
        private LayoutInflater mInflater;
        private PlayerDao.ListButtonClickListener listButtonClickListener;

        public MusicPlayListAdapter(Context context, List<MP3File> mp3Files) {
            this.mp3Files = mp3Files;
            mInflater = LayoutInflater.from(context);
        }

        /**
         * 删除按钮点击事件接口
         * @param listButtonClickListener 删除按钮单击接口
         */
        public void setListButtonClickListener(PlayerDao.ListButtonClickListener listButtonClickListener){
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

    /**
     * 歌曲列表适配器
     * Created by shize on 2016/11/17.
     */
    public static class MusicListAdapter extends BaseAdapter {

        private List<MP3File> mp3Files;
        private LayoutInflater mInflater;
        private PlayerDao.ListButtonClickListener.MenuButtonClickListener menuButtonClickListener;

        public MusicListAdapter(Context context, List<MP3File> mp3Files) {
            this.mp3Files = mp3Files;
            mInflater = LayoutInflater.from(context);
        }

        /**
         * 设置listView子项菜单按钮点击事件
         * @param menuButtonClickListener 点击事件
         */
        public void setMenuButtonClickListener(
                PlayerDao.ListButtonClickListener.MenuButtonClickListener menuButtonClickListener){
            this.menuButtonClickListener = menuButtonClickListener;
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
                viewHolder.action = (ImageButton) convertView.findViewById(R.id.my_music_local_item_action);
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
            viewHolder.action.setFocusable(false);
            viewHolder.action.setOnClickListener(new OnItemMenuClickListener(position));
            return convertView;
        }

        private final class ViewHolder {
            private TextView itemId;
            private TextView title;
            private TextView singer;
            private ImageButton action;
        }

        /**
         * 子项菜单按钮点击事件监听类
         */
        private class OnItemMenuClickListener implements View.OnClickListener {
            private int position;

            private OnItemMenuClickListener(int position){
                this.position = position;
            }

            @Override
            public void onClick(View v) {
                menuButtonClickListener.onMenuClickListener(position);
            }
        }
    }

    /**
     * 自定义tabLayout和pagerView适配器
     * Created by shize on 2016/11/10.
     */
    public static class MainTabAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        public MainTabAdapter(FragmentManager fm, ArrayList<Fragment> fragments,
                              ArrayList<String> titles) {
            super(fm);
            this.fragments = fragments;
            this.titles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return titles.size();
        }

        /**
         * 显示tab上的文字
         * @param position 当前tab位置
         * @return tab上的文字
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    /**
     * 我的音乐索引列表适配器
     * Created by shize on 2016/11/17.
     */
    public static class MainIndexListAdapter extends BaseAdapter {

        private List<HashMap<String,Object>> indexList;
        private String[] keys;
        private int[] values;
        private int itemId;
        private LayoutInflater mInflater;

        /**
         * 构造索引列表适配器
         * @param context 上下文
         * @param indexList 适配数据
         * @param itemId 适配列表子项id
         * @param keys 数据集合键
         * @param values 数据集合键对应控件的id
         */
        public MainIndexListAdapter(Context context,List<HashMap<String,Object>> indexList, int itemId,
                                   String[] keys, int[] values) {
            this.indexList = indexList;
            this.itemId = itemId;
            this.keys = keys;
            this.values = values;
            mInflater = LayoutInflater.from(context);
            Log.i("SearchMusic", "MainIndexListAdapter: construct");
        }

        @Override
        public int getCount() {
            return indexList.size();
        }

        @Override
        public Object getItem(int position) {
            return indexList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * 设置子项和其控件
         * @param position 子项位置
         * @param convertView 上一个子项
         * @param parent 父容器
         * @return 子项
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.i("SearchMusic", "MainIndexListAdapter: 开始获取视图");
            ViewHolder viewHolder;
            // 判断是否缓存
            if (convertView == null) {
                Log.i("SearchMusic", "MainIndexListAdapter: 开始缓存视图");
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(itemId,null);
                viewHolder.imageView = (ImageView) convertView.findViewById(values[0]);
                viewHolder.textView = (TextView) convertView.findViewById(values[1]);
                convertView.setTag(viewHolder);
            } else {
                Log.i("SearchMusic", "MainIndexListAdapter: 开始读取视图");
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.imageView.setImageResource((Integer) indexList.get(position).get(keys[0]));
            viewHolder.textView.setText((String)indexList.get(position).get(keys[1]));
            Log.i("SearchMusic", "MainIndexListAdapter: 完成视图获取");
            return convertView;
        }

        /**
         * 缓存机制，增加滑动流畅性
         */
        final class ViewHolder{
            ImageView imageView;
            TextView textView;
        }
    }

    /**
     * 弹出框列表适配器
     * Created by shize on 2016/12/13.
     */
    public static class DialogListAdapter extends BaseAdapter {

        private List<HashMap<String, Object>> mData;
        private LayoutInflater inflater;

        public DialogListAdapter(Context context, List<HashMap<String, Object>> mData) {
            this.mData = mData;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            // 是否进行缓存
            if (convertView == null){
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.dialog_menu_item, null);
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.dialog_menu_item_icon);
                viewHolder.textView = (TextView) convertView.findViewById(R.id.dialog_menu_item_name);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.imageView.setImageResource((Integer) mData.get(position).get("icon"));
            viewHolder.textView.setText((CharSequence) mData.get(position).get("name"));
            return convertView;
        }

        private final class ViewHolder{
            private ImageView imageView;
            private TextView textView;
        }
    }

    /**
     * messageDialog适配器
     * Created by shize on 2016/12/14.
     */
    public static class DialogMessageAdapter extends BaseAdapter {

        private List<HashMap<String, String>> mData;
        private LayoutInflater mInflater;

        public DialogMessageAdapter(Context context, List<HashMap<String, String>> mData) {
            this.mData = mData;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
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
                convertView = mInflater.inflate(R.layout.dialog_message_item, null);
                viewHolder.title = (TextView) convertView.findViewById(R.id.dialog_message_title);
                viewHolder.content = (TextView) convertView.findViewById(R.id.dialog_message_content);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.title.setText(mData.get(position).get("title"));
            viewHolder.content.setText(mData.get(position).get("content"));
            return convertView;
        }

        private final class ViewHolder{
            private TextView title;
            private TextView content;
        }
    }
}
