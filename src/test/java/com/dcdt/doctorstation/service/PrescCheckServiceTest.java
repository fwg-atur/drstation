package com.dcdt.doctorstation.service;

import com.dcdt.cache.CheckResultCache;
import com.dcdt.doctorstation.entity.CheckMessage;
import com.dcdt.doctorstation.entity.CheckResults;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by LiRong on 2017/6/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext.xml")
public class PrescCheckServiceTest {
    @Autowired
    PrescCheckService prescCheckService;

    @Test
    public void testHandleJson() {
        String json = "<CheckInput TAG=\"1\">\n" +
                "    <Doctor NAME=\"王雁\" POSITION=\"主治医师\" USER_ID=\"000683\" DEPT_NAME=\"呼吸科\" DEPT_CODE=\"FKNF\" />\n" +
                "    <Patient NAME=\"朱永东\" ID=\"0003478286\" VISIT_ID=\"1\" PATIENT_PRES_ID=\"000347828620150924000648\" BIRTH=\"19651105\" HEIGHT=\"165\" WEIGHT=\"60\" GENDER=\"男\" PREGNANT=\"\" LACT=\"\" HEPATICAL=\"\" RENAL=\"\" PANCREAS=\"\" ALERGY_DRUGS=\"\" IDENTITY_TYPE=\"\" FEE_TYPE=\"\" \n" +
                "SCR=\"\" SCR_UNIT=\"\" GESTATION_AGE=\"\" PRETERM_BIRTH=\"\" DRUG_HISTORY=\"\" FAMILY_DISEASE_HISTORY=\"\" GENETIC_DISEASE=\"\"\n" +
                "MEDICARE_01=\"\" MEDICARE_02=\"\" MEDICARE_03=\"\" MEDICARE_04=\"\" MEDICARE_05=\"\"/>\n" +
                "    <Diagnosises DIAGNOSISES=\"呼吸道感染\" />\n" +
                "    <Advices>\n" +
                "       \n" +
                "\t\t<Advice DRUG_LO_ID=\"MED00003\" DRUG_LO_NAME=\"布洛芬缓释胶囊\" ADMINISTRATION=\"991013\" DOSAGE=\"800\" DOSAGE_UNIT=\"mg\" FREQ_COUNT=\"3\" FREQ_INTERVAL=\"\" FREQ_INTERVAL_UNIT=\"\" START_DAY=\"20150924\" END_DAY=\"\" REPEAT=\"1\" ORDER_NO=\"70285312\" ORDER_SUB_NO=\"\" DEPT_CODE=\"HX\" DOCTOR_NAME=\"王雁\" TITLE=\"\" AUTHORITY_LEVELS=\"\" ALERT_LEVELS=\"\" GROUP_ID=\"\" USER_ID=\"000648\" PRES_ID=\"001\" PRES_DATE=\"20150924\" PRES_SEQ_ID=\"11170097620150924\" PK_ORDER_NO=\"111700976\" COURSE=\"1\" PKG_COUNT=\"0\" PKG_UNIT=\"盒\" BAK_01=\"2\" BAK_02=\"否\" BAK_03=\"\" BAK_04=\"\" BAK_05=\"\" />\n" +
                "\t\t<Advice DRUG_LO_ID=\"MED00003\" DRUG_LO_NAME=\"布洛芬缓释胶囊\" ADMINISTRATION=\"991013\" DOSAGE=\"800\" DOSAGE_UNIT=\"mg\" FREQ_COUNT=\"3\" FREQ_INTERVAL=\"\" FREQ_INTERVAL_UNIT=\"\" START_DAY=\"20150924\" END_DAY=\"\" REPEAT=\"1\" ORDER_NO=\"70285312\" ORDER_SUB_NO=\"\" DEPT_CODE=\"HX\" DOCTOR_NAME=\"王雁\" TITLE=\"\" AUTHORITY_LEVELS=\"\" ALERT_LEVELS=\"\" GROUP_ID=\"\" USER_ID=\"000648\" PRES_ID=\"001\" PRES_DATE=\"20150924\" PRES_SEQ_ID=\"11170097620150924\" PK_ORDER_NO=\"111700976\" COURSE=\"1\" PKG_COUNT=\"0\" PKG_UNIT=\"盒\" BAK_01=\"2\" BAK_02=\"否\" BAK_03=\"\" BAK_04=\"\" BAK_05=\"\" />\t\t\n" +
                "\t</Advices>\n" +
                "</CheckInput>";

        CheckMessage c = prescCheckService.checkPresc(1, json);

        Assert.assertEquals(c.getHasProblem(), 1);
        Assert.assertEquals(c.getPresId(), "000347828620150924000648");

        CheckResults checkResults = CheckResultCache.removeCheckResult(c.getPresId());

        Gson g = new Gson();
        System.out.println(g.toJson(checkResults));

        Assert.assertTrue(CheckResultCache.isEmpty());
    }
}
