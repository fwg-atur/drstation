package com.dcdt.cache;

import com.dcdt.doctorstation.entity.Check;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by wtwang on 2018/12/25.
 */
public class NurseCheckResultCache {
    //审核结果缓存
    private static ConcurrentMap<String, Check> map = new ConcurrentHashMap<String, Check>();

    public static Check findNurseCheckResult(String presId) {
        return map.get(presId);
    }

    public static void putNurseCheckResult(String presId, Check check) {
        map.put(presId, check);
    }

    public static Check removeNurseCheckResult(String presId) {
        return map.remove(presId);
    }

    public static boolean isEmpty() {
        return map.isEmpty();
    }
}
