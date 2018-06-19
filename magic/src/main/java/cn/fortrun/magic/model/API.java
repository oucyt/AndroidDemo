package cn.fortrun.magic.model;

import cn.fortrun.magic.model.bean.DeviceConfigBean;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * description
 *
 * @author tianyu
 * @create 2018.05.22 下午1:54
 * @since 1.0.0
 */
public interface API {
    /**
     * 提交人脸识别结果
     *
     * @param hotelId
     * @param params
     * @return
     */
    @POST("wop-violet/identity")
    Observable<JSONResponse<Object>> postResult(@Query("hotel_id") String hotelId, @Body RequestBody params);

    /**
     * 旅业上传接口(公安)
     *
     * @param params
     * @return
     */
    @POST("libra/lvReport")
    Observable<JSONResponse<String>> lvReport(@Body RequestBody params);

//    /**
//     * 入住
//     *
//     * @param params
//     * @return
//     */
//    @POST("libra/checkin")
//    Observable<JSONResponse<RoomStatus>> checkin(@Body RequestBody params);
//
//    @POST("libra/order/order_guest/room_card/{order_guest}")
//    Observable updateCard(@Path("order_guest") String orderGuest);


    @POST("libra/get_config")
    Observable<JSONResponse<DeviceConfigBean>> getDeviceConfig(@Body RequestBody params);

//    @POST("libra/wxaapp/createwxaqrcode")
//    Observable<Response> getQrCode(@Body RequestBody params);
//
//    @POST("libra/room/info")
//    Observable<JSONResponse<CheckOutInfo>> getCheckoutInfo(@Body RequestBody params);

//
//    @POST("libra/order/subOrder")
//    Observable<JSONResponse<Order>> getHotelOrder(@Body RequestBody params);
//
//    @POST("libra/order/orderIds")
//    Observable<JSONResponse<List<String>>> getHotelOrderIds(@Body RequestBody params);
//
//    @POST("libra/facein")
//    Observable<JSONResponse<FaceIn>> remoteVerifyByIDCard(@Body RequestBody params);
//
//    @POST("libra/checkin")
//    Observable<JSONResponse<RoomStatus>> remoteCheckIn(@Body RequestBody params);
//
//
//    @POST("libra/facein")
//    Observable<JSONResponse<FaceIn>> faceIdentify(@Body RequestBody params);
//
//
//    @POST("/identity?hotel_id=xxx")
//    Observable<JSONResponse<String>> identifyUser(@Body RequestBody params);
//
//    @POST("taurus-rc/rcPrintSheet/updateGuestElectSign")
//    Observable<JSONResponse<Boolean>> commit(@Body RequestBody params);
//
//    @POST("taurus-rc/rcPrintSheet/bill/detail")
//    Observable<JSONResponse<BillConfig>> getOrderDetail(@Body RequestBody params);

}
