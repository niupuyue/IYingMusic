package com.paulniu.iyingmusic.widget;

import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import androidx.annotation.NonNull;

import com.paulniu.iyingmusic.interfaces.ITextviewClickable;

/**
 * Coder: niupuyue
 * Date: 2020/1/13
 * Time: 11:34
 * Desc: 可以点击的span
 * Version:
 */

public class SpanClickable extends ClickableSpan implements View.OnClickListener {
    private final ITextviewClickable clickListener;
    private int position;
    private boolean isUnderline;
    private int color;
    private boolean isBold = false;
    private float textSize;

    public SpanClickable(ITextviewClickable clickListener, int position, boolean isUnderline, int color, boolean isBold, float textSize) {
        this.clickListener = clickListener;
        this.position = position;
        this.isUnderline = isUnderline;
        this.color = color;
        this.isBold = isBold;
        this.textSize = textSize;
    }

    public void onClick(@NonNull View v) {
        if (this.clickListener != null) {
            this.clickListener.onSpanClick(this.position);
        }

    }

    public void updateDrawState(@NonNull TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(this.isUnderline);
        ds.setColor(this.color);
        if (this.isBold) {
            ds.setTypeface(Typeface.DEFAULT_BOLD);
        }

        if (this.textSize > 0.0F) {
            ds.setTextSize(this.textSize);
        }

    }
}