package com.ty.framework;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;

/**
 * description
 *
 * @author 87627
 * @create 2018.05.12 13:41
 * @since 1.0.0
 */
public interface WeatherApi {

    @GET("weatherApi?city=%E9%87%8D%E5%BA%86")
    Observable<ResponseBody> getWeather();
}
