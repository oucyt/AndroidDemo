package alibabamqtt;

import android.app.Application;
import android.util.Log;

import com.aliyun.alink.linksdk.channel.core.persistent.PersistentNet;
import com.aliyun.alink.linksdk.channel.core.persistent.event.IConnectionStateListener;
import com.aliyun.alink.linksdk.channel.core.persistent.event.PersistentEventDispatcher;
import com.aliyun.alink.linksdk.channel.core.persistent.mqtt.MqttConfigure;
import com.aliyun.alink.linksdk.channel.core.persistent.mqtt.MqttInitParams;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.alink.linksdk.tools.ThreadTools;

public class App extends Application {
    private static final String TAG = App.class.getName();

    @Override
    public void onCreate() {
        super.onCreate();

        String packageName = getPackageName();
        if (packageName == null || !packageName.equals(ThreadTools.getProcessName(this, android.os.Process.myPid()))) {
            return;
        }
        // 打开SDK内部日志输出开关。
        ALog.setLevel(ALog.LEVEL_DEBUG);
        initMqtt();
    }

    /**
     * 初始化长连接
     * 只能初始化一次 一般放在 application 初始化的时候初始化
     * 三元组信息请存储在在安全区域 不要写死在代码里
     */
    private void initMqtt() {
        Log.d(TAG, "initMqtt");
        // 环境配置需要在SDK初始化之前。SDK 支持自定义切换Host。
        MqttConfigure.mqttHost = "ssl://" + DemoConfig.productKey + ".iot-as-mqtt.cn-shanghai.aliyuncs.com:1883";
        // 拿三元组信息对SDK初始化，SDK 会进行 MQTT 建联。
        MqttInitParams initParams = new MqttInitParams(DemoConfig.productKey, DemoConfig.deviceName, DemoConfig.deviceSecret);
        PersistentNet.getInstance().init(this, initParams);
        // 添加通道状态变化监听
        PersistentEventDispatcher.getInstance().registerOnTunnelStateListener(connectionStateListener, false);
    }


    /**
     * 长连接断连 资源释放
     * 一般在 crash 捕捉的时候调用
     */
    private void deinitMqtt() {
        Log.d(TAG, "disconnect ");
        PersistentEventDispatcher.getInstance().unregisterOnTunnelStateListener(connectionStateListener);//取消连接监听
        PersistentNet.getInstance().destroy();
    }

    private IConnectionStateListener connectionStateListener = new IConnectionStateListener() {
        @Override
        public void onConnectFail(String s) {
            Log.d(TAG, "connection onConnectFail " + s);
        }

        @Override
        public void onConnected() {
            Log.d(TAG, "connection onConnected ");
        }

        @Override
        public void onDisconnect() {
            Log.d(TAG, "connection onDisconnect ");
        }
    };
}
