package com.dcdt.doctorstation.service;

import com.dcdt.cache.CheckResultCache;
import com.dcdt.cache.RetValCache;
import com.dcdt.doctorstation.entity.*;
import com.dcdt.utils.CommonUtil;
import com.dcdt.utils.HttpUtil;
import com.dcdt.utils.ParseXML;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by LiRong on 2017/6/20.
 */
@Service
public class PrescCheckService {
    @Value("${checkServerUrl}")
    private String checkServerUrl;

    @Value("${groupFlag}")
    private String groupFlag;

    private CacheService cacheService;
    private ParseXML parseXML = new ParseXML();


    private static final Logger logger = Logger.getLogger(PrescCheckService.class);

    /**
     * @param tag
     * @param data
     * @return 返回审核结果标识（flag,presId）,不包括审核结果字符串
     */
    public CheckMessage checkPresc(int tag, String data) {
        CheckMessage checkMessage = new CheckMessage();
        String url = checkServerUrl + "?tag=" + tag;
        if(!parseXML.filter(data)){
            return checkMessage;
        }
        data = data.replace("&nbsp;"," ");
        String checkJson = "";
        checkJson = HttpUtil.sendPost(url, data);
//        checkJson = getTestJson();
        logger.debug(checkJson);

        if(checkJson == null || checkJson.equals("")){
            checkMessage.setHasProblem(-2);
            return checkMessage;
        }

        if (tag == 2) return checkMessage;

        checkMessage = handleCheckJson(checkJson);
        if(checkMessage.getHasProblem() == -2){
            return checkMessage;
        }
        putXML2Cache(checkMessage.getPresId(), data);
        return checkMessage;
    }


    public CheckMessage_BZ checkPresc_BZ(int tag, String data) {
        CheckMessage_BZ checkMessage_bz = new CheckMessage_BZ();
        String url = checkServerUrl + "?tag=" + tag;
        if(!parseXML.filter(data)){
            return checkMessage_bz;
        }
        data = data.replace("&nbsp;"," ");
        String checkJson = "";
        checkJson = HttpUtil.sendPost(url, data);
//        checkJson = getTestJson();
        logger.debug(checkJson);



//        if(checkJson == null || checkJson.equals("")){
//            checkMessage.setHasProblem(-2);
//            return checkMessage;
//        }
//
//        if (tag == 2) return checkMessage;

        checkMessage_bz = handleCheckJson_bz(checkJson);
//        if(checkMessage.getHasProblem() == -2){
//            return checkMessage;
//        }
        putXML2Cache(checkMessage_bz.getPresId(), data);
        return checkMessage_bz;
    }

    private void putXML2Cache(String presId, String xml) {
        cacheService.putXML2Cache(presId, xml);
    }


    /**
     * 测试用
     *
     * @param tag
     * @param data
     * @return
     */
    public CheckMessage checkPrescForTest(int tag, String data) throws IOException {

        String checkJson = "<Check>\n" +
                "<CheckInput TAG=\"2\" >\n" +
                "\t<Doctor POSITION=\"234\" NAME=\"孙加超\" DEPT_CODE=\"急诊科[住院]\" DEPT_NAME=\"\" USER_ID=\"0499\"/>\n" +
                "\t<Patient NAME=\"喻唐古\" ID=\"0000646961\" GENDER=\"男\" BIRTH=\"19790316\" WEIGHT=\"\" HEIGHT=\"\" ALERGY_DRUGS=\"\" PREGNANT=\"否\" LACT=\"否\" HEPATICAL=\"\" RENAL=\"\" PANCREAS=\"\" VISIT_ID=\"\" PATIENT_PRES_ID=\"95\" IDENTITY_TYPE=\"\" FEE_TYPE=\"自费\" SCR=\"\" SCR_UNIT=\"\" GESTATION_AGE=\"\" PRETERM_BIRTH=\"\" DRUG_HISTORY=\"\" FAMILY_DISEASE_HISTORY=\"\" GENETIC_DISEASE=\"\" MEDICARE_01=\"\" MEDICARE_02=\"\" MEDICARE_03=\"\" MEDICARE_04=\"\" MEDICARE_05=\"\" WARD_CODE=\"\" WARD_NAME=\"\" BED_NO=\"\" INPATIENT_NO=\"\"/>\n" +
                "\t<Diagnosises DIAGNOSISES=\"不适\"/>\n" +
                "\t<Advices>\n" +
                "\t\t<Advice REPEAT=\"1\" DRUG_LO_NAME=\"阿斯匹林肠溶片[0.1g*30片](拜阿司匹灵)\" DRUG_LO_ID=\"XY000004\" ADMINISTRATION=\"外用\" DOSAGE=\"0.1\" DOSAGE_UNIT=\"g\" FREQ_COUNT=\"Qd\" FREQ_INTERVAL=\"\" FREQ_INTERVAL_UNIT=\"\" START_DAY=\"20190121\" END_DAY=\"\" DEPT_CODE=\"急诊科[住院]\" DOCTOR_NAME=\"孙加超\" ORDER_NO=\"I1901210000859\" ORDER_SUB_NO=\"1\" AUTHORITY_LEVELS=\"\" ALERT_LEVELS=\"\" TITLE=\"234\" GROUP_ID=\"I1901210000859\" USER_ID=\"0499\" PRES_ID=\"I190121000085\" PRES_DATE=\"20190121\" PRES_SEQ_ID=\"I190121000085\" PK_ORDER_NO=\"\" COURSE=\"1\" PKG_COUNT=\"\" PKG_UNIT=\"盒(30)\" BAK_01=\"\" BAK_02=\"否\" BAK_03=\"肠溶衣片\" BAK_04=\"0.1g*30片\" BAK_05=\"\" PERFORM_SCHEDULE=\"\"/>\n" +
                "\t</Advices>\n" +
                "</CheckInput>\n" +
                "\n" +
                "<CheckOutput AntiDrugPos=\"\">\n" +
                "    <PresInfo ORDER_ID=\"I1901210000859\" ORDER_SUB_ID=\"1\" DRUG_LO_ID=\"XY000004\" DRUG_LO_NAME=\"阿斯匹林肠溶片[0.1g*30片](拜阿司匹灵)\" />\n" +
                "</CheckOutput>\n" +
                "\n" +
                "</Check>";
        String path = "C:\\Users\\wtwang\\Desktop\\test.txt";
        File file = new File(path);
        StringBuilder sb = new StringBuilder("");
        try {
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(file));
            BufferedReader br = new BufferedReader(reader);

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String testJson = sb.toString();
        return handleCheckJson(testJson);
    }

