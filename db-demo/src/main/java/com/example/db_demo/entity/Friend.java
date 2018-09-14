package com.example.db_demo.entity;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * description
 *
 * @author 87627
 * @create 2018.09.09 22:30
 * @since 1.0.0
 */
@Entity
public class Friend {

    @Id
    private Long _id;

    private String name;

    private int age;

    @Generated(hash = 851585025)
    public Friend(Long _id, String name, int age) {
        this._id = _id;
        this.name = name;
        this.age = age;
    }

    @Generated(hash = 287143722)
    public Friend() {
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

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}
