package com.paulniu.iyingmusic.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.paulniu.iyingmusic.R;
import com.paulniu.iyingmusic.activity.FolderWithMusicListActivity;
import com.paulniu.iyingmusic.base.BaseActivity;
import com.paulniu.iyingmusic.db.entity.FolderInfoWithMusicCount;
import com.paulniu.iyingmusic.db.source.FolderInfoSource;
import com.paulniu.iyingmusic.interfaces.ITwoButtonListener;
import com.paulniu.iyingmusic.widget.AdPub;
import com.paulniu.iyingmusic.widget.dialog.FolderAddDialog;
import com.paulniu.iyingmusic.widget.dialog.MainFolderLongClickDialog;

import java.util.List;

/**
 * Coder: niupuyue
 * Date: 2020/1/14
 * Time: 18:45
 * Desc: 首页文件夹列表
 * Version:
 */
public class MainFolderAdapter extends BaseQuickAdapter<FolderInfoWithMusicCount, MainFolderAdapter.MainFolderViewHolder> {

    private BaseActivity mActivity;

    public MainFolderAdapter(BaseActivity activity, int layoutResId, @Nullable List<FolderInfoWithMusicCount> data) {
        super(layoutResId, data);
        this.mActivity = activity;
    }

    @Override
    protected void convert(final MainFolderViewHolder helper, final FolderInfoWithMusicCount item) {
        final Context context = helper.llMainFolderContainer.getContext();

        helper.tvMainFolderItemName.setText(item.folderName);
        helper.tvMainFolderItemCount.setText(item.musicCount + "首");
        helper.llMainFolderContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到对应的文件夹中
                Intent intent = FolderWithMusicListActivity.getIntent(context, item);
                context.startActivity(intent);
            }
        });

        // 设置长摁事件
        helper.llMainFolderContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // 显示弹窗，删除/重命名
                MainFolderLongClickDialog longClickDialog = new MainFolderLongClickDialog();
                longClickDialog.setListener(new MainFolderLongClickDialog.IMainFolderLongClickListener() {
                    @Override
                    public void onSelect(String type) {
                        if (TextUtils.equals(type, MainFolderLongClickDialog.SELECT_TYPE_DELETE)) {
                            // 删除文件夹
                            AdPub.showViewTwoButton(context, context.getString(R.string.MainActivity_delete_title),
                                    context.getString(R.string.MainActivity_delete_cancel), context.getString(R.string.MainActivity_delete_delete),
                                    new ITwoButtonListener() {
                                        @Override
                                        public void onLeftButtonOnclick() {

                                        }

                                        @Override
                                        public void onRightButtonOnclick() {
                                            FolderInfoSource.deleteFolderById(item.folderId);
                                            Toast.makeText(context, context.getString(R.string.MainActivity_delete_success), Toast.LENGTH_SHORT).show();
                                            notifyDataSetChanged();
                                        }
                                    });
                        } else {
                            // 重命名
                            FolderAddDialog dialog = new FolderAddDialog(FolderAddDialog.TYPE_RENAME);
                            dialog.setListener(new FolderAddDialog.IFolderAddDialogListener() {
                                @Override
                                public void onConfirm(String folderName) {
                                    if (TextUtils.isEmpty(folderName)) {
                                        return;
                                    }
                                    // 修改文件夹名称
                                    item.folderName = folderName;
                                    FolderInfoSource.updateOrInsertFolder(item);
                                    notifyDataSetChanged();
                                }

                                @Override
                                public void onCancel() {

                                }
                            });
                            dialog.show(mActivity.getSupportFragmentManager(), mActivity.getLocalClassName());
                        }
                    }
                });
                longClickDialog.show(mActivity.getSupportFragmentManager(), mActivity.getLocalClassName());
                return true;
            }
        });
    }

    class MainFolderViewHolder extends BaseViewHolder {

        private TextView tvMainFolderItemName;
        private TextView tvMainFolderItemCount;
        private LinearLayout llMainFolderContainer;

        public MainFolderViewHolder(View view) {
            super(view);
            tvMainFolderItemName = view.findViewById(R.id.tvMainFolderItemName);
            tvMainFolderItemCount = view.findViewById(R.id.tvMainFolderItemCount);
            llMainFolderContainer = view.findViewById(R.id.llMainFolderContainer);
        }
    }

}
