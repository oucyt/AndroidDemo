package cn.fortrun.magic.presenter;

import android.app.Activity;
import android.content.Intent;

import cn.fortrun.magic.common.Constants;
import cn.fortrun.magic.common.mvp.BasePresenterImpl;
import cn.fortrun.magic.interfaces.CallBack3;
import cn.fortrun.magic.model.ICheckoutModel;
import cn.fortrun.magic.model.bean.CheckOutInfo;
import cn.fortrun.magic.model.bean.DeviceConfigBean;
import cn.fortrun.magic.model.bean.RoomCardBean;
import cn.fortrun.magic.model.impl.CheckoutModelImpl;
import cn.fortrun.magic.ui.CheckoutView;

/**
 *
 */
public class CheckoutPresenterImpl extends BasePresenterImpl<CheckoutView> {

    private String TAG = CheckoutPresenterImpl.class.getName();
    private ICheckoutModel mICheckoutModel;

    public CheckoutPresenterImpl() {
        mICheckoutModel = new CheckoutModelImpl();
    }

    /**
     * 吃卡退房
     * 获取房间信息
     *
     * @param roomCard
     */
    public void getCheckoutInfo(final RoomCardBean roomCard) {

        mICheckoutModel.remoteCheckoutInfo(roomCard, new CallBack3() {

            @Override
            public void onSuccess(String code, String msg, Object data) {
//
//                getView().showRecycleRoomCard(false);
                if (!code.equals("0")) {
                    // 异常
                    getView().onErrorCodeNonZero(msg);
                    return;
                }

                CheckOutInfo info = (CheckOutInfo) data;
                getCheckoutInfoSuccess(roomCard,info);

            }

            @Override
            public void onFailed(Throwable e) {
                getView().showRecycleRoomCard(false);
                getView().onFailed(e);
                getView().showUnregistered();
            }
        });

    }

    private void getCheckoutInfoSuccess(RoomCardBean roomCard, CheckOutInfo info) {
        DeviceConfigBean bean = Constants.getDeviceConfig();
        if (bean.getHotel_config()==null)
            return;
        if (bean.getHotel_config().isEnabled_pms_in_guest_checkout()) {
            // 允许PMS入住用户离店
            flowArriveAndLeaveOneDay(roomCard, info, deviceConfig);
        } else {
            if (info.isDevice_check_in()) {
                // 自助机办理入住
                flowArriveAndLeaveOneDay(roomCard, info, deviceConfig);
            } else {
                // 人工入住

                Activity ac = getView().getActivity();
                Intent intent = new Intent(ac, CheckOutActivity.class);
                intent.putExtra("check_tag", "NO_DEVICE");
                if (roomCard != null) {
                    intent.putExtra("business", roomCard);
                }
                ac.startActivity(intent);
            }
        }
    }
    /**
     * 判断房间是否具有当日抵离的属性，以及酒店是否支持当日抵离
     * 下一流程为退房时间状态（提前、准时、超时)判断
     *
     * @param roomCard
     * @param info
     * @param deviceConfig
     */
    private void flowArriveAndLeaveOneDay(RoomCardBean roomCard, CheckOutInfo info, DeviceConfigBean deviceConfig) {

        if (info.isSame_day_io()) {
            // 房间属性为当日抵离的（参考钟点房）
            if (deviceConfig.getHotel_config().isEnabled_same_date_io()) {
                // 允许当日抵离，进入下一流程
                flowCheckoutType(info, roomCard, deviceConfig);
            } else {
                //不允许当日抵离,去前台退房
                Activity ac = getView().getActivity();
                Intent intent = new Intent(ac, CheckOutActivity.class);
                intent.putExtra("check_tag", "NO_CHECKOUT");
                intent.putExtra("business", roomCard);
                ac.startActivity(intent);
            }
        } else {
            // 进入下一流程
            flowCheckoutType(info, roomCard, deviceConfig);
        }
    }
}
