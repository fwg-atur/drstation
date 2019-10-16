package com.dcdt.doctorstation.service;

import com.dcdt.cache.PharmacistCheckResultCache;
import com.dcdt.cache.RetValCache;
import com.dcdt.doctorstation.entity.*;
import com.dcdt.utils.ChineseCharToEn;
import com.dcdt.utils.CommonUtil;
import com.dcdt.utils.HttpUtil;
import com.dcdt.utils.ParseXML;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by wtwang on 2017/12/11.
 */
@Service
public class PharmacistPrescCheckService {
    @Value("checkServerIp")
    private String checkServerIp;

    @Value("checkServerPort")
    private String checkServerPort;

    @Value("${pharmacistInterfereServerUrl}")
    private String pharmacistInterfereServerUrl;

    @Value("${pharmacistCheckServerUrl}")
    private String pharmacistCheckServerUrl;

    @Value("${cpPharmacistCheckUrl}")
    private String cpPharmacistCheckUrl;

    @Value("${groupFlag}")
    private String groupFlag;

//    @Value("${bzPharmacistCheckServerUrl}")
//    private String bzPharmacistCheckServerUrl;

    private CacheService cacheService;

    private PharmacistInfo pharmacistInfo;
    private ParseXML parseXML = new ParseXML();
    private String date;



    private static final Logger logger = LoggerFactory.getLogger(PharmacistPrescCheckService.class);

    /**
     * 测试用
     * @param patientID
     * @param visitDate
     * @param pharmacistInfoXML
     * @param xml
     * @return
     */
    public CheckMessage checkPharmacistPrescForTest(String patientID,String visitDate,String pharmacistInfoXML, String xml) {
        String checkXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><CheckList><Check><CheckInput TAG=\"2\">    <Doctor NAME=\"门急诊医生\" POSITION=\"副主任医师\" USER_ID=\"123\" DEPT_NAME=\"骨科\" DEPT_CODE=\"030401\"/>    <Patient NAME=\"门急诊病人1\" ID=\"1\" VISIT_ID=\"1233\" PATIENT_PRES_ID=\"1\" BIRTH=\"19840908\" HEIGHT=\"100\" WEIGHT=\"20\" GENDER=\"男\" PREGNANT=\"是\" LACT=\"是\" HEPATICAL=\"是\" RENAL=\"否\" PANCREAS=\"否\" ALERGY_DRUGS=\"选择性5-HT3受体抑制药类\" IDENTITY_TYPE=\"军人\" FEE_TYPE=\"医保\" SCR=\"\" SCR_UNIT=\"\" GESTATION_AGE=\"\" PRETERM_BIRTH=\"\" DRUG_HISTORY=\"\" FAMILY_DISEASE_HISTORY=\"\" GENETIC_DISEASE=\"\" MEDICARE_01=\"\" MEDICARE_02=\"\" MEDICARE_03=\"\" MEDICARE_04=\"\" MEDICARE_05=\"\"/>    <Diagnosises DIAGNOSISES=\"_感染\"/>    <Advices>        <Advice DRUG_LO_ID=\"0101003CP0\" DRUG_LO_NAME=\"阿莫西林胶囊\" ADMINISTRATION=\"静滴\" DOSAGE=\"5\" DOSAGE_UNIT=\"粒\" FREQ_COUNT=\"1\" FREQ_INTERVAL=\"1\" FREQ_INTERVAL_UNIT=\"日\" START_DAY=\"20150114\" END_DAY=\"20150114\" REPEAT=\"0\" ORDER_NO=\"1\" ORDER_SUB_NO=\"2\" DEPT_CODE=\"2426\" DOCTOR_NAME=\"闫洪生\" TITLE=\"副主任医师\" AUTHORITY_LEVELS=\"\" ALERT_LEVELS=\"1,2\" GROUP_ID=\"\" USER_ID=\"123\" PRES_ID=\"1\" PRES_DATE=\"20150114\" PRES_SEQ_ID=\"3138829120150114\" PK_ORDER_NO=\"\" COURSE=\"\" PKG_COUNT=\"101\" PKG_UNIT=\"粒\" BAK_01=\"\" BAK_02=\"\" BAK_03=\"胶囊\" BAK_04=\"0.25g\" BAK_05=\"昆明贝克诺\"/>        <Advice DRUG_LO_ID=\"3012302CP0\" DRUG_LO_NAME=\"安替可胶囊\" ADMINISTRATION=\"\" DOSAGE=\"100\" DOSAGE_UNIT=\"\" FREQ_COUNT=\"5\" FREQ_INTERVAL=\"1\" FREQ_INTERVAL_UNIT=\"日\" START_DAY=\"20150114\" END_DAY=\"20150114\" REPEAT=\"0\" ORDER_NO=\"6\" ORDER_SUB_NO=\"1\" DEPT_CODE=\"骨科\" DOCTOR_NAME=\"朱宏勋\" TITLE=\"主任医师\" AUTHORITY_LEVELS=\"\" ALERT_LEVELS=\"1,2\" GROUP_ID=\"\" USER_ID=\"009284\" PRES_ID=\"1\" PRES_DATE=\"20150114\" PRES_SEQ_ID=\"3138829120150114\" PK_ORDER_NO=\"\" COURSE=\"\" PKG_COUNT=\"2\" PKG_UNIT=\"\" BAK_01=\"\" BAK_02=\"\" BAK_03=\"\" BAK_04=\"0.22g\" BAK_05=\"\"/>    </Advices></CheckInput><CheckOutput AntiDrugPos=\"\">    <PresInfo ORDER_ID=\"1\" ORDER_SUB_ID=\"2\" DRUG_LO_ID=\"0101003CP0\" DRUG_LO_NAME=\"阿莫西林胶囊\">        <CheckInfo COLOR=\"红色\" NAME=\"医院管理\" WARNING_LEVEL=\"禁用\" WARNING_INFO=\"患者月最大定量：    100.00，患者已用：    202.00，当前处方用量：    101.00\" REF_SOURCE=\"医院药控相关规定。\" YPMC=\"\" JSXX=\"\" ZYJL=\"\" TYSM=\"\" LCSY=\"\"/>    </PresInfo>    <PresInfo ORDER_ID=\"6\" ORDER_SUB_ID=\"1\" DRUG_LO_ID=\"3012302CP0\" DRUG_LO_NAME=\"安替可胶囊\">        <CheckInfo COLOR=\"黄色\" NAME=\"适应症\" WARNING_LEVEL=\"慎用\" WARNING_INFO=\"超适应症用药。药品说明书适应症为：。医生诊断为：_感染\" REF_SOURCE=\"药品说明书及医院相关规定\" YPMC=\"\" JSXX=\"\" ZYJL=\"\" TYSM=\"\" LCSY=\"\"/>    </PresInfo></CheckOutput><CheckPharmacist>我觉得这个处方很好</CheckPharmacist></Check></CheckList>";
        return handleCheckXml(xml,checkXml);
    }


