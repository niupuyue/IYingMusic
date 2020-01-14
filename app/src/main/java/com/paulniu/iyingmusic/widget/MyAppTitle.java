package com.paulniu.iyingmusic.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paulniu.iyingmusic.App;
import com.paulniu.iyingmusic.R;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-11
 * Time: 23:53
 * Desc: 自定义状态栏
 * Version:
 */
public class MyAppTitle extends LinearLayout {

    private OnLeftButtonClickListener mLeftButtonClickListener;
    private OnRightButtonClickListener mRightButtonClickListener;
    private MyViewHolder mViewHolder;
    private int mRightRes;
    private int mLeftRes;

    public MyAppTitle(Context context) {
        super(context);
        init();
    }

    public MyAppTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public MyAppTitle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        final LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        View viewAppTitle = inflater.inflate(R.layout.view_activity_titlebar, null);
        this.addView(viewAppTitle, layoutParams);

        mViewHolder = new MyViewHolder(this);

        mViewHolder.tvLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLeftButtonClickListener != null) {
                    mLeftButtonClickListener.onLeftButtonClick(v);
                }
            }
        });

        mViewHolder.tvRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRightButtonClickListener != null) {
                    mRightButtonClickListener.OnRightButtonClick(v);
                }
            }
        });
    }

    public void initViewsVisible(boolean isLeftButtonVisible, boolean isCenterTitleVisible, boolean isRightIconVisible, boolean isRightTitleVisible) {
        // 左侧返回
        mViewHolder.tvLeft.setVisibility(isLeftButtonVisible ? View.VISIBLE : View.GONE);
        // 中间标题
        mViewHolder.tvCenterTitle.setVisibility(isCenterTitleVisible ? View.VISIBLE : View.GONE);

        //右侧图标和文案
        if (mViewHolder.tvRight != null) {
            //图标和文案都不可见
            if (!isRightIconVisible && !isRightTitleVisible) {
                mViewHolder.tvRight.setVisibility(View.INVISIBLE);
            }
            //图标可见文案不可见
            else if (isRightIconVisible && !isRightTitleVisible) {
                if (0 != mRightRes) {
                    setRightIcon(mRightRes);
                }
                mViewHolder.tvRight.setBackgroundColor(Color.TRANSPARENT);
                mViewHolder.tvRight.setVisibility(View.VISIBLE);
            }
            //图标不可见文案可见
            else if (!isRightIconVisible) {
                mViewHolder.tvRight.setCompoundDrawables(null, null, null, null);
                mViewHolder.tvRight.setVisibility(View.VISIBLE);
            }
            //图标和文案都可见
            else {
                if (0 != mRightRes) {
                    setRightIcon(mRightRes);
                }
                mViewHolder.tvRight.setBackgroundColor(Color.TRANSPARENT);
                mViewHolder.tvRight.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 是否展示底部的线
     */
    public void resetLineVisibility(boolean value) {
        mViewHolder.viewLineAt.setVisibility(value?View.VISIBLE: View.GONE);
    }

    /**
     * 设置MyAppTitle的背景
     */
    public void setMyAppTitleBg(Drawable drawable){
        if (null != mViewHolder.llMyAppTitleContainer && null != drawable){
            mViewHolder.llMyAppTitleContainer.setBackground(drawable);
        }
    }

    public void setMyAppTitleBg(int color){
        if (color > 0){
            setMyAppTitleBg(getResources().getColor(color));
        }
    }

    //////////////////////////////////////////
    //////////////////////////////////////////
    ////////////////设置主标题开始
    //////////////////////////////////////////
    //////////////////////////////////////////

    public TextView getCenterTextView() {
        if (null != mViewHolder) {
            return mViewHolder.tvCenterTitle;
        }
        return null;
    }


    /**
     * 设置标题
     */
    public void setAppTitle(String title) {
        mViewHolder.tvCenterTitle.setText(title);
    }

    /**
     * 设置标题
     */
    public void setAppTitle(int resID) {
        mViewHolder.tvCenterTitle.setText(resID);
    }

    //////////////////////////////////////////
    //////////////////////////////////////////
    ////////////////设置主标题结束
    //////////////////////////////////////////
    //////////////////////////////////////////

    //////////////////////////////////////////
    //////////////////////////////////////////
    ////////////////设置左侧按钮开始
    //////////////////////////////////////////
    //////////////////////////////////////////

    /**
     * 设置左侧按钮内容
     */
    public void setLeftTitle(String text) {
        mViewHolder.tvLeft.setCompoundDrawables(null, null, null, null);
        mViewHolder.tvLeft.setText(text);
    }

    /**
     * 获取左侧按钮的内容
     */
    public String getLeftTitle(){
        return mViewHolder.tvLeft.getText().toString();
    }

    /**
     * 左侧按钮是否显示
     */
    public boolean isLeftVisible(){
        try {
            return VISIBLE == mViewHolder.tvLeft.getVisibility();
        }catch (Exception ex){
            return false;
        }
    }

    /**
     * 设置左侧按钮是否可用
     */
    public void setLeftTitleEnable(boolean enable){
        if (null != mViewHolder.tvLeft){
            mViewHolder.tvLeft.setEnabled(enable);
        }
    }

    /**
     * 设置左侧图标
     */
    public void setLeftIcon(int leftIcon){
        mLeftRes = leftIcon;
        setLeftDrawable();
    }

    /**
     * 设置左侧图标实际方法
     */
    private void setLeftDrawable(){
        Drawable drawable = getResources().getDrawable(mLeftRes);
        if (null != drawable){
            drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
            mViewHolder.tvLeft.setCompoundDrawables(drawable,null,null,null);
        }
    }

    /**
     * 重置左侧按钮(显示返回图标)
     */
    public void resetLeft() {
        mViewHolder.tvLeft.setText("");
        Drawable drawable = getResources().getDrawable(R.drawable.bg_title_back_selector);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mViewHolder.tvLeft.setCompoundDrawables(drawable, null, null, null);
    }


    //////////////////////////////////////////
    //////////////////////////////////////////
    ////////////////设置左侧按钮结束
    //////////////////////////////////////////
    //////////////////////////////////////////

    //////////////////////////////////////////
    //////////////////////////////////////////
    ////////////////设置右侧按钮开始
    //////////////////////////////////////////
    //////////////////////////////////////////

    /**
     * 右侧按钮是否显示
     */
    public boolean isRightVisible() {
        try {
            return VISIBLE == mViewHolder.tvRight.getVisibility();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 设置右侧按钮的内容
     */
    public void setRightTitle(String text) {
            mViewHolder.tvRight.setText(text);
    }

    /**
     * 获取右侧按钮的文字
     */
    public String getRightTitle(){
        return mViewHolder.tvRight.getText().toString();
    }

    /**
     * 设置右侧按钮是否可用
     */
    public void setRightTitleEnable(boolean enable) {
        if (null != mViewHolder.tvRight) {
            mViewHolder.tvRight.setEnabled(enable);
        }
    }

    /**
     * 设置右侧按钮的字体颜色
     */
    public void setRightTitleTextColor(int colorResourceID) {

        if (colorResourceID == 0) {
            return;
        }
        mViewHolder.tvRight.setTextColor(colorResourceID);
    }

    /**
     * 设置右侧图标
     */
    public void setRightIcon(int sourceId) {
        mRightRes = sourceId;
        setRightDrawable();
    }

    /**
     * 设置右侧图标实际方法
     */
    private void setRightDrawable() {
        Drawable drawable = getResources().getDrawable(mRightRes);
        if (null != drawable) {
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            mViewHolder.tvRight.setCompoundDrawables(drawable, null, null, null);
        }
    }

    //////////////////////////////////////////
    //////////////////////////////////////////
    ////////////////设置右侧按钮结束
    //////////////////////////////////////////
    //////////////////////////////////////////

    public void setOnLeftButtonClickListener(OnLeftButtonClickListener listen) {
        mLeftButtonClickListener = listen;
    }

    public void setOnRightButtonClickListener(OnRightButtonClickListener listen) {
        mRightButtonClickListener = listen;
    }

    public interface OnLeftButtonClickListener {
        void onLeftButtonClick(View v);
    }

    public interface OnRightButtonClickListener {
        void OnRightButtonClick(View v);
    }

    private class MyViewHolder {
        TextView tvLeft;
        TextView tvCenterTitle;
        TextView tvRight;
        View viewLineAt;
        LinearLayout llMyAppTitleContainer;

        public MyViewHolder(View v) {
            tvLeft = v.findViewById(R.id.tvLeft);
            tvCenterTitle = v.findViewById(R.id.tvCenterTitle);
            tvRight = v.findViewById(R.id.tvRight);
            viewLineAt = v.findViewById(R.id.viewLineAt);

            llMyAppTitleContainer = v.findViewById(R.id.llMyAppTitleContainer);
        }
    }

}
