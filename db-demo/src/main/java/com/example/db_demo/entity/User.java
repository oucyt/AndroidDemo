package com.example.db_demo.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity
public class User {
    @Id
    private Long _id;

    @NotNull
    private String name;

    private Long friendId;

    @ToOne(joinProperty = "friendId")
    private Friend friend;

    @ToMany(referencedJoinProperty = "userId")
    private List<Customer> customers;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1507654846)
    private transient UserDao myDao;

    @Generated(hash = 935440315)
    public User(Long _id, @NotNull String name, Long friendId) {
        this._id = _id;
        this.name = name;
        this.friendId = friendId;
    }

    @Generated(hash = 586692638)
    public User() {
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

    public Long getFriendId() {
        return this.friendId;
    }

    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }

    @Generated(hash = 1034232818)
    private transient Long friend__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 2055334107)
    public Friend getFriend() {
        Long __key = this.friendId;
        if (friend__resolvedKey == null || !friend__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            FriendDao targetDao = daoSession.getFriendDao();
            Friend friendNew = targetDao.load(__key);
            synchronized (this) {
                friend = friendNew;
                friend__resolvedKey = __key;
            }
        }
        return friend;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 8469225)
    public void setFriend(Friend friend) {
        synchronized (this) {
            this.friend = friend;
            friendId = friend == null ? null : friend.get_id();
            friend__resolvedKey = friendId;
        }
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1201841370)
    public List<Customer> getCustomers() {
        if (customers == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CustomerDao targetDao = daoSession.getCustomerDao();
            List<Customer> customersNew = targetDao._queryUser_Customers(_id);
            synchronized (this) {
                if (customers == null) {
                    customers = customersNew;
                }
            }
        }
        return customers;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 608715293)
    public synchronized void resetCustomers() {
        customers = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2059241980)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserDao() : null;
    }

}