    /**

     * @param xml
     * @return 返回审核结果标识（flag,presId）,不包括审核结果字符串
     */
    public CheckMessage checkPharmacistPresc(String patientID,String visitDate,String pharmacistInfoXML, String xml) {
        String url = pharmacistCheckServerUrl + "?patientID=" + patientID + "&visitDate=" + visitDate;
        xml = xml.replace("&nbsp;"," ");
        CheckMessage message = new CheckMessage();
        String checkXml = HttpUtil.sendPost(url, "");
//        String checkXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><CheckList><Check><CheckInput TAG=\"2\">    <Doctor NAME=\"李秋菊\" POSITION=\"主治医师\" USER_ID=\"10645\" DEPT_NAME=\"儿二科\" DEPT_CODE=\"1050400\"/>    <Patient NAME=\"孙健之次子\" ID=\"000358649700\" VISIT_ID=\"1\" PATIENT_PRES_ID=\"000358649700_1\" BIRTH=\"20190916\" HEIGHT=\"\" WEIGHT=\"\" GENDER=\"男\" PREGNANT=\"\" LACT=\"\" HEPATICAL=\"\" RENAL=\"\" PANCREAS=\"\" ALERGY_DRUGS=\"\" IDENTITY_TYPE=\"\" FEE_TYPE=\"自费\" SCR=\"\" SCR_UNIT=\"\" GESTATION_AGE=\"\" PRETERM_BIRTH=\"\" DRUG_HISTORY=\"\" FAMILY_DISEASE_HISTORY=\"\" GENETIC_DISEASE=\"\" MEDICARE_01=\"\" MEDICARE_02=\"\" MEDICARE_03=\"\" MEDICARE_04=\"\" MEDICARE_05=\"\" WARD_CODE=\"1050401\" WARD_NAME=\"儿二科新生儿病房\" BED_NO=\"001\" INPATIENT_NO=\"413076\" PRE_OPERATION_NAME=\"\" PRE_OPERATION_CODE=\"\" OPERATION_NAME=\"\" OPERATION_CODE=\"\" ANAESTHESIA_NAME=\"\" ANAESTHESIA_CODE=\"\">        <LIS_VALUES/>    </Patient>    <Diagnosises DIAGNOSISES=\"\"/>    <Advices>        <Advice DRUG_LO_ID=\"00299600\" DRUG_LO_NAME=\"中/长链脂肪乳注射液(C8-24)\" ADMINISTRATION=\"静点\" DOSAGE=\"38\" DOSAGE_UNIT=\"ml\" FREQ_COUNT=\"ONCE\" FREQ_INTERVAL=\"\" FREQ_INTERVAL_UNIT=\"\" START_DAY=\"20190923\" END_DAY=\"18991230\" REPEAT=\"0\" ORDER_NO=\"63384822\" ORDER_SUB_NO=\"1\" DEPT_CODE=\"1050400\" DOCTOR_NAME=\"李秋菊\" TITLE=\"主治医师\" AUTHORITY_LEVELS=\"\" ALERT_LEVELS=\"\" GROUP_ID=\"63384822\" USER_ID=\"10645\" PRES_ID=\"6338482\" PRES_DATE=\"20190923\" PRES_SEQ_ID=\"63384822\" PK_ORDER_NO=\"\" COURSE=\"\" PKG_COUNT=\"1\" PKG_UNIT=\"瓶\" BAK_01=\"\" BAK_02=\"否\" BAK_03=\"注射液\" BAK_04=\"20% 250ml/瓶\" BAK_05=\"广州百特侨光医疗用品有限公司\" PERFORM_SCHEDULE=\"08:00\" TIME=\"08:00\"/>        <Advice DRUG_LO_ID=\"00196200\" DRUG_LO_NAME=\"脂溶性维生素注射液（Ⅱ）\" ADMINISTRATION=\"静点\" DOSAGE=\"2.5\" DOSAGE_UNIT=\"ml\" FREQ_COUNT=\"ONCE\" FREQ_INTERVAL=\"\" FREQ_INTERVAL_UNIT=\"\" START_DAY=\"20190923\" END_DAY=\"18991230\" REPEAT=\"0\" ORDER_NO=\"63384824\" ORDER_SUB_NO=\"1\" DEPT_CODE=\"1050400\" DOCTOR_NAME=\"李秋菊\" TITLE=\"主治医师\" AUTHORITY_LEVELS=\"\" ALERT_LEVELS=\"\" GROUP_ID=\"63384822\" USER_ID=\"10645\" PRES_ID=\"63384824\" PRES_DATE=\"20190923\" PRES_SEQ_ID=\"63384824\" PK_ORDER_NO=\"\" COURSE=\"\" PKG_COUNT=\"1\" PKG_UNIT=\"支\" BAK_01=\"\" BAK_02=\"否\" BAK_03=\"注射液\" BAK_04=\"10ml/支\" BAK_05=\"华瑞制药有限公司\" PERFORM_SCHEDULE=\"08:00\" TIME=\"08:00\"/>        <Advice DRUG_LO_ID=\"20128200\" DRUG_LO_NAME=\"小儿复方氨基酸注射液(18AA-I)\" ADMINISTRATION=\"静点\" DOSAGE=\"56\" DOSAGE_UNIT=\"ml\" FREQ_COUNT=\"ONCE\" FREQ_INTERVAL=\"\" FREQ_INTERVAL_UNIT=\"\" START_DAY=\"20190923\" END_DAY=\"18991230\" REPEAT=\"0\" ORDER_NO=\"63384826\" ORDER_SUB_NO=\"2\" DEPT_CODE=\"1050400\" DOCTOR_NAME=\"李秋菊\" TITLE=\"主治医师\" AUTHORITY_LEVELS=\"\" ALERT_LEVELS=\"\" GROUP_ID=\"63384822\" USER_ID=\"10645\" PRES_ID=\"63384826\" PRES_DATE=\"20190923\" PRES_SEQ_ID=\"63384826\" PK_ORDER_NO=\"\" COURSE=\"\" PKG_COUNT=\"1\" PKG_UNIT=\"袋\" BAK_01=\"\" BAK_02=\"否\" BAK_03=\"注射液\" BAK_04=\"6.74g:100ml/袋\" BAK_05=\"辰欣药业股份有限公司\" PERFORM_SCHEDULE=\"08:00\" TIME=\"08:00\"/>        <Advice DRUG_LO_ID=\"20078000\" DRUG_LO_NAME=\"注射用水溶性维生素\" ADMINISTRATION=\"静点\" DOSAGE=\"0.05\" DOSAGE_UNIT=\"瓶\" FREQ_COUNT=\"ONCE\" FREQ_INTERVAL=\"\" FREQ_INTERVAL_UNIT=\"\" START_DAY=\"20190923\" END_DAY=\"18991230\" REPEAT=\"0\" ORDER_NO=\"63384828\" ORDER_SUB_NO=\"3\" DEPT_CODE=\"1050400\" DOCTOR_NAME=\"李秋菊\" TITLE=\"主治医师\" AUTHORITY_LEVELS=\"\" ALERT_LEVELS=\"\" GROUP_ID=\"63384822\" USER_ID=\"10645\" PRES_ID=\"63384828\" PRES_DATE=\"20190923\" PRES_SEQ_ID=\"63384828\" PK_ORDER_NO=\"\" COURSE=\"\" PKG_COUNT=\"1\" PKG_UNIT=\"瓶\" BAK_01=\"\" BAK_02=\"否\" BAK_03=\"粉针剂\" BAK_04=\"1瓶 /瓶\" BAK_05=\"华瑞制药有限公司\" PERFORM_SCHEDULE=\"08:00\" TIME=\"08:00\"/>        <Advice DRUG_LO_ID=\"20022700\" DRUG_LO_NAME=\"多种微量元素注射液\" ADMINISTRATION=\"静点\" DOSAGE=\"0.5\" DOSAGE_UNIT=\"ml\" FREQ_COUNT=\"ONCE\" FREQ_INTERVAL=\"\" FREQ_INTERVAL_UNIT=\"\" START_DAY=\"20190923\" END_DAY=\"18991230\" REPEAT=\"0\" ORDER_NO=\"63384830\" ORDER_SUB_NO=\"4\" DEPT_CODE=\"1050400\" DOCTOR_NAME=\"李秋菊\" TITLE=\"主治医师\" AUTHORITY_LEVELS=\"\" ALERT_LEVELS=\"\" GROUP_ID=\"63384822\" USER_ID=\"10645\" PRES_ID=\"63384830\" PRES_DATE=\"20190923\" PRES_SEQ_ID=\"63384830\" PK_ORDER_NO=\"\" COURSE=\"\" PKG_COUNT=\"1\" PKG_UNIT=\"支\" BAK_01=\"\" BAK_02=\"否\" BAK_03=\"注射液\" BAK_04=\"10ml/支\" BAK_05=\"华瑞制药有限公司\" PERFORM_SCHEDULE=\"08:00\" TIME=\"08:00\"/>        <Advice DRUG_LO_ID=\"20031400\" DRUG_LO_NAME=\"浓氯化钠注射液\" ADMINISTRATION=\"静点\" DOSAGE=\"2.4\" DOSAGE_UNIT=\"ml\" FREQ_COUNT=\"ONCE\" FREQ_INTERVAL=\"\" FREQ_INTERVAL_UNIT=\"\" START_DAY=\"20190923\" END_DAY=\"18991230\" REPEAT=\"0\" ORDER_NO=\"63384832\" ORDER_SUB_NO=\"5\" DEPT_CODE=\"1050400\" DOCTOR_NAME=\"李秋菊\" TITLE=\"主治医师\" AUTHORITY_LEVELS=\"\" ALERT_LEVELS=\"\" GROUP_ID=\"63384822\" USER_ID=\"10645\" PRES_ID=\"63384832\" PRES_DATE=\"20190923\" PRES_SEQ_ID=\"63384832\" PK_ORDER_NO=\"\" COURSE=\"\" PKG_COUNT=\"1\" PKG_UNIT=\"支\" BAK_01=\"\" BAK_02=\"否\" BAK_03=\"注射液\" BAK_04=\"1g 10ml/支\" BAK_05=\"国药集团容生制药有限公司\" PERFORM_SCHEDULE=\"08:00\" TIME=\"08:00\"/>        <Advice DRUG_LO_ID=\"20121100\" DRUG_LO_NAME=\"葡萄糖注射液\" ADMINISTRATION=\"静点\" DOSAGE=\"32\" DOSAGE_UNIT=\"ml\" FREQ_COUNT=\"ONCE\" FREQ_INTERVAL=\"\" FREQ_INTERVAL_UNIT=\"\" START_DAY=\"20190923\" END_DAY=\"18991230\" REPEAT=\"0\" ORDER_NO=\"63384835\" ORDER_SUB_NO=\"6\" DEPT_CODE=\"1050400\" DOCTOR_NAME=\"李秋菊\" TITLE=\"主治医师\" AUTHORITY_LEVELS=\"\" ALERT_LEVELS=\"\" GROUP_ID=\"63384822\" USER_ID=\"10645\" PRES_ID=\"63384835\" PRES_DATE=\"20190923\" PRES_SEQ_ID=\"63384835\" PK_ORDER_NO=\"\" COURSE=\"\" PKG_COUNT=\"2\" PKG_UNIT=\"支\" BAK_01=\"\" BAK_02=\"否\" BAK_03=\"注射液\" BAK_04=\"50% 10g 20ml/支\" BAK_05=\"大冢制药\" PERFORM_SCHEDULE=\"08:00\" TIME=\"08:00\"/>        <Advice DRUG_LO_ID=\"20097100\" DRUG_LO_NAME=\"氯化钾注射液\" ADMINISTRATION=\"静点\" DOSAGE=\"1.3\" DOSAGE_UNIT=\"ml\" FREQ_COUNT=\"ONCE\" FREQ_INTERVAL=\"\" FREQ_INTERVAL_UNIT=\"\" START_DAY=\"20190923\" END_DAY=\"18991230\" REPEAT=\"0\" ORDER_NO=\"63384836\" ORDER_SUB_NO=\"7\" DEPT_CODE=\"1050400\" DOCTOR_NAME=\"李秋菊\" TITLE=\"主治医师\" AUTHORITY_LEVELS=\"\" ALERT_LEVELS=\"\" GROUP_ID=\"63384822\" USER_ID=\"10645\" PRES_ID=\"63384836\" PRES_DATE=\"20190923\" PRES_SEQ_ID=\"63384836\" PK_ORDER_NO=\"\" COURSE=\"\" PKG_COUNT=\"1\" PKG_UNIT=\"支\" BAK_01=\"\" BAK_02=\"否\" BAK_03=\"注射液\" BAK_04=\"1.5g 10ml/支\" BAK_05=\"大冢制药\" PERFORM_SCHEDULE=\"08:00\" TIME=\"08:00\"/>    </Advices></CheckInput><CheckOutput AntiDrugPos=\"\">    <PresInfo ORDER_ID=\"63384822\" ORDER_SUB_ID=\"1\" DRUG_LO_ID=\"00299600\" DRUG_LO_NAME=\"中/长链脂肪乳注射液(C8-24)\"/>    <PresInfo ORDER_ID=\"63384824\" ORDER_SUB_ID=\"1\" DRUG_LO_ID=\"00196200\" DRUG_LO_NAME=\"脂溶性维生素注射液（Ⅱ）\"/>    <PresInfo ORDER_ID=\"63384826\" ORDER_SUB_ID=\"2\" DRUG_LO_ID=\"20128200\" DRUG_LO_NAME=\"小儿复方氨基酸注射液(18AA-I)\"/>    <PresInfo ORDER_ID=\"63384828\" ORDER_SUB_ID=\"3\" DRUG_LO_ID=\"20078000\" DRUG_LO_NAME=\"注射用水溶性维生素\"/>    <PresInfo ORDER_ID=\"63384830\" ORDER_SUB_ID=\"4\" DRUG_LO_ID=\"20022700\" DRUG_LO_NAME=\"多种微量元素注射液\"/>    <PresInfo ORDER_ID=\"63384832\" ORDER_SUB_ID=\"5\" DRUG_LO_ID=\"20031400\" DRUG_LO_NAME=\"浓氯化钠注射液\"/>    <PresInfo ORDER_ID=\"63384835\" ORDER_SUB_ID=\"6\" DRUG_LO_ID=\"20121100\" DRUG_LO_NAME=\"葡萄糖注射液\"/>    <PresInfo ORDER_ID=\"63384836\" ORDER_SUB_ID=\"7\" DRUG_LO_ID=\"20097100\" DRUG_LO_NAME=\"氯化钾注射液\">        <CheckInfo COLOR=\"黄色\" NAME=\"用法用量\" WARNING_LEVEL=\"慎用\" WARNING_INFO=\"单次剂量超限。药品成分氯化钾的浓度超限。规定的浓度上限为：3.4mg/ml。计算得到的浓度值为：6.0938mg/ml。组号为：63384822。【浓度】钾浓度不超过3.4g/L(45mmol/L)。\" REF_SOURCE=\"15%氯化钾注射液说明书-中国大冢制药有限公司\" YPMC=\"\" JSXX=\"\" ZYJL=\"\" TYSM=\"\" LCSY=\"\"/>    </PresInfo></CheckOutput><CheckPharmacist></CheckPharmacist></Check></CheckList>";
        if(checkXml == null || checkXml.equals("")){
            message.setHasProblem(-2);
            return message;
        }
        checkXml = checkXml.replace("&nbsp;"," ");
        logger.debug(checkXml);
        pharmacistInfo = parseXML.parsePharmacistInfo(pharmacistInfoXML);
        date = visitDate;
        message = handleCheckXml(checkXml,xml);
        putXML2Cache(message.getPresId(), checkXml);
        return message;
    }



