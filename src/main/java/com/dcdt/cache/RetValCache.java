package com.dcdt.cache;

import com.dcdt.doctorstation.entity.CheckMessage_BZ;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by LiRong on 2017/6/24.
 */
@Component
public class RetValCache {
    private static ConcurrentMap<String, Integer> map = new ConcurrentHashMap<String, Integer>();
    private static ConcurrentMap<String, String> bz_map = new ConcurrentHashMap<String, String>();

    public static void putRetVal(String presId, int result) {
        map.putIfAbsent(presId, result);
    }

    public static int removeRetVal(String presId) {
        Integer i = map.remove(presId);
        return i == null ? -1 : i;
    }

    public static boolean containsKey(String presId) {
        return map.containsKey(presId);
    }

    public static boolean isEmpty() {
        return map.isEmpty();
    }

    public static void putRetVal_bz(String presId, String message) {
        bz_map.putIfAbsent(presId, message);
    }

    public static String getRetVal_bz(String presId){
        return bz_map.get(presId);
    }

    public static String removeRetVal_bz(String presId) {
        return bz_map.remove(presId);
    }

    public static boolean containsKey_bz(String presId) {
        return bz_map.containsKey(presId);
    }

    public static boolean isEmpty_bz() {
        return bz_map.isEmpty();
    }
}
