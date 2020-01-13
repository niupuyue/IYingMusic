package com.paulniu.iyingmusic.adapter;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.paulniu.iyingmusic.R;
import com.paulniu.iyingmusic.db.entity.FolderInfo;
import com.paulniu.iyingmusic.db.entity.FolderInfoWithMusicCount;
import com.paulniu.iyingmusic.interfaces.IOnMusicScanSelectFolderListener;

import java.util.List;

/**
 * Coder: niupuyue
 * Date: 2020/1/13
 * Time: 16:52
 * Desc: 扫描本地音乐 选择导入的文件夹adapter
 * Version:
 */
public class MusicScanSelectFolderAdapter extends BaseQuickAdapter<FolderInfoWithMusicCount, MusicScanSelectFolderAdapter.SelectFolderViewHolder> {

    private IOnMusicScanSelectFolderListener listener;

    public MusicScanSelectFolderAdapter(int layoutResId, @Nullable List<FolderInfoWithMusicCount> data, IOnMusicScanSelectFolderListener listener) {
        super(layoutResId, data);
        this.listener = listener;
    }

    @Override
    protected void convert(SelectFolderViewHolder helper, final FolderInfoWithMusicCount item) {
        helper.tvSelectFolderItemFolderName.setText(item.folderName);
        helper.tvSelectFolderItemMusicCount.setText(String.valueOf(item.musicCount));

        helper.llMusicScanSelectFolderContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onSelectFolder(item);
                }
            }
        });
    }

    class SelectFolderViewHolder extends BaseViewHolder {

        private TextView tvSelectFolderItemFolderName;
        private TextView tvSelectFolderItemMusicCount;
        private LinearLayout llMusicScanSelectFolderContainer;

        public SelectFolderViewHolder(View view) {
            super(view);
            tvSelectFolderItemFolderName = view.findViewById(R.id.tvSelectFolderItemFolderName);
            tvSelectFolderItemMusicCount = view.findViewById(R.id.tvSelectFolderItemMusicCount);

            llMusicScanSelectFolderContainer = view.findViewById(R.id.llMusicScanSelectFolderContainer);
        }
    }

}
