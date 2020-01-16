package com.paulniu.iyingmusic.widget.dialog;

import com.paulniu.iyingmusic.R;
import com.paulniu.iyingmusic.widget.BaseDialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Coder: niupuyue
 * Date: 2020/1/16
 * Time: 16:04
 * Desc: 首页中文件夹长摁弹窗
 * Version:
 */
public class MainFolderLongClickDialog extends BaseDialog implements View.OnClickListener {

    public static final String SELECT_TYPE_DELETE = "delete";
    public static final String SELECT_TYPE_RENAME = "rename";

    public interface IMainFolderLongClickListener {
        void onSelect(String type);
    }

    private LinearLayout llMainFolderLongClickDelete;
    private LinearLayout llMainFolderLongClickRename;

    private View mRoot;

    private IMainFolderLongClickListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        if (null != dialog) {
            dialog.setCanceledOnTouchOutside(true);
            Window window = dialog.getWindow();
            window.requestFeature(Window.FEATURE_NO_TITLE);
        }
        mRoot = inflater.inflate(R.layout.dialog_main_folder_long_click, container, false);
        initViewById(mRoot);
        initListener();
        setCancelable(true);
        return mRoot;
    }

    private void initViewById(View root) {
        llMainFolderLongClickDelete = root.findViewById(R.id.llMainFolderLongClickDelete);
        llMainFolderLongClickRename = root.findViewById(R.id.llMainFolderLongClickRename);
    }

    private void initListener() {
        if (null != llMainFolderLongClickRename) {
            llMainFolderLongClickRename.setOnClickListener(this);
        }
        if (null != llMainFolderLongClickDelete) {
            llMainFolderLongClickDelete.setOnClickListener(this);
        }
    }

    //////////////////////////////////////////////////////
    //////////////////////////////////////////////////////
    ////////////////对外暴露的方法 开始
    //////////////////////////////////////////////////////
    //////////////////////////////////////////////////////
    public void setListener(IMainFolderLongClickListener listener) {
        this.listener = listener;
    }
    //////////////////////////////////////////////////////
    //////////////////////////////////////////////////////
    ////////////////对外暴露的方法 结束
    //////////////////////////////////////////////////////
    //////////////////////////////////////////////////////

    @Override
    public void onClick(View v) {
        if (null == v) {
            return;
        }
        if (null == listener) {
            return;
        }
        switch (v.getId()) {
            case R.id.llMainFolderLongClickDelete:
                listener.onSelect(SELECT_TYPE_DELETE);
                break;
            case R.id.llMainFolderLongClickRename:
                listener.onSelect(SELECT_TYPE_RENAME);
                break;
        }
        dismiss();
    }
}