    /**
     * @param xml
     * @return 返回审核结果标识（flag,presId）,不包括审核结果字符串
     */
    public CheckMessage checkPharmacistPresc_CP(String visitDate,String pharmacistInfoXML,String xml) {
        String url = cpPharmacistCheckUrl+"?silent=0";
        xml = xml.replace("&nbsp;"," ");
        CheckMessage message = new CheckMessage();
        String checkXml = HttpUtil.sendPost(url, xml);
        if(checkXml == null || checkXml.equals("")){
            message.setHasProblem(-2);
            return message;
        }
        checkXml = checkXml.replace("&nbsp;"," ");
        logger.debug(checkXml);
//        String checkXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><CheckList><Check><CheckInput TAG=\"2\">    <Doctor NAME=\"门急诊医生\" POSITION=\"副主任医师\" USER_ID=\"123\" DEPT_NAME=\"骨科\" DEPT_CODE=\"030401\"/>    <Patient NAME=\"门急诊病人1\" ID=\"1\" VISIT_ID=\"1233\" PATIENT_PRES_ID=\"1\" BIRTH=\"19840908\" HEIGHT=\"100\" WEIGHT=\"20\" GENDER=\"男\" PREGNANT=\"是\" LACT=\"是\" HEPATICAL=\"是\" RENAL=\"否\" PANCREAS=\"否\" ALERGY_DRUGS=\"选择性5-HT3受体抑制药类\" IDENTITY_TYPE=\"军人\" FEE_TYPE=\"医保\" SCR=\"\" SCR_UNIT=\"\" GESTATION_AGE=\"\" PRETERM_BIRTH=\"\" DRUG_HISTORY=\"\" FAMILY_DISEASE_HISTORY=\"\" GENETIC_DISEASE=\"\" MEDICARE_01=\"\" MEDICARE_02=\"\" MEDICARE_03=\"\" MEDICARE_04=\"\" MEDICARE_05=\"\"/>    <Diagnosises DIAGNOSISES=\"_感染\"/>    <Advices>        <Advice DRUG_LO_ID=\"0101003CP0\" DRUG_LO_NAME=\"阿莫西林胶囊\" ADMINISTRATION=\"静滴\" DOSAGE=\"5\" DOSAGE_UNIT=\"粒\" FREQ_COUNT=\"1\" FREQ_INTERVAL=\"1\" FREQ_INTERVAL_UNIT=\"日\" START_DAY=\"20150114\" END_DAY=\"20150114\" REPEAT=\"0\" ORDER_NO=\"1\" ORDER_SUB_NO=\"2\" DEPT_CODE=\"2426\" DOCTOR_NAME=\"闫洪生\" TITLE=\"副主任医师\" AUTHORITY_LEVELS=\"\" ALERT_LEVELS=\"1,2\" GROUP_ID=\"\" USER_ID=\"123\" PRES_ID=\"1\" PRES_DATE=\"20150114\" PRES_SEQ_ID=\"3138829120150114\" PK_ORDER_NO=\"\" COURSE=\"\" PKG_COUNT=\"101\" PKG_UNIT=\"粒\" BAK_01=\"\" BAK_02=\"\" BAK_03=\"胶囊\" BAK_04=\"0.25g\" BAK_05=\"昆明贝克诺\"/>        <Advice DRUG_LO_ID=\"3012302CP0\" DRUG_LO_NAME=\"安替可胶囊\" ADMINISTRATION=\"\" DOSAGE=\"100\" DOSAGE_UNIT=\"\" FREQ_COUNT=\"5\" FREQ_INTERVAL=\"1\" FREQ_INTERVAL_UNIT=\"日\" START_DAY=\"20150114\" END_DAY=\"20150114\" REPEAT=\"0\" ORDER_NO=\"6\" ORDER_SUB_NO=\"1\" DEPT_CODE=\"骨科\" DOCTOR_NAME=\"朱宏勋\" TITLE=\"主任医师\" AUTHORITY_LEVELS=\"\" ALERT_LEVELS=\"1,2\" GROUP_ID=\"\" USER_ID=\"009284\" PRES_ID=\"1\" PRES_DATE=\"20150114\" PRES_SEQ_ID=\"3138829120150114\" PK_ORDER_NO=\"\" COURSE=\"\" PKG_COUNT=\"2\" PKG_UNIT=\"\" BAK_01=\"\" BAK_02=\"\" BAK_03=\"\" BAK_04=\"0.22g\" BAK_05=\"\"/>    </Advices></CheckInput><CheckOutput AntiDrugPos=\"\">    <PresInfo ORDER_ID=\"1\" ORDER_SUB_ID=\"2\" DRUG_LO_ID=\"0101003CP0\" DRUG_LO_NAME=\"阿莫西林胶囊\">        <CheckInfo COLOR=\"红色\" NAME=\"医院管理\" WARNING_LEVEL=\"禁用\" WARNING_INFO=\"患者月最大定量：    100.00，患者已用：    202.00，当前处方用量：    101.00\" REF_SOURCE=\"医院药控相关规定。\" YPMC=\"\" JSXX=\"\" ZYJL=\"\" TYSM=\"\" LCSY=\"\"/>    </PresInfo>    <PresInfo ORDER_ID=\"6\" ORDER_SUB_ID=\"1\" DRUG_LO_ID=\"3012302CP0\" DRUG_LO_NAME=\"安替可胶囊\">        <CheckInfo COLOR=\"黄色\" NAME=\"适应症\" WARNING_LEVEL=\"慎用\" WARNING_INFO=\"超适应症用药。药品说明书适应症为：。医生诊断为：_感染\" REF_SOURCE=\"药品说明书及医院相关规定\" YPMC=\"\" JSXX=\"\" ZYJL=\"\" TYSM=\"\" LCSY=\"\"/>    </PresInfo></CheckOutput><CheckPharmacist>我觉得这个处方很好</CheckPharmacist></Check></CheckList>";
        pharmacistInfo = parseXML.parsePharmacistInfo(pharmacistInfoXML);
        date = visitDate;
        message = handleCheckXml_CP(checkXml,xml);
        putXML2Cache(message.getPresId(), checkXml);
        return message;
    }
    /**

     * @param xml
     * @return 返回审核结果标识（flag,presId）,不包括审核结果字符串
     */
    public CheckMessage checkPharmacistPrescSilent_CP(String xml) {
        String url = cpPharmacistCheckUrl+"?silent=1";
        xml = xml.replace("&nbsp;"," ");
        CheckMessage message = new CheckMessage();
        String checkTag = HttpUtil.sendPost(url, xml);
        if(checkTag == null || checkTag.equals("")){
            message.setHasProblem(-2);
            return message;
        }
        logger.debug(checkTag);
        message.setHasProblem(Integer.parseInt(checkTag));
        return message;
    }

    public String checkPharmacistInterfere(String xml){
        String url = pharmacistInterfereServerUrl;
        String retMessage = "";
        try {
            retMessage = HttpUtil.sendPost(url, xml);
        }catch (Exception e){
            logger.warn("药师干预消息发送失败！");
            e.printStackTrace();
        }
        return retMessage;
    }

    private void putXML2Cache(String presId, String xml) {
        cacheService.putXML2Cache(presId, xml);
    }

    /**
     * @param checkXml
     * @param inputXml
     * 将获取的审核结果xml和传入的xml分别解析后进行匹配
     */
    protected CheckMessage handleCheckXml(String checkXml,String inputXml) {
        //解析审核结果xml
        parseXML.parseXML(checkXml);

        //将解析结果用对象封装
        Check check = new Check();
        check.setCheckPresInput(parseXML.getCheckPresInput());
        check.setCheckPresOutput(parseXML.getCheckPresOutput());
        check.setCheckPharmacist(parseXML.getCheckPharmacist());

        //解析传入的xml
        CheckPresInput checkPresInput = parseXML.parseInputXml(inputXml);

        //调用函数比较两个对象中的医嘱
        List<PrescInfo> list = compareCheck(checkPresInput,check.getCheckPresOutput());

        //将与传入的xml中医嘱匹配的审核结果重新赋值给对象
        check.getCheckPresOutput().setPrescInfos(sortgroupPresInfo(list,groupFlag));

        String presId = checkPresInput.getPatient().getPATIENT_PRES_ID();
        if(presId == null || "".equals(presId)){
            presId = CommonUtil.getPresIdWithTime(checkPresInput.getPatient().getID());
        }else{
            presId = CommonUtil.getPresIdWithTime(presId);
        }

        //缓存审核结果,等到进入审核结果页面时再读取记录显示
        PharmacistCheckResultCache.putPharmacistCheckResult(presId, check);

        //处理审核信息
        int warnLevel = parseXML.getPharHighestWarningLevel(check.getCheckPresOutput().getPrescInfos());
        CheckMessage message = new CheckMessage();
        message.setPresId(presId);

//        if(warnLevel == 0){
//            //无问题
//            message.setHasProblem(0);
//        }else if(warnLevel == 1){
//            //有慎用问题
//            message.setHasProblem(1);
//        }else if(warnLevel == 2){
//            //有禁用问题
//            message.setHasProblem(2);
//        }else if(warnLevel == -1){
//            //有拦截问题
//            message.setHasProblem(-1);
//        }
        message.setHasProblem(warnLevel);
//        if(checkProblem(check.getCheckPresOutput())){
//            message.setHasProblem(1);
//        }

        return message;
    }

