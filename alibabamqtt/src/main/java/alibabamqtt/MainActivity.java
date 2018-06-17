package alibabamqtt;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.aliyun.alink.linksdk.channel.core.base.AError;
import com.aliyun.alink.linksdk.channel.core.base.ARequest;
import com.aliyun.alink.linksdk.channel.core.base.AResponse;
import com.aliyun.alink.linksdk.channel.core.base.IOnCallListener;
import com.aliyun.alink.linksdk.channel.core.persistent.IOnSubscribeListener;
import com.aliyun.alink.linksdk.channel.core.persistent.PersistentNet;
import com.aliyun.alink.linksdk.channel.core.persistent.event.IOnPushListener;
import com.aliyun.alink.linksdk.channel.core.persistent.event.PersistentEventDispatcher;
import com.aliyun.alink.linksdk.channel.core.persistent.mqtt.request.MqttPublishRequest;
import com.aliyun.alink.linksdk.tools.ALog;

import org.json.JSONException;
import org.json.JSONObject;

import cn.fortrun.alibabamqtt.R;

/**
 * TODO 在 DemoConfig 文件更新 productKey、deviceName、productSecret 三元组信息，即可运行本demo
 * 具体三元组创建方式 可参考 doc目录下的说明文档
 */
public class MainActivity extends Activity {

    private static final String TAG = "Mqtt_MainActivity";

    private String consoleStr = null;
    private String content = "{\"id\":\"102\"}";

    private TextView consoleTV = null;
    private EditText mEditText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        consoleTV = findViewById(R.id.textview_console);
        mEditText = findViewById(R.id.et_content);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //取消长链接push监听
        PersistentEventDispatcher.getInstance().unregisterOnPushListener(onPushListener);
        //取消订阅
        PersistentNet.getInstance().unSubscribe(DemoConfig.subTopic, subscribeListener);
    }

    /**
     * @param view 上行 发布 请求
     */
    public void publish(View view) throws JSONException {
        log("发布 " + DemoConfig.pubTopic);
        //Publish 请求
        MqttPublishRequest publishRequest = new MqttPublishRequest();
        // 设置要发布到的topic
        publishRequest.topic = DemoConfig.pubTopic;
        // 设置要发布的内容
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name","田宇");
        jsonObject.put("sex","男");
        publishRequest.payloadObj = jsonObject.toString();
        // 调用长连接发送请求，设置回调
        PersistentNet.getInstance().asyncSend(publishRequest, new IOnCallListener() {
            @Override
            public void onSuccess(ARequest request, AResponse response) {
                // 发布成功
                log("发布成功 " + DemoConfig.pubTopic);
            }

            @Override
            public void onFailed(ARequest request, AError error) {
                // 发布失败
                log("发布失败  " + DemoConfig.pubTopic);
            }

            @Override
            public boolean needUISafety() {
                return false; // 是否需要在UI线程返回
            }
        });
    }

    private String getContent(){
        return mEditText.getText().toString();
    }
    /**
     * @param view 订阅
     */
    public void subscribe(View view) {


        log("订阅 " + DemoConfig.subTopic);
        // 订阅
        PersistentNet.getInstance().subscribe(DemoConfig.subTopic, subscribeListener);
        // 订阅Topic后，云端推送的下行消息监听。
        PersistentEventDispatcher.getInstance().registerOnPushListener(onPushListener, false);
    }

    /**
     * @param view 取消订阅
     */
    public void cancelSubscribe(View view) {
        log("取消订阅 " + DemoConfig.subTopic);
        //取消订阅
        PersistentNet.getInstance().unSubscribe(DemoConfig.subTopic, subscribeListener);
    }

    /**
     * @param view 清除界面相关的日志
     */
    public void clearLog(View view) {
        log("clearLog ");
        clearMsg();
    }

    /**
     * 订阅或取消订阅的结果回调
     */
    private IOnSubscribeListener subscribeListener = new IOnSubscribeListener() {
        @Override
        public void onSuccess(String s) {
            // 订阅或取消订阅成功
            log("订阅或取消订阅成功 " + s);
        }

        @Override
        public void onFailed(String s, AError aError) {
            // 订阅或取消订阅失败
            String sss = String.format("订阅或取消订阅失败 code:%d msg:%s domain:%s",aError.getCode(),aError.getMsg(),aError.getDomain());
            log(sss);
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
            // 服务端下发的发布内容 客户端根据topic找到是什么订阅事件的发布
            log("下发 , onCommand topic=" + topic + ",data=" + sdata);
        }

        @Override
        public boolean shouldHandle(String topic) {
            // 是否要忽略某个topic事件的发布  这里设置true表示所有的topic都关系
            // 如果设置为返回false 则onCommand不会调用
            return true;
        }
    };

    /**
     * @param str 输出日志 并在界面显示
     */
    private void log(final String str) {
        ALog.d(TAG, "log(), " + str);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(str))
                    return;
                consoleStr = consoleStr + "\n \n" + str;
                consoleTV.setText(consoleStr);
            }
        });
    }

    private void clearMsg() {
        consoleStr = "";
        consoleTV.setText(consoleStr);
    }
}
