package com.example.audio;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.NetworkInterface;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created by wangting on 2017/11/7.
 * 获取设备唯一ID
 * 1， wifi mac地址（wifi）；
 * 2， IMEI（imei）；
 * 3， 序列号（sn）；
 * 4， id：随机码。若前面的都取不到时，则随机生成一个随机码，需要缓存。
 */

public class DeviceIdUtils {

    public static String getxxx(Context context) {


        String res = getSaveUuid();

        res = getMAC(context);//mac

        if (res.isEmpty())
            res = getIMEI(context);//imei
        if (res.isEmpty())
            res = getUUID();//uuid
        res = getMD5(res);
        writeTxtToFile(res, appDir.getPath() + "/", "device.cnf");
        return res;
    }

    private static File appDir = new File(Environment.getExternalStorageDirectory(), "magic");


    /**
     * 获取手机MAC地址
     *
     * @param context
     */
    public static String getMAC(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 如果当前设备系统大于等于6.0 使用下面的方法
            return getMD5("mac" + getMac());
        } else {
            try {
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                // 获取MAC地址
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String mac = wifiInfo.getMacAddress();

                return getMD5("mac" + mac);
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }
//        return "";
//        if (!TextUtils.isEmpty(result)) {
//            writeTxtToFile(result, appDir.getPath() + "/", "device.cnf");
//        }

    }

    /**
     * 获取mac地址
     *
     * @return
     */
    private static String getMac() {
        String str = "";
        String result = "";
        try {
            //查询文件路径
            Process pp = Runtime.getRuntime().exec("cat/sys/class/net/wlan0/address");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    result = str.trim();// 去空格
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (result == null || "".equals(result)) {
            try {
                return loadFileAsString("/sys/class/net/eth0/address").toUpperCase().substring(0, 17);
            } catch (Exception e) {
                e.printStackTrace();
                result = getAndroid7MAC();
            }
        }
        return result;
    }

    /**
     * 兼容7.0
     *
     * @return
     */
    private static String getAndroid7MAC() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;
                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
//                    Log.e("123", "getMAC: 4");
                    return "";
                }
                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }
                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "";
    }


    public static String loadFileAsString(String fileName) throws Exception {
        FileReader reader = new FileReader(fileName);
        String text = loadReaderAsString(reader);
        reader.close();
        return text;
    }

    public static String loadReaderAsString(Reader reader) throws Exception {
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[4096];
        int readLength = reader.read(buffer);
        while (readLength >= 0) {
            builder.append(buffer, 0, readLength);
            readLength = reader.read(buffer);
        }
        return builder.toString();
    }

    /**
     * 获取IMEI
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null)
            return "";
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return "";
        }
        return tm.getDeviceId();
    }


    /**
     * 得到全局唯一UUID
     */
    public static String getUUID() {
        return "random" + UUID.randomUUID().toString();
//            writeTxtToFile(uuid, appDir.getPath() + "/", "device.cnf");
    }

    /**
     * 保存uuid
     *
     * @return
     */
    public static String getSaveUuid() {
        String ePath = null;
        if (appDir.exists()) {
            File[] files = appDir.listFiles();
            if (files != null) {
                for (File e : files) {
                    ePath = e.getPath();
                }
            }
            if (!TextUtils.isEmpty(ePath)) {
                return ReadTxtFile(ePath);
            }
        }
        return "";
    }

    // 将字符串写入到文本文件中
    public static void writeTxtToFile(String strcontent, String filePath, String fileName) {
        //生成文件夹之后，再生成文件，不然会出错
        makeFilePath(filePath, fileName);
        String strFilePath = filePath + fileName;
        // 每次写入时，都换行写
        String strContent = strcontent + "\r\n";
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
//                Log.e("123", "Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
//            Log.e("123", "Error on write File:" + e);
        }
    }

    // 生成文件
    public static File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Log.e("123", "writeTxtToFile: zzz" + file.toString());
        return file;
    }

    // 生成文件夹
    public static void makeRootDirectory(String filePath) {
//        Log.e("123", "writeTxtToFile: zzz" + filePath);
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
//            Log.e("123:", e + "");
        }
    }

    //读取文本文件中的内容
    public static String ReadTxtFile(String strFilePath) {
        String path = strFilePath;
        String content = ""; //文件内容字符串
        //打开文件
        File file = new File(path);
        //如果path是传递过来的参数，可以做一个非目录的判断
        if (file.isDirectory()) {
//            Log.e("123", "The File doesn't not exist.");
        } else {
            try {
                InputStream instream = new FileInputStream(file);
                if (instream != null) {
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    //分行读取
                    while ((line = buffreader.readLine()) != null) {
                        content += line + "\n";
                    }
                    instream.close();
                }
            } catch (java.io.FileNotFoundException e) {
//                Log.e("123", "The File doesn't not exist.");
            } catch (IOException e) {
//                Log.e("123", e.getMessage());
            }
        }
        return content;
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
