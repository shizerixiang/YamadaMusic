package com.example.shize.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.shize.fragment.R;

/**
 * 定时界面
 * Created by shize on 2016/11/14.
 */
public class ClockActivity extends TransparentActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_main);
        initHeader();
    }

    /**
     * 初始化标题栏
     */
    private void initHeader(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        assert toolbar != null;
        toolbar.setTitle(getString(R.string.nav_item_custom));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_exit);
        toolbar.setNavigationOnClickListener(new OnBackClickListener());
    }

    /**
     * 返回按钮点击事件监听类
     */
    private class OnBackClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            ClockActivity.this.finish();
        }
    }
}
