package com.dcdt.utils;

import com.dcdt.doctorstation.entity.*;
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

    //解析审核结果xml
    public void parseXML(String xml){
        if(xml == null || xml.length() == 0){
            return;
        }
        Document document = null;
        if(xml.length() > 38) {
            xml = xml.substring(38);
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
                    if("黄色".equals(checkInfo.getCOLOR())){
                        checkInfo.setREGULAR_WARNING_LEVEL("1");
                    }
                    else if("橙色".equals(checkInfo.getCOLOR())){
                        checkInfo.setREGULAR_WARNING_LEVEL("0");
                    }
                    else if("红色".equals(checkInfo.getCOLOR())){
                        checkInfo.setREGULAR_WARNING_LEVEL("-1");
                    }
                    checkInfos.add(checkInfo);
                }
                prescInfo.setCheckInfos(checkInfos);
                prescInfos.add(prescInfo);
            }
            checkPresOutput.setPrescInfos(prescInfos);

            Object checkPharmacistObject = checkElement.getChild("CheckPharmacist");
            Element checkPharmacistElement = (Element) checkPharmacistObject;
            checkPharmacist.setPharmacistCheck(checkPharmacistElement.getText());

        }
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
        doctor.setNAME(doctorElement.getAttributeValue("NAME"));
        doctor.setPOSITION(doctorElement.getAttributeValue("POSITION"));
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

    //药师站获取最高警示级别
    public int getPharHighestWarningLevel(List<PrescInfo> prescInfos){
        int level = 0;
        for(PrescInfo prescInfo : prescInfos){
            if(prescInfo.getCheckInfos() != null && prescInfo.getCheckInfos().size() != 0){
                for(CheckInfo checkInfo : prescInfo.getCheckInfos()){
                    if("慎用".equals(checkInfo.getWARNING_LEVEL()) && level == 0){
                        level = 1;
                    }else if("禁忌".equals(checkInfo.getWARNING_LEVEL()) && (level == 0 || level == 1)){
                        level = 2;
                    }else if("拦截".equals(checkInfo.getWARNING_LEVEL())){
                        level = -1;
                    }
                }
            }
        }
        return level;
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
}
