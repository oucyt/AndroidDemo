package com.example.db_demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.db_demo.entity.Customer;
import com.example.db_demo.entity.Friend;
import com.example.db_demo.entity.FriendDao;
import com.example.db_demo.entity.User;
import com.example.db_demo.entity.UserDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * https://www.jianshu.com/p/6a70e57a9e8c
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_console)
    TextView mTvConsole;
    @BindView(R.id.btn_all)
    Button mBtnAll;
    @BindView(R.id.btn_insert)
    Button mBtnInsert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        test();
        test1();
//        test2();
        test3();
    }

    private void test3() {
        List<User> users = App.getDaoInstant().getUserDao().loadAll();
        int di = 9;
    }

    private void test2() {
        //查询地址是住在迪拜大楼的用户
        QueryBuilder<User> queryBuilder = App.getDaoInstant().getUserDao().queryBuilder();
        queryBuilder.join(UserDao.Properties.FriendId, Friend.class)
                .where(FriendDao.Properties.Name.eq("田宇"));
        List<User> users = queryBuilder.list();

    }

    private void test1() {

        Friend friend = new Friend();
        friend.setName("田宇");
        friend.setAge(23);
        long friendId = App.getDaoInstant().getFriendDao().insert(friend);

        User user = new User();
        user.setName("Tom");
        user.setFriendId(friendId);

        long flag = App.getDaoInstant().getUserDao().insert(user);
        List<Customer> customers = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Customer customer = new Customer();
            customer.setName("name:" + i);
            customer.setUserId(flag);
            customers.add(customer);
        }
        App.getDaoInstant().getCustomerDao().insertInTx(customers);
    }

    private void test() {
        User user = new User();
        user.setName("Limm");

        Friend friend = new Friend();
        friend.setName("六七个");
        friend.setAge(56);
        long friendFlag = App.getDaoInstant().getFriendDao().insert(friend);
        user.setFriendId(friendFlag);

        long flag = App.getDaoInstant().getUserDao().insert(user);
        Log.e("ddd", "flag:" + flag);
    }

    @OnClick({R.id.btn_all, R.id.btn_insert})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_all:
                break;
            case R.id.btn_insert:
                break;
        }
    }
}
