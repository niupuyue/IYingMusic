package com.paulniu.iyingmusic.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.paulniu.iyingmusic.service.SongPlayServiceManager;
import com.paulniu.iyingmusic.utils.ImmersionBarUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Method;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-05
 * Time: 22:59
 * Desc: Activity基类
 * Version:
 */
public abstract class BaseActivity extends FragmentActivity {

    public abstract int initViewLayout();

    public void initExtraData() {
    }

    public abstract void initViewById();

    public abstract void initData();

    public boolean needInitEvent(){
        return false;
    }

    public void initListener() {
    }

    public RxPermissions rxPermissions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initViewLayout());
        initExtraData();
        rxPermissions = new RxPermissions(this);
        if (needInitEvent()){
            initEventBus();
        }
        initViewById();
        initListener();
        initData();
    }

    /**
     * 检查类中有是否有onEvent方法
     */
    protected boolean hasMethodOnEvent() {
        boolean value = false;

        try {
            Method[] methods = this.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equalsIgnoreCase("onEvent")) {
                    value = true;
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return value;
    }

    public void initEventBus() {
        try {
            if (!EventBus.getDefault().isRegistered(this) && hasMethodOnEvent()) {
                EventBus.getDefault().register(this);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void unRegisterEventbus() {
        try {
            if (EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().unregister(this);
            }
        } catch (IllegalArgumentException ie) {

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void startSongService(){
        SongPlayServiceManager.startPlayService(this);
        // 执行耗时操作，更新播放列表 TODO

    }

    @Override
    public void onBackPressed() {
        unRegisterEventbus();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        try {
            EventBus.getDefault().unregister(this);
            ImmersionBarUtils.destory(this);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        super.onDestroy();
    }
}
