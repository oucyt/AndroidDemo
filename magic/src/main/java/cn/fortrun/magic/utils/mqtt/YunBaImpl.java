package cn.fortrun.magic.utils.mqtt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.fortrun.magic.common.Constants;
import cn.fortrun.magic.interfaces.IMQTTService;
import cn.fortrun.magic.model.bean.DeviceConfigBean;
import cn.fortrun.magic.utils.FileLogUtils;
import io.yunba.android.manager.YunBaManager;

/**
 * description
 * 云吧消息推送
 *
 * @author 87627
 * @create 2018.06.17 10:26
 * @since 1.0.0
 */
public class YunBaImpl implements IMQTTService {

    private String TAG = YunBaImpl.class.getName();
    private YunBaReceiver mYunBaReceiver;
    private String _topic;

    /**
     * 初始化
     *
     * @param context
     * @param config
     */
    @Override
    public void init(Context context, DeviceConfigBean config) {
        YunBaManager.start(context, Constants.YUNBA_KEY);
        registerReceiver(context);
        List<String> list = config.getTopics();
        if (list != null && !list.isEmpty()) {
            _topic = list.get(0);
            subscribe(context, _topic);
        }
    }

    /**
     * 销毁资源
     *
     * @param context
     */
    @Override
    public void destroy(Context context) {
        Log.e(TAG, "取消广播接收");
        unsubscribe(context, _topic);
        context.unregisterReceiver(mYunBaReceiver);
    }


    private void registerReceiver(Context context) {
        String packageName = context.getPackageName();
        Log.e(TAG, "注册广播 packageName:" + packageName);
        mYunBaReceiver = new YunBaReceiver();
        IntentFilter message = new IntentFilter();
        message.addAction(YunBaManager.MESSAGE_RECEIVED_ACTION);
        message.addCategory(packageName);
        context.registerReceiver(mYunBaReceiver, message);

        IntentFilter connection = new IntentFilter();
        connection.addAction(YunBaManager.MESSAGE_CONNECTED_ACTION);
        connection.addCategory(packageName);
        context.registerReceiver(mYunBaReceiver, connection);

        IntentFilter disconnection = new IntentFilter();
        disconnection.addAction(YunBaManager.MESSAGE_DISCONNECTED_ACTION);
        disconnection.addCategory(packageName);
        context.registerReceiver(mYunBaReceiver, disconnection);

        IntentFilter pres = new IntentFilter();
        pres.addAction(YunBaManager.PRESENCE_RECEIVED_ACTION);
        pres.addCategory(packageName);
        context.registerReceiver(mYunBaReceiver, pres);
    }

    /**
     * 订阅主题
     *
     * @param context
     * @param topic
     */
    @Override
    public void subscribe(Context context, final String topic) {

        IMqttActionListener listener = new IMqttActionListener() {

            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                FileLogUtils.getInstance().i("yunba subscribe success", "topic:" + topic);
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                FileLogUtils.getInstance().i("yunba subscribe failed", "topic:" + topic + " msg:" + exception.getMessage());
            }
        };
        YunBaManager.subscribe(context, topic, listener);
    }

    /**
     * 取消订阅
     *
     * @param context
     * @param topic
     */
    @Override
    public void unsubscribe(Context context, final String topic) {
        IMqttActionListener iMqttActionListener = new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                FileLogUtils.getInstance().i("yunba unsubscribe success", "topic:" + topic);

            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                if (exception instanceof MqttException) {
                    MqttException ex = (MqttException) exception;
                    FileLogUtils.getInstance().i("yunba unsubscribe failed", "topic:" + topic + " error:" + ex.getReasonCode() + " msg:" + ex.getMessage());
                }
            }
        };
        YunBaManager.unsubscribe(context, topic, iMqttActionListener);

    }

    /**
     * 发送消息
     *
     * @param context
     * @param receiver
     * @param message
     */
    @Override
    public void publish(Context context, final String receiver, final String message) {

        IMqttActionListener iMqttActionListener = new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                FileLogUtils.getInstance().i("yunba publish success", "receiver:" + receiver + " message:" + message);
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                if (exception instanceof MqttException) {
                    MqttException ex = (MqttException) exception;
                    FileLogUtils.getInstance().i("yunba publish failed", "receiver:" + receiver + " message:" + message + " error:" + ex.getReasonCode());
                }
            }
        };
        YunBaManager.publish(context, receiver, message, iMqttActionListener);
    }

    @Override
    public void receive(String json) {

    }

    /**
     * 云吧接收消息
     */
    public static class YunBaReceiver extends BroadcastReceiver {
        private String TAG = YunBaReceiver.class.getName();


        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction() == null ? "" : intent.getAction();

            switch (action) {
                case YunBaManager.MESSAGE_RECEIVED_ACTION:
                    // 消息抵达
                    String topic = intent.getStringExtra(YunBaManager.MQTT_TOPIC);
                    String message = intent.getStringExtra(YunBaManager.MQTT_MSG);

                    JsonObject jsonObject = new JsonParser().parse(message.toString()).getAsJsonObject();
                    String tid = jsonObject.get("tid").getAsString();
//                if (TidCache.isExists(tid)) {
//                    return;
//                }
//                // TODO 何时清除
//                TidCache.add(tid);
                    EventBus.getDefault().post(message);

                    break;
                case YunBaManager.MESSAGE_CONNECTED_ACTION:
                    FileLogUtils.getInstance().i("yunba connection", "success");
                    break;
                case YunBaManager.MESSAGE_DISCONNECTED_ACTION:
                    // TODO 需要显示一个界面吗
                    FileLogUtils.getInstance().i("yunba disconnection", "success");
                    break;
                case YunBaManager.PRESENCE_RECEIVED_ACTION:
//
//                String topic = intent.getStringExtra(YunBaManager.MQTT_TOPIC);
//                String msg = intent.getStringExtra(YunBaManager.MQTT_MSG);
//                StringBuilder showMsg = new StringBuilder();
//                showMsg.append("[Message from prensence] ").append(YunBaManager.MQTT_TOPIC)
//                        .append(" = ").append(topic).append(" ,")
//                        .append(YunBaManager.MQTT_MSG).append(" = ").append(msg);
//                Log.e(TAG, showMsg.toString());

                    break;
            }


        }
    }
}
