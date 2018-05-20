package com.ty.camera;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.surface_view)
    MySurfaceView mSurfaceView;
    @BindView(R.id.gl_surface_view)
    CameraGLSurfaceView mGlSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        test();
    }

    private void test() {
        mSurfaceView.onResume();
    }
}
