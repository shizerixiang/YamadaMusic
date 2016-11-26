package com.example.shize.util.ioutil;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.example.shize.entity.MP3File;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 搜索文件信息方法
 * Created by shize on 2016/11/16.
 */
public class SearchInfoUtil {
    // 存放符合条件的文件路径
    static List<String> musics = new ArrayList<>();
    private static final String TAG = "SearchMusic";

    /**
     * 扫描sdcard查找符合文件的路径
     * @param directory 需要检查的文件
     * @param externals 需要的文件扩展名
     * @return 返回符合条件的文件路径集合
     */
    public static List<String> searchFilePath(File directory, String[] externals) {
        Log.i("SearchMusic", "directory: "+(directory != null ? "1" : "0"));
        if (directory != null){
            if (directory.isDirectory()){
                File[] files = directory.listFiles();
                Log.i("SearchMusic", "files: "+(files != null ? "1" : "0"));
                if (files != null) {
                    for (File file : files){
                        searchFilePath(file,externals);
                    }
                }
            } else {
                String filePath = directory.getAbsolutePath();
                for (String ext : externals){
                    if (filePath.endsWith(ext)){
                        musics.add(filePath);
                        Log.i("SearchMusic", "Yes!!!!!");
                        break;
                    }
                }
            }
        }
        return musics;
    }

    /**
     * 在系统媒体库获取音乐文件信息
     * @return 完整音乐文件信息
     */
    public static List<MP3File> searchMusicInfo(Context context){
        List<MP3File> mp3Files = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        assert cursor != null : "SearchMusic: 未查找到数据库数据";
        while (cursor.moveToNext()){
            mp3Files.add(new MP3File(
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)),
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.IS_MUSIC))));
            Log.i(TAG, "searchMusicInfo: 在媒体库中找到了一个文件！！！");
        }
        cursor.close();
        return mp3Files;
    }

}
