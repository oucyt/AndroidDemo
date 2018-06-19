package cn.fortrun.magic.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.fortrun.magic.R;
import cn.fortrun.magic.common.BaseActivity;
import cn.fortrun.magic.model.BASEResponse;
import cn.fortrun.magic.model.bean.IDCardBean;
import cn.fortrun.magic.presenter.MainPresenterImpl;
import cn.fortrun.magic.ui.MainView;
import cn.fortrun.magic.ui.fragment.BaseSupportFragment;
import cn.fortrun.magic.ui.fragment.HomeFragment;
import cn.fortrun.magic.utils.Gsons;
import cn.fortrun.magic.utils.mqtt.MqttManager;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
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
        requestRxPermissions();
    }

    /**
     * 权限监测
     */
    @SuppressLint("CheckResult")
    public void requestRxPermissions() {
        RxPermissions rxPermissions = new RxPermissions(this);
        String[] permissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CAMERA,
                Manifest.permission.RECEIVE_BOOT_COMPLETED};
        rxPermissions.request(permissions).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean granted) throws Exception {
                if (granted) {
                    // TODO
                } else {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("提示")
                            .setMessage("该应用不可不授权指定权限，请前去设置打开该些权限")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startAppSettings();
                                }
                            }).show();
                }
            }
        });
    }

    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, 100);
    }

    @Override
    public Activity getAc() {
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


        if (cmd.equals(MqttManager.BACK_IDCARD_INFO)) {
            // 身份证
            BASEResponse baseResponse = Gsons.fromJson(json, BASEResponse.class, IDCardBean.class);
            IDCardBean roomCard = (IDCardBean) baseResponse.getData();
            EventBusActivityScope.getDefault(this).post(roomCard);
        }
//        if (cmd.equals(MqttManager.BACK_ROOMCARD_INFO)) {
//            //底座回收房卡，返回房卡信息
//            BASEResponse baseResponse = Gsons.fromJson(json, BASEResponse.class, RoomCardBean.class);
//            RoomCardBean roomCard = (RoomCardBean) baseResponse.getData();
//            showCheckoutUI();
//            if (roomCard != null) {
//                EventBusActivityScope.getDefault(this).post(roomCard);
//                mPresenter.getCheckoutInfo(roomCard, Constants.getDeviceConfig());
//            }
//        } else if (cmd.equals(CMD.Received.BASE_STATUS.code())) {
//            // 底座主动发送，返回底座信息
//            BASEResponse baseResponse = Gsons.fromJson(json, BASEResponse.class, BaseStateBean.class);
//            BaseStateBean baseState = (BaseStateBean) baseResponse.getData();
//            updateBaseStatus(baseState);
//        } else if (cmd.equals(CMD.Common.QUERY_DEVICE.code())) {
//            // 底座查询app状态，app升级过程为disable
//            String status = appStatus == APP_UPGRADING ? "DISABLE" : "ENABLE";
//            MQTTService.publish(cmd, status, sender);
//        } else if (cmd.equals(CMD.Common.AUTO_UPGRADE.code())) {
//            // 底座发起升级指令，app做出响应
//            BASEResponse baseResponse = Gsons.fromJson(json, BASEResponse.class, UpdateBean.class);
//            UpdateBean bean = (UpdateBean) baseResponse.getData();
////            mDownloadBinder.setInstallMode(true);
//            if (!isEmpty(bean.getFile())) {
//                SPUtils sp = SPs.getSP();
//                if (!sp.getBoolean("down_load")) {
//                    sp.put("down_load", true);
//                    long downloadId = mDownloadBinder.startDownload(bean.getFile());
//                    mPresenter.startCheckProgress(downloadId, mDownloadBinder);
//                }
//            }
//        } else if (cmd.equals(CMD.Common.DISABLE_DEVICE.code())) {
//            // 底座决定启用/禁用 app
//            BASEResponse baseResponse = Gsons.fromJson(json, BASEResponse.class, UpdateBean.class);
//            UpdateBean bean = (UpdateBean) baseResponse.getData();
//            changeAppStatus(bean);
//        }


    }

    private void showCheckoutUI() {
//        BaseSupportFragment fragment = findFragment(CheckoutFragment.class);
//        if (fragment == null)
//            start(CheckoutFragment.newInstance());
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
