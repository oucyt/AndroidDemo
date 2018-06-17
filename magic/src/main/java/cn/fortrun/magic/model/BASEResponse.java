package cn.fortrun.magic.model;

import java.io.Serializable;

/**
 * Created by wangting on 2017/11/20.
 * mqtt返回的基本数据格式
 */

public class BASEResponse<T> implements Serializable {

    /**
     * code : 0
     * data : {}
     * sender : /devices/server1
     * cmd : 3021
     * tid : 8cb6cd7c50b046488f5d8e99aa931975
     * sid : 70f72fa4f6304b699d6c168d3b4c46a9
     */
    private String code;
    private String sender;
    private String cmd;
    private String tid;
    private String sid;
    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
