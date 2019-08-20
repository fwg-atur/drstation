package com.dcdt.cache;

import com.dcdt.doctorstation.entity.CheckMessage_BZ;
import com.dcdt.doctorstation.entity.CheckResults;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by LiRong on 2017/6/23.
 */

/**
 * 单例
 */
@Component
public class CheckResultCache {

    //审核结果缓存
    private static ConcurrentMap<String, CheckResults> map = new ConcurrentHashMap<String, CheckResults>();

    //滨医审核状态缓存
    private static ConcurrentMap<String,CheckMessage_BZ> stateMap = new ConcurrentHashMap<String, CheckMessage_BZ>();

    public static CheckResults findCheckResult(String presId) {
        return map.get(presId);
    }

    public static void putCheckResult(String presId, CheckResults checkResult) {
        map.put(presId, checkResult);
    }

    public static CheckResults removeCheckResult(String presId) {
        return map.remove(presId);
    }

    public static CheckMessage_BZ findCheckState(String presId) {
        return stateMap.get(presId);
    }

    public static void putCheckState(String presId, CheckMessage_BZ message) {
        stateMap.put(presId, message);
    }

    public static CheckMessage_BZ removeCheckState(String presId) {
        return stateMap.remove(presId);
    }

    public static boolean isEmpty() {
        return map.isEmpty();
    }

}

