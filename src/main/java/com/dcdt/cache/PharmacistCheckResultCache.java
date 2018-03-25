package com.dcdt.cache;

import com.dcdt.doctorstation.entity.Check;
import com.dcdt.doctorstation.entity.CheckPresOutput;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by wtwang on 2018/3/17.
 */
@Component
public class PharmacistCheckResultCache {
    //审核结果缓存
    private static ConcurrentMap<String, Check> map = new ConcurrentHashMap<String, Check>();

    public static Check findPharmacistCheckResult(String presId) {
        return map.get(presId);
    }

    public static void putPharmacistCheckResult(String presId, Check check) {
        map.put(presId, check);
    }

    public static Check removePharmacistCheckResult(String presId) {
        return map.remove(presId);
    }

    public static boolean isEmpty() {
        return map.isEmpty();
    }
}
