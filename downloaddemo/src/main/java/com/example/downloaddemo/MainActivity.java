package com.example.downloaddemo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_download)
    Button mBtnDownload;
    @BindView(R.id.tv_console)
    TextView mTvConsole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_download)
    public void onViewClicked() {
        startDownload();
    }

    @SuppressLint("CheckResult")
    private void startDownload() {
        mTvConsole.setText("开始下载");
        String baseUrl = "http://for-test01.oss-cn-beijing.aliyuncs.com/";
        DownloadUtils utils = new DownloadUtils(baseUrl, new JsDownloadListener() {
            @Override
            public void onStartDownload() {

            }

            @Override
            public void onProgress(int progress) {

            }

            @Override
            public void onFinishDownload() {

            }

            @Override
            public void onFail(String errorInfo) {

            }
        });
        String url = "%E5%B9%BF%E5%91%8A4.2%201080.mp4";
        utils.download(url, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath()+ File.separator+"ddd.mp4", new Consumer<InputStream>() {
            @Override
            public void accept(InputStream inputStream) throws Exception {
                mTvConsole.append("下载完成");
            }
        });
    }
}
