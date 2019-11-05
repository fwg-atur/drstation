package com.dcdt.doctorstation.service;

import com.dcdt.cache.CheckResultCache;
import com.dcdt.cache.RetValCache;
import com.dcdt.doctorstation.entity.*;
import com.dcdt.utils.CommonUtil;
import com.dcdt.utils.HttpUtil;
import com.dcdt.utils.ParseXML;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

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
    private ParseXML parseXML = new ParseXML();


    private static final Logger logger = Logger.getLogger(PrescCheckService.class);

    /**
     * @param tag
     * @param data
     * @return 返回审核结果标识（flag,presId）,不包括审核结果字符串
     */
    public CheckMessage checkPresc(int tag, String data) {
        CheckMessage checkMessage = new CheckMessage();
        String url = checkServerUrl + "?tag=" + tag;
        if(!parseXML.filter(data)){
            return checkMessage;
        }
        data = data.replace("&nbsp;"," ");
        String checkJson = "";
        checkJson = HttpUtil.sendPost(url, data);
//        checkJson = getTestJson();
        logger.debug(checkJson);

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


    public CheckMessage_BZ checkPresc_BZ(int tag, String data) {
        CheckMessage_BZ checkMessage_bz = new CheckMessage_BZ();
        String url = checkServerUrl + "?tag=" + tag;
        if(!parseXML.filter(data)){
            return checkMessage_bz;
        }
        data = data.replace("&nbsp;"," ");
        String checkJson = "";
        checkJson = HttpUtil.sendPost(url, data);
//        checkJson = getTestJson();
        logger.debug(checkJson);



//        if(checkJson == null || checkJson.equals("")){
//            checkMessage.setHasProblem(-2);
//            return checkMessage;
//        }
//
//        if (tag == 2) return checkMessage;

        checkMessage_bz = handleCheckJson_bz(checkJson);
//        if(checkMessage.getHasProblem() == -2){
//            return checkMessage;
//        }
        putXML2Cache(checkMessage_bz.getPresId(), data);
        return checkMessage_bz;
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
    public CheckMessage checkPrescForTest(int tag, String data) throws IOException {

        String checkJson = "<Check>\n" +
                "<CheckInput TAG=\"2\" >\n" +
                "\t<Doctor POSITION=\"234\" NAME=\"孙加超\" DEPT_CODE=\"急诊科[住院]\" DEPT_NAME=\"\" USER_ID=\"0499\"/>\n" +
                "\t<Patient NAME=\"喻唐古\" ID=\"0000646961\" GENDER=\"男\" BIRTH=\"19790316\" WEIGHT=\"\" HEIGHT=\"\" ALERGY_DRUGS=\"\" PREGNANT=\"否\" LACT=\"否\" HEPATICAL=\"\" RENAL=\"\" PANCREAS=\"\" VISIT_ID=\"\" PATIENT_PRES_ID=\"95\" IDENTITY_TYPE=\"\" FEE_TYPE=\"自费\" SCR=\"\" SCR_UNIT=\"\" GESTATION_AGE=\"\" PRETERM_BIRTH=\"\" DRUG_HISTORY=\"\" FAMILY_DISEASE_HISTORY=\"\" GENETIC_DISEASE=\"\" MEDICARE_01=\"\" MEDICARE_02=\"\" MEDICARE_03=\"\" MEDICARE_04=\"\" MEDICARE_05=\"\" WARD_CODE=\"\" WARD_NAME=\"\" BED_NO=\"\" INPATIENT_NO=\"\"/>\n" +
                "\t<Diagnosises DIAGNOSISES=\"不适\"/>\n" +
                "\t<Advices>\n" +
                "\t\t<Advice REPEAT=\"1\" DRUG_LO_NAME=\"阿斯匹林肠溶片[0.1g*30片](拜阿司匹灵)\" DRUG_LO_ID=\"XY000004\" ADMINISTRATION=\"外用\" DOSAGE=\"0.1\" DOSAGE_UNIT=\"g\" FREQ_COUNT=\"Qd\" FREQ_INTERVAL=\"\" FREQ_INTERVAL_UNIT=\"\" START_DAY=\"20190121\" END_DAY=\"\" DEPT_CODE=\"急诊科[住院]\" DOCTOR_NAME=\"孙加超\" ORDER_NO=\"I1901210000859\" ORDER_SUB_NO=\"1\" AUTHORITY_LEVELS=\"\" ALERT_LEVELS=\"\" TITLE=\"234\" GROUP_ID=\"I1901210000859\" USER_ID=\"0499\" PRES_ID=\"I190121000085\" PRES_DATE=\"20190121\" PRES_SEQ_ID=\"I190121000085\" PK_ORDER_NO=\"\" COURSE=\"1\" PKG_COUNT=\"\" PKG_UNIT=\"盒(30)\" BAK_01=\"\" BAK_02=\"否\" BAK_03=\"肠溶衣片\" BAK_04=\"0.1g*30片\" BAK_05=\"\" PERFORM_SCHEDULE=\"\"/>\n" +
                "\t</Advices>\n" +
                "</CheckInput>\n" +
                "\n" +
                "<CheckOutput AntiDrugPos=\"\">\n" +
                "    <PresInfo ORDER_ID=\"I1901210000859\" ORDER_SUB_ID=\"1\" DRUG_LO_ID=\"XY000004\" DRUG_LO_NAME=\"阿斯匹林肠溶片[0.1g*30片](拜阿司匹灵)\" />\n" +
                "</CheckOutput>\n" +
                "\n" +
                "</Check>";
        String path = "C:\\Users\\wtwang\\Desktop\\test.txt";
        File file = new File(path);
        StringBuilder sb = new StringBuilder("");
        try {
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(file));
            BufferedReader br = new BufferedReader(reader);

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String testJson = sb.toString();
        return handleCheckJson(testJson);
    }

    public String getTestJson() {
        String res =
                "{\"doctor\":{\"USER_ID\":\"530663\",\"NAME\":\"陈双坤\",\"POSITION\":\"医师\",\"DEPT_CODE\":\"40302\",\"DEPT_NAME\":\"骨科\"},\"patient\":{\"lisValueList\":[],\"NAME\":\"李玟金\",\"ID\":\"1\",\"GENDER\":\"女\",\"BIRTH\":\"20190306\",\"WEIGHT\":\"\",\"HEIGHT\":\"\",\"ALERGY_DRUGS\":\"\",\"PREGNANT\":\"否\",\"LACT\":\"否\",\"HEPATICAL\":\"\",\"RENAL\":\"\",\"PANCREAS\":\"\",\"VISIT_ID\":\"2\",\"PATIENT_PRES_ID\":\"C0000740776001\",\"IDENTITY_TYPE\":\"市医疗保险\",\"FEE_TYPE\":\"异地医保\",\"SCR\":\"\",\"SCR_UNIT\":\"\",\"GESTATION_AGE\":\"\",\"PRETERM_BIRTH\":\"\",\"DRUG_HISTORY\":\"\",\"FAMILY_DISEASE_HISTORY\":\"\",\"GENETIC_DISEASE\":\"\",\"MEDICARE_01\":\"\",\"MEDICARE_02\":\"\",\"MEDICARE_03\":\"\",\"MEDICARE_04\":\"\",\"MEDICARE_05\":\"\",\"WARD_CODE\":\"\",\"WARD_NAME\":\"\",\"BED_NO\":\"\",\"INPATIENT_NO\":\"2\",\"PRE_OPERATION_NAME\":\"\",\"PRE_OPERATION_CODE\":\"\",\"OPERATION_NAME\":\"\",\"OPERATION_CODE\":\"\",\"ANAESTHESIA_NAME\":\"\",\"ANAESTHESIA_CODE\":\"\",\"CHIEF_COMPLAINT\":\"\",\"PRESENT_ILLNESS_HISTORY\":\"\",\"PERSONAL_HISTORY\":\"\",\"PHYSICAL_EXAM\":\"\",\"AUXILIARY_EXAM\":\"\",\"SUGGESTION\":\"\"},\"diagnosis\":{\"DIAGNOSISES\":\"行动不便、病重垂危\"},\"advices\":[{\"checkInfoList\":[{\"warningLevelVirtual\":0,\"COLOR\":\"红色\",\"NAME\":\"用法用量\",\"WARNING_LEVEL\":\"禁用\",\"WARNING_INFO\":\"溶媒选择不适宜。相容：NS、D5W、D5NS、D5LR注射用苯磺顺阿曲库铵在乳酸林格氏注射液中出现降解产物的速度较快，不推荐乳酸林格氏注射液作为本品的稀释液。\",\"REF_SOURCE\":\"Micromedex注射用苯磺顺阿曲库铵说明书-浙江仙琚制药股份有限公司\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":2}],\"REPEAT\":\"5\",\"DRUG_LO_NAME\":\"苯磺顺阿曲库铵粉针\",\"DRUG_LO_ID\":\"768100\",\"ADMINISTRATION\":\"静脉推注\",\"DOSAGE\":\"5.000\",\"DOSAGE_UNIT\":\"mg\",\"FREQ_COUNT\":\"once\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20191017\",\"END_DAY\":\"\",\"DEPT_CODE\":\"40300\",\"DOCTOR_NAME\":\"黄毅婷\",\"ORDER_NO\":\"19756750\",\"ORDER_SUB_NO\":\"1\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"主治医师\",\"GROUP_ID\":\"3\",\"USER_ID\":\"259807\",\"PRES_ID\":\"0000740776_2019:10:22:22:40:55\",\"PRES_DATE\":\"20191017\",\"PRES_SEQ_ID\":\"0000740776001\",\"PK_ORDER_NO\":\"19756750001\",\"COURSE\":\"1\",\"PKG_COUNT\":\"\",\"PKG_UNIT\":\"\",\"BAK_01\":\"\",\"BAK_02\":\"\",\"BAK_03\":\"注射剂\",\"BAK_04\":\"5mg*10瓶/盒\",\"BAK_05\":\"浙江仙琚制药厂\",\"HIGHEST_WARNING_LEVEL\":2,\"PERFORM_SCHEDULE\":\"\",\"TIME\":\"\",\"ANTI_DRUG_REGISTER\":\"\",\"SKIN_TEST\":\"\",\"SKIN_TEST_RESULT\":\"\",\"INSTRUCT_DRUG_SPEED\":\"\",\"INSTRUCT_DRUG_SPEED_UNIT\":\"\",\"DRUG_REMARK\":\"\"},{\"checkInfoList\":[{\"warningLevelVirtual\":0,\"COLOR\":\"黄色\",\"NAME\":\"医院管理\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"药控科室定量审核。科室最大定量：     10.00，科室已用：      0.00，当前处方用量：61盒\",\"REF_SOURCE\":\"医院药控相关规定。sd as\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1}],\"REPEAT\":\"5\",\"DRUG_LO_NAME\":\"乳康丸\",\"DRUG_LO_ID\":\"3012307PL0\",\"ADMINISTRATION\":\"口服\",\"DOSAGE\":\"0.65\",\"DOSAGE_UNIT\":\"g\",\"FREQ_COUNT\":\"once\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20191017\",\"END_DAY\":\"\",\"DEPT_CODE\":\"40301\",\"DOCTOR_NAME\":\"黄毅婷\",\"ORDER_NO\":\"19756750\",\"ORDER_SUB_NO\":\"1\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"主治医师\",\"GROUP_ID\":\"3\",\"USER_ID\":\"259807\",\"PRES_ID\":\"0000740776_2019:10:22:22:40:55\",\"PRES_DATE\":\"20191017\",\"PRES_SEQ_ID\":\"0000740776001\",\"PK_ORDER_NO\":\"19756750001\",\"COURSE\":\"20\",\"PKG_COUNT\":\"61\",\"PKG_UNIT\":\"盒\",\"BAK_01\":\"\",\"BAK_02\":\"\",\"BAK_03\":\"丸剂\",\"BAK_04\":\"1丸\",\"BAK_05\":\"吉林吉尔吉\",\"HIGHEST_WARNING_LEVEL\":1,\"PERFORM_SCHEDULE\":\"\",\"TIME\":\"\",\"ANTI_DRUG_REGISTER\":\"\",\"SKIN_TEST\":\"\",\"SKIN_TEST_RESULT\":\"\",\"INSTRUCT_DRUG_SPEED\":\"\",\"INSTRUCT_DRUG_SPEED_UNIT\":\"\",\"DRUG_REMARK\":\"\"},{\"checkInfoList\":[],\"REPEAT\":\"5\",\"DRUG_LO_NAME\":\"咪达唑仑针(力月西)\",\"DRUG_LO_ID\":\"301059\",\"ADMINISTRATION\":\"静脉推注\",\"DOSAGE\":\"10.000\",\"DOSAGE_UNIT\":\"mg\",\"FREQ_COUNT\":\"s.t.\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20191017\",\"END_DAY\":\"\",\"DEPT_CODE\":\"40300\",\"DOCTOR_NAME\":\"黄毅婷\",\"ORDER_NO\":\"19756749\",\"ORDER_SUB_NO\":\"1\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"主治医师\",\"GROUP_ID\":\"2\",\"USER_ID\":\"259807\",\"PRES_ID\":\"0000740776_2019:10:22:22:40:55\",\"PRES_DATE\":\"20191017\",\"PRES_SEQ_ID\":\"0000740776001\",\"PK_ORDER_NO\":\"19756749001\",\"COURSE\":\"1\",\"PKG_COUNT\":\"\",\"PKG_UNIT\":\"\",\"BAK_01\":\"\",\"BAK_02\":\"否\",\"BAK_03\":\"注射剂\",\"BAK_04\":\"10mg:2ml/支\",\"BAK_05\":\"江苏恩华制药\",\"HIGHEST_WARNING_LEVEL\":0,\"PERFORM_SCHEDULE\":\"\",\"TIME\":\"\",\"ANTI_DRUG_REGISTER\":\"\",\"SKIN_TEST\":\"\",\"SKIN_TEST_RESULT\":\"\",\"INSTRUCT_DRUG_SPEED\":\"\",\"INSTRUCT_DRUG_SPEED_UNIT\":\"\",\"DRUG_REMARK\":\"\"},{\"checkInfoList\":[],\"REPEAT\":\"5\",\"DRUG_LO_NAME\":\"对乙酰氨基酚缓释片\",\"DRUG_LO_ID\":\"20041901\",\"ADMINISTRATION\":\"静脉推注\",\"DOSAGE\":\"0.65\",\"DOSAGE_UNIT\":\"g\",\"FREQ_COUNT\":\"once\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20191017\",\"END_DAY\":\"\",\"DEPT_CODE\":\"40300\",\"DOCTOR_NAME\":\"黄毅婷\",\"ORDER_NO\":\"19756750\",\"ORDER_SUB_NO\":\"1\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"主治医师\",\"GROUP_ID\":\"3\",\"USER_ID\":\"259807\",\"PRES_ID\":\"0000740776_2019:10:22:22:40:55\",\"PRES_DATE\":\"20191017\",\"PRES_SEQ_ID\":\"0000740776001\",\"PK_ORDER_NO\":\"19756750001\",\"COURSE\":\"20\",\"PKG_COUNT\":\"1\",\"PKG_UNIT\":\"盒\",\"BAK_01\":\"\",\"BAK_02\":\"\",\"BAK_03\":\"缓释片\",\"BAK_04\":\"0.65g x18粒/盒\",\"BAK_05\":\"\",\"HIGHEST_WARNING_LEVEL\":0,\"PERFORM_SCHEDULE\":\"\",\"TIME\":\"\",\"ANTI_DRUG_REGISTER\":\"\",\"SKIN_TEST\":\"\",\"SKIN_TEST_RESULT\":\"\",\"INSTRUCT_DRUG_SPEED\":\"\",\"INSTRUCT_DRUG_SPEED_UNIT\":\"\",\"DRUG_REMARK\":\"\"}],\"checkInfoMap\":{\"studio.atur.api.persistence.presc_stat.PrescInfoKey@d527971b\":[{\"warningLevelVirtual\":0,\"COLOR\":\"红色\",\"NAME\":\"用法用量\",\"WARNING_LEVEL\":\"禁用\",\"WARNING_INFO\":\"溶媒选择不适宜。相容：NS、D5W、D5NS、D5LR注射用苯磺顺阿曲库铵在乳酸林格氏注射液中出现降解产物的速度较快，不推荐乳酸林格氏注射液作为本品的稀释液。\",\"REF_SOURCE\":\"Micromedex注射用苯磺顺阿曲库铵说明书-浙江仙琚制药股份有限公司\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":2}],\"studio.atur.api.persistence.presc_stat.PrescInfoKey@cc7b256c\":[{\"warningLevelVirtual\":0,\"COLOR\":\"黄色\",\"NAME\":\"医院管理\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"药控科室定量审核。科室最大定量：     10.00，科室已用：      0.00，当前处方用量：61盒\",\"REF_SOURCE\":\"医院药控相关规定。sd as\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1}],\"studio.atur.api.persistence.presc_stat.PrescInfoKey@fae196bd\":[],\"studio.atur.api.persistence.presc_stat.PrescInfoKey@dda309f1\":[]},\"output\":\"\",\"presDate\":null,\"presID\":null,\"highestWarningLevelVirtual\":0,\"HIGHEST_WARNING_LEVEL\":2,\"WARNING_COUNT\":2,\"TAG\":\"1\"}";
        return res;
    }

