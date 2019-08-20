package com.dcdt.doctorstation.controller;

import com.dcdt.cache.CheckResultCache;
import com.dcdt.cache.Config;
import com.dcdt.doctorstation.entity.CheckMessage;
import com.dcdt.doctorstation.entity.CheckMessage_BZ;
import com.dcdt.doctorstation.entity.CheckPresInput;
import com.dcdt.doctorstation.entity.CheckResults;
import com.dcdt.doctorstation.service.CacheService;
import com.dcdt.doctorstation.service.PrescCheckService;
import com.dcdt.utils.CommonUtil;
import com.dcdt.utils.ParseXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by LiRong on 2017/6/20.
 */
@Controller
@RequestMapping("/submit")
public class SubmitPrescController {

    private PrescCheckService service;
    private Config config;

    @ResponseBody
    @RequestMapping("/sendCheck")
    public CheckMessage sendCheck(int tag, String xml) {
        return service.checkPresc(tag, xml);
    }

    @ResponseBody
    @RequestMapping("/sendCheck_BZ")
    public CheckMessage_BZ sendCheck_bz(int tag, String xml) {
        return service.checkPresc_BZ(tag, xml);
    }

    @ResponseBody
    @RequestMapping("/getDeptCode")
    public String getDeptCode(String xml) {
        CheckPresInput checkPresInput = new ParseXML().parseInputXml(xml);
        return checkPresInput.getDoctor().getDEPT_CODE();
    }

    @ResponseBody
    @RequestMapping("/beginIntervene")
    public String beginIntervene(@RequestBody String presId) {
        return service.checkPresc(presId);
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
    public CheckMessage sendCheckForTest(int tag, String xml) throws IOException {
        return service.checkPrescForTest(tag, xml);
    }

    @RequestMapping("/checkResultPage")
    public String findCheckResult(String presId, Model model) {
        CheckResults checkResult = service.findCheckResult(presId);
        model.addAttribute("checkResult", checkResult);
        model.addAttribute("checkResultJson", service.toJson(checkResult));
        model.addAttribute("presId", presId);
        model.addAttribute("config", config);
        return "checkResultPage";
    }

    @RequestMapping("findCheckResultJson")
    @ResponseBody
    public CheckResults findCheckResultJson(String presId) {
        return service.findCheckResult(presId);
    }

    @RequestMapping("/removeCheckResult")
    public CheckResults removeCheckResult(String presId) {
        return service.removeCheckResult(presId);
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

    @ResponseBody
    @RequestMapping("/setRetValue_bz")
    public void setRetValue_bz(String presId, String message) {
        service.putRetValue_bz(presId, message);
    }

    @ResponseBody
    @RequestMapping("/getRetValue_bz")
    public String getRetValue_bz(String presId) {
        return service.findRetValue_bz(presId);
    }

    @Autowired
    public void setService(PrescCheckService service) {
        this.service = service;
    }

    @Autowired
    public void setConfig(Config config) {
        this.config = config;
    }

}
