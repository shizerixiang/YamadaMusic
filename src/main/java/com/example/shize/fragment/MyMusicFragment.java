package com.example.shize.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.shize.activity.IndexLatelyMusicActivity;
import com.example.shize.activity.IndexLikeMusicActivity;
import com.example.shize.activity.IndexLocalMusicActivity;
import com.example.shize.adapter.MainIndexListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 我的音乐界面
 * Created by shize on 2016/11/10.
 */
public class MyMusicFragment extends Fragment {

    private List<HashMap<String, Object>> indexListData;
    private MainIndexListAdapter adapter;
    private View myMusicFragment;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myMusicFragment = inflater.inflate(R.layout.activity_main_fragment_my_music, container, false);
        context = container.getContext();
        // 初始化index内容
        initIndexPage();
        return myMusicFragment;
    }

    /**
     * 初始化index内容
     */
    private void initIndexPage(){
        // 设置数据
        ListView indexList = (ListView) myMusicFragment.findViewById(R.id.fragment_my_music_index_list);
        indexListData = new ArrayList<>();
        HashMap<String, Object> hashMap1 = new HashMap<>();
        hashMap1.put("image", R.drawable.main_index_local_music_icon);
        hashMap1.put("text", getString(R.string.main_index_local_music_text));
        indexListData.add(hashMap1);
        HashMap<String, Object> hashMap2 = new HashMap<>();
        hashMap2.put("image", R.drawable.main_index_like_music_icon);
        hashMap2.put("text", getString(R.string.main_index_like_music_text));
        indexListData.add(hashMap2);
        HashMap<String, Object> hashMap3 = new HashMap<>();
        hashMap3.put("image", R.drawable.main_index_lately_music_icon);
        hashMap3.put("text", getString(R.string.main_index_lately_music_text));
        indexListData.add(hashMap3);
        adapter = new MainIndexListAdapter(context,indexListData,
                R.layout.fragment_my_music_page_index_item,new String[]{"image","text"},
                new int[]{R.id.my_music_index_item_image,R.id.my_music_index_item_text});
        indexList.setAdapter(adapter);
        // 设置点击子项数据
        indexList.setOnItemClickListener(new onIndexItemClickListener());
    }

    /**
     * 索引子项点击事件监听类
     */
    private class onIndexItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent;
            switch (position){
                case 0:
                    intent = new Intent(context, IndexLocalMusicActivity.class);
                    startActivity(intent);
                    break;
                case 1:
                    intent = new Intent(context, IndexLikeMusicActivity.class);
                    startActivity(intent);
                    break;
                case 2:
                    intent = new Intent(context, IndexLatelyMusicActivity.class);
                    startActivity(intent);
                    break;

            }
        }
    }
}
