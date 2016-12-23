package com.example.shize.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 旋转唱片动画组件
 * Created by shize on 2016/12/16.
 */

public class RotatingAnimation extends ImageView {

    public RotatingAnimation(Context context) {
        super(context);
    }

    public RotatingAnimation(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RotatingAnimation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAnimation();
    }

    private void initAnimation() {
    }
}
