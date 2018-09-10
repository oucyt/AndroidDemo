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
    private Long id;

    private String name;

    private Long userId;

    @Generated(hash = 330035575)
    public Customer(Long id, String name, Long userId) {
        this.id = id;
        this.name = name;
        this.userId = userId;
    }

    @Generated(hash = 60841032)
    public Customer() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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
