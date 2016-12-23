package com.example.shize.dao;

import com.example.shize.entity.MP3File;

import java.util.List;

/**
 * 播放器接口类
 * Created by shize on 2016/12/13.
 */

public class PlayerDao {
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
            void deleteLocalMusic(int id, String url);

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

    /**
     * 播放列表删除按钮点击事件接口
     * Created by shize on 2016/11/21.
     */
    public interface ListButtonClickListener {

        /**
         * 播放列表删除按钮点击事件
         * @param url 删除歌曲路径
         * @param position 删除歌曲位于播放列表位置
         */
        void onDeleteClick(String url, int position);

        /**
         * 音乐列表子项按钮点击事件接口
         */
        interface MenuButtonClickListener {

            /**
             * 音乐列表菜单按钮点击事件
             * @param position 子项位于列表位置
             */
            void onMenuClickListener(int position);
        }
    }

    /**
     * 子线程处理结束接口
     * Created by shize on 2016/11/16.
     */
    public interface ThreadOverHandlerDao {

        /**
         * 搜索结果接口,用于接收搜索结果
         * Created by shize on 2016/11/18.
         */
        interface ThreadHandleResult {

            /**
             * 找到音乐文件
             * @param filePathList 结果文件路径集合
             */
            void onDataSearchSuccess(List<String> filePathList);

            /**
             * 未找到文件
             */
            void onDataSearchFailed();

        }

        /**
         * 处理完毕或失败
         * Created by shize on 2016/11/17.
         */
        interface HandleSaveLater {
            /**
             * 处理成功
             * @param count 文件个数
             */
            void onDataHandleSuccess(Integer count);

            /**
             * 处理失败
             */
            void onDataHandleFailed();
        }

    }

    /**
     * Dialog事件接口
     * Created by shize on 2016/12/13.
     */
    public interface DialogListItemDao {

        /**
         * 设置Dialog的listView子项点击事件
         * @param position 点击位置
         */
        void onDialogListItemClickListener(int position);

    }
}
