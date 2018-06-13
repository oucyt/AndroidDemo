package com.example.audio;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.ArrayList;
import java.util.List;

/**
 * description
 *
 * @author tianyu
 * @create 2018.05.23 下午4:27
 * @since 1.0.0
 */

public class SoundPlayUtils {


    // SoundPool对象
    public static SoundPool mSoundPlayer = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
    public static SoundPlayUtils soundPlayUtils;

    /**
     * 初始化
     *
     * @param context
     */
    public static SoundPlayUtils init(Context context) {
        if (soundPlayUtils == null) {
            soundPlayUtils = new SoundPlayUtils();
        }

        List<Integer> list = new ArrayList();
        list.add(R.raw.take_picture_completed);//拍照完成
        list.add(R.raw.take_picture_again);//重新拍照
        list.add(R.raw.verify_completed);//验证完成
        list.add(R.raw.didi);//滴滴声
        list.add(R.raw.verify_failed);//验证失败
        list.add(R.raw.face_screen);//正对屏幕
        list.add(R.raw.offline);//未发现网络
        list.add(R.raw.verify_pass);//验证通过
        list.add(R.raw.swipe_idcard);//请刷卡
        list.add(R.raw.verify_unpass);//验证未通过
        list.add(R.raw.verify_again);//请重新验证
        // 初始化声音

        for (int i : list)
            mSoundPlayer.load(context, i, 1);
        return soundPlayUtils;
    }


    /**
     * 播放声音
     * 序号从1开始
     *
     * @param soundID
     */
    public static void play(int soundID) {

        mSoundPlayer.play(soundID, 1, 1, 0, 0, 1);
    }

}