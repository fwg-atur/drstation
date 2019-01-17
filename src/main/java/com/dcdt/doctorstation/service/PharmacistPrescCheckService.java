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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        long min = -1;
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
                if(min == -1 || Long.parseLong(n_group_id)< min){
                    min = Long.parseLong(n_group_id);
                }
            }

            //tempList存放：group_id等于这次遍历最小值的处方
            List<PrescInfo> tempList = new ArrayList<PrescInfo>();
            for(int k=0;k<newList.size();++k){
                PrescInfo prescInfo = newList.get(k);
                String n_group_id = prescInfo.getGroup_id().replaceAll("[^\\d]+", "");

                if(Long.parseLong(n_group_id) == min){
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
            min = -1;
        }
        return finalList;
    }

    //对同一级别的问题按照order_id排序
    @Deprecated
    public List<PrescInfo> sortSameLevelOrderNo(List<PrescInfo> newList){
        //finalList存放：按照order_id从小到到排列,按照order_sub_id从小到大排列
        List<PrescInfo> finalList = new ArrayList<PrescInfo>();

        //min存放：order_id一轮的最小值
        long min = -1;
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
                if(min == -1 || Long.parseLong(n_order_id) < min){
                    min = Long.parseLong(n_order_id);
                }
            }
            //tempList存放：order_id等于这次遍历最小值的处方
            List<PrescInfo> tempList = new ArrayList<PrescInfo>();
            for(int k=0;k<newList.size();++k){
                PrescInfo prescInfo = newList.get(k);
                String n_order_id = prescInfo.getOrder_id().replaceAll("[^\\d]+", "");

                if(Long.parseLong(n_order_id) == min){
                    prescInfo.setOrder_id_flag(true);
                    tempList.add(prescInfo);
                }
            }

            //min2存放：order_id等于这次遍历最小值处方中的order_sub_id最小值
            long min2 = -1;
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
                    if(min2 == -1 || Long.parseLong(prescInfo.getOrder_sub_id()) < min2){
                        min2 = Long.parseLong(prescInfo.getOrder_sub_id());
                    }
                }
                //将order_sub_id等于最小值的处方加入到finalList中，并将order_sub_id_flag改为true表明已经处理过
                for(int l=0;l<tempList.size();++l){
                    PrescInfo prescInfo = tempList.get(l);
                    if(prescInfo.isOrder_sub_id_flag() || "".equals(prescInfo.getOrder_sub_id())){
                        continue;
                    }
                    if(Long.parseLong(prescInfo.getOrder_sub_id()) == min2){
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
                min2 = -1;
            }
            //将min改为-1，进行下一次寻找order_id的最小值
            min = -1;
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
        Map<Long,List<PrescInfo>> map = new HashMap<Long, List<PrescInfo>>();
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
            if(!map.containsKey(Long.parseLong(s_id))){
                List<PrescInfo> tempList = new ArrayList<PrescInfo>();
                tempList.add(prescInfo);
                map.put(Long.parseLong(s_id),tempList);
            }else{
                map.get(Long.parseLong(s_id)).add(prescInfo);
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

}
