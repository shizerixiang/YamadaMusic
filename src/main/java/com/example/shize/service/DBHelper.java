package com.example.shize.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.shize.dao.PlayerDao;
import com.example.shize.entity.MP3File;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库助手
 * Created by shize on 2016/12/13.
 */

public class DBHelper {
    /**
     * 处理音乐信息的事务
     * Created by shize on 2016/11/16.
     */
    public static class MusicService
            implements PlayerDao.MusicDao.LocalMusicDao,PlayerDao.MusicDao.LikeMusicDao,
            PlayerDao.MusicDao.LatelyMusicDao,PlayerDao.MusicDao.PlayListMusicDao {

        private DBOpenHelper dbOpenHelper;

        public MusicService(Context context, String dbName, int version) {
            this.dbOpenHelper = new DBOpenHelper(context, dbName, null, version);
        }

        /**
         * 添加本地歌曲信息
         *
         * @param mp3File 需要添加的歌曲信息
         */
        @Override
        public void saveLocalMusic(MP3File mp3File) {
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            db.execSQL("insert into musicInfo(title,artist,album,albumId,duration,size,url,isMusic,isLike) " +
                    "values(?,?,?,?,?,?,?,?,0)", new Object[]{
                    mp3File.getTitle(), mp3File.getArtist(), mp3File.getAlbum(), mp3File.getAlbumId(),
                    mp3File.getDuration(), mp3File.getSize(), mp3File.getUrl(), mp3File.isMusic()
            });
            // 如果只有一个类与数据库操作相关可以不关闭数据库（提升很小的性能）
            db.close();
        }

        /**
         * 删除歌曲信息
         *
         * @param id 需要删除的歌曲id
         */
        @Override
        public void deleteLocalMusic(int id, String url) {
            deletePlayMusic(url);
            deleteLatelyMusic(url);
            deleteLikeMusic(url);
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            db.execSQL("delete from musicInfo where id=?", new Object[]{id});
            db.close();
        }

        /**
         * 修改歌曲信息
         *
         * @param mp3File 修改后的歌曲
         */
        @Override
        public void updateLocalMusic(MP3File mp3File) {
            updateMusic(mp3File);
        }

        /**
         * 共用一个修改歌曲的方法
         *
         * @param mp3File 修改后的歌曲
         */
        private void updateMusic(MP3File mp3File) {
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            db.execSQL("update musicInfo set title=?,artist=?,album=?,albumId=?,duration=?,size=?," +
                    "url=?,isMusic=?,isLike=? where id=?", new Object[]{
                    mp3File.getTitle(), mp3File.getArtist(), mp3File.getAlbum(), mp3File.getAlbumId(),
                    mp3File.getDuration(), mp3File.getSize(), mp3File.getUrl(), mp3File.isMusic(),
                    mp3File.getIsLike(), mp3File.getId()
            });
            db.close();
        }

        /**
         * 查询所有的歌曲信息
         *
         * @return 所有的歌曲信息
         */
        @Override
        public List<MP3File> findAllLocalMusic() {
            List<MP3File> musicList = new ArrayList<>();
            SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from musicInfo", null);
            while (cursor.moveToNext()) {
                MP3File mp3File = new MP3File(cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("title")),
                        cursor.getString(cursor.getColumnIndex("artist")),
                        cursor.getString(cursor.getColumnIndex("album")),
                        cursor.getInt(cursor.getColumnIndex("albumId")),
                        cursor.getInt(cursor.getColumnIndex("duration")),
                        cursor.getInt(cursor.getColumnIndex("size")),
                        cursor.getString(cursor.getColumnIndex("url")),
                        cursor.getInt(cursor.getColumnIndex("isMusic")),
                        cursor.getInt(cursor.getColumnIndex("isLike")));
                musicList.add(mp3File);
            }
            cursor.close();
            db.close();
            return musicList;
        }

