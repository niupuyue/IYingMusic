package com.paulniu.iyingmusic.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.paulniu.iyingmusic.App;

import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-05
 * Time: 23:29
 * Desc: 手机基础工具类
 * Version:
 */
public class PhoneBaseUtils {

    // 手机品牌
    public enum PhoneType {
        Huawei(1),
        Xiaomi(2),
        Oppo(3),
        Vivo(4),
        Meizu(5),
        Oneplus(6),
        Samsung(7),
        Other(0);

        public int value;

        PhoneType(int value) {
            this.value = value;
        }

    }

    /**
     * 获取当前手机的品牌
     *
     * @return
     */
    public static PhoneType getPhoneType() {
        if (!TextUtils.isEmpty(getEmuiVersion())) {
            Log.e("NPL", "当前手机是：华为");
            return PhoneType.Huawei;
        } else if (TextUtils.equals(Build.MANUFACTURER, "xiaomi")) {
            Log.e("NPL", "当前手机是：小米");
            return PhoneType.Xiaomi;
        } else if (isFlyme()) {
            Log.e("NPL", "当前手机是：魅族");
            return PhoneType.Meizu;
        } else if (isViVo()) {
            Log.e("NPL", "当前手机是：Vivo");
            return PhoneType.Vivo;
        } else if (isOppo()) {
            Log.e("NPL", "当前手机是：Oppo");
            return PhoneType.Oppo;
        } else if (isSamsung()) {
            Log.e("NPL", "当前手机是：三星");
            return PhoneType.Samsung;
        } else {
            Log.e("NPL", "当前手机是：其他");
            return PhoneType.Other;
        }
    }

    /**
     * 返回是否是EMUI版本
     *
     * @return
     */
    private static String getEmuiVersion() {
        String emuiVersion = "";
        Class<?>[] clsArray = new Class[]{String.class};
        Object[] objArray = new Object[]{"ro.build.version.emui"};
        try {
            Class<?> systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method get = systemPropertiesClass.getDeclaredMethod("get", clsArray);
            String version = (String) get.invoke(systemPropertiesClass, objArray);
            if (!TextUtils.isEmpty(version)) {
                return version;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return emuiVersion;
    }

    /**
     * 判断是否是魅族系统
     */
    private static boolean isFlyme() {
        try {
            final Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        } catch (final Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断是否是Vivo系统
     */
    public static boolean isViVo() {
        try {
            return android.os.Build.BRAND.toLowerCase().contains("vivo");
        } catch (final Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断是否是oppo手机
     *
     * @return
     */
    public static boolean isOppo() {
        try {
            return android.os.Build.BRAND.toLowerCase().contains("oppo");
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * 判断是否是三星手机
     *
     * @return
     */
    public static boolean isSamsung() {
        try {
            return android.os.Build.BRAND.toLowerCase().contains("samsung");
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * 判断手机是否开启位置信息
     *
     * @return
     */
    private static boolean isLocServiceEnable() {
        LocationManager locationManager = (LocationManager) App.getContext().getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }

    /**
     * 获取当前应用的SHA1
     *
     * @return
     */
    public static String getSHA1() {
        try {
            PackageInfo info = App.getContext().getPackageManager().getPackageInfo(
                    App.getContext().getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length() - 1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}
