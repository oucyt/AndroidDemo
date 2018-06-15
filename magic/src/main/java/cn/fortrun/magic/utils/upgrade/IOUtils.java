package cn.fortrun.magic.utils.upgrade;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.text.TextUtils;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by wangting on 2017/10/30.
 */

public class IOUtils {
    public static void closeIO(Closeable... closeables) {
        if (closeables != null) {
            for (Closeable closeable : closeables) {
                if (closeable != null) {
                    try {
                        closeable.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 删除之前的apk
     *
     * @param
     */
    public static void clearApk(Context context, String apkName) {
        File file = new File(apkName);
        DeleteFile(file);
//        File file=new File(apkName);
//        if (file.isDirectory()){
//            File[] files = file.listFiles();
//            //列出当前文件夹下的所有文件
//            for (File f : files) {
//                if (f.getName().toLowerCase().endsWith(".apk")){
//                    Log.e("fileName", f.getName()); //打印文件名
//                    clear(f.getName()); //递归删除
//                }
//            }
//        }
//        File apkFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), apkName);
//        if (apkFile.exists()) {
//            apkFile.delete();
//        }
//        return apkFile;
    }

    private static void clear(String f) {
        File apkFile = new File(f);
        if (apkFile.exists()) {
            apkFile.delete();
        }
    }

    public static void DeleteFile(File file) {
        if (file.exists() == false) {
            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    DeleteFile(f);
                }
                file.delete();
            }
        }
    }


    public static boolean isForeground(Activity activity) {
        return isForeground(activity, activity.getClass().getName());
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param context   Context
     * @param className 界面的类名
     * @return 是否在前台显示
     */
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className))
            return false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            return className.equals(cpn.getClassName());
        }
        return false;
    }

}
