package cn.fortrun.magic.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.greenrobot.eventbus.EventBus;

import cn.fortrun.magic.common.Constants;
import cn.fortrun.magic.interfaces.IMQTTService;
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

    /**
     * 初始化
     *
     * @param context
     */
    @Override
    public void init(Context context) {
        YunBaManager.start(context, Constants.YUNBA_KEY);
        registerReceiver(context);
    }

    /**
     * 销毁资源
     *
     * @param context
     */
    @Override
    public void destroy(Context context) {
        Log.e(TAG, "取消广播接收");
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
                Log.e(TAG, String.format("订阅topic[%s]成功", topic));
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.e(TAG, String.format("订阅topic[%s]失败", topic));
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
    public void unsubscribe(Context context, String topic) {
        IMqttActionListener iMqttActionListener = new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.e(TAG, "取消订阅成功");
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                if (exception instanceof MqttException) {
                    MqttException ex = (MqttException) exception;
                    String msg = "publish failed with error code : " + ex.getReasonCode();

                    Log.e(TAG, "取消订阅失败");
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
    public void publish(Context context, String receiver, String message) {

        IMqttActionListener iMqttActionListener = new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.e(TAG, "消息发送成功");
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                if (exception instanceof MqttException) {
                    MqttException ex = (MqttException) exception;
                    String msg = "publish failed with error code : " + ex.getReasonCode();

                    Log.e(TAG, "消息发送失败");
                }
            }
        };
        YunBaManager.publish(context, receiver, message, iMqttActionListener);

    }

    /**
     * 云吧接收消息
     */
    public static class YunBaReceiver extends BroadcastReceiver {
        private String TAG = YunBaReceiver.class.getName();


        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(YunBaManager.MESSAGE_RECEIVED_ACTION)) {
                // 消息抵达
                String topic = intent.getStringExtra(YunBaManager.MQTT_TOPIC);
                String message = intent.getStringExtra(YunBaManager.MQTT_MSG);

                JsonObject jsonObject = new JsonParser().parse(message.toString()).getAsJsonObject();
                String tid = jsonObject.get("tid").getAsString();
                if (TidCache.isExists(tid)) {
                    return;
                }
                // TODO 何时清除
                TidCache.add(tid);
                EventBus.getDefault().post(message);

            } else if (action.equals(YunBaManager.MESSAGE_CONNECTED_ACTION)) {
                Log.e(TAG, "[YunBa] 已连接");
            } else if (action.equals(YunBaManager.MESSAGE_DISCONNECTED_ACTION)) {
                // TODO 需要显示一个界面吗
                Toast.makeText(context, "云吧断开连接", Toast.LENGTH_SHORT).show();
            } else if (action.equals(YunBaManager.PRESENCE_RECEIVED_ACTION)) {
//
//                String topic = intent.getStringExtra(YunBaManager.MQTT_TOPIC);
//                String msg = intent.getStringExtra(YunBaManager.MQTT_MSG);
//                StringBuilder showMsg = new StringBuilder();
//                showMsg.append("[Message from prensence] ").append(YunBaManager.MQTT_TOPIC)
//                        .append(" = ").append(topic).append(" ,")
//                        .append(YunBaManager.MQTT_MSG).append(" = ").append(msg);
//                Log.e(TAG, showMsg.toString());

            }


        }
    }
}
