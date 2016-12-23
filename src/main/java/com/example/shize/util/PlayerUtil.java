package com.example.shize.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.MediaMetadataRetriever;
import android.provider.MediaStore;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;

import com.example.shize.dao.PlayerDao;
import com.example.shize.entity.MP3File;
import com.example.shize.fragment.R;
import com.example.shize.service.MusicPlayerService;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 播放器相关工具类
 * Created by shize on 2016/12/13.
 */

public class PlayerUtil {
    private final static String TAG = "PlayerUtil";
    private static Bitmap APP_IMAGE = null;
    private static Bitmap ART_IMAGE = null;
    private static String URL = null;
    private static int type = 0;
    /**
     * 图片设置工具类
     * Created by shize on 2016/11/21.
     */

    public static class ImageUtil {

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
                return R.drawable.animvectordrawable_music;
            } else {
                return R.drawable.animvectordrawable_music2;
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

        /**
         * 获取音频文件内的专辑图片
         * @param context 上下文
         * @param url 音频文件路径
         * @param flag 标识
         * @return 图片画布
         */
        public static Bitmap getArtImage(Context context, String url, int flag) {
            // 判断是否已经加载过默认图片
            if (APP_IMAGE == null){
                APP_IMAGE = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.ic_launcher);
            }
            // 判断是否是第一次加载
            if (URL == null || !URL.equals(url) || flag != type) {
                setArtImage(url);
            } else if (ART_IMAGE == null){
                ART_IMAGE = APP_IMAGE;
            }
            type = flag;
            return ART_IMAGE;
        }

