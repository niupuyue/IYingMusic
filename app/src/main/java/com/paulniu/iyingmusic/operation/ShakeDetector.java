package com.paulniu.iyingmusic.operation;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.paulniu.iyingmusic.interfaces.IOnShakeListener;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-05
 * Time: 17:22
 * Desc: 监听手机甩动(甩动切歌)
 * Version:
 */
public class ShakeDetector implements SensorEventListener {

    /**
     * 传感器管理对象
     */
    private SensorManager mSensorManager;
    /**
     * 甩动监听
     */
    private IOnShakeListener listener;
    /**
     * 位置
     */
    private float lowX, lowY, lowZ;
    private final float FILTERING_VALUE = 0.1f;
    /**
     * 监听是否开启
     */
    private boolean isDetector;

    /**
     * 构造方法创建传感器管理对象
     *
     * @param context 上下文对象
     */
    public ShakeDetector(Context context) {
        if (null != context) {
            mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        }
    }

    /**
     * 设置甩动监听对象
     *
     * @param shakeListener 甩动监听对象
     */
    public void setListener(IOnShakeListener shakeListener) {
        if (null != shakeListener) {
            this.listener = shakeListener;
        }
    }

    /**
     * 启动甩动监听(注册监听)
     */
    public void start() {
        // 正处于监听状态，不要重复注册，直接返回
        if (isDetector) {
            return;
        }
        if (null == mSensorManager) {
            throw new UnsupportedOperationException();
        }
        // 开始注册甩动监听
        boolean isSuccess = mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        if (!isSuccess) {
            throw new UnsupportedOperationException();
        }
        isDetector = true;
    }

    /**
     * 停止甩动监听(注销监听)
     */
    public void stop() {
        // 已经停止甩动监听，不需要重复注销，直接返回
        if (!isDetector) {
            return;
        }
        if (null != mSensorManager) {
            mSensorManager.unregisterListener(this);
        }
        isDetector = false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            float x = event.values[SensorManager.DATA_X];
            float y = event.values[SensorManager.DATA_Y];
            float z = event.values[SensorManager.DATA_Z];

            lowX = x * FILTERING_VALUE + lowX * (1.0f - FILTERING_VALUE);
            lowY = y * FILTERING_VALUE + lowY * (1.0f - FILTERING_VALUE);
            lowZ = z * FILTERING_VALUE + lowZ * (1.0f - FILTERING_VALUE);

            float highX = x - lowX;
            float highY = y - lowY;
            float highZ = z - lowZ;

            if (highX >= 10 || highY >= 10 || highZ >= 10) {
                listener.onShake();
                stop();
            }

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
