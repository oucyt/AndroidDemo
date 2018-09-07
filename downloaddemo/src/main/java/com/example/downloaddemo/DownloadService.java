package com.example.downloaddemo;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * description
 *
 * @author 87627
 * @create 2018.09.06 22:39
 * @since 1.0.0
 */
public  interface DownloadService {

    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);

}