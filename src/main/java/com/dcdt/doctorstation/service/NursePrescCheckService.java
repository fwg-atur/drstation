package com.dcdt.doctorstation.service;

import com.dcdt.cache.NurseCheckResultCache;
import com.dcdt.cache.RetValCache;
import com.dcdt.doctorstation.entity.*;
import com.dcdt.utils.HttpUtil;
import com.dcdt.utils.ParseXML;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wtwang on 2018/12/21.
 */
@Service
public class NursePrescCheckService {
    @Value("${nurseCheckServerUrl}")
    private String nurseCheckServerUrl;

    private CacheService cacheService;

    private List<NurseCheckResult> nurseCheckResults;

    private static final Logger logger = Logger.getLogger(NursePrescCheckService.class);

    /**
     * 测试用
     * @param data
     * @return
     */
    public CheckMessage checkNursePrescForTest(String data) {
        String checkJson = "<CheckList><Check><CheckInput><Doctor POSITION=\"副主任\" NAME=\"武欣\" DEPT_CODE=\"030401\" DEPT_NAME=\"(杨)产科(1B)\" USER_ID=\"6230\"/><Patient NAME=\"范芸莹\" ID=\"26384916\" GENDER=\"女\" BIRTH=\"20150403\" WEIGHT=\"\" HEIGHT=\"\" ALERGY_DRUGS=\"\" PREGNANT=\"是\" LACT=\"否\" HEPATICAL=\"\" RENAL=\"\" PANCREAS=\"\" VISIT_ID=\"1-1\" PATIENT_PRES_ID=\"2018112800084\" IDENTITY_TYPE=\"\" FEE_TYPE=\"\" SCR=\"\" SCR_UNIT=\"umol/L\" GESTATION_AGE=\"12\" PRETERM_BIRTH=\"\" DRUG_HISTORY=\"\" FAMILY_DISEASE_HISTORY=\"\" GENETIC_DISEASE=\"\" MEDICARE_01=\"\" MEDICARE_02=\"\" MEDICARE_03=\"\" MEDICARE_04=\"\" MEDICARE_05=\"\" WARD_CODE=\"\" WARD_NAME=\"\" BED_NO=\"\" INPATIENT_NO=\"\"/><Diagnosises DIAGNOSISES=\"阿尔茨海默病\"/><Advices><Advice REPEAT=\"\" DRUG_LO_NAME=\"注射用替考拉宁(进口)\" DRUG_LO_ID=\"010101\" ADMINISTRATION=\"口服\" DOSAGE=\"0.4\" DOSAGE_UNIT=\"克\" FREQ_COUNT=\"1\" FREQ_INTERVAL=\"1\" FREQ_INTERVAL_UNIT=\"日\" START_DAY=\"20181128\" END_DAY=\"\" DEPT_CODE=\"23940\" DOCTOR_NAME=\"武欣\" ORDER_NO=\"1\" ORDER_SUB_NO=\"1\" AUTHORITY_LEVELS=\"\" ALERT_LEVELS=\"\" TITLE=\"副主任\" GROUP_ID=\"1\" USER_ID=\"6230\" PRES_ID=\"2018112800084\" PRES_DATE=\"20181128\" PRES_SEQ_ID=\"\" PK_ORDER_NO=\"\" COURSE=\"24\" PKG_COUNT=\"1\" PKG_UNIT=\"盒\" BAK_01=\"\" BAK_02=\"\" BAK_03=\"片剂\" BAK_04=\"12克\" BAK_05=\"杨子制药\" PERFORM_SCHEDULE=\"10|16|20\"/></Advices></CheckInput><CheckOutput><PresInfo ORDER_ID=\"1\" ORDER_SUB_ID=\"1\" DRUG_LO_ID=\"010101\" DRUG_LO_NAME=\"注射用替考拉宁(进口)\" GANYU_STATE=\"未干预\" GANYU_INFO=\"\"><CheckInfo COLOR=\"红色\" NAME=\"适应症\" WARNING_LEVEL=\"禁用\" WARNING_INFO=\"超适应症用药。药品说明书适应症为：肚子疼、屁股疼、脑子疼、心房颤动。医生诊断为：阿尔茨海默病\" REF_SOURCE=\"临床用药指南\" YPMC=\"\" JSXX=\"\" ZYJL=\"\" TYSM=\"\" LCSY=\"\"/></PresInfo></CheckOutput></Check></CheckList>";
        CheckMessage checkMessage = new CheckMessage();
        nurseCheckResults = processCheckList(handleNurseCheckXml(checkJson));
        if(nurseCheckResults.size()!=0){
            checkMessage.setHasProblem(1);
        }
//        putXML2Cache(checkMessage.getPresId(), checkJson);
        return checkMessage;
    }

