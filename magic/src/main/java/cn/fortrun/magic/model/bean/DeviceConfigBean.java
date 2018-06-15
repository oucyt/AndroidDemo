package cn.fortrun.magic.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wangting on 2017/11/
 * modified 2018年06月12日
 * 设备配置信息
 * 包含旅业配置、酒店配置、app配置
 */

public class DeviceConfigBean implements Serializable {

    /**
     * device_name : null
     * hotel_id : 0864f6731acb11e780ad5cb9018d9b5c
     * hotel_reception_area_id : null
     * host : test-front.yunba.io
     * port : 8883
     * client_id : 0000004906-000000041033
     * username : 3225554332185189248
     * password : 9e46ddc3a1e02
     * topics : ["devices/80ce57c15d607d563241a083b8edbbfd"]
     * expires : 1559026254000
     * device_type : 32
     * exts : {"bottom_topic":"devices/8669c4fcc26c4f4d8b7b30cbcf432fd5"}
     * lvye_configs : []
     * hotel_config : {"support_room_card":true,"product_version":"master","integration_room_lock":true,"enabled_mirror_introduce":true,"pms_integration":true,"facein_pass_value":80,"customer_service_tel":"13917456925","rc_status":true,"advanced_checkout":true,"facein_reject_value":50,"enabled_mirror_brand":false,"enabled_same_date_io":true,"enabled_identity_check":true,"enabled_auto_give_room":true,"enabled_mobile_checkin":true,"enabled_mirror_show_device_name":false,"support_face_in":true,"enabled_pms_in_guest_checkout":true,"electron_sign":1}
     * system_config : {"product_version_address_map":"{\"master\":\"123.206.99.219\",\"v2.5.0\":\"10.105.43.166\"}"}
     */

    private String device_name;
    private String hotel_id;
    private Object hotel_reception_area_id;
    private String host;
    private String port;
    private String client_id;
    private String username;
    private String password;
    private long expires;
    private String device_type;
    private ExtsBean exts;
    private HotelConfigBean hotel_config;
    private SystemConfigBean system_config;
    private List<String> topics;
    private List<?> lvye_configs;

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(String hotel_id) {
        this.hotel_id = hotel_id;
    }

    public Object getHotel_reception_area_id() {
        return hotel_reception_area_id;
    }