    public String getTestJson() {
        String res = "";
//                "{\"doctor\":{\"USER_ID\":\"ZAR\",\"NAME\":\"张霭润\",\"POSITION\":\"住院医师\",\"DEPT_CODE\":\"5\",\"DEPT_NAME\":\"儿科\"},\"patient\":{\"NAME\":\"薛智铧\",\"ID\":\"77770000039259\",\"GENDER\":\"男\",\"BIRTH\":\"20120714\",\"WEIGHT\":\"\",\"HEIGHT\":\"\",\"ALERGY_DRUGS\":\"\",\"PREGNANT\":\"\",\"LACT\":\"\",\"HEPATICAL\":\"\",\"RENAL\":\"\",\"PANCREAS\":\"\",\"VISIT_ID\":\"10009\",\"PATIENT_PRES_ID\":\"105650_10009\",\"IDENTITY_TYPE\":\"\",\"FEE_TYPE\":\"普通\",\"SCR\":\"\",\"SCR_UNIT\":\"\",\"GESTATION_AGE\":\"\",\"PRETERM_BIRTH\":\"\",\"DRUG_HISTORY\":\"\",\"FAMILY_DISEASE_HISTORY\":\"\",\"GENETIC_DISEASE\":\"\",\"MEDICARE_01\":\"\",\"MEDICARE_02\":\"\",\"MEDICARE_03\":\"\",\"MEDICARE_04\":\"\",\"MEDICARE_05\":\"\",\"WARD_CODE\":\"\",\"WARD_NAME\":\"\",\"BED_NO\":\"\",\"INPATIENT_NO\":\"\"},\"diagnosis\":{\"DIAGNOSISES\":\"急性淋巴细胞白血病、急性淋巴细胞白血病复发（B、IR、TEL/AML+、巩固治疗1）、自身免疫性溶血性贫血、口腔溃疡、葡萄糖-6-磷酸酶缺乏、左侧豆状核出血（陈旧性）、松果体囊肿可能、副鼻窦炎、腺样体肥大\"},\"advices\":[{\"checkInfoList\":[{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和碳酸氢钠注射液(药品码621，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n碱性药物可破坏维生素B类，降低后者的药效。\\r\\n【处理】\\r\\n合用需谨慎。\\r\\n\\n\",\"REF_SOURCE\":\"干酵母片说明书,维生素B1片说明书\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和碳酸氢钠注射液(药品码621，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n碱性药物可破坏维生素B类，降低后者的药效。\\r\\n【处理】\\r\\n合用需谨慎。\\r\\n\\n\",\"REF_SOURCE\":\"干酵母片说明书,维生素B1片说明书\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和碳酸氢钠注射液(药品码621，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n碱性药物可破坏维生素B类，降低后者的药效。\\r\\n【处理】\\r\\n合用需谨慎。\\r\\n\\n\",\"REF_SOURCE\":\"干酵母片说明书,维生素B1片说明书\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和碳酸氢钠注射液(药品码621，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n碱性药物可破坏维生素B类，降低后者的药效。\\r\\n【处理】\\r\\n合用需谨慎。\\r\\n\\n\",\"REF_SOURCE\":\"干酵母片说明书,维生素B1片说明书\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和注射用甲泼尼龙琥珀酸钠(药品码595，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  维生素B6与免疫抑制药(青霉胺、环磷酰胺、肾上腺皮质激素、异烟肼等)合用，可能引起贫血或周围神经炎。其中，维生素B6与环磷酰胺合用，同时还可减轻环磷酰胺所引起的肝脏、胃肠道不良反应。\\r\\n【机制】\\r\\n  免疫抑制药对维生素B6有拮抗作用或可增加维生素B6肾排泄率\\n\\n【处理】\\r\\n  长期服用上述免疫抑制药者，维生素B6需要量增加，可一日加服25mg维生素B6。\\r\\n\",\"REF_SOURCE\":\"贾公孚,谢惠民.临床药物新用联用大全.第一版.北京:人民卫生出版社,1999.352-354,436-437,维生素B6片说明书,中华人民共和国药典.2000年版.二部.临床用药须知.国家药典委员会编.北京:化学工业出版社,2001.894-895\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1}],\"REPEAT\":\"0\",\"DRUG_LO_NAME\":\"维生素B6注射液\",\"DRUG_LO_ID\":\"1872\",\"ADMINISTRATION\":\"静脉滴注\",\"DOSAGE\":\"100\",\"DOSAGE_UNIT\":\"mg\",\"FREQ_COUNT\":\"qd\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20190307\",\"END_DAY\":\"20190307\",\"DEPT_CODE\":\"5\",\"DOCTOR_NAME\":\"张霭润\",\"ORDER_NO\":\"14439589\",\"ORDER_SUB_NO\":\"4\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"住院医师\",\"GROUP_ID\":\"14439589\",\"USER_ID\":\"ZAR\",\"PRES_ID\":\"105650_10009\",\"PRES_DATE\":\"20190307\",\"PRES_SEQ_ID\":\"105650_10009\",\"PK_ORDER_NO\":\"14439589\",\"COURSE\":\"0\",\"PKG_COUNT\":\"\",\"PKG_UNIT\":\"盒\",\"BAK_01\":\"\",\"BAK_02\":\"否\",\"BAK_03\":\"注射液\",\"BAK_04\":\"1ml:50mg*10\",\"BAK_05\":\"\",\"HIGHEST_WARNING_LEVEL\":1,\"PERFORM_SCHEDULE\":\"\",\"ANTI_DRUG_REGISTER\":\"\"},{\"checkInfoList\":[{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和呋塞米注射液(药品码1876，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  碳酸氢钠与利尿药合用，有促使出现低氯化物碱中毒的危险性。\\r\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"国家基本药物(西药).第二版.国家药品监督管理局.北京:人民卫生出版社,2002.756,国家药典委员会.中华人民共和国药典(二部)临床用药须知.2000年版.北京:化学工业出版社,2001.P366-368,龙胆碳酸氢钠片说明书,依他尼酸片说明书(再评价说明书)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和呋塞米注射液(药品码1876，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  碳酸氢钠与利尿药合用，有促使出现低氯化物碱中毒的危险性。\\r\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"国家基本药物(西药).第二版.国家药品监督管理局.北京:人民卫生出版社,2002.756,国家药典委员会.中华人民共和国药典(二部)临床用药须知.2000年版.北京:化学工业出版社,2001.P366-368,龙胆碳酸氢钠片说明书,依他尼酸片说明书(再评价说明书)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和维生素B6注射液(药品码1872，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n碱性药物可破坏维生素B类，降低后者的药效。\\r\\n【处理】\\r\\n合用需谨慎。\\r\\n\\n\",\"REF_SOURCE\":\"干酵母片说明书,维生素B1片说明书\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和呋塞米注射液(药品码1876，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  碳酸氢钠与利尿药合用，有促使出现低氯化物碱中毒的危险性。\\r\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"国家基本药物(西药).第二版.国家药品监督管理局.北京:人民卫生出版社,2002.756,国家药典委员会.中华人民共和国药典(二部)临床用药须知.2000年版.北京:化学工业出版社,2001.P366-368,龙胆碳酸氢钠片说明书,依他尼酸片说明书(再评价说明书)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和呋塞米注射液(药品码1876，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  碳酸氢钠与利尿药合用，有促使出现低氯化物碱中毒的危险性。\\r\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"国家基本药物(西药).第二版.国家药品监督管理局.北京:人民卫生出版社,2002.756,国家药典委员会.中华人民共和国药典(二部)临床用药须知.2000年版.北京:化学工业出版社,2001.P366-368,龙胆碳酸氢钠片说明书,依他尼酸片说明书(再评价说明书)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和注射用甲泼尼龙琥珀酸钠(药品码595，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  碳酸氢钠与肾上腺皮质激素(尤其是具有较强的盐皮质激素作用者)合用时，易致高钠血症和水肿。\\r\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"碳酸氢钠注射液说明书(湖北广济药业股份有限公司)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1}],\"REPEAT\":\"0\",\"DRUG_LO_NAME\":\"碳酸氢钠注射液\",\"DRUG_LO_ID\":\"621\",\"ADMINISTRATION\":\"静脉滴注\",\"DOSAGE\":\"50\",\"DOSAGE_UNIT\":\"ml\",\"FREQ_COUNT\":\"qd\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20190307\",\"END_DAY\":\"20190307\",\"DEPT_CODE\":\"5\",\"DOCTOR_NAME\":\"张霭润\",\"ORDER_NO\":\"14439590\",\"ORDER_SUB_NO\":\"2\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"住院医师\",\"GROUP_ID\":\"14439590\",\"USER_ID\":\"ZAR\",\"PRES_ID\":\"105650_10009\",\"PRES_DATE\":\"20190307\",\"PRES_SEQ_ID\":\"105650_10009\",\"PK_ORDER_NO\":\"14439590\",\"COURSE\":\"0\",\"PKG_COUNT\":\"\",\"PKG_UNIT\":\"瓶\",\"BAK_01\":\"\",\"BAK_02\":\"否\",\"BAK_03\":\"注射液\",\"BAK_04\":\"250ml:12.5g*1\",\"BAK_05\":\"\",\"HIGHEST_WARNING_LEVEL\":1,\"PERFORM_SCHEDULE\":\"\",\"ANTI_DRUG_REGISTER\":\"\"},{\"checkInfoList\":[{\"COLOR\":\"黄色\",\"NAME\":\"适应症\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"超适应症用药。药品说明书适应症为：水肿性疾病、充血性心力衰竭、肝硬化、肾脏疾病、肾炎、肾病、急性肾功能衰竭、慢性肾功能衰竭、急性肺水肿、急性脑水肿、高血压、急性肾功能衰竭、肾脏血流灌注不足、失水、休克、中毒、麻醉意外、循环功能不全、急性肾小管坏死、高钾血症、高钙血症、稀释性低钠血症、抗利尿激素分泌过多症、SIADH、急性药物毒物中毒、巴比妥类药物中毒、。医生诊断为：急性淋巴细胞白血病、急性淋巴细胞白血病复发（B、IR、TEL/AML+、巩固治疗1）、自身免疫性溶血性贫血、口腔溃疡、葡萄糖-6-磷酸酶缺乏、左侧豆状核出血（陈旧性）、松果体囊肿可能、副鼻窦炎、腺样体肥大\",\"REF_SOURCE\":\"药品说明书及医院相关规定\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和碳酸氢钠注射液(药品码621，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  碳酸氢钠与利尿药合用，有促使出现低氯化物碱中毒的危险性。\\r\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"国家基本药物(西药).第二版.国家药品监督管理局.北京:人民卫生出版社,2002.756,国家药典委员会.中华人民共和国药典(二部)临床用药须知.2000年版.北京:化学工业出版社,2001.P366-368,龙胆碳酸氢钠片说明书,依他尼酸片说明书(再评价说明书)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和注射用头孢哌酮钠舒巴坦钠(药品码2003，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  头孢类抗生素与强利尿药合用可加重肾毒性。托拉塞米在高剂量时也可能加重头孢类抗生素的耳毒性与肾毒性。\\r\\n\\n\\n【机制】\\r\\n  可能是利尿药脱水作用引起血药浓度升高所致。此外，头孢噻啶与呋塞米合用，可使肾小球滤过率和肾小管分泌竞争降低，从而增加头孢噻啶的肾毒性。\\r\\n\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"李大魁,孙忠实,王功立.常用处方药使用指南.第一版.北京:中国协和医科大学出版社,2000.13-16,李大魁等.常用处方药使用指南.第一版.中国协和医科大学出版社,2000.16-18,头孢克洛咀嚼片说明书(迪沙药业集团有限公司),托拉塞米注射液说明书(深圳信立泰药业有限公司),盐酸头孢他美酯干混悬剂说明书(浙江震元制药有限公司),中华人民共和国药典.2000年版.二部.临床用药须知.国家药典委员会编.北京:化学工业出版社,2001.639-640,周德尚.新编进口药品手册.南昌:江西科学技术出版社,65-67,注射用头孢他啶说明书(国药集团),注射用头孢替唑钠说明书(天津新丰制药有限公司)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和碳酸氢钠注射液(药品码621，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  碳酸氢钠与利尿药合用，有促使出现低氯化物碱中毒的危险性。\\r\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"国家基本药物(西药).第二版.国家药品监督管理局.北京:人民卫生出版社,2002.756,国家药典委员会.中华人民共和国药典(二部)临床用药须知.2000年版.北京:化学工业出版社,2001.P366-368,龙胆碳酸氢钠片说明书,依他尼酸片说明书(再评价说明书)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和碳酸氢钠注射液(药品码621，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  碳酸氢钠与利尿药合用，有促使出现低氯化物碱中毒的危险性。\\r\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"国家基本药物(西药).第二版.国家药品监督管理局.北京:人民卫生出版社,2002.756,国家药典委员会.中华人民共和国药典(二部)临床用药须知.2000年版.北京:化学工业出版社,2001.P366-368,龙胆碳酸氢钠片说明书,依他尼酸片说明书(再评价说明书)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和注射用甲泼尼龙琥珀酸钠(药品码595，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  肾上腺皮质激素可致水钠潴留，降低排钾利尿药(袢利尿药、噻嗪类利尿药、碳酸酐酶抑制药、吲达帕胺等)的利尿作用，升高电解质紊乱的风险，尤其是低钾血症。\\r\\n【处理】\\r\\n   对使用排钾利尿药的患者，当开始、停用或改变皮质激素类药治疗时，应严密监测血钾及心脏功能。必要时予以纠正低钾血症，还应注意患者是否正在使用洋地黄糖苷类药。\\r\\n\",\"REF_SOURCE\":\"比索洛尔氢氯噻嗪片说明书(德国默克公司(Merck KGaA)),陈新谦,金有豫,汤光.新编药物学.第15版.北京:人民卫生出版社,2003.487-488,醋酸曲安奈德注射液说明书(上海旭东海普药业有限公司),贾公孚,谢惠民.临床药物新用联用大全.第一版.北京:人民卫生出版社,1999.294-297,罗明生,高天惠等.现代临床药物大典.第一版.成都:四川科学技术出版社,2001.777,依他尼酸片说明书(再评价品说明书),乙酰唑胺片说明书,吲达帕胺片说明书(北京市燕京制药厂)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1}],\"REPEAT\":\"0\",\"DRUG_LO_NAME\":\"呋塞米注射液\",\"DRUG_LO_ID\":\"1876\",\"ADMINISTRATION\":\"静脉注射\",\"DOSAGE\":\"15\",\"DOSAGE_UNIT\":\"mg\",\"FREQ_COUNT\":\"qd\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20190307\",\"END_DAY\":\"20190307\",\"DEPT_CODE\":\"5\",\"DOCTOR_NAME\":\"张霭润\",\"ORDER_NO\":\"14439591\",\"ORDER_SUB_NO\":\"2\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"住院医师\",\"GROUP_ID\":\"14439591\",\"USER_ID\":\"ZAR\",\"PRES_ID\":\"105650_10009\",\"PRES_DATE\":\"20190307\",\"PRES_SEQ_ID\":\"105650_10009\",\"PK_ORDER_NO\":\"14439591\",\"COURSE\":\"0\",\"PKG_COUNT\":\"\",\"PKG_UNIT\":\"盒\",\"BAK_01\":\"\",\"BAK_02\":\"否\",\"BAK_03\":\"注射液\",\"BAK_04\":\"2ml:20mg*10\",\"BAK_05\":\"\",\"HIGHEST_WARNING_LEVEL\":1,\"PERFORM_SCHEDULE\":\"\",\"ANTI_DRUG_REGISTER\":\"\"},{\"checkInfoList\":[{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和呋塞米注射液(药品码1876，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  碳酸氢钠与利尿药合用，有促使出现低氯化物碱中毒的危险性。\\r\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"国家基本药物(西药).第二版.国家药品监督管理局.北京:人民卫生出版社,2002.756,国家药典委员会.中华人民共和国药典(二部)临床用药须知.2000年版.北京:化学工业出版社,2001.P366-368,龙胆碳酸氢钠片说明书,依他尼酸片说明书(再评价说明书)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和维生素B6注射液(药品码1872，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n碱性药物可破坏维生素B类，降低后者的药效。\\r\\n【处理】\\r\\n合用需谨慎。\\r\\n\\n\",\"REF_SOURCE\":\"干酵母片说明书,维生素B1片说明书\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和呋塞米注射液(药品码1876，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  碳酸氢钠与利尿药合用，有促使出现低氯化物碱中毒的危险性。\\r\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"国家基本药物(西药).第二版.国家药品监督管理局.北京:人民卫生出版社,2002.756,国家药典委员会.中华人民共和国药典(二部)临床用药须知.2000年版.北京:化学工业出版社,2001.P366-368,龙胆碳酸氢钠片说明书,依他尼酸片说明书(再评价说明书)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和呋塞米注射液(药品码1876，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  碳酸氢钠与利尿药合用，有促使出现低氯化物碱中毒的危险性。\\r\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"国家基本药物(西药).第二版.国家药品监督管理局.北京:人民卫生出版社,2002.756,国家药典委员会.中华人民共和国药典(二部)临床用药须知.2000年版.北京:化学工业出版社,2001.P366-368,龙胆碳酸氢钠片说明书,依他尼酸片说明书(再评价说明书)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和注射用甲泼尼龙琥珀酸钠(药品码595，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  碳酸氢钠与肾上腺皮质激素(尤其是具有较强的盐皮质激素作用者)合用时，易致高钠血症和水肿。\\r\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"碳酸氢钠注射液说明书(湖北广济药业股份有限公司)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1}],\"REPEAT\":\"0\",\"DRUG_LO_NAME\":\"碳酸氢钠注射液\",\"DRUG_LO_ID\":\"621\",\"ADMINISTRATION\":\"静脉滴注\",\"DOSAGE\":\"50\",\"DOSAGE_UNIT\":\"ml\",\"FREQ_COUNT\":\"qd\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20190307\",\"END_DAY\":\"20190307\",\"DEPT_CODE\":\"5\",\"DOCTOR_NAME\":\"张霭润\",\"ORDER_NO\":\"14439647\",\"ORDER_SUB_NO\":\"2\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"住院医师\",\"GROUP_ID\":\"14439647\",\"USER_ID\":\"ZAR\",\"PRES_ID\":\"105650_10009\",\"PRES_DATE\":\"20190307\",\"PRES_SEQ_ID\":\"105650_10009\",\"PK_ORDER_NO\":\"14439647\",\"COURSE\":\"0\",\"PKG_COUNT\":\"\",\"PKG_UNIT\":\"瓶\",\"BAK_01\":\"\",\"BAK_02\":\"否\",\"BAK_03\":\"注射液\",\"BAK_04\":\"250ml:12.5g*1\",\"BAK_05\":\"\",\"HIGHEST_WARNING_LEVEL\":1,\"PERFORM_SCHEDULE\":\"\",\"ANTI_DRUG_REGISTER\":\"\"},{\"checkInfoList\":[{\"COLOR\":\"黄色\",\"NAME\":\"适应症\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"超适应症用药。药品说明书适应症为：水肿性疾病、充血性心力衰竭、肝硬化、肾脏疾病、肾炎、肾病、急性肾功能衰竭、慢性肾功能衰竭、急性肺水肿、急性脑水肿、高血压、急性肾功能衰竭、肾脏血流灌注不足、失水、休克、中毒、麻醉意外、循环功能不全、急性肾小管坏死、高钾血症、高钙血症、稀释性低钠血症、抗利尿激素分泌过多症、SIADH、急性药物毒物中毒、巴比妥类药物中毒、。医生诊断为：急性淋巴细胞白血病、急性淋巴细胞白血病复发（B、IR、TEL/AML+、巩固治疗1）、自身免疫性溶血性贫血、口腔溃疡、葡萄糖-6-磷酸酶缺乏、左侧豆状核出血（陈旧性）、松果体囊肿可能、副鼻窦炎、腺样体肥大\",\"REF_SOURCE\":\"药品说明书及医院相关规定\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和注射用头孢哌酮钠舒巴坦钠(药品码2003，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  头孢类抗生素与强利尿药合用可加重肾毒性。托拉塞米在高剂量时也可能加重头孢类抗生素的耳毒性与肾毒性。\\r\\n\\n\\n【机制】\\r\\n  可能是利尿药脱水作用引起血药浓度升高所致。此外，头孢噻啶与呋塞米合用，可使肾小球滤过率和肾小管分泌竞争降低，从而增加头孢噻啶的肾毒性。\\r\\n\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"李大魁,孙忠实,王功立.常用处方药使用指南.第一版.北京:中国协和医科大学出版社,2000.13-16,李大魁等.常用处方药使用指南.第一版.中国协和医科大学出版社,2000.16-18,头孢克洛咀嚼片说明书(迪沙药业集团有限公司),托拉塞米注射液说明书(深圳信立泰药业有限公司),盐酸头孢他美酯干混悬剂说明书(浙江震元制药有限公司),中华人民共和国药典.2000年版.二部.临床用药须知.国家药典委员会编.北京:化学工业出版社,2001.639-640,周德尚.新编进口药品手册.南昌:江西科学技术出版社,65-67,注射用头孢他啶说明书(国药集团),注射用头孢替唑钠说明书(天津新丰制药有限公司)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和碳酸氢钠注射液(药品码621，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  碳酸氢钠与利尿药合用，有促使出现低氯化物碱中毒的危险性。\\r\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"国家基本药物(西药).第二版.国家药品监督管理局.北京:人民卫生出版社,2002.756,国家药典委员会.中华人民共和国药典(二部)临床用药须知.2000年版.北京:化学工业出版社,2001.P366-368,龙胆碳酸氢钠片说明书,依他尼酸片说明书(再评价说明书)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和碳酸氢钠注射液(药品码621，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  碳酸氢钠与利尿药合用，有促使出现低氯化物碱中毒的危险性。\\r\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"国家基本药物(西药).第二版.国家药品监督管理局.北京:人民卫生出版社,2002.756,国家药典委员会.中华人民共和国药典(二部)临床用药须知.2000年版.北京:化学工业出版社,2001.P366-368,龙胆碳酸氢钠片说明书,依他尼酸片说明书(再评价说明书)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和注射用甲泼尼龙琥珀酸钠(药品码595，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  肾上腺皮质激素可致水钠潴留，降低排钾利尿药(袢利尿药、噻嗪类利尿药、碳酸酐酶抑制药、吲达帕胺等)的利尿作用，升高电解质紊乱的风险，尤其是低钾血症。\\r\\n【处理】\\r\\n   对使用排钾利尿药的患者，当开始、停用或改变皮质激素类药治疗时，应严密监测血钾及心脏功能。必要时予以纠正低钾血症，还应注意患者是否正在使用洋地黄糖苷类药。\\r\\n\",\"REF_SOURCE\":\"比索洛尔氢氯噻嗪片说明书(德国默克公司(Merck KGaA)),陈新谦,金有豫,汤光.新编药物学.第15版.北京:人民卫生出版社,2003.487-488,醋酸曲安奈德注射液说明书(上海旭东海普药业有限公司),贾公孚,谢惠民.临床药物新用联用大全.第一版.北京:人民卫生出版社,1999.294-297,罗明生,高天惠等.现代临床药物大典.第一版.成都:四川科学技术出版社,2001.777,依他尼酸片说明书(再评价品说明书),乙酰唑胺片说明书,吲达帕胺片说明书(北京市燕京制药厂)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1}],\"REPEAT\":\"0\",\"DRUG_LO_NAME\":\"呋塞米注射液\",\"DRUG_LO_ID\":\"1876\",\"ADMINISTRATION\":\"静脉注射\",\"DOSAGE\":\"15\",\"DOSAGE_UNIT\":\"mg\",\"FREQ_COUNT\":\"qd\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20190307\",\"END_DAY\":\"20190307\",\"DEPT_CODE\":\"5\",\"DOCTOR_NAME\":\"张霭润\",\"ORDER_NO\":\"14439648\",\"ORDER_SUB_NO\":\"2\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"住院医师\",\"GROUP_ID\":\"14439648\",\"USER_ID\":\"ZAR\",\"PRES_ID\":\"105650_10009\",\"PRES_DATE\":\"20190307\",\"PRES_SEQ_ID\":\"105650_10009\",\"PK_ORDER_NO\":\"14439648\",\"COURSE\":\"0\",\"PKG_COUNT\":\"\",\"PKG_UNIT\":\"盒\",\"BAK_01\":\"\",\"BAK_02\":\"否\",\"BAK_03\":\"注射液\",\"BAK_04\":\"2ml:20mg*10\",\"BAK_05\":\"\",\"HIGHEST_WARNING_LEVEL\":1,\"PERFORM_SCHEDULE\":\"\",\"ANTI_DRUG_REGISTER\":\"\"},{\"checkInfoList\":[{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和呋塞米注射液(药品码1876，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  头孢类抗生素与强利尿药合用可加重肾毒性。托拉塞米在高剂量时也可能加重头孢类抗生素的耳毒性与肾毒性。\\r\\n\\n\\n【机制】\\r\\n  可能是利尿药脱水作用引起血药浓度升高所致。此外，头孢噻啶与呋塞米合用，可使肾小球滤过率和肾小管分泌竞争降低，从而增加头孢噻啶的肾毒性。\\r\\n\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"李大魁,孙忠实,王功立.常用处方药使用指南.第一版.北京:中国协和医科大学出版社,2000.13-16,李大魁等.常用处方药使用指南.第一版.中国协和医科大学出版社,2000.16-18,头孢克洛咀嚼片说明书(迪沙药业集团有限公司),托拉塞米注射液说明书(深圳信立泰药业有限公司),盐酸头孢他美酯干混悬剂说明书(浙江震元制药有限公司),中华人民共和国药典.2000年版.二部.临床用药须知.国家药典委员会编.北京:化学工业出版社,2001.639-640,周德尚.新编进口药品手册.南昌:江西科学技术出版社,65-67,注射用头孢他啶说明书(国药集团),注射用头孢替唑钠说明书(天津新丰制药有限公司)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和呋塞米注射液(药品码1876，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  头孢类抗生素与强利尿药合用可加重肾毒性。托拉塞米在高剂量时也可能加重头孢类抗生素的耳毒性与肾毒性。\\r\\n\\n\\n【机制】\\r\\n  可能是利尿药脱水作用引起血药浓度升高所致。此外，头孢噻啶与呋塞米合用，可使肾小球滤过率和肾小管分泌竞争降低，从而增加头孢噻啶的肾毒性。\\r\\n\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"李大魁,孙忠实,王功立.常用处方药使用指南.第一版.北京:中国协和医科大学出版社,2000.13-16,李大魁等.常用处方药使用指南.第一版.中国协和医科大学出版社,2000.16-18,头孢克洛咀嚼片说明书(迪沙药业集团有限公司),托拉塞米注射液说明书(深圳信立泰药业有限公司),盐酸头孢他美酯干混悬剂说明书(浙江震元制药有限公司),中华人民共和国药典.2000年版.二部.临床用药须知.国家药典委员会编.北京:化学工业出版社,2001.639-640,周德尚.新编进口药品手册.南昌:江西科学技术出版社,65-67,注射用头孢他啶说明书(国药集团),注射用头孢替唑钠说明书(天津新丰制药有限公司)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1}],\"REPEAT\":\"1\",\"DRUG_LO_NAME\":\"注射用头孢哌酮钠舒巴坦钠\",\"DRUG_LO_ID\":\"2003\",\"ADMINISTRATION\":\"静脉滴注\",\"DOSAGE\":\"1.5\",\"DOSAGE_UNIT\":\"g\",\"FREQ_COUNT\":\"q8h\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20190301\",\"END_DAY\":\"20211124\",\"DEPT_CODE\":\"5\",\"DOCTOR_NAME\":\"薛红漫\",\"ORDER_NO\":\"14432834\",\"ORDER_SUB_NO\":\"2\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"主任医师\",\"GROUP_ID\":\"14432834\",\"USER_ID\":\"XHM\",\"PRES_ID\":\"105650_10009\",\"PRES_DATE\":\"20190301\",\"PRES_SEQ_ID\":\"105650_10009\",\"PK_ORDER_NO\":\"14432834\",\"COURSE\":\"\",\"PKG_COUNT\":\"3\",\"PKG_UNIT\":\"瓶\",\"BAK_01\":\"\",\"BAK_02\":\"是\",\"BAK_03\":\"注射用无菌粉末\",\"BAK_04\":\"1.5g*1\",\"BAK_05\":\"\",\"HIGHEST_WARNING_LEVEL\":1,\"PERFORM_SCHEDULE\":\"\",\"ANTI_DRUG_REGISTER\":\"\"},{\"checkInfoList\":[{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和碳酸氢钠注射液(药品码621，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n碱性药物可破坏维生素B类，降低后者的药效。\\r\\n【处理】\\r\\n合用需谨慎。\\r\\n\\n\",\"REF_SOURCE\":\"干酵母片说明书,维生素B1片说明书\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和碳酸氢钠注射液(药品码621，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n碱性药物可破坏维生素B类，降低后者的药效。\\r\\n【处理】\\r\\n合用需谨慎。\\r\\n\\n\",\"REF_SOURCE\":\"干酵母片说明书,维生素B1片说明书\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和注射用甲泼尼龙琥珀酸钠(药品码595，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  维生素B6与免疫抑制药(青霉胺、环磷酰胺、肾上腺皮质激素、异烟肼等)合用，可能引起贫血或周围神经炎。其中，维生素B6与环磷酰胺合用，同时还可减轻环磷酰胺所引起的肝脏、胃肠道不良反应。\\r\\n【机制】\\r\\n  免疫抑制药对维生素B6有拮抗作用或可增加维生素B6肾排泄率\\n\\n【处理】\\r\\n  长期服用上述免疫抑制药者，维生素B6需要量增加，可一日加服25mg维生素B6。\\r\\n\",\"REF_SOURCE\":\"贾公孚,谢惠民.临床药物新用联用大全.第一版.北京:人民卫生出版社,1999.352-354,436-437,维生素B6片说明书,中华人民共和国药典.2000年版.二部.临床用药须知.国家药典委员会编.北京:化学工业出版社,2001.894-895\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1}],\"REPEAT\":\"0\",\"DRUG_LO_NAME\":\"维生素B6注射液\",\"DRUG_LO_ID\":\"1872\",\"ADMINISTRATION\":\"静脉滴注\",\"DOSAGE\":\"100\",\"DOSAGE_UNIT\":\"mg\",\"FREQ_COUNT\":\"qd\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20190306\",\"END_DAY\":\"20190306\",\"DEPT_CODE\":\"5\",\"DOCTOR_NAME\":\"林超\",\"ORDER_NO\":\"14438263\",\"ORDER_SUB_NO\":\"2\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"住院医师\",\"GROUP_ID\":\"14438263\",\"USER_ID\":\"LC01\",\"PRES_ID\":\"105650_10009\",\"PRES_DATE\":\"20190306\",\"PRES_SEQ_ID\":\"105650_10009\",\"PK_ORDER_NO\":\"14438263\",\"COURSE\":\"0\",\"PKG_COUNT\":\"1\",\"PKG_UNIT\":\"盒\",\"BAK_01\":\"\",\"BAK_02\":\"否\",\"BAK_03\":\"注射液\",\"BAK_04\":\"1ml:50mg*10\",\"BAK_05\":\"\",\"HIGHEST_WARNING_LEVEL\":1,\"PERFORM_SCHEDULE\":\"\",\"ANTI_DRUG_REGISTER\":\"\"},{\"checkInfoList\":[{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和呋塞米注射液(药品码1876，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  碳酸氢钠与利尿药合用，有促使出现低氯化物碱中毒的危险性。\\r\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"国家基本药物(西药).第二版.国家药品监督管理局.北京:人民卫生出版社,2002.756,国家药典委员会.中华人民共和国药典(二部)临床用药须知.2000年版.北京:化学工业出版社,2001.P366-368,龙胆碳酸氢钠片说明书,依他尼酸片说明书(再评价说明书)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和呋塞米注射液(药品码1876，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  碳酸氢钠与利尿药合用，有促使出现低氯化物碱中毒的危险性。\\r\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"国家基本药物(西药).第二版.国家药品监督管理局.北京:人民卫生出版社,2002.756,国家药典委员会.中华人民共和国药典(二部)临床用药须知.2000年版.北京:化学工业出版社,2001.P366-368,龙胆碳酸氢钠片说明书,依他尼酸片说明书(再评价说明书)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和注射用甲泼尼龙琥珀酸钠(药品码595，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  碳酸氢钠与肾上腺皮质激素(尤其是具有较强的盐皮质激素作用者)合用时，易致高钠血症和水肿。\\r\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"碳酸氢钠注射液说明书(湖北广济药业股份有限公司)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1}],\"REPEAT\":\"0\",\"DRUG_LO_NAME\":\"碳酸氢钠注射液\",\"DRUG_LO_ID\":\"621\",\"ADMINISTRATION\":\"静脉滴注\",\"DOSAGE\":\"50\",\"DOSAGE_UNIT\":\"ml\",\"FREQ_COUNT\":\"qd\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20190306\",\"END_DAY\":\"20190306\",\"DEPT_CODE\":\"5\",\"DOCTOR_NAME\":\"林超\",\"ORDER_NO\":\"14438264\",\"ORDER_SUB_NO\":\"2\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"住院医师\",\"GROUP_ID\":\"14438264\",\"USER_ID\":\"LC01\",\"PRES_ID\":\"105650_10009\",\"PRES_DATE\":\"20190306\",\"PRES_SEQ_ID\":\"105650_10009\",\"PK_ORDER_NO\":\"14438264\",\"COURSE\":\"0\",\"PKG_COUNT\":\"1\",\"PKG_UNIT\":\"瓶\",\"BAK_01\":\"\",\"BAK_02\":\"否\",\"BAK_03\":\"注射液\",\"BAK_04\":\"250ml:12.5g*1\",\"BAK_05\":\"\",\"HIGHEST_WARNING_LEVEL\":1,\"PERFORM_SCHEDULE\":\"\",\"ANTI_DRUG_REGISTER\":\"\"},{\"checkInfoList\":[{\"COLOR\":\"黄色\",\"NAME\":\"适应症\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"超适应症用药。药品说明书适应症为：水肿性疾病、充血性心力衰竭、肝硬化、肾脏疾病、肾炎、肾病、急性肾功能衰竭、慢性肾功能衰竭、急性肺水肿、急性脑水肿、高血压、急性肾功能衰竭、肾脏血流灌注不足、失水、休克、中毒、麻醉意外、循环功能不全、急性肾小管坏死、高钾血症、高钙血症、稀释性低钠血症、抗利尿激素分泌过多症、SIADH、急性药物毒物中毒、巴比妥类药物中毒、。医生诊断为：急性淋巴细胞白血病、急性淋巴细胞白血病复发（B、IR、TEL/AML+、巩固治疗1）、自身免疫性溶血性贫血、口腔溃疡、葡萄糖-6-磷酸酶缺乏、左侧豆状核出血（陈旧性）、松果体囊肿可能、副鼻窦炎、腺样体肥大\",\"REF_SOURCE\":\"药品说明书及医院相关规定\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和碳酸氢钠注射液(药品码621，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  碳酸氢钠与利尿药合用，有促使出现低氯化物碱中毒的危险性。\\r\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"国家基本药物(西药).第二版.国家药品监督管理局.北京:人民卫生出版社,2002.756,国家药典委员会.中华人民共和国药典(二部)临床用药须知.2000年版.北京:化学工业出版社,2001.P366-368,龙胆碳酸氢钠片说明书,依他尼酸片说明书(再评价说明书)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和注射用甲泼尼龙琥珀酸钠(药品码595，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  肾上腺皮质激素可致水钠潴留，降低排钾利尿药(袢利尿药、噻嗪类利尿药、碳酸酐酶抑制药、吲达帕胺等)的利尿作用，升高电解质紊乱的风险，尤其是低钾血症。\\r\\n【处理】\\r\\n   对使用排钾利尿药的患者，当开始、停用或改变皮质激素类药治疗时，应严密监测血钾及心脏功能。必要时予以纠正低钾血症，还应注意患者是否正在使用洋地黄糖苷类药。\\r\\n\",\"REF_SOURCE\":\"比索洛尔氢氯噻嗪片说明书(德国默克公司(Merck KGaA)),陈新谦,金有豫,汤光.新编药物学.第15版.北京:人民卫生出版社,2003.487-488,醋酸曲安奈德注射液说明书(上海旭东海普药业有限公司),贾公孚,谢惠民.临床药物新用联用大全.第一版.北京:人民卫生出版社,1999.294-297,罗明生,高天惠等.现代临床药物大典.第一版.成都:四川科学技术出版社,2001.777,依他尼酸片说明书(再评价品说明书),乙酰唑胺片说明书,吲达帕胺片说明书(北京市燕京制药厂)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1}],\"REPEAT\":\"0\",\"DRUG_LO_NAME\":\"呋塞米注射液\",\"DRUG_LO_ID\":\"1876\",\"ADMINISTRATION\":\"静脉注射\",\"DOSAGE\":\"15\",\"DOSAGE_UNIT\":\"mg\",\"FREQ_COUNT\":\"qd\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20190306\",\"END_DAY\":\"20190306\",\"DEPT_CODE\":\"5\",\"DOCTOR_NAME\":\"林超\",\"ORDER_NO\":\"14438265\",\"ORDER_SUB_NO\":\"2\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"住院医师\",\"GROUP_ID\":\"14438265\",\"USER_ID\":\"LC01\",\"PRES_ID\":\"105650_10009\",\"PRES_DATE\":\"20190306\",\"PRES_SEQ_ID\":\"105650_10009\",\"PK_ORDER_NO\":\"14438265\",\"COURSE\":\"0\",\"PKG_COUNT\":\"1\",\"PKG_UNIT\":\"盒\",\"BAK_01\":\"\",\"BAK_02\":\"否\",\"BAK_03\":\"注射液\",\"BAK_04\":\"2ml:20mg*10\",\"BAK_05\":\"\",\"HIGHEST_WARNING_LEVEL\":1,\"PERFORM_SCHEDULE\":\"\",\"ANTI_DRUG_REGISTER\":\"\"},{\"checkInfoList\":[{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和呋塞米注射液(药品码1876，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  碳酸氢钠与利尿药合用，有促使出现低氯化物碱中毒的危险性。\\r\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"国家基本药物(西药).第二版.国家药品监督管理局.北京:人民卫生出版社,2002.756,国家药典委员会.中华人民共和国药典(二部)临床用药须知.2000年版.北京:化学工业出版社,2001.P366-368,龙胆碳酸氢钠片说明书,依他尼酸片说明书(再评价说明书)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和注射用甲泼尼龙琥珀酸钠(药品码595，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  碳酸氢钠与肾上腺皮质激素(尤其是具有较强的盐皮质激素作用者)合用时，易致高钠血症和水肿。\\r\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"碳酸氢钠注射液说明书(湖北广济药业股份有限公司)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1}],\"REPEAT\":\"0\",\"DRUG_LO_NAME\":\"碳酸氢钠注射液\",\"DRUG_LO_ID\":\"621\",\"ADMINISTRATION\":\"静脉滴注\",\"DOSAGE\":\"50\",\"DOSAGE_UNIT\":\"ml\",\"FREQ_COUNT\":\"qd\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20190306\",\"END_DAY\":\"20190306\",\"DEPT_CODE\":\"5\",\"DOCTOR_NAME\":\"林超\",\"ORDER_NO\":\"14438266\",\"ORDER_SUB_NO\":\"2\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"住院医师\",\"GROUP_ID\":\"14438266\",\"USER_ID\":\"LC01\",\"PRES_ID\":\"105650_10009\",\"PRES_DATE\":\"20190306\",\"PRES_SEQ_ID\":\"105650_10009\",\"PK_ORDER_NO\":\"14438266\",\"COURSE\":\"0\",\"PKG_COUNT\":\"1\",\"PKG_UNIT\":\"瓶\",\"BAK_01\":\"\",\"BAK_02\":\"否\",\"BAK_03\":\"注射液\",\"BAK_04\":\"250ml:12.5g*1\",\"BAK_05\":\"\",\"HIGHEST_WARNING_LEVEL\":1,\"PERFORM_SCHEDULE\":\"\",\"ANTI_DRUG_REGISTER\":\"\"},{\"checkInfoList\":[{\"COLOR\":\"黄色\",\"NAME\":\"适应症\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"超适应症用药。药品说明书适应症为：水肿性疾病、充血性心力衰竭、肝硬化、肾脏疾病、肾炎、肾病、急性肾功能衰竭、慢性肾功能衰竭、急性肺水肿、急性脑水肿、高血压、急性肾功能衰竭、肾脏血流灌注不足、失水、休克、中毒、麻醉意外、循环功能不全、急性肾小管坏死、高钾血症、高钙血症、稀释性低钠血症、抗利尿激素分泌过多症、SIADH、急性药物毒物中毒、巴比妥类药物中毒、。医生诊断为：急性淋巴细胞白血病、急性淋巴细胞白血病复发（B、IR、TEL/AML+、巩固治疗1）、自身免疫性溶血性贫血、口腔溃疡、葡萄糖-6-磷酸酶缺乏、左侧豆状核出血（陈旧性）、松果体囊肿可能、副鼻窦炎、腺样体肥大\",\"REF_SOURCE\":\"药品说明书及医院相关规定\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和注射用甲泼尼龙琥珀酸钠(药品码595，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  肾上腺皮质激素可致水钠潴留，降低排钾利尿药(袢利尿药、噻嗪类利尿药、碳酸酐酶抑制药、吲达帕胺等)的利尿作用，升高电解质紊乱的风险，尤其是低钾血症。\\r\\n【处理】\\r\\n   对使用排钾利尿药的患者，当开始、停用或改变皮质激素类药治疗时，应严密监测血钾及心脏功能。必要时予以纠正低钾血症，还应注意患者是否正在使用洋地黄糖苷类药。\\r\\n\",\"REF_SOURCE\":\"比索洛尔氢氯噻嗪片说明书(德国默克公司(Merck KGaA)),陈新谦,金有豫,汤光.新编药物学.第15版.北京:人民卫生出版社,2003.487-488,醋酸曲安奈德注射液说明书(上海旭东海普药业有限公司),贾公孚,谢惠民.临床药物新用联用大全.第一版.北京:人民卫生出版社,1999.294-297,罗明生,高天惠等.现代临床药物大典.第一版.成都:四川科学技术出版社,2001.777,依他尼酸片说明书(再评价品说明书),乙酰唑胺片说明书,吲达帕胺片说明书(北京市燕京制药厂)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1}],\"REPEAT\":\"0\",\"DRUG_LO_NAME\":\"呋塞米注射液\",\"DRUG_LO_ID\":\"1876\",\"ADMINISTRATION\":\"静脉注射\",\"DOSAGE\":\"15\",\"DOSAGE_UNIT\":\"mg\",\"FREQ_COUNT\":\"qd\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20190306\",\"END_DAY\":\"20190306\",\"DEPT_CODE\":\"5\",\"DOCTOR_NAME\":\"林超\",\"ORDER_NO\":\"14438267\",\"ORDER_SUB_NO\":\"2\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"住院医师\",\"GROUP_ID\":\"14438267\",\"USER_ID\":\"LC01\",\"PRES_ID\":\"105650_10009\",\"PRES_DATE\":\"20190306\",\"PRES_SEQ_ID\":\"105650_10009\",\"PK_ORDER_NO\":\"14438267\",\"COURSE\":\"0\",\"PKG_COUNT\":\"1\",\"PKG_UNIT\":\"盒\",\"BAK_01\":\"\",\"BAK_02\":\"否\",\"BAK_03\":\"注射液\",\"BAK_04\":\"2ml:20mg*10\",\"BAK_05\":\"\",\"HIGHEST_WARNING_LEVEL\":1,\"PERFORM_SCHEDULE\":\"\",\"ANTI_DRUG_REGISTER\":\"\"},{\"checkInfoList\":[],\"REPEAT\":\"0\",\"DRUG_LO_NAME\":\"5%葡萄糖注射液（500ml）\",\"DRUG_LO_ID\":\"616\",\"ADMINISTRATION\":\"静脉滴注\",\"DOSAGE\":\"1100\",\"DOSAGE_UNIT\":\"ml\",\"FREQ_COUNT\":\"qd\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20190307\",\"END_DAY\":\"20190307\",\"DEPT_CODE\":\"5\",\"DOCTOR_NAME\":\"张霭润\",\"ORDER_NO\":\"14439589\",\"ORDER_SUB_NO\":\"1\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"住院医师\",\"GROUP_ID\":\"14439589\",\"USER_ID\":\"ZAR\",\"PRES_ID\":\"105650_10009\",\"PRES_DATE\":\"20190307\",\"PRES_SEQ_ID\":\"105650_10009\",\"PK_ORDER_NO\":\"14439589\",\"COURSE\":\"0\",\"PKG_COUNT\":\"\",\"PKG_UNIT\":\"袋\",\"BAK_01\":\"\",\"BAK_02\":\"否\",\"BAK_03\":\"注射液\",\"BAK_04\":\"5%*500ml*1\",\"BAK_05\":\"\",\"HIGHEST_WARNING_LEVEL\":0,\"PERFORM_SCHEDULE\":\"\",\"ANTI_DRUG_REGISTER\":\"\"},{\"checkInfoList\":[],\"REPEAT\":\"0\",\"DRUG_LO_NAME\":\"0.9%氯化钠注射液（500ml）\",\"DRUG_LO_ID\":\"611\",\"ADMINISTRATION\":\"静脉滴注\",\"DOSAGE\":\"500\",\"DOSAGE_UNIT\":\"ml\",\"FREQ_COUNT\":\"qd\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20190307\",\"END_DAY\":\"20190307\",\"DEPT_CODE\":\"5\",\"DOCTOR_NAME\":\"张霭润\",\"ORDER_NO\":\"14439589\",\"ORDER_SUB_NO\":\"2\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"住院医师\",\"GROUP_ID\":\"14439589\",\"USER_ID\":\"ZAR\",\"PRES_ID\":\"105650_10009\",\"PRES_DATE\":\"20190307\",\"PRES_SEQ_ID\":\"105650_10009\",\"PK_ORDER_NO\":\"14439589\",\"COURSE\":\"0\",\"PKG_COUNT\":\"\",\"PKG_UNIT\":\"袋\",\"BAK_01\":\"\",\"BAK_02\":\"否\",\"BAK_03\":\"注射液\",\"BAK_04\":\"0.9%*500ml*1\",\"BAK_05\":\"\",\"HIGHEST_WARNING_LEVEL\":0,\"PERFORM_SCHEDULE\":\"\",\"ANTI_DRUG_REGISTER\":\"\"},{\"checkInfoList\":[],\"REPEAT\":\"0\",\"DRUG_LO_NAME\":\"10%氯化钾注射液\",\"DRUG_LO_ID\":\"4395\",\"ADMINISTRATION\":\"静脉滴注\",\"DOSAGE\":\"3\",\"DOSAGE_UNIT\":\"g\",\"FREQ_COUNT\":\"qd\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20190307\",\"END_DAY\":\"20190307\",\"DEPT_CODE\":\"5\",\"DOCTOR_NAME\":\"张霭润\",\"ORDER_NO\":\"14439589\",\"ORDER_SUB_NO\":\"3\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"住院医师\",\"GROUP_ID\":\"14439589\",\"USER_ID\":\"ZAR\",\"PRES_ID\":\"105650_10009\",\"PRES_DATE\":\"20190307\",\"PRES_SEQ_ID\":\"105650_10009\",\"PK_ORDER_NO\":\"14439589\",\"COURSE\":\"0\",\"PKG_COUNT\":\"\",\"PKG_UNIT\":\"盒\",\"BAK_01\":\"\",\"BAK_02\":\"否\",\"BAK_03\":\"注射液\",\"BAK_04\":\"10ml:1g*50\",\"BAK_05\":\"\",\"HIGHEST_WARNING_LEVEL\":0,\"PERFORM_SCHEDULE\":\"\",\"ANTI_DRUG_REGISTER\":\"\"},{\"checkInfoList\":[],\"REPEAT\":\"0\",\"DRUG_LO_NAME\":\"5%葡萄糖注射液（250ml）\",\"DRUG_LO_ID\":\"619\",\"ADMINISTRATION\":\"静脉滴注\",\"DOSAGE\":\"125\",\"DOSAGE_UNIT\":\"ml\",\"FREQ_COUNT\":\"qd\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20190307\",\"END_DAY\":\"20190307\",\"DEPT_CODE\":\"5\",\"DOCTOR_NAME\":\"张霭润\",\"ORDER_NO\":\"14439590\",\"ORDER_SUB_NO\":\"1\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"住院医师\",\"GROUP_ID\":\"14439590\",\"USER_ID\":\"ZAR\",\"PRES_ID\":\"105650_10009\",\"PRES_DATE\":\"20190307\",\"PRES_SEQ_ID\":\"105650_10009\",\"PK_ORDER_NO\":\"14439590\",\"COURSE\":\"0\",\"PKG_COUNT\":\"\",\"PKG_UNIT\":\"袋\",\"BAK_01\":\"\",\"BAK_02\":\"否\",\"BAK_03\":\"注射液\",\"BAK_04\":\"5%*250ml*1\",\"BAK_05\":\"\",\"HIGHEST_WARNING_LEVEL\":0,\"PERFORM_SCHEDULE\":\"\",\"ANTI_DRUG_REGISTER\":\"\"},{\"checkInfoList\":[],\"REPEAT\":\"0\",\"DRUG_LO_NAME\":\"0.9%氯化钠注射液（10ml）\",\"DRUG_LO_ID\":\"4398\",\"ADMINISTRATION\":\"静脉注射\",\"DOSAGE\":\"10\",\"DOSAGE_UNIT\":\"ml\",\"FREQ_COUNT\":\"qd\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20190307\",\"END_DAY\":\"20190307\",\"DEPT_CODE\":\"5\",\"DOCTOR_NAME\":\"张霭润\",\"ORDER_NO\":\"14439591\",\"ORDER_SUB_NO\":\"1\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"住院医师\",\"GROUP_ID\":\"14439591\",\"USER_ID\":\"ZAR\",\"PRES_ID\":\"105650_10009\",\"PRES_DATE\":\"20190307\",\"PRES_SEQ_ID\":\"105650_10009\",\"PK_ORDER_NO\":\"14439591\",\"COURSE\":\"0\",\"PKG_COUNT\":\"\",\"PKG_UNIT\":\"盒\",\"BAK_01\":\"\",\"BAK_02\":\"否\",\"BAK_03\":\"注射液\",\"BAK_04\":\"0.9%*10ml*40\",\"BAK_05\":\"\",\"HIGHEST_WARNING_LEVEL\":0,\"PERFORM_SCHEDULE\":\"\",\"ANTI_DRUG_REGISTER\":\"\"},{\"checkInfoList\":[],\"REPEAT\":\"0\",\"DRUG_LO_NAME\":\"5%葡萄糖注射液（250ml）\",\"DRUG_LO_ID\":\"619\",\"ADMINISTRATION\":\"静脉滴注\",\"DOSAGE\":\"125\",\"DOSAGE_UNIT\":\"ml\",\"FREQ_COUNT\":\"qd\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20190307\",\"END_DAY\":\"20190307\",\"DEPT_CODE\":\"5\",\"DOCTOR_NAME\":\"张霭润\",\"ORDER_NO\":\"14439647\",\"ORDER_SUB_NO\":\"1\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"住院医师\",\"GROUP_ID\":\"14439647\",\"USER_ID\":\"ZAR\",\"PRES_ID\":\"105650_10009\",\"PRES_DATE\":\"20190307\",\"PRES_SEQ_ID\":\"105650_10009\",\"PK_ORDER_NO\":\"14439647\",\"COURSE\":\"0\",\"PKG_COUNT\":\"\",\"PKG_UNIT\":\"袋\",\"BAK_01\":\"\",\"BAK_02\":\"否\",\"BAK_03\":\"注射液\",\"BAK_04\":\"5%*250ml*1\",\"BAK_05\":\"\",\"HIGHEST_WARNING_LEVEL\":0,\"PERFORM_SCHEDULE\":\"\",\"ANTI_DRUG_REGISTER\":\"\"},{\"checkInfoList\":[],\"REPEAT\":\"0\",\"DRUG_LO_NAME\":\"0.9%氯化钠注射液（10ml）\",\"DRUG_LO_ID\":\"4398\",\"ADMINISTRATION\":\"静脉注射\",\"DOSAGE\":\"10\",\"DOSAGE_UNIT\":\"ml\",\"FREQ_COUNT\":\"qd\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20190307\",\"END_DAY\":\"20190307\",\"DEPT_CODE\":\"5\",\"DOCTOR_NAME\":\"张霭润\",\"ORDER_NO\":\"14439648\",\"ORDER_SUB_NO\":\"1\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"住院医师\",\"GROUP_ID\":\"14439648\",\"USER_ID\":\"ZAR\",\"PRES_ID\":\"105650_10009\",\"PRES_DATE\":\"20190307\",\"PRES_SEQ_ID\":\"105650_10009\",\"PK_ORDER_NO\":\"14439648\",\"COURSE\":\"0\",\"PKG_COUNT\":\"\",\"PKG_UNIT\":\"盒\",\"BAK_01\":\"\",\"BAK_02\":\"否\",\"BAK_03\":\"注射液\",\"BAK_04\":\"0.9%*10ml*40\",\"BAK_05\":\"\",\"HIGHEST_WARNING_LEVEL\":0,\"PERFORM_SCHEDULE\":\"\",\"ANTI_DRUG_REGISTER\":\"\"},{\"checkInfoList\":[],\"REPEAT\":\"1\",\"DRUG_LO_NAME\":\"5%葡萄糖注射液（50ml）\",\"DRUG_LO_ID\":\"626\",\"ADMINISTRATION\":\"静脉滴注\",\"DOSAGE\":\"50\",\"DOSAGE_UNIT\":\"ml\",\"FREQ_COUNT\":\"qd\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20190221\",\"END_DAY\":\"20211116\",\"DEPT_CODE\":\"5\",\"DOCTOR_NAME\":\"林超\",\"ORDER_NO\":\"14424614\",\"ORDER_SUB_NO\":\"1\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"住院医师\",\"GROUP_ID\":\"14424614\",\"USER_ID\":\"LC01\",\"PRES_ID\":\"105650_10009\",\"PRES_DATE\":\"20190221\",\"PRES_SEQ_ID\":\"105650_10009\",\"PK_ORDER_NO\":\"14424614\",\"COURSE\":\"\",\"PKG_COUNT\":\"1\",\"PKG_UNIT\":\"瓶\",\"BAK_01\":\"\",\"BAK_02\":\"是\",\"BAK_03\":\"注射液\",\"BAK_04\":\"5%*50ml*1\",\"BAK_05\":\"\",\"HIGHEST_WARNING_LEVEL\":0,\"PERFORM_SCHEDULE\":\"\",\"ANTI_DRUG_REGISTER\":\"\"},{\"checkInfoList\":[],\"REPEAT\":\"1\",\"DRUG_LO_NAME\":\"注射用丁二磺酸腺苷蛋氨酸\",\"DRUG_LO_ID\":\"590\",\"ADMINISTRATION\":\"静脉滴注\",\"DOSAGE\":\"0.5\",\"DOSAGE_UNIT\":\"g\",\"FREQ_COUNT\":\"qd\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20190221\",\"END_DAY\":\"20211116\",\"DEPT_CODE\":\"5\",\"DOCTOR_NAME\":\"林超\",\"ORDER_NO\":\"14424614\",\"ORDER_SUB_NO\":\"2\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"住院医师\",\"GROUP_ID\":\"14424614\",\"USER_ID\":\"LC01\",\"PRES_ID\":\"105650_10009\",\"PRES_DATE\":\"20190221\",\"PRES_SEQ_ID\":\"105650_10009\",\"PK_ORDER_NO\":\"14424614\",\"COURSE\":\"\",\"PKG_COUNT\":\"1\",\"PKG_UNIT\":\"盒\",\"BAK_01\":\"\",\"BAK_02\":\"是\",\"BAK_03\":\"注射用无菌粉末\",\"BAK_04\":\"0.5g*5\",\"BAK_05\":\"\",\"HIGHEST_WARNING_LEVEL\":0,\"PERFORM_SCHEDULE\":\"\",\"ANTI_DRUG_REGISTER\":\"\"},{\"checkInfoList\":[],\"REPEAT\":\"1\",\"DRUG_LO_NAME\":\"0.9%氯化钠注射液（100ml）\",\"DRUG_LO_ID\":\"612\",\"ADMINISTRATION\":\"静脉滴注\",\"DOSAGE\":\"100\",\"DOSAGE_UNIT\":\"ml\",\"FREQ_COUNT\":\"q8h\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20190301\",\"END_DAY\":\"20211124\",\"DEPT_CODE\":\"5\",\"DOCTOR_NAME\":\"薛红漫\",\"ORDER_NO\":\"14432834\",\"ORDER_SUB_NO\":\"1\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"主任医师\",\"GROUP_ID\":\"14432834\",\"USER_ID\":\"XHM\",\"PRES_ID\":\"105650_10009\",\"PRES_DATE\":\"20190301\",\"PRES_SEQ_ID\":\"105650_10009\",\"PK_ORDER_NO\":\"14432834\",\"COURSE\":\"\",\"PKG_COUNT\":\"3\",\"PKG_UNIT\":\"袋\",\"BAK_01\":\"\",\"BAK_02\":\"是\",\"BAK_03\":\"注射液\",\"BAK_04\":\"0.9%*100ml*1\",\"BAK_05\":\"\",\"HIGHEST_WARNING_LEVEL\":0,\"PERFORM_SCHEDULE\":\"\",\"ANTI_DRUG_REGISTER\":\"\"},{\"checkInfoList\":[],\"REPEAT\":\"1\",\"DRUG_LO_NAME\":\"谷胱甘肽片\",\"DRUG_LO_ID\":\"264\",\"ADMINISTRATION\":\"口服\",\"DOSAGE\":\"0.2\",\"DOSAGE_UNIT\":\"g\",\"FREQ_COUNT\":\"tid\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20190303\",\"END_DAY\":\"20211126\",\"DEPT_CODE\":\"5\",\"DOCTOR_NAME\":\"林超\",\"ORDER_NO\":\"14434429\",\"ORDER_SUB_NO\":\"1\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"住院医师\",\"GROUP_ID\":\"14434429\",\"USER_ID\":\"LC01\",\"PRES_ID\":\"105650_10009\",\"PRES_DATE\":\"20190303\",\"PRES_SEQ_ID\":\"105650_10009\",\"PK_ORDER_NO\":\"14434429\",\"COURSE\":\"\",\"PKG_COUNT\":\"3\",\"PKG_UNIT\":\"盒\",\"BAK_01\":\"\",\"BAK_02\":\"是\",\"BAK_03\":\"片剂\",\"BAK_04\":\"0.1g*36\",\"BAK_05\":\"\",\"HIGHEST_WARNING_LEVEL\":0,\"PERFORM_SCHEDULE\":\"\",\"ANTI_DRUG_REGISTER\":\"\"},{\"checkInfoList\":[],\"REPEAT\":\"1\",\"DRUG_LO_NAME\":\"0.9%氯化钠注射液（250ml）\",\"DRUG_LO_ID\":\"613\",\"ADMINISTRATION\":\"漱口用\",\"DOSAGE\":\"250\",\"DOSAGE_UNIT\":\"ml\",\"FREQ_COUNT\":\"qd\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20190305\",\"END_DAY\":\"20211128\",\"DEPT_CODE\":\"5\",\"DOCTOR_NAME\":\"林超\",\"ORDER_NO\":\"14437751\",\"ORDER_SUB_NO\":\"1\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"住院医师\",\"GROUP_ID\":\"14437751\",\"USER_ID\":\"LC01\",\"PRES_ID\":\"105650_10009\",\"PRES_DATE\":\"20190305\",\"PRES_SEQ_ID\":\"105650_10009\",\"PK_ORDER_NO\":\"14437751\",\"COURSE\":\"\",\"PKG_COUNT\":\"1\",\"PKG_UNIT\":\"袋\",\"BAK_01\":\"\",\"BAK_02\":\"是\",\"BAK_03\":\"注射液\",\"BAK_04\":\"0.9%*250ml*1\",\"BAK_05\":\"\",\"HIGHEST_WARNING_LEVEL\":0,\"PERFORM_SCHEDULE\":\"\",\"ANTI_DRUG_REGISTER\":\"\"},{\"checkInfoList\":[],\"REPEAT\":\"1\",\"DRUG_LO_NAME\":\"亚叶酸钙注射液\",\"DRUG_LO_ID\":\"1970\",\"ADMINISTRATION\":\"漱口用\",\"DOSAGE\":\"0.1\",\"DOSAGE_UNIT\":\"g\",\"FREQ_COUNT\":\"qd\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20190305\",\"END_DAY\":\"20211128\",\"DEPT_CODE\":\"5\",\"DOCTOR_NAME\":\"林超\",\"ORDER_NO\":\"14437751\",\"ORDER_SUB_NO\":\"2\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"住院医师\",\"GROUP_ID\":\"14437751\",\"USER_ID\":\"LC01\",\"PRES_ID\":\"105650_10009\",\"PRES_DATE\":\"20190305\",\"PRES_SEQ_ID\":\"105650_10009\",\"PK_ORDER_NO\":\"14437751\",\"COURSE\":\"\",\"PKG_COUNT\":\"1\",\"PKG_UNIT\":\"盒\",\"BAK_01\":\"\",\"BAK_02\":\"是\",\"BAK_03\":\"注射液\",\"BAK_04\":\"10ml:0.1g*2\",\"BAK_05\":\"\",\"HIGHEST_WARNING_LEVEL\":0,\"PERFORM_SCHEDULE\":\"\",\"ANTI_DRUG_REGISTER\":\"\"},{\"checkInfoList\":[],\"REPEAT\":\"1\",\"DRUG_LO_NAME\":\"维生素B12注射液\",\"DRUG_LO_ID\":\"427\",\"ADMINISTRATION\":\"漱口用\",\"DOSAGE\":\"0.5\",\"DOSAGE_UNIT\":\"mg\",\"FREQ_COUNT\":\"qd\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20190305\",\"END_DAY\":\"20211128\",\"DEPT_CODE\":\"5\",\"DOCTOR_NAME\":\"林超\",\"ORDER_NO\":\"14437751\",\"ORDER_SUB_NO\":\"3\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"住院医师\",\"GROUP_ID\":\"14437751\",\"USER_ID\":\"LC01\",\"PRES_ID\":\"105650_10009\",\"PRES_DATE\":\"20190305\",\"PRES_SEQ_ID\":\"105650_10009\",\"PK_ORDER_NO\":\"14437751\",\"COURSE\":\"\",\"PKG_COUNT\":\"1\",\"PKG_UNIT\":\"盒\",\"BAK_01\":\"\",\"BAK_02\":\"是\",\"BAK_03\":\"注射液\",\"BAK_04\":\"1ml:0.5mg*10\",\"BAK_05\":\"\",\"HIGHEST_WARNING_LEVEL\":0,\"PERFORM_SCHEDULE\":\"\",\"ANTI_DRUG_REGISTER\":\"\"},{\"checkInfoList\":[],\"REPEAT\":\"0\",\"DRUG_LO_NAME\":\"5%葡萄糖注射液（500ml）\",\"DRUG_LO_ID\":\"616\",\"ADMINISTRATION\":\"静脉滴注\",\"DOSAGE\":\"1100\",\"DOSAGE_UNIT\":\"ml\",\"FREQ_COUNT\":\"qd\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20190306\",\"END_DAY\":\"20190306\",\"DEPT_CODE\":\"5\",\"DOCTOR_NAME\":\"林超\",\"ORDER_NO\":\"14438263\",\"ORDER_SUB_NO\":\"1\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"住院医师\",\"GROUP_ID\":\"14438263\",\"USER_ID\":\"LC01\",\"PRES_ID\":\"105650_10009\",\"PRES_DATE\":\"20190306\",\"PRES_SEQ_ID\":\"105650_10009\",\"PK_ORDER_NO\":\"14438263\",\"COURSE\":\"0\",\"PKG_COUNT\":\"1\",\"PKG_UNIT\":\"袋\",\"BAK_01\":\"\",\"BAK_02\":\"否\",\"BAK_03\":\"注射液\",\"BAK_04\":\"5%*500ml*1\",\"BAK_05\":\"\",\"HIGHEST_WARNING_LEVEL\":0,\"PERFORM_SCHEDULE\":\"\",\"ANTI_DRUG_REGISTER\":\"\"},{\"checkInfoList\":[],\"REPEAT\":\"0\",\"DRUG_LO_NAME\":\"0.9%氯化钠注射液（500ml）\",\"DRUG_LO_ID\":\"611\",\"ADMINISTRATION\":\"静脉滴注\",\"DOSAGE\":\"500\",\"DOSAGE_UNIT\":\"ml\",\"FREQ_COUNT\":\"qd\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20190306\",\"END_DAY\":\"20190306\",\"DEPT_CODE\":\"5\",\"DOCTOR_NAME\":\"林超\",\"ORDER_NO\":\"14438263\",\"ORDER_SUB_NO\":\"3\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"住院医师\",\"GROUP_ID\":\"14438263\",\"USER_ID\":\"LC01\",\"PRES_ID\":\"105650_10009\",\"PRES_DATE\":\"20190306\",\"PRES_SEQ_ID\":\"105650_10009\",\"PK_ORDER_NO\":\"14438263\",\"COURSE\":\"0\",\"PKG_COUNT\":\"1\",\"PKG_UNIT\":\"袋\",\"BAK_01\":\"\",\"BAK_02\":\"否\",\"BAK_03\":\"注射液\",\"BAK_04\":\"0.9%*500ml*1\",\"BAK_05\":\"\",\"HIGHEST_WARNING_LEVEL\":0,\"PERFORM_SCHEDULE\":\"\",\"ANTI_DRUG_REGISTER\":\"\"},{\"checkInfoList\":[],\"REPEAT\":\"0\",\"DRUG_LO_NAME\":\"10%氯化钾注射液\",\"DRUG_LO_ID\":\"4395\",\"ADMINISTRATION\":\"静脉滴注\",\"DOSAGE\":\"3\",\"DOSAGE_UNIT\":\"g\",\"FREQ_COUNT\":\"qd\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20190306\",\"END_DAY\":\"20190306\",\"DEPT_CODE\":\"5\",\"DOCTOR_NAME\":\"林超\",\"ORDER_NO\":\"14438263\",\"ORDER_SUB_NO\":\"4\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"住院医师\",\"GROUP_ID\":\"14438263\",\"USER_ID\":\"LC01\",\"PRES_ID\":\"105650_10009\",\"PRES_DATE\":\"20190306\",\"PRES_SEQ_ID\":\"105650_10009\",\"PK_ORDER_NO\":\"14438263\",\"COURSE\":\"0\",\"PKG_COUNT\":\"1\",\"PKG_UNIT\":\"盒\",\"BAK_01\":\"\",\"BAK_02\":\"否\",\"BAK_03\":\"注射液\",\"BAK_04\":\"10ml:1g*50\",\"BAK_05\":\"\",\"HIGHEST_WARNING_LEVEL\":0,\"PERFORM_SCHEDULE\":\"\",\"ANTI_DRUG_REGISTER\":\"\"},{\"checkInfoList\":[],\"REPEAT\":\"0\",\"DRUG_LO_NAME\":\"5%葡萄糖注射液（250ml）\",\"DRUG_LO_ID\":\"619\",\"ADMINISTRATION\":\"静脉滴注\",\"DOSAGE\":\"125\",\"DOSAGE_UNIT\":\"ml\",\"FREQ_COUNT\":\"qd\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20190306\",\"END_DAY\":\"20190306\",\"DEPT_CODE\":\"5\",\"DOCTOR_NAME\":\"林超\",\"ORDER_NO\":\"14438264\",\"ORDER_SUB_NO\":\"1\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"住院医师\",\"GROUP_ID\":\"14438264\",\"USER_ID\":\"LC01\",\"PRES_ID\":\"105650_10009\",\"PRES_DATE\":\"20190306\",\"PRES_SEQ_ID\":\"105650_10009\",\"PK_ORDER_NO\":\"14438264\",\"COURSE\":\"0\",\"PKG_COUNT\":\"1\",\"PKG_UNIT\":\"袋\",\"BAK_01\":\"\",\"BAK_02\":\"否\",\"BAK_03\":\"注射液\",\"BAK_04\":\"5%*250ml*1\",\"BAK_05\":\"\",\"HIGHEST_WARNING_LEVEL\":0,\"PERFORM_SCHEDULE\":\"\",\"ANTI_DRUG_REGISTER\":\"\"},{\"checkInfoList\":[],\"REPEAT\":\"0\",\"DRUG_LO_NAME\":\"0.9%氯化钠注射液（10ml）\",\"DRUG_LO_ID\":\"4398\",\"ADMINISTRATION\":\"静脉注射\",\"DOSAGE\":\"10\",\"DOSAGE_UNIT\":\"ml\",\"FREQ_COUNT\":\"qd\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20190306\",\"END_DAY\":\"20190306\",\"DEPT_CODE\":\"5\",\"DOCTOR_NAME\":\"林超\",\"ORDER_NO\":\"14438265\",\"ORDER_SUB_NO\":\"1\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"住院医师\",\"GROUP_ID\":\"14438265\",\"USER_ID\":\"LC01\",\"PRES_ID\":\"105650_10009\",\"PRES_DATE\":\"20190306\",\"PRES_SEQ_ID\":\"105650_10009\",\"PK_ORDER_NO\":\"14438265\",\"COURSE\":\"0\",\"PKG_COUNT\":\"1\",\"PKG_UNIT\":\"盒\",\"BAK_01\":\"\",\"BAK_02\":\"否\",\"BAK_03\":\"注射液\",\"BAK_04\":\"0.9%*10ml*40\",\"BAK_05\":\"\",\"HIGHEST_WARNING_LEVEL\":0,\"PERFORM_SCHEDULE\":\"\",\"ANTI_DRUG_REGISTER\":\"\"},{\"checkInfoList\":[],\"REPEAT\":\"0\",\"DRUG_LO_NAME\":\"5%葡萄糖注射液（250ml）\",\"DRUG_LO_ID\":\"619\",\"ADMINISTRATION\":\"静脉滴注\",\"DOSAGE\":\"125\",\"DOSAGE_UNIT\":\"ml\",\"FREQ_COUNT\":\"qd\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20190306\",\"END_DAY\":\"20190306\",\"DEPT_CODE\":\"5\",\"DOCTOR_NAME\":\"林超\",\"ORDER_NO\":\"14438266\",\"ORDER_SUB_NO\":\"1\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"住院医师\",\"GROUP_ID\":\"14438266\",\"USER_ID\":\"LC01\",\"PRES_ID\":\"105650_10009\",\"PRES_DATE\":\"20190306\",\"PRES_SEQ_ID\":\"105650_10009\",\"PK_ORDER_NO\":\"14438266\",\"COURSE\":\"0\",\"PKG_COUNT\":\"1\",\"PKG_UNIT\":\"袋\",\"BAK_01\":\"\",\"BAK_02\":\"否\",\"BAK_03\":\"注射液\",\"BAK_04\":\"5%*250ml*1\",\"BAK_05\":\"\",\"HIGHEST_WARNING_LEVEL\":0,\"PERFORM_SCHEDULE\":\"\",\"ANTI_DRUG_REGISTER\":\"\"},{\"checkInfoList\":[],\"REPEAT\":\"0\",\"DRUG_LO_NAME\":\"0.9%氯化钠注射液（10ml）\",\"DRUG_LO_ID\":\"4398\",\"ADMINISTRATION\":\"静脉注射\",\"DOSAGE\":\"10\",\"DOSAGE_UNIT\":\"ml\",\"FREQ_COUNT\":\"qd\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20190306\",\"END_DAY\":\"20190306\",\"DEPT_CODE\":\"5\",\"DOCTOR_NAME\":\"林超\",\"ORDER_NO\":\"14438267\",\"ORDER_SUB_NO\":\"1\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"住院医师\",\"GROUP_ID\":\"14438267\",\"USER_ID\":\"LC01\",\"PRES_ID\":\"105650_10009\",\"PRES_DATE\":\"20190306\",\"PRES_SEQ_ID\":\"105650_10009\",\"PK_ORDER_NO\":\"14438267\",\"COURSE\":\"0\",\"PKG_COUNT\":\"1\",\"PKG_UNIT\":\"盒\",\"BAK_01\":\"\",\"BAK_02\":\"否\",\"BAK_03\":\"注射液\",\"BAK_04\":\"0.9%*10ml*40\",\"BAK_05\":\"\",\"HIGHEST_WARNING_LEVEL\":0,\"PERFORM_SCHEDULE\":\"\",\"ANTI_DRUG_REGISTER\":\"\"},{\"checkInfoList\":[],\"REPEAT\":\"0\",\"DRUG_LO_NAME\":\"5%葡萄糖注射液（50ml）\",\"DRUG_LO_ID\":\"626\",\"ADMINISTRATION\":\"静脉滴注\",\"DOSAGE\":\"50\",\"DOSAGE_UNIT\":\"ml\",\"FREQ_COUNT\":\"qd\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20190306\",\"END_DAY\":\"20190306\",\"DEPT_CODE\":\"5\",\"DOCTOR_NAME\":\"林超\",\"ORDER_NO\":\"14438286\",\"ORDER_SUB_NO\":\"1\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"住院医师\",\"GROUP_ID\":\"14438286\",\"USER_ID\":\"LC01\",\"PRES_ID\":\"105650_10009\",\"PRES_DATE\":\"20190306\",\"PRES_SEQ_ID\":\"105650_10009\",\"PK_ORDER_NO\":\"14438286\",\"COURSE\":\"0\",\"PKG_COUNT\":\"1\",\"PKG_UNIT\":\"瓶\",\"BAK_01\":\"\",\"BAK_02\":\"否\",\"BAK_03\":\"注射液\",\"BAK_04\":\"5%*50ml*1\",\"BAK_05\":\"\",\"HIGHEST_WARNING_LEVEL\":0,\"PERFORM_SCHEDULE\":\"\",\"ANTI_DRUG_REGISTER\":\"\"},{\"checkInfoList\":[],\"REPEAT\":\"0\",\"DRUG_LO_NAME\":\"注射用甲泼尼龙琥珀酸钠\",\"DRUG_LO_ID\":\"595\",\"ADMINISTRATION\":\"静脉滴注\",\"DOSAGE\":\"20\",\"DOSAGE_UNIT\":\"mg\",\"FREQ_COUNT\":\"qd\",\"FREQ_INTERVAL\":\"\",\"FREQ_INTERVAL_UNIT\":\"\",\"START_DAY\":\"20190306\",\"END_DAY\":\"20190306\",\"DEPT_CODE\":\"5\",\"DOCTOR_NAME\":\"林超\",\"ORDER_NO\":\"14438286\",\"ORDER_SUB_NO\":\"2\",\"AUTHORITY_LEVELS\":\"\",\"ALERT_LEVELS\":\"\",\"TITLE\":\"住院医师\",\"GROUP_ID\":\"14438286\",\"USER_ID\":\"LC01\",\"PRES_ID\":\"105650_10009\",\"PRES_DATE\":\"20190306\",\"PRES_SEQ_ID\":\"105650_10009\",\"PK_ORDER_NO\":\"14438286\",\"COURSE\":\"0\",\"PKG_COUNT\":\"1\",\"PKG_UNIT\":\"支\",\"BAK_01\":\"\",\"BAK_02\":\"否\",\"BAK_03\":\"注射用无菌粉末\",\"BAK_04\":\"40mg*1\",\"BAK_05\":\"\",\"HIGHEST_WARNING_LEVEL\":0,\"PERFORM_SCHEDULE\":\"\",\"ANTI_DRUG_REGISTER\":\"\"}],\"checkInfoMap\":{\"studio.atur.api.persistence.presc_stat.PrescInfoKey@70bffe3c\":[{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和呋塞米注射液(药品码1876，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  碳酸氢钠与利尿药合用，有促使出现低氯化物碱中毒的危险性。\\r\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"国家基本药物(西药).第二版.国家药品监督管理局.北京:人民卫生出版社,2002.756,国家药典委员会.中华人民共和国药典(二部)临床用药须知.2000年版.北京:化学工业出版社,2001.P366-368,龙胆碳酸氢钠片说明书,依他尼酸片说明书(再评价说明书)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和呋塞米注射液(药品码1876，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  碳酸氢钠与利尿药合用，有促使出现低氯化物碱中毒的危险性。\\r\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"国家基本药物(西药).第二版.国家药品监督管理局.北京:人民卫生出版社,2002.756,国家药典委员会.中华人民共和国药典(二部)临床用药须知.2000年版.北京:化学工业出版社,2001.P366-368,龙胆碳酸氢钠片说明书,依他尼酸片说明书(再评价说明书)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和维生素B6注射液(药品码1872，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n碱性药物可破坏维生素B类，降低后者的药效。\\r\\n【处理】\\r\\n合用需谨慎。\\r\\n\\n\",\"REF_SOURCE\":\"干酵母片说明书,维生素B1片说明书\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和呋塞米注射液(药品码1876，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  碳酸氢钠与利尿药合用，有促使出现低氯化物碱中毒的危险性。\\r\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"国家基本药物(西药).第二版.国家药品监督管理局.北京:人民卫生出版社,2002.756,国家药典委员会.中华人民共和国药典(二部)临床用药须知.2000年版.北京:化学工业出版社,2001.P366-368,龙胆碳酸氢钠片说明书,依他尼酸片说明书(再评价说明书)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和呋塞米注射液(药品码1876，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  碳酸氢钠与利尿药合用，有促使出现低氯化物碱中毒的危险性。\\r\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"国家基本药物(西药).第二版.国家药品监督管理局.北京:人民卫生出版社,2002.756,国家药典委员会.中华人民共和国药典(二部)临床用药须知.2000年版.北京:化学工业出版社,2001.P366-368,龙胆碳酸氢钠片说明书,依他尼酸片说明书(再评价说明书)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和注射用甲泼尼龙琥珀酸钠(药品码595，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  碳酸氢钠与肾上腺皮质激素(尤其是具有较强的盐皮质激素作用者)合用时，易致高钠血症和水肿。\\r\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"碳酸氢钠注射液说明书(湖北广济药业股份有限公司)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1}],\"studio.atur.api.persistence.presc_stat.PrescInfoKey@ee498c51\":[],\"studio.atur.api.persistence.presc_stat.PrescInfoKey@b8f3f1c8\":[],\"studio.atur.api.persistence.presc_stat.PrescInfoKey@179c8901\":[{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和碳酸氢钠注射液(药品码621，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n碱性药物可破坏维生素B类，降低后者的药效。\\r\\n【处理】\\r\\n合用需谨慎。\\r\\n\\n\",\"REF_SOURCE\":\"干酵母片说明书,维生素B1片说明书\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和碳酸氢钠注射液(药品码621，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n碱性药物可破坏维生素B类，降低后者的药效。\\r\\n【处理】\\r\\n合用需谨慎。\\r\\n\\n\",\"REF_SOURCE\":\"干酵母片说明书,维生素B1片说明书\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和碳酸氢钠注射液(药品码621，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n碱性药物可破坏维生素B类，降低后者的药效。\\r\\n【处理】\\r\\n合用需谨慎。\\r\\n\\n\",\"REF_SOURCE\":\"干酵母片说明书,维生素B1片说明书\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和碳酸氢钠注射液(药品码621，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n碱性药物可破坏维生素B类，降低后者的药效。\\r\\n【处理】\\r\\n合用需谨慎。\\r\\n\\n\",\"REF_SOURCE\":\"干酵母片说明书,维生素B1片说明书\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和注射用甲泼尼龙琥珀酸钠(药品码595，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  维生素B6与免疫抑制药(青霉胺、环磷酰胺、肾上腺皮质激素、异烟肼等)合用，可能引起贫血或周围神经炎。其中，维生素B6与环磷酰胺合用，同时还可减轻环磷酰胺所引起的肝脏、胃肠道不良反应。\\r\\n【机制】\\r\\n  免疫抑制药对维生素B6有拮抗作用或可增加维生素B6肾排泄率\\n\\n【处理】\\r\\n  长期服用上述免疫抑制药者，维生素B6需要量增加，可一日加服25mg维生素B6。\\r\\n\",\"REF_SOURCE\":\"贾公孚,谢惠民.临床药物新用联用大全.第一版.北京:人民卫生出版社,1999.352-354,436-437,维生素B6片说明书,中华人民共和国药典.2000年版.二部.临床用药须知.国家药典委员会编.北京:化学工业出版社,2001.894-895\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1}],\"studio.atur.api.persistence.presc_stat.PrescInfoKey@6d142ef8\":[],\"studio.atur.api.persistence.presc_stat.PrescInfoKey@4b537bba\":[],\"studio.atur.api.persistence.presc_stat.PrescInfoKey@4ca0e961\":[],\"studio.atur.api.persistence.presc_stat.PrescInfoKey@fd3ad121\":[],\"studio.atur.api.persistence.presc_stat.PrescInfoKey@b6bc4254\":[],\"studio.atur.api.persistence.presc_stat.PrescInfoKey@4ca000a3\":[],\"studio.atur.api.persistence.presc_stat.PrescInfoKey@b41c522c\":[],\"studio.atur.api.persistence.presc_stat.PrescInfoKey@ca97d126\":[{\"COLOR\":\"黄色\",\"NAME\":\"适应症\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"超适应症用药。药品说明书适应症为：水肿性疾病、充血性心力衰竭、肝硬化、肾脏疾病、肾炎、肾病、急性肾功能衰竭、慢性肾功能衰竭、急性肺水肿、急性脑水肿、高血压、急性肾功能衰竭、肾脏血流灌注不足、失水、休克、中毒、麻醉意外、循环功能不全、急性肾小管坏死、高钾血症、高钙血症、稀释性低钠血症、抗利尿激素分泌过多症、SIADH、急性药物毒物中毒、巴比妥类药物中毒、。医生诊断为：急性淋巴细胞白血病、急性淋巴细胞白血病复发（B、IR、TEL/AML+、巩固治疗1）、自身免疫性溶血性贫血、口腔溃疡、葡萄糖-6-磷酸酶缺乏、左侧豆状核出血（陈旧性）、松果体囊肿可能、副鼻窦炎、腺样体肥大\",\"REF_SOURCE\":\"药品说明书及医院相关规定\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和注射用甲泼尼龙琥珀酸钠(药品码595，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  肾上腺皮质激素可致水钠潴留，降低排钾利尿药(袢利尿药、噻嗪类利尿药、碳酸酐酶抑制药、吲达帕胺等)的利尿作用，升高电解质紊乱的风险，尤其是低钾血症。\\r\\n【处理】\\r\\n   对使用排钾利尿药的患者，当开始、停用或改变皮质激素类药治疗时，应严密监测血钾及心脏功能。必要时予以纠正低钾血症，还应注意患者是否正在使用洋地黄糖苷类药。\\r\\n\",\"REF_SOURCE\":\"比索洛尔氢氯噻嗪片说明书(德国默克公司(Merck KGaA)),陈新谦,金有豫,汤光.新编药物学.第15版.北京:人民卫生出版社,2003.487-488,醋酸曲安奈德注射液说明书(上海旭东海普药业有限公司),贾公孚,谢惠民.临床药物新用联用大全.第一版.北京:人民卫生出版社,1999.294-297,罗明生,高天惠等.现代临床药物大典.第一版.成都:四川科学技术出版社,2001.777,依他尼酸片说明书(再评价品说明书),乙酰唑胺片说明书,吲达帕胺片说明书(北京市燕京制药厂)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1}],\"studio.atur.api.persistence.presc_stat.PrescInfoKey@3809ea4c\":[],\"studio.atur.api.persistence.presc_stat.PrescInfoKey@a75395e5\":[],\"studio.atur.api.persistence.presc_stat.PrescInfoKey@7c9897bb\":[],\"studio.atur.api.persistence.presc_stat.PrescInfoKey@d07c5dc6\":[],\"studio.atur.api.persistence.presc_stat.PrescInfoKey@b41d3aea\":[],\"studio.atur.api.persistence.presc_stat.PrescInfoKey@aa0085f\":[],\"studio.atur.api.persistence.presc_stat.PrescInfoKey@635b440\":[{\"COLOR\":\"黄色\",\"NAME\":\"适应症\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"超适应症用药。药品说明书适应症为：水肿性疾病、充血性心力衰竭、肝硬化、肾脏疾病、肾炎、肾病、急性肾功能衰竭、慢性肾功能衰竭、急性肺水肿、急性脑水肿、高血压、急性肾功能衰竭、肾脏血流灌注不足、失水、休克、中毒、麻醉意外、循环功能不全、急性肾小管坏死、高钾血症、高钙血症、稀释性低钠血症、抗利尿激素分泌过多症、SIADH、急性药物毒物中毒、巴比妥类药物中毒、。医生诊断为：急性淋巴细胞白血病、急性淋巴细胞白血病复发（B、IR、TEL/AML+、巩固治疗1）、自身免疫性溶血性贫血、口腔溃疡、葡萄糖-6-磷酸酶缺乏、左侧豆状核出血（陈旧性）、松果体囊肿可能、副鼻窦炎、腺样体肥大\",\"REF_SOURCE\":\"药品说明书及医院相关规定\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和注射用头孢哌酮钠舒巴坦钠(药品码2003，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  头孢类抗生素与强利尿药合用可加重肾毒性。托拉塞米在高剂量时也可能加重头孢类抗生素的耳毒性与肾毒性。\\r\\n\\n\\n【机制】\\r\\n  可能是利尿药脱水作用引起血药浓度升高所致。此外，头孢噻啶与呋塞米合用，可使肾小球滤过率和肾小管分泌竞争降低，从而增加头孢噻啶的肾毒性。\\r\\n\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"李大魁,孙忠实,王功立.常用处方药使用指南.第一版.北京:中国协和医科大学出版社,2000.13-16,李大魁等.常用处方药使用指南.第一版.中国协和医科大学出版社,2000.16-18,头孢克洛咀嚼片说明书(迪沙药业集团有限公司),托拉塞米注射液说明书(深圳信立泰药业有限公司),盐酸头孢他美酯干混悬剂说明书(浙江震元制药有限公司),中华人民共和国药典.2000年版.二部.临床用药须知.国家药典委员会编.北京:化学工业出版社,2001.639-640,周德尚.新编进口药品手册.南昌:江西科学技术出版社,65-67,注射用头孢他啶说明书(国药集团),注射用头孢替唑钠说明书(天津新丰制药有限公司)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和碳酸氢钠注射液(药品码621，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  碳酸氢钠与利尿药合用，有促使出现低氯化物碱中毒的危险性。\\r\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"国家基本药物(西药).第二版.国家药品监督管理局.北京:人民卫生出版社,2002.756,国家药典委员会.中华人民共和国药典(二部)临床用药须知.2000年版.北京:化学工业出版社,2001.P366-368,龙胆碳酸氢钠片说明书,依他尼酸片说明书(再评价说明书)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和碳酸氢钠注射液(药品码621，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  碳酸氢钠与利尿药合用，有促使出现低氯化物碱中毒的危险性。\\r\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"国家基本药物(西药).第二版.国家药品监督管理局.北京:人民卫生出版社,2002.756,国家药典委员会.中华人民共和国药典(二部)临床用药须知.2000年版.北京:化学工业出版社,2001.P366-368,龙胆碳酸氢钠片说明书,依他尼酸片说明书(再评价说明书)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和注射用甲泼尼龙琥珀酸钠(药品码595，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  肾上腺皮质激素可致水钠潴留，降低排钾利尿药(袢利尿药、噻嗪类利尿药、碳酸酐酶抑制药、吲达帕胺等)的利尿作用，升高电解质紊乱的风险，尤其是低钾血症。\\r\\n【处理】\\r\\n   对使用排钾利尿药的患者，当开始、停用或改变皮质激素类药治疗时，应严密监测血钾及心脏功能。必要时予以纠正低钾血症，还应注意患者是否正在使用洋地黄糖苷类药。\\r\\n\",\"REF_SOURCE\":\"比索洛尔氢氯噻嗪片说明书(德国默克公司(Merck KGaA)),陈新谦,金有豫,汤光.新编药物学.第15版.北京:人民卫生出版社,2003.487-488,醋酸曲安奈德注射液说明书(上海旭东海普药业有限公司),贾公孚,谢惠民.临床药物新用联用大全.第一版.北京:人民卫生出版社,1999.294-297,罗明生,高天惠等.现代临床药物大典.第一版.成都:四川科学技术出版社,2001.777,依他尼酸片说明书(再评价品说明书),乙酰唑胺片说明书,吲达帕胺片说明书(北京市燕京制药厂)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1}],\"studio.atur.api.persistence.presc_stat.PrescInfoKey@3693acd5\":[{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和呋塞米注射液(药品码1876，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  碳酸氢钠与利尿药合用，有促使出现低氯化物碱中毒的危险性。\\r\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"国家基本药物(西药).第二版.国家药品监督管理局.北京:人民卫生出版社,2002.756,国家药典委员会.中华人民共和国药典(二部)临床用药须知.2000年版.北京:化学工业出版社,2001.P366-368,龙胆碳酸氢钠片说明书,依他尼酸片说明书(再评价说明书)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和注射用甲泼尼龙琥珀酸钠(药品码595，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  碳酸氢钠与肾上腺皮质激素(尤其是具有较强的盐皮质激素作用者)合用时，易致高钠血症和水肿。\\r\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"碳酸氢钠注射液说明书(湖北广济药业股份有限公司)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1}],\"studio.atur.api.persistence.presc_stat.PrescInfoKey@42730bec\":[],\"studio.atur.api.persistence.presc_stat.PrescInfoKey@d36b6fc9\":[{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和呋塞米注射液(药品码1876，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  头孢类抗生素与强利尿药合用可加重肾毒性。托拉塞米在高剂量时也可能加重头孢类抗生素的耳毒性与肾毒性。\\r\\n\\n\\n【机制】\\r\\n  可能是利尿药脱水作用引起血药浓度升高所致。此外，头孢噻啶与呋塞米合用，可使肾小球滤过率和肾小管分泌竞争降低，从而增加头孢噻啶的肾毒性。\\r\\n\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"李大魁,孙忠实,王功立.常用处方药使用指南.第一版.北京:中国协和医科大学出版社,2000.13-16,李大魁等.常用处方药使用指南.第一版.中国协和医科大学出版社,2000.16-18,头孢克洛咀嚼片说明书(迪沙药业集团有限公司),托拉塞米注射液说明书(深圳信立泰药业有限公司),盐酸头孢他美酯干混悬剂说明书(浙江震元制药有限公司),中华人民共和国药典.2000年版.二部.临床用药须知.国家药典委员会编.北京:化学工业出版社,2001.639-640,周德尚.新编进口药品手册.南昌:江西科学技术出版社,65-67,注射用头孢他啶说明书(国药集团),注射用头孢替唑钠说明书(天津新丰制药有限公司)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和呋塞米注射液(药品码1876，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  头孢类抗生素与强利尿药合用可加重肾毒性。托拉塞米在高剂量时也可能加重头孢类抗生素的耳毒性与肾毒性。\\r\\n\\n\\n【机制】\\r\\n  可能是利尿药脱水作用引起血药浓度升高所致。此外，头孢噻啶与呋塞米合用，可使肾小球滤过率和肾小管分泌竞争降低，从而增加头孢噻啶的肾毒性。\\r\\n\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"李大魁,孙忠实,王功立.常用处方药使用指南.第一版.北京:中国协和医科大学出版社,2000.13-16,李大魁等.常用处方药使用指南.第一版.中国协和医科大学出版社,2000.16-18,头孢克洛咀嚼片说明书(迪沙药业集团有限公司),托拉塞米注射液说明书(深圳信立泰药业有限公司),盐酸头孢他美酯干混悬剂说明书(浙江震元制药有限公司),中华人民共和国药典.2000年版.二部.临床用药须知.国家药典委员会编.北京:化学工业出版社,2001.639-640,周德尚.新编进口药品手册.南昌:江西科学技术出版社,65-67,注射用头孢他啶说明书(国药集团),注射用头孢替唑钠说明书(天津新丰制药有限公司)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1}],\"studio.atur.api.persistence.presc_stat.PrescInfoKey@354f436d\":[],\"studio.atur.api.persistence.presc_stat.PrescInfoKey@3692c417\":[{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和呋塞米注射液(药品码1876，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  碳酸氢钠与利尿药合用，有促使出现低氯化物碱中毒的危险性。\\r\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"国家基本药物(西药).第二版.国家药品监督管理局.北京:人民卫生出版社,2002.756,国家药典委员会.中华人民共和国药典(二部)临床用药须知.2000年版.北京:化学工业出版社,2001.P366-368,龙胆碳酸氢钠片说明书,依他尼酸片说明书(再评价说明书)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和呋塞米注射液(药品码1876，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  碳酸氢钠与利尿药合用，有促使出现低氯化物碱中毒的危险性。\\r\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"国家基本药物(西药).第二版.国家药品监督管理局.北京:人民卫生出版社,2002.756,国家药典委员会.中华人民共和国药典(二部)临床用药须知.2000年版.北京:化学工业出版社,2001.P366-368,龙胆碳酸氢钠片说明书,依他尼酸片说明书(再评价说明书)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和注射用甲泼尼龙琥珀酸钠(药品码595，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  碳酸氢钠与肾上腺皮质激素(尤其是具有较强的盐皮质激素作用者)合用时，易致高钠血症和水肿。\\r\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"碳酸氢钠注射液说明书(湖北广济药业股份有限公司)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1}],\"studio.atur.api.persistence.presc_stat.PrescInfoKey@883ecc7b\":[],\"studio.atur.api.persistence.presc_stat.PrescInfoKey@efbb1e04\":[],\"studio.atur.api.persistence.presc_stat.PrescInfoKey@86cd3ac8\":[],\"studio.atur.api.persistence.presc_stat.PrescInfoKey@ca96e868\":[{\"COLOR\":\"黄色\",\"NAME\":\"适应症\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"超适应症用药。药品说明书适应症为：水肿性疾病、充血性心力衰竭、肝硬化、肾脏疾病、肾炎、肾病、急性肾功能衰竭、慢性肾功能衰竭、急性肺水肿、急性脑水肿、高血压、急性肾功能衰竭、肾脏血流灌注不足、失水、休克、中毒、麻醉意外、循环功能不全、急性肾小管坏死、高钾血症、高钙血症、稀释性低钠血症、抗利尿激素分泌过多症、SIADH、急性药物毒物中毒、巴比妥类药物中毒、。医生诊断为：急性淋巴细胞白血病、急性淋巴细胞白血病复发（B、IR、TEL/AML+、巩固治疗1）、自身免疫性溶血性贫血、口腔溃疡、葡萄糖-6-磷酸酶缺乏、左侧豆状核出血（陈旧性）、松果体囊肿可能、副鼻窦炎、腺样体肥大\",\"REF_SOURCE\":\"药品说明书及医院相关规定\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和碳酸氢钠注射液(药品码621，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  碳酸氢钠与利尿药合用，有促使出现低氯化物碱中毒的危险性。\\r\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"国家基本药物(西药).第二版.国家药品监督管理局.北京:人民卫生出版社,2002.756,国家药典委员会.中华人民共和国药典(二部)临床用药须知.2000年版.北京:化学工业出版社,2001.P366-368,龙胆碳酸氢钠片说明书,依他尼酸片说明书(再评价说明书)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和注射用甲泼尼龙琥珀酸钠(药品码595，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  肾上腺皮质激素可致水钠潴留，降低排钾利尿药(袢利尿药、噻嗪类利尿药、碳酸酐酶抑制药、吲达帕胺等)的利尿作用，升高电解质紊乱的风险，尤其是低钾血症。\\r\\n【处理】\\r\\n   对使用排钾利尿药的患者，当开始、停用或改变皮质激素类药治疗时，应严密监测血钾及心脏功能。必要时予以纠正低钾血症，还应注意患者是否正在使用洋地黄糖苷类药。\\r\\n\",\"REF_SOURCE\":\"比索洛尔氢氯噻嗪片说明书(德国默克公司(Merck KGaA)),陈新谦,金有豫,汤光.新编药物学.第15版.北京:人民卫生出版社,2003.487-488,醋酸曲安奈德注射液说明书(上海旭东海普药业有限公司),贾公孚,谢惠民.临床药物新用联用大全.第一版.北京:人民卫生出版社,1999.294-297,罗明生,高天惠等.现代临床药物大典.第一版.成都:四川科学技术出版社,2001.777,依他尼酸片说明书(再评价品说明书),乙酰唑胺片说明书,吲达帕胺片说明书(北京市燕京制药厂)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1}],\"studio.atur.api.persistence.presc_stat.PrescInfoKey@f317a022\":[],\"studio.atur.api.persistence.presc_stat.PrescInfoKey@4c4228d\":[{\"COLOR\":\"黄色\",\"NAME\":\"适应症\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"超适应症用药。药品说明书适应症为：水肿性疾病、充血性心力衰竭、肝硬化、肾脏疾病、肾炎、肾病、急性肾功能衰竭、慢性肾功能衰竭、急性肺水肿、急性脑水肿、高血压、急性肾功能衰竭、肾脏血流灌注不足、失水、休克、中毒、麻醉意外、循环功能不全、急性肾小管坏死、高钾血症、高钙血症、稀释性低钠血症、抗利尿激素分泌过多症、SIADH、急性药物毒物中毒、巴比妥类药物中毒、。医生诊断为：急性淋巴细胞白血病、急性淋巴细胞白血病复发（B、IR、TEL/AML+、巩固治疗1）、自身免疫性溶血性贫血、口腔溃疡、葡萄糖-6-磷酸酶缺乏、左侧豆状核出血（陈旧性）、松果体囊肿可能、副鼻窦炎、腺样体肥大\",\"REF_SOURCE\":\"药品说明书及医院相关规定\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和碳酸氢钠注射液(药品码621，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  碳酸氢钠与利尿药合用，有促使出现低氯化物碱中毒的危险性。\\r\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"国家基本药物(西药).第二版.国家药品监督管理局.北京:人民卫生出版社,2002.756,国家药典委员会.中华人民共和国药典(二部)临床用药须知.2000年版.北京:化学工业出版社,2001.P366-368,龙胆碳酸氢钠片说明书,依他尼酸片说明书(再评价说明书)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和注射用头孢哌酮钠舒巴坦钠(药品码2003，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  头孢类抗生素与强利尿药合用可加重肾毒性。托拉塞米在高剂量时也可能加重头孢类抗生素的耳毒性与肾毒性。\\r\\n\\n\\n【机制】\\r\\n  可能是利尿药脱水作用引起血药浓度升高所致。此外，头孢噻啶与呋塞米合用，可使肾小球滤过率和肾小管分泌竞争降低，从而增加头孢噻啶的肾毒性。\\r\\n\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"李大魁,孙忠实,王功立.常用处方药使用指南.第一版.北京:中国协和医科大学出版社,2000.13-16,李大魁等.常用处方药使用指南.第一版.中国协和医科大学出版社,2000.16-18,头孢克洛咀嚼片说明书(迪沙药业集团有限公司),托拉塞米注射液说明书(深圳信立泰药业有限公司),盐酸头孢他美酯干混悬剂说明书(浙江震元制药有限公司),中华人民共和国药典.2000年版.二部.临床用药须知.国家药典委员会编.北京:化学工业出版社,2001.639-640,周德尚.新编进口药品手册.南昌:江西科学技术出版社,65-67,注射用头孢他啶说明书(国药集团),注射用头孢替唑钠说明书(天津新丰制药有限公司)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和碳酸氢钠注射液(药品码621，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  碳酸氢钠与利尿药合用，有促使出现低氯化物碱中毒的危险性。\\r\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"国家基本药物(西药).第二版.国家药品监督管理局.北京:人民卫生出版社,2002.756,国家药典委员会.中华人民共和国药典(二部)临床用药须知.2000年版.北京:化学工业出版社,2001.P366-368,龙胆碳酸氢钠片说明书,依他尼酸片说明书(再评价说明书)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和碳酸氢钠注射液(药品码621，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  碳酸氢钠与利尿药合用，有促使出现低氯化物碱中毒的危险性。\\r\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"国家基本药物(西药).第二版.国家药品监督管理局.北京:人民卫生出版社,2002.756,国家药典委员会.中华人民共和国药典(二部)临床用药须知.2000年版.北京:化学工业出版社,2001.P366-368,龙胆碳酸氢钠片说明书,依他尼酸片说明书(再评价说明书)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和注射用甲泼尼龙琥珀酸钠(药品码595，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  肾上腺皮质激素可致水钠潴留，降低排钾利尿药(袢利尿药、噻嗪类利尿药、碳酸酐酶抑制药、吲达帕胺等)的利尿作用，升高电解质紊乱的风险，尤其是低钾血症。\\r\\n【处理】\\r\\n   对使用排钾利尿药的患者，当开始、停用或改变皮质激素类药治疗时，应严密监测血钾及心脏功能。必要时予以纠正低钾血症，还应注意患者是否正在使用洋地黄糖苷类药。\\r\\n\",\"REF_SOURCE\":\"比索洛尔氢氯噻嗪片说明书(德国默克公司(Merck KGaA)),陈新谦,金有豫,汤光.新编药物学.第15版.北京:人民卫生出版社,2003.487-488,醋酸曲安奈德注射液说明书(上海旭东海普药业有限公司),贾公孚,谢惠民.临床药物新用联用大全.第一版.北京:人民卫生出版社,1999.294-297,罗明生,高天惠等.现代临床药物大典.第一版.成都:四川科学技术出版社,2001.777,依他尼酸片说明书(再评价品说明书),乙酰唑胺片说明书,吲达帕胺片说明书(北京市燕京制药厂)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1}],\"studio.atur.api.persistence.presc_stat.PrescInfoKey@dd78d325\":[{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和碳酸氢钠注射液(药品码621，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n碱性药物可破坏维生素B类，降低后者的药效。\\r\\n【处理】\\r\\n合用需谨慎。\\r\\n\\n\",\"REF_SOURCE\":\"干酵母片说明书,维生素B1片说明书\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和碳酸氢钠注射液(药品码621，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n碱性药物可破坏维生素B类，降低后者的药效。\\r\\n【处理】\\r\\n合用需谨慎。\\r\\n\\n\",\"REF_SOURCE\":\"干酵母片说明书,维生素B1片说明书\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和注射用甲泼尼龙琥珀酸钠(药品码595，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  维生素B6与免疫抑制药(青霉胺、环磷酰胺、肾上腺皮质激素、异烟肼等)合用，可能引起贫血或周围神经炎。其中，维生素B6与环磷酰胺合用，同时还可减轻环磷酰胺所引起的肝脏、胃肠道不良反应。\\r\\n【机制】\\r\\n  免疫抑制药对维生素B6有拮抗作用或可增加维生素B6肾排泄率\\n\\n【处理】\\r\\n  长期服用上述免疫抑制药者，维生素B6需要量增加，可一日加服25mg维生素B6。\\r\\n\",\"REF_SOURCE\":\"贾公孚,谢惠民.临床药物新用联用大全.第一版.北京:人民卫生出版社,1999.352-354,436-437,维生素B6片说明书,中华人民共和国药典.2000年版.二部.临床用药须知.国家药典委员会编.北京:化学工业出版社,2001.894-895\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1}],\"studio.atur.api.persistence.presc_stat.PrescInfoKey@b64da1be\":[],\"studio.atur.api.persistence.presc_stat.PrescInfoKey@72318fef\":[{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和呋塞米注射液(药品码1876，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  碳酸氢钠与利尿药合用，有促使出现低氯化物碱中毒的危险性。\\r\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"国家基本药物(西药).第二版.国家药品监督管理局.北京:人民卫生出版社,2002.756,国家药典委员会.中华人民共和国药典(二部)临床用药须知.2000年版.北京:化学工业出版社,2001.P366-368,龙胆碳酸氢钠片说明书,依他尼酸片说明书(再评价说明书)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和维生素B6注射液(药品码1872，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n碱性药物可破坏维生素B类，降低后者的药效。\\r\\n【处理】\\r\\n合用需谨慎。\\r\\n\\n\",\"REF_SOURCE\":\"干酵母片说明书,维生素B1片说明书\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和呋塞米注射液(药品码1876，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  碳酸氢钠与利尿药合用，有促使出现低氯化物碱中毒的危险性。\\r\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"国家基本药物(西药).第二版.国家药品监督管理局.北京:人民卫生出版社,2002.756,国家药典委员会.中华人民共和国药典(二部)临床用药须知.2000年版.北京:化学工业出版社,2001.P366-368,龙胆碳酸氢钠片说明书,依他尼酸片说明书(再评价说明书)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和呋塞米注射液(药品码1876，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  碳酸氢钠与利尿药合用，有促使出现低氯化物碱中毒的危险性。\\r\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"国家基本药物(西药).第二版.国家药品监督管理局.北京:人民卫生出版社,2002.756,国家药典委员会.中华人民共和国药典(二部)临床用药须知.2000年版.北京:化学工业出版社,2001.P366-368,龙胆碳酸氢钠片说明书,依他尼酸片说明书(再评价说明书)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1},{\"COLOR\":\"黄色\",\"NAME\":\"相互作用\",\"WARNING_LEVEL\":\"慎用\",\"WARNING_INFO\":\"和注射用甲泼尼龙琥珀酸钠(药品码595，处方号105650_10009)一起使用会发生相互作用。【结果】\\r\\n  碳酸氢钠与肾上腺皮质激素(尤其是具有较强的盐皮质激素作用者)合用时，易致高钠血症和水肿。\\r\\n【处理】\\r\\n  两药合用需谨慎\",\"REF_SOURCE\":\"碳酸氢钠注射液说明书(湖北广济药业股份有限公司)\",\"YPMC\":\"\",\"JSXX\":\"\",\"ZYJL\":\"\",\"TYSM\":\"\",\"LCSY\":\"\",\"REGULAR_WARNING_LEVEL\":1}],\"studio.atur.api.persistence.presc_stat.PrescInfoKey@d647a2ca\":[]},\"output\":\"\",\"presDate\":null,\"presID\":null,\"HIGHEST_WARNING_LEVEL\":1,\"WARNING_COUNT\":42,\"TAG\":\"1\"}";
        return res;
    }

/*
    public String getTestJson(){
        String res = "{\n" +
                "    \"doctor\": {\n" +
                "        \"USER_ID\": \"1150\",\n" +
                "        \"NAME\": \"徐杰\",\n" +
                "        \"POSITION\": \"主治医师\",\n" +
                "        \"DEPT_CODE\": \"重症医学科\",\n" +
                "        \"DEPT_NAME\": \"重症医学科\"\n" +
                "    },\n" +
                "    \"patient\": {\n" +
                "        \"NAME\": \"柳巨武\",\n" +
                "        \"ID\": \"0000689061\",\n" +
                "        \"GENDER\": \"男\",\n" +
                "        \"BIRTH\": \"19630413\",\n" +
                "        \"WEIGHT\": \"\",\n" +
                "        \"HEIGHT\": \"\",\n" +
                "        \"ALERGY_DRUGS\": \"\",\n" +
                "        \"PREGNANT\": \"否\",\n" +
                "        \"LACT\": \"否\",\n" +
                "        \"HEPATICAL\": \"\",\n" +
                "        \"RENAL\": \"\",\n" +
                "        \"PANCREAS\": \"\",\n" +
                "        \"VISIT_ID\": \"\",\n" +
                "        \"PATIENT_PRES_ID\": \"77704\",\n" +
                "        \"IDENTITY_TYPE\": \"\",\n" +
                "        \"FEE_TYPE\": \"自费\",\n" +
                "        \"SCR\": \"\",\n" +
                "        \"SCR_UNIT\": \"\",\n" +
                "        \"GESTATION_AGE\": \"\",\n" +
                "        \"PRETERM_BIRTH\": \"\",\n" +
                "        \"DRUG_HISTORY\": \"\",\n" +
                "        \"FAMILY_DISEASE_HISTORY\": \"\",\n" +
                "        \"GENETIC_DISEASE\": \"\",\n" +
                "        \"MEDICARE_01\": \"\",\n" +
                "        \"MEDICARE_02\": \"\",\n" +
                "        \"MEDICARE_03\": \"\",\n" +
                "        \"MEDICARE_04\": \"\",\n" +
                "        \"MEDICARE_05\": \"\",\n" +
                "        \"WARD_CODE\": \"\",\n" +
                "        \"WARD_NAME\": \"\",\n" +
                "        \"BED_NO\": \"\",\n" +
                "        \"INPATIENT_NO\": \"\"\n" +
                "    },\n" +
                "    \"diagnosis\": {\n" +
                "        \"DIAGNOSISES\": \"创伤性蛛网膜下腔出血\"\n" +
                "    },\n" +
                "    \"advices\": [],\n" +
                "    \"checkInfoMap\": null,\n" +
                "    \"output\": \"\",\n" +
                "    \"presDate\": null,\n" +
                "    \"presID\": null,\n" +
                "    \"highestWarningLevelVirtual\": 0,\n" +
                "    \"HIGHEST_WARNING_LEVEL\": 0,\n" +
                "    \"WARNING_COUNT\": 0,\n" +
                "    \"TAG\": \"1\"\n" +
                "}";
        return res;
    }
*/
    /**
     * @param checkJson
     */
    protected CheckMessage handleCheckJson(String checkJson) {
        Gson g = new Gson();
        CheckResults results = g.fromJson(checkJson, CheckResults.class);
        String presId = results.getPatient().getPATIENT_PRES_ID();
        if(presId == null || "".equals(presId)){
            presId = CommonUtil.getPresIdWithTime(results.getPatient().getID());
        }else{
            presId = CommonUtil.getPresIdWithTime(presId);
        }

        if(results.getCheckInfoMap() == null){
            CheckMessage message = new CheckMessage();
            if(results.getAdvices() != null && results.getAdvices().size() != 0) {
                message.setHasProblem(-2);
            }else{
                message.setPresId(presId);
                message.setHasProblem(0);
            }
            return message;
        }
        results.setAdvices(sortgroupAdvice(results.getAdvices(),groupFlag));


        int warnLevel = results.getHIGHEST_WARNING_LEVEL();

        //缓存审核结果,等到进入审核结果页面时再读取记录显示
        if (warnLevel != 0) {
            CheckResultCache.putCheckResult(presId, results);
        }

        //处理审核信息
        CheckMessage message = new CheckMessage();
        message.setPresId(presId);
        message.setHasProblem(warnLevel == 0 ? 0 : 1);

        return message;
    }


