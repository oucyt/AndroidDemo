package cn.fortrun.magic.utils.mqtt;

import android.content.Context;

import com.aliyun.alink.linksdk.channel.core.base.AError;
import com.aliyun.alink.linksdk.channel.core.persistent.IOnSubscribeListener;
import com.aliyun.alink.linksdk.channel.core.persistent.PersistentNet;
import com.aliyun.alink.linksdk.channel.core.persistent.event.IConnectionStateListener;
import com.aliyun.alink.linksdk.channel.core.persistent.event.IOnPushListener;
import com.aliyun.alink.linksdk.channel.core.persistent.event.PersistentEventDispatcher;
import com.aliyun.alink.linksdk.channel.core.persistent.mqtt.MqttConfigure;
import com.aliyun.alink.linksdk.channel.core.persistent.mqtt.MqttInitParams;

import org.greenrobot.eventbus.EventBus;

import cn.fortrun.magic.interfaces.IMQTTService;
import cn.fortrun.magic.model.bean.DeviceConfigBean;
import cn.fortrun.magic.utils.FileLogUtils;

/**
 * description
 *
 * @author 87627
 * @create 2018.06.17 10:27
 * @since 1.0.0
 */
public class AliIotImpl implements IMQTTService {
    private final String TAG = AliIotImpl.class.getName();

    @Override
    public void init(Context context, DeviceConfigBean config) {
//                "host":"b1CydLRBV7K.iot-as-mqtt.cn-shanghai.aliyuncs.com",
//                "port":"1883",
//                "client_id":"{zhou001}|securemode=3,signmethod=hmacsha1,timestamp=789|",
//                "username":"{zhou001}&{b1CydLRBV7K}",
//                "password":"{bdbf2c1fd7031146c72d7e6066869217d07f3041}",
        String host ="b1CydLRBV7K.iot-as-mqtt.cn-shanghai.aliyuncs.com";// config.getHost();
        String port ="1883";// config.getPort();
        String username = "zhou001&b1CydLRBV7K";//config.getUsername();
        String password = "bdbf2c1fd7031146c72d7e6066869217d07f3041";//config.getPassword();
        // 环境配置需要在SDK初始化之前。SDK 支持自定义切换Host。
        String[] ab = username.split("&");
        if (ab.length != 2)
            return;
        String productKey = ab[1];
        String deviceName = ab[0];
        String deviceSecret = password;// TODO 解密
        MqttConfigure.mqttHost = String.format("ssl://%s:%s", host, port);
        // 拿三元组信息对SDK初始化，SDK 会进行 MQTT 建联。
        MqttInitParams initParams = new MqttInitParams(productKey, deviceName, deviceSecret);
        PersistentNet.getInstance().init(context, initParams);
        // 添加通道状态变化监听
        PersistentEventDispatcher.getInstance().registerOnTunnelStateListener(connectionStateListener, false);

        String topic = "";//TODO
        subscribe(context, topic);
    }

    @Override
    public void destroy(Context context) {
        PersistentEventDispatcher.getInstance().unregisterOnTunnelStateListener(connectionStateListener);//取消连接监听
        PersistentNet.getInstance().destroy();
    }

    @Override
    public void subscribe(Context context, String topic) {
        // 订阅
        PersistentNet.getInstance().subscribe(topic, subscribeListener);
        // 订阅Topic后，云端推送的下行消息监听。
        PersistentEventDispatcher.getInstance().registerOnPushListener(onPushListener, false);
    }

    @Override
    public void unsubscribe(Context context, String topic) {
        //取消长链接push监听
        PersistentEventDispatcher.getInstance().unregisterOnPushListener(onPushListener);
        //取消订阅
        PersistentNet.getInstance().unSubscribe(topic, subscribeListener);
    }

    @Override
    public void publish(Context context, String receiver, String message) {

    }

    @Override
    public void receive(String json) {
        EventBus.getDefault().post(json);
    }


    private IConnectionStateListener connectionStateListener = new IConnectionStateListener() {
        @Override
        public void onConnectFail(String s) {
            FileLogUtils.getInstance().i("ali_iot onConnectFail", "message:" + s);
        }

        @Override
        public void onConnected() {
            FileLogUtils.getInstance().i("ali_iot onConnected", "");
        }

        @Override
        public void onDisconnect() {
            FileLogUtils.getInstance().i("ali_iot onDisconnect", "");
        }
    };


    /**
     * 订阅或取消订阅的结果回调
     */
    private IOnSubscribeListener subscribeListener = new IOnSubscribeListener() {
        @Override
        public void onSuccess(String s) {
            FileLogUtils.getInstance().i("ali_iot subscribe success", "message:" + s);
        }

        @Override
        public void onFailed(String s, AError aError) {
            FileLogUtils.getInstance().i("ali_iot subscribe failed", "code:" + aError.getCode() + " message:" + aError.getMsg());
        }

        @Override
        public boolean needUISafety() {
            return false;
        }
    };

    /**
     * 订阅之后 服务端下发的相关topic的下行数据通道
     */
    private IOnPushListener onPushListener = new IOnPushListener() {
        @Override
        public void onCommand(String topic, String sdata) {
            FileLogUtils.getInstance().i("ali_iot received message", "topic:" + topic + " message:" + sdata);
            receive(sdata);
            // 服务端下发的发布内容 客户端根据topic找到是什么订阅事件的发布
        }

        @Override
        public boolean shouldHandle(String topic) {
            // 是否要忽略某个topic事件的发布  这里设置true表示所有的topic都关系
            // 如果设置为返回false 则onCommand不会调用
            return true;
        }
    };
}
