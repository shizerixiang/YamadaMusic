package com.example.shize.service;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.example.shize.dao.PlayerDao;
import com.example.shize.entity.MP3File;
import com.example.shize.util.PlayerUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 线程服务
 * Created by shize on 2016/12/13.
 */

public class ThreadService {
    /**
     * 扫描文件子线程
     * Created by shize on 2016/11/16.
     */
    public static class SearchMusicPathThread extends AsyncTask<Void, Integer, List<String>> {
        private static final String TAG = "SearchMusic";
        // 存放符合条件的文件路径
        private static List<String> musics = new ArrayList<>();
        // 接收结果接口
        public PlayerDao.ThreadOverHandlerDao.ThreadHandleResult searchResult;

        /**
         * 完成接口，在这放入线程执行完毕后需要执行的代码
         * @param searchResult 接口
         */
        public void setOnResult(PlayerDao.ThreadOverHandlerDao.ThreadHandleResult searchResult){
            this.searchResult = searchResult;
        }

        /**
         * 准备方法
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * 后台运行
         * @param params 参数
         * @return 结果
         */
        @Override
        protected List<String> doInBackground(Void... params) {
            List<String> files = new ArrayList<>();
            try {
                String[] externals = {".mp3"};
                File file = new File(Environment.getExternalStorageDirectory() + "/");
                files = PlayerUtil.SearchInfoUtil.searchFilePath(file, externals);
                Log.i(TAG, "run: success  "+file+externals[0]);
                publishProgress(1);
            } catch (Exception e) {
                Log.e(TAG, "run: 查找音乐文件失败！" + e.getMessage());
            }
            return files;
        }

        /**
         * 通过接口实现在UI线程内显示进度
         * @param values 进度参数
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values[0]);
        }

        /**
         * 通过接口实现在UI线程内显示数据
         * @param strings 搜索结果
         */
        @Override
        protected void onPostExecute(List<String> strings) {
            super.onPostExecute(strings);
            if (strings.size() >= 0) {
                // 利用接口接受数据，传给Activity
                searchResult.onDataSearchSuccess(strings);
                Log.i(TAG, "onPostExecute: 找到音乐文件！！！！！");
            } else {
                searchResult.onDataSearchFailed();
                Log.i(TAG, "onPostExecute: 未找到文件！！！！！");
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    /**
     * 搜索音乐文件所有信息
     * Created by shize on 2016/11/17.
     */
    public static class SearchMusicInfoThread extends AsyncTask<Context, Integer, Integer> {

        private final static String TAG = "SearchMusic";
        private PlayerDao.ThreadOverHandlerDao.HandleSaveLater handleSaveLater;
        private boolean isCancel = false;

        public void setHandleComplete(PlayerDao.ThreadOverHandlerDao.HandleSaveLater handleSaveLater){
            this.handleSaveLater = handleSaveLater;
        }

        /**
         * 准备工作
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * 执行方法，在子线程中执行
         * @param params 参数
         * @return 结果
         */
        @Override
        protected Integer doInBackground(Context... params) {
            PlayerDao.MusicDao.LocalMusicDao musicService = new DBHelper.MusicService(params[0], "Media.db", 1);
            List<MP3File> mp3Files;
            mp3Files = PlayerUtil.SearchInfoUtil.searchMusicInfo(params[0]);
            Log.i(TAG, "doInBackground: 找到了"+ mp3Files.size()+"个音乐文件！！！");
            if (!isCancel) {
                PlayerUtil.MusicHandleInfoUtil.saveMusicInfo(mp3Files, musicService);
            }
            return mp3Files.size();
        }

        /**
         * 执行进度，在主线程中执行
         * @param values 进度
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values[0]);
            Log.i(TAG, "onProgressUpdate: 显示进度！！！");
        }

        /**
         * 处理结果，在主线程中执行
         * @param integer 结果
         */
        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer > 0 && !isCancel){
                handleSaveLater.onDataHandleSuccess(integer);
                Log.i(TAG, "onPostExecute: 处理完毕！！！");
            } else {
                handleSaveLater.onDataHandleFailed();
                Log.i(TAG, "onPostExecute: 处理失败！！！");
            }
        }

        /**
         * 取消扫描
         */
        public void cancelSearch(){
            isCancel = true;
        }

        /**
         * 手动取消执行该方法
         */
        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.i(TAG, "onCancelled: 操作被取消！！！");
        }
    }
}
