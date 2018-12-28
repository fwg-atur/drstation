package com.dcdt.doctorstation.service;

import com.dcdt.cache.CheckResultCache;
import com.dcdt.cache.RetValCache;
import com.dcdt.doctorstation.entity.Advice;
import com.dcdt.doctorstation.entity.CheckInfo;
import com.dcdt.doctorstation.entity.CheckMessage;
import com.dcdt.doctorstation.entity.CheckResults;
import com.dcdt.utils.CommonUtil;
import com.dcdt.utils.HttpUtil;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by LiRong on 2017/6/20.
 */
@Service
public class PrescCheckService {
    @Value("${checkServerUrl}")
    private String checkServerUrl;

    @Value("${groupFlag}")
    private String groupFlag;

    private CacheService cacheService;

    private static final Logger logger = Logger.getLogger(PrescCheckService.class);

    /**
     * @param tag
     * @param data
     * @return 返回审核结果标识（flag,presId）,不包括审核结果字符串
     */
    public CheckMessage checkPresc(int tag, String data) {
        String url = checkServerUrl + "?tag=" + tag;
        data = data.replace("&nbsp;"," ");
        String checkJson = "";
        checkJson = HttpUtil.sendPost(url, data);
//        String checkJson = getTestJson();
        logger.debug(checkJson);

        CheckMessage checkMessage = new CheckMessage();

        if(checkJson == null || checkJson.equals("")){
            checkMessage.setHasProblem(-2);
            return checkMessage;
        }

        if (tag == 2) return checkMessage;

        checkMessage = handleCheckJson(checkJson);
        if(checkMessage.getHasProblem() == -2){
            return checkMessage;
        }
        putXML2Cache(checkMessage.getPresId(), data);
        return checkMessage;
    }

    private void putXML2Cache(String presId, String xml) {
        cacheService.putXML2Cache(presId, xml);
    }


