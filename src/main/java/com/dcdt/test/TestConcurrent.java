package com.dcdt.test;

import com.dcdt.utils.HttpUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wtwang on 2018/11/9.
 */
public class TestConcurrent implements Runnable{
    private static int totalTime = 0;
    private static int i = 0;
    public static void main(String[] args) throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(10);//10是线程数
        for (int i = 0; i < 50; i++) {
            TestConcurrent test = new TestConcurrent();
            service.execute(test);
            //Thread.sleep(1000);
        }
    }


    public void run(){
        long time1=System.currentTimeMillis();
        String url = "http://10.201.17.201:8081/service/api/checkFromXmlToJson.do?tag=1";
        String url2 = "http://10.201.17.201:8081/service/api/query/QueryPresPharm.do?patientID=90280019&visitDate=20181110";
//        String testURL = "http://localhost:80/DCStation/submit/testConcurrent";
        String xml = "<CheckInput TAG=\"2\">\n" +
                "    <Doctor NAME=\"王雁\" POSITION=\"主治医师\" USER_ID=\"00064834\" DEPT_NAME=\"心内\" DEPT_CODE=\"2512\" />\n" +
                "    <Patient NAME=\"郑茜\" ID=\"90280019\" VISIT_ID=\"1\" PATIENT_PRES_ID=\"000347828234\" BIRTH=\"19651105\" HEIGHT=\"165\" WEIGHT=\"60\" GENDER=\"男\" PREGNANT=\"\" LACT=\"\" HEPATICAL=\"\" RENAL=\"\" PANCREAS=\"\" ALERGY_DRUGS=\"\" \n" +
                "\tIDENTITY_TYPE=\"军人\" FEE_TYPE=\"\" \n" +
                "\tSCR=\"\" SCR_UNIT=\"\" GESTATION_AGE=\"\" PRETERM_BIRTH=\"\" DRUG_HISTORY=\"\" FAMILY_DISEASE_HISTORY=\"\" GENETIC_DISEASE=\"\"\n" +
                "\tMEDICARE_01=\"\" MEDICARE_02=\"\" MEDICARE_03=\"\" MEDICARE_04=\"\" MEDICARE_05=\"\"/>\n" +
                "    <Diagnosises DIAGNOSISES=\"肺炎、行动不便\" />\n" +
                "    <Advices>\n" +
                "\t\t<Advice \n" +
                "\t\tDRUG_LO_ID=\"1099028IJ2\" DRUG_LO_NAME=\"注射用卡铂\" ADMINISTRATION=\"口服\" DOSAGE=\"299\" DOSAGE_UNIT=\"g\" FREQ_COUNT=\"2\" FREQ_INTERVAL=\"1\" FREQ_INTERVAL_UNIT=\"日\" START_DAY=\"20150924\" END_DAY=\"\" REPEAT=\"1\" ORDER_NO=\"70285312\" ORDER_SUB_NO=\"\" \n" +
                "\t\tDEPT_CODE=\"2046\" DOCTOR_NAME=\"王雁\" TITLE=\"医师\" AUTHORITY_LEVELS=\"\" ALERT_LEVELS=\"\" GROUP_ID=\"\" \n" +
                "\t\tUSER_ID=\"00231\" PRES_ID=\"0006482\" PRES_DATE=\"20150924\" PRES_SEQ_ID=\"11170097620150924\" PK_ORDER_NO=\"111700976\" COURSE=\"3\" \n" +
                "\t\tPKG_COUNT=\"10\" PKG_UNIT=\"盒\" \n" +
                "\t\tBAK_01=\"\" BAK_02=\"\" BAK_03=\"注射剂\" BAK_04=\"0.1g\" BAK_05=\"杨子制药\" />\n" +
                "\t\t<Advice \n" +
                "\t\tDRUG_LO_ID=\"0199010IJ1\" DRUG_LO_NAME=\"甲硝唑氯化钠注射液\" ADMINISTRATION=\"口服\" DOSAGE=\"299\" DOSAGE_UNIT=\"m g\" FREQ_COUNT=\"2\" FREQ_INTERVAL=\"1\" FREQ_INTERVAL_UNIT=\"日\" START_DAY=\"20150924\" END_DAY=\"\" REPEAT=\"1\" ORDER_NO=\"70285312\" ORDER_SUB_NO=\"\" \n" +
                "\t\tDEPT_CODE=\"2046\" DOCTOR_NAME=\"王雁\" TITLE=\"医师\" AUTHORITY_LEVELS=\"\" ALERT_LEVELS=\"\" GROUP_ID=\"\" \n" +
                "\t\tUSER_ID=\"\" PRES_ID=\"0006482\" PRES_DATE=\"20150924\" PRES_SEQ_ID=\"11170097620150924\" PK_ORDER_NO=\"111700976\" COURSE=\"9\" \n" +
                "\t\tPKG_COUNT=\"10\" PKG_UNIT=\"盒\" \n" +
                "\t\tBAK_01=\"\" BAK_02=\"\" BAK_03=\"注射液\" BAK_04=\"25mg\" BAK_05=\"京曙光药业\" />\n" +
                "\n" +
                "\t</Advices>\n" +
                "</CheckInput>\n";
        String data = xml;
        String checkJson = HttpUtil.sendPost(url, data);
        System.out.println(checkJson);
        long time2=System.currentTimeMillis();
        long interval=time2-time1;
        System.out.println(interval);
        totalTime += interval;
        System.out.println("totalTime:"+totalTime);
    }
}
