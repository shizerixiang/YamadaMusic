package com.example.shize.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.shize.adapter.MusicPlayListAdapter;
import com.example.shize.dao.ListButtonClickListener;
import com.example.shize.dao.MusicDao;
import com.example.shize.entity.MP3File;
import com.example.shize.fragment.R;
import com.example.shize.service.MusicPlayerService;
import com.example.shize.service.dbservice.MusicService;

import java.util.List;

/**
 * 播放列队
 * Created by shize on 2016/11/19.
 */

public class PlayListActivity extends Activity {
    private MusicDao.PlayListMusicDao playListMusicDao;
    private List<MP3File> mp3Files;
    private MusicPlayListAdapter adapter;
    private final static String TAG = "playList";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list_main);
        initWindow();
        playListMusicDao = new MusicService(this,"Media.db",1);
        initPlayList();
    }

    /**
     * 初始化列表
     */
    private void initPlayList() {
        Button clearBtn = (Button) findViewById(R.id.play_list_clear);
        clearBtn.setOnClickListener(new OnClearClickListener());

        ListView playList = (ListView) findViewById(R.id.play_list_list_view);
        mp3Files = playListMusicDao.findAllPlayMusic();
        adapter = new MusicPlayListAdapter(this, mp3Files);
        adapter.setListButtonClickListener(new ListDeleteClickListener());
        playList.setAdapter(adapter);
        Log.i(TAG, "initPlayList: 初始化之前！！！");
        playList.setOnItemClickListener(new OnPlayListClickListener());
        Log.i(TAG, "initPlayList: 初始化之后！！！");
    }

    /**
     * 初始化对话框大小和位置
     */
    private void initWindow() {
        Window window = getWindow();
        window.getDecorView().setPadding(0,0,0,0);
        window.setWindowAnimations(R.style.DialogAnimation);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.BOTTOM;
        window.setAttributes(layoutParams);
    }

    /**
     * 监听播放列表子项点击事件
     */
    private class OnPlayListClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // 点击事件
            Log.i("playList", "onItemClick: 触发了事件！！！");
            Intent intent = new Intent(PlayListActivity.this, MusicPlayerService.class);
            intent.setAction(MusicPlayerService.ACTION_PLAY);
            intent.putExtra("position", String.valueOf(position));
            startService(intent);
        }
    }

    /**
     * 播放列队删除按钮点击事件监听类
     */
    private class ListDeleteClickListener implements ListButtonClickListener {
        @Override
        public void onDeleteClick(String url, int position) {
            Log.i(TAG, "onDeleteClick: 点击删除按钮！！！");
            // 删除表中的音乐数据
            playListMusicDao.deletePlayMusic(url);
            // 重新启动播放服务
            Intent intent = new Intent(PlayListActivity.this, MusicPlayerService.class);
            intent.setAction(MusicPlayerService.ACTION_PLAY);
            startService(intent);
            // 更新列表
            mp3Files.remove(position);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 清空按钮点击事件监听类
     */
    private class OnClearClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.i(TAG, "onClick: 清空播放列表！！！");
            // 清空表中数据
            playListMusicDao.clearPlayList();
            // 停止播放服务
            Intent intent = new Intent(PlayListActivity.this,MusicPlayerService.class);
            intent.setAction(MusicPlayerService.ACTION_STOP);
            startService(intent);
            // 更新列表
            mp3Files.clear();
            adapter.notifyDataSetChanged();
        }
    }
}
