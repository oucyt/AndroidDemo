package cn.fortrun.magic.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;

/**
 * description
 *
 * @author tianyu
 * @create 2018.05.22 下午12:02
 * @since 1.0.0
 */
public class PathUtils {
    private static String TAG = PathUtils.class.getSimpleName();

    /**
     * 获得文件存储根路径
     *
     * @return
     */
    public static String getRoot(Context context) {
        //获得外部存储路径,默认路径为 /storage/emulated/0/Android/data/com.waka.workspace.logtofile/files/Logs/log_2016-03-14_16-15-09.log

        String result;

        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            //如果外部存储可用
            result = context.getExternalFilesDir(null).getPath();
        } else {
            //直接存在/data/data里，非root手机是看不到的
            result = context.getFilesDir().getPath();
        }
        Log.e(TAG, "getRoot > " + result);
        return result;
    }

    public static String getSnapshot(Context context, String filename) {
        String result = getRoot(context) + File.separator + "Snapshot";
        File file = new File(result);
        if (!file.exists())
            file.mkdirs();
        return result + File.separator + filename;
    }
}
