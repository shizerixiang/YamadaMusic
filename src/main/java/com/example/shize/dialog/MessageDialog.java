package com.example.shize.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.shize.dao.PlayerDao;
import com.example.shize.fragment.R;
import com.example.shize.util.PlayerUtil;

/**
 * 长按弹出框
 * Created by shize on 2016/12/12.
 */
public class MessageDialog extends Dialog {

    public MessageDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
    }

    /**
     * 初始化窗口
     */
    private void initWindow() {
        Window window = getWindow();
        assert window != null;
        window.getDecorView().setPadding(0,0,0,0);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.windowAnimations = R.style.AlertAnimation;
        window.setAttributes(layoutParams);
    }

    /**
     * 构造器
     */
    public static class Builder {
        private Context context;
        // Dialog标题
        private String title;
        // 列表数据适配器
        private BaseAdapter mAdapter;
        // 列表子项点击事件
        private PlayerDao.DialogListItemDao mDialogListItemDao;
        private MessageDialog dialog;

        /**
         * 构造Dialog
         * @param context 上下文
         */
        public Builder(Context context){
            this.context = context;
        }

        /**
         * 构造Dialog
         * @param context 上下文
         * @param mAdapter listView适配器
         */
        public Builder(Context context, BaseAdapter mAdapter){
            this.context = context;
            this.mAdapter = mAdapter;
        }

        /**
         * 构造Dialog
         * @param context 上下文
         * @param mAdapter listView适配器
         * @param title Dialog标题
         */
        public Builder(Context context, BaseAdapter mAdapter, String title){
            this.context = context;
            this.mAdapter = mAdapter;
            this.title = title;
        }

        /**
         * 设置标题
         * @param title 标题
         * @return 构造器
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * 设置listView适配器
         * @param mAdapter 适配器
         * @return 构造器
         */
        public Builder setAdapter(BaseAdapter mAdapter) {
            this.mAdapter = mAdapter;
            return this;
        }

        /**
         * 设置listView的子项点击事件
         * @return 构造器
         */
        public Builder setOnDialogListItemClickListener(PlayerDao.DialogListItemDao mDialogListItemDao){
            this.mDialogListItemDao = mDialogListItemDao;
            return this;
        }

        /**
         * 创建Dialog
         * @return 返回自定义弹窗
         */
        public MessageDialog create(){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            dialog = new MessageDialog(context, R.style.MessageDialog);
            View layout = inflater.inflate(R.layout.dialog_menu, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            initList(layout);
            dialog.setContentView(layout);
            return dialog;
        }

        /**
         * 初始化列表
         * @param layout 布局视图
         */
        private void initList(View layout) {
            ListView listView = (ListView) layout.findViewById(R.id.dialog_list);
            if (mAdapter != null) {
                listView.setAdapter(mAdapter);
                if (mDialogListItemDao != null) {
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position,
                                                long id) {
                            mDialogListItemDao.onDialogListItemClickListener(position);
                            dialog.dismiss();
                        }
                    });
                }
                listView.setLayoutAnimation(PlayerUtil.AnimationUtil.getListFlyAnimation());
            }

            if (title != null){
                ((TextView)layout.findViewById(R.id.dialog_menu_title)).setText(title);
            }
        }
    }
}
