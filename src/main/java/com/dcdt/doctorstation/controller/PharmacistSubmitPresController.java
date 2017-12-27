package com.dcdt.doctorstation.controller;

import com.dcdt.doctorstation.entity.CheckMessage;
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

    @ResponseBody
    @RequestMapping("/sendPharmacistCheck")
    public CheckMessage sendPharmacistCheck(int tag,String patientId,String presDate,String xml) {
        return service.checkPharmacistPresc(tag,patientId,presDate,xml);
    }

    @RequestMapping("/pharmacistCheckResultPage")
    public String findPharmacistCheckResult(String presId,Model model){
        CheckResults checkResult = service.findCheckResult(presId);
        model.addAttribute("checkResult", checkResult);
        model.addAttribute("checkResultJson", service.toJson(checkResult));
        model.addAttribute("presId", presId);
        return "pharmacistCheckResultPage";
    }

    @RequestMapping("findCheckResultJson")
    @ResponseBody
    public CheckResults findCheckResultJson(String presId) {
        return service.findCheckResult(presId);
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
}
