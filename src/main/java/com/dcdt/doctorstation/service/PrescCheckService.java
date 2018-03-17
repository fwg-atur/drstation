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
        String checkJson = "{\"hasProblem\":\"1\",\"presId\":\"110\",\"resultJsonString\":\"resultJsonString\"}";
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
