<%--
  Created by IntelliJ IDEA.
  User: wtwang
  Date: 2017/11/29
  Time: 9:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>
        实时审核Web版
    </title>
    <script type="text/javascript" src="${pageContext.servletContext.contextPath}/js/dcdt_IE.js"></script>
</head>
<body>
<div class="container-fluid">
    <div class="row-fluid">
        <div class="span12">
            <div class="page-header" contenteditable="true">
                <h1>实时审核Web版
                    <small> DCDT.XML</small>
                </h1>
            </div>
            <div class="hero-unit" contenteditable="true">
                <h3>

                </h3>
                <form id="queryForm" method="get">
                    <label>患者id</label>
                    <br>
                    <input type="text" id="patientID" name="patientID" value="000358649700">
                    <br>
                    <label>日期</label>
                    <br>
                    <input type="text" id="visitDate" name="visitDate" value="20191008">
                    <br>
                    <label>药师信息</label>
                    <br>
                    <textarea id="pharmacistInfo" name="pharmacistInfo" style="min-height: 100px;width: 100%">
                        &lt;Pharmacist&gt;
                            &lt;PharmacistInfo PHARMACIST_ID="1" PHARMACIST_NAME="药师" TELEPHONE="123" /&gt;
                        &lt;/Pharmacist&gt;
                    </textarea>
                    <label>数据字符串</label>
                    <br>
                    <textarea id="dcdt" name="dcdt" style="min-height: 300px;width: 100%">
                       <%--<CheckInput TAG="2">--%>
    <%--<Doctor NAME="李秋菊" POSITION="主治医师" USER_ID="10645" DEPT_NAME="儿二科" DEPT_CODE="1050400" />--%>
    <%--<Patient NAME="孙健之次子" ID="000358649700" VISIT_ID="1" PATIENT_PRES_ID="000358649700_1" BIRTH="20190916" HEIGHT="" WEIGHT="" GENDER="男" PREGNANT="" LACT="" HEPATICAL="" RENAL="" PANCREAS="" ALERGY_DRUGS="" IDENTITY_TYPE="" FEE_TYPE="自费" SCR="" SCR_UNIT="" GESTATION_AGE="" PRETERM_BIRTH="" DRUG_HISTORY="" FAMILY_DISEASE_HISTORY="" GENETIC_DISEASE="" MEDICARE_01="" MEDICARE_02="" MEDICARE_03="" MEDICARE_04="" MEDICARE_05="" WARD_CODE="1050401" WARD_NAME="儿二科新生儿病房" BED_NO="001" INPATIENT_NO="413076" PRE_OPERATION_NAME="" PRE_OPERATION_CODE="" OPERATION_NAME="" OPERATION_CODE="" ANAESTHESIA_NAME="" ANAESTHESIA_CODE="">--%>
        <%--<LIS_VALUES />--%>
    <%--</Patient>--%>
    <%--<Diagnosises DIAGNOSISES="" />--%>
    <%--<Advices>--%>
        <%--<Advice DRUG_LO_ID="00299600" DRUG_LO_NAME="中/长链脂肪乳注射液(C8-24)" ADMINISTRATION="静点" DOSAGE="38" DOSAGE_UNIT="ml" FREQ_COUNT="ONCE" FREQ_INTERVAL="" FREQ_INTERVAL_UNIT="" START_DAY="20190923" END_DAY="18991230" REPEAT="0" ORDER_NO="63384822" ORDER_SUB_NO="1" DEPT_CODE="1050400" DOCTOR_NAME="李秋菊" TITLE="主治医师" AUTHORITY_LEVELS="" ALERT_LEVELS="" GROUP_ID="63384822" USER_ID="10645" PRES_ID="6338482" PRES_DATE="20190923" PRES_SEQ_ID="63384822" PK_ORDER_NO="" COURSE="" PKG_COUNT="1" PKG_UNIT="瓶" BAK_01="" BAK_02="否" BAK_03="注射液" BAK_04="20% 250ml/瓶" BAK_05="广州百特侨光医疗用品有限公司" PERFORM_SCHEDULE="08:00" TIME="08:00" />--%>
        <%--<Advice DRUG_LO_ID="00196200" DRUG_LO_NAME="脂溶性维生素注射液（Ⅱ）" ADMINISTRATION="静点" DOSAGE="2.5" DOSAGE_UNIT="ml" FREQ_COUNT="ONCE" FREQ_INTERVAL="" FREQ_INTERVAL_UNIT="" START_DAY="20190923" END_DAY="18991230" REPEAT="0" ORDER_NO="63384824" ORDER_SUB_NO="1" DEPT_CODE="1050400" DOCTOR_NAME="李秋菊" TITLE="主治医师" AUTHORITY_LEVELS="" ALERT_LEVELS="" GROUP_ID="63384822" USER_ID="10645" PRES_ID="63384824" PRES_DATE="20190923" PRES_SEQ_ID="63384824" PK_ORDER_NO="" COURSE="" PKG_COUNT="1" PKG_UNIT="支" BAK_01="" BAK_02="否" BAK_03="注射液" BAK_04="10ml/支" BAK_05="华瑞制药有限公司" PERFORM_SCHEDULE="08:00" TIME="08:00" />--%>
        <%--<Advice DRUG_LO_ID="20128200" DRUG_LO_NAME="小儿复方氨基酸注射液(18AA-I)" ADMINISTRATION="静点" DOSAGE="56" DOSAGE_UNIT="ml" FREQ_COUNT="ONCE" FREQ_INTERVAL="" FREQ_INTERVAL_UNIT="" START_DAY="20190923" END_DAY="18991230" REPEAT="0" ORDER_NO="63384826" ORDER_SUB_NO="2" DEPT_CODE="1050400" DOCTOR_NAME="李秋菊" TITLE="主治医师" AUTHORITY_LEVELS="" ALERT_LEVELS="" GROUP_ID="63384822" USER_ID="10645" PRES_ID="63384826" PRES_DATE="20190923" PRES_SEQ_ID="63384826" PK_ORDER_NO="" COURSE="" PKG_COUNT="1" PKG_UNIT="袋" BAK_01="" BAK_02="否" BAK_03="注射液" BAK_04="6.74g:100ml/袋" BAK_05="辰欣药业股份有限公司" PERFORM_SCHEDULE="08:00" TIME="08:00" />--%>
        <%--<Advice DRUG_LO_ID="20078000" DRUG_LO_NAME="注射用水溶性维生素" ADMINISTRATION="静点" DOSAGE="0.05" DOSAGE_UNIT="瓶" FREQ_COUNT="ONCE" FREQ_INTERVAL="" FREQ_INTERVAL_UNIT="" START_DAY="20190923" END_DAY="18991230" REPEAT="0" ORDER_NO="63384828" ORDER_SUB_NO="3" DEPT_CODE="1050400" DOCTOR_NAME="李秋菊" TITLE="主治医师" AUTHORITY_LEVELS="" ALERT_LEVELS="" GROUP_ID="63384822" USER_ID="10645" PRES_ID="63384828" PRES_DATE="20190923" PRES_SEQ_ID="63384828" PK_ORDER_NO="" COURSE="" PKG_COUNT="1" PKG_UNIT="瓶" BAK_01="" BAK_02="否" BAK_03="粉针剂" BAK_04="1瓶 /瓶" BAK_05="华瑞制药有限公司" PERFORM_SCHEDULE="08:00" TIME="08:00" />--%>
        <%--<Advice DRUG_LO_ID="20022700" DRUG_LO_NAME="多种微量元素注射液" ADMINISTRATION="静点" DOSAGE="0.5" DOSAGE_UNIT="ml" FREQ_COUNT="ONCE" FREQ_INTERVAL="" FREQ_INTERVAL_UNIT="" START_DAY="20190923" END_DAY="18991230" REPEAT="0" ORDER_NO="63384830" ORDER_SUB_NO="4" DEPT_CODE="1050400" DOCTOR_NAME="李秋菊" TITLE="主治医师" AUTHORITY_LEVELS="" ALERT_LEVELS="" GROUP_ID="63384822" USER_ID="10645" PRES_ID="63384830" PRES_DATE="20190923" PRES_SEQ_ID="63384830" PK_ORDER_NO="" COURSE="" PKG_COUNT="1" PKG_UNIT="支" BAK_01="" BAK_02="否" BAK_03="注射液" BAK_04="10ml/支" BAK_05="华瑞制药有限公司" PERFORM_SCHEDULE="08:00" TIME="08:00" />--%>
        <%--<Advice DRUG_LO_ID="20031400" DRUG_LO_NAME="浓氯化钠注射液" ADMINISTRATION="静点" DOSAGE="2.4" DOSAGE_UNIT="ml" FREQ_COUNT="ONCE" FREQ_INTERVAL="" FREQ_INTERVAL_UNIT="" START_DAY="20190923" END_DAY="18991230" REPEAT="0" ORDER_NO="63384832" ORDER_SUB_NO="5" DEPT_CODE="1050400" DOCTOR_NAME="李秋菊" TITLE="主治医师" AUTHORITY_LEVELS="" ALERT_LEVELS="" GROUP_ID="63384822" USER_ID="10645" PRES_ID="63384832" PRES_DATE="20190923" PRES_SEQ_ID="63384832" PK_ORDER_NO="" COURSE="" PKG_COUNT="1" PKG_UNIT="支" BAK_01="" BAK_02="否" BAK_03="注射液" BAK_04="1g 10ml/支" BAK_05="国药集团容生制药有限公司" PERFORM_SCHEDULE="08:00" TIME="08:00" />--%>
        <%--<Advice DRUG_LO_ID="20121100" DRUG_LO_NAME="葡萄糖注射液" ADMINISTRATION="静点" DOSAGE="32" DOSAGE_UNIT="ml" FREQ_COUNT="ONCE" FREQ_INTERVAL="" FREQ_INTERVAL_UNIT="" START_DAY="20190923" END_DAY="18991230" REPEAT="0" ORDER_NO="63384835" ORDER_SUB_NO="6" DEPT_CODE="1050400" DOCTOR_NAME="李秋菊" TITLE="主治医师" AUTHORITY_LEVELS="" ALERT_LEVELS="" GROUP_ID="63384822" USER_ID="10645" PRES_ID="63384835" PRES_DATE="20190923" PRES_SEQ_ID="63384835" PK_ORDER_NO="" COURSE="" PKG_COUNT="2" PKG_UNIT="支" BAK_01="" BAK_02="否" BAK_03="注射液" BAK_04="50% 10g 20ml/支" BAK_05="大冢制药" PERFORM_SCHEDULE="08:00" TIME="08:00" />--%>
        <%--<Advice DRUG_LO_ID="20097100" DRUG_LO_NAME="氯化钾注射液" ADMINISTRATION="静点" DOSAGE="1.3" DOSAGE_UNIT="ml" FREQ_COUNT="ONCE" FREQ_INTERVAL="" FREQ_INTERVAL_UNIT="" START_DAY="20190923" END_DAY="18991230" REPEAT="0" ORDER_NO="63384836" ORDER_SUB_NO="7" DEPT_CODE="1050400" DOCTOR_NAME="李秋菊" TITLE="主治医师" AUTHORITY_LEVELS="" ALERT_LEVELS="" GROUP_ID="63384822" USER_ID="10645" PRES_ID="63384836" PRES_DATE="20190923" PRES_SEQ_ID="63384836" PK_ORDER_NO="" COURSE="" PKG_COUNT="1" PKG_UNIT="支" BAK_01="" BAK_02="否" BAK_03="注射液" BAK_04="1.5g 10ml/支" BAK_05="大冢制药" PERFORM_SCHEDULE="08:00" TIME="08:00" />--%>
    <%--</Advices>--%>
