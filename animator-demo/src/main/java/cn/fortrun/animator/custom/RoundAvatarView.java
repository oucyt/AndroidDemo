package cn.fortrun.animator.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import cn.fortrun.animator.R;

/**
 * description
 * https://blog.csdn.net/tianjian4592/article/details/44336949
 *
 * @author tianyu
 * @create 2018.09.14 18:36
 * @since 1.0.0
 */
public class RoundAvatarView extends View {
    private Paint mBitmapPaint = new Paint();
    private int mTotalWidth = 600;
    private float mHalfWidth = mTotalWidth / 2;
    private Bitmap mBitmap;

    private static final int[] mColors = new int[]{
            Color.TRANSPARENT, Color.TRANSPARENT, Color.WHITE
    };
    private static final float[] mPositions = new float[]{
            0, 0.6f, 1f
    };

    public RoundAvatarView(Context context) {
        super(context);
    }

    public RoundAvatarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //创建位图
        mBitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.simpson)).getBitmap();
        //将图scale成我们想要的大小
        mBitmap = Bitmap.createScaledBitmap(mBitmap, mTotalWidth, mTotalWidth, false);

        // 创建位图渲染
        BitmapShader bitmapShader = new BitmapShader(mBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        // 创建环形渐变
        RadialGradient radialGradient = new RadialGradient(mHalfWidth, mHalfWidth, mHalfWidth,
                mColors, mPositions, Shader.TileMode.MIRROR);
        // 创建组合渐变，由于直接按原样绘制就好，所以选择Mode.SRC_OVER
        ComposeShader composeShader = new ComposeShader(bitmapShader, radialGradient,
                PorterDuff.Mode.SRC_OVER);
        // 将位图渲染设置给paint
        mBitmapPaint.setShader(composeShader);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(mHalfWidth, mHalfWidth, mHalfWidth, mBitmapPaint);
    }
}
