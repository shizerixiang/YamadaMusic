package com.example.shize.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.shize.dao.MusicDao;
import com.example.shize.entity.MP3File;
import com.example.shize.fragment.R;
import com.example.shize.service.dbservice.MusicService;
import com.example.shize.util.uiutil.ImageUtil;

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * 播放音乐服务
 * Created by shize on 2016/11/14.
 */
public class MusicPlayerService extends Service {
    private final static String TAG = "MusicPlayerService";
    // 播放动作
    public final static String ACTION_PLAY = "com.example.shize.action.ACTION_PLAY";
    public final static String ACTION_PAUSE = "com.example.shize.action.ACTION_PAUSE";
    public final static String ACTION_STOP = "com.example.shize.action.ACTION_STOP";
    public final static String ACTION_NEXT = "com.example.shize.action.ACTION_NEXT";
    public final static String ACTION_FRONT = "com.example.shize.action.ACTION_FRONT";
    public final static String ACTION_MODE = "com.example.shize.action.ACTION_MODE";
    public static final String ACTION_EXIT = "com.example.shize.action.ACTION_EXIT";
    // 播放模式
    public final static String PLAY_MODE_SINGLE_LOOP = "com.example.shize.mode.SINGLE_LOOP";
    public final static String PLAY_MODE_LIST_PLAY = "com.example.shize.mode.LIST_PLAY";
    public final static String PLAY_MODE_LIST_LOOP = "com.example.shize.mode.LIST_LOOP";
    public final static String PLAY_MODE_RANDOM = "com.example.shize.mode.RANDOM";
    // 广播接受动作
    public final static String BROADCAST_ACTION_RECEIVER = "com.example.shize.MUSIC_BROADCAST";