    protected CheckMessage_BZ handleCheckJson_bz(String checkJson) {
        Gson g = new Gson();
        CheckResults results = g.fromJson(checkJson, CheckResults.class);
        String presId = results.getPatient().getPATIENT_PRES_ID();
        if(presId == null || "".equals(presId)){
            presId = CommonUtil.getPresIdWithTime(results.getPatient().getID());
        }else{
            presId = CommonUtil.getPresIdWithTime(presId);
        }

        if(results.getCheckInfoMap() == null){
            CheckMessage_BZ message = new CheckMessage_BZ();
            if(results.getAdvices() != null && results.getAdvices().size() != 0) {

            }else{
                message.setPresId(presId);
                message.setHasProblem(0);
                message.setState(1);
                message.setRetXml("<CheckResult STATE=\"0\" STYLE=\"\" CHECK_PHARMACIST_CODE=\"\" CHECK_PHARMACIST_NAME=\"\" CHECK_STATE=\"\" />");
                CheckResultCache.putCheckState(presId,message);
            }
            return message;
        }
        results.setAdvices(sortgroupAdvice(results.getAdvices(),groupFlag));


        int warnLevel = results.getHIGHEST_WARNING_LEVEL();

        //缓存审核结果,等到进入审核结果页面时再读取记录显示
        if (warnLevel != 0) {
            CheckResultCache.putCheckResult(presId, results);
        }

        //处理审核信息
        CheckMessage_BZ message = new CheckMessage_BZ();
        message.setPresId(presId);
        message.setHasProblem(warnLevel == 0 ? 0 : 1);
        if(warnLevel == 0){
            message.setState(1);
            message.setRetXml("<CheckResult STATE=\"0\" STYLE=\"\" CHECK_PHARMACIST_CODE=\"\" CHECK_PHARMACIST_NAME=\"\" CHECK_STATE=\"\" />");
        }
        CheckResultCache.putCheckState(presId,message);
        return message;
    }


