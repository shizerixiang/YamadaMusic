package com.example.shize.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.shize.adapter.MusicListAdapter;
import com.example.shize.dao.MusicDao;
import com.example.shize.entity.MP3File;
import com.example.shize.fragment.R;
import com.example.shize.service.MusicPlayerService;
import com.example.shize.service.dbservice.MusicService;
import com.example.shize.util.uiutil.DialogUtil;

import java.util.List;

/**
 * 我喜欢的音乐界面
 * Created by shize on 2016/11/17.
 */
public class IndexLikeMusicActivity extends AppCompatActivity {
    private MusicDao.LikeMusicDao musicService;
    private List<MP3File> mp3Files;
    private MusicListAdapter musicListAdapter;
    private final static String TAG = "IndexLikeMusicActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_music_page_index_like);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        musicService = new MusicService(this, "Media.db", 1);
        // 初始化歌曲列表
        initListView();
    }

    /**
     * 初始化喜欢的歌曲列表
     */
    private void initListView() {
        Log.i(TAG, "initListView: 执行喜欢的列表初始化！！！");
        ListView likeMusic = (ListView) findViewById(R.id.fragment_my_music_index_like_list);
        mp3Files = musicService.findAllLikeMusic();
        Log.i(TAG, "initListView: 找到了"+ mp3Files.size()+"个数据！！！");
        musicListAdapter = new MusicListAdapter(this, mp3Files);
        assert likeMusic != null;
        likeMusic.setAdapter(musicListAdapter);
        likeMusic.setOnItemClickListener(new onLikeListItemClickListener());
        likeMusic.setOnItemLongClickListener(new onLikeListItemLongClickListener());
    }

    /**
     * 返回按钮
     * @param item actionBar子项
     * @return 是否拦截
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    /**
     * 点击列表子项
     */
    private class onLikeListItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // 先添加进播放列队
            MP3File mp3File = mp3Files.get(position);
            musicService.addPlayList(mp3File);
            // 开始播放
            Intent intent = new Intent(IndexLikeMusicActivity.this, MusicPlayerService.class);
            intent.setAction(MusicPlayerService.ACTION_PLAY);
            intent.putExtra("position", String.valueOf(musicService.getPlayListPosition(mp3File.getUrl())));
            startService(intent);
        }
    }

    /**
     * 长按列表子项
     */
    private class onLikeListItemLongClickListener implements AdapterView.OnItemLongClickListener {
        private MP3File mp3File;
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            mp3File = mp3Files.get(position);

            AlertDialog.Builder dialog = new AlertDialog.Builder(IndexLikeMusicActivity.this);
            dialog.setTitle(R.string.music_handle_menu);
            dialog.setItems(new String[]{getString(R.string.music_handle_menu_delete),
                    getString(R.string.music_handle_menu_message)},new onItemClickListener());
            dialog.show();
            return true;
        }

        /**
         * 弹窗子项点击事件监听类
         */
        private class onItemClickListener implements DialogInterface.OnClickListener {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "onDialogItemClick: "+which);
                switch (which){
                    case 0:
                        musicService.deleteLikeMusic(mp3File.getUrl());
                        mp3Files.remove(mp3File);
                        musicListAdapter.notifyDataSetChanged();
                        break;
                    case 1:
                        DialogUtil.showMusicMessage(IndexLikeMusicActivity.this, mp3File);
                        break;
                }
            }
        }
    }
}
