package com.example.shize.util.dbutil;

import android.util.Log;

import com.example.shize.dao.MusicDao;
import com.example.shize.entity.MP3File;

import java.util.List;

/**
 * 用于对数据库操作类
 * Created by shize on 2016/11/17.
 */
public class MusicHandleInfoUtil {
    private final static String TAG = "SearchMusic";

    /**
     * 向数据库插入已经查询到的音乐数据
     * @param mp3Files 传入需要插入的音乐信息集合
     */
    public static void saveMusicInfo(List<MP3File> mp3Files, MusicDao.LocalMusicDao musicService){
        for (MP3File mp3File : mp3Files){
            MP3File info = musicService.findLocalMusic(mp3File.getUrl());
            if (info == null){
                musicService.saveLocalMusic(mp3File);
                Log.i(TAG, "saveMusicInfo: 成功存入一条信息！！！");
            } else {
                Log.i(TAG, "saveMusicInfo: 重复了一条信息！！！");
            }
        }
    }

    /**
     * 通过url在系统提供的多媒体数据获取音乐文件的其他信息
     * @param url 所查询到的音乐文件路径信息
     * @return 返回完整的音乐文件信息
     */
    public static MP3File searchMusicInfo(String url){
        return null;
    }

}
