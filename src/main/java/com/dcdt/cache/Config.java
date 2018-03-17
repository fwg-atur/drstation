package com.dcdt.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by LiRong on 2017/12/8.
 */
@Component
public class Config {
    @Value("${drStationServerIp}")
    private String drStationServerIp;

    @Value("${drStationServerPort}")
    private String drStationServerPort;

    @Value("${drugDescriptionURL}")
    private String drugDescriptionURL;

    @Value("${checkInterval}")
    private long checkInterval;

    @Value("${longestWaitTime}")
    private long longestWaitTime;

    @Value("${checkInterveneStateURL}")
    private String checkInterveneStateURL;

    @Value("${checkInterveneMessageURL}")
    private String checkInterveneMessageURL;

    @Value("${interveneFlag}")
    private int interveneFlag;

    public String getDrStationServerIp() {
        return drStationServerIp;
    }

    public String getDrStationServerPort() {
        return drStationServerPort;
    }

    public String getDrugDescriptionURL() {
        return drugDescriptionURL;
    }

    public long getCheckInterval() {
        return checkInterval;
    }

    public long getLongestWaitTime() {
        return longestWaitTime;
    }

    public String getCheckInterveneStateURL() {
        return checkInterveneStateURL;
    }

    public String getCheckInterveneMessageURL() {
        return checkInterveneMessageURL;
    }

    public int getInterveneFlag() {
        return interveneFlag;
    }
}
