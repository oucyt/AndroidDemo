package cn.fortrun.magic.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import cn.fortrun.magic.common.Constants;
import cn.fortrun.magic.common.mvp.BasePresenterImpl;
import cn.fortrun.magic.interfaces.CallBack3;
import cn.fortrun.magic.interfaces.CallBack4;
import cn.fortrun.magic.model.IHomeModel;
import cn.fortrun.magic.model.bean.DeviceConfigBean;
import cn.fortrun.magic.model.impl.HomeModelImpl;
import cn.fortrun.magic.ui.HomeView;
import cn.fortrun.magic.utils.NetUtils;

/**
 *
 */
public class HomePresenterImpl extends BasePresenterImpl<HomeView> {

    private String TAG = HomePresenterImpl.class.getName();
    private final IHomeModel mIHomeModel;

    public HomePresenterImpl() {
        mIHomeModel = new HomeModelImpl();
    }

    public void getConfig() {
        Log.e(TAG, "请求配置信息");
        mIHomeModel.getConfig(new CallBack3() {
            @Override
            public void onSuccess(String code, String msg, Object data) {
                if (!code.equals("0")) {
                    getView().onErrorCodeNonZero(msg);
                    return;
                }
                DeviceConfigBean config = (DeviceConfigBean) data;
                getConfigSuccess(config);
            }

            @Override
            public void onFailed(Throwable e) {
                int i = 0;
            }
        });
    }

    /**
     * 成功返回配置信息
     *
     * @param config
     */
    private void getConfigSuccess(DeviceConfigBean config) {

        if (config == null) {
            getView().showUnregistered();
            return;
        }
        // 保存配置信息
        Constants.setDeviceConfig(config);

//                openMQTTService(config);

        DeviceConfigBean.HotelConfigBean hotelConfig = config.getHotel_config();
        if (hotelConfig == null) {
            Toast.makeText(getView().getActivity(), "酒店配置信息为null,请重启app", Toast.LENGTH_SHORT).show();
            return;
        }
        // 决定是否显示二维码
        getQrCode(hotelConfig.isEnabled_mobile_checkin(), config.getHotel_id(), Constants.getAndroid().getDeviceId());
        // 显示app信息
        getView().showAppInfo(config, Constants.getAndroid());
        getView().showNavigationBar(config);

        String devBranch = Constants.wqt_version;//开发分支
        String prodBranch = config.getHotel_config().getProduct_version(); // 服务器分支
        boolean different = !isEmpty(devBranch) && !isEmpty(prodBranch) && !devBranch.equals(prodBranch);
        //当前灰度版本不一致时，重新请求后台配置
        if (different) {
            Constants.wqt_version = prodBranch;
            getConfig();
        }
    }

    /**
     * 展示二维码
     *
     * @param enableMobileCheckin 支持手机入住
     * @param hotelId             酒店Id
     * @param deviceId            设备id
     */
    private void getQrCode(boolean enableMobileCheckin, String hotelId, String deviceId) {
        if (enableMobileCheckin) {
            // 支持手机入住
            boolean fromInternet = Constants.wqt_env.contains("p") || Constants.wqt_env.contains("s");
            if (fromInternet) {
                // 通过网络请求生成二维码
                getQrCode(hotelId);
            } else {
                String url = NetUtils.getQRCodeUrl(String.format("hotel_id=%s&device_id=%s", hotelId, deviceId));
                Bitmap bitmap = CodeUtils.createImage(url, Constants.QR_CODE_WIDTH, Constants.QR_CODE_WIDTH, null);
                getView().showQrCode(bitmap);
            }
        } else {
            getView().showQrCode(null);
        }
    }

    /**
     * 生成二维码
     *
     * @param hotelId 酒店id
     */
    private void getQrCode(String hotelId) {

        mIHomeModel.getQrCode(hotelId, new CallBack4() {
            @Override
            public void onSuccess(Object obj) {
                byte[] bytes = (byte[]) obj;
                if (bytes == null) {
                    Logger.e("网络请求生成二维码为空");
                    return;
                }
                final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                getView().showQrCode(bitmap);
            }

            @Override
            public void onFailed(Throwable e) {
                getView().onFailed(e);
                getView().showUnregistered();
            }
        });
    }


}
