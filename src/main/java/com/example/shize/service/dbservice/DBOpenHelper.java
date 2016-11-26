package com.example.shize.service.dbservice;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库助手类
 * Created by shize on 2016/11/16.
 */
public class DBOpenHelper extends SQLiteOpenHelper {

    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
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
