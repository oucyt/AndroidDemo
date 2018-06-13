package com.example.audio;

import java.io.Serializable;

/**
 * Created by wangting on 2017/11/24.
 * 底座状态监测
 */

public class BaseState implements Serializable {

    /**
     * hotel_id :
     * device_id :
     * device_name :
     * service_status : ON|OFF
     * recycle_box : FULL|OK
     * issue_box : EMPTY|LACK|OK
     * is_used_by_consumer : true|false
     * has_read_card : true|false
     */

    private String hotel_id;
    private String device_id;
    private String device_name;
    private String service_status;
    private String recycle_box;
    private String issue_box;
    private String is_used_by_consumer;
    private String has_read_card;

    public String getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(String hotel_id) {
        this.hotel_id = hotel_id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getService_status() {
        return service_status;
    }

    public void setService_status(String service_status) {
        this.service_status = service_status;
    }

    public String getRecycle_box() {
        return recycle_box;
    }

    public void setRecycle_box(String recycle_box) {
        this.recycle_box = recycle_box;
    }

    public String getIssue_box() {
        return issue_box;
    }

    public void setIssue_box(String issue_box) {
        this.issue_box = issue_box;
    }

    public String getIs_used_by_consumer() {
        return is_used_by_consumer;
    }

    public void setIs_used_by_consumer(String is_used_by_consumer) {
        this.is_used_by_consumer = is_used_by_consumer;
    }

    public String getHas_read_card() {
        return has_read_card;
    }

    public void setHas_read_card(String has_read_card) {
        this.has_read_card = has_read_card;
    }
}
