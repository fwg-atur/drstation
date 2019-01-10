package com.dcdt.doctorstation.controller;

import com.dcdt.cache.Config;
import com.dcdt.doctorstation.entity.Check;
import com.dcdt.doctorstation.entity.CheckMessage;
import com.dcdt.doctorstation.entity.NurseCheckResult;
import com.dcdt.doctorstation.service.NursePrescCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

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
    public String findCheckResult(Model model) {
        List<NurseCheckResult> checkList = service.getNurseCheckResults();
        model.addAttribute("nurseCheckList", checkList);
        model.addAttribute("nurseCheckListJson", service.toJson(checkList));
        model.addAttribute("config", config);
        return "nurseCheckResultPage";
    }

//    @RequestMapping("findCheckResultJson")
//    @ResponseBody
//    public Check findCheckResultJson(String presId) {
//        return service.findNurseCheckResult(presId);
//    }

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
