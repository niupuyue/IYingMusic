package com.paulniu.iyingmusic.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.paulniu.iyingmusic.R;
import com.paulniu.iyingmusic.adapter.FolderMusicAdapter;
import com.paulniu.iyingmusic.aidl.IPlayControl;
import com.paulniu.iyingmusic.aidl.Song;
import com.paulniu.iyingmusic.base.BaseActivity;
import com.paulniu.iyingmusic.db.entity.FolderInfo;
import com.paulniu.iyingmusic.db.entity.SongInfo;
import com.paulniu.iyingmusic.db.source.SongInfoSource;
import com.paulniu.iyingmusic.interfaces.IOnFolderMusicListener;
import com.paulniu.iyingmusic.interfaces.OnServiceConnect;
import com.paulniu.iyingmusic.interfaces.PlayServiceCallback;
import com.paulniu.iyingmusic.service.PlayServiceConnection;
import com.paulniu.iyingmusic.service.SongPlayController;
import com.paulniu.iyingmusic.service.SongPlayServiceManager;
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
public class FolderWithMusicListActivity extends BaseActivity implements View.OnClickListener, IOnFolderMusicListener, PlayServiceCallback, OnServiceConnect {

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

    private PlayServiceConnection sServiceConnection;
    private SongPlayServiceManager playServiceManager;
    private IPlayControl playControl;

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

        playServiceManager = new SongPlayServiceManager(this);
        // 初始化音乐播放服务
        sServiceConnection = new PlayServiceConnection(this, this, this);
        // 绑定成功后回调 onConnected
        playServiceManager.bindService(sServiceConnection);
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
        if (null != musicInfo && null != playControl) {
            // 点击音乐播放
            try {
                int currIndex = playControl.currentSongIndex();
                if (currIndex >=0 && currIndex<songInfos.size() && TextUtils.equals(musicInfo.data,songInfos.get(currIndex).data) && musicInfo.id == songInfos.get(currIndex).id){
                    if (null != playControl.currentSong()){
                        String curPath = playControl.currentSong().path;
                        if (!TextUtils.isEmpty(curPath) && TextUtils.equals(musicInfo.data,curPath) && playControl.status() != SongPlayController.STATUS_PLAYING){
                            playControl.resume();
                            return;
                        }
                    }
                }
                playControl.play(new Song(musicInfo.data));
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 初始化广播
     */
    private void initBroadcastReceivers(){

    }

    /**
     * 播放服务连接成功
     * @param name
     * @param service
     */
    @Override
    public void onConnected(ComponentName name, IBinder service) {
        // 注册广播
        initBroadcastReceivers();

        playControl = IPlayControl.Stub.asInterface(service);
    }

    /**
     * 播放服务连接失败
     * @param name
     */
    @Override
    public void disConnected(ComponentName name) {
        sServiceConnection = null;
        sServiceConnection = new PlayServiceConnection(this,this,this);
        playServiceManager.bindService(sServiceConnection);
    }

    /**
     * 切歌
     * @param song
     * @param index
     * @param isNext
     */
    @Override
    public void songChanged(Song song, int index, boolean isNext) {

    }

    /**
     * 开始播放
     * @param song
     * @param index
     * @param status
     */
    @Override
    public void startPlay(Song song, int index, int status) {

    }

    /**
     * 停止播放
     * @param song
     * @param index
     * @param status
     */
    @Override
    public void stopPlay(Song song, int index, int status) {

    }

    /**
     * 播放列表改变
     * @param current
     * @param index
     * @param id
     */
    @Override
    public void onPlayListChange(Song current, int index, int id) {

    }

    /**
     * 数据更新成功
     * @param mControl
     */
    @Override
    public void dataIsReady(IPlayControl mControl) {

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
