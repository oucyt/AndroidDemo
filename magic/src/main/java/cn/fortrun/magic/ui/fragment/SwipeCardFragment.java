package cn.fortrun.magic.ui.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import cn.fortrun.magic.R;
import cn.fortrun.magic.model.bean.IDCardBean;
import cn.fortrun.magic.presenter.ResultPresenterImpl;
import cn.fortrun.magic.ui.ResultView;
import cn.fortrun.magic.utils.mqtt.MqttManager;
import me.yokeyword.eventbusactivityscope.EventBusActivityScope;

/**
 * description
 * 刷卡提示界面
 *
 * @author tianyu
 * @create 2018.06.14 上午11:29
 * @since 1.0.0
 */
public class SwipeCardFragment extends BaseFragment<ResultPresenterImpl, ResultView> implements ResultView {

    private static final String TITLE = "title";
    private static final String SUB = "sub";
    private static final String URL = "url";
    private static final String RESID = "res_id";
    private static final String COUNTDOWN = "countdown";

    @BindView(R.id.btn_left)
    Button mBtnLeft;
    @BindView(R.id.tv_center)
    TextView mTvCenter;
    @BindView(R.id.tv_right)
    TextView mTvRight;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_sub)
    TextView mTvSub;
    @BindView(R.id.image_view)
    ImageView mImageView;

    private CountDownTimer mCountDownTimer;


    private OnGetIDCardSuccessListener mOnGetIDCardSuccessListener;

    public static SwipeCardFragment newInstance() {
        return new SwipeCardFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.attachView(this);
        EventBusActivityScope.getDefault(_mActivity).register(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getIDCard(IDCardBean bean) {
        if (bean != null && mOnGetIDCardSuccessListener != null) {
            mOnGetIDCardSuccessListener.doNext(bean);
        }
    }

    private void initView() {
        Bundle bundle = getArguments();
        if (bundle == null)
            return;
        String title = bundle.getString(TITLE);
        String sub = bundle.getString(SUB);
        String url = bundle.getString(URL);
        int resId = bundle.getInt(RESID);
        int count = bundle.getInt(COUNTDOWN);
        mTvTitle.setText(title);
        mTvSub.setText(sub);
        if (!TextUtils.isEmpty(url)) {
            // TODO
        }
        if (resId > 0)
            mImageView.setImageResource(resId);

        // 显示返回键
        mBtnLeft.setVisibility(View.VISIBLE);
        //                Logger.i("倒计时 还剩 %ds", mTempMillisInFuture / 1000);
//                mTempMillisInFuture = millisUntilFinished;//保存当前计时器的值
//                mICountDown.onTick(millisUntilFinished);
//                Logger.i("倒计时结束");
//                mICountDown.onFinish();
        mCountDownTimer = new CountDownTimer(count * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
//                Logger.i("倒计时 还剩 %ds", mTempMillisInFuture / 1000);
//                mTempMillisInFuture = millisUntilFinished;//保存当前计时器的值
//                mICountDown.onTick(millisUntilFinished);
                String s = (int) (millisUntilFinished / 1000) + "s";
                mTvRight.setText(s);
            }

            @Override
            public void onFinish() {
//                Logger.i("倒计时结束");
//                mICountDown.onFinish();
                pop();
            }
        }.start();

        // 激活读卡器
        MqttManager.activation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusActivityScope.getDefault(_mActivity).unregister(this);
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
        // 释放读卡器
        MqttManager.release();
    }

    /**
     * @param title      标题
     * @param sub        子标题
     * @param url        二维码链接，需要自己生成图片
     * @param resourceId 资源id，可直接使用
     * @param countdown  倒计时长
     */
    public static Bundle deliver(String title, String sub, String url, int resourceId, int countdown) {
        Bundle bundle = new Bundle();
        bundle.putString(TITLE, title);
        bundle.putString(SUB, sub);
        bundle.putString(URL, url);
        bundle.putInt(RESID, resourceId);
        bundle.putInt(COUNTDOWN, countdown);
        return bundle;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_notice;
    }

    @Override
    protected ResultPresenterImpl getPresenter() {
        return new ResultPresenterImpl();
    }


    @OnClick(R.id.btn_left)
    public void onViewClicked() {
        pop();
    }

    public void setOnGetIDCardSuccessListener(OnGetIDCardSuccessListener onGetIDCardSuccessListener) {
        mOnGetIDCardSuccessListener = onGetIDCardSuccessListener;
    }

    /**
     * 获取身份证后，决定下一步的走向 1.拉取订单 2.相机预览
     */
    public static interface OnGetIDCardSuccessListener {
        void doNext(IDCardBean idCardBean);
    }
}
