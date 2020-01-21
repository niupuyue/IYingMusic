package com.paulniu.iyingmusic.widget.pop;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.paulniu.iyingmusic.R;
import com.paulniu.iyingmusic.adapter.CurrSongListAdapter;
import com.paulniu.iyingmusic.db.entity.SongInfo;
import com.paulniu.iyingmusic.interfaces.IOnCurrSongListListener;
import com.paulniu.iyingmusic.interfaces.ITwoButtonListener;
import com.paulniu.iyingmusic.utils.SPUtils;
import com.paulniu.iyingmusic.utils.ScreenUtils;
import com.paulniu.iyingmusic.widget.AdPub;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-18
 * Time: 03:34
 * Desc: 当前歌曲列表弹窗
 * 歌曲播放列表和当前显示的列表需要分开，防止数据错乱
 * Version:
 */
public class CurrSongListPop extends PopupWindow implements View.OnClickListener, IOnCurrSongListListener {

    public interface OnCurrSongListListener {
        /**
         * 清空播放列表
         */
        void clearAll();

        /**
         * 改变播放模式：列表循环，随机播放，单曲循环
         *
         * @param mode
         */
        void onChangePlayMode(int mode);
    }

    private OnCurrSongListListener listListener;
    private Activity mActivity;
    private View mRoot;

    private LinearLayout llCurSongListContainer;
    private RecyclerView rvCurrSongList;
    private TextView tvCurrSongListPlayMode;
    private ImageView ivCurrSongListClearAll;
    private View viewOther;

    private CurrSongListAdapter adapter;
    private List<SongInfo> currSongList = new ArrayList<>();

    public CurrSongListPop(Activity activity, OnCurrSongListListener listListener) {
        if (null == activity || null == listListener) {
            return;
        }
        this.mActivity = activity;
        this.listListener = listListener;
        initContentView();
        initSetup();
    }

    private void initContentView() {
        if (null == mActivity) {
            return;
        }
        mRoot = LayoutInflater.from(mActivity).inflate(R.layout.view_pop_current_song_list, null);

        llCurSongListContainer = mRoot.findViewById(R.id.llCurSongListContainer);
        rvCurrSongList = mRoot.findViewById(R.id.rvCurrSongList);
        tvCurrSongListPlayMode = mRoot.findViewById(R.id.tvCurrSongListPlayMode);
        ivCurrSongListClearAll = mRoot.findViewById(R.id.ivCurrSongListClearAll);
        viewOther = mRoot.findViewById(R.id.viewOther);

        if (null != tvCurrSongListPlayMode) {
            tvCurrSongListPlayMode.setOnClickListener(this);
        }
        if (null != ivCurrSongListClearAll) {
            ivCurrSongListClearAll.setOnClickListener(this);
        }
        if (null != viewOther) {
            viewOther.setOnClickListener(this);
        }

//        // 设置页面布局最大高度
//        if (null != llCurSongListContainer) {
//            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) llCurSongListContainer.getLayoutParams();
//            layoutParams.height = 500;
//            layoutParams.width = ScreenUtils.getScreenWidth();
//            llCurSongListContainer.setLayoutParams(layoutParams);
//        }

        switchPlayMode(false);

        // 设置适配器，获取当前播放列表
        adapter = new CurrSongListAdapter(R.layout.item_current_song_list, currSongList, this);
        if (null != rvCurrSongList) {
            LinearLayoutManager manager = new LinearLayoutManager(mActivity);
            rvCurrSongList.setLayoutManager(manager);
            rvCurrSongList.setAdapter(adapter);
        }
    }

    /**
     * 设置正在播放语音列表
     */
    public void setSongList(List<SongInfo> infos) {
        this.currSongList.clear();
        this.currSongList.addAll(infos);
        adapter.setNewData(this.currSongList);
        adapter.notifyDataSetChanged();
    }

    private void initSetup() {
        setFocusable(true);
        setOutsideTouchable(true);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setAnimationStyle(R.style.DownAndUpAnimation);
        setContentView(mRoot);
        fitPopupWindowOverStatusBar(true);
        update();
    }

