package cn.fortrun.magic.model.bean;

import java.io.Serializable;

/**
 * Created by wangting on 2017/11/10.
 * 身份证信息
 */


public class IDCardBean implements Serializable {
    /**
     * name : 汪婷婷
     * nation : 汉
     * sex : 女
     * idnumber : 341023199509184522
     * address : 安徽省黟县美溪乡庙林村田畈组
     * url : http://idcard-1252821823.cossh.myqcloud.com/20171115/ab13ead5c1a75620310ad65fc27e5d36.png
     * is_fake : false
     * validity_start_date : 20160608
     * validity_end_date : 20260608
     * issuing_authority : 黟县公安局
     */

    private String name;
    private String nation;
    private String sex;
    private String idnumber;
    private String address;
    private String url;
    private boolean is_fake;
    private String validity_start_date;
    private String validity_end_date;
    private String issuing_authority;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isIs_fake() {
        return is_fake;
    }

    public void setIs_fake(boolean is_fake) {
        this.is_fake = is_fake;
    }

    public String getValidity_start_date() {
        return validity_start_date;
    }

    public void setValidity_start_date(String validity_start_date) {
        this.validity_start_date = validity_start_date;
    }

    public String getValidity_end_date() {
        return validity_end_date;
    }

    public void setValidity_end_date(String validity_end_date) {
        this.validity_end_date = validity_end_date;
    }

    public String getIssuing_authority() {
        return issuing_authority;
    }

    public void setIssuing_authority(String issuing_authority) {
        this.issuing_authority = issuing_authority;
    }

}
