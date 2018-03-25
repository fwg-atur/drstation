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
    public CheckMessage sendPharmacistCheck(String patientID,String visitDate,String xml) {
        return service.checkPharmacistPresc(patientID,visitDate,xml);
    }

    @RequestMapping("/pharmacistCheckResultPage")
    public String findPharmacistCheckResult(String presId,Model model){
        Check check = service.findPharmacistCheckResult(presId);
        model.addAttribute("pharmacistCheckResult", check);
        model.addAttribute("pharmacistCheckResultJson", service.toJson(check));
//        model.addAttribute("pharmacistCheckResultJson", service.toJson(check));
//        model.addAttribute("checkPresInput",check.getCheckPresInput());
//        model.addAttribute("checkPresOutput",check.getCheckPresOutput());
//        model.addAttribute("presInfos",check.getCheckPresOutput().getPrescInfos());
        model.addAttribute("presId", presId);
        model.addAttribute("checkPharmacist",check.getCheckPharmacist());
        model.addAttribute("config", config);
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