    /**
     * 测试用
     *
     * @param tag
     * @param data
     * @return
     */
    public CheckMessage checkPrescForTest(int tag, String data) {
        String checkJson = "{\"doctor\":{\"USER_ID\":\"kd6\",\"NAME\":\"科大6\",\"POSITION\":\"其他\",\"DEPT_CODE\":\"10001\",\"DEPT_NAME\":\"保健科门诊\"},\"patient\":\n" +
                "{\"NAME\":\"王辰\",\"ID\":\"10900444\",\"GENDER\":\"男\",\"BIRTH\":\"19920030\",\"WEIGHT\":\"\",\"HEIGHT\":\"\",\"ALERGY_DRUGS\":\"\",\"PREGNANT\":\n" +
                "\"否\",\"LACT\":\"否\",\"HEPATICAL\":\"\",\"RENAL\":\"\",\"PANCREAS\":\"\",\"VISIT_ID\":\"\",\"PATIENT_PRES_ID\":\"499263\",\"IDENTITY_TYPE\":\"公民\",\n" +
                "\"FEE_TYPE\":\"自费\",\"SCR\":\"\",\"SCR_UNIT\":\"\",\"GESTATION_AGE\":\"\",\"PRETERM_BIRTH\":\"\",\"DRUG_HISTORY\":\"\",\"FAMILY_DISEASE_HISTORY\":\n" +
                "\"\",\"GENETIC_DISEASE\":\"\",\"MEDICARE_01\":\"\",\"MEDICARE_02\":\"\",\"MEDICARE_03\":\"\",\"MEDICARE_04\":\"\",\"MEDICARE_05\":\"\"},\"diagnosis\":\n" +
                "{\"DIAGNOSISES\":\"恶性贫血\"},\"advices\":[{\"checkInfoList\":[" +
                "{\"COLOR\":\"黄色\",\"NAME\":\"用法用量\",\"WARNING_LEVEL\":\"慎用\",\n" +
                "\"WARNING_INFO\":\"疗程超限。药品疗程超过天。处方的疗程为1天。科室编码：10001。科室名称：\",\"REF_SOURCE\":\"药品说明书及医院相关规定\",\n" +
                "\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":0}],\"REPEAT\":\"\",\"DRUG_LO_NAME\":\"10%氯化钠注射液氯化钠注射液氯化钠注\",\"DRUG_LO_ID\":\"280154007\",\n" +
                "\"ADMINISTRATION\":\"ivdrip\",\"DOSAGE\":\"10\",\"DOSAGE_UNIT\":\"ml\",\"FREQ_COUNT\":\"qd\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20180121\",\"END_DAY\":\"\",\n" +
                "\"DEPT_CODE\":\"10001\",\"DOCTOR_NAME\":\"科大6\",\"ORDER_NO\":\"1\",\"ORDER_SUB_NO\":\"1\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"其他\",\"GROUP_ID\":\"1\",\n" +
                "\"USER_ID\":\"kd6\",\"PRES_ID\":\"499263\",\"PRES_DATE\":\"20180121\",\"PRES_SEQ_ID\":\"499263\",\"PK_ORDER_NO\":\"1114774\",\"COURSE\":\"1\",\"PKG_COUNT\":\"2\",\"PKG_UNIT\":\n" +
                "\"支\",\"BAK_01\":\"\",\"BAK_02\":\"\",\"BAK_03\":\"注射剂\",\"BAK_04\":\"10ml\",\"BAK_05\":\"国药容生\",\"HIGHEST_WARNING_LEVEL\":1},\n" +
                "{\"checkInfoList\":[{\"COLOR\":\"黄色\",\"NAME\":\"医院管理\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"疗程超限。药品疗程超过天。处方的疗程为4天。\n" +
                "科室编码：10001。科室名称：\",\"REF_SOURCE\":\"药品说明书及医院相关规定\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\n" +
                "\"REGULAR_WARNING_LEVEL\":-1}],\"REPEAT\":\"\",\"DRUG_LO_NAME\":\"氨茶碱片\",\"DRUG_LO_ID\":\"230131005\",\"ADMINISTRATION\":\"口服\",\"DOSAGE\":\"0.1\",\n" +
                "\"DOSAGE_UNIT\":\"g\",\"FREQ_COUNT\":\"tid\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20180121\",\"END_DAY\":\"\",\n" +
                "\"DEPT_CODE\":\"10001\",\"DOCTOR_NAME\":\"科大6\",\"ORDER_NO\":\"2\",\"ORDER_SUB_NO\":\"1\",\"AUTHORITY_LEVELS\":\"\",\n" +
                "\"ALERT_LEVELS\":\"\",\"TITLE\":\"其他\",\"GROUP_ID\":\"2\",\"USER_ID\":\"kd6\",\"PRES_ID\":\"499263\",\"PRES_DATE\":\"20180121\",\"PRES_SEQ_ID\":\"499263\",\n" +
                "\"PK_ORDER_NO\":\"1114775\",\"COURSE\":\"4\",\"PKG_COUNT\":\"10\",\"PKG_UNIT\":\"片\",\"BAK_01\":\"\",\"BAK_02\":\"\",\"BAK_03\":\"注射剂\",\"BAK_04\":\"0.1g\",\n" +
                "\"BAK_05\":\"山东新华\",\"HIGHEST_WARNING_LEVEL\":1},\n" +"{\"checkInfoList\":[{\"COLOR\":\"黄色\",\"NAME\":\"医院管理\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"疗程超限。药品疗程超过天。处方的疗程为4天。\n" +
                "科室编码：10001。科室名称：\",\"REF_SOURCE\":\"药品说明书及医院相关规定\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\n" +
                "\"REGULAR_WARNING_LEVEL\":-1}],\"REPEAT\":\"\",\"DRUG_LO_NAME\":\"氨茶碱片\",\"DRUG_LO_ID\":\"230131005\",\"ADMINISTRATION\":\"口服\",\"DOSAGE\":\"0.1\",\n" +
                "\"DOSAGE_UNIT\":\"g\",\"FREQ_COUNT\":\"tid\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20180121\",\"END_DAY\":\"\",\n" +
                "\"DEPT_CODE\":\"10001\",\"DOCTOR_NAME\":\"科大6\",\"ORDER_NO\":\"2\",\"ORDER_SUB_NO\":\"1\",\"AUTHORITY_LEVELS\":\"\",\n" +
                "\"ALERT_LEVELS\":\"\",\"TITLE\":\"其他\",\"GROUP_ID\":\"2\",\"USER_ID\":\"kd6\",\"PRES_ID\":\"499263\",\"PRES_DATE\":\"20180121\",\"PRES_SEQ_ID\":\"499263\",\n" +
                "\"PK_ORDER_NO\":\"1114775\",\"COURSE\":\"4\",\"PKG_COUNT\":\"10\",\"PKG_UNIT\":\"片\",\"BAK_01\":\"\",\"BAK_02\":\"\",\"BAK_03\":\"注射剂\",\"BAK_04\":\"0.1g\",\n" +
                "\"BAK_05\":\"山东新华\",\"HIGHEST_WARNING_LEVEL\":1},\n" +"{\"checkInfoList\":[{\"COLOR\":\"黄色\",\"NAME\":\"医院管理\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"疗程超限。药品疗程超过天。处方的疗程为4天。\n" +
                "科室编码：10001。科室名称：\",\"REF_SOURCE\":\"药品说明书及医院相关规定\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\n" +
                "\"REGULAR_WARNING_LEVEL\":-1}],\"REPEAT\":\"\",\"DRUG_LO_NAME\":\"氨茶碱片\",\"DRUG_LO_ID\":\"230131005\",\"ADMINISTRATION\":\"口服\",\"DOSAGE\":\"0.1\",\n" +
                "\"DOSAGE_UNIT\":\"g\",\"FREQ_COUNT\":\"tid\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20180121\",\"END_DAY\":\"\",\n" +
                "\"DEPT_CODE\":\"10001\",\"DOCTOR_NAME\":\"科大6\",\"ORDER_NO\":\"2\",\"ORDER_SUB_NO\":\"1\",\"AUTHORITY_LEVELS\":\"\",\n" +
                "\"ALERT_LEVELS\":\"\",\"TITLE\":\"其他\",\"GROUP_ID\":\"2\",\"USER_ID\":\"kd6\",\"PRES_ID\":\"499263\",\"PRES_DATE\":\"20180121\",\"PRES_SEQ_ID\":\"499263\",\n" +
                "\"PK_ORDER_NO\":\"1114775\",\"COURSE\":\"4\",\"PKG_COUNT\":\"10\",\"PKG_UNIT\":\"片\",\"BAK_01\":\"\",\"BAK_02\":\"\",\"BAK_03\":\"注射剂\",\"BAK_04\":\"0.1g\",\n" +
                "\"BAK_05\":\"山东新华\",\"HIGHEST_WARNING_LEVEL\":1},\n" +
                "{\"checkInfoList\":[{\"COLOR\":\"黄色\",\"NAME\":\"医院管理\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"疗程超限。药品疗程超过天。处方的疗程为4天。\n" +
                "科室编码：10001。科室名称：\",\"REF_SOURCE\":\"药品说明书及医院相关规定\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\n" +
                "\"REGULAR_WARNING_LEVEL\":-1}],\"REPEAT\":\"\",\"DRUG_LO_NAME\":\"氨茶碱片\",\"DRUG_LO_ID\":\"230131005\",\"ADMINISTRATION\":\"口服\",\"DOSAGE\":\"0.1\",\n" +
                "\"DOSAGE_UNIT\":\"g\",\"FREQ_COUNT\":\"tid\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20180121\",\"END_DAY\":\"\",\n" +
                "\"DEPT_CODE\":\"10001\",\"DOCTOR_NAME\":\"科大6\",\"ORDER_NO\":\"2\",\"ORDER_SUB_NO\":\"1\",\"AUTHORITY_LEVELS\":\"\",\n" +
                "\"ALERT_LEVELS\":\"\",\"TITLE\":\"其他\",\"GROUP_ID\":\"2\",\"USER_ID\":\"kd6\",\"PRES_ID\":\"499263\",\"PRES_DATE\":\"20180121\",\"PRES_SEQ_ID\":\"499263\",\n" +
                "\"PK_ORDER_NO\":\"1114775\",\"COURSE\":\"4\",\"PKG_COUNT\":\"10\",\"PKG_UNIT\":\"片\",\"BAK_01\":\"\",\"BAK_02\":\"\",\"BAK_03\":\"注射剂\",\"BAK_04\":\"0.1g\",\n" +
                "\"BAK_05\":\"山东新华\",\"HIGHEST_WARNING_LEVEL\":1},\n" +
                "{\"checkInfoList\":[{\"COLOR\":\"黄色\",\"NAME\":\"用法用量\",\"WARNING_LEVEL\":\"慎用\",\n" +
                "\"WARNING_INFO\":\"疗程超限。药品疗程超过天。处方的疗程为20天。科室编码：10001。科室名称：\",\"REF_SOURCE\":\n" +
                "\"药品说明书及医院相关规定\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1}],\n" +
                "\"REPEAT\":\"\",\"DRUG_LO_NAME\":\"阿法骨化醇片\",\"DRUG_LO_ID\":\"160131015\",\"ADMINISTRATION\":\"口服\",\"DOSAGE\":\"0.5\",\"DOSAGE_UNIT\":\"ug\",\"FREQ_COUNT\":\"qd\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20180121\",\"END_DAY\":\"\",\"DEPT_CODE\":\"10001\",\"DOCTOR_NAME\":\"科大6\",\"ORDER_NO\":\"3\",\"ORDER_SUB_NO\":\"1\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"其他\",\"GROUP_ID\":\"3\",\"USER_ID\":\"kd6\",\"PRES_ID\":\"499263\",\"PRES_DATE\":\"20180121\",\"PRES_SEQ_ID\":\"499263\",\"PK_ORDER_NO\":\"1114776\",\"COURSE\":\"20\",\"PKG_COUNT\":\"2\",\"PKG_UNIT\":\"盒\",\"BAK_01\":\"\",\"BAK_02\":\"\",\"BAK_03\":\"注射剂\",\"BAK_04\":\"0.5ug\",\"BAK_05\":\"重庆药友\",\"HIGHEST_WARNING_LEVEL\":1}],\"checkInfoMap\":{\"studio.atur.api.persistence.presc_stat.PrescInfoKey@399cbc89\":[{\"COLOR\":\"黄色\",\"NAME\":\"用法用量\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"疗程超限。药品疗程超过天。处方的疗程为1天。科室编码：10001。科室名称：\",\"REF_SOURCE\":\"药品说明书及医院相关规定\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1}],\"studio.atur.api.persistence.presc_stat.PrescInfoKey@715e8b1c\":[{\"COLOR\":\"黄色\",\"NAME\":\"用法用量\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"疗程超限。药品疗程超过天。处方的疗程为20天。科室编码：10001。科室名称：\",\"REF_SOURCE\":\"药品说明书及医院相关规定\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1}],\"studio.atur.api.persistence.presc_stat.PrescInfoKey@611de3eb\":[{\"COLOR\":\"黄色\",\"NAME\":\"用法用量\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"疗程超限。药品疗程超过天。处方的疗程为4天。科室编码：10001。科室名称：\",\"REF_SOURCE\":\"药品说明书及医院相关规定\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1}]},\"output\":\"<CheckOutput AntiDrugPos=\\\"\\\">\\n    <PresInfo ORDER_ID=\\\"1\\\" ORDER_SUB_ID=\\\"1\\\" DRUG_LO_ID=\\\"280154007\\\" DRUG_LO_NAME=\\\"10%氯化钠注射液氯化钠注射液氯化钠注\\\">\\n        <CheckInfo COLOR=\\\"黄色\\\" NAME=\\\"用法用量\\\" WARNING_LEVEL=\\\"慎用\\\" WARNING_INFO=\\\"疗程超限。药品疗程超过天。处方的疗程为1天。科室编码：10001。科室名称：\\\" REF_SOURCE=\\\"药品说明书及医院相关规定\\\" YPMC=\\\"\\\" JSXX=\\\"\\\" ZYJL=\\\"\\\" TYSM=\\\"\\\" LCSY=\\\"\\\" />\\n    </PresInfo>\\n    <PresInfo ORDER_ID=\\\"2\\\" ORDER_SUB_ID=\\\"1\\\" DRUG_LO_ID=\\\"230131005\\\" DRUG_LO_NAME=\\\"氨茶碱片\\\">\\n        <CheckInfo COLOR=\\\"黄色\\\" NAME=\\\"用法用量\\\" WARNING_LEVEL=\\\"慎用\\\" WARNING_INFO=\\\"疗程超限。药品疗程超过天。处方的疗程为4天。科室编码：10001。科室名称：\\\" REF_SOURCE=\\\"药品说明书及医院相关规定\\\" YPMC=\\\"\\\" JSXX=\\\"\\\" ZYJL=\\\"\\\" TYSM=\\\"\\\" LCSY=\\\"\\\" />\\n </PresInfo>\\n     <PresInfo ORDER_ID=\\\"3\\\" ORDER_SUB_ID=\\\"1\\\" DRUG_LO_ID=\\\"160131015\\\" DRUG_LO_NAME=\\\"阿法骨化醇片\\\">\\n        <CheckInfo COLOR=\\\"黄色\\\" NAME=\\\"用法用量\\\" WARNING_LEVEL=\\\"慎用\\\" WARNING_INFO=\\\"疗程超限。药品疗程超过天。处方的疗程为20天。科室编码：10001。科室名称：\\\" REF_SOURCE=\\\"药品说明书及医院相关规定\\\" YPMC=\\\"\\\" JSXX=\\\"\\\" ZYJL=\\\"\\\" TYSM=\\\"\\\" LCSY=\\\"\\\" />\\n    </PresInfo>\\n</CheckOutput>\\n\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\",\"HIGHEST_WARNING_LEVEL\":1,\"WARNING_COUNT\":3,\"TAG\":\"1\"}";
        return handleCheckJson(checkJson);
    }

    public String getTestJson() {
        String res = "{\"doctor\":{\"USER_ID\":\"3947\",\"NAME\":\"王健\",\"POSITION\":\"住院医师\",\"DEPT_CODE\":\"126\",\"DEPT_NAME\":\"麻醉科手术室\"},\"patient\":{\"NAME\":\"朱宸若曦\",\"ID\":\"1902964\",\"GENDER\":\"女\",\"BIRTH\":\"20151101\",\"WEIGHT\":\"\",\"HEIGHT\":\"\",\"ALERGY_DRUGS\":\"\",\"PREGNANT\":\"\",\"LACT\":\"\",\"HEPATICAL\":\"\",\"RENAL\":\"\",\"PANCREAS\":\"\",\"VISIT_ID\":\"\",\"PATIENT_PRES_ID\":\"2018010500655\",\"IDENTITY_TYPE\":\"\",\"FEE_TYPE\":\"\",\"SCR\":\"\",\"SCR_UNIT\":\"umol/L\",\"GESTATION_AGE\":\"\",\"PRETERM_BIRTH\":\"\",\"DRUG_HISTORY\":\"\",\"FAMILY_DISEASE_HISTORY\":\"\",\"GENETIC_DISEASE\":\"\",\"MEDICARE_01\":\"\",\"MEDICARE_02\":\"\",\"MEDICARE_03\":\"\",\"MEDICARE_04\":\"\",\"MEDICARE_05\":\"\"},\"diagnosis\":{\"DIAGNOSISES\":\"病毒性感冒\"},\"advices\":[{\"checkInfoList\":[],\"REPEAT\":\"\",\"DRUG_LO_NAME\":\"硫酸氨基葡萄糖钾胶囊\",\"DRUG_LO_ID\":\"1602\",\"ADMINISTRATION\":\" po\",\"DOSAGE\":\"0.25\",\"DOSAGE_UNIT\":\"g\",\"FREQ_COUNT\":\"2\",\"FREQ_INTERVAL\":\"1\",\"FREQ_INTERVAL_UNIT\":\"日\",\"START_DAY\":\"20180105\",\"END_DAY\":\"\",\"DEPT_CODE\":\"126\",\"DOCTOR_NAME\":\"王健\",\"ORDER_NO\":\"1\",\"ORDER_SUB_NO\":\"1\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"住院医师\",\"GROUP_ID\":\"1\",\"USER_ID\":\"3947\",\"PRES_ID\":\"2018010500655\",\"PRES_DATE\":\"20180105\",\"PRES_SEQ_ID\":\"\",\"PK_ORDER_NO\":\"\",\"COURSE\":\"10\",\"PKG_COUNT\":\"20\",\"PKG_UNIT\":\"粒\",\"BAK_01\":\"\",\"BAK_02\":\"\",\"BAK_03\":\"胶囊剂\",\"BAK_04\":\"0.25g*20\",\"BAK_05\":\"山西康宝生物制品股分有限公司\",\"HIGHEST_WARNING_LEVEL\":0},{\"checkInfoList\":[],\"REPEAT\":\"\",\"DRUG_LO_NAME\":\"硫酸氨基葡萄糖钾胶囊\",\"DRUG_LO_ID\":\"1602\",\"ADMINISTRATION\":\"po\",\"DOSAGE\":\"0.25\",\"DOSAGE_UNIT\":\"g\",\"FREQ_COUNT\":\"2\",\"FREQ_INTERVAL\":\"1\",\"FREQ_INTERVAL_UNIT\":\"日\",\"START_DAY\":\"20180105\",\"END_DAY\":\"\",\"DEPT_CODE\":\"126\",\"DOCTOR_NAME\":\"王健\",\"ORDER_NO\":\"2\",\"ORDER_SUB_NO\":\"1\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"住院医师\",\"GROUP_ID\":\"2\",\"USER_ID\":\"3947\",\"PRES_ID\":\"2018010500655\",\"PRES_DATE\":\"20180105\",\"PRES_SEQ_ID\":\"\",\"PK_ORDER_NO\":\"\",\"COURSE\":\"10\",\"PKG_COUNT\":\"20\",\"PKG_UNIT\":\"粒\",\"BAK_01\":\"\",\"BAK_02\":\"\",\"BAK_03\":\"胶囊剂\",\"BAK_04\":\"0.25g*20\",\"BAK_05\":\"山西康宝生物制品股分有限公司\",\"HIGHEST_WARNING_LEVEL\":0},{\"checkInfoList\":[],\"REPEAT\":\"\",\"DRUG_LO_NAME\":\"硫酸氨基葡萄糖钾胶囊\",\"DRUG_LO_ID\":\"1602\",\"ADMINISTRATION\":\"po\",\"DOSAGE\":\"0.25\",\"DOSAGE_UNIT\":\"g\",\"FREQ_COUNT\":\"2\",\"FREQ_INTERVAL\":\"1\",\"FREQ_INTERVAL_UNIT\":\"日\",\"START_DAY\":\"20180105\",\"END_DAY\":\"\",\"DEPT_CODE\":\"126\",\"DOCTOR_NAME\":\"王健\",\"ORDER_NO\":\"3\",\"ORDER_SUB_NO\":\"1\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"住院医师\",\"GROUP_ID\":\"3\",\"USER_ID\":\"3947\",\"PRES_ID\":\"2018010500655\",\"PRES_DATE\":\"20180105\",\"PRES_SEQ_ID\":\"\",\"PK_ORDER_NO\":\"\",\"COURSE\":\"10\",\"PKG_COUNT\":\"20\",\"PKG_UNIT\":\"粒\",\"BAK_01\":\"\",\"BAK_02\":\"\",\"BAK_03\":\"胶囊剂\",\"BAK_04\":\"0.25g*20\",\"BAK_05\":\"山西康宝生物制品股分有限公司\",\"HIGHEST_WARNING_LEVEL\":0},{\"checkInfoList\":[],\"REPEAT\":\"\",\"DRUG_LO_NAME\":\"硫酸氨基葡萄糖钾胶囊\",\"DRUG_LO_ID\":\"1602\",\"ADMINISTRATION\":\"po\",\"DOSAGE\":\"0.25\",\"DOSAGE_UNIT\":\"g\",\"FREQ_COUNT\":\"2\",\"FREQ_INTERVAL\":\"1\",\"FREQ_INTERVAL_UNIT\":\"日\",\"START_DAY\":\"20180105\",\"END_DAY\":\"\",\"DEPT_CODE\":\"126\",\"DOCTOR_NAME\":\"王健\",\"ORDER_NO\":\"4\",\"ORDER_SUB_NO\":\"1\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"住院医师\",\"GROUP_ID\":\"4\",\"USER_ID\":\"3947\",\"PRES_ID\":\"2018010500655\",\"PRES_DATE\":\"20180105\",\"PRES_SEQ_ID\":\"\",\"PK_ORDER_NO\":\"\",\"COURSE\":\"10\",\"PKG_COUNT\":\"1\",\"PKG_UNIT\":\"盒\",\"BAK_01\":\"\",\"BAK_02\":\"\",\"BAK_03\":\"胶囊剂\",\"BAK_04\":\"0.25g*20\",\"BAK_05\":\"山西康宝生物制品股分有限公司\",\"HIGHEST_WARNING_LEVEL\":0},{\"checkInfoList\":[],\"REPEAT\":\"\",\"DRUG_LO_NAME\":\"硫酸氨基葡萄糖钾胶囊\",\"DRUG_LO_ID\":\"1602\",\"ADMINISTRATION\":\"po\",\"DOSAGE\":\"0.25\",\"DOSAGE_UNIT\":\"g\",\"FREQ_COUNT\":\"2\",\"FREQ_INTERVAL\":\"1\",\"FREQ_INTERVAL_UNIT\":\"日\",\"START_DAY\":\"20180105\",\"END_DAY\":\"\",\"DEPT_CODE\":\"126\",\"DOCTOR_NAME\":\"王健\",\"ORDER_NO\":\"5\",\"ORDER_SUB_NO\":\"1\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"住院医师\",\"GROUP_ID\":\"5\",\"USER_ID\":\"3947\",\"PRES_ID\":\"2018010500655\",\"PRES_DATE\":\"20180105\",\"PRES_SEQ_ID\":\"\",\"PK_ORDER_NO\":\"\",\"COURSE\":\"10\",\"PKG_COUNT\":\"1\",\"PKG_UNIT\":\"盒\",\"BAK_01\":\"\",\"BAK_02\":\"\",\"BAK_03\":\"胶囊剂\",\"BAK_04\":\"0.25g*20\",\"BAK_05\":\"山西康宝生物制品股分有限公司\",\"HIGHEST_WARNING_LEVEL\":0},{\"checkInfoList\":[],\"REPEAT\":\"\",\"DRUG_LO_NAME\":\"硫酸氨基葡萄糖钾胶囊\",\"DRUG_LO_ID\":\"1602\",\"ADMINISTRATION\":\"po\",\"DOSAGE\":\"0.25\",\"DOSAGE_UNIT\":\"g\",\"FREQ_COUNT\":\"2\",\"FREQ_INTERVAL\":\"1\",\"FREQ_INTERVAL_UNIT\":\"日\",\"START_DAY\":\"20180105\",\"END_DAY\":\"\",\"DEPT_CODE\":\"126\",\"DOCTOR_NAME\":\"王健\",\"ORDER_NO\":\"6\",\"ORDER_SUB_NO\":\"1\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"住院医师\",\"GROUP_ID\":\"6\",\"USER_ID\":\"3947\",\"PRES_ID\":\"2018010500655\",\"PRES_DATE\":\"20180105\",\"PRES_SEQ_ID\":\"\",\"PK_ORDER_NO\":\"\",\"COURSE\":\"10\",\"PKG_COUNT\":\"1\",\"PKG_UNIT\":\"盒\",\"BAK_01\":\"\",\"BAK_02\":\"\",\"BAK_03\":\"胶囊剂\",\"BAK_04\":\"0.25g*20\",\"BAK_05\":\"山西康宝生物制品股分有限公司\",\"HIGHEST_WARNING_LEVEL\":0}],\"HIGHEST_WARNING_LEVEL\":1,\"WARNING_COUNT\":3}";
        return res;
    }

    /**
     * @param checkJson
     */
    protected CheckMessage handleCheckJson(String checkJson) {
        Gson g = new Gson();
        CheckResults results = g.fromJson(checkJson, CheckResults.class);
        if(results.getCheckInfoMap() == null){
            CheckMessage message = new CheckMessage();
            message.setHasProblem(-2);
            return message;
        }
        results.setAdvices(sortCheckResult(results.getAdvices()));

        String presId = results.getPatient().getPATIENT_PRES_ID();
        if(presId == null || "".equals(presId)){
            presId = CommonUtil.getPresIdWithTime(results.getPatient().getID());
        }else{
            presId = CommonUtil.getPresIdWithTime(presId);
        }
        int warnLevel = results.getHIGHEST_WARNING_LEVEL();

        //缓存审核结果,等到进入审核结果页面时再读取记录显示
        if (warnLevel != 0) {
            CheckResultCache.putCheckResult(presId, results);
        }

        //处理审核信息
        CheckMessage message = new CheckMessage();
        message.setPresId(presId);
        message.setHasProblem(warnLevel == 0 ? 0 : 1);

        return message;
    }

    public String toJson(CheckResults checkResults) {
        Gson gson = new Gson();
        return gson.toJson(checkResults);
    }

    public CheckResults findCheckResult(String presId) {
        return CheckResultCache.findCheckResult(presId);
    }

    public CheckResults removeCheckResult(String presId) {
        return CheckResultCache.removeCheckResult(presId);
    }

    public void putRetValue(String presId, int result) {
        RetValCache.putRetVal(presId, result);
    }

    /**
     * 如果该id没有信息，则返回-2。
     * 下一步返回0
     * 返回修改返回-1
     *
     * @param presId
     * @return
     */
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

    public String checkPresc(String presId) {
        String url = checkServerUrl + "?tag=3";
//        String presIdWithTime = CommonUtil.getPresIdWithTime(presId);
        String data = cacheService.getXMLFromCache(presId, presId);
        HttpUtil.sendPost(url, data);
        return presId;
    }

    public List<Advice> sortCheckResult(List<Advice> advices){
        List<Advice> newList0 = new ArrayList<Advice>();
        List<Advice> newList1 = new ArrayList<Advice>();
        List<Advice> newList2 = new ArrayList<Advice>();
        List<Advice> newList3 = new ArrayList<Advice>();
        if(advices == null || advices.size() <= 1){
            return advices;
        }
        for (Advice advice : advices) {
            List<CheckInfo> checkInfos = advice.getCheckInfoList();
            if(getHighestLevelFromCheckInfoList(checkInfos) == 3){
                newList3.add(advice);
            }else if(getHighestLevelFromCheckInfoList(checkInfos) == 2){
                newList2.add(advice);
            }else if(getHighestLevelFromCheckInfoList(checkInfos) == 1){
                newList1.add(advice);
            }else if(getHighestLevelFromCheckInfoList(checkInfos) == 0){
                newList0.add(advice);
            }
        }

        List<Advice> finalList = new ArrayList<Advice>();

        finalList = sortHelp(newList3,finalList);
        finalList = sortHelp(newList2,finalList);
        finalList = sortHelp(newList1,finalList);
        finalList = sortHelp(newList0,finalList);

        return finalList;

    }

    //辅助方法，避免重复代码
    public List<Advice> sortHelp(List<Advice> list,List<Advice> finalList){
        if("1".equals(groupFlag)){
            list = sortSameLevelGroupId(list);
        }
        else if("2".equals(groupFlag)) {
            list = sortSameLevelOrderNo(list);
        }
        for(Advice advice:list){
            finalList.add(advice);
        }
        return finalList;
    }

    //对同一级别的问题按照group_id排序
    public List<Advice> sortSameLevelGroupId(List<Advice> newList){
        //finalList存放：按照group_id从小到到排列
        List<Advice> finalList = new ArrayList<Advice>();

        //min存放：group_id一轮的最小值
        long min = -1;
        for(int i=0;i<newList.size();++i){
            for(int j=0;j<newList.size();++j){
                Advice advice = newList.get(j);
                if(advice.isGroup_id_flag() == true){
                    continue;
                }
                String n_group_id = advice.getGROUP_ID().replaceAll("[^\\d]+", "");
                if(min == -1 || Long.parseLong(n_group_id)< min){
                    min = Long.parseLong(n_group_id);
                }
            }

            //tempList存放：group_id等于这次遍历最小值的处方
            List<Advice> tempList = new ArrayList<Advice>();
            for(int k=0;k<newList.size();++k){
                Advice advice = newList.get(k);
                String n_group_id = advice.getGROUP_ID().replaceAll("[^\\d]+", "");
                if(Long.parseLong(n_group_id) == min){
                    advice.setGroup_id_flag(true);
                    tempList.add(advice);
                }
            }

            //将tempList中的处方加入到finalList中
            int x = 0;
            for(int l=0;l<tempList.size();++l){
                Advice advice = tempList.get(l);
                if(tempList.size() > 1){
                    if(x == 0){
                        advice.setKh("┍ ");
                    }
                    else if(x == tempList.size()-1){
                        advice.setKh("┕ ");
                    }
                    else{
                        advice.setKh("");
                    }
                }else{
                    advice.setKh("");
                }
                ++x;
                finalList.add(advice);
            }
            min = -1;
        }
        return finalList;
    }

    //对同一级别的问题按照order_no排序
    public List<Advice> sortSameLevelOrderNo(List<Advice> newList){
        //finalList存放：按照order_no从小到到排列,按照order_sub_no从小到大排列
        List<Advice> finalList = new ArrayList<Advice>();

        //min存放：order_no一轮的最小值
        long min = -1;
        for(int i=0;i<newList.size();++i){
            //本循环的目的：取到一轮遍历中order_no的最小值
            for(int j=0;j<newList.size();++j){
                Advice advice = newList.get(j);
                //order_no_flag为true表示已经有序加到finalList中，不需要再处理
                if(advice.isOrder_no_flag() == true){
                    continue;
                }

                //对order_no中包含非数字的处理，替换非数字为空格
                String n_order_no = advice.getORDER_NO().replaceAll("[^\\d]+", "");
                //取一轮遍历中order_no的最小值
                if(min == -1 || Long.parseLong(n_order_no) < min){
                    min = Long.parseLong(n_order_no);
                }
            }
            //tempList存放：order_no等于这次遍历最小值的处方
            List<Advice> tempList = new ArrayList<Advice>();
            for(int k=0;k<newList.size();++k){
                Advice advice = newList.get(k);
                String n_order_no = advice.getORDER_NO().replaceAll("[^\\d]+", "");

                if(Long.parseLong(n_order_no) == min){
                    advice.setOrder_no_flag(true);
                    tempList.add(advice);
                }
            }

            //min2存放：order_no等于这次遍历最小值处方中的order_sub_no最小值
            long min2 = -1;
            int x = 0;
            for(int m=0;m<tempList.size();++m){
                //本循环的目的：取到相同order_no的order_sub_no最小值
                for(int n=0;n<tempList.size();++n){
                    Advice advice = tempList.get(n);
                    //order_sub_no_flag为true表示已经有序加到finalList中，不需要再处理
                    if("".equals(advice.getORDER_SUB_NO())){
                        continue;
                    }
                    if(advice.isOrder_sub_no_flag() == true){
                        continue;
                    }
                    //取一轮遍历中order_sub_no的最小值
                    if(min2 == -1 || Long.parseLong(advice.getORDER_SUB_NO()) < min2){
                        min2 = Long.parseLong(advice.getORDER_SUB_NO());
                    }
                }

                //将order_sub_no等于最小值的处方加入到finalList中，并将order_sub_no_flag改为true表明已经处理过
                for(int l=0;l<tempList.size();++l){
                    Advice advice = tempList.get(l);
                    if(advice.isOrder_sub_no_flag() == true || "".equals(advice.getORDER_SUB_NO())){
                        continue;
                    }
                    if(Long.parseLong(advice.getORDER_SUB_NO()) == min2){
                        //给成组的处方加上左侧方括号
                        if(tempList.size() > 1){
                            if(x == 0){
                                advice.setKh("┍ ");
                            }
                            else if(x == tempList.size()-1){
                                advice.setKh("┕ ");
                            }
                            else{
                                advice.setKh("");
                            }
                        }else{
                            advice.setKh("");
                        }
                        ++x;
                        finalList.add(advice);
                        advice.setOrder_sub_no_flag(true);
                    }
                }

                //将order_sub_no为空并且还未加到finalList的处方加入到finalList
                for(int y=0;y<tempList.size();++y){
                    Advice advice = tempList.get(y);
                    if(advice.isOrder_sub_no_flag() == false) {
                        if ("".equals(advice.getORDER_SUB_NO())) {
                            //给成组的处方加上左侧方括号
                            if (tempList.size() > 1) {
                                if (x == 0) {
                                    advice.setKh("┍ ");
                                } else if (x == tempList.size() - 1) {
                                    advice.setKh("┕ ");
                                } else {
                                    advice.setKh("");
                                }
                            } else {
                                advice.setKh("");
                            }
                            ++x;
                            finalList.add(advice);
                            advice.setOrder_sub_no_flag(true);
                        }
                    }
                }
                //将min2改为-1，进行下一次寻找order_sub_no的最小值
                min2 = -1;
            }
            //将min改为-1，进行下一次寻找order_no的最小值
            min = -1;
        }

        return finalList;
    }

    public int getHighestLevelFromCheckInfoList(List<CheckInfo> checkInfos){
        int highestLevel = 0;
        for(CheckInfo checkInfo: checkInfos){
            if(highestLevel < Integer.parseInt(checkInfo.getREGULAR_WARNING_LEVEL())){
                highestLevel = Integer.parseInt(checkInfo.getREGULAR_WARNING_LEVEL());
            } else if(Integer.parseInt(checkInfo.getREGULAR_WARNING_LEVEL()) == -1 && highestLevel != 3){
                highestLevel = 3;
            }
        }
        return highestLevel;
    }

}
