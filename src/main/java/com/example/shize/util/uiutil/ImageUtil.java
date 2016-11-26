package com.example.shize.util.uiutil;

import com.example.shize.fragment.R;
import com.example.shize.service.MusicPlayerService;

/**
 * 图片设置工具类
 * Created by shize on 2016/11/21.
 */

public class ImageUtil {

    /**
     * 设置播放/暂停按钮图片
     * @return 图片资源id
     */
    public static int getPlayImage() {
        if (MusicPlayerService.playerOpenState){
            return R.drawable.play_under_music_pause;
        } else {
            return R.drawable.play_under_music_play;
        }
    }

    /**
     * 设置播放/暂停按钮图片
     * @return 图片资源id
     */
    public static int getPlayImage(boolean state) {
        if (state){
            return R.drawable.play_under_music_pause;
        } else {
            return R.drawable.play_under_music_play;
        }
    }

    /**
     * 设置播放模式按钮图片(点击事件)
     * @return 图片资源id
     */
    public static int getModeImage(String playMode) {
        switch (playMode){
            case MusicPlayerService.PLAY_MODE_LIST_PLAY:
                return R.drawable.play_under_music_mode_one_list;
            case MusicPlayerService.PLAY_MODE_LIST_LOOP:
                return R.drawable.play_under_music_mode_loop_list;
            case MusicPlayerService.PLAY_MODE_SINGLE_LOOP:
                return R.drawable.play_under_music_mode_loop_one;
            case MusicPlayerService.PLAY_MODE_RANDOM:
                return R.drawable.play_under_music_mode_random;
            default:
                return R.drawable.play_under_music_mode_one_list;
        }
    }

    /**
     * 设置播放模式按钮图片(初始化)
     */
    public static int getModeImage() {
        switch (MusicPlayerService.playMode){
            case MusicPlayerService.PLAY_MODE_LIST_PLAY:
                return R.drawable.play_under_music_mode_one_list;
            case MusicPlayerService.PLAY_MODE_LIST_LOOP:
                return R.drawable.play_under_music_mode_loop_list;
            case MusicPlayerService.PLAY_MODE_SINGLE_LOOP:
                return R.drawable.play_under_music_mode_loop_one;
            case MusicPlayerService.PLAY_MODE_RANDOM:
                return R.drawable.play_under_music_mode_random;
            default:
                return R.drawable.play_under_music_mode_one_list;
        }
    }

    /**
     * 迷你播放器图标
     * @param state 播放器状态
     * @return 图片id
     */
    public static int getMiniPlayImage(boolean state) {
        if (state){
            return R.drawable.under_main_stop;
        } else {
            return R.drawable.under_main_play;
        }
    }

    /**
     * 设置状态栏属性
     * @param playerOpenState 播放器状态
     * @return 图片资源id
     */
    public static int getStatusPlayImage(boolean playerOpenState) {
        if (playerOpenState){
            return R.drawable.notification_stop;
        } else {
            return R.drawable.notification_play;
        }
    }
}
