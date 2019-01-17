package com.dcdt.doctorstation.controller;

import com.dcdt.cache.Config;
import com.dcdt.doctorstation.entity.Check;
import com.dcdt.doctorstation.entity.CheckMessage;
import com.dcdt.doctorstation.entity.CheckPresOutput;
import com.dcdt.doctorstation.entity.CheckResults;
import com.dcdt.doctorstation.service.PharmacistPrescCheckService;
import com.dcdt.doctorstation.service.PrescCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by wtwang on 2017/11/30.
 */
@Controller
@RequestMapping("/pharmacistSubmit")
public class PharmacistSubmitPresController{

    private PharmacistPrescCheckService service;
    private Config config;

    @ResponseBody
    @RequestMapping("/sendPharmacistCheck")
    public CheckMessage sendPharmacistCheck(String patientID,String visitDate,String pharmacistInfo,String xml) {
        return service.checkPharmacistPresc(patientID,visitDate,pharmacistInfo,xml);
    }

    /**
     * 测试用
     * @param patientID
     * @param visitDate
     * @param pharmacistInfo
     * @param xml
     * @return
     */
    @ResponseBody
    @RequestMapping("/sendPharmacistCheckForTest")
    public CheckMessage sendPharmacistCheckForTest(String patientID,String visitDate,String pharmacistInfo,String xml) {
        return service.checkPharmacistPrescForTest(patientID,visitDate,pharmacistInfo,xml);
    }

    @ResponseBody
    @RequestMapping("/sendPharmacistCheck_CP")
    public CheckMessage sendPharmacistCheck_CP(String visitDate,String pharmacistInfo,String xml) {
        return service.checkPharmacistPresc_CP(visitDate,pharmacistInfo,xml);
    }

    @ResponseBody
    @RequestMapping("/sendPharmacistCheckSilent_CP")
    public CheckMessage sendPharmacistCheckSilent_CP(String xml) {
        return service.checkPharmacistPrescSilent_CP(xml);
    }

    @RequestMapping("/sendPharmacistInterfere")
    public String sendPharmacistInterfere(String xml) {
        return service.checkPharmacistInterfere(xml);
    }

    @RequestMapping("/pharmacistCheckResultPage")
    public String findPharmacistCheckResult(String presId,Model model){
        Check check = service.findPharmacistCheckResult(presId);
        model.addAttribute("pharmacistCheckResult", check);
        model.addAttribute("pharmacistCheckResultJson", service.toJson(check));
        model.addAttribute("presId", presId);
        model.addAttribute("checkPharmacist",check.getCheckPharmacist());
        model.addAttribute("config", config);
        model.addAttribute("pharmacistInfo",service.toJson(service.getPharmacistInfo()));
        model.addAttribute("date",service.getDate());
        return "pharmacistCheckResultPage";
    }

    @RequestMapping("findCheckResultJson")
    @ResponseBody
    public Check findCheckResultJson(String presId) {
        return service.findPharmacistCheckResult(presId);
    }

    @ResponseBody
    @RequestMapping("/setRetValue")
    public void setRetValue(String presId, int retVal) {
        service.putRetValue(presId, retVal);
    }

    @ResponseBody
    @RequestMapping("/getRetValue")
    public int getRetValue(String presId) {
        return service.findRetValue(presId);
    }

    @Autowired
    public void setService(PharmacistPrescCheckService service) {
        this.service = service;
    }

    @Autowired
    public void setConfig(Config config) {
        this.config = config;
    }
}
