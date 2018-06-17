package com.example.audio;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_play)
    Button mBtnPlay;
    @BindView(R.id.text_view)
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        SoundPlayUtils.init(this);
    }

    @OnClick(R.id.btn_play)
    public void onViewClicked() {
        String json  = "{\"data\":{\"name\":\"田宇\",\"nation\":\"汉\",\"sex\":\"男\",\"idnumber\":\"500382199202226738\",\"address\":\"重庆市合川市钱塘镇湖塘村1组53号\",\"url\":\"http://idcard-1252821823.cossh.myqcloud.com/20180607/88435219a8a32dc42ebbc525464f31e9.png\",\"is_fake\":false,\"validity_start_date\":\"20080715\",\"validity_end_date\":\"20180715\",\"issuing_authority\":\"合川市公安局\"},\"tid\":\"472999bfe76740458e0636943d004449\",\"sender\":\"devices/8669c4fcc26c4f4d8b7b30cbcf432fd5\",\"code\":\"0\",\"cmd\":\"3051\",\"sid\":\"\"}";
        Type type;

        BASEResponse<IDCard> id  = fromJsonObject(json,IDCard.class);
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        String cmd = jsonObject.get("cmd").getAsString();

            //底座状态监测
            type = new TypeToken<BASEResponse<IDCard>>() {
            }.getType();
            BASEResponse baseresponse = new Gson().fromJson(json, type);
            IDCard idCard = (IDCard) baseresponse.getData();


//        String ss = String.format("mac:%s\nimei:%s\nuuid:%s",DeviceIdUtils.getMAC(this),DeviceIdUtils.getIMEI(this),DeviceIdUtils.getUUID(this));
//        mTextView.setText(ss);
//        SoundPlayUtils.play(5);
//        useSoundPool();

//        MediaPlayer mediaPlayer = MediaPlayer.create(this,R.raw.checkphoto);
//        mediaPlayer.start();
    }
    public   <T> BASEResponse<T> fromJsonObject(String reader, Class<T> clazz) {
        Type type = new ParameterizedTypeImpl(BASEResponse.class, new Class[]{clazz});
        return new Gson().fromJson(reader, type);
    }
    private void useSoundPool() {
        SoundPool soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);

        soundPool.load(this, R.raw.take_picture_again, 0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {

                soundPool.play(1, 1, 1, 0, 0, 1);
            }
        });
    }
}
