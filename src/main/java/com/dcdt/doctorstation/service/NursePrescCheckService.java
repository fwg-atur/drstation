package com.dcdt.doctorstation.service;

import com.dcdt.cache.NurseCheckResultCache;
import com.dcdt.cache.RetValCache;
import com.dcdt.doctorstation.entity.Check;
import com.dcdt.doctorstation.entity.CheckMessage;
import com.dcdt.utils.HttpUtil;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by wtwang on 2018/12/21.
 */
@Service
public class NursePrescCheckService {
    @Value("${nurseCheckServerUrl}")
    private String nurseCheckServerUrl;

    private CacheService cacheService;

    private static final Logger logger = Logger.getLogger(NursePrescCheckService.class);

    public CheckMessage checkNursePresc(String data) {
        String url = nurseCheckServerUrl;
        String checkJson = "";
        checkJson = HttpUtil.sendPost(url, data);
        logger.debug(checkJson);
        CheckMessage checkMessage = new CheckMessage();
        putXML2Cache(checkMessage.getPresId(), checkJson);
        return checkMessage;
    }

    public <T> String toJson(T check) {
        Gson gson = new Gson();
        return gson.toJson(check);
    }

    public Check findNurseCheckResult(String presId) {
        return NurseCheckResultCache.removeNurseCheckResult(presId);
    }


    public void putRetValue(String presId, int result) {
        RetValCache.putRetVal(presId, result);
    }

    public int findRetValue(String presId) {
        if (notBackOrNext(presId))
            return -2;
        return RetValCache.removeRetVal(presId);
    }

    private boolean notBackOrNext(String presId) {
        return !RetValCache.containsKey(presId);
    }

    @Autowired
    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    private void putXML2Cache(String presId, String xml) {
        cacheService.putXML2Cache(presId, xml);
    }

}
