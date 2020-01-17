package com.paulniu.iyingmusic.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.paulniu.iyingmusic.R;
import com.paulniu.iyingmusic.db.entity.SongInfo;
import com.paulniu.iyingmusic.interfaces.IOnCurrSongListListener;

import java.util.List;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-18
 * Time: 04:15
 * Desc: 播放列表适配器adapter
 * Version:
 */
public class CurrSongListAdapter extends BaseQuickAdapter<SongInfo, CurrSongListAdapter.ViewHolder> {

    private IOnCurrSongListListener listListener;

    public CurrSongListAdapter(int layoutResId, @Nullable List<SongInfo> data, IOnCurrSongListListener listListener) {
        super(layoutResId, data);
        this.listListener = listListener;
    }

    @Override
    protected void convert(ViewHolder helper, final SongInfo item) {
        Context context = helper.rlCurrSongListItemContainer.getContext();

        helper.tvCurrSongListItemSongName.setText(item.songName);
        helper.tvCurrSongListItemSongArtist.setText(item.artist);

        helper.ivCurrSongListItemDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != listListener) {
                    listListener.onCurrSongListDelete(item);
                }
            }
        });

        helper.rlCurrSongListItemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != listListener) {
                    listListener.onCurrSongListClick(item);
                }
            }
        });

        if (item.isPlaying) {
            helper.tvCurrSongListItemSongArtist.setTextColor(context.getResources().getColor(R.color.red_4d52));
            helper.tvCurrSongListItemSongArtist.setTextColor(context.getResources().getColor(R.color.red_4d52));
            helper.viewDivider.setBackground(context.getResources().getDrawable(R.color.red_4d52));
        } else {
            helper.tvCurrSongListItemSongName.setTextColor(context.getResources().getColor(R.color.gray_666));
            helper.tvCurrSongListItemSongArtist.setTextColor(context.getResources().getColor(R.color.gray_999));
            helper.viewDivider.setBackground(context.getResources().getDrawable(R.color.gray_999));
        }
    }

    class ViewHolder extends BaseViewHolder {

        private TextView tvCurrSongListItemSongName;
        private TextView tvCurrSongListItemSongArtist;
        private ImageView ivCurrSongListItemDelete;
        private View viewDivider;

        private RelativeLayout rlCurrSongListItemContainer;

        public ViewHolder(View view) {
            super(view);
            tvCurrSongListItemSongName = view.findViewById(R.id.tvCurrSongListItemSongName);
            tvCurrSongListItemSongArtist = view.findViewById(R.id.tvCurrSongListItemSongArtist);
            ivCurrSongListItemDelete = view.findViewById(R.id.ivCurrSongListItemDelete);
            viewDivider = view.findViewById(R.id.viewDivider);

            rlCurrSongListItemContainer = view.findViewById(R.id.rlCurrSongListItemContainer);
        }
    }

}