    // 上一首音乐路径（用于判断上一首音乐是否暂停）
    private String path;
    // 媒体播放器
    private MediaPlayer mediaPlayer;
    // 播放位置（返回上一次暂停播放时的位置，前提是播放音乐路径相同）
    private int position = 0;
    // 当前播放模式
    public static String playMode = PLAY_MODE_LIST_PLAY;
    private Intent intent;
    // 播放列队服务
    private MusicDao.PlayListMusicDao playListMusicDao;
    // 播放列队
    private List<MP3File> mp3Files;
    // 起始播放位置
    private int startListPosition = 0;
    // 播放器的状态
    public static boolean playerOpenState = false;
    // 初始化播放歌曲标题
    private String title;
    // 初始化播放歌曲歌手名
    private String artist;
    // 控制发送广播的子线程
    private boolean isStopBroadcast = false;
    // 状态栏
    private NotificationManager notificationManager;
    private static final int NOTIFICATION_ID_BASIC = 11111;
    private RemoteViews contentView;
    private Notification notification;
    // 用户是否需要关闭程序
    private boolean closeApplication = false;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new OnMusicCompletionListener());
        // 初始化播放列表
        playListMusicDao = new MusicService(this,"Media.db",1);
        mp3Files = playListMusicDao.findAllPlayMusic();
        // 初始化标题
        initMusicTitle();
        // 设置迷你播放器在状态栏
        initStatus();
        // 启动子线程发送广播
        startBroadcast();
    }

    /**
     * 初始化歌曲标题
     */
    private void initMusicTitle() {
        if (mp3Files.size() > 0){
            title = mp3Files.get(0).getTitle();
            artist = mp3Files.get(0).getArtist();
        } else {
            title = getString(R.string.play_header_music_name);
            artist = getString(R.string.play_header_singer_name);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.intent = intent;
        // 每次发送播放指令时更新列表
        mp3Files = playListMusicDao.findAllPlayMusic();
        if (mp3Files.size() != 0 || intent.getAction().equals(ACTION_EXIT)) {
            // 检查用户意图
            setAction();
        } else {
            stop();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 启动广播发送的子线程
     */
    private void startBroadcast(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isStopBroadcast) {
                    // 设置需要发送的消息
                    sendMessage();
                    // 设置状态栏控件属性
                    setStatusAttribute();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 设置状态栏控件属性
     */
    private void setStatusAttribute() {
        // 初始化状态栏迷你播放器控件点击事件
        initStatusClick();
        notification.contentView = contentView;
        notification.contentView.setTextViewText(R.id.notification_music_name, title);
        notification.contentView.setTextViewText(R.id.notification_singer_name, artist);
        notification.contentView.setImageViewResource(
                R.id.notification_play, ImageUtil.getStatusPlayImage(playerOpenState));
        notificationManager.notify(NOTIFICATION_ID_BASIC,notification);
    }

    /**
     * 初始化状态栏信息
     */
    private void initStatus() {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.app_logo);
        Intent intent = null;
        try {
            intent = new Intent(this,Class.forName("com.example.shize.activity.MainActivity"));
            // 设置为栈顶复用模式
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        } catch (ClassNotFoundException e) {
            Log.i(TAG, "initStatus: 未找到Activity！！！");
            e.printStackTrace();
        }
        builder.setContentIntent(PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        contentView = new RemoteViews(getPackageName(),R.layout.notification);
        notification = builder.build();
        // 初始化状态栏迷你播放器控件点击事件
        initStatusClick();
        notification.contentView = contentView;
        notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID_BASIC, notification);
    }

    /**
     * 初始化状态栏迷你播放器控件点击事件
     */
    private void initStatusClick() {
        // 点击意图是事先加载完成的，无法动态判断状态
        if (MusicPlayerService.playerOpenState){
            contentView.setOnClickPendingIntent(R.id.notification_play, getStopIntent());
        } else {
            contentView.setOnClickPendingIntent(R.id.notification_play, getPlayIntent());
        }
        contentView.setOnClickPendingIntent(R.id.notification_next, getNextIntent());
        contentView.setOnClickPendingIntent(R.id.notification_exit, getExitIntent());
    }

    /**
     * 获取播放按钮点击事件意图
     * @return 意图
     */
    @NonNull
    private PendingIntent getPlayIntent() {
        Intent intent = new Intent(this,MusicPlayerService.class);
        intent.setAction(MusicPlayerService.ACTION_PLAY);
        return PendingIntent.getService(this,1, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * 获取暂停按钮点击事件意图
     * @return 意图
     */
    @NonNull
    private PendingIntent getStopIntent() {
        Intent intent = new Intent(this,MusicPlayerService.class);
        intent.setAction(MusicPlayerService.ACTION_PAUSE);
        return PendingIntent.getService(this,1, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * 获取下一首按钮点击事件意图
     * @return 意图
     */
    @NonNull
    private PendingIntent getNextIntent() {
        Intent intent = new Intent(this,MusicPlayerService.class);
        intent.setAction(MusicPlayerService.ACTION_NEXT);
        return PendingIntent.getService(this,2, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * 获取退出按钮点击事件意图
     * @return 意图
     */
    private PendingIntent getExitIntent() {
        Intent intent = new Intent(this,MusicPlayerService.class);
        intent.setAction(MusicPlayerService.ACTION_EXIT);
        return PendingIntent.getService(this,3, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * 发送消息给广播接收器
     */
    private void sendMessage(){
        Intent intent = new Intent();
        if (mp3Files.size() > 0) {
            intent.putExtra("title", title);
            intent.putExtra("artist", artist);
            intent.putExtra("position", mediaPlayer.getCurrentPosition());
            Log.i(TAG, "run: 时间指针：" + mediaPlayer.getCurrentPosition());
            intent.putExtra("time", mp3Files.get(startListPosition).getDuration());
            intent.putExtra("state", playerOpenState);
            intent.setAction(BROADCAST_ACTION_RECEIVER);
            Log.i(TAG, "run: 发送了一个广播！！！");
        } else {
            intent.putExtra("title", "歌曲名称");
            intent.putExtra("artist", "歌手名称");
            intent.putExtra("position", 0);
            Log.i(TAG, "run: 时间指针：" + 0);
            intent.putExtra("time", 1);
            intent.putExtra("state", false);
            intent.setAction(BROADCAST_ACTION_RECEIVER);
        }
        // 发送用户请求状态，是否需要关闭程序
        intent.putExtra("close", closeApplication);
        sendBroadcast(intent);
    }

    /**
     * 设置动作
     */
    private void setAction() {
        switch (intent.getAction()) {
            case ACTION_PLAY:
                if (intent.getStringExtra("position") != null) {
                    startListPosition = Integer.parseInt(intent.getStringExtra("position"));
                    Log.i(TAG, "setAction: startListPosition="+startListPosition);
                }
                if (startListPosition >= mp3Files.size()){
                    startListPosition = 0;
                }
                play(mp3Files.get(startListPosition).getUrl());
                break;
            case ACTION_PAUSE:
                pause();
                break;
            case ACTION_STOP:
                stop();
                break;
            case ACTION_NEXT:
                next();
                break;
            case ACTION_FRONT:
                front();
                break;
            case ACTION_MODE:
                // 设置播放模式
                playMode = intent.getStringExtra("mode");
                break;
            case ACTION_EXIT:
                playerOpenState = false;
                closeApplication = true;
                break;
        }
    }

    /**
     * 开始播放
     * @param path 播放路径
     */
    private void play(String path) {
        try {
            // 参数恢复到初始状态
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            // 进行异步缓冲
            mediaPlayer.prepareAsync();
            if (path.equals(this.path)) {
                mediaPlayer.setOnPreparedListener(new onPlayerPreparedListener(position));
            } else {
                this.path = path;
                mediaPlayer.setOnPreparedListener(new onPlayerPreparedListener(0));
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "onStartCommand: 路径读取失败！！！");
        }
    }

    /**
     * 暂停播放
     */
    private void pause() {
        if (mediaPlayer.isPlaying()) {
            // 获取当前播放位置信息
            position = mediaPlayer.getCurrentPosition();
            Log.i(TAG, "pause: " + (position / 1000));
            mediaPlayer.pause();
            playerOpenState = false;
        }
    }

    /**
     * 停止播放
     */
    private void stop() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        playerOpenState = false;
        position = 0;
    }

    /**
     * 下一首
     */
    private void next() {
        mediaPlayer.stop();
        startListPosition++;
        if (startListPosition >= mp3Files.size()){
            startListPosition = 0;
        }
        play(mp3Files.get(startListPosition).getUrl());
    }

    /**
     * 上一首
     */
    private void front() {
        mediaPlayer.stop();
        startListPosition--;
        if (startListPosition < 0){
            startListPosition = mp3Files.size() - 1;
        }
        play(mp3Files.get(startListPosition).getUrl());
    }

    /**
     * 根据模式选择播放方法
     */
    private void playWithMode(){
        switch (playMode){
            case PLAY_MODE_LIST_PLAY:
                listPlay();
                break;
            case PLAY_MODE_LIST_LOOP:
                listLoop();
                break;
            case PLAY_MODE_SINGLE_LOOP:
                singleLoop();
                break;
            case PLAY_MODE_RANDOM:
                randomPlay();
                break;
        }
    }

    /**
     * 单曲循环
     */
    private void singleLoop() {
        startListPosition--;
        position = 0;
        next();
    }

    /**
     * 列表播放
     */
    private void listPlay() {
        Log.i(TAG, "listPlay: 当前播放列表位置："+startListPosition);
        // 判断播放的列队位置，若不到倒数第二个则继续下一首
        if (startListPosition < mp3Files.size()-1){
            next();
        } else {
            stop();
        }
    }

    /**
     * 列表循环
     */
    private void listLoop() {
        next();
    }

    /**
     * 随机播放
     */
    private void randomPlay() {
        Random random = new Random();
        startListPosition = random.nextInt(mp3Files.size());
        Log.i(TAG, "randomPlay: 随机生成了数字" + startListPosition);
        next();
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            // 释放媒体播放器
            mediaPlayer.release();
        }
        // 停止广播的发送
        isStopBroadcast = true;
        // 关闭状态栏的悬浮栏
        stopNotification();
    }

    /**
     * 关闭悬浮栏
     */
    private void stopNotification() {
        notificationManager = (NotificationManager) getSystemService(
                getBaseContext().NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID_BASIC);
    }

    /**
     * 播放事件监听类
     */
    private class onPlayerPreparedListener implements MediaPlayer.OnPreparedListener {
        private int position;

        private onPlayerPreparedListener(int position) {
            this.position = position;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            title = mp3Files.get(startListPosition).getTitle();
            artist = mp3Files.get(startListPosition).getArtist();
            Log.i(TAG, "onPrepared: 开始播放歌曲："+title);
            mediaPlayer.start();
            // 开启状态
            playerOpenState = true;
            if (this.position > 0) {
                mediaPlayer.seekTo(this.position);
            }
        }
    }

    /**
     * 播放完成事件监听类
     */
    private class OnMusicCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            mp3Files = playListMusicDao.findAllPlayMusic();
            playWithMode();
            Log.i(TAG, "onCompletion: 播放执行完毕了！！！");
        }
    }
}
