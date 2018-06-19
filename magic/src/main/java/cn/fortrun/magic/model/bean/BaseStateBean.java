package cn.fortrun.magic.model.bean;

import java.io.Serializable;

/**
 * Created by wangting on 2017/11/24.
 * 底座状态监测
 */

public class BaseStateBean implements Serializable {

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
     */
    private String hotel_id;
    private String device_id;
    private String service_status;
    private String recycle_box;
    private String issue_box;
    private String is_used_by_consumer;
    private String has_read_card;
    private String host_version;
    private String bottom_version;
    private String firmware_version;
    private String pc_cpu;
    private String pc_hd;
    private String pc_os;
    private String pc_mem;
    private String pc_lan;
    private String pc_desk;
    private String pc_tvid;
    private String pc_time;

    public String getPc_tvid() {
        return pc_tvid;
    }

    public void setPc_tvid(String pc_tvid) {
        this.pc_tvid = pc_tvid;
    }

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

    public String getHost_version() {
        return host_version;
    }

    public void setHost_version(String host_version) {
        this.host_version = host_version;
    }

    public String getBottom_version() {
        return bottom_version;
    }

    public void setBottom_version(String bottom_version) {
        this.bottom_version = bottom_version;
    }

    public String getFirmware_version() {
        return firmware_version;
    }

    public void setFirmware_version(String firmware_version) {
        this.firmware_version = firmware_version;
    }

    public String getPc_cpu() {
        return pc_cpu;
    }

    public void setPc_cpu(String pc_cpu) {
        this.pc_cpu = pc_cpu;
    }

    public String getPc_hd() {
        return pc_hd;
    }

    public void setPc_hd(String pc_hd) {
        this.pc_hd = pc_hd;
    }

    public String getPc_os() {
        return pc_os;
    }

    public void setPc_os(String pc_os) {
        this.pc_os = pc_os;
    }

    public String getPc_mem() {
        return pc_mem;
    }

    public void setPc_mem(String pc_mem) {
        this.pc_mem = pc_mem;
    }

    public String getPc_lan() {
        return pc_lan;
    }

    public void setPc_lan(String pc_lan) {
        this.pc_lan = pc_lan;
    }

    public String getPc_desk() {
        return pc_desk;
    }

    public void setPc_desk(String pc_desk) {
        this.pc_desk = pc_desk;
    }

    public String getPc_time() {
        return pc_time;
    }

    public void setPc_time(String pc_time) {
        this.pc_time = pc_time;
    }
}
