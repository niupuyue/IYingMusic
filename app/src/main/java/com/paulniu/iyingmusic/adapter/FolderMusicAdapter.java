package com.paulniu.iyingmusic.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.paulniu.iyingmusic.R;
import com.paulniu.iyingmusic.interfaces.IOnFolderMusicListener;
import com.paulniu.iyingmusic.model.MusicInfo;

import java.util.List;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-14
 * Time: 20:48
 * Desc: 文件夹中音乐列表
 * Version:
 */
public class FolderMusicAdapter extends BaseQuickAdapter<MusicInfo, FolderMusicAdapter.FolderMusicViewHolder> {

    private IOnFolderMusicListener listener;

    public FolderMusicAdapter(int layoutResId, @Nullable List<MusicInfo> data, IOnFolderMusicListener listener) {
        super(layoutResId, data);
        this.listener = listener;
    }

    @Override
    protected void convert(FolderMusicViewHolder helper, final MusicInfo item) {
        Context context = helper.llFolderMusicItemContainer.getContext();

        helper.tvFolderMusicItemName.setText(item.musicName);
        helper.tvFolderMusicItemArtist.setText(item.artist);
        helper.tvFolderMusicItemPath.setText(item.data);
        helper.tvFolderMusicItemDuration.setText(item.duration);
        helper.tvFolderMusicItemSize.setText(String.valueOf(item.size));

        Glide.with(context).load(context.getResources().getDrawable(R.mipmap.ic_launcher)).into(helper.ivFolderMusicItemAvator);

        helper.cbFolderMusicItemFavorite.setChecked(item.favorite == 0 ? false : true);
        helper.cbFolderMusicItemFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean favorite) {
                if (null != listener) {
                    listener.onFavorite(item, favorite);
                }
            }
        });
    }

    class FolderMusicViewHolder extends BaseViewHolder {

        private ImageView ivFolderMusicItemAvator;
        private CheckBox cbFolderMusicItemFavorite;
        private TextView tvFolderMusicItemName;
        private TextView tvFolderMusicItemArtist;
        private TextView tvFolderMusicItemPath;
        private TextView tvFolderMusicItemDuration;
        private TextView tvFolderMusicItemSize;

        private LinearLayout llFolderMusicItemContainer;

        public FolderMusicViewHolder(View view) {
            super(view);
            ivFolderMusicItemAvator = view.findViewById(R.id.ivFolderMusicItemAvator);
            cbFolderMusicItemFavorite = view.findViewById(R.id.cbFolderMusicItemFavorite);
            tvFolderMusicItemName = view.findViewById(R.id.tvFolderMusicItemName);
            tvFolderMusicItemArtist = view.findViewById(R.id.tvFolderMusicItemArtist);
            tvFolderMusicItemPath = view.findViewById(R.id.tvFolderMusicItemPath);
            tvFolderMusicItemDuration = view.findViewById(R.id.tvFolderMusicItemDuration);
            tvFolderMusicItemSize = view.findViewById(R.id.tvFolderMusicItemSize);

            llFolderMusicItemContainer = view.findViewById(R.id.llFolderMusicItemContainer);
        }
    }

}