    public CheckMessage checkNursePresc(String data) {
        String url = nurseCheckServerUrl;
//        String checkJson = "<CheckList><Check><CheckInput><Doctor POSITION=\"副主任\" NAME=\"武欣\" DEPT_CODE=\"030401\" DEPT_NAME=\"(杨)产科(1B)\" USER_ID=\"6230\"/><Patient NAME=\"范芸莹\" ID=\"26384916\" GENDER=\"女\" BIRTH=\"20150403\" WEIGHT=\"\" HEIGHT=\"\" ALERGY_DRUGS=\"\" PREGNANT=\"是\" LACT=\"否\" HEPATICAL=\"\" RENAL=\"\" PANCREAS=\"\" VISIT_ID=\"1-1\" PATIENT_PRES_ID=\"2018112800084\" IDENTITY_TYPE=\"\" FEE_TYPE=\"\" SCR=\"\" SCR_UNIT=\"umol/L\" GESTATION_AGE=\"12\" PRETERM_BIRTH=\"\" DRUG_HISTORY=\"\" FAMILY_DISEASE_HISTORY=\"\" GENETIC_DISEASE=\"\" MEDICARE_01=\"\" MEDICARE_02=\"\" MEDICARE_03=\"\" MEDICARE_04=\"\" MEDICARE_05=\"\" WARD_CODE=\"\" WARD_NAME=\"\" BED_NO=\"\" INPATIENT_NO=\"\"/><Diagnosises DIAGNOSISES=\"阿尔茨海默病\"/><Advices><Advice REPEAT=\"\" DRUG_LO_NAME=\"注射用替考拉宁(进口)\" DRUG_LO_ID=\"010101\" ADMINISTRATION=\"口服\" DOSAGE=\"0.4\" DOSAGE_UNIT=\"克\" FREQ_COUNT=\"1\" FREQ_INTERVAL=\"1\" FREQ_INTERVAL_UNIT=\"日\" START_DAY=\"20181128\" END_DAY=\"\" DEPT_CODE=\"23940\" DOCTOR_NAME=\"武欣\" ORDER_NO=\"1\" ORDER_SUB_NO=\"1\" AUTHORITY_LEVELS=\"\" ALERT_LEVELS=\"\" TITLE=\"副主任\" GROUP_ID=\"1\" USER_ID=\"6230\" PRES_ID=\"2018112800084\" PRES_DATE=\"20181128\" PRES_SEQ_ID=\"\" PK_ORDER_NO=\"\" COURSE=\"24\" PKG_COUNT=\"1\" PKG_UNIT=\"盒\" BAK_01=\"\" BAK_02=\"\" BAK_03=\"片剂\" BAK_04=\"12克\" BAK_05=\"杨子制药\" PERFORM_SCHEDULE=\"10|16|20\"/></Advices></CheckInput><CheckOutput><PresInfo ORDER_ID=\"1\" ORDER_SUB_ID=\"1\" DRUG_LO_ID=\"010101\" DRUG_LO_NAME=\"注射用替考拉宁(进口)\" GANYU_STATE=\"未干预\" GANYU_INFO=\"\"><CheckInfo COLOR=\"红色\" NAME=\"适应症\" WARNING_LEVEL=\"禁用\" WARNING_INFO=\"超适应症用药。药品说明书适应症为：肚子疼、屁股疼、脑子疼、心房颤动。医生诊断为：阿尔茨海默病\" REF_SOURCE=\"临床用药指南\" YPMC=\"\" JSXX=\"\" ZYJL=\"\" TYSM=\"\" LCSY=\"\"/></PresInfo></CheckOutput></Check></CheckList>";
        String checkJson = HttpUtil.sendPost(url, data);
        logger.debug(checkJson);
        CheckMessage checkMessage = new CheckMessage();
        nurseCheckResults = processCheckList(handleNurseCheckXml(checkJson));
        if(nurseCheckResults.size()!=0){
            checkMessage.setHasProblem(1);
        }
//        putXML2Cache(checkMessage.getPresId(), checkJson);
        return checkMessage;
    }

