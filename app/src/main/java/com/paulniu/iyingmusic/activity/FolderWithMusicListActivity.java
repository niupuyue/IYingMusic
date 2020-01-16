package com.paulniu.iyingmusic.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.paulniu.iyingmusic.R;
import com.paulniu.iyingmusic.adapter.FolderMusicAdapter;
import com.paulniu.iyingmusic.base.BaseActivity;
import com.paulniu.iyingmusic.db.entity.FolderInfo;
import com.paulniu.iyingmusic.db.entity.SongInfo;
import com.paulniu.iyingmusic.db.source.SongInfoSource;
import com.paulniu.iyingmusic.interfaces.IOnFolderMusicListener;
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
public class FolderWithMusicListActivity extends BaseActivity implements View.OnClickListener, IOnFolderMusicListener {

    private static String EXTRA_OBJECT_FOLDERINFO = "folderInfo";

    public static Intent getIntent(Context content, FolderInfo folderInfo) {
        Intent intent = new Intent(content, FolderWithMusicListActivity.class);
        intent.putExtra(EXTRA_OBJECT_FOLDERINFO, folderInfo);
        return intent;
    }

    private MyAppTitle myAppTitle;
    private RecyclerView rvFolderWithMusicList;
    private LinearLayout llFolderWithMusicListContainer;
    private View viewErrorFolderMusic;

    private List<SongInfo> songInfos = new ArrayList<>();
    private FolderInfo folderInfo;
    private FolderMusicAdapter adapter;

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

        llFolderWithMusicListContainer = findViewById(R.id.llFolderWithMusicListContainer);
        rvFolderWithMusicList = findViewById(R.id.rvFolderWithMusicList);

        viewErrorFolderMusic = findViewById(R.id.viewErrorFolderMusic);
    }

    @Override
    public void initData() {
        if (null == folderInfo) {
            return;
        }
        if (null != myAppTitle) {
            myAppTitle.setAppTitle(folderInfo.folderName);
            myAppTitle.setOnLeftButtonClickListener(new MyAppTitle.OnLeftButtonClickListener() {
                @Override
                public void onLeftButtonClick(View v) {
                    onBackPressed();
                }
            });
        }
        // 根据folderId查找音乐
        if (null != songInfos) {
            if (null != viewErrorFolderMusic) {
                viewErrorFolderMusic.setVisibility(View.GONE);
            }
            if (null != llFolderWithMusicListContainer) {
                llFolderWithMusicListContainer.setVisibility(View.VISIBLE);
            }
            songInfos.clear();
            songInfos = SongInfoSource.getSongInfosByFolderId(folderInfo.folderId);
            adapter = new FolderMusicAdapter(R.layout.item_folder_music, songInfos, this);
            LinearLayoutManager manager = new LinearLayoutManager(this);
            rvFolderWithMusicList.setLayoutManager(manager);
            rvFolderWithMusicList.setAdapter(adapter);
        } else {
            if (null != viewErrorFolderMusic) {
                viewErrorFolderMusic.setVisibility(View.VISIBLE);
            }
            if (null != llFolderWithMusicListContainer) {
                llFolderWithMusicListContainer.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void initListener() {
        if (null != viewErrorFolderMusic) {
            viewErrorFolderMusic.setOnClickListener(this);
        }
    }

    @Override
    public void onFavorite(SongInfo musicInfo, boolean isFavorite) {
        if (null != songInfos && songInfos.size() > 0 && null != musicInfo) {
            int position = -1;
            for (int i = 0; i < songInfos.size(); i++) {
                if (null != songInfos.get(i) && TextUtils.equals(musicInfo.data, songInfos.get(i).data) && TextUtils.equals(musicInfo.songName, songInfos.get(i).songName)) {
                    position = i;
                    break;
                }
            }
            songInfos.get(position).favorite = isFavorite;
            if (null != adapter) {
                adapter.notifyDataSetChanged();
            }
            // 将修改内容写入数据库
            SongInfoSource.updateSongInfoToFavorite(musicInfo, isFavorite);
            Toast.makeText(this, getString(R.string.FolderWithMusicListActivity_add_myfavorite_success), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMusicItemClick(SongInfo musicInfo) {
        if (null != musicInfo) {
            // 点击音乐播放
        }
    }

    @Override
    public void onClick(View v) {
        if (null == v) {
            return;
        }
        switch (v.getId()) {
            case R.id.viewErrorFolderMusic:
                // 重新加载数据
                initData();
                break;
        }
    }

}