    public String toJson(CheckResults checkResults) {
        Gson gson = new Gson();
        return gson.toJson(checkResults);
    }

    public CheckResults findCheckResult(String presId) {
        return CheckResultCache.findCheckResult(presId);
    }

    public CheckResults removeCheckResult(String presId) {
        return CheckResultCache.removeCheckResult(presId);
    }

    public void putRetValue(String presId, int result) {
        RetValCache.putRetVal(presId, result);
    }

    /**
     * 如果该id没有信息，则返回-2。
     * 下一步返回0
     * 返回修改返回-1
     *
     * @param presId
     * @return
     */
    public int findRetValue(String presId) {
        if (notBackOrNext(presId))
            return -2;
        return RetValCache.removeRetVal(presId);
    }

    public void putRetCache_bz(String presId, CheckMessage_BZ message) {
        CheckResultCache.putCheckState(presId, message);
    }

    //从缓存中取出相应的CheckMessage
    public CheckMessage_BZ getRetCache_bz(String presId){
        return CheckResultCache.findCheckState(presId);
    }

    public void putRetValue_bz(String presId, String message) {
        RetValCache.putRetVal_bz(presId, message);
    }

    //根据presId获取缓存中的返回字符串
    public String findRetValue_bz(String presId){
        if(RetValCache.containsKey_bz(presId)){
            return RetValCache.removeRetVal_bz(presId);
        }else{
            String retXml = "<CheckResult STATE=\"-1\" STYLE=\"\" CHECK_PHARMACIST_CODE=\"\" CHECK_PHARMACIST_NAME=\"\" CHECK_STATE=\"\" />";
            return retXml;
        }
    }

