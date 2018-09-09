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
    private Long id;

    private String name;

    private int age;

    @Generated(hash = 695648774)
    public Friend(Long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @Generated(hash = 287143722)
    public Friend() {
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

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
