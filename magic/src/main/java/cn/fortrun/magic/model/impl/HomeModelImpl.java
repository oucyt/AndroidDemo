package cn.fortrun.magic.model.impl;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import cn.fortrun.magic.common.Constants;
import cn.fortrun.magic.common.mvp.BaseModelImpl;
import cn.fortrun.magic.interfaces.CallBack3;
import cn.fortrun.magic.interfaces.CallBack4;
import cn.fortrun.magic.model.API;
import cn.fortrun.magic.model.IHomeModel;
import cn.fortrun.magic.model.JSONResponse;
import cn.fortrun.magic.model.bean.DeviceConfigBean;
import cn.fortrun.magic.utils.net.RetrofitFactory;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

/**
 * description
 *
 * @author tianyu
 * @create 2018.06.15 上午10:31
 * @since 1.0.0
 */
public class HomeModelImpl extends BaseModelImpl implements IHomeModel {


    @Override
    public void getConfig(final CallBack3 callBack) {
        Retrofit retrofit = RetrofitFactory.getInstance();
        API api = retrofit.create(API.class);

        Map<String, String> params = new HashMap<>();
        params.put("device_id", Constants.getAndroid().getDeviceId());
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(params));

        api.getDeviceConfig(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JSONResponse<DeviceConfigBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(JSONResponse<DeviceConfigBean> response) {
                        callBack.onSuccess(response.getErrcode(), response.getErrmsg(), response.getData());
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onFailed(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getQrCode(String hotelId, CallBack4 callBack) {

    }
}
