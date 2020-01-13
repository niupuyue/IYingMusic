package com.paulniu.iyingmusic.utils;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-12
 * Time: 22:07
 * Desc: 基础工具类
 * Version:
 */
public class BaseUtils {


    public static void CloseKeyBord(Activity activity) {
        try {
            View view = activity.getWindow().peekDecorView();
            if (view != null) {
                InputMethodManager inputmanger = (InputMethodManager)activity.getSystemService("input_method");
                inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public static void CloseKeyBord(Activity activity, EditText editText) {
        try {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService("input_method");
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public static void OpenKeyBord(Activity activity, EditText editText) {
        try {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService("input_method");
            if (imm.isActive()) {
                imm.showSoftInput(editText, 2);
                imm.toggleSoftInput(2, 1);
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

}
