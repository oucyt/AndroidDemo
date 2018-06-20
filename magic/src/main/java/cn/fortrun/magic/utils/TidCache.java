package cn.fortrun.magic.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wangting on 2018/1/3.
 * tid去重工具类
 * Mqtt业务id保证唯一，避免重复处理
 */

public class TidCache {
    private static Map<String, String> cache = new ConcurrentHashMap();

    public static void add(String tid) {
        cache.put(tid, tid);
    }

    public static void clear() {
        cache.clear();
    }

    public static boolean isExists(String tid) {
        return cache.containsKey(tid);
    }
}
