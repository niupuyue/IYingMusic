package com.paulniu.iyingmusic.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.paulniu.iyingmusic.R;
import com.paulniu.iyingmusic.interfaces.IOneButtonListener;
import com.paulniu.iyingmusic.interfaces.ITwoButtonAddCloseListener;
import com.paulniu.iyingmusic.interfaces.ITwoButtonListener;
import com.paulniu.iyingmusic.utils.BaseUtils;
import com.paulniu.iyingmusic.utils.ScreenUtils;

/**
 * Coder: niupuyue
 * Date: 2020/1/16
 * Time: 16:30
 * Desc: 简单弹窗
 * Version:
 */
public class AdPub {

    /**
     * 1个按钮,只有内容
     */
    public static void showViewOneButton(Context context, String content, String buttonText, final IOneButtonListener listener) {
        show(context, false, "", content, "", buttonText, new ITwoButtonListener() {
            @Override
            public void onLeftButtonOnclick() {
            }

            @Override
            public void onRightButtonOnclick() {
                if (listener != null) {
                    listener.onButtonOnclick();
                }
            }
        });
    }

    /**
     * 1个按钮,只有内容
     */
    public static void showViewOneButton(Context context, String content, String buttonText, int gravity, final IOneButtonListener listener) {
        show(context, false, "", content, "", buttonText, gravity, false, new ITwoButtonAddCloseListener() {
            @Override
            public void onLeftButtonOnclick() {

            }

            @Override
            public void onRightButtonOnclick() {
                if (listener != null) {
                    listener.onButtonOnclick();
                }
            }

            @Override
            public void onClose() {

            }
        });
    }

    /**
     * 1个按钮,有标题和内容
     */
    public static void showViewOneButton(Context context, String title, CharSequence content, String buttonText, final IOneButtonListener listener) {
        show(context, false, title, content, "", buttonText, new ITwoButtonListener() {
            @Override
            public void onLeftButtonOnclick() {
            }

            @Override
            public void onRightButtonOnclick() {

                if (listener != null) {
                    listener.onButtonOnclick();
                }
            }
        });
    }

    /**
     * 1个按钮,有标题和内容
     */
    public static void showViewOneButton(Context context, String title, CharSequence content, String buttonText, int gravity, final IOneButtonListener listener) {
        show(context, false, title, content, "", buttonText, gravity, false, new ITwoButtonAddCloseListener() {
            @Override
            public void onLeftButtonOnclick() {

            }

            @Override
            public void onRightButtonOnclick() {
                if (listener != null) {
                    listener.onButtonOnclick();
                }
            }

            @Override
            public void onClose() {

            }
        });
    }

    /**
     * 1个按钮,只有标题
     */
    public static void showViewOneButtonAddClose(Context context, String title, String buttonText, final ITwoButtonAddCloseListener listener) {
        show(context, false, "", title, "", buttonText, Gravity.CENTER, true, new ITwoButtonAddCloseListener() {
            @Override
            public void onLeftButtonOnclick() {
                if (listener != null) {
                    listener.onLeftButtonOnclick();
                }
            }

            @Override
            public void onRightButtonOnclick() {
                if (listener != null) {
                    listener.onRightButtonOnclick();
                }
            }

            @Override
            public void onClose() {
                if (listener != null) {
                    listener.onClose();
                }
            }
        });
    }

    /**
     * 2个按钮,只有内容
     */
    public static void showViewTwoButton(Context context, CharSequence content, String leftButtonText, String rightButtonText, final ITwoButtonListener listener) {
        show(context, false, "", content, leftButtonText, rightButtonText, listener);
    }

    /**
     * 2个按钮,只有内容
     */
    public static void showViewTwoButton(Context context, boolean isCancel, CharSequence content, String leftButtonText, String rightButtonText, final ITwoButtonListener listener) {
        show(context, isCancel, "", content, leftButtonText, rightButtonText, listener);
    }

    /**
     * 2个按钮,有标题和内容
     */
    public static void showViewTwoButton(Context context, String title, CharSequence content, String leftButtonText, String rightButtonText, final ITwoButtonListener listener) {
        show(context, false, title, content, leftButtonText, rightButtonText, listener);
    }

    /**
     * @param context
     * @param cancel          是否允许点击外面关闭
     * @param title           标题
     * @param content         内容
     * @param leftButtonText  左侧按钮
     * @param rightButtonText 右侧按钮
     * @param listener
     */
    private static void show(Context context, boolean cancel, String title, CharSequence content, String leftButtonText, String rightButtonText, final ITwoButtonListener listener) {
        show(context, cancel, title, content, leftButtonText, rightButtonText, Gravity.CENTER, false, new ITwoButtonAddCloseListener() {
            @Override
            public void onLeftButtonOnclick() {
                if (listener != null) {
                    listener.onLeftButtonOnclick();
                }
            }

            @Override
            public void onRightButtonOnclick() {
                if (listener != null) {
                    listener.onRightButtonOnclick();
                }
            }

            @Override
            public void onClose() {

            }
        });
    }

