package cn.fortrun.magic.model;

import cn.fortrun.magic.common.mvp.IModel;
import cn.fortrun.magic.interfaces.CallBack3;
import cn.fortrun.magic.model.bean.RoomCardBean;

/**
 * description
 *
 * @author tianyu
 * @create 2018.06.15 上午10:31
 * @since 1.0.0
 */
public interface ICheckoutModel extends IModel {

    /**
     * 回收房卡
     *
     * @param roomCardBean
     * @param callBack2
     */
    void remoteCheckoutInfo(RoomCardBean roomCardBean,  CallBack3 callBack2);
}
