package com.dcdt.doctorstation.controller;

import com.dcdt.cache.Config;
import com.dcdt.doctorstation.entity.Check;
import com.dcdt.doctorstation.entity.CheckMessage;
import com.dcdt.doctorstation.service.NursePrescCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by wtwang on 2018/12/21.
 */
@Controller
@RequestMapping("/nurseSubmit")
public class NurseSubmitPrescController {
    private NursePrescCheckService service;
    private Config config;

    @ResponseBody
    @RequestMapping("/sendNurseCheck")
    public CheckMessage sendNurseCheck(String xml) {
        return service.checkNursePresc(xml);
    }

    @RequestMapping("/nurseCheckResultPage")
    public String findCheckResult(String presId, Model model) {
        Check check = service.findNurseCheckResult(presId);
        model.addAttribute("nurseCheckResult", check);
        model.addAttribute("nurseCheckResultJson", service.toJson(check));
        model.addAttribute("presId", presId);
        model.addAttribute("config", config);
        return "nurseCheckResultPage";
    }

    @RequestMapping("findCheckResultJson")
    @ResponseBody
    public Check findCheckResultJson(String presId) {
        return service.findNurseCheckResult(presId);
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
    public void setService(NursePrescCheckService service) {
        this.service = service;
    }

    @Autowired
    public void setConfig(Config config) {
        this.config = config;
    }
}
