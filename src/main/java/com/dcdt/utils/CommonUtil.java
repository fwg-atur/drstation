package com.dcdt.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by LiRong on 2017/12/18.
 */
public class CommonUtil {
    public static String getPresIdWithTime(String presId) {
        Calendar calendar = Calendar.getInstance();
        String[] temps = new String[]{
                StringUtils.leftPad(String.valueOf(calendar.get(Calendar.YEAR)), 2, "0"),
                StringUtils.leftPad(String.valueOf(calendar.get(Calendar.MONTH) + 1), 2, "0"),
                StringUtils.leftPad(String.valueOf(calendar.get(Calendar.DATE)), 2, "0"),
                StringUtils.leftPad(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)), 2, "0"),
                StringUtils.leftPad(String.valueOf(calendar.get(Calendar.MINUTE)), 2, "0"),
                StringUtils.leftPad(String.valueOf(calendar.get(Calendar.SECOND)), 2, "0"),
        };
        String res = StringUtils.join(temps, ':');
        return presId + "_" + res;
    }

    public static String changeXMLWithPattern(String xml, String presIdWithTime) {
        String s = xml.replaceAll("TAG=\"1\"", "TAG=\"3\"");
        s = s.replaceAll("PRES_ID=\".*?\" ", String.format("PRES_ID=\"%s\" ", presIdWithTime));
//        s = s.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");

        return s;
    }


}
