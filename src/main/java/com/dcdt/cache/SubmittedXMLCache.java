package com.dcdt.cache;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by LiRong on 2017/12/18.
 */
@Component
public class SubmittedXMLCache {
    private ConcurrentMap<String, String> cache;

    public SubmittedXMLCache() {
        cache = new ConcurrentHashMap<String, String>();
    }

    public void put(String presId, String xml) {
        cache.put(presId, xml);
    }

    public String get(String presId) {
        return cache.get(presId);
    }

    public String remove(String presId) {
        return cache.remove(presId);
    }
}
