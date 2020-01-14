package com.paulniu.iyingmusic.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.paulniu.iyingmusic.R;
import com.paulniu.iyingmusic.activity.FolderWithMusicListActivity;
import com.paulniu.iyingmusic.db.entity.FolderInfo;
import com.paulniu.iyingmusic.db.entity.FolderInfoWithMusicCount;

import java.util.List;

/**
 * Coder: niupuyue
 * Date: 2020/1/14
 * Time: 18:45
 * Desc: 首页文件夹列表
 * Version:
 */
public class MainFolderAdapter extends BaseQuickAdapter<FolderInfoWithMusicCount, MainFolderAdapter.MainFolderViewHolder> {

    public MainFolderAdapter(int layoutResId, @Nullable List<FolderInfoWithMusicCount> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final MainFolderViewHolder helper, final FolderInfoWithMusicCount item) {
        final Context context = helper.rlMainFolderContainer.getContext();

        helper.tvMainFolderItemName.setText(item.folderName);
        helper.tvMainFolderItemCount.setText(item.musicCount + "首");
        helper.rlMainFolderContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到对应的文件夹中
                Intent intent = FolderWithMusicListActivity.getIntent(context, item);
                context.startActivity(intent);
            }
        });

        // 动态设置宽高
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) helper.rlMainFolderContainer.getLayoutParams();
        
    }

    class MainFolderViewHolder extends BaseViewHolder {

        private TextView tvMainFolderItemName;
        private TextView tvMainFolderItemCount;
        private RelativeLayout rlMainFolderContainer;

        public MainFolderViewHolder(View view) {
            super(view);
            tvMainFolderItemName = view.findViewById(R.id.tvMainFolderItemName);
            tvMainFolderItemCount = view.findViewById(R.id.tvMainFolderItemCount);
            rlMainFolderContainer = view.findViewById(R.id.rlMainFolderContainer);
        }
    }

}
