package com.paulniu.iyingmusic.widget.pop;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.paulniu.iyingmusic.R;
import com.paulniu.iyingmusic.adapter.MusicScanSelectFolderAdapter;
import com.paulniu.iyingmusic.db.entity.FolderInfo;
import com.paulniu.iyingmusic.db.entity.FolderInfoWithMusicCount;
import com.paulniu.iyingmusic.db.source.FolderInfoSource;
import com.paulniu.iyingmusic.db.source.SongInfoSource;
import com.paulniu.iyingmusic.interfaces.IOnMusicScanSelectFolderListener;
import com.paulniu.iyingmusic.utils.BaseUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Coder: niupuyue
 * Date: 2020/1/13
 * Time: 10:18
 * Desc: 扫描本地音乐选择要导入的文件夹
 * Version:
 */
public class MusicScanSelectFolderPop extends PopupWindow implements View.OnClickListener, IOnMusicScanSelectFolderListener {

    public interface OnMusicScanSelectFolderListener {
        void onConfirm(FolderInfo folderInfo);

        void onCancel();
    }

    private OnMusicScanSelectFolderListener listener;
    private Activity mActivity;
    private View mRoot;

    private ImageView ivMusicScanSelectFolderClose;
    private RecyclerView rvMusicScanSelectFolder;
    private EditText etMusicScanSelectFolderAddNewFolder;
    private TextView tvMusicScanSelectFolderNew;
    private TextView tvMusicScanSelectFolderAddNewFolderOK;
    private TextView tvMusicScanSelectFolderAddNewFolderCancel;
    private LinearLayout llMusicScanSelectFolderNormal;
    private LinearLayout llMusicScanSelectFolderNew;

    private String folderName;
    private List<FolderInfo> localFolders = new ArrayList<>();
    private MusicScanSelectFolderAdapter adapter;

    public MusicScanSelectFolderPop(Activity activity, OnMusicScanSelectFolderListener listener) {
        if (null == activity || null == listener) {
            return;
        }
        this.mActivity = activity;
        this.listener = listener;
        initContentView();
        initSetup();
    }

    private void initContentView() {
        if (null == mActivity) {
            return;
        }
        mRoot = LayoutInflater.from(mActivity).inflate(R.layout.view_pop_music_scan_select_folder, null);
        ivMusicScanSelectFolderClose = mRoot.findViewById(R.id.ivMusicScanSelectFolderClose);
        rvMusicScanSelectFolder = mRoot.findViewById(R.id.rvMusicScanSelectFolder);
        etMusicScanSelectFolderAddNewFolder = mRoot.findViewById(R.id.etMusicScanSelectFolderAddNewFolder);
        tvMusicScanSelectFolderNew = mRoot.findViewById(R.id.tvMusicScanSelectFolderNew);
        tvMusicScanSelectFolderAddNewFolderOK = mRoot.findViewById(R.id.tvMusicScanSelectFolderAddNewFolderOK);
        tvMusicScanSelectFolderAddNewFolderCancel = mRoot.findViewById(R.id.tvMusicScanSelectFolderAddNewFolderCancel);
        llMusicScanSelectFolderNormal = mRoot.findViewById(R.id.llMusicScanSelectFolderNormal);
        llMusicScanSelectFolderNew = mRoot.findViewById(R.id.llMusicScanSelectFolderNew);

        ivMusicScanSelectFolderClose.setOnClickListener(this);
        tvMusicScanSelectFolderNew.setOnClickListener(this);
        tvMusicScanSelectFolderAddNewFolderOK.setOnClickListener(this);
        tvMusicScanSelectFolderAddNewFolderCancel.setOnClickListener(this);
    }

    private void initSetup() {
        setFocusable(true);
        setOutsideTouchable(true);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setAnimationStyle(R.style.FadeAnimationShort);
        setContentView(mRoot);
        fitPopupWindowOverStatusBar(true);
        update();

        initData();
    }

    /**
     * 设置popupWindow全屏展示(但是不包括虚拟键盘)
     */
    public void fitPopupWindowOverStatusBar(boolean needFullScreen) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                Field mLayoutInScreen = PopupWindow.class.getDeclaredField("mLayoutInScreen");
                mLayoutInScreen.setAccessible(true);
                mLayoutInScreen.set(this, needFullScreen);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取本地数据库中文件夹数据
     */
    private void initData() {
        List<FolderInfoWithMusicCount> folderInfos = SongInfoSource.getFolderSongList();
        if (null != folderInfos && folderInfos.size() > 0) {
            rvMusicScanSelectFolder.setLayoutManager(new LinearLayoutManager(mActivity));
            adapter = new MusicScanSelectFolderAdapter(R.layout.item_music_scan_select_folder, folderInfos, this);
            rvMusicScanSelectFolder.setAdapter(adapter);
        }
    }

    /**
     * 创建文件夹
     */
    private void createNewFolder() {
        if (null != etMusicScanSelectFolderAddNewFolder) {
            folderName = etMusicScanSelectFolderAddNewFolder.getText().toString().trim();
            if (TextUtils.isEmpty(folderName)) {
                // 输入框是空
                Toast.makeText(mActivity, mActivity.getString(R.string.MusicScanSelectFolderPop_input_folder_name_empty), Toast.LENGTH_SHORT).show();
                return;
            }
            // 创建文件夹
            FolderInfo newFolder = new FolderInfo();
            newFolder.folderName = folderName;
            newFolder.folderPath = null;
            long folderId = FolderInfoSource.updateOrInsertFolder(newFolder);
            if (folderId > 0) {
                // 新建文件夹成功
                FolderInfo folderInfo = FolderInfoSource.getFolderInfoById((int) folderId);
                if (null != folderInfo && null != listener) {
                    listener.onConfirm(folderInfo);
                }
            } else {
                // 插入失败
                Toast.makeText(mActivity, mActivity.getString(R.string.MusicScanSelectFolderPop_new_folder_fail), Toast.LENGTH_SHORT).show();
            }
            dismiss();
        }
    }

    /**
     * 选择文件夹导入
     *
     * @param folderInfo
     */
    @Override
    public void onSelectFolder(FolderInfo folderInfo) {
        if (null != folderInfo && null != listener) {
            listener.onConfirm(folderInfo);
            dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        if (null == v) {
            return;
        }
        switch (v.getId()) {
            case R.id.ivMusicScanSelectFolderClose:
                dismiss();
                break;
            case R.id.tvMusicScanSelectFolderNew:
                if (null != llMusicScanSelectFolderNormal) {
                    llMusicScanSelectFolderNormal.setVisibility(View.GONE);
                }
                if (null != llMusicScanSelectFolderNew) {
                    llMusicScanSelectFolderNew.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tvMusicScanSelectFolderAddNewFolderOK:
                // 创建新的文件夹，并且使用文件夹
                createNewFolder();
                break;
            case R.id.tvMusicScanSelectFolderAddNewFolderCancel:
                if (null != llMusicScanSelectFolderNormal) {
                    llMusicScanSelectFolderNormal.setVisibility(View.VISIBLE);
                }
                if (null != llMusicScanSelectFolderNew) {
                    llMusicScanSelectFolderNew.setVisibility(View.GONE);
                }
                // 关闭软键盘
                BaseUtils.CloseKeyBord(mActivity);
                break;
        }
    }
}
