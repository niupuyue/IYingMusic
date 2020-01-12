package com.paulniu.iyingmusic.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.paulniu.iyingmusic.R;
import com.paulniu.iyingmusic.model.MusicInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-12
 * Time: 22:24
 * Desc: 扫描本地存储中的音乐文件适配器
 * Version:
 */
public class MusicScanAdapter extends BaseQuickAdapter<MusicInfo, BaseViewHolder> {

    private List<MusicInfo> musicInfos;

    public MusicScanAdapter(int layoutResId, @Nullable List<MusicInfo> data) {
        super(layoutResId, data);
        if (null != data) {
            musicInfos = data;
        } else {
            musicInfos = new ArrayList<>();
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, MusicInfo item) {
        helper.setText(R.id.tvMusicScanItemName, item.musicName);
        helper.setText(R.id.tvMusicScanItemPath, item.data);
        helper.setText(R.id.tvMusicScanItemArtist, item.artist);
        helper.setText(R.id.tvMusicScanItemDuration, item.duration);
        // 设置图片 TODO 因为没有图片，所以暂时随机出来图片
    }

    class MusicScanViewHolder extends BaseViewHolder {

        private ImageView ivMusicScanItemAvator;
        private TextView tvMusicScanItemName;
        private TextView tvMusicScanItemPath;
        private TextView tvMusicScanItemDuration;
        private TextView tvMusicScanItemArtist;

        public MusicScanViewHolder(View view) {
            super(view);
        }
    }
}
