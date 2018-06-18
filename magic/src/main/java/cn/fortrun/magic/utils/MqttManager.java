package cn.fortrun.magic.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.UUID;

import cn.fortrun.magic.common.Constants;
import cn.fortrun.magic.interfaces.IMQTTService;

/**
 * description
 *
 * @author 87627
 * @create 2018.06.17 10:44
 * @since 1.0.0
 */
public class MqttManager {

    private static IMQTTService mService;
    /*================主动发送指令=======================*/
    public static final String ACTIVATION_ID_CARD = "3103";/*激活刷卡器*/
    public static final String RELEASE_CARD_READER = "3102";/*释放刷卡器*/
    /**
     * {
     * "device_id":"xxx",//设备ID
     * "hotel_id":"xxx",//酒店ID
     * "type":"30",//30魔镜，31底座
     * "status":"DISABLE",//DISABLE禁用、ENABLE启用
     * "software_version":"2.0.0" //软件版本
     * }
     */
    public static final String SEND_APP_INFO = "1010";/*回传app信息，app和吐卡器谁收到这个指令都要将各自的信息发送给对方*/

    /**
     * {
     * "business_id":"", // 业务操作ID，来自3073指令的传入值
     * "card_op_result":"1|2", // 1-吐卡，2-吞卡（含注销卡操作）
     * }
     */
    public static final String what_xxx = "3124";/**/

    /*================主动发送指令=======================*/


    /*================被动接收指令=======================*/

    /**
     * {
     * "hotel_id":"",酒店id
     * "lock_code_type":"",门锁代码类型 0未知 默认1门锁序列号 2房号
     * "lock_code":"",门锁代码
     * "business_id":""业务操作ID
     * }
     * 当lock_code_type=0时，表示底座吃到了一张无效的卡
     */
    public static final String BACK_ROOMCARD_INFO = "3073";/*返回房卡信息*/

    /**
     * {
     * name: "", //姓名,
     * nation: "", //民族,(汉族)
     * sex: "", //性别,（男性|女性）
     * idnumber: "", //二代证号码,
     * address: "", //住址,
     * url: "" //二代证图片url,
     * validity_start_date:"",//身份证有效开始日期
     * validity_end_date:"",//身份证有效结束日期(长期有效为"长期")
     * issuing_authority:"",//身份证签发机关
     * is_fake: true, // 是否虚假用户
     * }
     */
    public static final String BACK_IDCARD_INFO = "3051";/*返回身份证信息*/

    /**
     * {
     * "hotel_id": "", // 酒店id
     * "device_id": "", // 设备id
     * "service_status": "ON|OFF", // 服务状态：开|关
     * "recycle_box": "FULL|OK", // 回收槽：满|正常
     * "issue_box": "EMPTY|LACK|OK", // 发卡槽：空|缺卡|正常
     * "is_used_by_consumer": true|false, // 是否正在被用户使用
     * "has_read_card": true|false, // 是否读到了卡
     * "host_version": "", // Host版本号
     * "bottom_version": "", // Bottom版本号
     * "firmware_version": "", // 发卡机固件版本号
     * "pc_cpu": "", // CPU信息
     * "pc_hd": "", // 硬盘信息
     * "pc_os": "", // 操作系统
     * "pc_mem": "", // 内存总容量
     * "pc_lan": "", // 网卡信息
     * "pc_desk": "", // 桌面名称
     * “pc_tvid”:"", // TVID
     * “pc_time”:"", // 电脑时间
     * }
     * 【回收槽满 | 发卡槽空时，2秒上报一次】
     */
    public static final String BACK_CARD_FEEDER_INFO = "3077";/*返回发卡器的状态信息*/

    /**
     * {
     * "suborder_id":""//入住单ID
     * "identity_status:""//手动身份核验状态AGREED通过、REFUSED拒绝
     * "identity_id":""//身份核验ID
     * }
     */
    public static final String BACK_MANUAL_REVIEW_RESULTS = "3022";/*人工审核结果*/

    /**
     * {
     * get_card_result: "", //取卡结果（1-正常取走，2-超时吞卡）,
     * }
     */
    public static final String BACK_WHO_GET_ROOMCARD = "3053";/*发卡器返回取卡结果 （1-正常取走，2-超时吞卡）  */

    /**
     * {
     * "file":"http://xx.xx.cn/xxx.apk",//软件下载地址
     * "main_hint":"系统升级中",//主提示
     * "sub_hint":"预计10分钟完成，请稍后使用"//子提示
     * }
     */
    public static final String REQUEST_UPDATE_APP = "1011";/*要求升级app版本*/

    /**
     * {
     * "status":"DISABLE",//DISABLE禁用、ENABLE启用
     * "main_hint":"系统升级中",//主提示
     * "sub_hint":"预计10分钟完成，请稍后使用"//子提示
     * }
     * TODO
     */
    public static final String DISABLE_DEVICE = "1012";/*启/禁用设备*/
    /*================被动接收指令=======================*/


    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    /**
     * 获取App订阅的主题——app的deviceId
     *
     * @return
     */
    private static String getSelfTopic() {
        return Constants.getDeviceConfig().getTopics().get(0);
    }

    /**
     * 目标主题——底座的deviceId
     *
     * @return
     */
    private static String getTargetTopic() {
        return Constants.getDeviceConfig().getExts().getBottom_topic();
    }

    /**
     * 激活读卡器
     */
    public static void activation() {
//        {
//            "loop_time": "", //循环时间（单位：秒）,
//        }
        JsonObject data = new JsonObject();
        data.addProperty("loop_time", 60);
        publish(getSelfTopic(), getTargetTopic(), ACTIVATION_ID_CARD, data);
    }

    /**
     * 释放读卡器
     */
    public static void release() {
        publish(getSelfTopic(), getTargetTopic(), RELEASE_CARD_READER);
    }

    /**
     * 接收到指令，要求返回app对应的信息
     */
    public static void backAppInfo(String whoRequest, boolean enable) {
        JsonObject data = new JsonObject();
        data.addProperty("device_id", Constants.getAndroid().getDeviceId());
        data.addProperty("hotel_id", Constants.getDeviceConfig().getHotel_id());
        data.addProperty("type", 30);
        data.addProperty("status", enable ? "ENABLE" : "DISABLE");
        data.addProperty("software_version", Constants.getAndroid().getAppVersion());
        publish(getSelfTopic(), whoRequest, SEND_APP_INFO);
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
     * 发送
     *
     * @param sender   自己订阅的topic
     * @param receiver 接受者订阅的topic
     * @param cmd      指令号
     * @param data     携带的json数据
     */
    public static void publish(String sender, String receiver, String cmd, JsonObject data) {

        if (mService == null) {
            try {
                throw new Exception("请先初始化mqtt组件");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        JsonObject params = new JsonObject();
        params.addProperty("tid", UUID.randomUUID().toString().replaceAll("-", ""));// 业务唯一id
        params.addProperty("sender", sender);//发送者订阅的topic
        params.addProperty("cmd", cmd);//指令号
        params.addProperty("code", "0");//错误码
        params.addProperty("sid", "");//超级权限调用

        // 携带的json数据
        params.add("data", data);//超级权限调用
        mService.publish(mContext, receiver, new Gson().toJson(params));
    }


}
