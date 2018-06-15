package cn.fortrun.magic.ui.activity;

import android.app.Activity;
import android.widget.Toast;

import cn.fortrun.magic.R;
import cn.fortrun.magic.common.BaseActivity;
import cn.fortrun.magic.presenter.MainPresenterImpl;
import cn.fortrun.magic.ui.MainView;
import cn.fortrun.magic.ui.fragment.BaseSupportFragment;
import cn.fortrun.magic.ui.fragment.HomeFragment;


public class MainActivity extends BaseActivity<MainPresenterImpl, MainView> implements MainView {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected Object getPresenter() {
        return new MainPresenterImpl();
    }

    @Override
    protected void initView() {
        mPresenter.attachView(this);
        BaseSupportFragment fragment = findFragment(HomeFragment.class);
        if (fragment == null) {
            loadRootFragment(R.id.fl_container, HomeFragment.newInstance());
        }
    }

    @Override
    public Activity getActivity() {
        return MainActivity.this;
    }

    @Override
    public void onErrorCodeNonZero(String msg) {

    }

    @Override
    public void onFailed(Throwable e) {

    }

    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;

    @Override
    public void onBackPressedSupport() {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                pop();
            } else {
                if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
                    finish();
                } else {
                    TOUCH_TIME = System.currentTimeMillis();
                    Toast.makeText(this, R.string.main_press_again_exit, Toast.LENGTH_SHORT).show();
                }
            }
    }
}
