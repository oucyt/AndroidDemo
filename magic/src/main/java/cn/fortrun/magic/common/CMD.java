package cn.fortrun.magic.common;

/**
 * description
 * 底座指令集
 * 包含控制指令和返回码
 * https://shimo.im/doc/24M0tSxnwmscr4Sr
 *
 * @author 87627
 * @create 2018.05.11 11:43
 * @since 1.0.0
 */
public class CMD {
    /**
     * APP请求指令集
     */
    public enum Send {
        READ_ID_CARD("3103"),/*请求读取身份证*/
        RELEASE_BASE("3102")/*请求释放底座*/;

        private String code;

        Send(String code) {
            this.code = code;
        }

        public String code() {
            return code;
        }

    }

    /**
     * 底座回传指令集
     */
    public enum Received {
        RECYCLE_ROOM_CARD("3703"),/*回收房卡时的回传信息*/
        GET_ID_CARD("3051"),/*读卡器回传的身份证信息*/
        BASE_STATUS("3077"),/*返回底座的状态*/
        MANUAL_REVIEW_RESULTS("3022"),/*人工审核结果*/
        WHO_GET_CARD("3053")/*PUSH取卡结果 （1-正常取走，2-超时吞卡）  */;

        private String code;

        Received(String code) {
            this.code = code;
        }

        public String code() {
            return code;
        }
    }

    public enum Common {
        QUERY_DEVICE("1010"),/*查询设备信息*/
        AUTO_UPGRADE("1011"),/*自动升级指令*/
        DISABLE_DEVICE("1012")/*启/禁用设备*/;

        private String code;

        Common(String code) {
            this.code = code;
        }

        public String code() {
            return code;
        }
    }


}
