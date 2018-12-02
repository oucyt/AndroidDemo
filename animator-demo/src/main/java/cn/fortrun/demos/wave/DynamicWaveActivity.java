package cn.fortrun.demos.wave;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import cn.fortrun.demos.R;

/**
 * description
 *
 * @author tianyu
 * @create 2018.09.13 21:00
 * @since 1.0.0
 */
public class DynamicWaveActivity extends AppCompatActivity {

    DynamicWave wave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity1);
        wave = findViewById(R.id.dw_wave);
        wave.setAnimationEndListener(new DynamicWave.AnimationEndListener() {
            @Override
            public void animationEnd() {
                Log.i("111", "已经结束了");
            }
        });
        TextView tv_start = findViewById(R.id.tv_start);
        TextView tv_still = findViewById(R.id.tv_still);
        TextView tv_stop = findViewById(R.id.tv_stop);
        tv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wave.resetWave();//重置
                wave.startWave();
            }
        });
        tv_still.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wave.startWave();
            }
        });
        tv_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wave.stopWave();
            }
        });
    }
}