    public void setHotel_reception_area_id(Object hotel_reception_area_id) {
        this.hotel_reception_area_id = hotel_reception_area_id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public ExtsBean getExts() {
        return exts;
    }

    public void setExts(ExtsBean exts) {
        this.exts = exts;
    }

    public HotelConfigBean getHotel_config() {
        return hotel_config;
    }

    public void setHotel_config(HotelConfigBean hotel_config) {
        this.hotel_config = hotel_config;
    }

    public SystemConfigBean getSystem_config() {
        return system_config;
    }

    public void setSystem_config(SystemConfigBean system_config) {
        this.system_config = system_config;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public List<?> getLvye_configs() {
        return lvye_configs;
    }

    public void setLvye_configs(List<?> lvye_configs) {
        this.lvye_configs = lvye_configs;
    }

    public static class ExtsBean {
        /**
         * bottom_topic : devices/8669c4fcc26c4f4d8b7b30cbcf432fd5
         */

        private String bottom_topic;

        public String getBottom_topic() {
            return bottom_topic;
        }

        public void setBottom_topic(String bottom_topic) {
            this.bottom_topic = bottom_topic;
        }
    }

    public static class HotelConfigBean {
        /**
         * support_room_card : true
         * product_version : master
         * integration_room_lock : true
         * enabled_mirror_introduce : true
         * pms_integration : true
         * facein_pass_value : 80
         * customer_service_tel : 13917456925
         * rc_status : true
         * advanced_checkout : true
         * facein_reject_value : 50
         * enabled_mirror_brand : false
         * enabled_same_date_io : true
         * enabled_identity_check : true
         * enabled_auto_give_room : true
         * enabled_mobile_checkin : true
         * enabled_mirror_show_device_name : false
         * support_face_in : true
         * enabled_pms_in_guest_checkout : true
         * electron_sign : 1
         */

        private boolean support_room_card;
        private String product_version;
        private boolean integration_room_lock;
        private boolean enabled_mirror_introduce;
        private boolean pms_integration;
        private int facein_pass_value;
        private String customer_service_tel;
        private boolean rc_status;
        private boolean advanced_checkout;
        private int facein_reject_value;
        private boolean enabled_mirror_brand;
        private boolean enabled_same_date_io;
        private boolean enabled_identity_check;
        private boolean enabled_auto_give_room;
        private boolean enabled_mobile_checkin;
        private boolean enabled_mirror_show_device_name;
        private boolean support_face_in;
        private boolean enabled_pms_in_guest_checkout;
        private int electron_sign;

        public boolean isSupport_room_card() {
            return support_room_card;
        }

        public void setSupport_room_card(boolean support_room_card) {
            this.support_room_card = support_room_card;
        }

        public String getProduct_version() {
            return product_version;
        }

        public void setProduct_version(String product_version) {
            this.product_version = product_version;
        }

        public boolean isIntegration_room_lock() {
            return integration_room_lock;
        }

        public void setIntegration_room_lock(boolean integration_room_lock) {
            this.integration_room_lock = integration_room_lock;
        }

        public boolean isEnabled_mirror_introduce() {
            return enabled_mirror_introduce;
        }

        public void setEnabled_mirror_introduce(boolean enabled_mirror_introduce) {
            this.enabled_mirror_introduce = enabled_mirror_introduce;
        }

        public boolean isPms_integration() {
            return pms_integration;
        }

        public void setPms_integration(boolean pms_integration) {
            this.pms_integration = pms_integration;
        }

        public int getFacein_pass_value() {
            return facein_pass_value;
        }

        public void setFacein_pass_value(int facein_pass_value) {
            this.facein_pass_value = facein_pass_value;
        }

        public String getCustomer_service_tel() {
            return customer_service_tel;
        }

        public void setCustomer_service_tel(String customer_service_tel) {
            this.customer_service_tel = customer_service_tel;
        }

        public boolean isRc_status() {
            return rc_status;
        }

        public void setRc_status(boolean rc_status) {
            this.rc_status = rc_status;
        }

        public boolean isAdvanced_checkout() {
            return advanced_checkout;
        }

        public void setAdvanced_checkout(boolean advanced_checkout) {
            this.advanced_checkout = advanced_checkout;
        }

        public int getFacein_reject_value() {
            return facein_reject_value;
        }

        public void setFacein_reject_value(int facein_reject_value) {
            this.facein_reject_value = facein_reject_value;
        }

        public boolean isEnabled_mirror_brand() {
            return enabled_mirror_brand;
        }

        public void setEnabled_mirror_brand(boolean enabled_mirror_brand) {
            this.enabled_mirror_brand = enabled_mirror_brand;
        }

        public boolean isEnabled_same_date_io() {
            return enabled_same_date_io;
        }

        public void setEnabled_same_date_io(boolean enabled_same_date_io) {
            this.enabled_same_date_io = enabled_same_date_io;
        }

        public boolean isEnabled_identity_check() {
            return enabled_identity_check;
        }

        public void setEnabled_identity_check(boolean enabled_identity_check) {
            this.enabled_identity_check = enabled_identity_check;
        }

        public boolean isEnabled_auto_give_room() {
            return enabled_auto_give_room;
        }

        public void setEnabled_auto_give_room(boolean enabled_auto_give_room) {
            this.enabled_auto_give_room = enabled_auto_give_room;
        }

        public boolean isEnabled_mobile_checkin() {
            return enabled_mobile_checkin;
        }

        public void setEnabled_mobile_checkin(boolean enabled_mobile_checkin) {
            this.enabled_mobile_checkin = enabled_mobile_checkin;
        }

        public boolean isEnabled_mirror_show_device_name() {
            return enabled_mirror_show_device_name;
        }

        public void setEnabled_mirror_show_device_name(boolean enabled_mirror_show_device_name) {
            this.enabled_mirror_show_device_name = enabled_mirror_show_device_name;
        }

        public boolean isSupport_face_in() {
            return support_face_in;
        }

        public void setSupport_face_in(boolean support_face_in) {
            this.support_face_in = support_face_in;
        }

        public boolean isEnabled_pms_in_guest_checkout() {
            return enabled_pms_in_guest_checkout;
        }

        public void setEnabled_pms_in_guest_checkout(boolean enabled_pms_in_guest_checkout) {
            this.enabled_pms_in_guest_checkout = enabled_pms_in_guest_checkout;
        }

        public int getElectron_sign() {
            return electron_sign;
        }

        public void setElectron_sign(int electron_sign) {
            this.electron_sign = electron_sign;
        }
    }

    public static class SystemConfigBean {
        /**
         * product_version_address_map : {"master":"123.206.99.219","v2.5.0":"10.105.43.166"}
         */

        private String product_version_address_map;

        public String getProduct_version_address_map() {
            return product_version_address_map;
        }

        public void setProduct_version_address_map(String product_version_address_map) {
            this.product_version_address_map = product_version_address_map;
        }
    }
}
