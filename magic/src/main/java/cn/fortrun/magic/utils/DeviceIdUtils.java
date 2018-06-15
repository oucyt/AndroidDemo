package cn.fortrun.magic.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.blankj.utilcode.util.DeviceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;

/**
 * Created by wangting on 2017/11/7.
 * 获取设备唯一ID
 * 1， wifi mac地址（wifi）；
 * // * 2， IMEI（imei）；
 * // * 3， 序列号（sn）；
 * // * 4， id：随机码。若前面的都取不到时，则随机生成一个随机码，需要缓存。
 * 出现过多台设备生成相同的id，后仅采用mac地址序列号作为唯一标志
 */

public class DeviceIdUtils {

    private static String TAG = DeviceIdUtils.class.getName();

    /**
     * 获取设备号
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        // AS重装并不会清除/data/data文件
        String parentPath = context.getCacheDir().getPath();
        String filename = "deviceId.bak";
        // 加载本地缓存
        String result = localDeviceId(parentPath + File.separator + filename);
        if (!result.isEmpty()) {
            Log.e(TAG, "从本地加载DeviceID");
            return result.replaceAll("\n", "");
        }

        // wifi mac地址
        result = DeviceUtils.getMacAddress();
        Log.e("mac地址：", result);

        if (result.isEmpty() || result.equals("please open wifi")) {
            Toast.makeText(context, "请打开wifi,并重启", Toast.LENGTH_SHORT).show();
            return "";
        }
//        Log.e(TAG, EncryptUtils.encryptMD5ToString(result));//与getMD5方法生成的不一致
        result = getMD5(result);
        // 缓存到本地
        saveDeviceId(result, parentPath, filename);
        return result;
    }


    /**
     * 保存DeviceId到本地
     *
     * @param str
     * @param parentPath
     * @param filename
     */
    public static void saveDeviceId(String str, String parentPath, String filename) {


        String path = parentPath + File.separator + filename;


        File file = new File(parentPath);
        if (!file.exists())
            file.mkdirs();//创建多层路径
        try {
            file = new File(path);
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(str.getBytes());
            raf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 加载本地DeviceId
     *
     * @param path
     * @return
     */
    public static String localDeviceId(String path) {

        String result = "";
        File file = new File(path);
        if (file.exists()) {
            try {
                InputStream fileInputStream = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
                String line;
                //分行读取
                if ((line = reader.readLine()) != null) {
                    result = line;
                }
                fileInputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * @param pwd 待加密字符串
     * @return String lKF21665 2012-3-6
     */
    public static String getMD5(String pwd) {

        try {
            pwd = URLEncoder.encode(pwd, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
        }
        byte[] source = pwd.getBytes();
        String s = null;
        // 用来将字节转换成 16 进制表示的字符
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            // MD5 的计算结果是一个 128 位的长整数， 用字节表示就是 16 个字节
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source);
            byte tmp[] = md.digest();

            // 每个字节用 16 进制表示的话，使用两个字符，所以表示成 16进制需要 32 个字符
            char str[] = new char[16 * 2];

            // 表示转换结果中对应的字符位置
            int k = 0;

            // 从第一个字节开始，对 MD5 的每一个字节转换成 16 进制字符的转换
            for (int i = 0; i < 16; i++) {
                // 取第 i 个字节
                byte byte0 = tmp[i];
                // 取字节中高 4 位的数字转换,>>> 为逻辑右移，将符号位一起右移
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                // 取字节中低 4 位的数字转换
                str[k++] = hexDigits[byte0 & 0xf];
            }
            s = new String(str); // 换后的结果转换为字符串

        } catch (Exception e) {
//            System.out.println("MD5 error" + e.toString());
        }
        return s;
    }

}
