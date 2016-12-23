package com.example.shize.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.shize.adapter.PlayerAdapter;
import com.example.shize.dao.PlayerDao;
import com.example.shize.dialog.MessageDialog;
import com.example.shize.entity.MP3File;
import com.example.shize.fragment.R;
import com.example.shize.service.DBHelper;
import com.example.shize.service.MusicPlayerService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.shize.util.PlayerUtil.DialogUtil.setHashMap;
import static com.example.shize.util.PlayerUtil.DialogUtil.getMessageList;

/**
 * 我喜欢的音乐界面
 * Created by shize on 2016/11/17.
 */
public class IndexLikeMusicActivity extends TransparentActivity {
    private final static String TAG = "IndexLikeMusicActivity";
    private PlayerDao.MusicDao.LikeMusicDao musicService;
    private List<MP3File> mp3Files;
    private PlayerAdapter.MusicListAdapter musicListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_music_page_index_like);
        initHeader();
        musicService = new DBHelper.MusicService(this, "Media.db", 1);
        // 初始化歌曲列表
        initListView();
    }

    /**
     * 初始化标题栏
     */
    private void initHeader(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        assert toolbar != null;
        toolbar.setTitle(getString(R.string.main_index_like_music_text));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_exit);
        toolbar.setNavigationOnClickListener(new OnBackClickListener());
    }

    /**
     * 初始化喜欢的歌曲列表
     */
    private void initListView() {
        Log.i(TAG, "initListView: 执行喜欢的列表初始化！！！");
        ListView likeMusic = (ListView) findViewById(R.id.fragment_my_music_index_like_list);
        mp3Files = musicService.findAllLikeMusic();
        Log.i(TAG, "initListView: 找到了"+ mp3Files.size()+"个数据！！！");
        musicListAdapter = new PlayerAdapter.MusicListAdapter(this, mp3Files);
        musicListAdapter.setMenuButtonClickListener(new OnMenuClickListener());
        assert likeMusic != null;
        likeMusic.setAdapter(musicListAdapter);
        likeMusic.setOnItemClickListener(new onLikeListItemClickListener());
    }

    /**
     * 返回按钮点击事件监听类
     */
    private class OnBackClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            IndexLikeMusicActivity.this.finish();
        }
    }

    /**
     * 点击列表子项
     */
    private class onLikeListItemClickListener
            implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // 先添加进播放列队
            MP3File mp3File = mp3Files.get(position);
            musicService.addPlayList(mp3File);
            // 开始播放
            Intent intent = new Intent(IndexLikeMusicActivity.this, MusicPlayerService.class);
            intent.setAction(MusicPlayerService.ACTION_PLAY);
            intent.putExtra("position", String.valueOf(musicService.getPlayListPosition(
                    mp3File.getUrl())));
            startService(intent);
        }
    }

    /**
    * 菜单点击事件监听类
    */
    private class OnMenuClickListener
            implements PlayerDao.ListButtonClickListener.MenuButtonClickListener {
        private MP3File mp3File;

        @Override
        public void onMenuClickListener(int position) {
            mp3File = mp3Files.get(position);
            PlayerAdapter.DialogListAdapter adapter = new PlayerAdapter.DialogListAdapter(
                    IndexLikeMusicActivity.this,
                    getListData());
            MessageDialog.Builder dialog = new MessageDialog.Builder(IndexLikeMusicActivity.this,
                    adapter, mp3File.getTitle());
            dialog.setOnDialogListItemClickListener(new OnMenuClickListener.DialogListItemClickListener());
            dialog.create().show();
        }

        private List<HashMap<String, Object>> getListData() {
            List<HashMap<String, Object>> data = new ArrayList<>();
            data.add(setHashMap(getString(R.string.dialog_next), R.drawable.dialog_icon_next));
            data.add(setHashMap(getString(R.string.dialog_delete), R.drawable.dialog_icon_delete));
            data.add(setHashMap(getString(R.string.dialog_artist) + mp3File.getArtist(),
                    R.drawable.dialog_icon_artist));
            data.add(setHashMap(getString(R.string.dialog_album) + mp3File.getAlbum(),
                    R.drawable.dialog_icon_album));
            data.add(setHashMap(getString(R.string.dialog_info), R.drawable.dialog_icon_info));
            return data;
        }

        private class DialogListItemClickListener implements PlayerDao.DialogListItemDao {
            @Override
            public void onDialogListItemClickListener(int position) {
                Log.i(TAG, "onDialogItemClick: " + position);
                switch (position) {
                    case 0:
                        // 添加播放列表并在当前歌曲之后
                        musicService.addPlayList(mp3File);
                        break;
                    case 1:
                        // 删除歌曲信息
                        musicService.deleteLikeMusic(mp3File.getUrl());
                        mp3Files.remove(mp3File);
                        musicListAdapter.notifyDataSetChanged();
                        break;
                    case 2:
                        // 搜索本地该歌手的其他歌曲
                        break;
                    case 3:
                        // 搜索本地该专辑的其他歌曲
                        break;
                    case 4:
                        // 查看歌曲详细信息
                        PlayerAdapter.DialogMessageAdapter adapter = new PlayerAdapter.DialogMessageAdapter(
                                IndexLikeMusicActivity.this, getMessageList(
                                IndexLikeMusicActivity.this, mp3File));
                        MessageDialog.Builder dialog = new MessageDialog.Builder(
                                IndexLikeMusicActivity.this, adapter, mp3File.getTitle());
                        dialog.create().show();
                        break;
                }
            }
        }
    }
}
