package com.ty.glide;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.image_view)
    ImageView mImageView;
    @BindView(R.id.btn)
    Button mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        https://wqt.fortrun.cn/p/master/libra/wxaapp/qrcode?hotel_id=e455781a04fb461da25db491797820a0&device_id=5f1cbabedbe72bf94f098e2dafa09298


    }

    @OnClick(R.id.btn)
    public void onViewClicked() {
        String url1 = "http://pic24.photophoto.cn/20120830/0005018305110431_b.jpg";
        String url2 = "https://wqt.fortrun.cn/p/master/libra/wxaapp/qrcode?hotel_id=e455781a04fb461da25db491797820a0&device_id=5f1cbabedbe72bf94f098e2dafa09298";
        Glide.with(MainActivity.this)
                .load(url2)
                .into(mImageView);
    }


}