<%--</CheckInput>--%>
                            <OrderList>
	<Order PATIENT_ID="1000710510" VISIT_ID="1" ORDER_NO="01569458160820693912" ORDER_SUB_NO="1" GROUP_ID="01569458160820693912" DRUG_USE_TIME="2019-07-26 15:59:00" />
	<Order PATIENT_ID="1000710510" VISIT_ID="1" ORDER_NO="01569458160820694600" ORDER_SUB_NO="1" GROUP_ID="01569458160820694600" DRUG_USE_TIME="2019-07-26 15:59:00" />

	<Order PATIENT_ID="000358649700" VISIT_ID="2" ORDER_NO="63384822" ORDER_SUB_NO="1" GROUP_ID="63384822" DRUG_USE_TIME="2019-07-26 15:59:00" />
    <Order PATIENT_ID="000358649700" VISIT_ID="2" ORDER_NO="63384824" ORDER_SUB_NO="1" GROUP_ID="63384824" DRUG_USE_TIME="2019-07-26 15:59:00" />
    <Order PATIENT_ID="000358649700" VISIT_ID="2" ORDER_NO="63384826" ORDER_SUB_NO="2" GROUP_ID="63384826" DRUG_USE_TIME="2019-07-26 15:59:00" />
    <Order PATIENT_ID="000358649700" VISIT_ID="2" ORDER_NO="63384828" ORDER_SUB_NO="3" GROUP_ID="63384828" DRUG_USE_TIME="2019-07-26 15:59:00" />
    <Order PATIENT_ID="000358649700" VISIT_ID="2" ORDER_NO="63384830" ORDER_SUB_NO="4" GROUP_ID="63384830" DRUG_USE_TIME="2019-07-26 15:59:00" />
    <Order PATIENT_ID="000358649700" VISIT_ID="2" ORDER_NO="63384832" ORDER_SUB_NO="5" GROUP_ID="63384832" DRUG_USE_TIME="2019-07-26 15:59:00" />
    <Order PATIENT_ID="000358649700" VISIT_ID="2" ORDER_NO="63384835" ORDER_SUB_NO="1" GROUP_ID="63384835" DRUG_USE_TIME="2019-07-26 15:59:00" />
    <Order PATIENT_ID="000358649700" VISIT_ID="2" ORDER_NO="63384835" ORDER_SUB_NO="2" GROUP_ID="63384835" DRUG_USE_TIME="2019-07-26 15:59:00" />
