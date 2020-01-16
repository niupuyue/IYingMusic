package com.paulniu.iyingmusic.widget.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.paulniu.iyingmusic.R;
import com.paulniu.iyingmusic.widget.BaseDialog;

/**
 * Coder: niupuyue
 * Date: 2020/1/16
 * Time: 15:28
 * Desc: 添加文件夹弹窗
 * Version:
 */
public class FolderAddDialog extends BaseDialog implements View.OnClickListener {

    public static final String TYPE_NEW = "newFolder";
    public static final String TYPE_RENAME = "renameFolder";

    public interface IFolderAddDialogListener {
        void onConfirm(String folderName);

        void onCancel();
    }

    private TextView tvDialogFolderAddConfirm;
    private TextView tvDialogFolderAddCancel;
    private EditText etDialogFolderInput;
    private TextView tvFolderAddDialogTitle;
    private View mRoot;

    private IFolderAddDialogListener listener;

    private String type = TYPE_NEW;

    public FolderAddDialog(String type) {
        this.type = type;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        if (null != dialog) {
            dialog.setCanceledOnTouchOutside(false);
            Window window = dialog.getWindow();
            window.requestFeature(Window.FEATURE_NO_TITLE);
        }
        mRoot = inflater.inflate(R.layout.dialog_folder_add, container, false);
        initViewById(mRoot);
        initListener();
        initData();
        setCancelable(false);
        return mRoot;
    }

    private void initViewById(View mRoot) {
        tvDialogFolderAddConfirm = mRoot.findViewById(R.id.tvDialogFolderAddConfirm);
        tvDialogFolderAddCancel = mRoot.findViewById(R.id.tvDialogFolderAddCancel);
        etDialogFolderInput = mRoot.findViewById(R.id.etDialogFolderInput);
        tvFolderAddDialogTitle = mRoot.findViewById(R.id.tvFolderAddDialogTitle);
    }

    private void initListener() {
        if (null != tvDialogFolderAddConfirm) {
            tvDialogFolderAddConfirm.setOnClickListener(this);
        }
        if (null != tvDialogFolderAddCancel) {
            tvDialogFolderAddCancel.setOnClickListener(this);
        }
    }

    private void initData() {
        if (TextUtils.equals(type, TYPE_NEW)) {
            // 新建文件夹
            if (null != tvFolderAddDialogTitle) {
                tvFolderAddDialogTitle.setText(getString(R.string.MainActivity_add_folder_dialog_title));
            }
            if (null != etDialogFolderInput) {
                etDialogFolderInput.setHint(getString(R.string.MainActivity_add_folder_dialog_input_hint));
            }
            if (null != tvDialogFolderAddConfirm) {
                tvDialogFolderAddConfirm.setText(getString(R.string.MainActivity_add_folder_dialog_add));
            }
        } else if (TextUtils.equals(type, TYPE_RENAME)) {
            // 重命名文件夹
            if (null != tvFolderAddDialogTitle) {
                tvFolderAddDialogTitle.setText(getString(R.string.MainActivity_rename_folder_title));
            }
            if (null != etDialogFolderInput) {
                etDialogFolderInput.setHint(getString(R.string.MainActivity_rename_folder_input_hint));
            }
            if (null != tvDialogFolderAddConfirm) {
                tvDialogFolderAddConfirm.setText(getString(R.string.MainActivity_rename_folder_confirm));
            }
        }
    }

    private void setOnConfirmClick() {
        if (null != etDialogFolderInput) {
            String folderName = etDialogFolderInput.getText().toString();
            if (TextUtils.isEmpty(folderName)) {
                Toast.makeText(getContext(), getString(R.string.MainActivity_add_folder_dialog_foldername_empty), Toast.LENGTH_SHORT).show();
                return;
            } else {
                listener.onConfirm(folderName);
                dismiss();
            }
        }
    }

    //////////////////////////////////////////////////////
    //////////////////////////////////////////////////////
    ////////////////对外暴露的方法 开始
    //////////////////////////////////////////////////////
    //////////////////////////////////////////////////////
    public void setListener(IFolderAddDialogListener listener) {
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
            case R.id.tvDialogFolderAddCancel:
                listener.onCancel();
                dismiss();
                break;
            case R.id.tvDialogFolderAddConfirm:
                setOnConfirmClick();
                break;
        }
    }
}
