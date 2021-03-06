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
import com.paulniu.iyingmusic.db.entity.SongInfo;
import com.paulniu.iyingmusic.interfaces.IOnFolderMusicListener;
import com.paulniu.iyingmusic.utils.StringUtils;

import java.util.List;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-14
 * Time: 20:48
 * Desc: 文件夹中音乐列表
 * Version:
 */
public class FolderMusicAdapter extends BaseQuickAdapter<SongInfo, FolderMusicAdapter.FolderMusicViewHolder> {

    private IOnFolderMusicListener listener;

    public FolderMusicAdapter(int layoutResId, @Nullable List<SongInfo> data, IOnFolderMusicListener listener) {
        super(layoutResId, data);
        this.listener = listener;
    }

    @Override
    protected void convert(FolderMusicViewHolder helper, final SongInfo item) {
        Context context = helper.llFolderMusicItemContainer.getContext();

        helper.tvFolderMusicItemName.setText(item.songName);
        helper.tvFolderMusicItemArtist.setText(item.artist);
        helper.tvFolderMusicItemPath.setText(item.data);
        helper.tvFolderMusicItemDuration.setText(StringUtils.getGenTimeMS(item.duration));
        helper.tvFolderMusicItemSize.setText((item.size >> 10 >> 10 )+ " MB");

        Glide.with(context).load(context.getResources().getDrawable(R.mipmap.ic_launcher)).into(helper.ivFolderMusicItemAvator);

        helper.cbFolderMusicItemFavorite.setChecked(item.favorite);
        helper.cbFolderMusicItemFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean favorite) {
                if (null != listener) {
                    listener.onFavorite(item, favorite);
                }
            }
        });

        helper.llFolderMusicItemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onMusicItemClick(item);
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