</OrderList>




                    </textarea>
                </form>
                <p>
                    <button onclick="testPharmacistCheckBZ()" class="btn btn-primary btn-large">检查 »</button>
                    <button onclick="testPharmacistCheckSilent(1)" class="btn btn-primary btn-large">静默检查 »</button>
                    <%--<button onclick="testCheck(2);" class="btn btn-primary btn-large">提交处方 »</button>--%>
                </p>
            </div>

        </div>
    </div>
</div>


<script language="javascript" type="text/javascript">

</script>

<div id="checkResult">
    <%--<div id="bg">--%>
    <%--</div>--%>
    <%--<div id="show">--%>
    <%--&lt;%&ndash;测试&ndash;%&gt;--%>
    <%--&lt;%&ndash;<input id="btnclose" type="button" value="Close" onclick="hidediv();"/>&ndash;%&gt;--%>
    <%--<div>--%>
    <%--<iframe src="about:blank" name="left" height="650px" width="1000px"></iframe>--%>
    <%--</div>--%>
    <%--</div>--%>
    <%--<a id="checkResultButton"--%>
    <%--href="http://localhost:8080/DCStation/submit/checkResultPage?presId=000347828620150924000649&random=123"--%>
    <%--target="left">在左框中打开链接</a>--%>
</div>
</body>

<script type="text/javascript" src="${pageContext.servletContext.contextPath}/js/dcdt_IE.js"></script>
<%--<script type="text/javascript" src="${pageContext.servletContext.contextPath}/js/dcdt_Chrome.js"></script>--%>
<script type="text/javascript" src="${pageContext.servletContext.contextPath}/js/jquery.min.js"></script>
<%--<script type="text/javascript"--%>
<script type="text/javascript" src="${pageContext.servletContext.contextPath}/js/jquery.xdomainrequest.min.js"></script>


</html>
