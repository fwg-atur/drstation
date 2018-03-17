package com.dcdt.doctorstation.controller;

/**
 * Created by LiRong on 2017/6/19.
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping("/index")
    public  String index(){
        logger.info("the first jsp pages");
        return "index";
    }


    @RequestMapping("/pharmacistIndex")
    public  String pharmacistIndex(){
        //输出日志文件
        logger.info("the first jsp pages of pharmacistStation");
        //返回pharmacistIndex.jsp这个视图
        return "pharmacistIndex";
    }

}