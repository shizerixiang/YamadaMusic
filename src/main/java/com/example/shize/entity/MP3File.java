package com.example.shize.entity;

/**
 * mp3文件的实体类
 * Created by shize on 2016/11/16.
 */
public class MP3File {

    // 音乐id
    private int id;
    // 音乐标题
    private String title;
    // 艺术家
    private String artist;
    // 专辑
    private String album;
    // 专辑id
    private int albumId;
    // 时长
    private int duration;
    // 文件大小
    private int size;
    // 文件路径
    private String url;
    // 是否为音乐
    private int isMusic;
    // 是否喜欢
    private int isLike;

    public MP3File() {
    }

    public MP3File(int id, String title, String artist, String album, int albumId, int duration,
                   int size, String url, int isMusic, int isLike) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.albumId = albumId;
        this.duration = duration;
        this.size = size;
        this.url = url;
        this.isMusic = isMusic;
        this.isLike = isLike;
    }

    public MP3File(String title, String artist, String album, int albumId, int duration, int size,
                   String url, int isMusic, int isLike) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.albumId = albumId;
        this.duration = duration;
        this.size = size;
        this.url = url;
        this.isMusic = isMusic;
        this.isLike = isLike;
    }

    public MP3File(String title, String artist, String album, int albumId, int duration, int size,
                   String url, int isMusic) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.albumId = albumId;
        this.duration = duration;
        this.size = size;
        this.url = url;
        this.isMusic = isMusic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int isMusic() {
        return isMusic;
    }

    public void setMusic(int music) {
        isMusic = music;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }
}
