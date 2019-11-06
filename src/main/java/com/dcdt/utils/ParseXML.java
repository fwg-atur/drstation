package com.dcdt.utils;

import com.dcdt.doctorstation.entity.*;
import com.dcdt.doctorstation.service.PharmacistPrescCheckService;
import org.jdom.Document;
import org.jdom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wtwang on 2018/3/17.
 */
public class ParseXML {
    CheckPresInput checkPresInput = new CheckPresInput();
    CheckPresOutput checkPresOutput = new CheckPresOutput();
    CheckPharmacist checkPharmacist = new CheckPharmacist();

    //医生站过滤非法xml
    public boolean filter(String xml){
        if(!xml.contains("CheckInput"))
            return false;
        return true;
    }

    //解析审核结果xml
    public void parseXML(String xml){
        if(xml == null || xml.length() == 0){
            return;
        }
        Document document = null;

        int index = xml.indexOf("Check");
        if(index != -1) {
            xml = xml.substring(index-1);
        }
        try {
            document = XmlUtil.readDocumentFromStr(xml.trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Element root = document.getRootElement();

        for(Object checkObject:root.getChildren("Check")){
            Element checkElement = (Element) checkObject;

            Object checkInputObject = checkElement.getChild("CheckInput");
            Element checkInputElement = (Element) checkInputObject;

            Object doctorObject = checkInputElement.getChild("Doctor");
            Element doctorElement = (Element) doctorObject;
            Doctor doctor = new Doctor();
            doctor.setNAME(doctorElement.getAttributeValue("NAME"));
            doctor.setPOSITION(doctorElement.getAttributeValue("POSITION"));
            doctor.setUSER_ID(doctorElement.getAttributeValue("USER_ID"));
            doctor.setDEPT_NAME(doctorElement.getAttributeValue("DEPT_NAME"));
            doctor.setDEPT_CODE(doctorElement.getAttributeValue("DEPT_CODE"));
            checkPresInput.setDoctor(doctor);

            Object patientObject = checkInputElement.getChild("Patient");
            Element patientElement = (Element) patientObject;
            Patient patient = new Patient();
            patient.setNAME(patientElement.getAttributeValue("NAME"));
            patient.setID(patientElement.getAttributeValue("ID"));
            patient.setVISIT_ID(patientElement.getAttributeValue("VISIT_ID"));
            patient.setPATIENT_PRES_ID(patientElement.getAttributeValue("PATIENT_PRES_ID"));
            patient.setBIRTH(patientElement.getAttributeValue("BIRTH"));
            patient.setHEIGHT(patientElement.getAttributeValue("HEIGHT"));
            patient.setWEIGHT(patientElement.getAttributeValue("WEIGHT"));
            patient.setGENDER(patientElement.getAttributeValue("GENDER"));
            patient.setPREGNANT(patientElement.getAttributeValue("PREGNANT"));
            patient.setLACT(patientElement.getAttributeValue("LACT"));
            patient.setHEPATICAL(patientElement.getAttributeValue("HEPATICAL"));
            patient.setRENAL(patientElement.getAttributeValue("RENAL"));
            patient.setPANCREAS(patientElement.getAttributeValue("PANCREAS"));
            patient.setALERGY_DRUGS(patientElement.getAttributeValue("ALERGY_DRUGS"));
            patient.setIDENTITY_TYPE(patientElement.getAttributeValue("IDENTITY_TYPE"));
            patient.setFEE_TYPE(patientElement.getAttributeValue("FEE_TYPE"));
            patient.setSCR(patientElement.getAttributeValue("SCR"));
            patient.setSCR_UNIT(patientElement.getAttributeValue("SCR_UNIT"));
            patient.setGESTATION_AGE(patientElement.getAttributeValue("GESTATION_AGE"));
            patient.setPRETERM_BIRTH(patientElement.getAttributeValue("PRETERM_BIRTH"));
            patient.setDRUG_HISTORY(patientElement.getAttributeValue("DRUG_HISTORY"));
            patient.setFAMILY_DISEASE_HISTORY(patientElement.getAttributeValue("FAMILY_DISEASE_HISTORY"));
            patient.setGENETIC_DISEASE(patientElement.getAttributeValue("GENETIC_DISEASE"));
            patient.setMEDICARE_01(patientElement.getAttributeValue("MEDICARE_01"));
            patient.setMEDICARE_02(patientElement.getAttributeValue("MEDICARE_02"));
            patient.setMEDICARE_03(patientElement.getAttributeValue("MEDICARE_03"));
            patient.setMEDICARE_04(patientElement.getAttributeValue("MEDICARE_04"));
            patient.setMEDICARE_05(patientElement.getAttributeValue("MEDICARE_05"));
            checkPresInput.setPatient(patient);

            Object diagnosisObject = checkInputElement.getChild("Diagnosises");
            Element diagnosisElement = (Element) diagnosisObject;
            Diagnosis diagnosis = new Diagnosis();
            diagnosis.setDIAGNOSISES(diagnosisElement.getAttributeValue("DIAGNOSISES"));
            checkPresInput.setDiagnosis(diagnosis);

            List<Advice> advices = new ArrayList<Advice>();
            Object advicesObject = checkInputElement.getChild("Advices");
            Element advicesElement = (Element) advicesObject;
            for(Object adviceObject : advicesElement.getChildren("Advice")){
                Element adviceElement = (Element) adviceObject;
                Advice advice = new Advice();
                advice.setDRUG_LO_ID(adviceElement.getAttributeValue("DRUG_LO_ID"));
                advice.setDRUG_LO_NAME(adviceElement.getAttributeValue("DRUG_LO_NAME"));
                advice.setADMINISTRATION(adviceElement.getAttributeValue("ADMINISTRATION"));
                advice.setDOSAGE(adviceElement.getAttributeValue("DOSAGE"));
                advice.setDOSAGE_UNIT(adviceElement.getAttributeValue("DOSAGE_UNIT"));
                advice.setFREQ_COUNT(adviceElement.getAttributeValue("FREQ_COUNT"));
                advice.setFREQ_INTERVAL(adviceElement.getAttributeValue("FREQ_INTERVAL"));
                advice.setFREQ_INTERVAL_UNIT(adviceElement.getAttributeValue("FREQ_INTERVAL_UNIT"));
                advice.setSTART_DAY(adviceElement.getAttributeValue("START_DAY"));
                advice.setEND_DAY(adviceElement.getAttributeValue("END_DAY"));
                advice.setREPEAT(adviceElement.getAttributeValue("REPEAT"));
                advice.setORDER_NO(adviceElement.getAttributeValue("ORDER_NO"));
                advice.setORDER_SUB_NO(adviceElement.getAttributeValue("ORDER_SUB_NO"));
                advice.setDEPT_CODE(adviceElement.getAttributeValue("DEPT_CODE"));
                advice.setDOCTOR_NAME(adviceElement.getAttributeValue("DOCTOR_NAME"));
                advice.setTITLE(adviceElement.getAttributeValue("TITLE"));
                advice.setAUTHORITY_LEVELS(adviceElement.getAttributeValue("AUTHORITY_LEVELS"));
                advice.setALERT_LEVELS(adviceElement.getAttributeValue("ALERT_LEVELS"));
                advice.setGROUP_ID(adviceElement.getAttributeValue("GROUP_ID"));
                advice.setUSER_ID(adviceElement.getAttributeValue("USER_ID"));
                advice.setPRES_ID(adviceElement.getAttributeValue("PRES_ID"));
                advice.setPRES_DATE(adviceElement.getAttributeValue("PRES_DATE"));
                advice.setPRES_SEQ_ID(adviceElement.getAttributeValue("PRES_SEQ_ID"));
                advice.setPK_ORDER_NO(adviceElement.getAttributeValue("PK_ORDER_NO"));
                advice.setCOURSE(adviceElement.getAttributeValue("COURSE"));
                advice.setPKG_COUNT(adviceElement.getAttributeValue("PKG_COUNT"));
                advice.setPKG_UNIT(adviceElement.getAttributeValue("PKG_UNIT"));
                advice.setBAK_01(adviceElement.getAttributeValue("BAK_01"));
                advice.setBAK_02(adviceElement.getAttributeValue("BAK_02"));
                advice.setBAK_03(adviceElement.getAttributeValue("BAK_03"));
                advice.setBAK_04(adviceElement.getAttributeValue("BAK_04"));
                advice.setBAK_05(adviceElement.getAttributeValue("BAK_05"));
                advices.add(advice);
            }
            checkPresInput.setAdvices(advices);


            Object checkOutputObject = checkElement.getChild("CheckOutput");
            Element checkOutputElement = (Element) checkOutputObject;

            List<PrescInfo> prescInfos = new ArrayList<PrescInfo>();
            for(Object presInfoObject : checkOutputElement.getChildren("PresInfo")){
                Element presInfoElement = (Element) presInfoObject;
                PrescInfo prescInfo = new PrescInfo();
                prescInfo.setGroup_id(presInfoElement.getAttributeValue("GROUP_ID"));
                prescInfo.setOrder_id(presInfoElement.getAttributeValue("ORDER_ID"));
                prescInfo.setOrder_sub_id(presInfoElement.getAttributeValue("ORDER_SUB_ID"));
                prescInfo.setDrug_lo_id(presInfoElement.getAttributeValue("DRUG_LO_ID"));
                prescInfo.setDrug_lo_name(presInfoElement.getAttributeValue("DRUG_LO_NAME"));

                List<CheckInfo> checkInfos = new ArrayList<CheckInfo>();
                for(Object checkInfoObject : presInfoElement.getChildren("CheckInfo")){
                    Element checkInfoElement = (Element) checkInfoObject;
                    CheckInfo checkInfo = new CheckInfo();
                    checkInfo.setCOLOR(checkInfoElement.getAttributeValue("COLOR"));
                    checkInfo.setNAME(checkInfoElement.getAttributeValue("NAME"));
                    checkInfo.setWARNING_LEVEL(checkInfoElement.getAttributeValue("WARNING_LEVEL"));
                    checkInfo.setWARNING_INFO(checkInfoElement.getAttributeValue("WARNING_INFO"));
                    checkInfo.setREF_SOURCE(checkInfoElement.getAttributeValue("REF_SOURCE"));
                    checkInfo.setYPMC(checkInfoElement.getAttributeValue("YPMC"));
                    checkInfo.setJSXX(checkInfoElement.getAttributeValue("JSXX"));
                    checkInfo.setZYJL(checkInfoElement.getAttributeValue("ZYJL"));
                    checkInfo.setTYSM(checkInfoElement.getAttributeValue("TYSM"));
                    checkInfo.setLCSY(checkInfoElement.getAttributeValue("LCSY"));
                    if("慎用".equals(checkInfo.getWARNING_LEVEL())){
                        checkInfo.setREGULAR_WARNING_LEVEL("1");
                    }
                    else if("禁忌".equals(checkInfo.getWARNING_LEVEL()) || "禁用".equals(checkInfo.getWARNING_LEVEL())){
                        checkInfo.setREGULAR_WARNING_LEVEL("2");
                    }
                    else if("强制阻断".equals(checkInfo.getWARNING_LEVEL())){
                        checkInfo.setREGULAR_WARNING_LEVEL("-1");
                    }else {
                        checkInfo.setREGULAR_WARNING_LEVEL("0");
                    }
                    checkInfos.add(checkInfo);
                }
                prescInfo.setCheckInfos(checkInfos);
                prescInfos.add(prescInfo);
            }
            checkPresOutput.setPrescInfos(prescInfos);

            Object checkPharmacistObject = checkElement.getChild("CheckPharmacist");
            Element checkPharmacistElement = (Element) checkPharmacistObject;
            checkPharmacist.setPharmacistCheck(checkPharmacistElement.getValue().replaceAll("\\t","<br/>"));

        }
    }

    //解析昌平接口审核结果xml
    public void parseXML_CP(String xml){
        if(xml == null || xml.length() == 0){
            return;
        }
        Document document = null;

        int index = xml.indexOf("Check");
        if(index != -1) {
            xml = xml.substring(index-1);
        }
        try {
            document = XmlUtil.readDocumentFromStr(xml.trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Element root = document.getRootElement();


        Object checkInputObject = root.getChild("CheckInput");
        Element checkInputElement = (Element) checkInputObject;

        Object doctorObject = checkInputElement.getChild("Doctor");
        Element doctorElement = (Element) doctorObject;
        Doctor doctor = new Doctor();
        doctor.setNAME(doctorElement.getAttributeValue("NAME"));
        doctor.setPOSITION(doctorElement.getAttributeValue("POSITION"));
        doctor.setUSER_ID(doctorElement.getAttributeValue("USER_ID"));
        doctor.setDEPT_NAME(doctorElement.getAttributeValue("DEPT_NAME"));
        doctor.setDEPT_CODE(doctorElement.getAttributeValue("DEPT_CODE"));
        checkPresInput.setDoctor(doctor);

        Object patientObject = checkInputElement.getChild("Patient");
        Element patientElement = (Element) patientObject;
        Patient patient = new Patient();
        patient.setNAME(patientElement.getAttributeValue("NAME"));
        patient.setID(patientElement.getAttributeValue("ID"));
        patient.setVISIT_ID(patientElement.getAttributeValue("VISIT_ID"));
        patient.setPATIENT_PRES_ID(patientElement.getAttributeValue("PATIENT_PRES_ID"));
        patient.setBIRTH(patientElement.getAttributeValue("BIRTH"));
        patient.setHEIGHT(patientElement.getAttributeValue("HEIGHT"));
        patient.setWEIGHT(patientElement.getAttributeValue("WEIGHT"));
        patient.setGENDER(patientElement.getAttributeValue("GENDER"));
        patient.setPREGNANT(patientElement.getAttributeValue("PREGNANT"));
        patient.setLACT(patientElement.getAttributeValue("LACT"));
        patient.setHEPATICAL(patientElement.getAttributeValue("HEPATICAL"));
        patient.setRENAL(patientElement.getAttributeValue("RENAL"));
        patient.setPANCREAS(patientElement.getAttributeValue("PANCREAS"));
        patient.setALERGY_DRUGS(patientElement.getAttributeValue("ALERGY_DRUGS"));
        patient.setIDENTITY_TYPE(patientElement.getAttributeValue("IDENTITY_TYPE"));
        patient.setFEE_TYPE(patientElement.getAttributeValue("FEE_TYPE"));
        patient.setSCR(patientElement.getAttributeValue("SCR"));
        patient.setSCR_UNIT(patientElement.getAttributeValue("SCR_UNIT"));
        patient.setGESTATION_AGE(patientElement.getAttributeValue("GESTATION_AGE"));
        patient.setPRETERM_BIRTH(patientElement.getAttributeValue("PRETERM_BIRTH"));
        patient.setDRUG_HISTORY(patientElement.getAttributeValue("DRUG_HISTORY"));
        patient.setFAMILY_DISEASE_HISTORY(patientElement.getAttributeValue("FAMILY_DISEASE_HISTORY"));
        patient.setGENETIC_DISEASE(patientElement.getAttributeValue("GENETIC_DISEASE"));
        patient.setMEDICARE_01(patientElement.getAttributeValue("MEDICARE_01"));
        patient.setMEDICARE_02(patientElement.getAttributeValue("MEDICARE_02"));
        patient.setMEDICARE_03(patientElement.getAttributeValue("MEDICARE_03"));
        patient.setMEDICARE_04(patientElement.getAttributeValue("MEDICARE_04"));
        patient.setMEDICARE_05(patientElement.getAttributeValue("MEDICARE_05"));
        checkPresInput.setPatient(patient);

        Object diagnosisObject = checkInputElement.getChild("Diagnosises");
        Element diagnosisElement = (Element) diagnosisObject;
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setDIAGNOSISES(diagnosisElement.getAttributeValue("DIAGNOSISES"));
        checkPresInput.setDiagnosis(diagnosis);

        List<Advice> advices = new ArrayList<Advice>();
        Object advicesObject = checkInputElement.getChild("Advices");
        Element advicesElement = (Element) advicesObject;
        for(Object adviceObject : advicesElement.getChildren("Advice")){
            Element adviceElement = (Element) adviceObject;
            Advice advice = new Advice();
            advice.setDRUG_LO_ID(adviceElement.getAttributeValue("DRUG_LO_ID"));
            advice.setDRUG_LO_NAME(adviceElement.getAttributeValue("DRUG_LO_NAME"));
            advice.setADMINISTRATION(adviceElement.getAttributeValue("ADMINISTRATION"));
            advice.setDOSAGE(adviceElement.getAttributeValue("DOSAGE"));
            advice.setDOSAGE_UNIT(adviceElement.getAttributeValue("DOSAGE_UNIT"));
            advice.setFREQ_COUNT(adviceElement.getAttributeValue("FREQ_COUNT"));
            advice.setFREQ_INTERVAL(adviceElement.getAttributeValue("FREQ_INTERVAL"));
            advice.setFREQ_INTERVAL_UNIT(adviceElement.getAttributeValue("FREQ_INTERVAL_UNIT"));
            advice.setSTART_DAY(adviceElement.getAttributeValue("START_DAY"));
            advice.setEND_DAY(adviceElement.getAttributeValue("END_DAY"));
            advice.setREPEAT(adviceElement.getAttributeValue("REPEAT"));
            advice.setORDER_NO(adviceElement.getAttributeValue("ORDER_NO"));
            advice.setORDER_SUB_NO(adviceElement.getAttributeValue("ORDER_SUB_NO"));
            advice.setDEPT_CODE(adviceElement.getAttributeValue("DEPT_CODE"));
            advice.setDOCTOR_NAME(adviceElement.getAttributeValue("DOCTOR_NAME"));
            advice.setTITLE(adviceElement.getAttributeValue("TITLE"));
            advice.setAUTHORITY_LEVELS(adviceElement.getAttributeValue("AUTHORITY_LEVELS"));
            advice.setALERT_LEVELS(adviceElement.getAttributeValue("ALERT_LEVELS"));
            advice.setGROUP_ID(adviceElement.getAttributeValue("GROUP_ID"));
            advice.setUSER_ID(adviceElement.getAttributeValue("USER_ID"));
            advice.setPRES_ID(adviceElement.getAttributeValue("PRES_ID"));
            advice.setPRES_DATE(adviceElement.getAttributeValue("PRES_DATE"));
            advice.setPRES_SEQ_ID(adviceElement.getAttributeValue("PRES_SEQ_ID"));
            advice.setPK_ORDER_NO(adviceElement.getAttributeValue("PK_ORDER_NO"));
            advice.setCOURSE(adviceElement.getAttributeValue("COURSE"));
            advice.setPKG_COUNT(adviceElement.getAttributeValue("PKG_COUNT"));
            advice.setPKG_UNIT(adviceElement.getAttributeValue("PKG_UNIT"));
            advice.setBAK_01(adviceElement.getAttributeValue("BAK_01"));
            advice.setBAK_02(adviceElement.getAttributeValue("BAK_02"));
            advice.setBAK_03(adviceElement.getAttributeValue("BAK_03"));
            advice.setBAK_04(adviceElement.getAttributeValue("BAK_04"));
            advice.setBAK_05(adviceElement.getAttributeValue("BAK_05"));
            advices.add(advice);
        }
        checkPresInput.setAdvices(advices);


        Object checkOutputObject = root.getChild("CheckOutput");
        Element checkOutputElement = (Element) checkOutputObject;

        List<PrescInfo> prescInfos = new ArrayList<PrescInfo>();
        for(Object presInfoObject : checkOutputElement.getChildren("PresInfo")){
            Element presInfoElement = (Element) presInfoObject;
            PrescInfo prescInfo = new PrescInfo();
            prescInfo.setGroup_id(presInfoElement.getAttributeValue("GROUP_ID"));
            prescInfo.setOrder_id(presInfoElement.getAttributeValue("ORDER_ID"));
            prescInfo.setOrder_sub_id(presInfoElement.getAttributeValue("ORDER_SUB_ID"));
            prescInfo.setDrug_lo_id(presInfoElement.getAttributeValue("DRUG_LO_ID"));
            prescInfo.setDrug_lo_name(presInfoElement.getAttributeValue("DRUG_LO_NAME"));
            prescInfo.setGanyu_state(presInfoElement.getAttributeValue("GANYU_STATE"));
            prescInfo.setGanyu_info(presInfoElement.getAttributeValue("GANYU_INFO"));
            prescInfo.setMedication_flag(presInfoElement.getAttributeValue("MEDICATION_FLAG"));

            List<CheckInfo> checkInfos = new ArrayList<CheckInfo>();
            for(Object checkInfoObject : presInfoElement.getChildren("CheckInfo")){
                Element checkInfoElement = (Element) checkInfoObject;
                CheckInfo checkInfo = new CheckInfo();
                checkInfo.setCOLOR(checkInfoElement.getAttributeValue("COLOR"));
                checkInfo.setNAME(checkInfoElement.getAttributeValue("NAME"));
                checkInfo.setWARNING_LEVEL(checkInfoElement.getAttributeValue("WARNING_LEVEL"));
                checkInfo.setWARNING_INFO(checkInfoElement.getAttributeValue("WARNING_INFO"));
                checkInfo.setREF_SOURCE(checkInfoElement.getAttributeValue("REF_SOURCE"));
                checkInfo.setYPMC(checkInfoElement.getAttributeValue("YPMC"));
                checkInfo.setJSXX(checkInfoElement.getAttributeValue("JSXX"));
                checkInfo.setZYJL(checkInfoElement.getAttributeValue("ZYJL"));
                checkInfo.setTYSM(checkInfoElement.getAttributeValue("TYSM"));
                checkInfo.setLCSY(checkInfoElement.getAttributeValue("LCSY"));
                if("慎用".equals(checkInfo.getWARNING_LEVEL())){
                    checkInfo.setREGULAR_WARNING_LEVEL("1");
                }
                else if("禁忌".equals(checkInfo.getWARNING_LEVEL()) || "禁用".equals(checkInfo.getWARNING_LEVEL())){
                    checkInfo.setREGULAR_WARNING_LEVEL("2");
                }
                else if("强制阻断".equals(checkInfo.getWARNING_LEVEL())){
                    checkInfo.setREGULAR_WARNING_LEVEL("-1");
                }else {
                    checkInfo.setREGULAR_WARNING_LEVEL("0");
                }
                checkInfos.add(checkInfo);
            }
            prescInfo.setCheckInfos(checkInfos);
            prescInfos.add(prescInfo);
        }
        checkPresOutput.setPrescInfos(prescInfos);

        Object checkPharmacistObject = root.getChild("CheckPharmacist");
        Element checkPharmacistElement = (Element) checkPharmacistObject;
        checkPharmacist.setPharmacistCheck(checkPharmacistElement.getValue().replaceAll("\\t","<br/>"));
    }


    //解析药师站传入的xml
    public CheckPresInput parseInputXml(String xml){
        CheckPresInput checkPresInput = new CheckPresInput();
        Document document = null;
        try {
            document = XmlUtil.readDocumentFromStr(xml.trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Element root = document.getRootElement();

        Object doctorObject = root.getChild("Doctor");
        Element doctorElement = (Element) doctorObject;
        Doctor doctor = new Doctor();
        doctor.setPOSITION(doctorElement.getAttributeValue("POSITION"));
        doctor.setNAME(doctorElement.getAttributeValue("NAME"));
        doctor.setUSER_ID(doctorElement.getAttributeValue("USER_ID"));
        doctor.setDEPT_NAME(doctorElement.getAttributeValue("DEPT_NAME"));
        doctor.setDEPT_CODE(doctorElement.getAttributeValue("DEPT_CODE"));
        checkPresInput.setDoctor(doctor);

        Object patientObject = root.getChild("Patient");
        Element patientElement = (Element) patientObject;
        Patient patient = new Patient();
        patient.setNAME(patientElement.getAttributeValue("NAME"));
        patient.setID(patientElement.getAttributeValue("ID"));
        patient.setVISIT_ID(patientElement.getAttributeValue("VISIT_ID"));
        patient.setPATIENT_PRES_ID(patientElement.getAttributeValue("PATIENT_PRES_ID"));
        patient.setBIRTH(patientElement.getAttributeValue("BIRTH"));
        patient.setHEIGHT(patientElement.getAttributeValue("HEIGHT"));
        patient.setWEIGHT(patientElement.getAttributeValue("WEIGHT"));
        patient.setGENDER(patientElement.getAttributeValue("GENDER"));
        patient.setPREGNANT(patientElement.getAttributeValue("PREGNANT"));
        patient.setLACT(patientElement.getAttributeValue("LACT"));
        patient.setHEPATICAL(patientElement.getAttributeValue("HEPATICAL"));
        patient.setRENAL(patientElement.getAttributeValue("RENAL"));
        patient.setPANCREAS(patientElement.getAttributeValue("PANCREAS"));
        patient.setALERGY_DRUGS(patientElement.getAttributeValue("ALERGY_DRUGS"));
        patient.setIDENTITY_TYPE(patientElement.getAttributeValue("IDENTITY_TYPE"));
        patient.setFEE_TYPE(patientElement.getAttributeValue("FEE_TYPE"));
        patient.setSCR(patientElement.getAttributeValue("SCR"));
        patient.setSCR_UNIT(patientElement.getAttributeValue("SCR_UNIT"));
        patient.setGESTATION_AGE(patientElement.getAttributeValue("GESTATION_AGE"));
        patient.setPRETERM_BIRTH(patientElement.getAttributeValue("PRETERM_BIRTH"));
        patient.setDRUG_HISTORY(patientElement.getAttributeValue("DRUG_HISTORY"));
        patient.setFAMILY_DISEASE_HISTORY(patientElement.getAttributeValue("FAMILY_DISEASE_HISTORY"));
        patient.setGENETIC_DISEASE(patientElement.getAttributeValue("GENETIC_DISEASE"));
        patient.setMEDICARE_01(patientElement.getAttributeValue("MEDICARE_01"));
        patient.setMEDICARE_02(patientElement.getAttributeValue("MEDICARE_02"));
        patient.setMEDICARE_03(patientElement.getAttributeValue("MEDICARE_03"));
        patient.setMEDICARE_04(patientElement.getAttributeValue("MEDICARE_04"));
        patient.setMEDICARE_05(patientElement.getAttributeValue("MEDICARE_05"));
        checkPresInput.setPatient(patient);

        Object diagnosisObject = root.getChild("Diagnosises");
        Element diagnosisElement = (Element) diagnosisObject;
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setDIAGNOSISES(diagnosisElement.getAttributeValue("DIAGNOSISES"));
        checkPresInput.setDiagnosis(diagnosis);

        List<Advice> advices = new ArrayList<Advice>();
        Object advicesObject = root.getChild("Advices");
        Element advicesElement = (Element) advicesObject;
        for(Object adviceObject : advicesElement.getChildren("Advice")){
            Element adviceElement = (Element) adviceObject;
            Advice advice = new Advice();
            advice.setDRUG_LO_ID(adviceElement.getAttributeValue("DRUG_LO_ID"));
            advice.setDRUG_LO_NAME(adviceElement.getAttributeValue("DRUG_LO_NAME"));
            advice.setADMINISTRATION(adviceElement.getAttributeValue("ADMINISTRATION"));
            advice.setDOSAGE(adviceElement.getAttributeValue("DOSAGE"));
            advice.setDOSAGE_UNIT(adviceElement.getAttributeValue("DOSAGE_UNIT"));
            advice.setFREQ_COUNT(adviceElement.getAttributeValue("FREQ_COUNT"));
            advice.setFREQ_INTERVAL(adviceElement.getAttributeValue("FREQ_INTERVAL"));
            advice.setFREQ_INTERVAL_UNIT(adviceElement.getAttributeValue("FREQ_INTERVAL_UNIT"));
            advice.setSTART_DAY(adviceElement.getAttributeValue("START_DAY"));
            advice.setEND_DAY(adviceElement.getAttributeValue("END_DAY"));
            advice.setREPEAT(adviceElement.getAttributeValue("REPEAT"));
            advice.setORDER_NO(adviceElement.getAttributeValue("ORDER_NO"));
            advice.setORDER_SUB_NO(adviceElement.getAttributeValue("ORDER_SUB_NO"));
            advice.setDEPT_CODE(adviceElement.getAttributeValue("DEPT_CODE"));
            advice.setDOCTOR_NAME(adviceElement.getAttributeValue("DOCTOR_NAME"));
            advice.setTITLE(adviceElement.getAttributeValue("TITLE"));
            advice.setAUTHORITY_LEVELS(adviceElement.getAttributeValue("AUTHORITY_LEVELS"));
            advice.setALERT_LEVELS(adviceElement.getAttributeValue("ALERT_LEVELS"));
            advice.setGROUP_ID(adviceElement.getAttributeValue("GROUP_ID"));
            advice.setUSER_ID(adviceElement.getAttributeValue("USER_ID"));
            advice.setPRES_ID(adviceElement.getAttributeValue("PRES_ID"));
            advice.setPRES_DATE(adviceElement.getAttributeValue("PRES_DATE"));
            advice.setPRES_SEQ_ID(adviceElement.getAttributeValue("PRES_SEQ_ID"));
            advice.setPK_ORDER_NO(adviceElement.getAttributeValue("PK_ORDER_NO"));
            advice.setCOURSE(adviceElement.getAttributeValue("COURSE"));
            advice.setPKG_COUNT(adviceElement.getAttributeValue("PKG_COUNT"));
            advice.setPKG_UNIT(adviceElement.getAttributeValue("PKG_UNIT"));
            advice.setBAK_01(adviceElement.getAttributeValue("BAK_01"));
            advice.setBAK_02(adviceElement.getAttributeValue("BAK_02"));
            advice.setBAK_03(adviceElement.getAttributeValue("BAK_03"));
            advice.setBAK_04(adviceElement.getAttributeValue("BAK_04"));
            advice.setBAK_05(adviceElement.getAttributeValue("BAK_05"));
            advices.add(advice);
        }
        checkPresInput.setAdvices(advices);

        return checkPresInput;
    }

    //解析鄱阳药师站传入的xml
    public CheckPresInput parseInputXml_PY(String xml){
        CheckPresInput checkPresInput = new CheckPresInput();
        Document document = null;
        try {
            document = XmlUtil.readDocumentFromStr(xml.trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Element root = document.getRootElement();
        Patient patient = new Patient();
        patient.setID(root.getAttributeValue("PATIENT_ID"));
        patient.setVISIT_ID(root.getAttributeValue("VISIT_ID"));
        patient.setPATIENT_PRES_ID(root.getAttributeValue("PATIENT_PRES_ID"));
        checkPresInput.setPatient(patient);
        List<Advice> advices = new ArrayList<Advice>();
        Object advicesObject = root.getChild("Advices");
        Element advicesElement = (Element) advicesObject;
        for(Object adviceObject : advicesElement.getChildren("Advice")) {
            Element adviceElement = (Element) adviceObject;
            Advice advice = new Advice();
            advice.setPRES_ID(adviceElement.getAttributeValue("PRES_ID"));
            advice.setDRUG_LO_ID(adviceElement.getAttributeValue("DRUG_CODE"));
            advice.setORDER_NO(adviceElement.getAttributeValue("ORDER_NO"));
            advice.setORDER_SUB_NO(adviceElement.getAttributeValue("ORDER_SUB_NO"));
            advices.add(advice);
        }
        checkPresInput.setAdvices(advices);
        return checkPresInput;
    }

    //解析护士站xml
    public List<Check> parseNurseCheckXml(String xml){
        List<Check> res = new ArrayList<Check>();
        if(xml == null || xml.length() == 0){
            return res;
        }
        Document document = null;

        try {
            document = XmlUtil.readDocumentFromStr(xml.trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Element root = document.getRootElement();

        for(Object checkObject:root.getChildren("Check")) {
            Check check = new Check();
            CheckPresInput checkPresInput = new CheckPresInput();
            CheckPresOutput checkPresOutput = new CheckPresOutput();
            Element checkElement = (Element) checkObject;

            Object checkInputObject = checkElement.getChild("CheckInput");
            Element checkInputElement = (Element) checkInputObject;

            Object doctorObject = checkInputElement.getChild("Doctor");
            Element doctorElement = (Element) doctorObject;
            Doctor doctor = new Doctor();
            doctor.setNAME(doctorElement.getAttributeValue("NAME"));
            doctor.setPOSITION(doctorElement.getAttributeValue("POSITION"));
            doctor.setUSER_ID(doctorElement.getAttributeValue("USER_ID"));
            doctor.setDEPT_NAME(doctorElement.getAttributeValue("DEPT_NAME"));
            doctor.setDEPT_CODE(doctorElement.getAttributeValue("DEPT_CODE"));
            checkPresInput.setDoctor(doctor);

            Object patientObject = checkInputElement.getChild("Patient");
            Element patientElement = (Element) patientObject;
            Patient patient = new Patient();
            patient.setNAME(patientElement.getAttributeValue("NAME"));
            patient.setID(patientElement.getAttributeValue("ID"));
            patient.setVISIT_ID(patientElement.getAttributeValue("VISIT_ID"));
            patient.setPATIENT_PRES_ID(patientElement.getAttributeValue("PATIENT_PRES_ID"));
            patient.setBIRTH(patientElement.getAttributeValue("BIRTH"));
            patient.setHEIGHT(patientElement.getAttributeValue("HEIGHT"));
            patient.setWEIGHT(patientElement.getAttributeValue("WEIGHT"));
            patient.setGENDER(patientElement.getAttributeValue("GENDER"));
            patient.setPREGNANT(patientElement.getAttributeValue("PREGNANT"));
            patient.setLACT(patientElement.getAttributeValue("LACT"));
            patient.setHEPATICAL(patientElement.getAttributeValue("HEPATICAL"));
            patient.setRENAL(patientElement.getAttributeValue("RENAL"));
            patient.setPANCREAS(patientElement.getAttributeValue("PANCREAS"));
            patient.setALERGY_DRUGS(patientElement.getAttributeValue("ALERGY_DRUGS"));
            patient.setIDENTITY_TYPE(patientElement.getAttributeValue("IDENTITY_TYPE"));
            patient.setFEE_TYPE(patientElement.getAttributeValue("FEE_TYPE"));
            patient.setSCR(patientElement.getAttributeValue("SCR"));
            patient.setSCR_UNIT(patientElement.getAttributeValue("SCR_UNIT"));
            patient.setGESTATION_AGE(patientElement.getAttributeValue("GESTATION_AGE"));
            patient.setPRETERM_BIRTH(patientElement.getAttributeValue("PRETERM_BIRTH"));
            patient.setDRUG_HISTORY(patientElement.getAttributeValue("DRUG_HISTORY"));
            patient.setFAMILY_DISEASE_HISTORY(patientElement.getAttributeValue("FAMILY_DISEASE_HISTORY"));
            patient.setGENETIC_DISEASE(patientElement.getAttributeValue("GENETIC_DISEASE"));
            patient.setMEDICARE_01(patientElement.getAttributeValue("MEDICARE_01"));
            patient.setMEDICARE_02(patientElement.getAttributeValue("MEDICARE_02"));
            patient.setMEDICARE_03(patientElement.getAttributeValue("MEDICARE_03"));
            patient.setMEDICARE_04(patientElement.getAttributeValue("MEDICARE_04"));
            patient.setMEDICARE_05(patientElement.getAttributeValue("MEDICARE_05"));

            patient.setBED_NO(patientElement.getAttributeValue("BED_NO"));
            checkPresInput.setPatient(patient);

            Object diagnosisObject = checkInputElement.getChild("Diagnosises");
            Element diagnosisElement = (Element) diagnosisObject;
            Diagnosis diagnosis = new Diagnosis();
            diagnosis.setDIAGNOSISES(diagnosisElement.getAttributeValue("DIAGNOSISES"));
            checkPresInput.setDiagnosis(diagnosis);

            List<Advice> advices = new ArrayList<Advice>();
            Object advicesObject = checkInputElement.getChild("Advices");
            Element advicesElement = (Element) advicesObject;
            for (Object adviceObject : advicesElement.getChildren("Advice")) {
                Element adviceElement = (Element) adviceObject;
                Advice advice = new Advice();
                advice.setDRUG_LO_ID(adviceElement.getAttributeValue("DRUG_LO_ID"));
                advice.setDRUG_LO_NAME(adviceElement.getAttributeValue("DRUG_LO_NAME"));
                advice.setADMINISTRATION(adviceElement.getAttributeValue("ADMINISTRATION"));
                advice.setDOSAGE(adviceElement.getAttributeValue("DOSAGE"));
                advice.setDOSAGE_UNIT(adviceElement.getAttributeValue("DOSAGE_UNIT"));
                advice.setFREQ_COUNT(adviceElement.getAttributeValue("FREQ_COUNT"));
                advice.setFREQ_INTERVAL(adviceElement.getAttributeValue("FREQ_INTERVAL"));
                advice.setFREQ_INTERVAL_UNIT(adviceElement.getAttributeValue("FREQ_INTERVAL_UNIT"));
                advice.setSTART_DAY(adviceElement.getAttributeValue("START_DAY"));
                advice.setEND_DAY(adviceElement.getAttributeValue("END_DAY"));
                advice.setREPEAT(adviceElement.getAttributeValue("REPEAT"));
                advice.setORDER_NO(adviceElement.getAttributeValue("ORDER_NO"));
                advice.setORDER_SUB_NO(adviceElement.getAttributeValue("ORDER_SUB_NO"));
                advice.setDEPT_CODE(adviceElement.getAttributeValue("DEPT_CODE"));
                advice.setDOCTOR_NAME(adviceElement.getAttributeValue("DOCTOR_NAME"));
                advice.setTITLE(adviceElement.getAttributeValue("TITLE"));
                advice.setAUTHORITY_LEVELS(adviceElement.getAttributeValue("AUTHORITY_LEVELS"));
                advice.setALERT_LEVELS(adviceElement.getAttributeValue("ALERT_LEVELS"));
                advice.setGROUP_ID(adviceElement.getAttributeValue("GROUP_ID"));
                advice.setUSER_ID(adviceElement.getAttributeValue("USER_ID"));
                advice.setPRES_ID(adviceElement.getAttributeValue("PRES_ID"));
                advice.setPRES_DATE(adviceElement.getAttributeValue("PRES_DATE"));
                advice.setPRES_SEQ_ID(adviceElement.getAttributeValue("PRES_SEQ_ID"));
                advice.setPK_ORDER_NO(adviceElement.getAttributeValue("PK_ORDER_NO"));
                advice.setCOURSE(adviceElement.getAttributeValue("COURSE"));
                advice.setPKG_COUNT(adviceElement.getAttributeValue("PKG_COUNT"));
                advice.setPKG_UNIT(adviceElement.getAttributeValue("PKG_UNIT"));
                advice.setBAK_01(adviceElement.getAttributeValue("BAK_01"));
                advice.setBAK_02(adviceElement.getAttributeValue("BAK_02"));
                advice.setBAK_03(adviceElement.getAttributeValue("BAK_03"));
                advice.setBAK_04(adviceElement.getAttributeValue("BAK_04"));
                advice.setBAK_05(adviceElement.getAttributeValue("BAK_05"));

                advice.setENTER_DATE_TIME(adviceElement.getAttributeValue("ENTER_DATE_TIME"));
                advice.setSELF_DRUG(adviceElement.getAttributeValue("SELF_DRUG"));
                advices.add(advice);
            }
            checkPresInput.setAdvices(advices);


            Object checkOutputObject = checkElement.getChild("CheckOutput");
            Element checkOutputElement = (Element) checkOutputObject;

            List<PrescInfo> prescInfos = new ArrayList<PrescInfo>();
            for (Object presInfoObject : checkOutputElement.getChildren("PresInfo")) {
                Element presInfoElement = (Element) presInfoObject;
                PrescInfo prescInfo = new PrescInfo();
                prescInfo.setGroup_id(presInfoElement.getAttributeValue("GROUP_ID"));
                prescInfo.setOrder_id(presInfoElement.getAttributeValue("ORDER_ID"));
                prescInfo.setOrder_sub_id(presInfoElement.getAttributeValue("ORDER_SUB_ID"));
                prescInfo.setDrug_lo_id(presInfoElement.getAttributeValue("DRUG_LO_ID"));
                prescInfo.setDrug_lo_name(presInfoElement.getAttributeValue("DRUG_LO_NAME"));

                List<CheckInfo> checkInfos = new ArrayList<CheckInfo>();
                for (Object checkInfoObject : presInfoElement.getChildren("CheckInfo")) {
                    Element checkInfoElement = (Element) checkInfoObject;
                    CheckInfo checkInfo = new CheckInfo();
                    checkInfo.setCOLOR(checkInfoElement.getAttributeValue("COLOR"));
                    checkInfo.setNAME(checkInfoElement.getAttributeValue("NAME"));
                    checkInfo.setWARNING_LEVEL(checkInfoElement.getAttributeValue("WARNING_LEVEL"));
                    checkInfo.setWARNING_INFO(checkInfoElement.getAttributeValue("WARNING_INFO"));
                    checkInfo.setREF_SOURCE(checkInfoElement.getAttributeValue("REF_SOURCE"));
                    checkInfo.setYPMC(checkInfoElement.getAttributeValue("YPMC"));
                    checkInfo.setJSXX(checkInfoElement.getAttributeValue("JSXX"));
                    checkInfo.setZYJL(checkInfoElement.getAttributeValue("ZYJL"));
                    checkInfo.setTYSM(checkInfoElement.getAttributeValue("TYSM"));
                    checkInfo.setLCSY(checkInfoElement.getAttributeValue("LCSY"));
                    if ("慎用".equals(checkInfo.getWARNING_LEVEL())) {
                        checkInfo.setREGULAR_WARNING_LEVEL("1");
                    } else if ("禁忌".equals(checkInfo.getWARNING_LEVEL()) || "禁用".equals(checkInfo.getWARNING_LEVEL())) {
                        checkInfo.setREGULAR_WARNING_LEVEL("2");
                    } else if ("强制阻断".equals(checkInfo.getWARNING_LEVEL())) {
                        checkInfo.setREGULAR_WARNING_LEVEL("-1");
                    } else {
                        checkInfo.setREGULAR_WARNING_LEVEL("0");
                    }
                    checkInfos.add(checkInfo);
                }
                prescInfo.setCheckInfos(checkInfos);
                prescInfos.add(prescInfo);
            }
            checkPresOutput.setPrescInfos(prescInfos);
            check.setCheckPresInput(checkPresInput);
            check.setCheckPresOutput(checkPresOutput);
            res.add(check);
        }
        return res;
    }

    //解析药师信息xml
    public PharmacistInfo parsePharmacistInfo(String xml){
        Document document = null;
        try {
            document = XmlUtil.readDocumentFromStr(xml.trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Element root = document.getRootElement();
        Object pharmacistObject = root.getChild("PharmacistInfo");
        Element pharmacistElement = (Element) pharmacistObject;
        PharmacistInfo pharmacistInfo = new PharmacistInfo();
        pharmacistInfo.setPharmacist_id(pharmacistElement.getAttributeValue("PHARMACIST_ID"));
        pharmacistInfo.setPharmacist_name(pharmacistElement.getAttributeValue("PHARMACIST_NAME"));
        pharmacistInfo.setTelephone(pharmacistElement.getAttributeValue("TELEPHONE"));
        return pharmacistInfo;
    }


    //解析BZ接口审核结果xml
    public  List<Order>  parseXML_BZ(String xml){
        if(xml == null || xml.length() == 0){
            return null;
        }
        Document document = null;

        int index = xml.indexOf("OrderList");
        if(index != -1) {
            xml = xml.substring(index-1);
        }
        try {
            document = XmlUtil.readDocumentFromStr(xml.trim());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Element root = document.getRootElement();

        List<Order> orderList = new ArrayList<Order>();
        for(Object orderObject:root.getChildren("Order")) {
            Order order = new Order();
            Element orderElement = (Element) orderObject;
            order.setBED_NO(orderElement.getAttributeValue("BED_NO"));
            order.setPATIENT_ID(orderElement.getAttributeValue("PATIENT_ID"));
            order.setVISIT_ID(orderElement.getAttributeValue("VISIT_ID"));
            order.setPATIENT_NAME(orderElement.getAttributeValue("PATIENT_NAME"));
            order.setDOCTOR_ID(orderElement.getAttributeValue("DOCTOR_ID"));
            order.setDOCTOR_NAME(orderElement.getAttributeValue("DOCTOR_NAME"));
            order.setDRUG_NAME(orderElement.getAttributeValue("DRUG_NAME"));
            order.setDRUG_SPEC(orderElement.getAttributeValue("DRUG_SPEC"));
            order.setORDER_TYPE(orderElement.getAttributeValue("ORDER_TYPE"));
            order.setSTART_TIME(orderElement.getAttributeValue("START_TIME"));
            order.setADMINISTRATION(orderElement.getAttributeValue("ADMINISTRATION"));
            order.setFREQUENCY(orderElement.getAttributeValue("FREQUENCY"));
            order.setDOSAGE(orderElement.getAttributeValue("DOSAGE"));
            order.setUSE_TIME(orderElement.getAttributeValue("USE_TIME"));
            order.setPROBLEM_TYPE(orderElement.getAttributeValue("PROBLEM_TYPE"));
            order.setPROBLEM_LEVEL(orderElement.getAttributeValue("PROBLEM_LEVEL"));
            order.setDRUG_STATE(orderElement.getAttributeValue("DRUG_STATE"));
            order.setGROUP_ID(orderElement.getAttributeValue("GROUP_ID"));
            order.setINPATIENT_NO(orderElement.getAttributeValue("INPATIENT_NO"));


            Object presInfoObject = orderElement.getChild("PresInfo");
            Element presInfoElement = (Element) presInfoObject;
            PrescInfo prescInfo = new PrescInfo();
            prescInfo.setGroup_id(presInfoElement.getAttributeValue("GROUP_ID"));
            prescInfo.setOrder_id(presInfoElement.getAttributeValue("ORDER_ID"));
            prescInfo.setOrder_sub_id(presInfoElement.getAttributeValue("ORDER_SUB_ID"));
            prescInfo.setDrug_lo_id(presInfoElement.getAttributeValue("DRUG_LO_ID"));
            prescInfo.setDrug_lo_name(presInfoElement.getAttributeValue("DRUG_LO_NAME"));
            prescInfo.setGanyu_state(presInfoElement.getAttributeValue("GANYU_STATE"));
            prescInfo.setGanyu_info(presInfoElement.getAttributeValue("GANYU_INFO"));
            prescInfo.setMedication_flag(presInfoElement.getAttributeValue("MEDICATION_FLAG"));

            List<CheckInfo> checkInfos = new ArrayList<CheckInfo>();
            for (Object checkInfoObject : presInfoElement.getChildren("CheckInfo")) {
                Element checkInfoElement = (Element) checkInfoObject;
                CheckInfo checkInfo = new CheckInfo();
                checkInfo.setCOLOR(checkInfoElement.getAttributeValue("COLOR"));
                checkInfo.setNAME(checkInfoElement.getAttributeValue("NAME"));
                checkInfo.setWARNING_LEVEL(checkInfoElement.getAttributeValue("WARNING_LEVEL"));
                checkInfo.setWARNING_INFO(checkInfoElement.getAttributeValue("WARNING_INFO"));
                checkInfo.setREF_SOURCE(checkInfoElement.getAttributeValue("REF_SOURCE"));
                checkInfo.setYPMC(checkInfoElement.getAttributeValue("YPMC"));
                checkInfo.setJSXX(checkInfoElement.getAttributeValue("JSXX"));
                checkInfo.setZYJL(checkInfoElement.getAttributeValue("ZYJL"));
                checkInfo.setTYSM(checkInfoElement.getAttributeValue("TYSM"));
                checkInfo.setLCSY(checkInfoElement.getAttributeValue("LCSY"));
                if ("慎用".equals(checkInfo.getWARNING_LEVEL())) {
                    checkInfo.setREGULAR_WARNING_LEVEL("1");
                } else if ("禁忌".equals(checkInfo.getWARNING_LEVEL()) || "禁用".equals(checkInfo.getWARNING_LEVEL())) {
                    checkInfo.setREGULAR_WARNING_LEVEL("2");
                } else if ("强制阻断".equals(checkInfo.getWARNING_LEVEL())) {
                    checkInfo.setREGULAR_WARNING_LEVEL("-1");
                } else {
                    checkInfo.setREGULAR_WARNING_LEVEL("0");
                }
                checkInfos.add(checkInfo);
            }
            prescInfo.setCheckInfos(checkInfos);
            order.setPrescInfo(prescInfo);
            orderList.add(order);
        }

        return orderList;
    }


    //药师站获取最高警示级别
    public int getPharHighestWarningLevel(List<PrescInfo> prescInfos){
        int level = 0;
        for(PrescInfo prescInfo : prescInfos){
            if(prescInfo.getCheckInfos() != null && prescInfo.getCheckInfos().size() != 0){
                for(CheckInfo checkInfo : prescInfo.getCheckInfos()){
                    if("用药监测".equals(checkInfo.getNAME())){
                        continue;
                    }
                    if("慎用".equals(checkInfo.getWARNING_LEVEL()) && level == 0){
                        level = 1;
                    }else if(("禁忌".equals(checkInfo.getWARNING_LEVEL()) || "禁用".equals(checkInfo.getWARNING_LEVEL())) && (level == 0 || level == 1)){
                        level = 2;
                    }else if("拦截".equals(checkInfo.getWARNING_LEVEL())){
                        level = -1;
                    }
                }
            }
        }
        return level;
    }


    /**
     * add by Yikmat   2019.10.10
     * BZ药师站获取最高警示级别,问题类别
     */
    public ProblemInfo getPharHighestWarningLevel_BZ(PrescInfo prescInfo){
        ProblemInfo problemInfo = new ProblemInfo();
        int level = 0;
        String problem_type = "";
        String problem_level = "";
        if(prescInfo.getCheckInfos() != null && prescInfo.getCheckInfos().size() != 0){
            for(CheckInfo checkInfo : prescInfo.getCheckInfos()){
//                if("用药监测".equals(checkInfo.getNAME())){
//                    continue;
//                }else
                if(("慎用".equals(checkInfo.getWARNING_LEVEL())||"提示".equals(checkInfo.getWARNING_LEVEL())) && level == 0){
                    level = 1;
                    problem_type = checkInfo.getNAME();
                    problem_level = checkInfo.getWARNING_LEVEL();
                }else if(("禁忌".equals(checkInfo.getWARNING_LEVEL())||"禁用".equals(checkInfo.getWARNING_LEVEL())) && (level == 0 || level ==1)){
                    level = 2;
                    problem_type = checkInfo.getNAME();
                    problem_level = checkInfo.getWARNING_LEVEL();
                }else if(("强制阻断".equals(checkInfo.getWARNING_LEVEL()) || "强制登记".equals(checkInfo.getWARNING_LEVEL()))){
                    level = 3;
                    problem_type = checkInfo.getNAME();
                    problem_level = checkInfo.getWARNING_LEVEL();
                }
            }
        }
        problemInfo.setProblem_level(level);
        problemInfo.setProblem_level_name(problem_level);
        problemInfo.setProblem_type(problem_type);
        return problemInfo;
    }

    /**
     * add by Yikmat   2019.10.10
     * BZ药师站返回相应的数字
     */
    public int getRegularWarnLevel_BZ(String warningLevel) {
        int warn = 0;
        if ("提示".equals(warningLevel)) {
            warn = 1;
        } else if ("慎用".equals(warningLevel)) {
            warn = 1;
        } else if ("禁忌".equals(warningLevel)) {
            warn = 2;
        } else if ("禁用".equals(warningLevel)) {
            warn = 2;
        } else if ("强制阻断".equals(warningLevel)) {
            warn = 3;
        } else if ("强制登记".equals(warningLevel)) {
            warn = 3;
        }
        return warn;
    }

    public CheckPresInput getCheckPresInput() {
        return checkPresInput;
    }

    public void setCheckPresInput(CheckPresInput checkPresInput) {
        this.checkPresInput = checkPresInput;
    }

    public CheckPresOutput getCheckPresOutput() {
        return checkPresOutput;
    }

    public void setCheckPresOutput(CheckPresOutput checkPresOutput) {
        this.checkPresOutput = checkPresOutput;
    }

    public CheckPharmacist getCheckPharmacist() {
        return checkPharmacist;
    }

    public void setCheckPharmacist(CheckPharmacist checkPharmacist) {
        this.checkPharmacist = checkPharmacist;
    }


    public static void main(String[] args) {
        String xml = "<OrderList>\n" +
                "    <Order BED_NO=\"001\" PATIENT_ID=\"000358649700\" VISIT_ID=\"2\" PATIENT_NAME=\"孙健之次子\" DOCTOR_ID=\"10645\" DOCTOR_NAME=\"李秋菊\" DRUG_NAME=\"多种微量元素注射液\" DRUG_SPEC=\"10ml/支\" ORDER_TYPE=\"临时\" START_TIME=\"20190923\" ADMINISTRATION=\"静点\" FREQUENCY=\"ONCE\" DOSAGE=\"0.5ml\" USE_TIME=\"2019-07-26 15:59:00\" PROBLEM_TYPE=\"\" PROBLEM_LEVEL=\"\" DRUG_STATE=\"\" GROUP_ID=\"63384822\" INPATIENT_NO=\"413076\">\n" +
                "        <PresInfo ORDER_ID=\"63384830\" ORDER_SUB_ID=\"4\" GROUP_ID=\"63384822\" DRUG_LO_ID=\"20022700\" DRUG_LO_NAME=\"多种微量元素注射液\" GANYU_STATE=\"未干预\" GANYU_INFO=\"\"/>\n" +
                "    </Order>\n" +
                "    <Order BED_NO=\"001\" PATIENT_ID=\"000358649700\" VISIT_ID=\"2\" PATIENT_NAME=\"孙健之次子\" DOCTOR_ID=\"10645\" DOCTOR_NAME=\"李秋菊\" DRUG_NAME=\"脂溶性维生素注射液（Ⅱ）\" DRUG_SPEC=\"10ml/支\" ORDER_TYPE=\"临时\" START_TIME=\"20190923\" ADMINISTRATION=\"静点\" FREQUENCY=\"ONCE\" DOSAGE=\"2.5ml\" USE_TIME=\"2019-07-26 15:59:00\" PROBLEM_TYPE=\"\" PROBLEM_LEVEL=\"\" DRUG_STATE=\"\" GROUP_ID=\"63384822\" INPATIENT_NO=\"413076\">\n" +
                "        <PresInfo ORDER_ID=\"63384824\" ORDER_SUB_ID=\"1\" GROUP_ID=\"63384822\" DRUG_LO_ID=\"00196200\" DRUG_LO_NAME=\"脂溶性维生素注射液（Ⅱ）\" GANYU_STATE=\"未干预\" GANYU_INFO=\"\"/>\n" +
                "    </Order>\n" +
                "    <Order BED_NO=\"001\" PATIENT_ID=\"000358649700\" VISIT_ID=\"2\" PATIENT_NAME=\"孙健之次子\" DOCTOR_ID=\"10645\" DOCTOR_NAME=\"李秋菊\" DRUG_NAME=\"葡萄糖注射液\" DRUG_SPEC=\"50% 10g 20ml/支\" ORDER_TYPE=\"临时\" START_TIME=\"20190923\" ADMINISTRATION=\"静点\" FREQUENCY=\"ONCE\" DOSAGE=\"32ml\" USE_TIME=\"2019-07-26 15:59:00\" PROBLEM_TYPE=\"\" PROBLEM_LEVEL=\"\" DRUG_STATE=\"\" GROUP_ID=\"63384822\" INPATIENT_NO=\"413076\">\n" +
                "        <PresInfo ORDER_ID=\"63384835\" ORDER_SUB_ID=\"1\" GROUP_ID=\"63384822\" DRUG_LO_ID=\"20121100\" DRUG_LO_NAME=\"葡萄糖注射液\" GANYU_STATE=\"未干预\" GANYU_INFO=\"\"/>\n" +
                "    </Order>\n" +
                "    <Order BED_NO=\"001\" PATIENT_ID=\"000358649700\" VISIT_ID=\"2\" PATIENT_NAME=\"孙健之次子\" DOCTOR_ID=\"10645\" DOCTOR_NAME=\"李秋菊\" DRUG_NAME=\"小儿复方氨基酸注射液(18AA-I)\" DRUG_SPEC=\"6.74g:100ml/袋\" ORDER_TYPE=\"临时\" START_TIME=\"20190923\" ADMINISTRATION=\"静点\" FREQUENCY=\"ONCE\" DOSAGE=\"56ml\" USE_TIME=\"2019-07-26 15:59:00\" PROBLEM_TYPE=\"\" PROBLEM_LEVEL=\"\" DRUG_STATE=\"\" GROUP_ID=\"63384822\" INPATIENT_NO=\"413076\">\n" +
                "        <PresInfo ORDER_ID=\"63384826\" ORDER_SUB_ID=\"2\" GROUP_ID=\"63384822\" DRUG_LO_ID=\"20128200\" DRUG_LO_NAME=\"小儿复方氨基酸注射液(18AA-I)\" GANYU_STATE=\"未干预\" GANYU_INFO=\"\"/>\n" +
                "    </Order>\n" +
                "    <Order BED_NO=\"001\" PATIENT_ID=\"000358649700\" VISIT_ID=\"2\" PATIENT_NAME=\"孙健之次子\" DOCTOR_ID=\"10645\" DOCTOR_NAME=\"李秋菊\" DRUG_NAME=\"氯化钾注射液\" DRUG_SPEC=\"1.5g 10ml/支\" ORDER_TYPE=\"临时\" START_TIME=\"20190923\" ADMINISTRATION=\"静点\" FREQUENCY=\"ONCE\" DOSAGE=\"1.3ml\" USE_TIME=\"2019-07-26 15:59:00\" PROBLEM_TYPE=\"\" PROBLEM_LEVEL=\"\" DRUG_STATE=\"\" GROUP_ID=\"63384822\" INPATIENT_NO=\"413076\">\n" +
                "        <PresInfo ORDER_ID=\"63384835\" ORDER_SUB_ID=\"2\" GROUP_ID=\"63384822\" DRUG_LO_ID=\"20097100\" DRUG_LO_NAME=\"氯化钾注射液\" GANYU_STATE=\"未干预\" GANYU_INFO=\"\">\n" +
                "            <CheckInfo COLOR=\"黄色\" NAME=\"用法用量\" WARNING_LEVEL=\"慎用\" WARNING_INFO=\"单次剂量超限。药品成分氯化钾的浓度超限。规定的浓度上限为：3.4mg/ml。计算得到的浓度值为：6.0938mg/ml。组号为：63384822。【浓度】钾浓度不超过3.4g/L(45mmol/L)。\" REF_SOURCE=\"15%氯化钾注射液说明书-中国大冢制药有限公司\" YPMC=\"\" JSXX=\"\" ZYJL=\"\" TYSM=\"\" LCSY=\"\"/>\n" +
                "        </PresInfo>\n" +
                "    </Order>\n" +
                "    <Order BED_NO=\"001\" PATIENT_ID=\"000358649700\" VISIT_ID=\"2\" PATIENT_NAME=\"孙健之次子\" DOCTOR_ID=\"10645\" DOCTOR_NAME=\"李秋菊\" DRUG_NAME=\"中/长链脂肪乳注射液(C8-24)\" DRUG_SPEC=\"20% 250ml/瓶\" ORDER_TYPE=\"临时\" START_TIME=\"20190923\" ADMINISTRATION=\"静点\" FREQUENCY=\"ONCE\" DOSAGE=\"38ml\" USE_TIME=\"2019-07-26 15:59:00\" PROBLEM_TYPE=\"\" PROBLEM_LEVEL=\"\" DRUG_STATE=\"\" GROUP_ID=\"63384822\" INPATIENT_NO=\"413076\">\n" +
                "        <PresInfo ORDER_ID=\"63384822\" ORDER_SUB_ID=\"1\" GROUP_ID=\"63384822\" DRUG_LO_ID=\"00299600\" DRUG_LO_NAME=\"中/长链脂肪乳注射液(C8-24)\" GANYU_STATE=\"未干预\" GANYU_INFO=\"\">\n" +
                "            <CheckInfo COLOR=\"红色\" NAME=\"用法用量\" WARNING_LEVEL=\"强制阻断\" WARNING_INFO=\"使用频次超限。药品说明书规定的每日最大使用频次为1。处方的每日频次为2。医院用法用量中的规则编码：770\" REF_SOURCE=\"药品说明书及医院相关规定\" YPMC=\"\" JSXX=\"\" ZYJL=\"\" TYSM=\"\" LCSY=\"\"/>\n" +
                "        </PresInfo>\n" +
                "    </Order>\n" +
                "    <Order BED_NO=\"001\" PATIENT_ID=\"000358649700\" VISIT_ID=\"2\" PATIENT_NAME=\"孙健之次子\" DOCTOR_ID=\"10645\" DOCTOR_NAME=\"李秋菊\" DRUG_NAME=\"浓氯化钠注射液\" DRUG_SPEC=\"1g 10ml/支\" ORDER_TYPE=\"临时\" START_TIME=\"20190923\" ADMINISTRATION=\"静点\" FREQUENCY=\"ONCE\" DOSAGE=\"2.4ml\" USE_TIME=\"2019-07-26 15:59:00\" PROBLEM_TYPE=\"\" PROBLEM_LEVEL=\"\" DRUG_STATE=\"\" GROUP_ID=\"63384822\" INPATIENT_NO=\"413076\">\n" +
                "        <PresInfo ORDER_ID=\"63384832\" ORDER_SUB_ID=\"5\" GROUP_ID=\"63384822\" DRUG_LO_ID=\"20031400\" DRUG_LO_NAME=\"浓氯化钠注射液\" GANYU_STATE=\"未干预\" GANYU_INFO=\"\"/>\n" +
                "    </Order>\n" +
                "    <Order BED_NO=\"001\" PATIENT_ID=\"000358649700\" VISIT_ID=\"2\" PATIENT_NAME=\"孙健之次子\" DOCTOR_ID=\"10645\" DOCTOR_NAME=\"李秋菊\" DRUG_NAME=\"注射用水溶性维生素\" DRUG_SPEC=\"1瓶 /瓶\" ORDER_TYPE=\"临时\" START_TIME=\"20190923\" ADMINISTRATION=\"静点\" FREQUENCY=\"ONCE\" DOSAGE=\"0.05瓶\" USE_TIME=\"2019-07-26 15:59:00\" PROBLEM_TYPE=\"\" PROBLEM_LEVEL=\"\" DRUG_STATE=\"\" GROUP_ID=\"63384822\" INPATIENT_NO=\"413076\">\n" +
                "        <PresInfo ORDER_ID=\"63384828\" ORDER_SUB_ID=\"3\" GROUP_ID=\"63384822\" DRUG_LO_ID=\"20078000\" DRUG_LO_NAME=\"注射用水溶性维生素\" GANYU_STATE=\"未干预\" GANYU_INFO=\"\"/>\n" +
                "    </Order>\n" +
                "    <Order BED_NO=\"222\" PATIENT_ID=\"1000710510\" VISIT_ID=\"1\" PATIENT_NAME=\"繁蓓\" DOCTOR_ID=\"0465\" DOCTOR_NAME=\"俞雨生\" DRUG_NAME=\"培哚普利叔丁胺片☆(施维雅)[市公乙]\" DRUG_SPEC=\"8mg\" ORDER_TYPE=\"临时\" START_TIME=\"20190926\" ADMINISTRATION=\"口服\" FREQUENCY=\"2/日\" DOSAGE=\"8mg\" USE_TIME=\"2019-07-26 15:59:00\" PROBLEM_TYPE=\"\" PROBLEM_LEVEL=\"\" DRUG_STATE=\"\" GROUP_ID=\"01569458160820694600\" INPATIENT_NO=\"111\">\n" +
                "        <PresInfo ORDER_ID=\"01569458160820694600\" ORDER_SUB_ID=\"1\" GROUP_ID=\"01569458160820694600\" DRUG_LO_ID=\"1504724TA1\" DRUG_LO_NAME=\"培哚普利叔丁胺片☆(施维雅)[市公乙]\" GANYU_STATE=\"未干预\" GANYU_INFO=\"\">\n" +
                "            <CheckInfo COLOR=\"黄色\" NAME=\"用法用量\" WARNING_LEVEL=\"慎用\" WARNING_INFO=\"单日极量超限。药品说明书规定的单日极量为8mg。处方单次剂量为8mg，给药频次为：2次每1日。医院用法用量中的规则编码：770\" REF_SOURCE=\"药品说明书及医院相关规定\" YPMC=\"\" JSXX=\"\" ZYJL=\"\" TYSM=\"\" LCSY=\"\"/>\n" +
                "            <CheckInfo COLOR=\"红色\" NAME=\"用法用量\" WARNING_LEVEL=\"强制阻断\" WARNING_INFO=\"使用频次超限。药品说明书规定的每日最大使用频次为1。处方的每日频次为2。医院用法用量中的规则编码：770\" REF_SOURCE=\"药品说明书及医院相关规定\" YPMC=\"\" JSXX=\"\" ZYJL=\"\" TYSM=\"\" LCSY=\"\"/>\n" +
                "        </PresInfo>\n" +
                "    </Order>\n" +
                "    <Order BED_NO=\"222\" PATIENT_ID=\"1000710510\" VISIT_ID=\"1\" PATIENT_NAME=\"繁蓓\" DOCTOR_ID=\"0465\" DOCTOR_NAME=\"俞雨生\" DRUG_NAME=\"新养肾丸☆[公.1][乙10]\" DRUG_SPEC=\"60g*60\" ORDER_TYPE=\"临时\" START_TIME=\"20190926\" ADMINISTRATION=\"口服\" FREQUENCY=\"2/日\" DOSAGE=\"3g\" USE_TIME=\"2019-07-26 15:59:00\" PROBLEM_TYPE=\"\" PROBLEM_LEVEL=\"\" DRUG_STATE=\"\" GROUP_ID=\"01569458160820693912\" INPATIENT_NO=\"111\">\n" +
                "        <PresInfo ORDER_ID=\"01569458160820693912\" ORDER_SUB_ID=\"1\" GROUP_ID=\"01569458160820693912\" DRUG_LO_ID=\"3050326PL1\" DRUG_LO_NAME=\"新养肾丸☆[公.1][乙10]\" GANYU_STATE=\"未干预\" GANYU_INFO=\"\"/>\n" +
                "    </Order>\n" +
                "</OrderList>";
        ;

        String xml_py = "<CheckPatient PATIENT_ID=\"2019043813\" VISIT_ID=\"1\" PATIENT_PRES_ID=\"2019043812_1\">\n" +
                "    <Advices>\n" +
                "\t  <Advice PRES_ID=\"2019043812_1\" DRUG_CODE=\"Y000027405\" ORDER_NO=\"1\" ORDER_SUB_NO=\"1\" />\n" +
                "\t  <Advice PRES_ID=\"2019043812_1\" DRUG_CODE=\"Y000027289\" ORDER_NO=\"1\" ORDER_SUB_NO=\"2\" />\n" +
                "\t  <Advice PRES_ID=\"2019043812_1\" DRUG_CODE=\"Y000028436\" ORDER_NO=\"2\" ORDER_SUB_NO=\"1\" />\n" +
                "\t  <Advice PRES_ID=\"2019043812_1\" DRUG_CODE=\"Y000024064\" ORDER_NO=\"2\" ORDER_SUB_NO=\"2\" />\n" +
                "    </Advices>\n" +
                "</CheckPatient>";
        new ParseXML().parseInputXml_PY(xml_py);
//        new PharmacistPrescCheckService().handleCheckXml_BZ(new ParseXML().parseXML_BZ(xml));
    }
}
