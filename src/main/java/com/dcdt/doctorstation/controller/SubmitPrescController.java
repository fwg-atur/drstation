package com.dcdt.doctorstation.controller;

import com.dcdt.doctorstation.entity.CheckMessage;
import com.dcdt.doctorstation.entity.CheckResults;
import com.dcdt.doctorstation.service.PrescCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by LiRong on 2017/6/20.
 */
@Controller
@RequestMapping("/submit")
public class SubmitPrescController {

    private PrescCheckService service;

    @ResponseBody
    @RequestMapping("/sendCheck")
    public CheckMessage sendCheck(int tag, String xml) {
        return service.checkPresc(tag, xml);
    }

    /**
     * 测试用
     *
     * @param tag
     * @param xml
     * @return
     */
    @ResponseBody
    @RequestMapping("/sendCheckForTest")
    public CheckMessage sendCheckForTest(int tag, String xml) {
        return service.checkPrescForTest(tag, xml);
    }

    @RequestMapping("/checkResultPage")
    public String findCheckResult(String presId, Model model) {
        CheckResults checkResult = service.findCheckResult(presId);
        model.addAttribute("checkResult", checkResult);
        model.addAttribute("checkResultJson", service.toJson(checkResult));
        model.addAttribute("presId", presId);
        return "checkResultPage";
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
    public void setService(PrescCheckService service) {
        this.service = service;
    }
}
