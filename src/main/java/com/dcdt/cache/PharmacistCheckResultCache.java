package com.dcdt.cache;

import com.dcdt.doctorstation.entity.Check;
import com.dcdt.doctorstation.entity.Order;
import com.dcdt.doctorstation.entity.OrderInfo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by wtwang on 2018/3/17.
 */
@Component
public class PharmacistCheckResultCache {
    //审核结果缓存
    private static ConcurrentMap<String, Check> map = new ConcurrentHashMap<String, Check>();
    private static ConcurrentMap<String, List<OrderInfo>> orderListMap = new ConcurrentHashMap<String, List<OrderInfo>>();

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

    public static void putPharmacistCheckResult_BZ(String presId, List<OrderInfo> orderList) {
        orderListMap.put(presId, orderList);
    }

    public static List<OrderInfo> findPharmacistCheckResult_BZ(String presId) {
        return orderListMap.get(presId);
    }

    public static void removePharmacistCheckResult_BZ(String presId) {
         orderListMap.remove(presId);
    }



}
