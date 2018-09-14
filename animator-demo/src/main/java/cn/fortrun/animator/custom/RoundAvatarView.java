package cn.fortrun.animator.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * description
 * https://blog.csdn.net/tianjian4592/article/details/44336949
 *
 * @author tianyu
 * @create 2018.09.14 18:36
 * @since 1.0.0
 */
public class RoundAvatarView extends View {
    public RoundAvatarView(Context context) {
        super(context);
    }

    public RoundAvatarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundAvatarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
