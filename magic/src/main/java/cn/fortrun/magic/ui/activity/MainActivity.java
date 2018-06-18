package cn.fortrun.magic.ui.activity;

import android.app.Activity;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.fortrun.magic.R;
import cn.fortrun.magic.common.BaseActivity;
import cn.fortrun.magic.common.CMD;
import cn.fortrun.magic.common.Constants;
import cn.fortrun.magic.model.BASEResponse;
import cn.fortrun.magic.model.bean.RoomCardBean;
import cn.fortrun.magic.presenter.MainPresenterImpl;
import cn.fortrun.magic.ui.MainView;
import cn.fortrun.magic.ui.fragment.BaseSupportFragment;
import cn.fortrun.magic.ui.fragment.CheckoutFragment;
import cn.fortrun.magic.ui.fragment.HomeFragment;
import cn.fortrun.magic.utils.Gsons;
import cn.fortrun.magic.utils.MqttManager;
import me.yokeyword.eventbusactivityscope.EventBusActivityScope;


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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMqttMessage(String json) {
        if (isEmpty(json))
            return;

        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        String cmd = jsonObject.get("cmd").getAsString();
        String sender = jsonObject.get("sender").getAsString();

        if (cmd.equals(MqttManager.BACK_ROOMCARD_INFO)) {
            //底座回收房卡，返回房卡信息
            BASEResponse baseResponse = Gsons.fromJson(json, BASEResponse.class, RoomCardBean.class);
            RoomCardBean roomCard = (RoomCardBean) baseResponse.getData();
            showCheckoutUI();
            if (roomCard != null) {
                EventBusActivityScope.getDefault(this).post(roomCard);
                mPresenter.getCheckoutInfo(roomCard, Constants.getDeviceConfig());
            }
        } else if (cmd.equals(CMD.Received.BASE_STATUS.code())) {
            // 底座主动发送，返回底座信息
            BASEResponse baseResponse = Gsons.fromJson(json, BASEResponse.class, BaseStateBean.class);
            BaseStateBean baseState = (BaseStateBean) baseResponse.getData();
            updateBaseStatus(baseState);
        } else if (cmd.equals(CMD.Common.QUERY_DEVICE.code())) {
            // 底座查询app状态，app升级过程为disable
            String status = appStatus == APP_UPGRADING ? "DISABLE" : "ENABLE";
            MQTTService.publish(cmd, status, sender);
        } else if (cmd.equals(CMD.Common.AUTO_UPGRADE.code())) {
            // 底座发起升级指令，app做出响应
            BASEResponse baseResponse = Gsons.fromJson(json, BASEResponse.class, UpdateBean.class);
            UpdateBean bean = (UpdateBean) baseResponse.getData();
//            mDownloadBinder.setInstallMode(true);
            if (!isEmpty(bean.getFile())) {
                SPUtils sp = SPs.getSP();
                if (!sp.getBoolean("down_load")) {
                    sp.put("down_load", true);
                    long downloadId = mDownloadBinder.startDownload(bean.getFile());
                    mPresenter.startCheckProgress(downloadId, mDownloadBinder);
                }
            }
        } else if (cmd.equals(CMD.Common.DISABLE_DEVICE.code())) {
            // 底座决定启用/禁用 app
            BASEResponse baseResponse = Gsons.fromJson(json, BASEResponse.class, UpdateBean.class);
            UpdateBean bean = (UpdateBean) baseResponse.getData();
            changeAppStatus(bean);
        }


    }

    private void showCheckoutUI() {
        BaseSupportFragment fragment = findFragment(CheckoutFragment.class);
        if (fragment == null)
            start(CheckoutFragment.newInstance());
        // TODO
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
