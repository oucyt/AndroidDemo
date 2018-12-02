package cn.fortrun.demos.wave;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * description
 *
 * @author tianyu
 * @create 2018.09.13 21:01
 * @since 1.0.0
 */
public class DynamicWave extends View {

    // y=Asin(wx+φ)+b
    // y = Asin(wx+b)+h,b为初始周期点、一般不用管

    /**
     * 初始高度
     */
    private static final int OFFSET_H = 0;

    /**
     * 波动幅度
     */
    private static int A = 10;
    /**
     * 第一条水波移动速度
     */
    private static final int TRANSLATE_X_SPEED_ONE = 3;
    /**
     * 第二条水波移动速度
     */
    private static final int TRANSLATE_X_SPEED_TWO = 4;
    /**
     * 控件总宽高
     */
    private static int mTotalWidth, mTotalHeight;


    private float[] mYPositions;
    private int mXOffsetSpeedOne;
    private int mXOffsetSpeedTwo;
    private int mXOneOffset;
    private int mXTwoOffset;
    private Paint mWavePaint;

    private DrawFilter mDrawFilter;
    private Paint mCirclePaint;
    private PorterDuffXfermode mMode;
    /**
     * 监听水位到顶
     */
    private int waterHeight = 0;
    /**
     * 水位
     */
    private boolean isStart = false;
    /**
     * 动画开始标志
     */
    private boolean isDraw = true;
    /**
     * 是否绘制水波动画，跟暂停无关
     */
    private AnimationEndListener listener;

    public DynamicWave(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 将dp转化为px，用于控制不同分辨率上移动速度基本一致
        mXOffsetSpeedOne = dp2px(context, TRANSLATE_X_SPEED_ONE);
        mXOffsetSpeedTwo = dp2px(context, TRANSLATE_X_SPEED_TWO);
        // 初始绘制波纹的画笔
        mWavePaint = new Paint();
        mCirclePaint = new Paint();
        // 去除画笔锯齿
        mWavePaint.setAntiAlias(true);
        // 设置风格为实线
        mWavePaint.setStyle(Paint.Style.FILL);
        mWavePaint = new Paint();
        // 防抖动
        mWavePaint.setDither(true);
        // 开启图像过滤
        mWavePaint.setFilterBitmap(true);
        // 设置画笔颜色
        mWavePaint.setColor(Color.parseColor("#8bc4ff"));
        mCirclePaint.setColor(Color.WHITE);
        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

        // 重叠部分，显示源图，非重叠部分显示目标图 ps:目标图在下层
        mMode = new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP);
        mWavePaint.setXfermode(mMode);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 从canvas层面去除绘制时锯齿 https://www.cnblogs.com/duanweishi/p/4449500.html
        canvas.setDrawFilter(mDrawFilter);
        // 保存当前图层的状态
        canvas.saveLayer(0, 0, mTotalWidth, mTotalHeight, null);
        // 绘制目标图，圆
        drawBgCircle(canvas);
        // 水位自增
        if (isStart) {
            waterHeight++;
        }

        if (waterHeight > 0) {
            //只有在水位大于0才绘制水波纹
            long time = System.currentTimeMillis();
            // 绘制波浪
            drawWaves(canvas);
            Log.i(getClass().getSimpleName(), "耗时：" + (System.currentTimeMillis() - time));
            // 更新水平偏移量
            refreshXOffsets();

            // 引发view重绘，一般可以考虑延迟20-30ms重绘，空出时间片
            if (waterHeight <= mTotalHeight + A) {
                if (isDraw) {
                    postInvalidateDelayed(30);
                }
            } else {
                if (listener != null) {
                    listener.animationEnd();
                }
            }
        }
    }

    private void drawBgCircle(Canvas canvas) {
        canvas.drawCircle(mTotalWidth / 2, mTotalWidth / 2, mTotalWidth / 2, mCirclePaint);
    }

    private void drawWaves(Canvas canvas) {
        for (int i = 0; i < mTotalWidth; i++) {
            // 从左至右，绘制柱形图
            // 绘制第一条水波纹
            int idx1 = ((mXOneOffset + i) % mYPositions.length);
            int idx2 = ((mXTwoOffset + i) % mYPositions.length);
            canvas.drawLine(i, mTotalWidth - mYPositions[idx1] - waterHeight, i, mTotalWidth, mWavePaint);
            // 绘制第二条水波纹
            canvas.drawLine(i, mTotalWidth - mYPositions[idx2] - waterHeight, i, mTotalWidth, mWavePaint);
        }
    }

    private void refreshXOffsets() {
        // 改变两条波纹的移动点
        mXOneOffset += mXOffsetSpeedOne;
        mXTwoOffset += mXOffsetSpeedTwo;
        // 如果已经移动到结尾处，则重头记录
        if (mXOneOffset >= mTotalWidth) {
            mXOneOffset = 0;
        }

        if (mXTwoOffset > mTotalWidth) {
            mXTwoOffset = 0;
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 记录下view的宽高
        mTotalWidth = w;
        mTotalHeight = h;
        // 用于保存原始波纹的y值
        mYPositions = new float[mTotalWidth];

        /*波动周期,定为view总宽度*/
        float w1 = (float) (2 * Math.PI / mTotalWidth);
        // 根据view总宽度得出所有对应的y值
        for (int i = 0; i < mTotalWidth; i++) {
            mYPositions[i] = (float) (A * Math.sin(w1 * i) + OFFSET_H);
        }
    }

    /**
     * @category 开始波动
     */
    public void startWave() {
        if (!isStart) {
            isStart = true;
            isDraw = true;
            if (waterHeight == 0) {
                postInvalidateDelayed(30);
            }
        }
    }

    /**
     * @category 停止波动
     */
    public void stopWave() {
        if (isStart) {
            isStart = false;
        }
    }

    /**
     * @category 重置
     */
    public void resetWave() {
        isStart = false;
        isDraw = false;
        setWaterHeight(0);
        postInvalidateDelayed(30);
    }

    /**
     * 设置水位高度
     *
     * @param waterHeight
     */
    public void setWaterHeight(int waterHeight) {
        this.waterHeight = waterHeight;
    }

    public void setAnimationEndListener(AnimationEndListener listener) {
        this.listener = listener;
    }

    public interface AnimationEndListener {
        void animationEnd();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}