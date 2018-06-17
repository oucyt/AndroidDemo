package cn.fortrun.magic.interfaces;

import android.content.Context;

import cn.fortrun.mirror.model.entity.DeviceConfig;

/**
 * description
 * MQTT服务接口
 *
 * @author tianyu
 * @create 2018.06.15 下午3:02
 * @since 1.0.0
 */
public interface IMQTTService {
    /**
     * 初始化
     */
    void init(Context context);

    /**
     * 订阅主题
     */
    void subscribe(Context context, DeviceConfig config);

    /**
     * 取消订阅
     *
     * @param context
     * @param config
     */
    void unsubscribe(Context context, DeviceConfig config);

    /**
     * 发送指令
     *
     * @param cmd
     * @param obj
     * @param sender
     */
    void publish(Context context, String cmd, Object obj, String sender);
}
