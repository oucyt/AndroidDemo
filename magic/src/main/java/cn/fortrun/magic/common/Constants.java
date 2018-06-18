package cn.fortrun.magic.common;

import android.content.Context;
import android.os.Build;

import com.blankj.utilcode.util.AppUtils;

import java.util.ArrayList;
import java.util.List;

import cn.fortrun.magic.model.bean.DeviceConfigBean;
import cn.fortrun.magic.utils.DeviceIdUtils;


/**
 * Created by wt on 2017/3/6.
 */
public class Constants {

    /**
     * Android设备信息
     */
    private static Android android;
//    /**
//     * 底座信息（Windows）
//     */
//    private static BaseStateBean baseState;

    /**
     * 平板、酒店的配置信息
     */
    private static DeviceConfigBean deviceConfig;

    public static void init(Context context) {
        android = new Android();
        android.setDarkMode(isBigSize());
        android.setDeviceId(DeviceIdUtils.getDeviceId(context));
        android.setAppVersion(AppUtils.getAppVersionName());
    }

    /**
     * 设备类型
     *
     * @return 系统版本号
     */
    private static boolean isBigSize() {
        String model = Build.MODEL;
        List<String> models = new ArrayList<>();
        models.add("Softwinner");
        models.add("eagle");
        models.add("nv60");
        return models.contains(model);
    }

    public static Android getAndroid() {
        return android;
    }

    public static void setAndroid(Android android) {
        Constants.android = android;
    }

//    public static BaseStateBean getBaseState() {
//        return baseState;
//    }
//
//    public static void setBaseState(BaseStateBean baseState) {
//        Constants.baseState = baseState;
//    }

    public static DeviceConfigBean getDeviceConfig() {
        return deviceConfig;
    }

    public static void setDeviceConfig(DeviceConfigBean deviceConfig) {
        Constants.deviceConfig = deviceConfig;
    }

    /**
     * preferences表名
     */
    public static final String PREFERENCES_NAME = "com.magic.pv.client_preferences";

    /**
     * 需要用到的Android设备的基本信息
     */
    public static class Android {
        /**
         * 黑色主题模式
         */
        boolean darkMode;
        /**
         * 设备唯一id
         */
        String deviceId;

        String appVersion;

        public boolean isDarkMode() {
            return darkMode;
        }

        void setDarkMode(boolean darkMode) {
            this.darkMode = darkMode;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getAppVersion() {
            return appVersion;
        }

        void setAppVersion(String appVersion) {
            this.appVersion = appVersion;
        }
    }

//
//    //是否开启mobile_checkin
//    public static final String ENABLED_MOBILE_CHECKIN = "enabled_mobile_checkin";
//
//    //是否支持吐房卡
//    public static final String SUPPORT_ROOM_CARD = "support_room_card";
//
//    //是否对接了门锁
//    public static final String INTEGRATION_ROOM_LOCK = "integration_room_lock";
//
//    //是否开启自动分房
//    public static final String ENABLED_AUTO_GIVE_ROOM = "enabled_auto_give_room";
//
//    //是否开启身份核验
//    public static final String ENABLED_IDENTITY_CHECK = "enabled_identity_check";
//
//    //是否开启rc单打印
//    public static final String RC_STATUS = "rc_status";
//
//    //签单状态
//    public static final String ELECTRON_SIGN = "electron_sign";
//
//    //酒店ID
//    public static final String HOTEL_ID = "hotel_id";
//
//
//    //是否允许PMS入住用户离店
//    public static final String ENABLED_PMS_IN_GUEST_CHECKOUT = "enabled_pms_in_guest_checkout";
//
//    //是否开启宾客账单签名
//    public static final String BILL_STATUS = "bill_status";
//
//
//    //apk是否下载完成
//    public static String DOWNLOAD_APK_NAME = "";



    /*云吧key*/
    public static String YUNBA_KEY = "581202f86cf991dc38fd2078";



    public static int QR_CODE_WIDTH = 255 ;

    public static String BASE_URL = "https://wqt.fortrun.cn";
    public static String wqt_env = "q";//环境
    public static String wqt_version = "master";//分支

    public static String MAGIC_URL = String.format("%s/%s/%s/", Constants.BASE_URL,
            Constants.wqt_env, Constants.wqt_version);


}
