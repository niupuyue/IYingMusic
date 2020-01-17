package com.paulniu.iyingmusic.service;

import android.content.Context;

import com.paulniu.iyingmusic.aidl.PlayControlImpl;

/**
 * Coder: niupuyue
 * Date: 2020/1/17
 * Time: 11:30
 * Desc:
 * Version:
 */
public class SongPlayServiceIBinder extends PlayControlImpl {
    private Context mContext;

    public SongPlayServiceIBinder(Context context){
        super(context);
        this.mContext = context;
    }

}
