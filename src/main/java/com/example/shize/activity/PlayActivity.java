package com.example.shize.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shize.fragment.R;
import com.example.shize.service.MusicPlayerService;
import com.example.shize.util.uiutil.DialogUtil;
import com.example.shize.util.uiutil.ImageUtil;

/**
 * 播放音乐界面
 * Created by shize on 2016/11/14.
 */
public class PlayActivity extends AppCompatActivity {

    private static final String TAG="yamada_music_play";
    private Toolbar toolbar;
    private ImageButton playBtn;
    private ImageButton modeBtn;
    private MusicInfoReceiver receiver;
    private TextView allTimeTxt;
    private TextView positionTxt;
    private SeekBar timeBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 状态栏悬浮
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_play_main);
        // 初始化控件
        initContent();
    }

    /**
     * 初始化控件
     */
    private void initContent(){
        // 设置toolbar
        toolbar = (Toolbar) findViewById(R.id.play_header);
        // 需要设置好toolbar后才能给按钮设置点击事件
        setSupportActionBar(toolbar);
        assert toolbar != null;
        toolbar.setNavigationIcon(R.drawable.play_toolbar_return);
        toolbar.setNavigationOnClickListener(new OnBackClickListener());

        // 初始化播放列表按钮
        ImageButton playListBtn = (ImageButton) findViewById(R.id.play_under_music_list);
        assert playListBtn != null;
        playListBtn.setOnClickListener(new OnPlayListClickListener());

        // 初始化播放/暂停按钮
        playBtn = (ImageButton) findViewById(R.id.play_under_music_play);
        assert playBtn != null;
        // 设置播放/暂停按钮初始化图片
        playBtn.setImageResource(ImageUtil.getPlayImage());
        playBtn.setOnClickListener(new OnPlayClickListener());

        // 初始化上一首下一首按钮
        ImageButton nextBtn = (ImageButton) findViewById(R.id.play_under_music_next);
        assert nextBtn != null;
        nextBtn.setOnClickListener(new OnNextClickListener());
        ImageButton frontBtn = (ImageButton) findViewById(R.id.play_under_music_front);
        assert frontBtn != null;
        frontBtn.setOnClickListener(new OnFrontClickListener());

        // 初始化播放模式
        modeBtn = (ImageButton) findViewById(R.id.play_under_music_mode);
        assert modeBtn != null;
        modeBtn.setImageResource(ImageUtil.getModeImage());
        modeBtn.setOnClickListener(new OnModeClickListener());

        // 注册广播接收器
        receiver = new MusicInfoReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MusicPlayerService.BROADCAST_ACTION_RECEIVER);
        this.registerReceiver(receiver, filter);

        // 初始化时间信息
        allTimeTxt = (TextView) findViewById(R.id.play_under_music_time_all);
        positionTxt = (TextView) findViewById(R.id.play_under_music_time_now);
        timeBar = (SeekBar) findViewById(R.id.play_under_music_bar);
    }

    /**
     * 紧接着onCreate方法之后执行，适合在onCreate方法之后执行的初始化
     * @param savedInstanceState 保存参数
     */
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toolbar.setTitle(R.string.play_header_music_name);
        toolbar.setSubtitle(R.string.play_header_singer_name);
    }

    /**
     * 重写回退按键方法
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    /**
     * 头部返回按钮点击事件监听类
     */
    private class OnBackClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    }

    /**
     * 播放列表按钮点击事件监听类
     */
    private class OnPlayListClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(PlayActivity.this, PlayListActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 播放/暂停按钮点击事件监听类
     */
    private class OnPlayClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(PlayActivity.this, MusicPlayerService.class);
            // 判断播放器的状态
            if (MusicPlayerService.playerOpenState){
                // 暂停播放动作同时切换播放图片
                intent.setAction(MusicPlayerService.ACTION_PAUSE);
            } else {
                // 启动播放动作同时切换暂停图片
                intent.setAction(MusicPlayerService.ACTION_PLAY);
            }
            startService(intent);
            // 设置播放按钮图片
            playBtn.setImageResource(ImageUtil.getPlayImage(false));
        }
    }

    /**
     * 下一首按钮点击事件监听类
     */
    private class OnNextClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(PlayActivity.this, MusicPlayerService.class);
            intent.setAction(MusicPlayerService.ACTION_NEXT);
            startService(intent);
            playBtn.setImageResource(ImageUtil.getPlayImage(false));
        }
    }

    /**
     * 上一首按钮点击事件监听类
     */
    private class OnFrontClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(PlayActivity.this, MusicPlayerService.class);
            intent.setAction(MusicPlayerService.ACTION_FRONT);
            startService(intent);
            playBtn.setImageResource(ImageUtil.getPlayImage(false));
        }
    }

    /**
     * 模式按钮点击事件监听类
     */
    private class OnModeClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(PlayActivity.this, MusicPlayerService.class);
            intent.setAction(MusicPlayerService.ACTION_MODE);
            // 用于提示的文字
            String hint = null;
            // 用于设置图片的模式
            String mode = null;
            switch (MusicPlayerService.playMode) {
                case MusicPlayerService.PLAY_MODE_LIST_PLAY:
                    // 切换列表循环模式
                    intent.putExtra("mode", MusicPlayerService.PLAY_MODE_LIST_LOOP);
                    hint = getString(R.string.mode_list_loop);
                    mode = MusicPlayerService.PLAY_MODE_LIST_LOOP;
                    break;
                case MusicPlayerService.PLAY_MODE_LIST_LOOP:
                    // 切换单曲播放模式
                    intent.putExtra("mode", MusicPlayerService.PLAY_MODE_SINGLE_LOOP);
                    hint = getString(R.string.mode_single_loop);
                    mode = MusicPlayerService.PLAY_MODE_SINGLE_LOOP;
                    break;
                case MusicPlayerService.PLAY_MODE_SINGLE_LOOP:
                    // 切换单曲循环模式
                    intent.putExtra("mode", MusicPlayerService.PLAY_MODE_RANDOM);
                    hint = getString(R.string.mode_random);
                    mode = MusicPlayerService.PLAY_MODE_RANDOM;
                    break;
                case MusicPlayerService.PLAY_MODE_RANDOM:
                    // 切换列表播放模式
                    intent.putExtra("mode", MusicPlayerService.PLAY_MODE_LIST_PLAY);
                    hint = getString(R.string.mode_list_play);
                    mode = MusicPlayerService.PLAY_MODE_LIST_PLAY;
                    break;
            }
            startService(intent);
            // 设置播放模式按钮图片
            modeBtn.setImageResource(ImageUtil.getModeImage(mode));
            Toast.makeText(PlayActivity.this, hint, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 歌曲信息的广播接收器
     */
    public class MusicInfoReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive: 接受了一个广播！！！");
            toolbar.setTitle(intent.getStringExtra("title"));
            toolbar.setSubtitle(intent.getStringExtra("artist"));
            allTimeTxt.setText(DialogUtil.changeDuration(intent.getIntExtra("time", 0)));
            positionTxt.setText(DialogUtil.changeDuration(intent.getIntExtra("position", 0)));
            timeBar.setMax(intent.getIntExtra("time", 0));
            timeBar.setProgress(intent.getIntExtra("position", 0));
            playBtn.setImageResource(ImageUtil.getPlayImage(intent.getBooleanExtra("state", false)));
        }
    }
}
