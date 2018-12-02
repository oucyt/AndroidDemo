package cn.fortrun.demos;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;

/**
 * description
 *
 * @author 87627
 * @create 2018.12.02 16:12
 * @since 1.0.0
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initAndroidUtils(this);
    }

    /**
     *
     * @param context
     */
    private void initAndroidUtils(Context context) {
        Utils.init(this);
        String dir = getExternalFilesDir(null).getPath();
        String prefix = "rzt";
        LogUtils.getConfig()
                .setLogSwitch(true)      // 设置 log 总开关
                .setConsoleSwitch(true)  // 设置 log 控制台开关
                .setGlobalTag("oucyt") // 设置 log 全局 tag
                .setLogHeadSwitch(true)// 设置 log 头部信息开关
                .setLog2FileSwitch(false) // 设置 log 文件开关
                .setDir(dir)            // 设置 log 文件存储目录
                .setFilePrefix(prefix)     // 设置 log 文件前缀
                .setBorderSwitch(false)   // 设置 log 边框开关
                .setSingleTagSwitch(false)// 设置 log 单一 tag 开关（为美化 AS 3.1 的 Logcat）
//                .setConsoleFilter  // 设置 log 控制台过滤器
//                .setFileFilter     // 设置 log 文件过滤器
//                .setStackDeep(3)      // 设置 log 栈深度
//                .setStackOffset(2)    // 设置 log 栈偏移
//                .setSaveDays(2)       // 设置 log 可保留天数
//                .addFormatter      // 新增 log 格式化器
        ;

    }
}
