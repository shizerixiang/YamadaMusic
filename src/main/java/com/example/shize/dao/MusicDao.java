package com.example.shize.dao;

import com.example.shize.entity.MP3File;

import java.util.List;

/**
 * 音乐相关事务接口
 * Created by shize on 2016/11/18.
 */
public interface MusicDao {
    /**
     * 最近播放列表事务接口
     * Created by shize on 2016/11/18.
     */
    interface LatelyMusicDao {

        /**
         * 加入最近播放列表
         * @param url 音乐路径
         */
        void saveLatelyMusic(String url);

        /**
         * 删除最近播放列表的音乐信息
         * @param url 音乐路径
         */
        void deleteLatelyMusic(String url);

        /**
         * 修改最近播放列表的音乐信息
         * @param mp3File 修改后的音乐信息
         */
        void updateLatelyMusic(MP3File mp3File);

        /**
         * 查找所有最近播放列表的音乐信息
         * @return 音乐信息集合
         */
        List<MP3File> findAllLatelyMusic();

        /**
         * 查找最近播放列表的音乐信息
         * @param url 需要查询音乐的路径
         * @return 音乐文件所有信息
         */
        MP3File findLatelyMusic(String url);

        /**
         * 添加音乐进我喜欢列表
         * @param mp3File 音乐信息
         */
        void addLikeFromLately(MP3File mp3File);

        /**
         * 从最近播放列表加入播放列队
         * @param mp3File 音乐信息
         */
        void addPlayList(MP3File mp3File);

        /**
         * 获取歌曲在播放列表中的位置
         * @param url 歌曲路径
         * @return 位置
         */
        int getPlayListPosition(String url);
    }

    /**
     * 喜欢的音乐事务接口
     * Created by shize on 2016/11/18.
     */
    interface LikeMusicDao {

        /**
         * 加入喜欢列表
         * @param url 喜欢的音乐路径
         */
        void saveLikeMusic(String url);

        /**
         * 删除喜欢列表的音乐信息
         * @param url 音乐路径
         */
        void deleteLikeMusic(String url);

        /**
         * 修改喜欢列表的音乐信息
         * @param mp3File 修改后的音乐信息
         */
        void updateLikeMusic(MP3File mp3File);

        /**
         * 查找所有喜欢列表的音乐信息
         * @return 喜欢列表音乐信息集合
         */
        List<MP3File> findAllLikeMusic();

        /**
         * 查找喜欢列表的音乐信息
         * @param url 需要查询音乐的路径
         * @return 音乐文件所有信息
         */
        MP3File findLikeMusic(String url);

        /**
         * 从喜欢列表加入播放列队
         * @param mp3File 音乐信息
         */
        void addPlayList(MP3File mp3File);

        /**
         * 获取歌曲在播放列表中的位置
         * @param url 歌曲路径
         * @return 位置
         */
        int getPlayListPosition(String url);

    }

    /**
     * 本地音乐事务接口
     * Created by shize on 2016/11/16.
     */
    interface LocalMusicDao {

        /**
         * 存储本地音乐文件信息
         * @param mp3File 音乐文件信息
         */
        void saveLocalMusic(MP3File mp3File);

        /**
         * 删除本地音乐文件信息
         * @param id 音乐文件id
         * @param url 文件路径
         */
        void deleteLocalMusic(int id,String url);

        /**
         * 更新本地音乐文件信息
         * @param mp3File 更新后的音乐文件信息
         */
        void updateLocalMusic(MP3File mp3File);

        /**
         * 查询所有本地音乐文件信息
         * @return 返回所有文件信息
         */
        List<MP3File> findAllLocalMusic();

        /**
         * 根据文件路径查找对应本地音乐文件信息
         * @param url 文件路径
         * @return 文件完整信息
         */
        MP3File findLocalMusic(String url);

        /**
         * 添加音乐进我喜欢列表
         * @param mp3File 音乐信息
         */
        void addLikeFromLocal(MP3File mp3File);

        /**
         * 从本地列表移除喜欢
         * @param url 文件路径
         */
        void removeLikeFromLocal(String url);

        /**
         * 添加本地歌曲进播放列队
         * @param mp3File 音乐信息
         */
        void addPlayList(MP3File mp3File);

        /**
         * 获取歌曲在播放列表中的位置
         * @param url 歌曲路径
         * @return 位置
         */
        int getPlayListPosition(String url);
    }

    /**
     * 播放列队事务接口类
     */
    interface PlayListMusicDao{
        /**
         * 加入播放列队
         * @param url 音乐路径
         */
        void savePlayMusic(String url);

        /**
         * 移除播放列队
         * @param url 音乐路径
         */
        void deletePlayMusic(String url);

        /**
         * 查找播放列队
         * @return 播放列队
         */
        List<MP3File> findAllPlayMusic();

        /**
         * 查找列队单一歌曲
         * @param url 音乐路径
         * @return 音乐信息
         */
        MP3File findPlayMusic(String url);

        /**
         * 查找播放列队是否已有该歌曲
         * @param url 歌曲路径
         * @return 判断结果
         */
        boolean hasMusic(String url);

        /**
         * 清空播放列队
         */
        void clearPlayList();
    }
}
