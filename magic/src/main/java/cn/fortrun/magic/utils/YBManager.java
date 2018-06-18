package cn.fortrun.magic.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.greenrobot.eventbus.EventBus;

import java.util.UUID;

import cn.fortrun.mirror.MyApp;
import cn.fortrun.mirror.model.entity.CardDate;
import cn.fortrun.mirror.model.entity.Config;
import io.yunba.android.manager.YunBaManager;

/**
 * description
 * 云吧服务工具类
 *
 * @author tianyu
 * @create 2018.06.05 下午2:21
 * @since 1.0.0
 */
public class YBManager {
    private static String TAG = YBManager.class.getName();

    private static Context mContext;
    private static Config mConfig;


    /**
     * 云吧服务初始化
     *
     * @param context
     */
    public static void init(Context context) {
        mContext = context;
        YunBaManager.setThirdPartyEnable(context, true);
        YunBaManager.start(context, "581202f86cf991dc38fd2078");
    }

    /**
     * 订阅云吧主题
     *
     * @param context
     */
    public static void register(Context context, Config config) {

        mConfig = config;
        String topic = config.getTopics().get(0);
        IMqttActionListener listener = new IMqttActionListener() {

            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.e(TAG, String.format("订阅topic[%s]成功", topic));
//                 StringBuilder showMsg = new StringBuilder();
//                showMsg.append("subscribe succ：").append(io.yunba.android.manager.YunBaManager.MQTT_TOPIC)
//                        .append(" = ").append("");
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.e(TAG, String.format("订阅topic[%s]失败", topic));
            }
        };

        YunBaManager.subscribe(context, topic, listener);
    }

    /**
     * 订阅云吧主题
     *
     * @param context
     * @param topic
     */
    public static void register(Context context, String[] topic) {

        IMqttActionListener listener = new IMqttActionListener() {

            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
//                Log.e(TAG, String.format("订阅topic[%s]成功", topic));
//                 StringBuilder showMsg = new StringBuilder();
//                showMsg.append("subscribe succ：").append(io.yunba.android.manager.YunBaManager.MQTT_TOPIC)
//                        .append(" = ").append("");
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
//                Log.e(TAG, String.format("订阅topic[%s]失败", topic));
            }
        };

        YunBaManager.subscribe(context, topic, listener);
    }


    /**
     * 发送
     *
     * @param sender   自己订阅的topic
     * @param receiver 接受者订阅的topic
     * @param cmd      指令号
     */
    public static void publish(String sender, String receiver, String cmd) {
        publish(sender, receiver, cmd, new JsonObject());
    }

    /**
     * @param sender   自己订阅的topic
     * @param receiver 接受者订阅的topic
     * @param cmd      指令号
     * @param data     携带的json数据
     */
    public static void publish(String sender, String receiver, String cmd, JsonObject data) {
        JsonObject params = new JsonObject();
        params.addProperty("tid", UUID.randomUUID().toString().replaceAll("-", ""));// 业务唯一id
        params.addProperty("sender", sender);//发送者订阅的topic
        params.addProperty("cmd", cmd);//指令号
        params.addProperty("code", "0");//错误码
        params.addProperty("sid", "");//超级权限调用

        // 携带的json数据
        params.add("data", data);//超级权限调用
    }


    public static void publish(String cmd, Object obj, String sender) {

//        FileLogUtils.getInstance().i("发送指令 " + cmd, new Gson().toJson(obj));
        JsonObject params = new JsonObject();
        JsonObject data = new JsonObject();
        /**
         * 3124 识别卡结果
         *{
         *"business_id":"", // 业务操作ID，来自3073指令的传入值
         *"card_op_result":"1|2", // 1-吐卡，2-吞卡（含注销卡操作）
         *}
         */
        /**
         * 1010 查询设备信息
         *{
         * "device_id":"xxx",//设备ID
         * "hotel_id":"xxx",//酒店ID
         * "type":"30",//30魔镜，31底座
         * "status":"DISABLE",//DISABLE禁用、ENABLE启用
         * "software_version":"2.0.0" //软件版本
         * }
         */
        if (cmd.equals("3124") && obj != null) {
            data.addProperty("business_id", ((CardDate) obj).getBusiness_id());
            data.addProperty("card_op_result", ((CardDate) obj).getCard_op_result());
        } else if ((cmd.equals("1010"))) {
            data.addProperty("device_id", MyApp.deviceId);
            data.addProperty("hotel_id", mConfig.getHotel_id());
            data.addProperty("type", 30);
            data.addProperty("status", (String) obj);
            data.addProperty("software_version", MyApp.version);
        } else {
            data.addProperty("loop_time", "60");
        }
        params.add("data", data);

        Integer qos = 0;
        Boolean retained = false;


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
        Log.e(TAG, "发送指令");
        FileLogUtils.getInstance().i("发送指令", new Gson().toJson(params));
        if (cmd.equals("1010")) {
            YunBaManager.publish(mContext, sender, new Gson().toJson(params), iMqttActionListener);
        } else {
            YunBaManager.publish(mContext, mConfig.getExts().getBottom_topic()/*底座订阅的topic*/, new Gson().toJson(params), iMqttActionListener);
        }


    }

    /**
     * 取消订阅
     */
    public static void unsubscribe(Context context, String topic) {
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


        YunBaManager.unsubscribe(context, mConfig.getTopics().get(0), iMqttActionListener);
    }


    public static class MyReceiver extends BroadcastReceiver {
        private String TAG = cn.fortrun.mirror.service.MyReceiver.class.getName();


        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if (action.equals(YunBaManager.MESSAGE_RECEIVED_ACTION)) {
                // 消息抵达
                String topic = intent.getStringExtra(YunBaManager.MQTT_TOPIC);
                String message = intent.getStringExtra(YunBaManager.MQTT_MSG);


                Log.e(TAG, message);
                FileLogUtils.getInstance().i("云吧推送", message);
//                JsonObject jsonObject = new JsonParser().parse(message.toString()).getAsJsonObject();
//                String tid = jsonObject.get("tid").getAsString();
//                if (TidCache.isExists(tid)) {
//                    return;
//                }
//                TidCache.add(tid);
                EventBus.getDefault().post(message);
//                try {
//                    String str1 = new String(message.getPayload(), "utf-8");
//                    String str2 = topic + ";qos:" + message.getQos() + ";retained:" + message.isRetained();
//                    FileLogUtils.getInstance().i("MQTT回传", str1);
////                FileLogUtils.getInstance().i("MQTT回传", str2);
//                    Log.e("wdt", "messageArrived:" + str1);
//                    Log.e("wdt", str2);
//                } catch (Exception e) {
//                    FileLogUtils.getInstance().e("MQTT回传数据转换异常", "");
//                }

            } else if (action.equals(YunBaManager.MESSAGE_CONNECTED_ACTION)) {

                Log.e(TAG, "[YunBa] 已连接");
            } else if (action.equals(YunBaManager.MESSAGE_DISCONNECTED_ACTION)) {
                Toast.makeText(context, "云吧断开连接", Toast.LENGTH_SHORT).show();

            } else if (action.equals(YunBaManager.PRESENCE_RECEIVED_ACTION)) {

                String topic = intent.getStringExtra(YunBaManager.MQTT_TOPIC);
                String msg = intent.getStringExtra(YunBaManager.MQTT_MSG);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append("[Message from prensence] ").append(YunBaManager.MQTT_TOPIC)
                        .append(" = ").append(topic).append(" ,")
                        .append(YunBaManager.MQTT_MSG).append(" = ").append(msg);
                Log.e(TAG, showMsg.toString());

            }


        }
    }

}
