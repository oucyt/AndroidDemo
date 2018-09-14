package com.example.db_demo.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * description
 *
 * @author 87627
 * @create 2018.09.10 8:40
 * @since 1.0.0
 */
@Entity
public class Customer {

    @Id
    private Long _id;

    private String name;

    private Long userId;

    @Generated(hash = 1967011719)
    public Customer(Long _id, String name, Long userId) {
        this._id = _id;
        this.name = name;
        this.userId = userId;
    }

    @Generated(hash = 60841032)
    public Customer() {
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
