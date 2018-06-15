package cn.fortrun.magic.interfaces;

/**
 * description
 * 倒计时接口
 * 需要倒计时的{@link android.app.Activity}实现它处理自己的逻辑，
 * {@link cn.fortrun.mirror.ui.common.BaseActivity}会在计时器的抽象方法中调用这个接口
 *
 * @author tianyu
 * @create 2018.05.18 下午5:52
 * @since 1.0.0
 */
public interface ICountDown {
    /**
     * 每秒都会调用，直到倒计时结束
     *
     * @param millisUntilFinished
     */
    void onTick(long millisUntilFinished);

    /**
     * 计时器结束时调用
     */
    void onFinish();
}