/*
    public String getTestJson(){
        String res = "{\n" +
                "    \"doctor\": {\n" +
                "        \"USER_ID\": \"1150\",\n" +
                "        \"NAME\": \"徐杰\",\n" +
                "        \"POSITION\": \"主治医师\",\n" +
                "        \"DEPT_CODE\": \"重症医学科\",\n" +
                "        \"DEPT_NAME\": \"重症医学科\"\n" +
                "    },\n" +
                "    \"patient\": {\n" +
                "        \"NAME\": \"柳巨武\",\n" +
                "        \"ID\": \"0000689061\",\n" +
                "        \"GENDER\": \"男\",\n" +
                "        \"BIRTH\": \"19630413\",\n" +
                "        \"WEIGHT\": \"\",\n" +
                "        \"HEIGHT\": \"\",\n" +
                "        \"ALERGY_DRUGS\": \"\",\n" +
                "        \"PREGNANT\": \"否\",\n" +
                "        \"LACT\": \"否\",\n" +
                "        \"HEPATICAL\": \"\",\n" +
                "        \"RENAL\": \"\",\n" +
                "        \"PANCREAS\": \"\",\n" +
                "        \"VISIT_ID\": \"\",\n" +
                "        \"PATIENT_PRES_ID\": \"77704\",\n" +
                "        \"IDENTITY_TYPE\": \"\",\n" +
                "        \"FEE_TYPE\": \"自费\",\n" +
                "        \"SCR\": \"\",\n" +
                "        \"SCR_UNIT\": \"\",\n" +
                "        \"GESTATION_AGE\": \"\",\n" +
                "        \"PRETERM_BIRTH\": \"\",\n" +
                "        \"DRUG_HISTORY\": \"\",\n" +
                "        \"FAMILY_DISEASE_HISTORY\": \"\",\n" +
                "        \"GENETIC_DISEASE\": \"\",\n" +
                "        \"MEDICARE_01\": \"\",\n" +
                "        \"MEDICARE_02\": \"\",\n" +
                "        \"MEDICARE_03\": \"\",\n" +
                "        \"MEDICARE_04\": \"\",\n" +
                "        \"MEDICARE_05\": \"\",\n" +
                "        \"WARD_CODE\": \"\",\n" +
                "        \"WARD_NAME\": \"\",\n" +
                "        \"BED_NO\": \"\",\n" +
                "        \"INPATIENT_NO\": \"\"\n" +
                "    },\n" +
                "    \"diagnosis\": {\n" +
                "        \"DIAGNOSISES\": \"创伤性蛛网膜下腔出血\"\n" +
                "    },\n" +
                "    \"advices\": [],\n" +
                "    \"checkInfoMap\": null,\n" +
                "    \"output\": \"\",\n" +
                "    \"presDate\": null,\n" +
                "    \"presID\": null,\n" +
                "    \"highestWarningLevelVirtual\": 0,\n" +
                "    \"HIGHEST_WARNING_LEVEL\": 0,\n" +
                "    \"WARNING_COUNT\": 0,\n" +
                "    \"TAG\": \"1\"\n" +
                "}";
        return res;
    }
*/
    /**
     * @param checkJson
     */
    protected CheckMessage handleCheckJson(String checkJson) {
        Gson g = new Gson();
        CheckResults results = g.fromJson(checkJson, CheckResults.class);
        String presId = results.getPatient().getPATIENT_PRES_ID();
        if(presId == null || "".equals(presId)){
            presId = CommonUtil.getPresIdWithTime(results.getPatient().getID());
        }else{
            presId = CommonUtil.getPresIdWithTime(presId);
        }

        if(results.getCheckInfoMap() == null){
            CheckMessage message = new CheckMessage();
            if(results.getAdvices() != null && results.getAdvices().size() != 0) {
                message.setHasProblem(-2);
            }else{
                message.setPresId(presId);
                message.setHasProblem(0);
            }
            return message;
        }
        results.setAdvices(sortgroupAdvice(results.getAdvices(),groupFlag));


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


    protected CheckMessage_BZ handleCheckJson_bz(String checkJson) {
        Gson g = new Gson();
        CheckResults results = g.fromJson(checkJson, CheckResults.class);
        String presId = results.getPatient().getPATIENT_PRES_ID();
        if(presId == null || "".equals(presId)){
            presId = CommonUtil.getPresIdWithTime(results.getPatient().getID());
        }else{
            presId = CommonUtil.getPresIdWithTime(presId);
        }

        if(results.getCheckInfoMap() == null){
            CheckMessage_BZ message = new CheckMessage_BZ();
            if(results.getAdvices() != null && results.getAdvices().size() != 0) {

            }else{
                message.setPresId(presId);
                message.setHasProblem(0);
                message.setState(1);
                message.setRetXml("<CheckResult STATE=\"0\" STYLE=\"\" CHECK_PHARMACIST_CODE=\"\" CHECK_PHARMACIST_NAME=\"\" CHECK_STATE=\"\" />");
                CheckResultCache.putCheckState(presId,message);
            }
            return message;
        }
        results.setAdvices(sortgroupAdvice(results.getAdvices(),groupFlag));


        int warnLevel = results.getHIGHEST_WARNING_LEVEL();

        //缓存审核结果,等到进入审核结果页面时再读取记录显示
        if (warnLevel != 0) {
            CheckResultCache.putCheckResult(presId, results);
        }

        //处理审核信息
        CheckMessage_BZ message = new CheckMessage_BZ();
        message.setPresId(presId);
        message.setHasProblem(warnLevel == 0 ? 0 : 1);
        if(warnLevel == 0){
            message.setState(1);
            message.setRetXml("<CheckResult STATE=\"0\" STYLE=\"\" CHECK_PHARMACIST_CODE=\"\" CHECK_PHARMACIST_NAME=\"\" CHECK_STATE=\"\" />");
        }
        CheckResultCache.putCheckState(presId,message);
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

    public void putRetCache_bz(String presId, CheckMessage_BZ message) {
        CheckResultCache.putCheckState(presId, message);
    }

    //从缓存中取出相应的CheckMessage
    public CheckMessage_BZ getRetCache_bz(String presId){
        return CheckResultCache.findCheckState(presId);
    }

    public void putRetValue_bz(String presId, String message) {
        RetValCache.putRetVal_bz(presId, message);
    }

    //根据presId获取缓存中的返回字符串
    public String findRetValue_bz(String presId){
        if(RetValCache.containsKey_bz(presId)){
            return RetValCache.removeRetVal_bz(presId);
        }else{
            String retXml = "<CheckResult STATE=\"-1\" STYLE=\"\" CHECK_PHARMACIST_CODE=\"\" CHECK_PHARMACIST_NAME=\"\" CHECK_STATE=\"\" />";
            return retXml;
        }
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
        logger.debug(url+" "+data);
        HttpUtil.sendPost(url, data);
        return presId;
    }


    @Deprecated
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
    @Deprecated
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
    @Deprecated
    public List<Advice> sortSameLevelGroupId(List<Advice> newList){
        //finalList存放：按照group_id从小到大排列
        List<Advice> finalList = new ArrayList<Advice>();

        //min存放：group_id一轮的最小值
        BigDecimal min = new BigDecimal(-1);
        for(int i=0;i<newList.size();++i){
            for(int j=0;j<newList.size();++j){
                Advice advice = newList.get(j);
                if(advice.isGroup_id_flag() == true){
                    continue;
                }
                if(advice.getGROUP_ID() == null || "".equals(advice.getGROUP_ID())){
                    advice.setGROUP_ID("0");
                }
                String n_group_id = advice.getGROUP_ID().replaceAll("[^\\d]+", "");
                if(min.equals(-1) || new BigDecimal(n_group_id).compareTo(min) == -1){
                    min = new BigDecimal(n_group_id);
                }
            }

            //tempList存放：group_id等于这次遍历最小值的处方
            List<Advice> tempList = new ArrayList<Advice>();
            for(int k=0;k<newList.size();++k){
                Advice advice = newList.get(k);
                String n_group_id = advice.getGROUP_ID().replaceAll("[^\\d]+", "");
                if(new BigDecimal(n_group_id).equals(min)){
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
            min = new BigDecimal(-1);
        }
        return finalList;
    }

    /**
     * add by wtwang @2019.01.16
     * group_id或者order_no相同为一组，同一组的医嘱在一起，计算组中最高的问题级别
     * 将原始问题医嘱列表按照group_id或者order_no成组分好
     * 并且成组的问题按照问题级别排好序
     * 各个组之间按照组的最高问题级别排好序
     * flag为1表示按照group_id分组，flag为2表示按照order_no分组
     * @param list
     * @return
     */
    public List<Advice> sortgroupAdvice(List<Advice> list,String flag){
        List<Advice> result = new ArrayList<Advice>();
        Map<BigDecimal,List<Advice>> map = new HashMap<BigDecimal, List<Advice>>();
        for(Advice advice : list){
            String s_id = "";
            // 区分按照group_id分组还是按照order_no分组
            if("1".equals(flag)){
                if(advice.getGROUP_ID() == null || "".equals(advice.getGROUP_ID())){
                    advice.setGROUP_ID("0");
                }
                s_id = advice.getGROUP_ID().replaceAll("[^\\d]+", "");
            }else if("2".equals(flag)){
                if(advice.getORDER_NO() == null || "".equals(advice.getORDER_NO())){
                    advice.setORDER_NO("0");
                }
                s_id = advice.getORDER_NO().replaceAll("[^\\d]+", "");
            }
            // 遍历list，将一组的医嘱存入同一个list中
            if(!map.containsKey(new BigDecimal(s_id))){
                List<Advice> tempList = new ArrayList<Advice>();
                tempList.add(advice);
                map.put(new BigDecimal(s_id),tempList);
            }else{
                map.get(new BigDecimal(s_id)).add(advice);
            }
        }

        // list_i表示成组医嘱中最该问题级别为i的集合
        List<List<Advice>> list_3 = new ArrayList<List<Advice>>();
        List<List<Advice>> list_2 = new ArrayList<List<Advice>>();
        List<List<Advice>> list_1 = new ArrayList<List<Advice>>();
        List<List<Advice>> list_0 = new ArrayList<List<Advice>>();
        // 遍历map中的成组医嘱，计算每组医嘱的最高问题级别
        for(List<Advice> tempList : map.values()){
            // 在此时将成组的医嘱按照问题级别从高到低排好序
            tempList = sortSameGroup(tempList);
            // 将同一组的医嘱用括号标识括起来
            tempList = handleSameGroup(tempList);

            int level = getHighestLevelFromAdviceList(tempList);
            // 根据最高问题级别将医嘱组加入到不同的list中
            switch (level){
                case 3:
                    list_3.add(tempList);
                    break;
                case 2:
                    list_2.add(tempList);
                    break;
                case 1:
                    list_1.add(tempList);
                    break;
                case 0:
                    list_0.add(tempList);
                    break;
                default:
                    break;
            }

        }

        // 将各个问题级别的医嘱组按照顺序加入到结果中
        for(List<Advice> list3 : list_3){
            for(Advice advice : list3){
                result.add(advice);
            }
        }
        for(List<Advice> list2 : list_2){
            for(Advice advice : list2){
                result.add(advice);
            }
        }
        for(List<Advice> list1 : list_1){
            for(Advice advice : list1){
                result.add(advice);
            }
        }
        for(List<Advice> list0 : list_0){
            for(Advice advice : list0){
                result.add(advice);
            }
        }


        return result;
    }

    /**
     * 将同组的医嘱用括号括起来
     * @param list
     * @return
     */
    public List<Advice> handleSameGroup(List<Advice> list){
        int x = 0;
        for(int l=0;l<list.size();++l){
            Advice advice = list.get(l);
            if(list.size() > 1){
                if(x == 0){
                    advice.setKh("┍ ");
                }
                else if(x == list.size()-1){
                    advice.setKh("┕ ");
                }
                else{
                    advice.setKh("");
                }
            }else{
                advice.setKh("");
            }
            ++x;
        }
        return list;
    }

    /**
     * add by wtwang @2019.01.17
     * 将同一分组的医嘱按照问题级别从高到低排序
     * @param list
     * @return
     */
    public List<Advice> sortSameGroup(List<Advice> list){
        List<Advice> result = new ArrayList<Advice>();
        Map<Integer,List<Advice>> map = new HashMap<Integer, List<Advice>>();

        // 遍历list，将问题级别相同医嘱放入同一个list中
        for(Advice advice : list){
            List<CheckInfo> checkInfos = advice.getCheckInfoList();
            int warning_level = getHighestLevelFromCheckInfoList(checkInfos);
            if(!map.containsKey(warning_level)){
                List<Advice> tempList = new ArrayList<Advice>();
                tempList.add(advice);
                map.put(warning_level,tempList);
            }else{
                map.get(warning_level).add(advice);
            }
        }

        // i从3到0表示问题级别，按照问题级别从高到低从map中取出医嘱加入到result中
        for(int i=3;i>=0;--i){
            List<Advice> tempList = map.get(i);
            if(tempList != null && tempList.size() != 0){
                for(Advice advice : tempList){
                    result.add(advice);
                }
            }
        }

        return result;
    }

    /**
     * add by wtwang @2019.01.17
     * 同一组的医嘱计算最高问题级别
     * @param list
     * @return
     */
    public int getHighestLevelFromAdviceList(List<Advice> list){
        int highestLevel = 0;
        for(Advice advice : list){
            List<CheckInfo> checkInfos = advice.getCheckInfoList();
            int tempLevel = getHighestLevelFromCheckInfoList(checkInfos);
            highestLevel = Math.max(highestLevel,tempLevel);
        }
        return highestLevel;
    }

    //对同一级别的问题按照order_no排序
    @Deprecated
    public List<Advice> sortSameLevelOrderNo(List<Advice> newList){
        //finalList存放：按照order_no从小到到排列,按照order_sub_no从小到大排列
        List<Advice> finalList = new ArrayList<Advice>();

        //min存放：order_no一轮的最小值
        BigDecimal min = new BigDecimal(-1);
        for(int i=0;i<newList.size();++i){
            //本循环的目的：取到一轮遍历中order_no的最小值
            for(int j=0;j<newList.size();++j){
                Advice advice = newList.get(j);
                //order_no_flag为true表示已经有序加到finalList中，不需要再处理
                if(advice.isOrder_no_flag() == true){
                    continue;
                }

                if(advice.getORDER_NO() == null || "".equals(advice.getORDER_NO())){
                    advice.setORDER_NO("0");
                }
                //对order_no中包含非数字的处理，替换非数字为空格
                String n_order_no = advice.getORDER_NO().replaceAll("[^\\d]+", "");
                //取一轮遍历中order_no的最小值
                if(min.equals(-1) || new BigDecimal(n_order_no).compareTo(min)==-1){
                    min = new BigDecimal(n_order_no);
                }
            }
            //tempList存放：order_no等于这次遍历最小值的处方
            List<Advice> tempList = new ArrayList<Advice>();
            for(int k=0;k<newList.size();++k){
                Advice advice = newList.get(k);
                String n_order_no = advice.getORDER_NO().replaceAll("[^\\d]+", "");

                if(new BigDecimal(n_order_no).equals(min)){
                    advice.setOrder_no_flag(true);
                    tempList.add(advice);
                }
            }

            //min2存放：order_no等于这次遍历最小值处方中的order_sub_no最小值
            BigDecimal min2 = new BigDecimal(-1);
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
                    if(min2.equals(-1) || new BigDecimal(advice.getORDER_SUB_NO()).compareTo(min2) == -1){
                        min2 = new BigDecimal(advice.getORDER_SUB_NO());
                    }
                }

                //将order_sub_no等于最小值的处方加入到finalList中，并将order_sub_no_flag改为true表明已经处理过
                for(int l=0;l<tempList.size();++l){
                    Advice advice = tempList.get(l);
                    if(advice.isOrder_sub_no_flag() == true || "".equals(advice.getORDER_SUB_NO())){
                        continue;
                    }
                    if(new BigDecimal(advice.getORDER_SUB_NO()).equals(min2)){
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
                min2 = new BigDecimal(-1);
            }
            //将min改为-1，进行下一次寻找order_no的最小值
            min = new BigDecimal(-1);
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
