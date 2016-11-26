package com.example.shize.util.uiutil;

import android.app.AlertDialog;
import android.content.Context;

import com.example.shize.entity.MP3File;
import com.example.shize.fragment.R;

/**
 * 弹出框工具类
 * Created by shize on 2016/11/18.
 */
public class DialogUtil {
    /**
     * 弹窗显示歌曲信息
     *
     * @param mp3File 歌曲信息
     */
    public static void showMusicMessage(Context context, MP3File mp3File) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(context.getString(R.string.music_dialog_title));
        dialog.setMessage(context.getString(R.string.music_message_name) + mp3File.getTitle() + "\n" +
                context.getString(R.string.music_message_singer) + mp3File.getArtist() + "\n" +
                context.getString(R.string.music_message_album) + mp3File.getAlbum() + "\n" +
                context.getString(R.string.music_message_duration) +
                changeDuration(mp3File.getDuration()) + "\n" +
                context.getString(R.string.music_message_size) + ((mp3File.getSize()) / 1024) + "KB");
        dialog.show();
    }

    /**
     * 转换时间显示格式
     *
     * @param duration 时间ms
     * @return 最终显示时间格式
     */
    public static String changeDuration(int duration) {
        String m;
        String s;
        if (duration / 60000 < 1) {
            m = "00";
        } else if (duration / 60000 > 1 && duration / 60000 < 10) {
            m = "0" + (duration / 60000);
        } else {
            m = String.valueOf(duration / 60000);
        }
        if (duration <= 100) {
            s = "00";
        } else if (duration > 100 && duration < 1000) {
            s = "01";
        } else if (duration / 1000 >= 1 && duration / 1000 < 9) {
            s = "0" + (duration / 1000 + 1);
        } else {
            s = String.valueOf(duration / 1000 % 60 + 1);
        }
        return m + ":" + s;
    }

}
