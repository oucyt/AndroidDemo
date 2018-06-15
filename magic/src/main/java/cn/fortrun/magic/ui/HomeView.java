package cn.fortrun.magic.ui;

import android.graphics.Bitmap;

import cn.fortrun.magic.common.Constants;
import cn.fortrun.magic.common.mvp.IView;
import cn.fortrun.magic.model.bean.DeviceConfigBean;

/**
 * description
 *
 * @author tianyu
 * @create 2018.06.14 下午4:00
 * @since 1.0.0
 */
public interface HomeView extends IView {

    /**
     * 未注册设备
     */
    void showUnregistered();

    /**
     * 将二维码展示在界面上
     *
     * @param bitmap
     */
    void showQrCode(Bitmap bitmap);

    void showAppInfo(DeviceConfigBean config, Constants.Android android);

    void showNavigationBar(DeviceConfigBean config);
}
