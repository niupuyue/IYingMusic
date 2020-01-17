package com.paulniu.iyingmusic.utils;

import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.paulniu.iyingmusic.App;
import com.paulniu.iyingmusic.interfaces.ITextviewClickable;
import com.paulniu.iyingmusic.model.SpanModel;
import com.paulniu.iyingmusic.widget.SpanClickable;

import java.util.ArrayList;
import java.util.List;

/**
 * Coder: niupuyue
 * Date: 2020/1/13
 * Time: 11:27
 * Desc: 字符创操作工具类
 * Version:
 */
public class StringUtils {

    public static void setSpanText(TextView tv, CharSequence text, List<SpanModel> hotWordModel, ITextviewClickable listener) {
        if (tv != null && !TextUtils.isEmpty(text) && null != hotWordModel && hotWordModel.size() > 0) {
            try {
                SpannableStringBuilder textBuilder = new SpannableStringBuilder(text);

                for (int i = 0; i < hotWordModel.size(); ++i) {
                    List<Integer> lis = getStrIndex(text + "", ((SpanModel) hotWordModel.get(i)).text);

                    for (int j = 0; j < lis.size(); ++j) {
                        textBuilder.setSpan(new SpanClickable(listener, i, ((SpanModel) hotWordModel.get(i)).isUnderline, ((SpanModel) hotWordModel.get(i)).color, ((SpanModel) hotWordModel.get(i)).isBold, ((SpanModel) hotWordModel.get(i)).textSize), (Integer) lis.get(j), (Integer) lis.get(j) + ((SpanModel) hotWordModel.get(i)).text.length(), 33);
                    }
                }

                tv.setMovementMethod(LinkMovementMethod.getInstance());
                tv.setHighlightColor(0);
                tv.setText(textBuilder);
            } catch (Exception var8) {
                var8.printStackTrace();
            }

        }
    }

    public static void setHotWordsText(TextView tv, CharSequence text, String[] arrHotWords, int colorResourceID, boolean isBold, ITextviewClickable listener) {
        setHotWordsText(tv, text, arrHotWords, colorResourceID, isBold, -1.0F, listener);
    }

    public static void setHotWordsText(TextView tv, CharSequence text, String[] arrHotWords, int colorResourceID, ITextviewClickable listener) {
        setHotWordsText(tv, text, arrHotWords, colorResourceID, false, listener);
    }

    public static void setHotWordsText(TextView tv, CharSequence text, String hotWords, int colorResourceID, ITextviewClickable listener) {
        setHotWordsText(tv, text, new String[]{hotWords}, colorResourceID, listener);
    }

    public static void setHotWordsText(TextView tv, CharSequence text, String hotWords, int colorResourceID) {
        setHotWordsText(tv, text, (String)hotWords, colorResourceID, (ITextviewClickable)null);
    }

    public static void setHotWordsText(TextView tv, CharSequence text, String[] arrHotWords, int colorResourceID) {
        setHotWordsText(tv, text, (String[])arrHotWords, colorResourceID, (ITextviewClickable)null);
    }

    public static void setHotWordsText(TextView tv, CharSequence text, String[] arrHotWords, int colorResourceID, boolean isBold, float textSize, ITextviewClickable listener) {
        if (tv != null && !TextUtils.isEmpty(text) && null != arrHotWords && arrHotWords.length > 0) {
            try {
                List<SpanModel> hotWordModels = new ArrayList();
                String[] var9 = arrHotWords;
                int var10 = arrHotWords.length;

                for (int var11 = 0; var11 < var10; ++var11) {
                    String arrHotWord = var9[var11];
                    SpanModel spanModel = new SpanModel();
                    spanModel.text = arrHotWord;
                    spanModel.isUnderline = false;
                    spanModel.color = ContextCompat.getColor(App.getContext(), colorResourceID);
                    spanModel.isBold = isBold;
                    spanModel.textSize = textSize;
                    hotWordModels.add(spanModel);
                }

                setSpanText(tv, text, hotWordModels, listener);
            } catch (Exception var13) {
                var13.printStackTrace();
            }

        }
    }

    /**
     * 获取hotword在字符串中的位置
     *
     * @param str     原始字符串
     * @param hotword 热词
     * @return 位置集合
     */
    public static List<Integer> getStrIndex(String str, String hotword) {
        List<Integer> lis = new ArrayList();
        String tempLowerCaseStr = str.toLowerCase();
        String tempLowerHotWord = hotword.toLowerCase();
        int indexOf = tempLowerCaseStr.indexOf(tempLowerHotWord);
        if (indexOf != -1) {
            lis.add(indexOf);
        }

        while (indexOf != -1) {
            indexOf = tempLowerCaseStr.indexOf(tempLowerHotWord, indexOf + 1);
            if (indexOf != -1) {
                lis.add(indexOf);
            }
        }

        return lis;
    }

    public static boolean isReal(String string) {
        return string != null && string.length() > 0 && !"null".equals(string);
    }

    public static String getGenTimeMS(int misec) {
        int min = misec / 1000 / 60;
        int sec = (misec / 1000) % 60;
        String minStr = min < 10 ? "0" + min : min + "";
        String secStr = sec < 10 ? "0" + sec : sec + "";
        return minStr + ":" + secStr;
    }

}
