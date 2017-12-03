package com.dcdt.doctorstation.controller;

import com.dcdt.doctorstation.entity.Advice;
import com.dcdt.doctorstation.entity.CheckInfo;
import com.dcdt.doctorstation.entity.CheckResults;
import com.dcdt.doctorstation.service.PrescCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sima on 2017/11/27.
 */
@Controller
@RequestMapping("/appeal")
public class AppealController {

    private PrescCheckService service;

    @RequestMapping("/appeal")
    public String appealForCheck(String presId, String drugName, String errorName, Model model){
        CheckResults checkResult = service.findCheckResult(presId);
        CheckResults retCheckResult = checkResult.clone();
        // 获取Advice列表
        List<Advice> adviceList = new ArrayList<Advice>();
        for (Advice advice : checkResult.getAdvices()){
            if (drugName.equals(advice.getDRUG_LO_NAME())){
                Advice ad = advice.clone();
                List<CheckInfo> checkInfoList = new ArrayList<CheckInfo>();
                for (CheckInfo checkInfo : advice.getCheckInfoList()){
                    if (errorName.equals(checkInfo.getNAME())){
                        CheckInfo ci = checkInfo.clone();
                        checkInfoList.add(ci);
                        break;
                    }
                }
                ad.setCheckInfoList(checkInfoList);
                adviceList.add(ad);
                break;
            }
        }
        retCheckResult.setAdvices(adviceList);
        model.addAttribute("checkResult", retCheckResult);
        return "appeal";
    }

    @Autowired
    public void setService(PrescCheckService service) {
        this.service = service;
    }
}
