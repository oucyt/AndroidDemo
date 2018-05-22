package com.ty.thread;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.lang.ref.SoftReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.text_view)
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        Log.i("dfasdf","fadsfasefw;ljfdlsajfoasldjfoassf jas,vnask;vdc");
//        System.out.println("sadfdasdf");
        System.out.println("开始 ");
         final UIHandler mUIHandler = new UIHandler(this);
        HandlerThread handlerThread = new HandlerThread("子线程");
        handlerThread.start();
        Handler mHandler = new Handler(handlerThread.getLooper()) {

            @Override
            public void handleMessage(Message msg) {
                //处理消息，这里是运行在子线程上的
                mUIHandler.sendEmptyMessage(2);
                System.out.println("HandlerThread运行在 " + Thread.currentThread().getName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                float score = FRUtils.recognition(mBytes, mCamera);
                mUIHandler.sendEmptyMessage(3);
            }
        };
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                mUIHandler.sendEmptyMessage(2);
//            }
//        }).start();
        while (true) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mHandler.sendEmptyMessage(4);
        }
    }

    private static class UIHandler extends Handler {

        SoftReference<Activity> mSoftReference;

        public UIHandler(Activity activity) {
            mSoftReference = new SoftReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity ac = (MainActivity) mSoftReference.get();
            System.out.println("ddddddddddddddddddddd");
            switch (msg.what) {
                case 2:
                    ac.mTextView.setText("子线程正在处理   " + Thread.currentThread().getName());
                    System.out.println("子线程正在处理   " + Thread.currentThread().getName());
                    break;

                case 3:
                    ac.mTextView.setText("子线程处理完毕   " + Thread.currentThread().getName());
                    System.out.println("子线程处理完毕   " + Thread.currentThread().getName());
            }
        }
    }
}
