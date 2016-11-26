package com.example.shize.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * 我的音乐索引列表适配器
 * Created by shize on 2016/11/17.
 */
public class MainIndexListAdapter extends BaseAdapter {

    private List<HashMap<String,Object>> indexList;
    private String[] keys;
    private int[] values;
    private int itemId;
    private LayoutInflater mInflater;

    /**
     * 构造索引列表适配器
     * @param context 上下文
     * @param indexList 适配数据
     * @param itemId 适配列表子项id
     * @param keys 数据集合键
     * @param values 数据集合键对应控件的id
     */
    public MainIndexListAdapter(Context context,List<HashMap<String,Object>> indexList, int itemId,
                               String[] keys, int[] values) {
        this.indexList = indexList;
        this.itemId = itemId;
        this.keys = keys;
        this.values = values;
        mInflater = LayoutInflater.from(context);
        Log.i("SearchMusic", "MainIndexListAdapter: construct");
    }

    @Override
    public int getCount() {
        return indexList.size();
    }

    @Override
    public Object getItem(int position) {
        return indexList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 设置子项和其控件
     * @param position 子项位置
     * @param convertView 上一个子项
     * @param parent 父容器
     * @return 子项
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i("SearchMusic", "MainIndexListAdapter: 开始获取视图");
        ViewHolder viewHolder;
        // 判断是否缓存
        if (convertView == null) {
            Log.i("SearchMusic", "MainIndexListAdapter: 开始缓存视图");
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(itemId,null);
            viewHolder.imageView = (ImageView) convertView.findViewById(values[0]);
            viewHolder.textView = (TextView) convertView.findViewById(values[1]);
            convertView.setTag(viewHolder);
        } else {
            Log.i("SearchMusic", "MainIndexListAdapter: 开始读取视图");
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.imageView.setImageResource((Integer) indexList.get(position).get(keys[0]));
        viewHolder.textView.setText((String)indexList.get(position).get(keys[1]));
        Log.i("SearchMusic", "MainIndexListAdapter: 完成视图获取");
        return convertView;
    }

    /**
     * 缓存机制，增加滑动流畅性
     */
    public final class ViewHolder{
        public ImageView imageView;
        public TextView textView;
    }
}
