package com.dcdt.utils;

import org.junit.Test;

/**
 * Created by LiRong on 2017/12/18.
 */
public class CommonUtilTest {
    @Test
    public void testPresIdWithTime() {
        String presId = "abc";
        System.out.println(presId + CommonUtil.getPresIdWithTime(presId));

        String xml = "   &lt;CheckInput TAG=\"1\" &gt;\n" +
                "                            &lt;Doctor NAME=\"王雁\" POSITION=\"主治医师\" USER_ID=\"000683\" DEPT_NAME=\"呼吸科\" DEPT_CODE=\"FKNF\" /&gt;\n" +
                "                            &lt;Patient NAME=\"朱永东\" ID=\"0003478286\" VISIT_ID=\"1\" PATIENT_PRES_ID=\"000347828620150924000649\" BIRTH=\"19651105\" HEIGHT=\"165\" WEIGHT=\"60\" GENDER=\"男\" PREGNANT=\"\" LACT=\"\" HEPATICAL=\"\" RENAL=\"\" PANCREAS=\"\" ALERGY_DRUGS=\"\" IDENTITY_TYPE=\"\" FEE_TYPE=\"\" SCR=\"\" SCR_UNIT=\"\" GESTATION_AGE=\"\" PRETERM_BIRTH=\"\" DRUG_HISTORY=\"\" FAMILY_DISEASE_HISTORY=\"\" GENETIC_DISEASE=\"\" MEDICARE_01=\"\" MEDICARE_02=\"\" MEDICARE_03=\"\" MEDICARE_04=\"\" MEDICARE_05=\"\" /&gt;\n" +
                "                            &lt;Diagnosises DIAGNOSISES=\"呼吸道感染\" /&gt;\n" +
                "                            &lt;Advices&gt;\n" +
                "                                &lt;Advice DRUG_LO_ID=\"MED00003\" DRUG_LO_NAME=\"布洛芬缓释胶囊\" ADMINISTRATION=\"991013\" DOSAGE=\"800\" DOSAGE_UNIT=\"mg\" FREQ_COUNT=\"3\" FREQ_INTERVAL=\"\" FREQ_INTERVAL_UNIT=\"\" START_DAY=\"20150924\" END_DAY=\"\" REPEAT=\"1\" ORDER_NO=\"70285312\" ORDER_SUB_NO=\"\" DEPT_CODE=\"HX\" DOCTOR_NAME=\"王雁\" TITLE=\"\" AUTHORITY_LEVELS=\"\" ALERT_LEVELS=\"\" GROUP_ID=\"\" USER_ID=\"000648\" PRES_ID=\"0003478286_2016:11:16:16:55:34\" PRES_DATE=\"20150924\" PRES_SEQ_ID=\"11170097620150924\" PK_ORDER_NO=\"111700976\" COURSE=\"1\" PKG_COUNT=\"0\" PKG_UNIT=\"盒\" BAK_01=\"2\" BAK_02=\"否\" BAK_03=\"\" BAK_04=\"\" BAK_05=\"\" /&gt;\n" +
                "                                &lt;Advice DRUG_LO_ID=\"MED00009\" DRUG_LO_NAME=\"头孢拉定胶囊\" ADMINISTRATION=\"991013\" DOSAGE=\"500\" DOSAGE_UNIT=\"ml\" FREQ_COUNT=\"3\" FREQ_INTERVAL=\"\" FREQ_INTERVAL_UNIT=\"\" START_DAY=\"20150924\" END_DAY=\"\" REPEAT=\"1\" ORDER_NO=\"70285314\" ORDER_SUB_NO=\"\" DEPT_CODE=\"HX\" DOCTOR_NAME=\"王雁\" TITLE=\"\" AUTHORITY_LEVELS=\"\" ALERT_LEVELS=\"\" GROUP_ID=\"\" USER_ID=\"000648\" PRES_ID=\"0003478286_2016:11:16:16:55:34\" PRES_DATE=\"20150924\" PRES_SEQ_ID=\"11170097620150924\" PK_ORDER_NO=\"111700976\" COURSE=\"1\" PKG_COUNT=\"101\" PKG_UNIT=\"盒\" BAK_01=\"2\" BAK_02=\"否\" BAK_03=\"\" BAK_04=\"\" BAK_05=\"\" /&gt;\n" +
                "                                &lt;Advice DRUG_LO_ID=\"MED00223\" DRUG_LO_NAME=\"氯沙坦钾氢氯噻嗪片\" ADMINISTRATION=\"991013\" DOSAGE=\"500\" DOSAGE_UNIT=\"ml\" FREQ_COUNT=\"3\" FREQ_INTERVAL=\"\" FREQ_INTERVAL_UNIT=\"\" START_DAY=\"20150924\" END_DAY=\"\" REPEAT=\"1\" ORDER_NO=\"70285314\" ORDER_SUB_NO=\"\" DEPT_CODE=\"HX\" DOCTOR_NAME=\"王雁\" TITLE=\"\" AUTHORITY_LEVELS=\"\" ALERT_LEVELS=\"\" GROUP_ID=\"\" USER_ID=\"000683\" PRES_ID=\"0003478286_2016:11:16:16:55:34\" PRES_DATE=\"20150924\" PRES_SEQ_ID=\"11170097620150924\" PK_ORDER_NO=\"111700976\" COURSE=\"1\" PKG_COUNT=\"9\" PKG_UNIT=\"盒\" BAK_01=\"2\" BAK_02=\"否\" BAK_03=\"\" BAK_04=\"\" BAK_05=\"\" /&gt;\n" +
                "                                &lt;Advice DRUG_LO_ID=\"MED00238\" DRUG_LO_NAME=\"马来酸依那普利片\" ADMINISTRATION=\"991013\" DOSAGE=\"500\" DOSAGE_UNIT=\"ml\" FREQ_COUNT=\"3\" FREQ_INTERVAL=\"\" FREQ_INTERVAL_UNIT=\"\" START_DAY=\"20150924\" END_DAY=\"\" REPEAT=\"1\" ORDER_NO=\"70285314\" ORDER_SUB_NO=\"\" DEPT_CODE=\"HX\" DOCTOR_NAME=\"王雁\" TITLE=\"\" AUTHORITY_LEVELS=\"\" ALERT_LEVELS=\"\" GROUP_ID=\"\" USER_ID=\"000683\" PRES_ID=\"0003478286_2016:11:16:16:55:34\" PRES_DATE=\"20150924\" PRES_SEQ_ID=\"11170097620150924\" PK_ORDER_NO=\"111700976\" COURSE=\"1\" PKG_COUNT=\"9\" PKG_UNIT=\"盒\" BAK_01=\"2\" BAK_02=\"否\" BAK_03=\"\" BAK_04=\"\" BAK_05=\"\" /&gt;\n" +
                "                                &lt;Advice DRUG_LO_ID=\"MED00168\" DRUG_LO_NAME=\"注射用美罗培南\" ADMINISTRATION=\"030101\" DOSAGE=\"20\" DOSAGE_UNIT=\"mg\" FREQ_COUNT=\"3\" FREQ_INTERVAL=\"\" FREQ_INTERVAL_UNIT=\"\" START_DAY=\"20150114\" END_DAY=\"20150114\" REPEAT=\"0\" ORDER_NO=\"32325701\" ORDER_SUB_NO=\"\" DEPT_CODE=\"XW  \" DOCTOR_NAME=\"李晓丽\" TITLE=\"\" AUTHORITY_LEVELS=\"\" ALERT_LEVELS=\"\" GROUP_ID=\"\" USER_ID=\"002329\" PRES_ID=\"31388291\" PRES_DATE=\"20150114\" PRES_SEQ_ID=\"3138829120150114\" PK_ORDER_NO=\"\" COURSE=\"1\" PKG_COUNT=\"\" PKG_UNIT=\"\" BAK_01=\"\" BAK_02=\"\" BAK_03=\"\" BAK_04=\"\" BAK_05=\"\"/&gt;\n" +
                "                            &lt;/Advices&gt;\n" +
                "                        &lt;/CheckInput&gt;";

        String s = CommonUtil.changeXMLWithPattern(xml, "abcde");
        System.out.println(s);
    }


}
