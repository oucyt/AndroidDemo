package com.example.downloaddemo;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * description
 *
 * @author 87627
 * @create 2018.09.06 22:39
 * @since 1.0.0
 */
public class JsDownloadInterceptor  implements Interceptor {

    private JsDownloadListener downloadListener;

    public JsDownloadInterceptor(JsDownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        return response.newBuilder().body(
                new JsResponseBody(response.body(), downloadListener)).build();
    }
}