package cn.fortrun.magic.model.bean;

import java.io.Serializable;

/**
 * Created by wangting on 2017/12/9.
 * 底座返回的卡信息
 */

public class RoomCardBean implements Serializable {
    /**
     * hotel_id : 0864f6731acb11e780ad5cb9018d9b5c
     * lock_code_type : 0
     * lock_code :
     * business_id : 9bdd5a37841a45e1b0c4403353ba22f3
     */
    private String hotel_id;
    private String lock_code_type;
    private String lock_code;
    private String business_id;
    private String card_op_result;

    public String getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(String hotel_id) {
        this.hotel_id = hotel_id;
    }

    public String getLock_code_type() {
        return lock_code_type;
    }

    public void setLock_code_type(String lock_code_type) {
        this.lock_code_type = lock_code_type;
    }

    public String getLock_code() {
        return lock_code;
    }

    public void setLock_code(String lock_code) {
        this.lock_code = lock_code;
    }

    public String getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(String business_id) {
        this.business_id = business_id;
    }

    public String getCard_op_result() {
        return card_op_result;
    }

    public void setCard_op_result(String card_op_result) {
        this.card_op_result = card_op_result;
    }
}
