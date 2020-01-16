package com.paulniu.iyingmusic.adapter;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.paulniu.iyingmusic.R;
import com.paulniu.iyingmusic.db.entity.SongInfo;
import com.paulniu.iyingmusic.interfaces.IOnMusicScanSelectMusicListener;

import java.util.List;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-12
 * Time: 22:24
 * Desc: 扫描本地存储中的音乐文件适配器
 * Version:
 */
public class MusicScanAdapter extends BaseQuickAdapter<SongInfo, MusicScanAdapter.MusicScanViewHolder> {

    private IOnMusicScanSelectMusicListener listener;

    public MusicScanAdapter(int layoutResId, @Nullable List<SongInfo> data, IOnMusicScanSelectMusicListener listener) {
        super(layoutResId, data);
        this.listener = listener;
    }

    @Override
    protected void convert(MusicScanViewHolder helper, final SongInfo item) {
        helper.tvMusicScanItemName.setText(item.songName);
        helper.tvMusicScanItemPath.setText(item.data);
        helper.tvMusicScanItemArtist.setText(item.artist);
        helper.tvMusicScanItemDuration.setText(String.valueOf(item.duration));
        if (item.isShowChecked) {
            helper.cbMusicScanItemSelect.setVisibility(View.VISIBLE);
            if (item.isChecked) {
                helper.cbMusicScanItemSelect.setChecked(true);
            } else {
                helper.cbMusicScanItemSelect.setChecked(false);
            }
        } else {
            helper.cbMusicScanItemSelect.setVisibility(View.GONE);
        }
        helper.cbMusicScanItemSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (null == listener) {
                    return;
                }
                if (isChecked) {
                    listener.onMusicScanSelectMusic(item);
                } else {
                    listener.onMusicScanUnselectMusic(item);
                }
            }
        });
        // 设置图片 TODO 因为没有图片，所以暂时随机出来图片
    }

    class MusicScanViewHolder extends BaseViewHolder {

        private ImageView ivMusicScanItemAvator;
        private TextView tvMusicScanItemName;
        private TextView tvMusicScanItemPath;
        private TextView tvMusicScanItemDuration;
        private TextView tvMusicScanItemArtist;
        private CheckBox cbMusicScanItemSelect;

        public MusicScanViewHolder(View view) {
            super(view);
            ivMusicScanItemAvator = view.findViewById(R.id.ivMusicScanItemAvator);
            tvMusicScanItemName = view.findViewById(R.id.tvMusicScanItemName);
            tvMusicScanItemPath = view.findViewById(R.id.tvMusicScanItemPath);
            tvMusicScanItemDuration = view.findViewById(R.id.tvMusicScanItemDuration);
            tvMusicScanItemArtist = view.findViewById(R.id.tvMusicScanItemArtist);
            cbMusicScanItemSelect = view.findViewById(R.id.cbMusicScanItemSelect);
        }
    }
}