    /**
     * @param checkXml
     * @param inputXml
     * 将获取的审核结果xml和传入的xml分别解析后进行匹配
     */
    protected CheckMessage handleCheckXml_CP(String checkXml,String inputXml) {
        //解析审核结果xml
        parseXML.parseXML_CP(checkXml);

        //将解析结果用对象封装
        Check check = new Check();
        check.setCheckPresInput(parseXML.getCheckPresInput());
        check.setCheckPresOutput(parseXML.getCheckPresOutput());
        check.setCheckPharmacist(parseXML.getCheckPharmacist());

        //解析传入的xml
        CheckPresInput checkPresInput = parseXML.parseInputXml(inputXml);

        //调用函数比较两个对象中的医嘱
        List<PrescInfo> list = compareCheck(checkPresInput,check.getCheckPresOutput());

        //将与传入的xml中医嘱匹配的审核结果重新赋值给对象
        check.getCheckPresOutput().setPrescInfos(sortgroupPresInfo(list,groupFlag));

        String presId = checkPresInput.getPatient().getPATIENT_PRES_ID();
        if(presId == null || "".equals(presId)){
            presId = CommonUtil.getPresIdWithTime(checkPresInput.getPatient().getID());
        }else{
            presId = CommonUtil.getPresIdWithTime(presId);
        }

        //缓存审核结果,等到进入审核结果页面时再读取记录显示
        PharmacistCheckResultCache.putPharmacistCheckResult(presId, check);

        //处理审核信息
        int warnLevel = parseXML.getPharHighestWarningLevel(check.getCheckPresOutput().getPrescInfos());
        CheckMessage message = new CheckMessage();
        message.setPresId(presId);

//        if(warnLevel == 0){
//            //无问题
//            message.setHasProblem(0);
//        }else if(warnLevel == 1){
//            //有慎用问题
//            message.setHasProblem(1);
//        }else if(warnLevel == 2){
//            //有禁用问题
//            message.setHasProblem(2);
//        }else if(warnLevel == -1){
//            //有拦截问题
//            message.setHasProblem(-1);
//        }
        message.setHasProblem(warnLevel);
//        if(checkProblem(check.getCheckPresOutput())){
//            message.setHasProblem(1);
//        }

        return message;
    }

    protected List<PrescInfo> compareCheck(CheckPresInput checkPresInput, CheckPresOutput checkPresOutput){
        List<Advice> adviceList = checkPresInput.getAdvices();
        List<PrescInfo> prescInfoList = checkPresOutput.getPrescInfos();
        List<PrescInfo> prescInfos = new ArrayList<PrescInfo>();

        //遍历传入xml的医嘱列表
        if(adviceList != null) {
            for (Advice advice : adviceList) {
                //遍历审核结果xml的审核结果医嘱列表
                if(prescInfoList != null) {
                    for (PrescInfo prescInfo : prescInfoList) {
                        if (advice.getDRUG_LO_ID().equals(prescInfo.getDrug_lo_id())
                                && advice.getORDER_NO().equals(prescInfo.getOrder_id())
                                && advice.getORDER_SUB_NO().equals(prescInfo.getOrder_sub_id())) {
                            prescInfos.add(prescInfo);
                        }
                    }
                }
            }
        }
        return prescInfos;
    }

    protected boolean checkProblem(CheckPresOutput checkPresOutput){
        List<PrescInfo> prescInfos = checkPresOutput.getPrescInfos();

        for(PrescInfo prescInfo : prescInfos){
            if(null != prescInfo.getCheckInfos() && 0 != prescInfo.getCheckInfos().size()){
                return true;
            }
        }
        return false;
    }

    public <T> String toJson(T check) {
        Gson gson = new Gson();
        return gson.toJson(check);
    }

    public Check findPharmacistCheckResult(String presId) {
        return PharmacistCheckResultCache.removePharmacistCheckResult(presId);
    }


    public void putRetValue(String presId, int result) {
        RetValCache.putRetVal(presId, result);
    }

    public int findRetValue(String presId) {
        if (notBackOrNext(presId))
            return -2;
        return RetValCache.removeRetVal(presId);
    }

    public void putRetValue_bz(String presId, String message) {
        RetValCache.putRetVal_bz(presId, message);
    }

