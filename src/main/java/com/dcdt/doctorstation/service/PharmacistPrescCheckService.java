package com.dcdt.doctorstation.service;

import com.dcdt.cache.PharmacistCheckResultCache;
import com.dcdt.cache.RetValCache;
import com.dcdt.doctorstation.entity.*;
import com.dcdt.utils.CommonUtil;
import com.dcdt.utils.HttpUtil;
import com.dcdt.utils.ParseXML;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    private CacheService cacheService;

    private PharmacistInfo pharmacistInfo;
    private ParseXML parseXML = new ParseXML();
    private String date;



    private static final Logger logger = LoggerFactory.getLogger(PharmacistPrescCheckService.class);

    /**

     * @param xml
     * @return 返回审核结果标识（flag,presId）,不包括审核结果字符串
     */
    public CheckMessage checkPharmacistPresc(String patientID,String visitDate,String pharmacistInfoXML, String xml) {
        String url = pharmacistCheckServerUrl + "?patientID=" + patientID + "&visitDate=" + visitDate;
        xml = xml.replace("&nbsp;"," ");
        String checkXml = HttpUtil.sendPost(url, "");
        checkXml = checkXml.replace("&nbsp;"," ");
        logger.debug(checkXml);
//        String checkXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><CheckList><Check><CheckInput TAG=\"2\">    <Doctor NAME=\"门急诊医生\" POSITION=\"副主任医师\" USER_ID=\"123\" DEPT_NAME=\"骨科\" DEPT_CODE=\"030401\"/>    <Patient NAME=\"门急诊病人1\" ID=\"1\" VISIT_ID=\"1233\" PATIENT_PRES_ID=\"1\" BIRTH=\"19840908\" HEIGHT=\"100\" WEIGHT=\"20\" GENDER=\"男\" PREGNANT=\"是\" LACT=\"是\" HEPATICAL=\"是\" RENAL=\"否\" PANCREAS=\"否\" ALERGY_DRUGS=\"选择性5-HT3受体抑制药类\" IDENTITY_TYPE=\"军人\" FEE_TYPE=\"医保\" SCR=\"\" SCR_UNIT=\"\" GESTATION_AGE=\"\" PRETERM_BIRTH=\"\" DRUG_HISTORY=\"\" FAMILY_DISEASE_HISTORY=\"\" GENETIC_DISEASE=\"\" MEDICARE_01=\"\" MEDICARE_02=\"\" MEDICARE_03=\"\" MEDICARE_04=\"\" MEDICARE_05=\"\"/>    <Diagnosises DIAGNOSISES=\"_感染\"/>    <Advices>        <Advice DRUG_LO_ID=\"0101003CP0\" DRUG_LO_NAME=\"阿莫西林胶囊\" ADMINISTRATION=\"静滴\" DOSAGE=\"5\" DOSAGE_UNIT=\"粒\" FREQ_COUNT=\"1\" FREQ_INTERVAL=\"1\" FREQ_INTERVAL_UNIT=\"日\" START_DAY=\"20150114\" END_DAY=\"20150114\" REPEAT=\"0\" ORDER_NO=\"1\" ORDER_SUB_NO=\"2\" DEPT_CODE=\"2426\" DOCTOR_NAME=\"闫洪生\" TITLE=\"副主任医师\" AUTHORITY_LEVELS=\"\" ALERT_LEVELS=\"1,2\" GROUP_ID=\"\" USER_ID=\"123\" PRES_ID=\"1\" PRES_DATE=\"20150114\" PRES_SEQ_ID=\"3138829120150114\" PK_ORDER_NO=\"\" COURSE=\"\" PKG_COUNT=\"101\" PKG_UNIT=\"粒\" BAK_01=\"\" BAK_02=\"\" BAK_03=\"胶囊\" BAK_04=\"0.25g\" BAK_05=\"昆明贝克诺\"/>        <Advice DRUG_LO_ID=\"3012302CP0\" DRUG_LO_NAME=\"安替可胶囊\" ADMINISTRATION=\"\" DOSAGE=\"100\" DOSAGE_UNIT=\"\" FREQ_COUNT=\"5\" FREQ_INTERVAL=\"1\" FREQ_INTERVAL_UNIT=\"日\" START_DAY=\"20150114\" END_DAY=\"20150114\" REPEAT=\"0\" ORDER_NO=\"6\" ORDER_SUB_NO=\"1\" DEPT_CODE=\"骨科\" DOCTOR_NAME=\"朱宏勋\" TITLE=\"主任医师\" AUTHORITY_LEVELS=\"\" ALERT_LEVELS=\"1,2\" GROUP_ID=\"\" USER_ID=\"009284\" PRES_ID=\"1\" PRES_DATE=\"20150114\" PRES_SEQ_ID=\"3138829120150114\" PK_ORDER_NO=\"\" COURSE=\"\" PKG_COUNT=\"2\" PKG_UNIT=\"\" BAK_01=\"\" BAK_02=\"\" BAK_03=\"\" BAK_04=\"0.22g\" BAK_05=\"\"/>    </Advices></CheckInput><CheckOutput AntiDrugPos=\"\">    <PresInfo ORDER_ID=\"1\" ORDER_SUB_ID=\"2\" DRUG_LO_ID=\"0101003CP0\" DRUG_LO_NAME=\"阿莫西林胶囊\">        <CheckInfo COLOR=\"红色\" NAME=\"医院管理\" WARNING_LEVEL=\"禁用\" WARNING_INFO=\"患者月最大定量：    100.00，患者已用：    202.00，当前处方用量：    101.00\" REF_SOURCE=\"医院药控相关规定。\" YPMC=\"\" JSXX=\"\" ZYJL=\"\" TYSM=\"\" LCSY=\"\"/>    </PresInfo>    <PresInfo ORDER_ID=\"6\" ORDER_SUB_ID=\"1\" DRUG_LO_ID=\"3012302CP0\" DRUG_LO_NAME=\"安替可胶囊\">        <CheckInfo COLOR=\"黄色\" NAME=\"适应症\" WARNING_LEVEL=\"慎用\" WARNING_INFO=\"超适应症用药。药品说明书适应症为：。医生诊断为：_感染\" REF_SOURCE=\"药品说明书及医院相关规定\" YPMC=\"\" JSXX=\"\" ZYJL=\"\" TYSM=\"\" LCSY=\"\"/>    </PresInfo></CheckOutput><CheckPharmacist>我觉得这个处方很好</CheckPharmacist></Check></CheckList>";
        pharmacistInfo = parseXML.parsePharmacistInfo(pharmacistInfoXML);
        date = visitDate;
        CheckMessage message = handleCheckXml(checkXml,xml);
        putXML2Cache(message.getPresId(), checkXml);
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
        check.getCheckPresOutput().setPrescInfos(sortCheckResult(list));

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

    public List<PrescInfo> sortCheckResult(List<PrescInfo> prescInfos){
        List<PrescInfo> newList = new ArrayList<PrescInfo>();
        if(prescInfos == null || prescInfos.size() <= 1){
            return prescInfos;
        }
        for(int i=0;i<4;++i) {
            for (PrescInfo prescInfo : prescInfos) {
                List<CheckInfo> checkInfos = prescInfo.getCheckInfos();
                if(i == 0 && getHighestLevelFromCheckInfoList(checkInfos) == 3){
                    newList.add(prescInfo);
                }else if(i == 1 && getHighestLevelFromCheckInfoList(checkInfos) == 2){
                    newList.add(prescInfo);
                }else if(i == 2 && getHighestLevelFromCheckInfoList(checkInfos) == 1){
                    newList.add(prescInfo);
                }else if(i == 3 && getHighestLevelFromCheckInfoList(checkInfos) == 0){
                    newList.add(prescInfo);
                }
            }
        }
        return newList;

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
