package cn.fortrun.magic.model.impl;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import cn.fortrun.magic.common.Constants;
import cn.fortrun.magic.common.mvp.BaseModelImpl;
import cn.fortrun.magic.interfaces.CallBack3;
import cn.fortrun.magic.model.API;
import cn.fortrun.magic.model.ICheckoutModel;
import cn.fortrun.magic.model.JSONResponse;
import cn.fortrun.magic.model.bean.CheckOutInfo;
import cn.fortrun.magic.model.bean.RoomCardBean;
import cn.fortrun.magic.utils.net.RetrofitFactory;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

/**
 * description
 * @author tianyu
 * @create 2018.06.15 上午10:31
 * @since 1.0.0
 */
public class CheckoutModelImpl extends BaseModelImpl implements ICheckoutModel {
    /**
     * 回收房卡
     *
     * @param roomCard
     * @param callBack2
     */
    @Override
    public void remoteCheckoutInfo(RoomCardBean roomCard, final CallBack3 callBack2) {

        Map<String, String> params = new HashMap<>();
        params.put("hotel_id", roomCard.getHotel_id());
        params.put("lock_code_type", roomCard.getLock_code_type());
        params.put("lock_code", roomCard.getLock_code());
        params.put("device_id", Constants.getAndroid().getDeviceId());

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(params));

        Retrofit retrofit = RetrofitFactory.getInstance();
        API api = retrofit.create(API.class);
        api.getCheckoutInfo(body).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JSONResponse<CheckOutInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(JSONResponse<CheckOutInfo> response) {
                        callBack2.onSuccess(response.getErrcode(), response.getErrmsg(), response.getData());
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack2.onFailed(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
}