    private boolean notBackOrNext(String presId) {
        return !RetValCache.containsKey(presId);
    }

    @Autowired
    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public String checkPresc(String presId) {
        String url = checkServerUrl + "?tag=3";
//        String presIdWithTime = CommonUtil.getPresIdWithTime(presId);
        String data = cacheService.getXMLFromCache(presId, presId);
        logger.debug(url+" "+data);
        HttpUtil.sendPost(url, data);
        return presId;
    }


    @Deprecated
    public List<Advice> sortCheckResult(List<Advice> advices){
        List<Advice> newList0 = new ArrayList<Advice>();
        List<Advice> newList1 = new ArrayList<Advice>();
        List<Advice> newList2 = new ArrayList<Advice>();
        List<Advice> newList3 = new ArrayList<Advice>();
        if(advices == null || advices.size() <= 1){
            return advices;
        }
        for (Advice advice : advices) {
            List<CheckInfo> checkInfos = advice.getCheckInfoList();
            if(getHighestLevelFromCheckInfoList(checkInfos) == 3){
                newList3.add(advice);
            }else if(getHighestLevelFromCheckInfoList(checkInfos) == 2){
                newList2.add(advice);
            }else if(getHighestLevelFromCheckInfoList(checkInfos) == 1){
                newList1.add(advice);
            }else if(getHighestLevelFromCheckInfoList(checkInfos) == 0){
                newList0.add(advice);
            }
        }

        List<Advice> finalList = new ArrayList<Advice>();

        finalList = sortHelp(newList3,finalList);
        finalList = sortHelp(newList2,finalList);
        finalList = sortHelp(newList1,finalList);
        finalList = sortHelp(newList0,finalList);

        return finalList;

    }

