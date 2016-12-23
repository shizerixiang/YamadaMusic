package com.example.shize.activity;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.example.shize.fragment.R;

/**
 * 更换主题界面
 * Created by shize on 2016/11/14.
 */
public class ThemeActivity extends TransparentActivity {

    private static boolean isChanged = false;
    private ImageButton testImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_main);
        initHeader();
        testImage = (ImageButton) findViewById(R.id.animation_test);
        testImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimatedVectorDrawable animatedVectorDrawable =
                        (AnimatedVectorDrawable) testImage.getDrawable();
                if (animatedVectorDrawable.isRunning()) {
                    animatedVectorDrawable.stop();
                } else {
                    if (isChanged){
                        testImage.setImageResource(R.drawable.animvectordrawable_music);
                        isChanged = false;
                    } else {
                        testImage.setImageResource(R.drawable.animvectordrawable_music2);
                        isChanged = true;
                    }
                    ((AnimatedVectorDrawable) testImage.getDrawable()).start();
                }
            }
        });
    }

    /**
     * 初始化标题栏
     */
    private void initHeader(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        assert toolbar != null;
        toolbar.setTitle(getString(R.string.nav_item_design));
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
            ThemeActivity.this.finish();
        }
    }
}
