package cn.fortrun.magic;

import android.app.Application;
import android.util.Log;

import com.blankj.utilcode.util.Utils;
import com.squareup.leakcanary.LeakCanary;

import cn.fortrun.magic.common.Constants;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import me.yokeyword.fragmentation.Fragmentation;
import me.yokeyword.fragmentation.helper.ExceptionHandler;

/**
 * Created by wangting on 2017/11/1.
 */

public class MyApplication extends Application {
    private String TAG = MyApplication.class.getName();


    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        Log.e(TAG, "Android工具库[AndroidUtilCode]初始化完成");
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        Log.e(TAG, "内存泄露检测工具[LeakCanary]初始化完成");
//        CrashReport.initCrashReport(getApplicationContext(), "9fbcbe586d", true);
//        Log.e(TAG, "程序异常日志上传工具[Bugly]初始化完成");
//        FileLogUtils.getInstance().init(this);
//        Log.e(TAG, "本地日志工具[FileLogUtils]初始化完成");

        Constants.init(this);
        Log.e(TAG, "系统常量[Constants]初始化完成");
        Fragmentation.builder()
                // 设置 栈视图 模式为 （默认）悬浮球模式   SHAKE: 摇一摇唤出  NONE：隐藏， 仅在Debug环境生效
                .stackViewMode(Fragmentation.BUBBLE)
                .debug(true) // 实际场景建议.debug(BuildConfig.DEBUG)
                /**
                 * 可以获取到{@link me.yokeyword.fragmentation.exception.AfterSaveStateTransactionWarning}
                 * 在遇到After onSaveInstanceState时，不会抛出异常，会回调到下面的ExceptionHandler
                 */
                .handleException(new ExceptionHandler() {
                    @Override
                    public void onException(Exception e) {
                        // 以Bugtags为例子: 把捕获到的 Exception 传到 Bugtags 后台。
                        // Bugtags.sendException(e);
                    }
                })
                .install();
    }

    /**
     * RxJava2 当取消订阅后(dispose())，RxJava抛出的异常后续无法接收(此时后台线程仍在跑，可能会抛出IO等异常),全部由RxJavaPlugin接收，需要提前设置ErrorHandler
     * 详情：http://engineering.rallyhealth.com/mobile/rxjava/reactive/2017/03/15/migrating-to-rxjava-2.html#Error Handling
     */
    private void setRxJavaErrorHandler() {
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        });
    }


}
