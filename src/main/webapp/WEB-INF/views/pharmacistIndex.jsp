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
                    <input type="text" id="patientID" name="patientID" value="0000282461">
                    <br>
                    <label>日期</label>
                    <br>
                    <input type="text" id="visitDate" name="visitDate" value="20180818">
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
                            &lt;CheckInput TAG="2"&gt;
                                &lt;Doctor POSITION="主治医师" NAME="陈四八" DEPT_CODE="JZKMZ" DEPT_NAME="急诊科门诊" USER_ID="2133"/&gt;
                                &lt;Patient NAME="许辰铭" ID="0000282461" GENDER="男" BIRTH="20120825" WEIGHT="" HEIGHT="" ALERGY_DRUGS="" PREGNANT="" LACT="" HEPATICAL="" RENAL="" PANCREAS="" VISIT_ID="" PATIENT_PRES_ID="607650" IDENTITY_TYPE="" FEE_TYPE="农合" SCR="" SCR_UNIT="" GESTATION_AGE="" PRETERM_BIRTH="" DRUG_HISTORY="" FAMILY_DISEASE_HISTORY="" GENETIC_DISEASE="" MEDICARE_01="" MEDICARE_02="" MEDICARE_03="" MEDICARE_04="" MEDICARE_05="" WARD_CODE="" WARD_NAME="" BED_NO="" INPATIENT_NO=""/&gt;
                                &lt;Diagnosises DIAGNOSISES="急性胃肠炎"/&gt;
                                &lt;Advices&gt;
                                    &lt;Advice REPEAT="" DRUG_LO_NAME="大黄碳酸氢钠片[1片×100片/瓶]" DRUG_LO_ID="0689" ADMINISTRATION="口服" DOSAGE="1" DOSAGE_UNIT="片" FREQ_COUNT="tid" FREQ_INTERVAL="" FREQ_INTERVAL_UNIT="" START_DAY="20180817" END_DAY="" DEPT_CODE="JZKMZ" DOCTOR_NAME="陈四八" ORDER_NO="E180817000012" ORDER_SUB_NO="1" AUTHORITY_LEVELS="" ALERT_LEVELS="" TITLE="主治医师" GROUP_ID="E180817000012" USER_ID="2133" PRES_ID="E18081700001" PRES_DATE="20180817" PRES_SEQ_ID="E18081700001" PK_ORDER_NO="" COURSE="1" PKG_COUNT="3" PKG_UNIT="片" BAK_01="" BAK_02="否" BAK_03="片剂" BAK_04="1片×100片/瓶" BAK_05="福州海王福药" PERFORM_SCHEDULE=""/&gt;
                                    &lt;Advice REPEAT="" DRUG_LO_NAME="艾司奥美拉唑钠注射液(进口)[40mg/支]" DRUG_LO_ID="257A" ADMINISTRATION="ivgtt" DOSAGE="40" DOSAGE_UNIT="mg" FREQ_COUNT="qd" FREQ_INTERVAL="" FREQ_INTERVAL_UNIT="" START_DAY="20180817" END_DAY="" DEPT_CODE="JZKMZ" DOCTOR_NAME="陈四八" ORDER_NO="E180817000023" ORDER_SUB_NO="1" AUTHORITY_LEVELS="" ALERT_LEVELS="" TITLE="主治医师" GROUP_ID="E180817000023" USER_ID="2133" PRES_ID="E18081700002" PRES_DATE="20180817" PRES_SEQ_ID="E18081700002" PK_ORDER_NO="" COURSE="1" PKG_COUNT="1" PKG_UNIT="支(1支)" BAK_01="" BAK_02="否" BAK_03="针剂" BAK_04="40mg/支" BAK_05="阿斯利康制药制药有限公司" PERFORM_SCHEDULE=""/&gt;
                                    &lt;Advice REPEAT="" DRUG_LO_NAME="维生素A.D滴剂（胶囊型）[1500单位×50粒/盒]" DRUG_LO_ID="2C0C" ADMINISTRATION="口服" DOSAGE="1500" DOSAGE_UNIT="单位" FREQ_COUNT="bid" FREQ_INTERVAL="" FREQ_INTERVAL_UNIT="" START_DAY="20180817" END_DAY="" DEPT_CODE="JZKMZ" DOCTOR_NAME="陈四八" ORDER_NO="E180817000034" ORDER_SUB_NO="1" AUTHORITY_LEVELS="" ALERT_LEVELS="" TITLE="主治医师" GROUP_ID="E180817000034" USER_ID="2133" PRES_ID="E18081700003" PRES_DATE="20180817" PRES_SEQ_ID="E18081700003" PK_ORDER_NO="" COURSE="1" PKG_COUNT="2" PKG_UNIT="粒" BAK_01="" BAK_02="否" BAK_03="胶囊剂" BAK_04="1500单位×50粒/盒" BAK_05="青岛双鲸药业有限公司" PERFORM_SCHEDULE=""/&gt;
                                &lt;/Advices&gt;
                            &lt;/CheckInput&gt;
                    </textarea>
                </form>
                <p>
                    <button onclick="testPharmacistCheck_CP()" class="btn btn-primary btn-large">检查 »</button>
                    <button onclick="testPharmacistCheckSilent_CP()" class="btn btn-primary btn-large">静默检查 »</button>
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

<%--<script type="text/javascript" src="${pageContext.servletContext.contextPath}/js/dcdt_IE.js"></script>--%>
<script type="text/javascript" src="${pageContext.servletContext.contextPath}/js/dcdt_IE.js"></script>
<script type="text/javascript" src="${pageContext.servletContext.contextPath}/js/jquery.min.js"></script>
<%--<script type="text/javascript"--%>
<%--src="http://223.3.71.149:8081/${pageContext.servletContext.contextPath}/js/jquery.min.js"></script>--%>


</html>