    //根据presId获取缓存中的返回字符串
    public String findRetValue_bz(String presId){
        if(RetValCache.containsKey_bz(presId)){
            return RetValCache.removeRetVal_bz(presId);
        }else{
            String retXml = "<OrderList STATE=\"-1\" />";
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

    public PharmacistInfo getPharmacistInfo() {
        return pharmacistInfo;
    }

    public String getDate() {
        return date;
    }

    @Deprecated
    public List<PrescInfo> sortCheckResult(List<PrescInfo> prescInfos){
        //newList存放：已经按照问题级别从高到低排序好的处方
        List<PrescInfo> newList0 = new ArrayList<PrescInfo>();
        List<PrescInfo> newList1 = new ArrayList<PrescInfo>();
        List<PrescInfo> newList2 = new ArrayList<PrescInfo>();
        List<PrescInfo> newList3 = new ArrayList<PrescInfo>();
        if(prescInfos == null || prescInfos.size() <= 1){
            return prescInfos;
        }
        for (PrescInfo prescInfo : prescInfos) {
            List<CheckInfo> checkInfos = handleCheckInfos(prescInfo.getCheckInfos());
            if(getHighestLevelFromCheckInfoList(checkInfos) == 3){
                newList3.add(prescInfo);
            }else if(getHighestLevelFromCheckInfoList(checkInfos) == 2){
                newList2.add(prescInfo);
            }else if(getHighestLevelFromCheckInfoList(checkInfos) == 1){
                newList1.add(prescInfo);
            }else if(getHighestLevelFromCheckInfoList(checkInfos) == 0){
                newList0.add(prescInfo);
            }
        }
        List<PrescInfo> finalList = new ArrayList<PrescInfo>();

        finalList = sortHelp(newList3,finalList);
        finalList = sortHelp(newList2,finalList);
        finalList = sortHelp(newList1,finalList);
        finalList = sortHelp(newList0,finalList);

        return finalList;


    }

    //过滤药师站的用药监测问题
    public List<CheckInfo> handleCheckInfos(List<CheckInfo> list){
        List<CheckInfo> res = new ArrayList<CheckInfo>();
        for(CheckInfo checkInfo : list){
            if(!"用药监测".equals(checkInfo.getNAME())){
                res.add(checkInfo);
            }
        }
        return res;
    }

    //辅助方法，避免重复代码
    @Deprecated
    public List<PrescInfo> sortHelp(List<PrescInfo> list,List<PrescInfo> finalList){
        if("1".equals(groupFlag)){
            list = sortSameLevelGroupId(list);
        }
        else if("2".equals(groupFlag)) {
            list = sortSameLevelOrderNo(list);
        }
        for(PrescInfo prescInfo:list){
            finalList.add(prescInfo);
        }
        return finalList;
    }

    //对同一级别的问题按照group_id排序
    @Deprecated
    public List<PrescInfo> sortSameLevelGroupId(List<PrescInfo> newList){
        //finalList存放：按照group_id从小到到排列
        List<PrescInfo> finalList = new ArrayList<PrescInfo>();

        //min存放：group_id一轮的最小值
        BigDecimal min = new BigDecimal(-1);
        for(int i=0;i<newList.size();++i){
            for(int j=0;j<newList.size();++j){
                PrescInfo prescInfo = newList.get(j);
                if(prescInfo.isGroup_id_flag() == true){
                    continue;
                }
                if(prescInfo.getGroup_id() == null || "".equals(prescInfo.getGroup_id())){
                    prescInfo.setGroup_id("0");
                }
                String n_group_id = prescInfo.getGroup_id().replaceAll("[^\\d]+", "");
                if(min.equals(-1) || new BigDecimal(n_group_id).compareTo(min) == -1){
                    min = new BigDecimal(n_group_id);
                }
            }

            //tempList存放：group_id等于这次遍历最小值的处方
            List<PrescInfo> tempList = new ArrayList<PrescInfo>();
            for(int k=0;k<newList.size();++k){
                PrescInfo prescInfo = newList.get(k);
                String n_group_id = prescInfo.getGroup_id().replaceAll("[^\\d]+", "");

                if(new BigDecimal(n_group_id).equals(min)){
                    prescInfo.setGroup_id_flag(true);
                    tempList.add(prescInfo);
                }
            }

            //将tempList中的处方加入到finalList中
            int x = 0;
            for(int l=0;l<tempList.size();++l){
                PrescInfo prescInfo = tempList.get(l);
                if(tempList.size() > 1){
                    if(x == 0){
                        prescInfo.setKh("┍ ");
                    }
                    else if(x == tempList.size()-1){
                        prescInfo.setKh("┕ ");
                    }
                    else{
                        prescInfo.setKh("");
                    }
                }else{
                    prescInfo.setKh("");
                }
                ++x;
                finalList.add(prescInfo);
            }
            min = new BigDecimal(-1);
        }
        return finalList;
    }

    //对同一级别的问题按照order_id排序
    @Deprecated
    public List<PrescInfo> sortSameLevelOrderNo(List<PrescInfo> newList){
        //finalList存放：按照order_id从小到到排列,按照order_sub_id从小到大排列
        List<PrescInfo> finalList = new ArrayList<PrescInfo>();

        //min存放：order_id一轮的最小值
        BigDecimal min = new BigDecimal(-1);
        for(int i=0;i<newList.size();++i){
            //本循环的目的：取到一轮遍历中order_id的最小值
            for(int j=0;j<newList.size();++j){
                PrescInfo prescInfo = newList.get(j);
                //order_id_flag为true表示已经有序加到finalList中，不需要再处理
                if(prescInfo.isOrder_id_flag() == true){
                    continue;
                }
                if(prescInfo.getOrder_id() == null || "".equals(prescInfo.getOrder_id())){
                    prescInfo.setOrder_id("0");
                }
                //对order_id中包含非数字的处理，替换非数字为空格
                String n_order_id = prescInfo.getOrder_id().replaceAll("[^\\d]+", "");

                //取一轮遍历中order_id的最小值
                if(min.equals(-1) || new BigDecimal(n_order_id).compareTo(min) == -1){
                    min = new BigDecimal(n_order_id);
                }
            }
            //tempList存放：order_id等于这次遍历最小值的处方
            List<PrescInfo> tempList = new ArrayList<PrescInfo>();
            for(int k=0;k<newList.size();++k){
                PrescInfo prescInfo = newList.get(k);
                String n_order_id = prescInfo.getOrder_id().replaceAll("[^\\d]+", "");

                if(new BigDecimal(n_order_id).equals(min)){
                    prescInfo.setOrder_id_flag(true);
                    tempList.add(prescInfo);
                }
            }

            //min2存放：order_id等于这次遍历最小值处方中的order_sub_id最小值
            BigDecimal min2 = new BigDecimal(-1);
            int x = 0;
            for(int m=0;m<tempList.size();++m){
                //本循环的目的：取到相同order_id的order_sub_id最小值
                for(int n=0;n<tempList.size();++n){
                    PrescInfo prescInfo = tempList.get(n);
                    //order_sub_id_flag为true表示已经有序加到finalList中，不需要再处理
                    if("".equals(prescInfo.getOrder_sub_id())){
                        continue;
                    }
                    if(prescInfo.isOrder_sub_id_flag() == true){
                        continue;
                    }
                    //取一轮遍历中order_sub_id的最小值
                    if(min2.equals(-1) || new BigDecimal(prescInfo.getOrder_sub_id()).compareTo(min2) == -1){
                        min2 = new BigDecimal(prescInfo.getOrder_sub_id());
                    }
                }
                //将order_sub_id等于最小值的处方加入到finalList中，并将order_sub_id_flag改为true表明已经处理过
                for(int l=0;l<tempList.size();++l){
                    PrescInfo prescInfo = tempList.get(l);
                    if(prescInfo.isOrder_sub_id_flag() || "".equals(prescInfo.getOrder_sub_id())){
                        continue;
                    }
                    if(new BigDecimal(prescInfo.getOrder_sub_id()).equals(min2)){
                        //给成组的处方加上左侧方括号
                        if(tempList.size() > 1){
                            if(x == 0){
                                prescInfo.setKh("┍ ");
                            }
                            else if(x == tempList.size()-1){
                                prescInfo.setKh("┕ ");
                            }
                            else{
                                prescInfo.setKh("");
                            }
                        }else{
                            prescInfo.setKh("");
                        }
                        x++;
                        finalList.add(prescInfo);
                        prescInfo.setOrder_sub_id_flag(true);
                    }
                }

                //将order_sub_no为空并且还未加到finalList的处方加入到finalList
                for(int y=0;y<tempList.size();++y){
                    PrescInfo prescInfo = tempList.get(y);
                    if(prescInfo.isOrder_sub_id_flag() == false) {
                        if ("".equals(prescInfo.getOrder_sub_id())) {
                            //给成组的处方加上左侧方括号
                            if (tempList.size() > 1) {
                                if (x == 0) {
                                    prescInfo.setKh("┍ ");
                                } else if (x == tempList.size() - 1) {
                                    prescInfo.setKh("┕ ");
                                } else {
                                    prescInfo.setKh("");
                                }
                            } else {
                                prescInfo.setKh("");
                            }
                            ++x;
                            finalList.add(prescInfo);
                            prescInfo.setOrder_sub_id_flag(true);
                        }
                    }
                }
                //将min2改为-1，进行下一次寻找order_sub_id的最小值
                min2 = new BigDecimal(-1);
            }
            //将min改为-1，进行下一次寻找order_id的最小值
            min = new BigDecimal(-1);
        }

        return finalList;
    }


    /**
     * add by wtwang @2019.01.17
     * group_id或者order_no相同为一组，同一组的医嘱在一起，计算组中最高的问题级别
     * 将原始问题医嘱列表按照group_id或者order_no成组分好
     * 并且成组的问题按照问题级别排好序
     * 各个组之间按照组的最高问题级别排好序
     * flag为1表示按照group_id分组，flag为2表示按照order_no分组
     * @param list
     * @return
     */
    public List<PrescInfo> sortgroupPresInfo(List<PrescInfo> list,String flag){
        List<PrescInfo> result = new ArrayList<PrescInfo>();
        Map<BigDecimal,List<PrescInfo>> map = new HashMap<BigDecimal, List<PrescInfo>>();
        for(PrescInfo prescInfo : list){
            String s_id = "";
            // 区分按照group_id分组还是按照order_no分组
            if("1".equals(flag)){
                if(prescInfo.getGroup_id() == null || "".equals(prescInfo.getGroup_id())){
                    prescInfo.setGroup_id("0");
                }
                s_id = prescInfo.getGroup_id().replaceAll("[^\\d]+", "");
            }else if("2".equals(flag)){
                if(prescInfo.getOrder_id() == null || "".equals(prescInfo.getOrder_id())){
                    prescInfo.setOrder_id("0");
                }
                s_id = prescInfo.getOrder_id().replaceAll("[^\\d]+", "");
            }
            // 遍历list，将一组的医嘱存入同一个list中
            if(!map.containsKey(new BigDecimal(s_id))){
                List<PrescInfo> tempList = new ArrayList<PrescInfo>();
                tempList.add(prescInfo);
                map.put(new BigDecimal(s_id),tempList);
            }else{
                map.get(new BigDecimal(s_id)).add(prescInfo);
            }
        }

        // list_i表示成组医嘱中最该问题级别为i的集合
        List<List<PrescInfo>> list_3 = new ArrayList<List<PrescInfo>>();
        List<List<PrescInfo>> list_2 = new ArrayList<List<PrescInfo>>();
        List<List<PrescInfo>> list_1 = new ArrayList<List<PrescInfo>>();
        List<List<PrescInfo>> list_0 = new ArrayList<List<PrescInfo>>();
        // 遍历map中的成组医嘱，计算每组医嘱的最高问题级别
        for(List<PrescInfo> tempList : map.values()){
            // 在此时将成组的医嘱按照问题级别从高到低排好序
            tempList = sortSameGroup(tempList);
            // 将同一组的医嘱用括号标识括起来
            tempList = handleSameGroup(tempList);
            int level = getHighestLevelFromPresInfoList(tempList);
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
        for(List<PrescInfo> list3 : list_3){
            for(PrescInfo prescInfo : list3){
                result.add(prescInfo);
            }
        }
        for(List<PrescInfo> list2 : list_2){
            for(PrescInfo prescInfo : list2){
                result.add(prescInfo);
            }
        }
        for(List<PrescInfo> list1 : list_1){
            for(PrescInfo prescInfo : list1){
                result.add(prescInfo);
            }
        }
        for(List<PrescInfo> list0 : list_0){
            for(PrescInfo prescInfo : list0){
                result.add(prescInfo);
            }
        }


        return result;
    }

    /**
     * add by wtwang @2019.01.17
     * 将同一分组的医嘱按照问题级别从高到低排序
     * @param list
     * @return
     */
    public List<PrescInfo> sortSameGroup(List<PrescInfo> list){
        List<PrescInfo> result = new ArrayList<PrescInfo>();
        Map<Integer,List<PrescInfo>> map = new HashMap<Integer, List<PrescInfo>>();

        // 遍历list，将问题级别相同医嘱放入同一个list中
        for(PrescInfo prescInfo : list){
            List<CheckInfo> checkInfos = handleCheckInfos(prescInfo.getCheckInfos());
            int warning_level = getHighestLevelFromCheckInfoList(checkInfos);
            if(!map.containsKey(warning_level)){
                List<PrescInfo> tempList = new ArrayList<PrescInfo>();
                tempList.add(prescInfo);
                map.put(warning_level,tempList);
            }else{
                map.get(warning_level).add(prescInfo);
            }
        }

        // i从3到0表示问题级别，按照问题级别从高到低从map中取出医嘱加入到result中
        for(int i=3;i>=0;--i){
            List<PrescInfo> tempList = map.get(i);
            if(tempList != null && tempList.size() != 0){
                for(PrescInfo prescInfo : tempList){
                    result.add(prescInfo);
                }
            }
        }
        return result;
    }

    /**
     * 将同组的医嘱用括号括起来
     * @param list
     * @return
     */
    public List<PrescInfo> handleSameGroup(List<PrescInfo> list){
        int x = 0;
        for(int l=0;l<list.size();++l){
            PrescInfo prescInfo = list.get(l);
            if(list.size() > 1){
                if(x == 0){
                    prescInfo.setKh("┍ ");
                }
                else if(x == list.size()-1){
                    prescInfo.setKh("┕ ");
                }
                else{
                    prescInfo.setKh("");
                }
            }else{
                prescInfo.setKh("");
            }
            ++x;
        }
        return list;
    }

    /**
     * add by wtwang @2019.01.17
     * 同一组的医嘱计算最高问题级别
     * @param list
     * @return
     */
    public int getHighestLevelFromPresInfoList(List<PrescInfo> list){
        int highestLevel = 0;
        for(PrescInfo prescInfo : list){
            List<CheckInfo> checkInfos = handleCheckInfos(prescInfo.getCheckInfos());
            int tempLevel = getHighestLevelFromCheckInfoList(checkInfos);
            highestLevel = Math.max(highestLevel,tempLevel);
        }
        return highestLevel;
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



     /*------------------------------------------------------------------------BZ药师站  Begin-----------------------------------------------------------------------------------------------*/
    /**
     * @param xml
     * @return 返回审核结果标识（flag,presId）,不包括审核结果字符串
     */
    public CheckMessage checkPharmacistPresc_BZ(String xml,String pharmacistInfoXML) {
        String url = pharmacistCheckServerUrl ;
        xml = xml.replace("&nbsp;"," ");
        CheckMessage message = new CheckMessage();
        String checkXml = HttpUtil.sendPost(url, xml);
//        String checkXml = "<OrderList>\n" +
//                "    <Order BED_NO=\"001\" PATIENT_ID=\"000358649700\" VISIT_ID=\"2\" PATIENT_NAME=\"孙健之次子\" DOCTOR_ID=\"10645\" DOCTOR_NAME=\"李秋菊\" DRUG_NAME=\"多种微量元素注射液\" DRUG_SPEC=\"10ml/支\" ORDER_TYPE=\"临时\" START_TIME=\"20190923\" ADMINISTRATION=\"静点\" FREQUENCY=\"ONCE\" DOSAGE=\"0.5ml\" USE_TIME=\"2019-07-26 15:59:00\" PROBLEM_TYPE=\"\" PROBLEM_LEVEL=\"\" DRUG_STATE=\"\" GROUP_ID=\"63384822\" INPATIENT_NO=\"413076\">\n" +
//                "        <PresInfo ORDER_ID=\"63384830\" ORDER_SUB_ID=\"4\" GROUP_ID=\"63384822\" DRUG_LO_ID=\"20022700\" DRUG_LO_NAME=\"多种微量元素注射液\" GANYU_STATE=\"未干预\" GANYU_INFO=\"\"/>\n" +
//                "    </Order>\n" +
//                "    <Order BED_NO=\"001\" PATIENT_ID=\"000358649700\" VISIT_ID=\"2\" PATIENT_NAME=\"孙健之次子\" DOCTOR_ID=\"10645\" DOCTOR_NAME=\"李秋菊\" DRUG_NAME=\"脂溶性维生素注射液（Ⅱ）\" DRUG_SPEC=\"10ml/支\" ORDER_TYPE=\"临时\" START_TIME=\"20190923\" ADMINISTRATION=\"静点\" FREQUENCY=\"ONCE\" DOSAGE=\"2.5ml\" USE_TIME=\"2019-07-26 15:59:00\" PROBLEM_TYPE=\"\" PROBLEM_LEVEL=\"\" DRUG_STATE=\"\" GROUP_ID=\"63384822\" INPATIENT_NO=\"413076\">\n" +
//                "        <PresInfo ORDER_ID=\"63384824\" ORDER_SUB_ID=\"1\" GROUP_ID=\"63384822\" DRUG_LO_ID=\"00196200\" DRUG_LO_NAME=\"脂溶性维生素注射液（Ⅱ）\" GANYU_STATE=\"未干预\" GANYU_INFO=\"\"/>\n" +
//                "    </Order>\n" +
//                "    <Order BED_NO=\"001\" PATIENT_ID=\"000358649700\" VISIT_ID=\"2\" PATIENT_NAME=\"孙健之次子\" DOCTOR_ID=\"10645\" DOCTOR_NAME=\"李秋菊\" DRUG_NAME=\"葡萄糖注射液\" DRUG_SPEC=\"50% 10g 20ml/支\" ORDER_TYPE=\"临时\" START_TIME=\"20190923\" ADMINISTRATION=\"静点\" FREQUENCY=\"ONCE\" DOSAGE=\"32ml\" USE_TIME=\"2019-07-26 15:59:00\" PROBLEM_TYPE=\"\" PROBLEM_LEVEL=\"\" DRUG_STATE=\"\" GROUP_ID=\"63384822\" INPATIENT_NO=\"413076\">\n" +
//                "        <PresInfo ORDER_ID=\"63384835\" ORDER_SUB_ID=\"1\" GROUP_ID=\"63384822\" DRUG_LO_ID=\"20121100\" DRUG_LO_NAME=\"葡萄糖注射液\" GANYU_STATE=\"未干预\" GANYU_INFO=\"\"/>\n" +
//                "    </Order>\n" +
//                "    <Order BED_NO=\"001\" PATIENT_ID=\"000358649700\" VISIT_ID=\"2\" PATIENT_NAME=\"孙健之次子\" DOCTOR_ID=\"10645\" DOCTOR_NAME=\"李秋菊\" DRUG_NAME=\"小儿复方氨基酸注射液(18AA-I)\" DRUG_SPEC=\"6.74g:100ml/袋\" ORDER_TYPE=\"临时\" START_TIME=\"20190923\" ADMINISTRATION=\"静点\" FREQUENCY=\"ONCE\" DOSAGE=\"56ml\" USE_TIME=\"2019-07-26 15:59:00\" PROBLEM_TYPE=\"\" PROBLEM_LEVEL=\"\" DRUG_STATE=\"\" GROUP_ID=\"63384822\" INPATIENT_NO=\"413076\">\n" +
//                "        <PresInfo ORDER_ID=\"63384826\" ORDER_SUB_ID=\"2\" GROUP_ID=\"63384822\" DRUG_LO_ID=\"20128200\" DRUG_LO_NAME=\"小儿复方氨基酸注射液(18AA-I)\" GANYU_STATE=\"未干预\" GANYU_INFO=\"\"/>\n" +
//                "    </Order>\n" +
//                "    <Order BED_NO=\"001\" PATIENT_ID=\"000358649700\" VISIT_ID=\"2\" PATIENT_NAME=\"孙健之次子\" DOCTOR_ID=\"10645\" DOCTOR_NAME=\"李秋菊\" DRUG_NAME=\"氯化钾注射液\" DRUG_SPEC=\"1.5g 10ml/支\" ORDER_TYPE=\"临时\" START_TIME=\"20190923\" ADMINISTRATION=\"静点\" FREQUENCY=\"ONCE\" DOSAGE=\"1.3ml\" USE_TIME=\"2019-07-26 15:59:00\" PROBLEM_TYPE=\"\" PROBLEM_LEVEL=\"\" DRUG_STATE=\"\" GROUP_ID=\"63384822\" INPATIENT_NO=\"413076\">\n" +
//                "        <PresInfo ORDER_ID=\"63384835\" ORDER_SUB_ID=\"2\" GROUP_ID=\"63384822\" DRUG_LO_ID=\"20097100\" DRUG_LO_NAME=\"氯化钾注射液\" GANYU_STATE=\"未干预\" GANYU_INFO=\"\">\n" +
//                "            <CheckInfo COLOR=\"黄色\" NAME=\"用法用量\" WARNING_LEVEL=\"慎用\" WARNING_INFO=\"单次剂量超限。药品成分氯化钾的浓度超限。规定的浓度上限为：3.4mg/ml。计算得到的浓度值为：6.0938mg/ml。组号为：63384822。【浓度】钾浓度不超过3.4g/L(45mmol/L)。\" REF_SOURCE=\"15%氯化钾注射液说明书-中国大冢制药有限公司\" YPMC=\"\" JSXX=\"\" ZYJL=\"\" TYSM=\"\" LCSY=\"\"/>\n" +
//                "        </PresInfo>\n" +
//                "    </Order>\n" +
//                "    <Order BED_NO=\"001\" PATIENT_ID=\"000358649700\" VISIT_ID=\"2\" PATIENT_NAME=\"孙健之次子\" DOCTOR_ID=\"10645\" DOCTOR_NAME=\"李秋菊\" DRUG_NAME=\"中/长链脂肪乳注射液(C8-24)\" DRUG_SPEC=\"20% 250ml/瓶\" ORDER_TYPE=\"临时\" START_TIME=\"20190923\" ADMINISTRATION=\"静点\" FREQUENCY=\"ONCE\" DOSAGE=\"38ml\" USE_TIME=\"2019-07-26 15:59:00\" PROBLEM_TYPE=\"\" PROBLEM_LEVEL=\"\" DRUG_STATE=\"\" GROUP_ID=\"63384822\" INPATIENT_NO=\"413076\">\n" +
//                "        <PresInfo ORDER_ID=\"63384822\" ORDER_SUB_ID=\"1\" GROUP_ID=\"63384822\" DRUG_LO_ID=\"00299600\" DRUG_LO_NAME=\"中/长链脂肪乳注射液(C8-24)\" GANYU_STATE=\"未干预\" GANYU_INFO=\"\"/>\n" +
//                "    </Order>\n" +
//                "    <Order BED_NO=\"001\" PATIENT_ID=\"000358649700\" VISIT_ID=\"2\" PATIENT_NAME=\"孙健之次子\" DOCTOR_ID=\"10645\" DOCTOR_NAME=\"李秋菊\" DRUG_NAME=\"浓氯化钠注射液\" DRUG_SPEC=\"1g 10ml/支\" ORDER_TYPE=\"临时\" START_TIME=\"20190923\" ADMINISTRATION=\"静点\" FREQUENCY=\"ONCE\" DOSAGE=\"2.4ml\" USE_TIME=\"2019-07-26 15:59:00\" PROBLEM_TYPE=\"\" PROBLEM_LEVEL=\"\" DRUG_STATE=\"\" GROUP_ID=\"63384822\" INPATIENT_NO=\"413076\">\n" +
//                "        <PresInfo ORDER_ID=\"63384832\" ORDER_SUB_ID=\"5\" GROUP_ID=\"63384822\" DRUG_LO_ID=\"20031400\" DRUG_LO_NAME=\"浓氯化钠注射液\" GANYU_STATE=\"未干预\" GANYU_INFO=\"\"/>\n" +
//                "    </Order>\n" +
//                "    <Order BED_NO=\"001\" PATIENT_ID=\"000358649700\" VISIT_ID=\"2\" PATIENT_NAME=\"孙健之次子\" DOCTOR_ID=\"10645\" DOCTOR_NAME=\"李秋菊\" DRUG_NAME=\"注射用水溶性维生素\" DRUG_SPEC=\"1瓶 /瓶\" ORDER_TYPE=\"临时\" START_TIME=\"20190923\" ADMINISTRATION=\"静点\" FREQUENCY=\"ONCE\" DOSAGE=\"0.05瓶\" USE_TIME=\"2019-07-26 15:59:00\" PROBLEM_TYPE=\"\" PROBLEM_LEVEL=\"\" DRUG_STATE=\"\" GROUP_ID=\"63384822\" INPATIENT_NO=\"413076\">\n" +
//                "        <PresInfo ORDER_ID=\"63384828\" ORDER_SUB_ID=\"3\" GROUP_ID=\"63384822\" DRUG_LO_ID=\"20078000\" DRUG_LO_NAME=\"注射用水溶性维生素\" GANYU_STATE=\"未干预\" GANYU_INFO=\"\"/>\n" +
//                "    </Order>\n" +
//                "    <Order BED_NO=\"222\" PATIENT_ID=\"1000710510\" VISIT_ID=\"1\" PATIENT_NAME=\"贾蓓\" DOCTOR_ID=\"0465\" DOCTOR_NAME=\"俞雨生\" DRUG_NAME=\"培哚普利叔丁胺片☆(施维雅)[市公乙]\" DRUG_SPEC=\"8mg\" ORDER_TYPE=\"临时\" START_TIME=\"20190926\" ADMINISTRATION=\"口服\" FREQUENCY=\"2/日\" DOSAGE=\"8mg\" USE_TIME=\"2019-07-26 15:59:00\" PROBLEM_TYPE=\"\" PROBLEM_LEVEL=\"\" DRUG_STATE=\"\" GROUP_ID=\"01569458160820694600\" INPATIENT_NO=\"111\">\n" +
//                "        <PresInfo ORDER_ID=\"01569458160820694600\" ORDER_SUB_ID=\"1\" GROUP_ID=\"01569458160820694600\" DRUG_LO_ID=\"1504724TA1\" DRUG_LO_NAME=\"培哚普利叔丁胺片☆(施维雅)[市公乙]\" GANYU_STATE=\"未干预\" GANYU_INFO=\"\">\n" +
//                "            <CheckInfo COLOR=\"黄色\" NAME=\"用法用量\" WARNING_LEVEL=\"慎用\" WARNING_INFO=\"单日极量超限。药品说明书规定的单日极量为8mg。处方单次剂量为8mg，给药频次为：2次每1日。医院用法用量中的规则编码：770\" REF_SOURCE=\"药品说明书及医院相关规定\" YPMC=\"\" JSXX=\"\" ZYJL=\"\" TYSM=\"\" LCSY=\"\"/>\n" +
//                "            <CheckInfo COLOR=\"红色\" NAME=\"用法用量\" WARNING_LEVEL=\"强制阻断\" WARNING_INFO=\"使用频次超限。药品说明书规定的每日最大使用频次为1。处方的每日频次为2。医院用法用量中的规则编码：770\" REF_SOURCE=\"药品说明书及医院相关规定\" YPMC=\"\" JSXX=\"\" ZYJL=\"\" TYSM=\"\" LCSY=\"\"/>\n" +
//                "        </PresInfo>\n" +
//                "    </Order>\n" +
//                "    <Order BED_NO=\"222\" PATIENT_ID=\"1000710510\" VISIT_ID=\"1\" PATIENT_NAME=\"贾蓓\" DOCTOR_ID=\"0465\" DOCTOR_NAME=\"俞雨生\" DRUG_NAME=\"新养肾丸☆[公.1][乙10]\" DRUG_SPEC=\"60g*60\" ORDER_TYPE=\"临时\" START_TIME=\"20190926\" ADMINISTRATION=\"口服\" FREQUENCY=\"2/日\" DOSAGE=\"3g\" USE_TIME=\"2019-07-26 15:59:00\" PROBLEM_TYPE=\"\" PROBLEM_LEVEL=\"\" DRUG_STATE=\"\" GROUP_ID=\"01569458160820693912\" INPATIENT_NO=\"111\">\n" +
//                "        <PresInfo ORDER_ID=\"01569458160820693912\" ORDER_SUB_ID=\"1\" GROUP_ID=\"01569458160820693912\" DRUG_LO_ID=\"3050326PL1\" DRUG_LO_NAME=\"新养肾丸☆[公.1][乙10]\" GANYU_STATE=\"未干预\" GANYU_INFO=\"\"/>\n" +
//                "    </Order>\n" +
//                "    <Order BED_NO=\"001\" PATIENT_ID=\"000358649700\" VISIT_ID=\"2\" PATIENT_NAME=\"孙健之次子\" DOCTOR_ID=\"10645\" DOCTOR_NAME=\"李秋菊\" DRUG_NAME=\"氯化钾注射液\" DRUG_SPEC=\"1.5g 10ml/支\" ORDER_TYPE=\"临时\" START_TIME=\"20190923\" ADMINISTRATION=\"静点\" FREQUENCY=\"ONCE\" DOSAGE=\"1.3ml\" USE_TIME=\"2019-07-26 15:59:00\" PROBLEM_TYPE=\"\" PROBLEM_LEVEL=\"\" DRUG_STATE=\"\" GROUP_ID=\"63384822\" INPATIENT_NO=\"413076\">\n" +
//                "        <PresInfo ORDER_ID=\"63384836\" ORDER_SUB_ID=\"2\" GROUP_ID=\"63384822\" DRUG_LO_ID=\"20097100\" DRUG_LO_NAME=\"氯化钾注射液\" GANYU_STATE=\"未干预\" GANYU_INFO=\"\">\n" +
//                "            <CheckInfo COLOR=\"黄色\" NAME=\"用法用量\" WARNING_LEVEL=\"慎用\" WARNING_INFO=\"单次剂量超限。药品成分氯化钾的浓度超限。规定的浓度上限为：3.4mg/ml。计算得到的浓度值为：6.0938mg/ml。组号为：63384822。【浓度】钾浓度不超过3.4g/L(45mmol/L)。\" REF_SOURCE=\"15%氯化钾注射液说明书-中国大冢制药有限公司\" YPMC=\"\" JSXX=\"\" ZYJL=\"\" TYSM=\"\" LCSY=\"\"/>\n" +
//                "        </PresInfo>\n" +
//                "    </Order>\n" +
//                "    <Order BED_NO=\"001\" PATIENT_ID=\"000358649700\" VISIT_ID=\"2\" PATIENT_NAME=\"孙健之次子\" DOCTOR_ID=\"10645\" DOCTOR_NAME=\"李秋菊\" DRUG_NAME=\"氯化钾注射液\" DRUG_SPEC=\"1.5g 10ml/支\" ORDER_TYPE=\"临时\" START_TIME=\"20190923\" ADMINISTRATION=\"静点\" FREQUENCY=\"ONCE\" DOSAGE=\"1.3ml\" USE_TIME=\"2019-07-26 15:59:00\" PROBLEM_TYPE=\"\" PROBLEM_LEVEL=\"\" DRUG_STATE=\"\" GROUP_ID=\"63384822\" INPATIENT_NO=\"413076\">\n" +
//                "        <PresInfo ORDER_ID=\"63384838\" ORDER_SUB_ID=\"2\" GROUP_ID=\"63384822\" DRUG_LO_ID=\"20097100\" DRUG_LO_NAME=\"氯化钾注射液\" GANYU_STATE=\"未干预\" GANYU_INFO=\"\">\n" +
//                "            <CheckInfo COLOR=\"黄色\" NAME=\"用法用量\" WARNING_LEVEL=\"慎用\" WARNING_INFO=\"单次剂量超限。药品成分氯化钾的浓度超限。规定的浓度上限为：3.4mg/ml。计算得到的浓度值为：6.0938mg/ml。组号为：63384822。【浓度】钾浓度不超过3.4g/L(45mmol/L)。\" REF_SOURCE=\"15%氯化钾注射液说明书-中国大冢制药有限公司\" YPMC=\"\" JSXX=\"\" ZYJL=\"\" TYSM=\"\" LCSY=\"\"/>\n" +
//                "        </PresInfo>\n" +
//                "    </Order>\n" +
//                "    <Order BED_NO=\"001\" PATIENT_ID=\"000358649700\" VISIT_ID=\"2\" PATIENT_NAME=\"孙健之次子\" DOCTOR_ID=\"10645\" DOCTOR_NAME=\"李秋菊\" DRUG_NAME=\"氯化钾注射液\" DRUG_SPEC=\"1.5g 10ml/支\" ORDER_TYPE=\"临时\" START_TIME=\"20190923\" ADMINISTRATION=\"静点\" FREQUENCY=\"ONCE\" DOSAGE=\"1.3ml\" USE_TIME=\"2019-07-26 15:59:00\" PROBLEM_TYPE=\"\" PROBLEM_LEVEL=\"\" DRUG_STATE=\"\" GROUP_ID=\"63384822\" INPATIENT_NO=\"413076\">\n" +
//                "        <PresInfo ORDER_ID=\"63384838\" ORDER_SUB_ID=\"2\" GROUP_ID=\"63384822\" DRUG_LO_ID=\"20097100\" DRUG_LO_NAME=\"氯化钾注射液\" GANYU_STATE=\"未干预\" GANYU_INFO=\"\">\n" +
//                "            <CheckInfo COLOR=\"黄色\" NAME=\"用法用量\" WARNING_LEVEL=\"慎用\" WARNING_INFO=\"单次剂量超限。药品成分氯化钾的浓度超限。规定的浓度上限为：3.4mg/ml。计算得到的浓度值为：6.0938mg/ml。组号为：63384822。【浓度】钾浓度不超过3.4g/L(45mmol/L)。\" REF_SOURCE=\"15%氯化钾注射液说明书-中国大冢制药有限公司\" YPMC=\"\" JSXX=\"\" ZYJL=\"\" TYSM=\"\" LCSY=\"\"/>\n" +
//                "        </PresInfo>\n" +
//                "    </Order>\n" +
//                "    <Order BED_NO=\"001\" PATIENT_ID=\"000358649700\" VISIT_ID=\"2\" PATIENT_NAME=\"孙健之次子\" DOCTOR_ID=\"10645\" DOCTOR_NAME=\"李秋菊\" DRUG_NAME=\"氯化钾注射液\" DRUG_SPEC=\"1.5g 10ml/支\" ORDER_TYPE=\"临时\" START_TIME=\"20190923\" ADMINISTRATION=\"静点\" FREQUENCY=\"ONCE\" DOSAGE=\"1.3ml\" USE_TIME=\"2019-07-26 15:59:00\" PROBLEM_TYPE=\"\" PROBLEM_LEVEL=\"\" DRUG_STATE=\"\" GROUP_ID=\"63384822\" INPATIENT_NO=\"413076\">\n" +
//                "        <PresInfo ORDER_ID=\"63384838\" ORDER_SUB_ID=\"2\" GROUP_ID=\"63384822\" DRUG_LO_ID=\"20097100\" DRUG_LO_NAME=\"氯化钾注射液\" GANYU_STATE=\"未干预\" GANYU_INFO=\"\">\n" +
//                "            <CheckInfo COLOR=\"黄色\" NAME=\"用法用量\" WARNING_LEVEL=\"慎用\" WARNING_INFO=\"单次剂量超限。药品成分氯化钾的浓度超限。规定的浓度上限为：3.4mg/ml。计算得到的浓度值为：6.0938mg/ml。组号为：63384822。【浓度】钾浓度不超过3.4g/L(45mmol/L)。\" REF_SOURCE=\"15%氯化钾注射液说明书-中国大冢制药有限公司\" YPMC=\"\" JSXX=\"\" ZYJL=\"\" TYSM=\"\" LCSY=\"\"/>\n" +
//                "        </PresInfo>\n" +
//                "    </Order>\n" +
//                "    <Order BED_NO=\"001\" PATIENT_ID=\"000358649700\" VISIT_ID=\"2\" PATIENT_NAME=\"孙健之次子\" DOCTOR_ID=\"10645\" DOCTOR_NAME=\"李秋菊\" DRUG_NAME=\"氯化钾注射液\" DRUG_SPEC=\"1.5g 10ml/支\" ORDER_TYPE=\"临时\" START_TIME=\"20190923\" ADMINISTRATION=\"静点\" FREQUENCY=\"ONCE\" DOSAGE=\"1.3ml\" USE_TIME=\"2019-07-26 15:59:00\" PROBLEM_TYPE=\"\" PROBLEM_LEVEL=\"\" DRUG_STATE=\"\" GROUP_ID=\"63384822\" INPATIENT_NO=\"413076\">\n" +
//                "        <PresInfo ORDER_ID=\"73384838\" ORDER_SUB_ID=\"2\" GROUP_ID=\"63384822\" DRUG_LO_ID=\"20097100\" DRUG_LO_NAME=\"氯化钾注射液\" GANYU_STATE=\"未干预\" GANYU_INFO=\"\">\n" +
//                "            <CheckInfo COLOR=\"黄色\" NAME=\"用法用量\" WARNING_LEVEL=\"慎用\" WARNING_INFO=\"单次剂量超限。药品成分氯化钾的浓度超限。规定的浓度上限为：3.4mg/ml。计算得到的浓度值为：6.0938mg/ml。组号为：63384822。【浓度】钾浓度不超过3.4g/L(45mmol/L)。\" REF_SOURCE=\"15%氯化钾注射液说明书-中国大冢制药有限公司\" YPMC=\"\" JSXX=\"\" ZYJL=\"\" TYSM=\"\" LCSY=\"\"/>\n" +
//                "        </PresInfo>\n" +
//                "    </Order>\n" +
//                "    <Order BED_NO=\"001\" PATIENT_ID=\"000358649700\" VISIT_ID=\"2\" PATIENT_NAME=\"孙健之次子\" DOCTOR_ID=\"10645\" DOCTOR_NAME=\"李秋菊\" DRUG_NAME=\"氯化钾注射液\" DRUG_SPEC=\"1.5g 10ml/支\" ORDER_TYPE=\"临时\" START_TIME=\"20190923\" ADMINISTRATION=\"静点\" FREQUENCY=\"ONCE\" DOSAGE=\"1.3ml\" USE_TIME=\"2019-07-26 15:59:00\" PROBLEM_TYPE=\"\" PROBLEM_LEVEL=\"\" DRUG_STATE=\"\" GROUP_ID=\"63384822\" INPATIENT_NO=\"413076\">\n" +
//                "        <PresInfo ORDER_ID=\"7384838\" ORDER_SUB_ID=\"2\" GROUP_ID=\"63384822\" DRUG_LO_ID=\"20097100\" DRUG_LO_NAME=\"氯化钾注射液\" GANYU_STATE=\"未干预\" GANYU_INFO=\"\">\n" +
//                "            <CheckInfo COLOR=\"黄色\" NAME=\"用法用量\" WARNING_LEVEL=\"慎用\" WARNING_INFO=\"单次剂量超限。药品成分氯化钾的浓度超限。规定的浓度上限为：3.4mg/ml。计算得到的浓度值为：6.0938mg/ml。组号为：63384822。【浓度】钾浓度不超过3.4g/L(45mmol/L)。\" REF_SOURCE=\"15%氯化钾注射液说明书-中国大冢制药有限公司\" YPMC=\"\" JSXX=\"\" ZYJL=\"\" TYSM=\"\" LCSY=\"\"/>\n" +
//                "        </PresInfo>\n" +
//                "    </Order>\n" +
//                "    <Order BED_NO=\"001\" PATIENT_ID=\"000358649700\" VISIT_ID=\"2\" PATIENT_NAME=\"孙健之次子\" DOCTOR_ID=\"10645\" DOCTOR_NAME=\"李秋菊\" DRUG_NAME=\"氯化钾注射液\" DRUG_SPEC=\"1.5g 10ml/支\" ORDER_TYPE=\"临时\" START_TIME=\"20190923\" ADMINISTRATION=\"静点\" FREQUENCY=\"ONCE\" DOSAGE=\"1.3ml\" USE_TIME=\"2019-07-26 15:59:00\" PROBLEM_TYPE=\"\" PROBLEM_LEVEL=\"\" DRUG_STATE=\"\" GROUP_ID=\"63384822\" INPATIENT_NO=\"413076\">\n" +
//                "        <PresInfo ORDER_ID=\"8384838\" ORDER_SUB_ID=\"2\" GROUP_ID=\"63384822\" DRUG_LO_ID=\"20097100\" DRUG_LO_NAME=\"氯化钾注射液\" GANYU_STATE=\"未干预\" GANYU_INFO=\"\">\n" +
//                "            <CheckInfo COLOR=\"黄色\" NAME=\"用法用量\" WARNING_LEVEL=\"慎用\" WARNING_INFO=\"单次剂量超限。药品成分氯化钾的浓度超限。规定的浓度上限为：3.4mg/ml。计算得到的浓度值为：6.0938mg/ml。组号为：63384822。【浓度】钾浓度不超过3.4g/L(45mmol/L)。\" REF_SOURCE=\"15%氯化钾注射液说明书-中国大冢制药有限公司\" YPMC=\"\" JSXX=\"\" ZYJL=\"\" TYSM=\"\" LCSY=\"\"/>\n" +
//                "        </PresInfo>\n" +
//                "    </Order>\n" +
//                "</OrderList>";
        if(checkXml == null || checkXml.equals("")){
            message.setHasProblem(-2);
            return message;
        }
        checkXml = checkXml.replace("&nbsp;"," ");
        logger.debug(checkXml);
        pharmacistInfo = parseXML.parsePharmacistInfo(pharmacistInfoXML);

        List<Order> orderList = parseXML.parseXML_BZ(checkXml);
        //hasproblem   -2:中间层有问题   0：审核出药品没有问题  1：审核出药品有问题
        message = handleCheckXml_BZ(orderList);
        return message;
    }


    /**
     * @param
     */
    public CheckMessage handleCheckXml_BZ(List<Order> orderList) {

        // 生成随机数+ 时间戳  唯一id
        Random random = new Random();
        String ranNo = String.valueOf(random.nextInt(10000));
        ranNo = CommonUtil.getPresIdWithTime(ranNo);

        // 按group_id或者order_id进行医嘱分组，并对同一组医嘱按疾病严重程度排序
        List<OrderInfo> orderInfoList = sortgroupOrder_BZ(orderList,groupFlag);


        // 缓存审核结果,等到进入审核结果页面时再读取记录显示，以及用于排序
        PharmacistCheckResultCache.putPharmacistCheckResult_BZ(ranNo, orderInfoList);

        //此处默认按姓名升序排列
        getOrderListByCondition(orderInfoList,1);

        //返回结果
        CheckMessage message = new CheckMessage();
        if(orderInfoList==null||orderInfoList.size()==0){
            //没有问题
            message.setHasProblem(0);
        }else{
            message.setHasProblem(1);
            message.setPresId(ranNo);
        }


        return message;
    }


    /**
     * add by Yikmat @2019.10.10
     * 将原始问题医嘱列表group_id或者order_no相同为一组，同一组的医嘱在一起，
     * 计算组中最高的问题级别，患者姓名，床号  都提取出来，以便后续排序
     * flag为1表示按照group_id分组，flag为2表示按照order_no分组
     * @param
     * @return
     */
    public  List<OrderInfo> sortgroupOrder_BZ(List<Order> orderList,String flag){

        Map<BigDecimal,OrderInfo> map = new HashMap<BigDecimal, OrderInfo>();

        // 按order_id或group_id分组
        for(Order order : orderList){
            String s_id = "";
            // 区分按照group_id分组还是按照order_no分组
            if("1".equals(flag)){
                if(order.getPrescInfo().getGroup_id() == null || "".equals(order.getPrescInfo().getGroup_id())){
                    order.getPrescInfo().setGroup_id("0");
                }
                s_id = order.getPrescInfo().getGroup_id().replaceAll("[^\\d]+", "");
            }else if("2".equals(flag)){
                if(order.getPrescInfo().getOrder_id() == null || "".equals(order.getPrescInfo().getOrder_id())){
                    order.getPrescInfo().setOrder_id("0");
                }
                s_id = order.getPrescInfo().getOrder_id().replaceAll("[^\\d]+", "");
            }
            // 遍历list，将一组的医嘱存入同一个list中
            if(!map.containsKey(new BigDecimal(s_id))){
                OrderInfo orderInfo = new OrderInfo();
//                List<Order> tempList = new ArrayList<Order>();
//                tempList.add(order);
                orderInfo.getOrderList().add(order);
                map.put(new BigDecimal(s_id),orderInfo);
            }else{
                map.get(new BigDecimal(s_id)).getOrderList().add(order);
            }
        }

        // 遍历map中的成组医嘱，去除没有问题的医嘱组;有问题的医嘱组，在组内按照问题等级排列，并提取出床号、患者姓名、最严重问题等级
        Map<BigDecimal,OrderInfo> newMap = new HashMap<BigDecimal, OrderInfo>();
        int orderGroupId = 0;
        for(Map.Entry<BigDecimal,OrderInfo> entry : map.entrySet()){
            BigDecimal mapKey = entry.getKey();
            OrderInfo mapValue = entry.getValue();
            for(Order order:mapValue.getOrderList()){
                if(order.getPrescInfo().getCheckInfos()!=null&&order.getPrescInfo().getCheckInfos().size()!=0){
                    //  组内对问题严重等级排序，在此时将成组的医嘱按照问题级别从高到低排好序
                    List<Order> newOrderList =  sortSameGroupOrder_BZ(mapValue.getOrderList());
                    mapValue.setPROBLEM_LEVEL(newOrderList.get(0).getPROBLEM_LEVEL());
                    mapValue.setBED_NO(order.getBED_NO());
                    mapValue.setPATIENT_NAME(order.getPATIENT_NAME());
                    mapValue.setOrderGroupId(orderGroupId);
                    mapValue.setOrderList(newOrderList);
                    newMap.put(mapKey,mapValue);
                    orderGroupId +=1;
                    break;
                }
            }
        }

        //将map转为List
        List<OrderInfo> orderInfoList = new LinkedList<OrderInfo>();
        for(OrderInfo orderInfo : newMap.values()){
            orderInfoList.add(orderInfo);
        }
        return orderInfoList;
    }


    /**
     *  * add by Yikmat @2019.10.10
     * 对同一分组内的医嘱  按问题重要性进行排序
     */
    public List<Order> sortSameGroupOrder_BZ(List<Order> orderList) {

        List<Order> newOrderList = new LinkedList<Order>();
        Map<Integer, List<Order>> map = new HashMap<Integer, List<Order>>();

        for (Order order : orderList) {
            int warning_level = 0;
            ProblemInfo problemInfo = parseXML.getPharHighestWarningLevel_BZ(order.getPrescInfo());
            warning_level = problemInfo.getProblem_level();
            if (!map.containsKey(warning_level)) {
                List<Order> tempList = new LinkedList<Order>();
                tempList.add(order);
                map.put(warning_level, tempList);
            } else {
                map.get(warning_level).add(order);
            }
        }

        for (int i = 3; i >= 0; --i) {
            List<Order> tempList = map.get(i);
            if (tempList != null && tempList.size() != 0) {
                for (Order order : tempList) {
                    newOrderList.add(order);
                }
            }
        }

          //给同组的药品 加上连线
           newOrderList =  addGroupLineForSameGroup(newOrderList);

            newOrderList.get(0).setPROBLEM_TYPE(parseXML.getPharHighestWarningLevel_BZ(newOrderList.get(0).getPrescInfo()).getProblem_type());
            newOrderList.get(0).setPROBLEM_LEVEL(parseXML.getPharHighestWarningLevel_BZ(newOrderList.get(0).getPrescInfo()).getProblem_level_name());
            return newOrderList;
        }

    /**
     * 将同组的医嘱用括号括起来
     * @param list
     * @return
     */
    public List<Order> addGroupLineForSameGroup(List<Order> list){
        int x = 0;
        for(int i=0;i<list.size();++i){
            PrescInfo prescInfo = list.get(i).getPrescInfo();
            if(list.size() > 1){
                if(x == 0){
                    prescInfo.setKh("┍ ");
                }
                else if(x == list.size()-1){
                    prescInfo.setKh("┕ ");
                }
                else{
                    prescInfo.setKh("|");
                }
            }else{
                prescInfo.setKh("");
            }
            ++x;
        }
        return list;
    }

    /**
     * add by Yikmat 2019.10.10
     * 按要求对list进行排序
     * type 1 按床号升序，2 按床号降， 3 姓名升序 4 姓名降序， 5 按问题严重程度升序， 6 按问题严重程度降序
     */
    public List<OrderInfo> getOrderListByCondition(List<OrderInfo> infoList,int type){
        Boolean condition = true;

        for(int i = 0; i < infoList.size()-1; i++){
            for(int j = 1; j < infoList.size()-i; j++){
                if(type == 3){
                    condition =  new ChineseCharToEn().getPinYinFChar(infoList.get(j-1).getPATIENT_NAME()).compareTo(new ChineseCharToEn().getPinYinFChar(infoList.get(j).getPATIENT_NAME())) > 0;
                }else if(type == 4){
                    condition =   new ChineseCharToEn().getPinYinFChar(infoList.get(j-1).getPATIENT_NAME()).compareTo(new ChineseCharToEn().getPinYinFChar(infoList.get(j).getPATIENT_NAME())) < 0;
                } else if(type == 1){
                    condition = Integer.valueOf(infoList.get(j-1).getBED_NO()) > Integer.valueOf(infoList.get(j).getBED_NO());
                }else if(type == 2){
                    condition = Integer.valueOf(infoList.get(j-1).getBED_NO()) < Integer.valueOf(infoList.get(j).getBED_NO());
                }else if(type == 5){
                    condition = parseXML.getRegularWarnLevel_BZ(infoList.get(j-1).getPROBLEM_LEVEL()) > parseXML.getRegularWarnLevel_BZ(infoList.get(j).getPROBLEM_LEVEL());
                }else if(type == 6){
                    condition = parseXML.getRegularWarnLevel_BZ(infoList.get(j-1).getPROBLEM_LEVEL()) < parseXML.getRegularWarnLevel_BZ(infoList.get(j).getPROBLEM_LEVEL());
                }
                if (condition) { // 比较两个整数的大小
                    OrderInfo  a = infoList.get(j-1);
                    infoList.set((j-1), infoList.get(j));
                    infoList.set(j, a);
                }
            }
        }

        return infoList;
    }

    public List<OrderInfo> findPharmacistCheckResult_BZ(String presId) {
        return PharmacistCheckResultCache.findPharmacistCheckResult_BZ(presId);
    }

    public void removePharmacistCheckResult_BZ(String presId) {
         PharmacistCheckResultCache.removePharmacistCheckResult_BZ(presId);
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------
}
