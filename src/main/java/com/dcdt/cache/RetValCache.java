package com.dcdt.cache;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by LiRong on 2017/6/24.
 */
@Component
public class RetValCache {
    private static ConcurrentMap<String, Integer> map = new ConcurrentHashMap<String, Integer>();

    public static void putRetVal(String presId, int result) {
        map.putIfAbsent(presId, result);
    }

    public static int removeRetVal(String presId) {
        Integer i = map.remove(presId);
        return i == null ? -1 : i;
    }

    public static boolean isEmpty() {
        return map.isEmpty();
    }
}
