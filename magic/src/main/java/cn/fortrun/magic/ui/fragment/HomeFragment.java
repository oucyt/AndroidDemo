package cn.fortrun.magic.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.fortrun.magic.R;
import cn.fortrun.magic.common.Constants;
import cn.fortrun.magic.model.bean.DeviceConfigBean;
import cn.fortrun.magic.presenter.HomePresenterImpl;
import cn.fortrun.magic.ui.HomeView;
import cn.fortrun.magic.utils.DensityUtil;
import cn.fortrun.magic.utils.SupportMultipleScreensUtil;

/**
 * description
 *
 * @author tianyu
 * @create 2018.06.14 上午11:29
 * @since 1.0.0
 */
public class HomeFragment extends BaseFragment<HomePresenterImpl, HomeView> implements HomeView {
    /**
     * 左上角酒店logo或名称
     */
    @BindView(R.id.tv_hotel_logo)
    TextView mTvHotelLogo;
    /**
     * 广告页入口按钮
     */
    @BindView(R.id.btn_hotel_introduce)
    Button mBtnHotelIntroduce;
    /**
     * 身份证验证入口按钮
     */
    @BindView(R.id.btn_identity_check)
    Button mBtnIdentityCheck;
    /**
     * 正中二维码
     */
    @BindView(R.id.tv_qr_code)
    TextView mTvQrCode;
    /**
     * 取卡入口按钮
     */
    @BindView(R.id.btn_take_card)
    Button mBtnTakeCard;
    /**
     * 右下角app信息
     */
    @BindView(R.id.tv_app_info)
    TextView mTvAppInfo;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.attachView(this);
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        Toast.makeText(_mActivity, "动画结束时回调", Toast.LENGTH_SHORT).show();
        mPresenter.getConfig();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected HomePresenterImpl getPresenter() {
        return new HomePresenterImpl();
    }


    @OnClick({R.id.btn_hotel_introduce, R.id.btn_identity_check, R.id.btn_take_card})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_hotel_introduce:
                break;
            case R.id.btn_identity_check:
                break;
            case R.id.btn_take_card:
                break;
        }
    }

    @Override
    public void showUnregistered() {
        String deviceId = Constants.getAndroid().getDeviceId();
        if (!isEmpty(deviceId)) {
            Bitmap mQrCodeCached = CodeUtils.createImage(deviceId, Constants.QR_CODE_WIDTH, Constants.QR_CODE_WIDTH, null);
            // 显示二维码
            Drawable drawable = new BitmapDrawable(mQrCodeCached);
            mTvQrCode.setCompoundDrawables(null, drawable, null, null);
        }
    }

    @Override
    public void showQrCode(Bitmap bitmap) {
        if (bitmap != null) {
            DensityUtil.setParams(mTvQrCode, ScreenUtils.getScreenWidth(), Constants.QR_CODE_WIDTH);
            SupportMultipleScreensUtil.scale(mTvQrCode);
            Drawable drawable = new BitmapDrawable(bitmap);
            // 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mTvQrCode.setCompoundDrawables(null, drawable, null, null);
        } else {
            // 隐藏二维码提示语
            mTvQrCode.setText("");
        }
    }

    @Override
    public void showAppInfo(DeviceConfigBean config, Constants.Android android) {
        if (android == null || config == null)
            return;
        String appVersion = android.getAppVersion();//app版本号
        String deviceName = config.getDevice_name();//设备名称
        String env = Constants.wqt_env;//环境
        String serviceTel = config.getHotel_config() == null ? "" : config.getHotel_config().getCustomer_service_tel();

        StringBuilder sb = new StringBuilder("版本号：v" + appVersion);
        if (!isEmpty(deviceName))
            sb.append("\n设备名称：").append(deviceName);

        switch (env) {
            case "s":
                sb.append("\n环境：STG");
                break;
            case "q":
                sb.append("\n环境：TEST");
                break;
            case "i":
                sb.append("\n环境：INTG");
                break;
        }
        if (!isEmpty(serviceTel))
            sb.append("\n").append(serviceTel);

        mTvAppInfo.setText(sb.toString());
    }

    @Override
    public void showNavigationBar(DeviceConfigBean config) {
        DeviceConfigBean.HotelConfigBean hotel = config.getHotel_config();

        if (hotel == null)
            return;
        //是否开启身份核验
        switchVerifyButton(hotel.isEnabled_identity_check());
        //是否开启酒店自我介绍
        switchIntroduceButton(hotel.isEnabled_mirror_introduce());
        //是否开启品牌显示
        switchHotelBrand(hotel.isEnabled_mirror_brand(), "");

    }

    /**
     * 控制身份核验的按钮演示
     *
     * @param show
     */
    private void switchVerifyButton(boolean show) {
        if (show) {
            mBtnIdentityCheck.setVisibility(View.VISIBLE);
        } else {
            mBtnIdentityCheck.setVisibility(View.GONE);
        }
    }

    /**
     * 控制酒店介绍的按钮演示
     *
     * @param show
     */
    private void switchIntroduceButton(boolean show) {
        if (show) {
            mBtnHotelIntroduce.setVisibility(View.VISIBLE);
        } else {
            mBtnHotelIntroduce.setVisibility(View.GONE);
        }
    }

    /**
     * 控制酒店logo的显示
     *
     * @param show
     * @param url
     */
    private void switchHotelBrand(boolean show, String url) {
        if (show && !isEmpty(url)) {
            mTvHotelLogo.setText("");
            Glide.with(this)
                    .load(url)
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            Drawable drawable = new BitmapDrawable(resource);
                            // 这一步必须要做,否则不会显示.
                            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                            mTvHotelLogo.setCompoundDrawables(drawable, null, null, null);
                        }
                    });
        }
    }


}
