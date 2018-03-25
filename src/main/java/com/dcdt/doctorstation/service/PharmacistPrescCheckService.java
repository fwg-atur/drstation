package com.dcdt.doctorstation.service;

import com.dcdt.cache.PharmacistCheckResultCache;
import com.dcdt.cache.RetValCache;
import com.dcdt.doctorstation.entity.*;
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

    @Value("${pharmacistCheckServerUrl}")
    private String pharmacistCheckServerUrl;

    private CacheService cacheService;


    private static final Logger logger = LoggerFactory.getLogger(PharmacistPrescCheckService.class);

    /**

     * @param xml
     * @return 返回审核结果标识（flag,presId）,不包括审核结果字符串
     */
    public CheckMessage checkPharmacistPresc(String patientID,String visitDate, String xml) {
        String url = pharmacistCheckServerUrl + "?patientID=" + patientID + "&visitDate=" + visitDate;
        String checkXml = HttpUtil.sendPost(url, "");
        logger.info(checkXml);
        CheckMessage message = handleCheckXml(checkXml,xml);
        putXML2Cache(message.getPresId(), checkXml);
        return message;
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
        ParseXML parseXML = new ParseXML();

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
        check.getCheckPresOutput().setPrescInfos(list);

        String presId = check.getCheckPresInput().getPatient().getPATIENT_PRES_ID();

        //缓存审核结果,等到进入审核结果页面时再读取记录显示
        PharmacistCheckResultCache.putPharmacistCheckResult(presId, check);

        //处理审核信息
        CheckMessage message = new CheckMessage();
        message.setPresId(presId);
        if(checkProblem(check.getCheckPresOutput())){
            message.setHasProblem(1);
        }

        return message;
    }

    protected List<PrescInfo> compareCheck(CheckPresInput checkPresInput, CheckPresOutput checkPresOutput){
        List<Advice> adviceList = checkPresInput.getAdvices();
        List<PrescInfo> prescInfoList = checkPresOutput.getPrescInfos();
        List<PrescInfo> prescInfos = new ArrayList<PrescInfo>();

        //遍历传入xml的医嘱列表
        for(Advice advice : adviceList){
            //遍历审核结果xml的审核结果医嘱列表
            for(PrescInfo prescInfo : prescInfoList){
                if(advice.getDRUG_LO_ID().equals(prescInfo.getDrug_lo_id())){
                    prescInfos.add(prescInfo);
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


    public String toJson(Check check) {
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
        return RetValCache.removeRetVal(presId);
    }

    @Autowired
    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }
}
