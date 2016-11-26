package com.example.shize.dao;

import java.util.List;

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
         */
        void onDataHandleSuccess();

        /**
         * 处理失败
         */
        void onDataHandleFailed();
    }

}
