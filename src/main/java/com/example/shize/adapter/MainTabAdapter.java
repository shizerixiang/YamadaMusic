package com.example.shize.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * 自定义tabLayout和pagerView适配器
 * Created by shize on 2016/11/10.
 */
public class MainTabAdapter extends FragmentPagerAdapter{

    private ArrayList<Fragment> fragments;
    private ArrayList<String> titles;

    public MainTabAdapter(FragmentManager fm, ArrayList<Fragment> fragments,
                          ArrayList<String> titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    /**
     * 显示tab上的文字
     * @param position 当前tab位置
     * @return tab上的文字
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
