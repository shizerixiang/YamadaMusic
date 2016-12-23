package com.example.shize.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.shize.dao.PlayerDao;
import com.example.shize.fragment.R;
import com.example.shize.service.ThreadService;

/**
 * 扫描本地歌曲界面
 * Created by shize on 2016/11/14.
 */
public class SearchMusicActivity extends TransparentActivity {

    private static final int MY_READ_STORAGE_PERMISSION = 1;
    private static final String TAG = "SearchMusic";
    private Context context;
    private ProgressBar searchProgress;
    // 进度条指针
    private int searchPosition = 10;
    // 定义界面布局数组，用于切换界面
    private View[] views;
    // 定义动画数组，用于指定切换动画效果
    private Animation[] animations;
    private ThreadService.SearchMusicInfoThread searchMusicInfoThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_music_main);

        context = getApplicationContext();

        grantedPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                MY_READ_STORAGE_PERMISSION);
    }

    /**
     * 初始化控件
     */
    private void initControl(){
        // 初始化头部
        initHeader();
        // 初始化内容
        initContent();
    }

    /**
     * 初始化标题栏
     */
    private void initHeader(){
        // 初始化工具条标题
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        assert toolbar != null;
        toolbar.setTitle(getString(R.string.nav_item_search));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_exit);
        toolbar.setNavigationOnClickListener(new OnBackClickListener());
    }

    /**
     * 初始化内容
     */
    private void initContent() {
        // 初始化整体布局
        initContentLayout();
        // 初始化初始界面
        initContentIndex();
        // 初始化全盘扫描界面
        initContentAll();
    }

    /**
     * 初始化整体布局
     */
    private void initContentLayout() {
        // 初始化界面布局数组
        views = new View[2];
        views[0] = findViewById(R.id.search_under_index);
        views[1] = findViewById(R.id.search_under_all);
        // 初始化动画效果数组
        animations = new Animation[2];
        animations[0] = AnimationUtils.loadAnimation(this, R.anim.search_anim_visiable);
        animations[1] = AnimationUtils.loadAnimation(this, R.anim.search_anim_invisiable);
    }

    /**
     * 初始化全盘扫描界面
     */
    private void initContentAll() {
        // 进度条
        searchProgress = (ProgressBar) findViewById(R.id.search_progress);
        // 设置进度
        setSearchProgress();

        // 取消按钮
        Button cancelBtn = (Button) findViewById(R.id.search_music_button_all_cancel);
        assert cancelBtn != null;
        cancelBtn.setOnClickListener(new OnCancelClickListener());
    }

    /**
     * 设置进度
     */
    private void setSearchProgress() {
        Thread progressThread = new Thread(new progressRunnable());
        progressThread.start();
    }

    /**
     * 初始化扫描首个页面
     */
    private void initContentIndex() {
        // 全盘扫描按钮
        Button searchBtn = (Button) findViewById(R.id.search_music_button_search);
        assert searchBtn != null;
        searchBtn.setOnClickListener(new OnSearchClickListener());
    }

    /**
     * 返回按钮
     * @param item actionBar子项
     * @return 是否拦截
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    /**
     * 申请权限
     */
    private void grantedPermission(String[] permission, int permissionCode){
        // 判断是否已经被授权读取文件
        if (ContextCompat.checkSelfPermission(context,
                permission[0]) != PackageManager.PERMISSION_GRANTED){
            // 未被授权，是否被拒绝过，判断并给出提示
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    permission[0])){
                Log.i(TAG, "grantedPermission: 拒绝！！！");
            }
            // 申请授权,该方法是异步的，第二个参数可同时申请多个
            ActivityCompat.requestPermissions(this,
                    permission, permissionCode);
        } else {
            // 被授权，执行程序
            initControl();
        }
    }

    /**
     * 处理权限申请回调
     * @param requestCode 申请代号
     * @param permissions 申请权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_READ_STORAGE_PERMISSION:
                // 如果申请被取消，结果数组为空，所以要进行判断结果数组是否为空
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // 权限被授予，做你需要做的
                    initControl();
                } else {
                    // 权限未被授予
                    Log.i(TAG, "onRequestPermissionsResult: 没有权限！！！");
                }
                break;
        }
    }

    /**
     * 返回按钮点击事件监听类
     */
    private class OnBackClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            SearchMusicActivity.this.finish();
        }
    }

    /**
     * 扫描完成事件监听类
     */
    private class OnSearchFinishListener implements PlayerDao.ThreadOverHandlerDao.HandleSaveLater{

        @Override
        public void onDataHandleSuccess(Integer count){
            Log.i(TAG, "onDataHandleSuccess: 处理结束！！！");
            // 设置进度条完成跑条
            searchPosition = 100;
            searchProgress.setProgress(searchPosition);
            // 给出提示给用户
            Toast.makeText(SearchMusicActivity.this, getString(R.string.search_clear)+
                    ",找到了"+count+"首歌曲！", Toast.LENGTH_SHORT).show();
            // 跳转到初始界面
            views[0].startAnimation(animations[0]);
            views[0].setVisibility(View.VISIBLE);
            views[1].setVisibility(View.GONE);
            views[1].startAnimation(animations[1]);
        }

        @Override
        public void onDataHandleFailed() {
            Log.i(TAG, "onDataHandleSuccess: 处理失败！！！");
            Toast.makeText(SearchMusicActivity.this, getString(R.string.search_fail),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 全盘扫描点击事件类
     */
    private class OnSearchClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // 切换界面
            views[1].setVisibility(View.VISIBLE);
            views[1].startAnimation(animations[0]);
            views[0].startAnimation(animations[1]);
            views[0].setVisibility(View.GONE);
            // 扫描歌曲
            searchMusicInfoThread = new ThreadService.SearchMusicInfoThread();
            searchMusicInfoThread.execute(SearchMusicActivity.this);
            searchMusicInfoThread.setHandleComplete(new OnSearchFinishListener());
        }
    }

    /**
     * 进度条子线程监听类
     */
    private class progressRunnable implements Runnable {
        @Override
        public void run() {
            while (searchPosition < 90){
                searchProgress.setProgress(searchPosition);
                searchPosition += 1;
                try {
                    Thread.sleep(60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            searchPosition = 10;
        }
    }

    /**
     * 取消按钮点击事件监听类
     */
    private class OnCancelClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // 取消扫描服务
            searchMusicInfoThread.cancelSearch();
            // 跳转到初始界面
            views[0].startAnimation(animations[0]);
            views[0].setVisibility(View.VISIBLE);
            views[1].setVisibility(View.GONE);
            views[1].startAnimation(animations[1]);
        }
    }
}