    /**
     * 有主标题和副标题的2个按钮弹窗,右上角有关闭按钮
     *
     * @param title           副标题
     * @param leftButtonText  左侧按钮text
     * @param rightButtonText 右侧按钮tet
     */
    public static void showViewTwoButtonAddClose(Context context, String title, String leftButtonText, String rightButtonText, final ITwoButtonAddCloseListener listener) {

        show(context, false, "", title, leftButtonText, rightButtonText, Gravity.CENTER, true, new ITwoButtonAddCloseListener() {
            @Override
            public void onLeftButtonOnclick() {
                if (listener != null) {
                    listener.onLeftButtonOnclick();
                }
            }

            @Override
            public void onRightButtonOnclick() {
                if (listener != null) {
                    listener.onRightButtonOnclick();
                }
            }

            @Override
            public void onClose() {
                if (listener != null) {
                    listener.onClose();
                }
            }
        });
    }

    /**
     * @param context
     * @param cancel          是否允许点击外面关闭
     * @param title           标题
     * @param content         内容
     * @param leftButtonText  左侧按钮
     * @param rightButtonText 右侧按钮
     * @param gravity         内容gravity
     * @param listener
     */
    private static void show(Context context, boolean cancel, String title, CharSequence content, String leftButtonText, String rightButtonText, int gravity, boolean showClose, final ITwoButtonAddCloseListener listener) {
        try {
            if (context instanceof Activity && ((Activity) context).isFinishing()) {
                return;
            }

            final Dialog dialog = new Dialog(context, android.R.style.Theme_Holo_Dialog_NoActionBar);
            dialog.setCanceledOnTouchOutside(cancel);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }

            View view = View.inflate(context, R.layout.view_alertdialog_public_titleandcontent, null);

            TextView tvTitle = view.findViewById(R.id.tvTitle);
            TextView tvContent = view.findViewById(R.id.tvContent);
            TextView tvOnlyContent = view.findViewById(R.id.tvOnlyContent);

            // 只有标题
            if (!TextUtils.isEmpty(title) && TextUtils.isEmpty(content)) {
                tvTitle.setVisibility(View.GONE);
                tvContent.setVisibility(View.GONE);
                tvOnlyContent.setGravity(gravity);
                tvOnlyContent.setVisibility(TextUtils.isEmpty(title)?View.GONE:View.VISIBLE);
                tvOnlyContent.setText(title);
            }
            // 只有内容
            else if (TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)) {
                tvTitle.setVisibility(View.GONE);
                tvContent.setVisibility(View.GONE);
                tvOnlyContent.setGravity(gravity);
                tvOnlyContent.setVisibility(TextUtils.isEmpty(content)?View.GONE:View.VISIBLE);
                tvOnlyContent.setText(content);
            }
            // 有标题和内容
            else {
                tvTitle.getPaint().setFakeBoldText(true);
                tvTitle.setText(title);
                tvContent.setText(content);
                tvContent.setGravity(gravity);
                tvTitle.setVisibility(View.VISIBLE);
                tvContent.setVisibility(View.VISIBLE);
                tvOnlyContent.setVisibility(View.GONE);
            }

            TextView tvCancel = view.findViewById(R.id.tvCancel);
            tvCancel.setVisibility(TextUtils.isEmpty(leftButtonText)?View.GONE:View.VISIBLE);
            tvCancel.setText(leftButtonText);
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();

                    if (listener != null) {
                        listener.onLeftButtonOnclick();
                    }
                }
            });

            TextView tvOk = view.findViewById(R.id.tvOk);
            tvOk.setVisibility(TextUtils.isEmpty(rightButtonText)?View.GONE:View.VISIBLE);
            tvOk.setText(rightButtonText);
            tvOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();

                    if (listener != null) {
                        listener.onRightButtonOnclick();
                    }
                }
            });

            ImageView ivClose = view.findViewById(R.id.ivClose);
            ivClose.setVisibility(showClose?View.VISIBLE: View.GONE);
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();

                    if (listener != null) {
                        listener.onClose();
                    }
                }
            });

            dialog.setContentView(view);
            dialog.show();

            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();

            params.width = ScreenUtils.dp2px(showClose ? 290 : 275);
            dialog.getWindow().setAttributes(params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
