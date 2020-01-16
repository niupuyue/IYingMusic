package com.paulniu.iyingmusic.widget;

import android.app.Dialog;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Coder: niupuyue
 * Date: 2020/1/16
 * Time: 15:22
 * Desc: 弹窗样式基类
 * Version:
 */
public class BaseDialog extends DialogFragment {

    @Override
    public void onStart() {
        super.onStart();

        try {
            Dialog dialog = getDialog();
            if (null == dialog) {
                return;
            }

            Window window = dialog.getWindow();
            if (null == window) {
                return;
            }

            window.setGravity(Gravity.CENTER);
            window.setBackgroundDrawableResource(android.R.color.transparent);
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            window.setLayout((int) (dm.widthPixels * 0.8), ViewGroup.LayoutParams.WRAP_CONTENT);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (manager == null) {
            return;
        }
        if (isAdded()) {
            return;
        }
        try {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(this, tag);
            transaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnPositiveClickListener {
        void onClick();
    }

}
