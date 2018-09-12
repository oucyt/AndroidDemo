package com.example.db_demo;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.example.db_demo.entity.DaoMaster;
import com.example.db_demo.entity.DaoSession;

/**
 * description
 *
 * @author 87627
 * @create 2018.09.09 15:47
 * @since 1.0.0
 */
public class App extends Application{

    private static DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        //配置数据库
        setupDatabase();
    }
    /**
     * 配置数据库
     */
    private void setupDatabase() {
        //创建数据库shop.db"
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "shop.db", null);
        //获取可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();
        //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(db);
        //获取Dao对象管理者
        daoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoInstant() {
        return daoSession;
    }
}