        /**
         * 设置专辑图片
         * @param url 歌曲文件地址
         */
        private static void setArtImage(String url) {
            if (url != null) {
                MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
                try {
                    metadataRetriever.setDataSource(url);
                    byte[] artwork = metadataRetriever.getEmbeddedPicture();
                    if (artwork != null) {
                        ART_IMAGE =  BitmapFactory.decodeByteArray(artwork, 0, artwork.length);
                    } else {
                        ART_IMAGE = APP_IMAGE;
                    }
                } catch (Exception e) {
                    Log.i(TAG, "getArtImage: " + e);
                }
                metadataRetriever.release();
                URL = url;
            } else {
                ART_IMAGE = APP_IMAGE;
            }
        }
        /**
         * 转换图片成圆形
         *
         * @param bitmap
         *            传入Bitmap对象
         * @return 圆形图片
         */
        public static Bitmap toRoundBitmap(Bitmap bitmap) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            float roundPx;
            float left, top, right, bottom;
            if (width <= height) {
                roundPx = width / 2;
                left = 0;
                top = 0;
                right = width;
                bottom = width;
                height = width;
            } else {
                roundPx = height / 2;
                float clip = (width - height) / 2;
                left = clip;
                right = width - clip;
                top = 0;
                bottom = height;
                width = height;
            }

            Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);

            // 设置画笔无锯齿
            paint.setAntiAlias(true);

            // 填充整个Canvas
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);

            // 以下有两种方法画圆,drawRounRect和drawCircle
            // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);// 画圆角矩形，
            // 第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径。
            canvas.drawCircle(roundPx, roundPx, roundPx, paint);
            // 设置两张图片相交时的模式,参考http://trylovecatch.iteye.com/blog/1189452
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            //以Mode.SRC_IN模式合并bitmap和已经draw了的Circle
            canvas.drawBitmap(bitmap, null, src, paint);

            return output;
        }

    }

    /**
     * 弹出框工具类
     * Created by shize on 2016/11/18.
     */
    public static class DialogUtil {
        private static float KB = 1024;
        private static float MB = 1024*1024;
        private static float GB = 1024*1024*1024;

        /**
         * 初始化歌曲信息list
         * @param context 上下文
         * @param mp3File 音乐信息
         * @return 信息list
         */
        public static List<HashMap<String,String>> getMessageList(Context context,MP3File mp3File) {
            List<HashMap<String, String>> messageList = new ArrayList<>();
            messageList.add(getMessageHashMap(context.getString(R.string.message_file_name), mp3File.getTitle()));
            messageList.add(getMessageHashMap(context.getString(R.string.message_time),
                    PlayerUtil.DialogUtil.changeDuration(mp3File.getDuration())));
            messageList.add(getMessageHashMap(context.getString(R.string.message_file_size), changeSize(mp3File.getSize())));
            messageList.add(getMessageHashMap(context.getString(R.string.message_file_path),
                    mp3File.getUrl()));
            return messageList;
        }

        public static HashMap<String, Object> setHashMap(String name, int icon){
            HashMap<String, Object> item = new HashMap<>();
            item.put("name", name);
            item.put("icon", icon);
            return item;
        }

        public static HashMap<String,String> getMessageHashMap(String title, String content) {
            HashMap<String, String> item = new HashMap<>();
            item.put("title", title);
            item.put("content", content);
            return item;
        }

        /**
         * 转换时间显示格式
         *
         * @param duration 时间ms
         * @return 最终显示时间格式
         */
        public static String changeDuration(int duration) {
            String m, s;
            int sTime = (duration/1000) % 60;
            int mTime = duration / 60000;
            // 设置分
            if (mTime < 10) {
                m = "0" + mTime;
            } else {
                m = String.valueOf(mTime);
            }
            // 设置秒
            if (sTime < 10) {
                s = "0" + sTime;
            } else {
                s = String.valueOf(sTime);
            }
            return m + ":" + s;
        }

        /**
         * 设置文件大小显示
         * @param size 文件大小，单位b
         * @return 显示字符串
         */
        public static String changeSize(int size){
            String strSize;
            int kb, mb, gb;
            if (size < KB) {
                strSize = size+"B";
            } else if (size < MB){
                kb = (int) (size / KB);
                strSize = kb + "KB";
            } else if (size < GB){
                mb = (int) (size / MB);
                kb = (int) (((size % MB) / MB) * 10);
                strSize = mb + "." + kb + "MB";
            } else {
                gb = (int) (size / GB);
                mb = (int) (((size % GB) / GB) * 10);
                strSize = gb + "." + mb + "GB";
            }
            return strSize;
        }

    }

    /**
     * 搜索文件信息方法
     * Created by shize on 2016/11/16.
     */
    public static class SearchInfoUtil {
        private static final String TAG = "SearchMusic";
        // 存放符合条件的文件路径
        static List<String> musics = new ArrayList<>();

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

    /**
     * 用于对数据库操作类
     * Created by shize on 2016/11/17.
     */
    public static class MusicHandleInfoUtil {
        private final static String TAG = "SearchMusic";

        /**
         * 向数据库插入已经查询到的音乐数据
         * @param mp3Files 传入需要插入的音乐信息集合
         */
        public static void saveMusicInfo(List<MP3File> mp3Files, PlayerDao.MusicDao.LocalMusicDao musicService){
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

    /**
     * 动画工具
     * Created by shize on 2016/12/14.
     */
    public static class AnimationUtil {

        /**
         * list飞入动画
         */
        public static LayoutAnimationController getListFlyAnimation() {
            LayoutAnimationController lac;
            TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 6f, Animation.RELATIVE_TO_SELF,
                    0f, Animation.RELATIVE_TO_SELF, 6f, Animation.RELATIVE_TO_SELF, 0f);
            // 设置减速器
            animation.setInterpolator(new DecelerateInterpolator());
            // 动画时间
            animation.setDuration(500);
            // 动画减速度
            animation.setStartOffset(40);
            // 偏移延迟
            lac = new LayoutAnimationController(animation, 0.1f);
            lac.setInterpolator(new DecelerateInterpolator());
            return lac;
        }
    }
}
