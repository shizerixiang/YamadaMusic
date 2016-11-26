package com.example.shize.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shize.adapter.MainTabAdapter;
import com.example.shize.fragment.MusicLibFragment;
import com.example.shize.fragment.MusicRankingFragment;
import com.example.shize.fragment.MyMusicFragment;
import com.example.shize.fragment.R;
import com.example.shize.service.MusicPlayerService;
import com.example.shize.util.uiutil.ImageUtil;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final static String TAG = "yamada_music_main";
    private DrawerLayout drawerLayout;
    private TextView titleTxt;
    private TextView artistTxt;
    private ImageButton playBtn;
    private MusicInfoReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_main);
        initControls();
    }

    /**
     * 初始化控件
     */
    private void initControls(){
        // 初始化抽屉内容
        initContent();
        // 初始化抽屉侧边栏
        initDrawer();
        // 初始化底部迷你播放器
        initMiniPlayer();
    }

    /**
     * 初始化底部迷你播放器
     */
    private void initMiniPlayer() {
        // 注册广播接收器
        receiver = new MainActivity.MusicInfoReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MusicPlayerService.BROADCAST_ACTION_RECEIVER);
        this.registerReceiver(receiver, filter);

        // 加载底部图片
        ImageView singerIcon = (ImageView) findViewById(R.id.main_under_image);
        assert singerIcon != null : TAG+"未找到底部歌手图标控件";
        singerIcon.setOnTouchListener(new OnSingerIconTouchListener());

        // 初始化底部歌曲标题
        titleTxt = (TextView) findViewById(R.id.main_under_music_name);
        artistTxt = (TextView) findViewById(R.id.main_under_singer_name);

        // 初始化列表事件
        ImageButton playList = (ImageButton) findViewById(R.id.main_under_list_image);
        assert playList != null;
        playList.setOnClickListener(new OnPlayListClickListener());

        // 初始化底部播放按钮
        playBtn = (ImageButton) findViewById(R.id.main_under_action_image);
        assert playBtn != null;
        playBtn.setOnClickListener(new OnPlayButtonClickListener());

        // 初始化底部下一首按钮
        ImageButton nextBtn = (ImageButton) findViewById(R.id.main_under_next_image);
        assert nextBtn != null;
        nextBtn.setOnClickListener(new OnNextButtonClickListener());
    }

    /**
     * 侧边栏的初始化
     */
    private void initDrawer() {
        // 加载toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        // 设置抽屉侧边栏
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawerLayout != null : TAG + "未找到抽屉侧边栏！";
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        assert navigationView != null : TAG + "未找到NavigationView";
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * 初始化抽屉内容
     */
    private void initContent(){
        // 加载fragment
        ArrayList<Fragment> fragments = new ArrayList<>();
        MyMusicFragment myMusicFragment = new MyMusicFragment();
        MusicLibFragment musicLibFragment = new MusicLibFragment();
        MusicRankingFragment musicRankingFragment = new MusicRankingFragment();

        fragments.add(myMusicFragment);
        fragments.add(musicLibFragment);
        fragments.add(musicRankingFragment);

        ArrayList<String> titles = new ArrayList<>();
        titles.add(getString(R.string.menu_tab_my_music));
        titles.add(getString(R.string.menu_tab_lib_music));
        titles.add(getString(R.string.menu_tab_ranking_music));

        // 加载tabLayout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tab_layout);

        assert tabLayout != null;
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(2)));

        // 加载viewPager
        ViewPager viewPager = (ViewPager) findViewById(R.id.main_view_pager);
        MainTabAdapter myTabAdapter = new MainTabAdapter(this.getSupportFragmentManager(),
                fragments, titles);
        assert viewPager != null;
        viewPager.setAdapter(myTabAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * 创建菜单方法
     * @param menu 菜单
     * @return 返回boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_action_bar, menu);
        return true;
    }

    /**
     * 菜单子项事件监听
     * @param item 点击子项
     * @return 是否拦截
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.menu_action_bar_search:
                break;
            case R.id.menu_action_bar_collection:
                break;
            case R.id.menu_action_bar_option:
                intent = new Intent(this,OptionActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_action_bar_about:
                intent = new Intent(this,AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_action_bar_exit:
                onBackPressed();
        }
        return true;
    }

    /**
     * 监听返回按键反应抽屉动作
     */
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 抽屉侧边栏选项点击事件
     * @param item 点击子项
     * @return 是否拦截
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.nav_design:
                intent = new Intent(this,ThemeActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_night:
                break;
            case R.id.nav_listen:
                intent = new Intent(this,ListenerActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_custom:
                intent = new Intent(this,ClockActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_option:
                intent = new Intent(this,OptionActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_search:
                intent = new Intent(this,SearchMusicActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_exit:
                onBackPressed();
                finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 头像点击事件监听类
     */
    private class OnSingerIconTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Intent intent = new Intent(MainActivity.this, PlayActivity.class);
            // 设置元素关联动画shared Element效果（同时关联两个View）
            Pair onePair = new Pair<>(findViewById(R.id.main_under_action),
                            getString(R.string.transition_singer_name));
            Pair twoPair =  new Pair<>(findViewById(R.id.main_under_image),
                    getString(R.string.transition_singer_icon));
            ActivityOptionsCompat activityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,
                           onePair, twoPair);
            startActivity(intent,activityOptionsCompat.toBundle());
            // 设置系统默认淡入淡出效果
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            return false;
        }
    }

    /**
     * 播放列表点击事件监听类
     */
    private class OnPlayListClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, PlayListActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 销毁服务和广播接收器
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        Intent intent = new Intent(this, MusicPlayerService.class);
        stopService(intent);
    }

    /**
     * 迷你播放器播放/暂停按钮点击事件监听类
     */
    private class OnPlayButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, MusicPlayerService.class);
            if (MusicPlayerService.playerOpenState){
                intent.setAction(MusicPlayerService.ACTION_PAUSE);
            } else {
                intent.setAction(MusicPlayerService.ACTION_PLAY);
            }
            startService(intent);
        }
    }

    /**
     * 迷你播放器下一首按钮点击事件监听类
     */
    private class OnNextButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, MusicPlayerService.class);
            intent.setAction(MusicPlayerService.ACTION_NEXT);
            startService(intent);
        }
    }

    /**
     * 歌曲信息的广播接收器
     */
    public class MusicInfoReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive: 接受了一个广播！！！");
            titleTxt.setText(intent.getStringExtra("title"));
            artistTxt.setText(intent.getStringExtra("artist"));
            playBtn.setImageResource(ImageUtil.getMiniPlayImage(
                    intent.getBooleanExtra("state", false)));
            if (intent.getBooleanExtra("close", false)){
                // 关闭程序
                MainActivity.this.finish();
            }
        }
    }
}
