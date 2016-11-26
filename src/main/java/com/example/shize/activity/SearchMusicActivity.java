package com.example.shize.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.shize.dao.ThreadOverHandlerDao;
import com.example.shize.fragment.R;
import com.example.shize.service.threadservice.SearchMusicInfoThread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 扫描本地歌曲界面
 * Created by shize on 2016/11/14.
 */
public class SearchMusicActivity extends AppCompatActivity {

    private static final int MY_READ_STORAGE_PERMISSION = 1;
    private static final String TAG = "SearchMusic";
    private Context context;
    private SimpleAdapter adapter;
    private List<String> paths;
    private List<HashMap<String, Object>> items = new ArrayList<>();
    private ListView musics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_music_main);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.nav_item_search));
        context = getApplicationContext();

        grantedPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                MY_READ_STORAGE_PERMISSION);
    }

    private void initControl(){
        musics = (ListView) findViewById(R.id.search_content_music_list);
        Button searchBtn = (Button) findViewById(R.id.search_music_button_search);

        assert searchBtn != null;
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchMusicInfoThread searchMusicInfoThread = new SearchMusicInfoThread();
                searchMusicInfoThread.execute(SearchMusicActivity.this);
                searchMusicInfoThread.setHandleComplete(new ThreadOverHandlerDao.HandleSaveLater() {
                    @Override
                    public void onDataHandleSuccess() {
                        Log.i(TAG, "onDataHandleSuccess: 处理结束！！！");
                    }

                    @Override
                    public void onDataHandleFailed() {
                        Log.i(TAG, "onDataHandleSuccess: 处理失败！！！");
                    }
                });
            }
        });
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
            // 未被授权
            // 是否被拒绝过，判断并给出提示
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    permission[0])){

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
                }
                break;
        }
    }

}
