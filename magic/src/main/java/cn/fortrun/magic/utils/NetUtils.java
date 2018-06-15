package cn.fortrun.magic.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.orhanobut.logger.Logger;

import cn.fortrun.magic.common.Constants;

/**
 * 跟网络相关的工具类
 */
public class NetUtils {
    private NetUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 检测当的网络（WLAN、3G/2G）状态
     *
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否是wifi连接
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }
        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 打开网络设置界面
     */
    public static void openSetting(Activity activity) {
        if (android.os.Build.VERSION.SDK_INT > 10) {
            activity.startActivity(new Intent(
                    android.provider.Settings.ACTION_SETTINGS));
        } else {
            activity.startActivity(new Intent(
                    android.provider.Settings.ACTION_WIRELESS_SETTINGS));
        }
    }

    /**
     * 获取url
     *
     * @param projectName
     * @param path
     * @return
     */
    public static String getRealUrl(String projectName, String path) {
        return String.format("%s/%s/%s/%s/%s", Constants.BASE_URL,
                Constants.wqt_env, Constants.wqt_version, projectName, path);
    }

    /**
     * 获取二维码生存url
     *
     * @param param
     * @return
     */
    public static String getQRCodeUrl(String param) {
        String result = String.format("%s/%s?%s", Constants.BASE_URL, Constants.wqt_env, param);
        Logger.i("生成的二维码链接：%s", result);
        return result;
    }
}
