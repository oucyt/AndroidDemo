package cn.fortrun.magic.model.bean;

import java.io.Serializable;

/**
 * Created by wangting on 2017/12/7.
 * 退房时的订单信息
 */

public class CheckOutInfo implements Serializable {

    /**
     * device_check_in : true
     * has_invoice : true
     * cash_pledge : 100
     * checkout_type : EARLY
     * order_id :
     * suborder_id :
     * room_no :
     *same_day_io:true
     * room_card_in_count:0
     * room_card_out_count:0
     */

    private boolean device_check_in;
    private boolean has_invoice;
    private String cash_pledge;
    private String checkout_type;
    private String order_id;
    private String suborder_id;
    private String room_no;
    private boolean same_day_io;
    private int room_card_in_count;
    private int room_card_out_count;

    public boolean isDevice_check_in() {
        return device_check_in;
    }

    public void setDevice_check_in(boolean device_check_in) {
        this.device_check_in = device_check_in;
    }

    public boolean isHas_invoice() {
        return has_invoice;
    }

    public void setHas_invoice(boolean has_invoice) {
        this.has_invoice = has_invoice;
    }

    public String getCash_pledge() {
        return cash_pledge;
    }

    public void setCash_pledge(String cash_pledge) {
        this.cash_pledge = cash_pledge;
    }

    public String getCheckout_type() {
        return checkout_type;
    }

    public void setCheckout_type(String checkout_type) {
        this.checkout_type = checkout_type;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getSuborder_id() {
        return suborder_id;
    }

    public void setSuborder_id(String suborder_id) {
        this.suborder_id = suborder_id;
    }

    public String getRoom_no() {
        return room_no;
    }

    public void setRoom_no(String room_no) {
        this.room_no = room_no;
    }

    public boolean isSame_day_io() {
        return same_day_io;
    }

    public void setSame_day_io(boolean same_day_io) {
        this.same_day_io = same_day_io;
    }

    public int getRoom_card_in_count() {
        return room_card_in_count;
    }

    public void setRoom_card_in_count(int room_card_in_count) {
        this.room_card_in_count = room_card_in_count;
    }

    public int getRoom_card_out_count() {
        return room_card_out_count;
    }

    public void setRoom_card_out_count(int room_card_out_count) {
        this.room_card_out_count = room_card_out_count;
    }
}