        /**
         * 根据路径查找文件信息
         *
         * @param url 文件路径
         * @return 文件信息，未找到返回null
         */
        @Override
        public MP3File findLocalMusic(String url) {
            MP3File mp3File = null;
            SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from musicInfo where url=?", new String[]{url});
            if (cursor.moveToNext()) {
                mp3File = new MP3File(cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("title")),
                        cursor.getString(cursor.getColumnIndex("artist")),
                        cursor.getString(cursor.getColumnIndex("album")),
                        cursor.getInt(cursor.getColumnIndex("albumId")),
                        cursor.getInt(cursor.getColumnIndex("duration")),
                        cursor.getInt(cursor.getColumnIndex("size")),
                        cursor.getString(cursor.getColumnIndex("url")),
                        cursor.getInt(cursor.getColumnIndex("isMusic")),
                        cursor.getInt(cursor.getColumnIndex("isLike")));
            }
            cursor.close();
            db.close();
            return mp3File;
        }

        /**
         * 添加进我喜欢列表
         *
         * @param mp3File 音乐路径
         */
        @Override
        public void addLikeFromLocal(MP3File mp3File) {
            mp3File.setIsLike(1);
            updateMusic(mp3File);
            saveLikeMusic(mp3File.getUrl());
        }

        /**
         * 从本地列表删除喜欢
         * @param url 文件路径
         */
        @Override
        public void removeLikeFromLocal(String url) {
            deleteLikeMusic(url);
        }

        /**
         * 保存喜欢的歌曲
         *
         * @param url 喜欢的音乐路径
         */
        @Override
        public void saveLikeMusic(String url) {
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            db.execSQL("insert into likeMusic(url) values(?)", new String[]{url});
            db.close();
        }

        /**
         * 删除喜欢的歌曲
         *
         * @param url 音乐路径
         */
        @Override
        public void deleteLikeMusic(String url) {
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            db.execSQL("delete from likeMusic where url=?", new String[]{url});
            db.close();
            MP3File mp3File = findLocalMusic(url);
            mp3File.setIsLike(0);
            updateMusic(mp3File);
        }

        /**
         * 更新列表内的歌曲信息
         *
         * @param mp3File 修改后的音乐信息
         */
        @Override
        public void updateLikeMusic(MP3File mp3File) {
            updateMusic(mp3File);
        }

        /**
         * 查找所有喜欢列表歌曲
         *
         * @return 歌曲集合
         */
        @Override
        public List<MP3File> findAllLikeMusic() {
            List<MP3File> mp3Files = new ArrayList<>();
            SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from likeMusic", null);
            while (cursor.moveToNext()) {
                mp3Files.add(findLocalMusic(cursor.getString(cursor.getColumnIndex("url"))));
            }
            cursor.close();
            db.close();
            return mp3Files;
        }

        /**
         * 查找喜欢列表单个歌曲
         *
         * @param url 需要查询音乐的路径
         * @return 歌曲信息
         */
        @Override
        public MP3File findLikeMusic(String url) {
            return findLocalMusic(url);
        }

        /**
         * 保存歌曲到最近播放列表
         *
         * @param url 音乐路径
         */
        @Override
        public void saveLatelyMusic(String url) {
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            db.execSQL("insert into latelyMusic(url) values(url)");
            db.close();
        }

        /**
         * 从最近播放列表删除
         *
         * @param url 音乐路径
         */
        @Override
        public void deleteLatelyMusic(String url) {
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            db.execSQL("delete from latelyMusic where url=?", new String[]{url});
            db.close();
        }

        /**
         * 更新最近播放列表歌曲信息
         *
         * @param mp3File 修改后的音乐信息
         */
        @Override
        public void updateLatelyMusic(MP3File mp3File) {
            updateMusic(mp3File);
        }

        /**
         * 查找最近播放列表所有歌曲
         *
         * @return 歌曲集合
         */
        @Override
        public List<MP3File> findAllLatelyMusic() {
            List<MP3File> mp3Files = new ArrayList<>();
            SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from latelyMusic", null);
            while (cursor.moveToNext()) {
                mp3Files.add(findLocalMusic(cursor.getString(cursor.getColumnIndex("url"))));
            }
            cursor.close();
            db.close();
            return mp3Files;
        }

        /**
         * 查询最近播放列表单一歌曲
         *
         * @param url 需要查询音乐的路径
         * @return 歌曲信息
         */
        @Override
        public MP3File findLatelyMusic(String url) {
            return findLocalMusic(url);
        }

        /**
         * 添加进我喜欢列表
         *
         * @param mp3File 音乐路径
         */
        @Override
        public void addLikeFromLately(MP3File mp3File) {
            saveLikeMusic(mp3File.getUrl());
        }

        /**
         * 添加进播放列队
         * @param url 音乐路径
         */
        @Override
        public void savePlayMusic(String url) {
            if (!hasMusic(url)) {
                SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
                db.execSQL("insert into playList(url) values(?)", new String[]{url});
                db.close();
            }
        }

        /**
         * 从播放列队删除
         * @param url 音乐路径
         */
        @Override
        public void deletePlayMusic(String url) {
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            db.execSQL("delete from playList where url=?", new String[]{url});
            db.close();
        }

        /**
         * 查找播放列队所有歌曲
         * @return 播放列队
         */
        @Override
        public List<MP3File> findAllPlayMusic() {
            SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
            List<MP3File> mp3Files = new ArrayList<>();
            Cursor cursor = db.rawQuery("select * from playList", null);
            while(cursor.moveToNext()){
                MP3File mp3File = findLocalMusic(cursor.getString(cursor.getColumnIndex("url")));
                mp3Files.add(mp3File);
            }
            cursor.close();
            db.close();
            return mp3Files;
        }

        /**
         * 查找播放列队内单一歌曲
         * @param url 音乐路径
         * @return 音乐信息
         */
        @Override
        public MP3File findPlayMusic(String url) {
            return findLocalMusic(url);
        }

        /**
         * 是否已存在该歌曲
         * @param url 歌曲路径
         * @return 判断结果
         */
        @Override
        public boolean hasMusic(String url) {
            boolean flag = false;
            SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from playList where url=?", new String[]{url});
            if (cursor.moveToNext()){
                flag = true;
            }
            cursor.close();
            db.close();
            return flag;
        }

        /**
         * 清空播放列队
         */
        @Override
        public void clearPlayList() {
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            db.execSQL("delete from playList");
            db.close();
        }

        /**
         * 添加进播放列队
         * @param mp3File 音乐信息
         */
        @Override
        public void addPlayList(MP3File mp3File){
            savePlayMusic(mp3File.getUrl());
        }

        /**
         * 根据路径获取在播放列表中的位置
         * @param url 路径
         * @return 播放列表中的位置
         */
        @Override
        public int getPlayListPosition(String url){
            List<MP3File> mp3Files = findAllPlayMusic();
            for (int i = 0; i < mp3Files.size(); i++){
                if (mp3Files.get(i).getUrl().equals(url)){
                    return i;
                }
            }
            return 0;
        }
    }

    /**
     * 数据库助手类
     * Created by shize on 2016/11/16.
     */
    public static class DBOpenHelper extends SQLiteOpenHelper {

        public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                            int version) {
            super(context, name, factory, version);
        }

        /**
         * 只有第一次数据库创建时调用
         * @param db 数据库
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            // 本地音乐
            db.execSQL("create table musicInfo(" +
                    "id Integer primary key autoincrement," +
                    "title varchar(100)," +
                    "artist varchar(20)," +
                    "album varchar(100)," +
                    "albumId Integer ," +
                    "duration Integer," +
                    "size Integer," +
                    "url varchar(300)," +
                    "isMusic Integer," +
                    "isLike Integer)");
            // 喜欢列表
            db.execSQL("create table likeMusic(" +
                    "id Integer primary key autoincrement," +
                    "url varchar(300) unique)");
            // 最近播放列表
            db.execSQL("create table latelyMusic(" +
                    "id Integer primary key autoincrement," +
                    "url varchar(300))");
            // 播放列表
            db.execSQL("create table playList(" +
                    "id Integer primary key autoincrement," +
                    "url varchar(300) unique)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