    //辅助方法，避免重复代码
    @Deprecated
    public List<Advice> sortHelp(List<Advice> list,List<Advice> finalList){
        if("1".equals(groupFlag)){
            list = sortSameLevelGroupId(list);
        }
        else if("2".equals(groupFlag)) {
            list = sortSameLevelOrderNo(list);
        }
        for(Advice advice:list){
            finalList.add(advice);
        }
        return finalList;
    }

    //对同一级别的问题按照group_id排序
    @Deprecated
    public List<Advice> sortSameLevelGroupId(List<Advice> newList){
        //finalList存放：按照group_id从小到大排列
        List<Advice> finalList = new ArrayList<Advice>();

        //min存放：group_id一轮的最小值
        BigDecimal min = new BigDecimal(-1);
        for(int i=0;i<newList.size();++i){
            for(int j=0;j<newList.size();++j){
                Advice advice = newList.get(j);
                if(advice.isGroup_id_flag() == true){
                    continue;
                }
                if(advice.getGROUP_ID() == null || "".equals(advice.getGROUP_ID())){
                    advice.setGROUP_ID("0");
                }
                String n_group_id = advice.getGROUP_ID().replaceAll("[^\\d]+", "");
                if(min.equals(-1) || new BigDecimal(n_group_id).compareTo(min) == -1){
                    min = new BigDecimal(n_group_id);
                }
            }

            //tempList存放：group_id等于这次遍历最小值的处方
            List<Advice> tempList = new ArrayList<Advice>();
            for(int k=0;k<newList.size();++k){
                Advice advice = newList.get(k);
                String n_group_id = advice.getGROUP_ID().replaceAll("[^\\d]+", "");
                if(new BigDecimal(n_group_id).equals(min)){
                    advice.setGroup_id_flag(true);
                    tempList.add(advice);
                }
            }

            //将tempList中的处方加入到finalList中
            int x = 0;
            for(int l=0;l<tempList.size();++l){
                Advice advice = tempList.get(l);
                if(tempList.size() > 1){
                    if(x == 0){
                        advice.setKh("┍ ");
                    }
                    else if(x == tempList.size()-1){
                        advice.setKh("┕ ");
                    }
                    else{
                        advice.setKh("");
                    }
                }else{
                    advice.setKh("");
                }
                ++x;
                finalList.add(advice);
            }
            min = new BigDecimal(-1);
        }
        return finalList;
    }

