package com.paulniu.iyingmusic.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.paulniu.iyingmusic.R;
import com.paulniu.iyingmusic.base.BaseActivity;
import com.paulniu.iyingmusic.db.entity.FolderInfo;
import com.paulniu.iyingmusic.db.source.MusicInfoSource;
import com.paulniu.iyingmusic.model.MusicInfo;
import com.paulniu.iyingmusic.widget.MyAppTitle;

import java.util.ArrayList;
import java.util.List;

/**
 * Coder: niupuyue
 * Date: 2020/1/14
 * Time: 18:55
 * Desc: 文件夹详情页面
 * Version:
 */
public class FolderWithMusicListActivity extends BaseActivity implements View.OnClickListener {

    private static String EXTRA_OBJECT_FOLDERINFO = "folderInfo";

    public static Intent getIntent(Context content, FolderInfo folderInfo) {
        Intent intent = new Intent(content, FolderWithMusicListActivity.class);
        intent.putExtra(EXTRA_OBJECT_FOLDERINFO, folderInfo);
        return intent;
    }

    private MyAppTitle myAppTitle;

    private List<MusicInfo> musicInfos = new ArrayList<>();
    private FolderInfo folderInfo;

    @Override
    public int initViewLayout() {
        return R.layout.activity_folder_with_music;
    }

    @Override
    public void initExtraData() {
        folderInfo = getIntent().getParcelableExtra(EXTRA_OBJECT_FOLDERINFO);
    }

    @Override
    public void initViewById() {
        myAppTitle = findViewById(R.id.myAppTitle);
    }

    @Override
    public void initData() {
        if (null == folderInfo) {
            return;
        }
        if (null != myAppTitle) {
            myAppTitle.setAppTitle(folderInfo.folderName);
        }
        // 根据folderId查找音乐
        if (null != musicInfos) {
            musicInfos.clear();
            musicInfos = MusicInfoSource.getMusicInfosByFolderId(folderInfo.folderId);

        }
    }

    @Override
    public void onClick(View v) {

    }
}
