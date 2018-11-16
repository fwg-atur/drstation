package com.dcdt.doctorstation.service;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by wtwang on 2018/11/16.
 */
public class judgeContainCharacterTest {
    @Test
    public void judgeContainCharater() throws Exception {
        String s = "o111123";
        PrescCheckService p = new PrescCheckService();
        System.out.println(p.judgeContainCharater(s));
    }

}