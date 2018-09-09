package com.example.db_demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.db_demo.entity.Friend;
import com.example.db_demo.entity.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
        test();
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
