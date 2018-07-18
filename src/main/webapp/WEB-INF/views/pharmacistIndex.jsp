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
                    <input type="text" id="patientID" name="patientID" value="1">
                    <br>
                    <label>日期</label>
                    <br>
                    <input type="text" id="visitDate" name="visitDate" value="20180326">
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
                                &lt;Doctor POSITION="副主任医师" NAME="门急诊医生" DEPT_CODE="030401" DEPT_NAME="骨科" USER_ID="123" /&gt;
                                &lt;Patient NAME="门急诊病人1" ID="1" GENDER="男" BIRTH="19840908" WEIGHT="20" HEIGHT="100" ALERGY_DRUGS="选择性5-HT3受体抑制药类" PREGNANT="是" LACT="是" HEPATICAL="是" RENAL="否" PANCREAS="否" VISIT_ID="1233" PATIENT_PRES_ID="1" IDENTITY_TYPE="军人" FEE_TYPE="医保" WARD_CODE="11" WARD_NAME="病区22" BED_NO="1" /&gt;
                                &lt;Diagnosises DIAGNOSISES="_感染" />
                                &lt;Advices>
                                    &lt;Advice DRUG_LO_ID="0101003CP0" DRUG_LO_NAME="阿莫西林胶囊" ADMINISTRATION="静滴" DOSAGE="5" DOSAGE_UNIT="粒" FREQ_COUNT="1" FREQ_INTERVAL="1" FREQ_INTERVAL_UNIT="日" START_DAY="20150114" END_DAY="20150114" REPEAT="0" ORDER_NO="1" ORDER_SUB_NO="2" DEPT_CODE="2426" DOCTOR_NAME="闫洪生" TITLE="副主任医师" AUTHORITY_LEVELS="" ALERT_LEVELS="1,2" GROUP_ID="" USER_ID="123" PRES_ID="1" PRES_DATE="20150114" PRES_SEQ_ID="3138829120150114" PK_ORDER_NO="" COURSE="" PKG_COUNT="101" PKG_UNIT="粒" BAK_01="" BAK_02="" BAK_03="胶囊" BAK_04="0.25g" BAK_05="昆明贝克诺" SELF_DRUG="自" DOCTOR_COMMENT="多吃" /&gt;

                                    &lt;Advice DRUG_LO_ID="3012302CP0" DRUG_LO_NAME="安替可胶囊" ADMINISTRATION="" DOSAGE="100" DOSAGE_UNIT="" FREQ_COUNT="5" FREQ_INTERVAL="1" FREQ_INTERVAL_UNIT="日" START_DAY="20150114" END_DAY="20150114" REPEAT="0" ORDER_NO="6" ORDER_SUB_NO="1" DEPT_CODE="骨科" DOCTOR_NAME="朱宏勋" TITLE="主任医师" AUTHORITY_LEVELS="" ALERT_LEVELS="1,2" GROUP_ID="" USER_ID="009284" PRES_ID="1" PRES_DATE="20150114" PRES_SEQ_ID="3138829120150114" PK_ORDER_NO="" COURSE="" PKG_COUNT="2" PKG_UNIT="" BAK_01="" BAK_02="" BAK_03="" BAK_04="0.22g" BAK_05="" SELF_DRUG="自" DOCTOR_COMMENT="多吃" /&gt;
                                &lt;/Advices&gt;
                            &lt;/CheckInput&gt;
                    </textarea>
                </form>
                <p>
                    <button onclick="testPharmacistCheck(1);" class="btn btn-primary btn-large">检查 »</button>
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

<script type="text/javascript" src="${pageContext.servletContext.contextPath}/js/dcdt.js"></script>
<%--<script type="text/javascript" src="${pageContext.servletContext.contextPath}/js/dcdt_chrome.js"></script>--%>
<%--<script type="text/javascript"--%>
<%--src="http://223.3.71.149:8081/${pageContext.servletContext.contextPath}/js/jquery.min.js"></script>--%>


</html>
