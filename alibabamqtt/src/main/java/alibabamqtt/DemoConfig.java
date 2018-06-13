package alibabamqtt;

/**
 * date:    2018-05-22
 * description: 三元组配置文件
 */

public class DemoConfig {

    /**
     * 1.登录阿里云账号 https://account.aliyun.com/login/login.htm
     * 2.右上角点击 " 控制台 " 进入 https://home.console.aliyun.com/new#/
     * 3.点击左侧的 物联网套件 创建产品  创建设备 获得三元组；
     * 4.在该平台可以 看登录、上行、下行的日志，可以发布消息，定义topic等；
     * TODO change with your triad data
     * 注意： 三元组 需要安全存储 不要明文写在代码里
     */
    public final static String productKey = "b1y3laWmX5A"; // TODO use your productKey
    public final static String deviceName = "huawei"; // TODO use your deviceName
    public final static String deviceSecret = "OGAGsL0EbXzYAtCjsZq0Plh1XaPtGQV1"; // TODO use your deviceSecret

    private static String deviceName1 = "huawei";
    /**
     * 服务端开通套件之后 自动创建的 topic
     */
    public final static String subTopic = "/" + productKey + "/" + deviceName1 + "/get";
    public final static String pubTopic = "/" + productKey + "/" + deviceName1 + "/update";
}
