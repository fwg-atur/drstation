package com.dcdt.doctorstation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Random;

/**
 * Created by LiRong on 2017/12/8.
 */
@Controller
@RequestMapping("/test")
public class TestController {
    private int message_no = 1;

    @ResponseBody
    @RequestMapping("/testCheck1")
    public String sendCheck(@RequestBody String xml) {
        System.out.println(xml);
        return "<Response FUN='2'>\n" +
                "<Output PRES_STATE='0' MAX_MESSAGE_NO='1' />\n</Response>";
    }

    @ResponseBody
    @RequestMapping(value = "/testCheck2", produces = "application/json; charset=utf-8")
    public String sendCheckMessage(@RequestBody String xml) {
        String[] strings = new String[]{
                "<Response FUN='2'><Output MESSAGE_NO='@MESSAGE_NO@' MESSAGE_TEXT='药师发\n药师发\n药师发\n药师发\n药师发\n药师发\n" +
                        "药师发\n药师发\n药师发\n药师发\n药师发\n" +
                        "药师发\n药师发\n药师发\n药师发\n药师发\n" +
                        "药师发\n药师发\n药师发\n药师发\n药师发\n" +
                        "的消息' /><Output MESSAGE_NO='2' MESSAGE_TEXT='药师发的消息2' /></Response>",
                "<Response FUN='2'>\n" +
                        "<Output MESSAGE_NO='@MESSAGE_NO@' MESSAGE_TEXT='药师发\n" +
                        "的消息' />\n" +
                        "<Output MESSAGE_NO='2' MESSAGE_TEXT='药师发的\n" +
                        "消息2' /></Response>",
                "<Response FUN='2'><Output MESSAGE_NO='@MESSAGE_NO@' MESSAGE_TEXT='同意双签字发药' /></Response>",
                "<Response FUN='2'><Output MESSAGE_NO='@MESSAGE_NO@' MESSAGE_TEXT='同意医师观点' /></Response>",
                "<Response FUN='2'><Output MESSAGE_NO='@MESSAGE_NO@' MESSAGE_TEXT='同意医\n" +
                        "师观点' /></Response>"
        };
        System.out.println(xml);
        String response = strings[new Random().nextInt(strings.length)];
        response = response.replace("@MESSAGE_NO@", message_no + "");
        message_no++;
        System.out.println(response);
        return response;
    }


    @ResponseBody
    @RequestMapping("/testSQL")
    public String putSQL(@RequestBody String xml) {
        System.out.println(xml);
        return "OK";
    }


}
