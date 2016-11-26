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
import android.widget.Toast;

import com.example.shize.adapter.MusicListAdapter;
import com.example.shize.dao.MusicDao;
import com.example.shize.entity.MP3File;
import com.example.shize.fragment.R;
import com.example.shize.service.MusicPlayerService;
import com.example.shize.service.dbservice.MusicService;
import com.example.shize.util.uiutil.DialogUtil;

import java.util.List;

/**
 * 我的音乐界面
 * Created by shize on 2016/11/17.
 */
public class IndexLocalMusicActivity extends AppCompatActivity {
    private MusicDao.LocalMusicDao musicService;
    private List<MP3File> mp3Files;
    private MusicListAdapter musicListAdapter;
    private final static String TAG = "IndexLocalMusicActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_music_page_index_local);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.main_index_local_music_text));
        musicService = new MusicService(this, "Media.db", 1);
        initListView();
    }

    /**
     * 初始化本地歌曲列表
     */
    private void initListView() {
        Log.i(TAG, "initListView: 执行本地列表初始化！！！");
        ListView localMusic = (ListView) findViewById(R.id.fragment_my_music_index_local_list);
        mp3Files = musicService.findAllLocalMusic();
        Log.i(TAG, "initListView: 找到了"+ mp3Files.size()+"个数据！！！");
        musicListAdapter = new MusicListAdapter(this, mp3Files);
        assert localMusic != null;
        localMusic.setAdapter(musicListAdapter);
        localMusic.setOnItemClickListener(new onLocalListItemClickListener());
        localMusic.setOnItemLongClickListener(new onLocalListItemLongClickListener());
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
     * 音乐列表子项点击事件监听类
     */
    private class onLocalListItemClickListener
            implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // 先添加进播放列队
            MP3File mp3File = mp3Files.get(position);
            musicService.addPlayList(mp3File);
            // 开始播放
            Intent intent = new Intent(IndexLocalMusicActivity.this, MusicPlayerService.class);
            intent.setAction(MusicPlayerService.ACTION_PLAY);
            intent.putExtra("position", String.valueOf(musicService.getPlayListPosition(mp3File.getUrl())));
            startService(intent);
        }
    }

    /**
     * 歌曲列表子项长按事件监听类
     */
    private class onLocalListItemLongClickListener implements AdapterView.OnItemLongClickListener {
        private MP3File mp3File;
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            mp3File = mp3Files.get(position);
            String like = getString(R.string.music_add_like);
            if (mp3File.getIsLike() == 1) {
                like = getString(R.string.music_add_liked);
            }
            AlertDialog.Builder dialog = new AlertDialog.Builder(IndexLocalMusicActivity.this);
            dialog.setTitle(R.string.music_handle_menu);
            dialog.setItems(new String[]{like,getString(R.string.music_handle_menu_delete),
            getString(R.string.music_handle_menu_message)},new onItemClickListener());
            dialog.show();
            return true;
        }

        private class onItemClickListener implements DialogInterface.OnClickListener {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "onDialogItemClick: "+which);
                switch (which){
                    case 0:
                        if (mp3File.getIsLike() == 0) {
                            musicService.addLikeFromLocal(mp3File);
                            Toast.makeText(IndexLocalMusicActivity.this,R.string.success_add_like,
                                    Toast.LENGTH_SHORT).show();
                            Log.i(TAG, "onDialogItemClick: 添加进我喜欢列表！");
                        } else {
                            mp3File.setIsLike(0);
                            musicService.removeLikeFromLocal(mp3File.getUrl());
                            Toast.makeText(IndexLocalMusicActivity.this,R.string.success_remove_like,
                                    Toast.LENGTH_SHORT).show();
                            Log.i(TAG, "onDialogItemClick: 从我喜欢列表删除！");
                        }
                        break;
                    case 1:
                        musicService.deleteLocalMusic(mp3File.getId(), mp3File.getUrl());
                        mp3Files.remove(mp3File);
                        musicListAdapter.notifyDataSetChanged();
                        break;
                    case 2:
                        DialogUtil.showMusicMessage(IndexLocalMusicActivity.this, mp3File);
                        break;
                }
            }
        }
    }
}
