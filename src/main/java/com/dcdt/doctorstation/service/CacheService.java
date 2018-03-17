package com.dcdt.doctorstation.service;

import com.dcdt.cache.SubmittedXMLCache;
import com.dcdt.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

/**
 * Created by LiRong on 2017/12/18.
 */
@Service
public class CacheService {
    private SubmittedXMLCache cache;

    public void putXML2Cache(String presId, String xml) {
        cache.put(presId, xml);
    }

    public String getXMLFromCache(String presId, String presIdWithTime) {
        String xml = cache.get(presId);
        return handleXML(xml, presIdWithTime);
    }

    private String handleXML(String xml, String presIdWithTime) {
        return CommonUtil.changeXMLWithPattern(xml, presIdWithTime);
    }

    public String removeXMLFromCache(String presId) {
        return cache.remove(presId);
    }

    @Autowired
    public void setCache(SubmittedXMLCache cache) {
        this.cache = cache;
    }
}
