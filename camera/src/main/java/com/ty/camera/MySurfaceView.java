package com.ty.camera;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * description
 *
 * @author 87627
 * @create 2018.05.20 19:48
 * @since 1.0.0
 */
public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private String TAG = MySurfaceView.class.getSimpleName();
    private Camera mCamera;
    private boolean isFrontCamera = true;
    private Camera.CameraInfo mCameraInfo;
    private List<Camera.Size> mSupportedPreviewSizes;
    private SurfaceHolder mHolder;
    private Camera.Size mPreviewSize;

    public MySurfaceView(Context context) {
        super(context);
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void openFrontCamera() {
        mCameraInfo = new Camera.CameraInfo();
        int cameraCount = Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, mCameraInfo);
            if (mCameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                mCamera = Camera.open(camIdx);
                return;
            }
        }

        mCamera = Camera.open();
    }

    private void openBackCamera() {
        mCameraInfo = new Camera.CameraInfo();
        int cameraCount = Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, mCameraInfo);
            if (mCameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                mCamera = Camera.open(camIdx);
                return;
            }
        }

        mCamera = Camera.open();
    }

    public void onResume() {
        if (isFrontCamera)
            openFrontCamera();
        else
            openBackCamera();

        // 获取摄像头所支持的所有分辨率
        mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
        for (Camera.Size str : mSupportedPreviewSizes)
            Log.e("567", str.width + "/" + str.height);

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        try {
            mCamera.setDisplayOrientation(getCorrectCameraOrientation(mCameraInfo, mCamera));
            //设置预览展示
            mCamera.setPreviewDisplay(mHolder);
            mCamera.setPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {

                }
            });
            mCamera.startPreview();

        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    /**
     * 停止预览，释放Camera
     */
    public void doDestroyedCamera() {
        try {
            if (null != mCamera) {
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
        } catch (Exception e) {
            Log.e(TAG, e + "destoryCamera");
        }
    }

    public void onPause() {
        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    public void switchCamera() {
        isFrontCamera = !isFrontCamera;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Logger.i("surfaceCreated");
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Logger.i("surfaceCreated");
        doDestroyedCamera();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        Logger.i("format:%d,width:%d,height:%d", format, w, h);
        //如果您的预览可以更改或旋转，请注意这里的事件
        //确保在调整大小或格式停止预览
        if (holder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        //更改前停止预览
        try {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // 设置预览大小和作任何调整大小，旋转或重新修改这里
        // 用新设置开始预览
        try {
            Camera.Parameters parameters = mCamera.getParameters();
            Log.e("tw", mPreviewSize.width + "oooo" + mPreviewSize.height);
            double targetRatio = (double) w / h;
            List<String> focusModes = parameters.getSupportedFocusModes();
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO))
                //自动对焦
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);

            parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);

            Log.e("wtt", "surfaceChanged: " + getCorrectCameraOrientation(mCameraInfo, mCamera));
            //设置相机参数
            parameters.setRotation(0);
            mCamera.setParameters(parameters);
            //摄像头旋转角度
            mCamera.setDisplayOrientation(getCorrectCameraOrientation(mCameraInfo, mCamera));
            //设置预览展示
            mCamera.setPreviewDisplay(mHolder);

            mCamera.setPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {

                }
            });
            //开始预览
            mCamera.startPreview();

        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);
        if (mSupportedPreviewSizes != null) {
            //竖屏时获取最佳尺寸
            mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, Math.max(width, height), Math.min(width, height));

        }

        Log.e("tw", "resolveSize " + mPreviewSize.width + "/" + mPreviewSize.height);
    }


    /**
     * 这里是预览图尺寸处理的方法，就是在这把宽高调换，就可以达到效果
     *
     * @param sizes
     * @param w
     * @param h
     * @return
     */
    public Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) h / w;

        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;
        // 试着找到一个尺寸匹配的纵横比和尺寸
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }
        //找不到一个匹配的纵横比，进行处理
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }


    /**
     * 设置相机横竖屏
     *
     * @param info
     * @param camera
     * @return
     */
    private int getCorrectCameraOrientation(Camera.CameraInfo info, Camera camera) {

        int rotation = ((Activity) getContext()).getWindowManager().getDefaultDisplay().getRotation();
        Log.e(TAG, "getCorrectCameraOrientation: " + rotation);
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;

            case Surface.ROTATION_90:
                degrees = 90;
                break;

            case Surface.ROTATION_180:
                degrees = 180;
                break;

            case Surface.ROTATION_270:
                degrees = 270;
                break;

        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }

        return result;
    }
}