    public List<Check> handleNurseCheckXml(String xml){
        CheckMessage checkMessage = new CheckMessage();
        return new ParseXML().parseNurseCheckXml(xml);
    }

    public List<NurseCheckResult> processCheckList(List<Check> list){
        List<NurseCheckResult> res = new ArrayList<NurseCheckResult>();
        for(Check check:list){
            NurseCheckResult nurseCheckResult = new NurseCheckResult();
            CheckPresInput checkPresInput = check.getCheckPresInput();
            CheckPresOutput checkPresOutput = check.getCheckPresOutput();
            nurseCheckResult.setPatient(checkPresInput.getPatient());
            nurseCheckResult.setAdvices(checkPresInput.getAdvices());
            for(Advice advice : nurseCheckResult.getAdvices()){
                for(PrescInfo prescInfo : checkPresOutput.getPrescInfos()){
                    if(prescInfo.getOrder_id().equals(advice.getORDER_NO()) && prescInfo.getOrder_sub_id().equals(advice.getORDER_SUB_NO())
                            && prescInfo.getDrug_lo_id().equals(advice.getDRUG_LO_ID())){
                        advice.setCheckInfoList(prescInfo.getCheckInfos());
                    }
                }
            }
            int warning_level = getHighestLevelFromAdvicesList(nurseCheckResult.getAdvices());
            nurseCheckResult.setWarning_level(warning_level);
            res.add(nurseCheckResult);
        }
        return res;
    }

    public List<NurseCheckResult> sortByBedNo(List<NurseCheckResult> list){
        List<NurseCheckResult> res = new ArrayList<NurseCheckResult>();
        for(int i=0;i<list.size();++i) {
            int bed_no_min = Integer.MAX_VALUE;
            //第一次遍历，找出最小的床号
            for (NurseCheckResult nurseCheckResult : list) {
                if (nurseCheckResult.isBed_no_flag()) {
                    continue;
                }
                Patient patient = nurseCheckResult.getPatient();
                if (patient.getBED_NO() != null && Integer.parseInt(patient.getBED_NO()) < bed_no_min) {
                    bed_no_min = Integer.parseInt(patient.getBED_NO());
                }
            }
            //第二次遍历，将床号最小的记录加入到list中
            for (NurseCheckResult nurseCheckResult : list) {
                if (nurseCheckResult.isBed_no_flag()) {
                    continue;
                }
                Patient patient = nurseCheckResult.getPatient();
                if (patient.getBED_NO() != null && Integer.parseInt(patient.getBED_NO()) == bed_no_min) {
                    nurseCheckResult.setBed_no_flag(true);
                    res.add(nurseCheckResult);
                }
            }
        }
        //将床号为空的记录加入到list中
        for(NurseCheckResult nurseCheckResult:list){
            Patient patient = nurseCheckResult.getPatient();
            if(patient.getBED_NO() == null){
                res.add(nurseCheckResult);
            }
        }
        return res;
    }

    public List<NurseCheckResult> sortBySerious(List<NurseCheckResult> list){
        List<NurseCheckResult> res = new ArrayList<NurseCheckResult>();
        for(int i=0;i<list.size();++i) {
            int serious_max = Integer.MIN_VALUE;
            //第一次遍历，找出问题级别最高的记录
            for (NurseCheckResult nurseCheckResult : list) {
                if (nurseCheckResult.isSerious_flag()) {
                    continue;
                }
                int warning_level = nurseCheckResult.getWarning_level();
                if (warning_level > serious_max) {
                    serious_max = warning_level;
                }
            }
            //第二次遍历，将问题级别最高的记录加入到list中
            for (NurseCheckResult nurseCheckResult : list) {
                if (nurseCheckResult.isSerious_flag()) {
                    continue;
                }
                int warning_level = nurseCheckResult.getWarning_level();
                if (warning_level == serious_max) {
                    nurseCheckResult.setSerious_flag(true);
                    res.add(nurseCheckResult);
                }
            }
        }
        return res;
    }

    public int getHighestLevelFromAdvicesList(List<Advice> advices){
        int highestLevel = 0;
        for(Advice advice:advices){
            int tempLevel = getHighestLevelFromCheckInfoList(advice.getCheckInfoList());
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

    public List<NurseCheckResult> getNurseCheckResults() {
        return nurseCheckResults;
    }

    public void setNurseCheckResults(List<NurseCheckResult> nurseCheckResults) {
        this.nurseCheckResults = nurseCheckResults;
    }
}
