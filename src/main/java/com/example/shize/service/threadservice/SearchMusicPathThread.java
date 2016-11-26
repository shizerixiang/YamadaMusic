package com.example.shize.service.threadservice;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import com.example.shize.dao.ThreadOverHandlerDao;
import com.example.shize.util.ioutil.SearchInfoUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 扫描文件子线程
 * Created by shize on 2016/11/16.
 */
public class SearchMusicPathThread extends AsyncTask<Void, Integer, List<String>> {
    private static final String TAG = "SearchMusic";
    // 存放符合条件的文件路径
    private static List<String> musics = new ArrayList<>();
    // 接收结果接口
    public ThreadOverHandlerDao.ThreadHandleResult searchResult;

    /**
     * 完成接口，在这放入线程执行完毕后需要执行的代码
     * @param searchResult 接口
     */
    public void setOnResult(ThreadOverHandlerDao.ThreadHandleResult searchResult){
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
            files = SearchInfoUtil.searchFilePath(file, externals);
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
