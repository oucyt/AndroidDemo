package cn.fortrun.magic.interfaces;

import android.content.Context;

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
     * 销毁资源
     */
    void destroy(Context context);

    /**
     * 订阅主题
     */
    void subscribe(Context context, String topic);

    /**
     * 取消订阅
     *
     * @param context
     */
    void unsubscribe(Context context, String topic);

    /**
     * @param context
     * @param receiver
     * @param message
     */
    void publish(Context context, String receiver, String message);
}
