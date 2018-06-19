package cn.fortrun.magic.common;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Method;

import butterknife.ButterKnife;
import cn.fortrun.magic.common.mvp.IPresenter;
import cn.fortrun.magic.common.mvp.IView;
import cn.fortrun.magic.custom.MyDialog;
import cn.fortrun.magic.interfaces.ICountDown;
import cn.fortrun.magic.ui.activity.SupportActivity;
import cn.fortrun.magic.utils.SupportMultipleScreensUtil;
import cn.fortrun.magic.R;

/**
 * Created by wt on 2017/3/5.
 */
public abstract class BaseActivity<P extends IPresenter<V>, V extends IView> extends SupportActivity
        implements IView, View.OnClickListener {
    private static long lastTimeStamp = 0l;

    //判断横竖屏
    protected boolean isPortrait = false;

    //判断是否显示倒计时
    private boolean isShowTime = false;

    private Button mBtnBack;
    private TextView mTvTime;
    //判断是否已经绑定过EventBus

    private TextView mTvTitle;

    private View decorView;

    protected P mPresenter;
    private MyDialog mDialog;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayoutId() != 0) {
            //取消状态栏
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            // 横竖屏判断
            isPortrait = this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
            Logger.i("屏幕方向 %s", (isPortrait ? "竖屏" : "横屏"));
            if (isPortrait) {
                //竖屏
                SupportMultipleScreensUtil.init(this, 800);
            } else {
                //横屏
                SupportMultipleScreensUtil.init(this, 1280);
                isPortrait = false;
            }

            setContentView(getLayoutId());
            mPresenter = (P) getPresenter();

            // 内容显示区别父布局
            View rootView = findViewById(android.R.id.content);
            SupportMultipleScreensUtil.scale(rootView);
            //获取顶层视图
            decorView = getWindow().getDecorView();
            ButterKnife.bind(this);
//            EventBus.getDefault().register(this);
            mDialog = new MyDialog();

        }
        initView();

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // 存储当前倒计时的值，TODO 如果是0会怎么样
        outState.putLong(TAG_RESTORE_COUNTDOWN, mTempMillisInFuture);
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mTempMillisInFuture = savedInstanceState.getLong(TAG_RESTORE_COUNTDOWN);
        if (mTempMillisInFuture > 0) {
            Logger.i("倒计时重启。。。还剩 %ds", mTempMillisInFuture / 1000);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    protected abstract int getLayoutId();

    protected abstract Object getPresenter();

    protected abstract void initView();


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        //调用配置
        super.onStart();
        initAppUI();
    }


    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
    }

    /**
     * 记录当前计时器的值
     * 用于在翻转屏时保存与恢复
     */
    private long mTempMillisInFuture;
    /**
     * 倒计时标志位
     */
    private String TAG_RESTORE_COUNTDOWN = "tag_restore_countdown";

    /**
     * 由基类维护倒计时功能，集中处理翻转屏幕重走生命周期的问题
     */
    private CountDownTimer mCountDownTimer;

    /**
     * 开启倒计时
     *
     * @param millisInFuture 时长（ms）
     */
    public void openCountdown(final ICountDown mICountDown, long millisInFuture) {

        if (mICountDown == null)
            return;
        // 如果屏幕反转，那么会重走生命周期，导致这个函数被重新调用一次，并且传入的时长是初始值。
        // mTempMillisInFuture保存当前时长，默认为0
        if (mTempMillisInFuture > 0) {
            // 说明屏幕发生了翻转，并且倒计时逻辑还未结束
            millisInFuture = mTempMillisInFuture;
        }
        // TODO 如果mTempMillisInFuture已经被赋值为0，此时发生屏幕翻转如何判断，初始值为负？？？
        mCountDownTimer = new CountDownTimer(millisInFuture, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
//                Logger.i("倒计时 还剩 %ds", mTempMillisInFuture / 1000);
                mTempMillisInFuture = millisUntilFinished;//保存当前计时器的值
                mICountDown.onTick(millisUntilFinished);

            }

            @Override
            public void onFinish() {
                Logger.i("倒计时结束");
                mICountDown.onFinish();
            }
        }.start();
    }

    /**
     * 动态改变横竖屏背景
     */
    public void initBackground() {
//        RelativeLayout rl_root = (RelativeLayout) findViewById(R.id.rl_root);
//        if (MyApplication.isBig) {
//            rl_root.setBackgroundColor(Color.parseColor("#000000"));
//        }

    }

    public void initActionBarTitles(boolean showback, boolean showTime, boolean showTitle, String title) {
        this.isShowTime = showTime;
        mBtnBack = (Button) findViewById(R.id.btn_left);
        mTvTime = (TextView) findViewById(R.id.tv_right);
        mTvTitle = (TextView) findViewById(R.id.tv_center);

        mBtnBack.setOnClickListener(this);
        if (showTime) {
            mTvTime.setVisibility(View.VISIBLE);
        } else {
            mTvTime.setVisibility(View.GONE);
        }
        if (showback) {
            mBtnBack.setVisibility(View.VISIBLE);
        } else {
            mBtnBack.setVisibility(View.GONE);
        }
        if (showTitle) {
            mTvTitle.setVisibility(View.VISIBLE);
            mTvTitle.setText(title);
        } else {
            mTvTitle.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 退出程序.
     *
     * @param context
     */
    public static void exitApplication(Activity context) {
        long currentTimeStamp = System.currentTimeMillis();
        if (currentTimeStamp - lastTimeStamp > 1350L) {
            Toast.makeText(context, "再按一次退出", Toast.LENGTH_SHORT).show();
        } else {
            context.finish();
        }
        lastTimeStamp = currentTimeStamp;
    }

    /**
     * 执行关于你返回的操作
     */
    public void setBack() {
        finish();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                setBack();
                break;
        }
    }

    /**
     * 初始化app视图
     */
    public void initAppUI() {
        int flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        //判断当前版本在4.0以上并且存在虚拟按键，否则不做操作
        if (Build.VERSION.SDK_INT < 19 || !hasVirtualKey()) {
            //一定要判断是否存在按键，否则在没有按键的手机调用会影响别的功能。如之前没有考虑到，导致图传全屏变成小屏显示。
        } else {
            // 获取属性
            decorView.setSystemUiVisibility(flag);
        }
    }

    /**
     * 判断是否存在虚拟按键
     * TODO 虚拟按键
     *
     * @return
     */
    public boolean hasVirtualKey() {
        boolean hasNavigationBar = false;
        Resources resources = getResources();
        // 获取资源的id值，就是R.layout.activity,R.drawable.ic_launcher
        int id = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = resources.getBoolean(id);
        }
        try {
            Class<?> systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    protected boolean isEmpty(String str) {
        return TextUtils.isEmpty(str);
    }



    /**
     * 网络请求失败，P层完成请求后交给V层做后续处理
     *
     * @param e
     */
//    @Override
//    public void onFailed(Throwable e) {
//
//        if (NetUtils.isNetworkAvailable(this)) {
//
//            ToastUtils.showShort(this, getResources().getString(R.string.request_failed));
//        } else {
//            ToastUtils.showShort(this, getResources().getString(R.string.isNetWork));
//        }
//    }

//    @Override
//    public void loading() {
//        if (mDialog != null)
//            mDialog.show(getFragmentManager(), "loading");
//    }
//
//    @Override
//    public void loadFinished() {
//        if (mDialog != null)
//            mDialog.dismiss();
//    }

//    @Override
//    public void onErrorCodeNonZero(String msg) {
//        if (!isEmpty(msg)) {
//            Intent intent = new Intent(this, ErrorMsgUIActivity.class);
//            intent.putExtra("errmsg", msg);
//            startActivity(intent);
//            finish();
//        }
//    }
}
