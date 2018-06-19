package cn.fortrun.magic.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * description
 *
 * @author tianyu
 * @create 2018.05.18 上午10:07
 * @since 1.0.0
 */
public class FileLogUtils {

    private final String TAG = FileLogUtils.class.getName();

    private static FileLogUtils instance;
    private SimpleDateFormat mSimpleDateFormat;

    private FileLogUtils() {

    }

    public static FileLogUtils getInstance() {
        if (instance == null)
            instance = new FileLogUtils();
        return instance;
    }

    private String parentPath = "";

    /**
     * 初始化
     * 决定根路径
     *
     * @param context
     */
    public void init(Context context) {
        parentPath = PathUtils.getRoot(context);
        mSimpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        Log.i(TAG, "文件日志类初始化完毕，路径：" + parentPath);
    }


    private void write(char type, String tag, String msg) {
        if (parentPath.isEmpty()) {
            Log.e(TAG, "文件日志类初始化失败，根路径为空");
            return;
        }

        //如果不存在
        File file = new File(parentPath);
        if (!file.exists()) {
            if (file.mkdirs()) {

                Log.e(TAG, parentPath + " 创建成功");
            } else
                try {
                    throw new Exception(parentPath + " 创建失败");
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        Log.e(tag, msg);
        // 一个文件维护当天的日志
        Date today = new Date();

        String filename = new SimpleDateFormat("yyyy_MM_dd").format(today) + ".log";

        String path = parentPath + File.separator + filename;

        String content = String.format("[日志级别:%c,标签:%s,设备时间:%s] > \n%s\n\n", type, tag, mSimpleDateFormat.format(today), msg);

        try {
            RandomAccessFile raf = new RandomAccessFile(path, "rw");
            long pos = raf.length();
            byte[] bytes = content.getBytes();
            raf.seek(pos);
            raf.write(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void v(String tag, String msg) {
        write('v', tag, msg);
    }

    public void d(String tag, String msg) {
        write('d', tag, msg);
    }

    public void i(String tag, String msg) {
        write('i', tag, msg);
    }

    public void w(String tag, String msg) {
        write('w', tag, msg);
    }

    public void e(String tag, String msg) {
        write('e', tag, msg);
    }
}
