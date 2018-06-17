package cn.fortrun.magic.model;

import java.io.Serializable;

/**
 * Created by wangting on 2017/11/10.
 * 后台返回的基本数据格式
 */

public class JSONResponse<T> implements Serializable {
    private String errcode;
    private String errmsg;
    private T data;

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
