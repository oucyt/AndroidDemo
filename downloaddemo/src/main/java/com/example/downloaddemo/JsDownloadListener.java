package com.example.downloaddemo;

/**
 * description
 *
 * @author 87627
 * @create 2018.09.06 22:38
 * @since 1.0.0
 */
public  interface JsDownloadListener {

    void onStartDownload();

    void onProgress(int progress);

    void onFinishDownload();

    void onFail(String errorInfo);

}