    /**
     * 设置popupWindow全屏展示(但是不包括虚拟键盘)
     */
    public void fitPopupWindowOverStatusBar(boolean needFullScreen) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                Field mLayoutInScreen = PopupWindow.class.getDeclaredField("mLayoutInScreen");
                mLayoutInScreen.setAccessible(true);
                mLayoutInScreen.set(this, needFullScreen);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 切换播放模式
     */
    private void switchPlayMode(boolean isClick) {
        // 设置当前播放模式
        int playMode = SPUtils.getPlayMode();
        if (isClick) {
            playMode++;
            SPUtils.setPlayMode(playMode);
        }
        if (null != tvCurrSongListPlayMode) {
            if (playMode % 3 == 0) {
                // 列表循环
                tvCurrSongListPlayMode.setText(mActivity.getString(R.string.CurSongListPop_playmode_list));
                Drawable drawable = mActivity.getResources().getDrawable(R.mipmap.ic_play_mode_list);
                if (null != drawable) {
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tvCurrSongListPlayMode.setCompoundDrawables(drawable, null, null, null);
                }
            } else if (playMode % 3 == 1) {
                // 随机播放
                tvCurrSongListPlayMode.setText(mActivity.getString(R.string.CurSongListPop_playmode_random));
                Drawable drawable = mActivity.getResources().getDrawable(R.mipmap.ic_play_mode_random);
                if (null != drawable) {
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tvCurrSongListPlayMode.setCompoundDrawables(drawable, null, null, null);
                }
            } else {
                // 单曲循环
                tvCurrSongListPlayMode.setText(mActivity.getString(R.string.CurSongListPop_playmode_single));
                Drawable drawable = mActivity.getResources().getDrawable(R.mipmap.ic_play_mode_single);
                if (null != drawable) {
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tvCurrSongListPlayMode.setCompoundDrawables(drawable, null, null, null);
                }
            }
        }
    }

    @Override
    public void onCurrSongListClick(SongInfo songInfo) {
        // 点击音乐，播放
        if (null != songInfo && null != currSongList && currSongList.size() > 0) {
            int currIndex = -1;
            for (int i = 0; i < currSongList.size(); i++) {
                if (currSongList.get(i).id == songInfo.id || TextUtils.equals(currSongList.get(i).data, songInfo.data)) {
                    currIndex = i;
                    break;
                }
            }
            currSongList.get(currIndex).isPlaying = true;
            // 调用是否播放歌曲/暂停歌曲
        }
        // 更新适配器
        if (null != adapter) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCurrSongListDelete(SongInfo songInfo) {
        // 将歌曲从播放列表中删除
        if (null != songInfo && null != currSongList && currSongList.size() > 0) {
            int currIndex = -1;
            for (int i = 0; i < currSongList.size(); i++) {
                if (currSongList.get(i).id == songInfo.id || TextUtils.equals(currSongList.get(i).data, songInfo.data)) {
                    currIndex = i;
                    break;
                }
            }
            currSongList.remove(currIndex);
            // 更新正在播放的歌曲列表
        }
        // 适配器
        if (null != adapter) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view) {
        if (null == view) {
            return;
        }
        switch (view.getId()) {
            case R.id.ivCurrSongListClearAll:
                // 清空当前播放列表，并且停止当前音乐播放
                AdPub.showViewTwoButton(mActivity, mActivity.getString(R.string.CurSongListPop_clear_all_title), mActivity.getString(R.string.CurSongListPop_clear_all_cancel), mActivity.getString(R.string.CurSongListPop_clear_all_confirm), new ITwoButtonListener() {
                    @Override
                    public void onLeftButtonOnclick() {
                        // 取消，不需要做任何事情
                    }

                    @Override
                    public void onRightButtonOnclick() {
                        // 清空播放列表 TODO

                    }
                });
                break;
            case R.id.tvCurrSongListPlayMode:
                // 切换播放方式
                switchPlayMode(true);
                break;
            case R.id.viewOther:
                dismiss();
                break;
        }
    }
}
