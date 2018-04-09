package com.dcdt.doctorstation.service;

import com.dcdt.cache.CheckResultCache;
import com.dcdt.cache.RetValCache;
import com.dcdt.doctorstation.entity.CheckMessage;
import com.dcdt.doctorstation.entity.CheckResults;
import com.dcdt.utils.CommonUtil;
import com.dcdt.utils.HttpUtil;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by LiRong on 2017/6/20.
 */
@Service
public class PrescCheckService {
    @Value("${checkServerUrl}")
    private String checkServerUrl;

    private CacheService cacheService;

    private static final Logger logger = LoggerFactory.getLogger(PrescCheckService.class);

    /**
     * @param tag
     * @param data
     * @return 返回审核结果标识（flag,presId）,不包括审核结果字符串
     */
    public CheckMessage checkPresc(int tag, String data) {
        String url = checkServerUrl + "?tag=" + tag;
        String checkJson = HttpUtil.sendPost(url, data);
//        String checkJson = getTestJson();
        logger.info(checkJson);
        if (tag == 2) return new CheckMessage();

        CheckMessage message = handleCheckJson(checkJson);
        putXML2Cache(message.getPresId(), data);
        return message;
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
                "{\"DIAGNOSISES\":\"恶性贫血\"},\"advices\":[{\"checkInfoList\":[{\"COLOR\":\"黄色\",\"NAME\":\"用法用量\",\"WARNING_LEVEL\":\"慎用\",\n" +
                "\"WARNING_INFO\":\"疗程超限。药品疗程超过天。处方的疗程为1天。科室编码：10001。科室名称：\",\"REF_SOURCE\":\"药品说明书及医院相关规定\",\n" +
                "\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1}],\"REPEAT\":\"\",\"DRUG_LO_NAME\":\"10%氯化钠注射液\",\"DRUG_LO_ID\":\"280154007\",\n" +
                "\"ADMINISTRATION\":\"ivdrip\",\"DOSAGE\":\"10\",\"DOSAGE_UNIT\":\"ml\",\"FREQ_COUNT\":\"qd\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20180121\",\"END_DAY\":\"\",\n" +
                "\"DEPT_CODE\":\"10001\",\"DOCTOR_NAME\":\"科大6\",\"ORDER_NO\":\"1\",\"ORDER_SUB_NO\":\"1\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"其他\",\"GROUP_ID\":\"1\",\n" +
                "\"USER_ID\":\"kd6\",\"PRES_ID\":\"499263\",\"PRES_DATE\":\"20180121\",\"PRES_SEQ_ID\":\"499263\",\"PK_ORDER_NO\":\"1114774\",\"COURSE\":\"1\",\"PKG_COUNT\":\"2\",\"PKG_UNIT\":\n" +
                "\"支\",\"BAK_01\":\"\",\"BAK_02\":\"\",\"BAK_03\":\"注射剂\",\"BAK_04\":\"10ml\",\"BAK_05\":\"国药容生\",\"HIGHEST_WARNING_LEVEL\":1},\n" +
                "{\"checkInfoList\":[{\"COLOR\":\"黄色\",\"NAME\":\"用法用量\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"疗程超限。药品疗程超过天。处方的疗程为4天。\n" +
                "科室编码：10001。科室名称：\",\"REF_SOURCE\":\"药品说明书及医院相关规定\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\n" +
                "\"REGULAR_WARNING_LEVEL\":1}],\"REPEAT\":\"\",\"DRUG_LO_NAME\":\"氨茶碱片\",\"DRUG_LO_ID\":\"230131005\",\"ADMINISTRATION\":\"口服\",\"DOSAGE\":\"0.1\",\n" +
                "\"DOSAGE_UNIT\":\"g\",\"FREQ_COUNT\":\"tid\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20180121\",\"END_DAY\":\"\",\n" +
                "\"DEPT_CODE\":\"10001\",\"DOCTOR_NAME\":\"科大6\",\"ORDER_NO\":\"2\",\"ORDER_SUB_NO\":\"1\",\"AUTHORITY_LEVELS\":\"\",\n" +
                "\"ALERT_LEVELS\":\"\",\"TITLE\":\"其他\",\"GROUP_ID\":\"2\",\"USER_ID\":\"kd6\",\"PRES_ID\":\"499263\",\"PRES_DATE\":\"20180121\",\"PRES_SEQ_ID\":\"499263\",\n" +
                "\"PK_ORDER_NO\":\"1114775\",\"COURSE\":\"4\",\"PKG_COUNT\":\"10\",\"PKG_UNIT\":\"片\",\"BAK_01\":\"\",\"BAK_02\":\"\",\"BAK_03\":\"注射剂\",\"BAK_04\":\"0.1g\",\n" +
                "\"BAK_05\":\"山东新华\",\"HIGHEST_WARNING_LEVEL\":1},\n" +
                "{\"checkInfoList\":[{\"COLOR\":\"黄色\",\"NAME\":\"用法用量\",\"WARNING_LEVEL\":\"慎用\",\n" +
                "\"WARNING_INFO\":\"疗程超限。药品疗程超过天。处方的疗程为20天。科室编码：10001。科室名称：\",\"REF_SOURCE\":\n" +
                "\"药品说明书及医院相关规定\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1}],\n" +
                "\"REPEAT\":\"\",\"DRUG_LO_NAME\":\"阿法骨化醇片\",\"DRUG_LO_ID\":\"160131015\",\"ADMINISTRATION\":\"口服\",\"DOSAGE\":\"0.5\",\"DOSAGE_UNIT\":\"ug\",\"FREQ_COUNT\":\"qd\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20180121\",\"END_DAY\":\"\",\"DEPT_CODE\":\"10001\",\"DOCTOR_NAME\":\"科大6\",\"ORDER_NO\":\"3\",\"ORDER_SUB_NO\":\"1\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"其他\",\"GROUP_ID\":\"3\",\"USER_ID\":\"kd6\",\"PRES_ID\":\"499263\",\"PRES_DATE\":\"20180121\",\"PRES_SEQ_ID\":\"499263\",\"PK_ORDER_NO\":\"1114776\",\"COURSE\":\"20\",\"PKG_COUNT\":\"2\",\"PKG_UNIT\":\"盒\",\"BAK_01\":\"\",\"BAK_02\":\"\",\"BAK_03\":\"注射剂\",\"BAK_04\":\"0.5ug\",\"BAK_05\":\"重庆药友\",\"HIGHEST_WARNING_LEVEL\":1}],\"checkInfoMap\":{\"studio.atur.api.persistence.presc_stat.PrescInfoKey@399cbc89\":[{\"COLOR\":\"黄色\",\"NAME\":\"用法用量\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"疗程超限。药品疗程超过天。处方的疗程为1天。科室编码：10001。科室名称：\",\"REF_SOURCE\":\"药品说明书及医院相关规定\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1}],\"studio.atur.api.persistence.presc_stat.PrescInfoKey@715e8b1c\":[{\"COLOR\":\"黄色\",\"NAME\":\"用法用量\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"疗程超限。药品疗程超过天。处方的疗程为20天。科室编码：10001。科室名称：\",\"REF_SOURCE\":\"药品说明书及医院相关规定\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1}],\"studio.atur.api.persistence.presc_stat.PrescInfoKey@611de3eb\":[{\"COLOR\":\"黄色\",\"NAME\":\"用法用量\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"疗程超限。药品疗程超过天。处方的疗程为4天。科室编码：10001。科室名称：\",\"REF_SOURCE\":\"药品说明书及医院相关规定\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1}]},\"output\":\"<CheckOutput AntiDrugPos=\\\"\\\">\\n    <PresInfo ORDER_ID=\\\"1\\\" ORDER_SUB_ID=\\\"1\\\" DRUG_LO_ID=\\\"280154007\\\" DRUG_LO_NAME=\\\"10%氯化钠注射液\\\">\\n        <CheckInfo COLOR=\\\"黄色\\\" NAME=\\\"用法用量\\\" WARNING_LEVEL=\\\"慎用\\\" WARNING_INFO=\\\"疗程超限。药品疗程超过天。处方的疗程为1天。科室编码：10001。科室名称：\\\" REF_SOURCE=\\\"药品说明书及医院相关规定\\\" YPMC=\\\"\\\" JSXX=\\\"\\\" ZYJL=\\\"\\\" TYSM=\\\"\\\" LCSY=\\\"\\\" />\\n    </PresInfo>\\n    <PresInfo ORDER_ID=\\\"2\\\" ORDER_SUB_ID=\\\"1\\\" DRUG_LO_ID=\\\"230131005\\\" DRUG_LO_NAME=\\\"氨茶碱片\\\">\\n        <CheckInfo COLOR=\\\"黄色\\\" NAME=\\\"用法用量\\\" WARNING_LEVEL=\\\"慎用\\\" WARNING_INFO=\\\"疗程超限。药品疗程超过天。处方的疗程为4天。科室编码：10001。科室名称：\\\" REF_SOURCE=\\\"药品说明书及医院相关规定\\\" YPMC=\\\"\\\" JSXX=\\\"\\\" ZYJL=\\\"\\\" TYSM=\\\"\\\" LCSY=\\\"\\\" />\\n    </PresInfo>\\n    <PresInfo ORDER_ID=\\\"3\\\" ORDER_SUB_ID=\\\"1\\\" DRUG_LO_ID=\\\"160131015\\\" DRUG_LO_NAME=\\\"阿法骨化醇片\\\">\\n        <CheckInfo COLOR=\\\"黄色\\\" NAME=\\\"用法用量\\\" WARNING_LEVEL=\\\"慎用\\\" WARNING_INFO=\\\"疗程超限。药品疗程超过天。处方的疗程为20天。科室编码：10001。科室名称：\\\" REF_SOURCE=\\\"药品说明书及医院相关规定\\\" YPMC=\\\"\\\" JSXX=\\\"\\\" ZYJL=\\\"\\\" TYSM=\\\"\\\" LCSY=\\\"\\\" />\\n    </PresInfo>\\n</CheckOutput>\\n\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\",\"HIGHEST_WARNING_LEVEL\":1,\"WARNING_COUNT\":3,\"TAG\":\"1\"}";
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

        String presId = results.getPatient().getPATIENT_PRES_ID();
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
        String presIdWithTime = CommonUtil.getPresIdWithTime(presId);
        String data = cacheService.getXMLFromCache(presId, presIdWithTime);
        HttpUtil.sendPost(url, data);
        return presIdWithTime;
    }
}