    /**
     * add by wtwang @2019.01.16
     * group_id或者order_no相同为一组，同一组的医嘱在一起，计算组中最高的问题级别
     * 将原始问题医嘱列表按照group_id或者order_no成组分好
     * 并且成组的问题按照问题级别排好序
     * 各个组之间按照组的最高问题级别排好序
     * flag为1表示按照group_id分组，flag为2表示按照order_no分组
     * @param list
     * @return
     */
    public List<Advice> sortgroupAdvice(List<Advice> list,String flag){
        List<Advice> result = new ArrayList<Advice>();
        Map<BigDecimal,List<Advice>> map = new HashMap<BigDecimal, List<Advice>>();
        for(Advice advice : list){
            String s_id = "";
            // 区分按照group_id分组还是按照order_no分组
            if("1".equals(flag)){
                if(advice.getGROUP_ID() == null || "".equals(advice.getGROUP_ID())){
                    advice.setGROUP_ID("0");
                }
                s_id = advice.getGROUP_ID().replaceAll("[^\\d]+", "");
            }else if("2".equals(flag)){
                if(advice.getORDER_NO() == null || "".equals(advice.getORDER_NO())){
                    advice.setORDER_NO("0");
                }
                s_id = advice.getORDER_NO().replaceAll("[^\\d]+", "");
            }
            // 遍历list，将一组的医嘱存入同一个list中
            if(!map.containsKey(new BigDecimal(s_id))){
                List<Advice> tempList = new ArrayList<Advice>();
                tempList.add(advice);
                map.put(new BigDecimal(s_id),tempList);
            }else{
                map.get(new BigDecimal(s_id)).add(advice);
            }
        }

        // list_i表示成组医嘱中最该问题级别为i的集合
        List<List<Advice>> list_3 = new ArrayList<List<Advice>>();
        List<List<Advice>> list_2 = new ArrayList<List<Advice>>();
        List<List<Advice>> list_1 = new ArrayList<List<Advice>>();
        List<List<Advice>> list_0 = new ArrayList<List<Advice>>();
        // 遍历map中的成组医嘱，计算每组医嘱的最高问题级别
        for(List<Advice> tempList : map.values()){
            // 在此时将成组的医嘱按照问题级别从高到低排好序
            tempList = sortSameGroup(tempList);
            // 将同一组的医嘱用括号标识括起来
            tempList = handleSameGroup(tempList);

            int level = getHighestLevelFromAdviceList(tempList);
            // 根据最高问题级别将医嘱组加入到不同的list中
            switch (level){
                case 3:
                    list_3.add(tempList);
                    break;
                case 2:
                    list_2.add(tempList);
                    break;
                case 1:
                    list_1.add(tempList);
                    break;
                case 0:
                    list_0.add(tempList);
                    break;
                default:
                    break;
            }

        }

        // 将各个问题级别的医嘱组按照顺序加入到结果中
        for(List<Advice> list3 : list_3){
            for(Advice advice : list3){
                result.add(advice);
            }
        }
        for(List<Advice> list2 : list_2){
            for(Advice advice : list2){
                result.add(advice);
            }
        }
        for(List<Advice> list1 : list_1){
            for(Advice advice : list1){
                result.add(advice);
            }
        }
        for(List<Advice> list0 : list_0){
            for(Advice advice : list0){
                result.add(advice);
            }
        }


        return result;
    }

