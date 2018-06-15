package cn.fortrun.magic.model;

import cn.fortrun.magic.common.mvp.IModel;
import cn.fortrun.magic.interfaces.CallBack3;
import cn.fortrun.magic.interfaces.CallBack4;

/**
 * description
 *
 * @author tianyu
 * @create 2018.06.15 上午10:31
 * @since 1.0.0
 */
public interface IHomeModel extends IModel {
    /**
     * 请求设备所对应的酒店的配置信息
     *
     * @param callBack
     */
    void getConfig(CallBack3 callBack);

    /**
     * 请求二维码
     *
     * @param hotelId
     * @param callBack
     */
    void getQrCode(String hotelId, CallBack4 callBack);

}
