package com.example.shize.service.threadservice;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.shize.dao.MusicDao;
import com.example.shize.dao.ThreadOverHandlerDao;
import com.example.shize.entity.MP3File;
import com.example.shize.service.dbservice.MusicService;
import com.example.shize.util.dbutil.MusicHandleInfoUtil;
import com.example.shize.util.ioutil.SearchInfoUtil;

import java.util.List;

/**
 * 搜索音乐文件所有信息
 * Created by shize on 2016/11/17.
 */
public class SearchMusicInfoThread extends AsyncTask<Context, Integer, Integer> {

    private MusicDao.LocalMusicDao musicService;
    private ThreadOverHandlerDao.HandleSaveLater handleSaveLater;
    private final static String TAG = "SearchMusic";

    public void setHandleComplete(ThreadOverHandlerDao.HandleSaveLater handleSaveLater){
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
        musicService = new MusicService(params[0],"Media.db",1);
        List<MP3File> mp3Files;
        mp3Files = SearchInfoUtil.searchMusicInfo(params[0]);
        Log.i(TAG, "doInBackground: 找到了"+ mp3Files.size()+"个音乐文件！！！");
        MusicHandleInfoUtil.saveMusicInfo(mp3Files, musicService);
        return 1;
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
        if (integer == 1){
            handleSaveLater.onDataHandleSuccess();
            Log.i(TAG, "onPostExecute: 处理完毕！！！");
        } else {
            handleSaveLater.onDataHandleFailed();
            Log.i(TAG, "onPostExecute: 处理失败！！！");
        }
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