    /**
     * 将同组的医嘱用括号括起来
     * @param list
     * @return
     */
    public List<Advice> handleSameGroup(List<Advice> list){
        int x = 0;
        for(int l=0;l<list.size();++l){
            Advice advice = list.get(l);
            if(list.size() > 1){
                if(x == 0){
                    advice.setKh("┍ ");
                }
                else if(x == list.size()-1){
                    advice.setKh("┕ ");
                }
                else{
                    advice.setKh("");
                }
            }else{
                advice.setKh("");
            }
            ++x;
        }
        return list;
    }

    /**
     * add by wtwang @2019.01.17
     * 将同一分组的医嘱按照问题级别从高到低排序
     * @param list
     * @return
     */
    public List<Advice> sortSameGroup(List<Advice> list){
        List<Advice> result = new ArrayList<Advice>();
        Map<Integer,List<Advice>> map = new HashMap<Integer, List<Advice>>();

        // 遍历list，将问题级别相同医嘱放入同一个list中
        for(Advice advice : list){
            List<CheckInfo> checkInfos = advice.getCheckInfoList();
            int warning_level = getHighestLevelFromCheckInfoList(checkInfos);
            if(!map.containsKey(warning_level)){
                List<Advice> tempList = new ArrayList<Advice>();
                tempList.add(advice);
                map.put(warning_level,tempList);
            }else{
                map.get(warning_level).add(advice);
            }
        }

        // i从3到0表示问题级别，按照问题级别从高到低从map中取出医嘱加入到result中
        for(int i=3;i>=0;--i){
            List<Advice> tempList = map.get(i);
            if(tempList != null && tempList.size() != 0){
                for(Advice advice : tempList){
                    result.add(advice);
                }
            }
        }

        return result;
    }

    /**
     * add by wtwang @2019.01.17
     * 同一组的医嘱计算最高问题级别
     * @param list
     * @return
     */
    public int getHighestLevelFromAdviceList(List<Advice> list){
        int highestLevel = 0;
        for(Advice advice : list){
            List<CheckInfo> checkInfos = advice.getCheckInfoList();
            int tempLevel = getHighestLevelFromCheckInfoList(checkInfos);
            highestLevel = Math.max(highestLevel,tempLevel);
        }
        return highestLevel;
    }

    //对同一级别的问题按照order_no排序
    @Deprecated
    public List<Advice> sortSameLevelOrderNo(List<Advice> newList){
        //finalList存放：按照order_no从小到到排列,按照order_sub_no从小到大排列
        List<Advice> finalList = new ArrayList<Advice>();

        //min存放：order_no一轮的最小值
        BigDecimal min = new BigDecimal(-1);
        for(int i=0;i<newList.size();++i){
            //本循环的目的：取到一轮遍历中order_no的最小值
            for(int j=0;j<newList.size();++j){
                Advice advice = newList.get(j);
                //order_no_flag为true表示已经有序加到finalList中，不需要再处理
                if(advice.isOrder_no_flag() == true){
                    continue;
                }

                if(advice.getORDER_NO() == null || "".equals(advice.getORDER_NO())){
                    advice.setORDER_NO("0");
                }
                //对order_no中包含非数字的处理，替换非数字为空格
                String n_order_no = advice.getORDER_NO().replaceAll("[^\\d]+", "");
                //取一轮遍历中order_no的最小值
                if(min.equals(-1) || new BigDecimal(n_order_no).compareTo(min)==-1){
                    min = new BigDecimal(n_order_no);
                }
            }
            //tempList存放：order_no等于这次遍历最小值的处方
            List<Advice> tempList = new ArrayList<Advice>();
            for(int k=0;k<newList.size();++k){
                Advice advice = newList.get(k);
                String n_order_no = advice.getORDER_NO().replaceAll("[^\\d]+", "");

                if(new BigDecimal(n_order_no).equals(min)){
                    advice.setOrder_no_flag(true);
                    tempList.add(advice);
                }
            }

            //min2存放：order_no等于这次遍历最小值处方中的order_sub_no最小值
            BigDecimal min2 = new BigDecimal(-1);
            int x = 0;
            for(int m=0;m<tempList.size();++m){
                //本循环的目的：取到相同order_no的order_sub_no最小值
                for(int n=0;n<tempList.size();++n){
                    Advice advice = tempList.get(n);
                    //order_sub_no_flag为true表示已经有序加到finalList中，不需要再处理
                    if("".equals(advice.getORDER_SUB_NO())){
                        continue;
                    }
                    if(advice.isOrder_sub_no_flag() == true){
                        continue;
                    }
                    //取一轮遍历中order_sub_no的最小值
                    if(min2.equals(-1) || new BigDecimal(advice.getORDER_SUB_NO()).compareTo(min2) == -1){
                        min2 = new BigDecimal(advice.getORDER_SUB_NO());
                    }
                }

                //将order_sub_no等于最小值的处方加入到finalList中，并将order_sub_no_flag改为true表明已经处理过
                for(int l=0;l<tempList.size();++l){
                    Advice advice = tempList.get(l);
                    if(advice.isOrder_sub_no_flag() == true || "".equals(advice.getORDER_SUB_NO())){
                        continue;
                    }
                    if(new BigDecimal(advice.getORDER_SUB_NO()).equals(min2)){
                        //给成组的处方加上左侧方括号
                        if(tempList.size() > 1){
                            if(x == 0){
                                advice.setKh("┍ ");
                            }
                            else if(x == tempList.size()-1){
                                advice.setKh("┕ ");
                            }
                            else{
                                advice.setKh("");
                            }
                        }else{
                            advice.setKh("");
                        }
                        ++x;
                        finalList.add(advice);
                        advice.setOrder_sub_no_flag(true);
                    }
                }

                //将order_sub_no为空并且还未加到finalList的处方加入到finalList
                for(int y=0;y<tempList.size();++y){
                    Advice advice = tempList.get(y);
                    if(advice.isOrder_sub_no_flag() == false) {
                        if ("".equals(advice.getORDER_SUB_NO())) {
                            //给成组的处方加上左侧方括号
                            if (tempList.size() > 1) {
                                if (x == 0) {
                                    advice.setKh("┍ ");
                                } else if (x == tempList.size() - 1) {
                                    advice.setKh("┕ ");
                                } else {
                                    advice.setKh("");
                                }
                            } else {
                                advice.setKh("");
                            }
                            ++x;
                            finalList.add(advice);
                            advice.setOrder_sub_no_flag(true);
                        }
                    }
                }
                //将min2改为-1，进行下一次寻找order_sub_no的最小值
                min2 = new BigDecimal(-1);
            }
            //将min改为-1，进行下一次寻找order_no的最小值
            min = new BigDecimal(-1);
        }

        return finalList;
    }


    public int getHighestLevelFromCheckInfoList(List<CheckInfo> checkInfos){
        int highestLevel = 0;
        for(CheckInfo checkInfo: checkInfos){
            if(highestLevel < Integer.parseInt(checkInfo.getREGULAR_WARNING_LEVEL())){
                highestLevel = Integer.parseInt(checkInfo.getREGULAR_WARNING_LEVEL());
            } else if(Integer.parseInt(checkInfo.getREGULAR_WARNING_LEVEL()) == -1 && highestLevel != 3){
                highestLevel = 3;
            }
        }
        return highestLevel;
    }

}
