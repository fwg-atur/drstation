<%--
  Created by IntelliJ IDEA.
  User: sima
  Date: 2017/11/27
  Time: 10:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="http://localhost:80/DCStation/css/checkResultPage.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.servletContext.contextPath}/js/jquery.min.js"></script>
    <title>
        医生反馈
    </title>
</head>
<body>
<div>
    <div>
        <div class="main-content">
            <div class="head-top">
                <%--<p></p>--%>
            </div>
            <div class="head-info-lg">
                <p>医生反馈</p>
            </div>
            <div class="info-area">
                <div class="detail-info left-area" >
                    <div class="head-info-sm"><p>医生信息</p></div>
                    <div class="detail-content" style="padding-top: 20px">
                        <table id="doctor_table">
                            <tr>
                                <td><b>医生姓名：</b></td><td id="docotor_name">${checkResult.doctor.NAME}</td>
                                <td><b>医生ID:</b></td><td id="docotor_id">${checkResult.doctor.USER_ID}</td>
                            </tr>
                            <tr>
                                <td><b>医生职称：</b></td><td id="docotor_title">${checkResult.doctor.POSITION}</td>
                            </tr>
                            <tr>
                                <td><b>科室名称:</b></td><td id="dept_name">${checkResult.doctor.DEPT_NAME}</td>
                                <td><b>科室ID:</b></td><td id="dept_id">${checkResult.doctor.DEPT_CODE}</td>
                            </tr>

                        </table>
                    </div>
                </div>
                <div class="detail-info right-area">
                    <div class="head-info-sm"><p>患者信息</p></div>
                    <div class="detail-content" style="padding-top: 20px">
                        <table id="patient_table">
                            <tr>
                                <td><b>患者姓名：</b></td><td>${checkResult.patient.NAME}</td>
                                <td><b>患者ID:</b></td><td>${checkResult.patient.ID}</td>
                            </tr>
                            <tr>
                                <td><b>门诊号:</b></td><td colspan="2">${checkResult.patient.PATIENT_PRES_ID}</td>
                            </tr>
                            <tr>
                                <td><b>住院次数:</b></td><td>${checkResult.patient.VISIT_ID}</td>
                            </tr>
                            <tr>
                                <td><b>主医嘱号:</b></td><td>${checkResult.advices[0].ORDER_NO}</td>
                                <td><b>子医嘱号:</b></td><td>${checkResult.advices[0].ORDER_SUB_NO}</td>
                            </tr>
                            <tr>
                                <td><b>诊断名称：</b></td><td  colspan="2">${checkResult.diagnosis.DIAGNOSISES}</td>
                            </tr>

                        </table>
                    </div>
                </div>
                <div class="detail-info left-area" >
                    <div class="head-info-sm"><p>提示信息</p></div>
                    <div class="detail-content" style="padding-top: 20px; overflow:hidden;">
                        <table id="info_table">
                            <tr>
                                <td><b>药品名称：</b></td><td>${checkResult.advices[0].DRUG_LO_NAME}</td>
                                <td><b>药品ID：</b></td><td>${checkResult.advices[0].DRUG_LO_ID}</td>
                            </tr>
                            <tr>
                                <td><b>问题类型：</b></td><td>${checkResult.advices[0].checkInfoList[0].NAME}</td>
                                <td><b>严重程度：</b></td><td>${checkResult.advices[0].checkInfoList[0].WARNING_LEVEL}</td>
                            </tr>
                            <tr>
                                <td><b>警示信息：</b></td>
                            </tr>
                            <tr>
                                <td colspan="4">${checkResult.advices[0].checkInfoList[0].WARNING_INFO}</td>
                            </tr>
                            <tr>
                                <td><b>参考资料：</b></td>
                            </tr>
                            <tr>
                                <td colspan="4" >${checkResult.advices[0].checkInfoList[0].REF_SOURCE}</td>
                            </tr>
                        </table>
                    </div>
                </div>
                <div class="detail-info right-area">
                    <div class="head-info-sm"><p>反馈信息</p></div>
                    <div class="detail-content" style="padding-top: 20px;">
                        <table id="appeal_table">
                            <tr>
                                <td><b>意见与建议：</b></td>
                            </tr>
                            <tr>
                                <td colspan="5"><textarea id="suggestion4appeal" style="width: 100%"></textarea></td>
                            </tr>
                            <tr>
                                <td><b>参考依据:</b></td><td colspan="4"><select id="sel4evi" style="width: 60%"><option value="0">文献</option><option value="1">指南</option> </select></td>
                            </tr>
                            <tr>
                                <td><b>文献名称:</b></td><td colspan="4"><input id="input4doc"></td>
                            </tr>
                            <tr>
                                <td><b>重要程度:</b></td><td><select id="importanceSel"><option value="0">一般重要</option><option value="1">比较重要</option><option value="2">非常重要</option> </select></td>
                                <td>&nbsp;&nbsp;&nbsp;</td>
                                <td><b>可信度:</b></td><td><select id="reliabilitySel"><option value="0">一般可信</option><option value="1">比较可信</option><option value="2">非常可信</option> </select></td>
                            </tr>

                        </table>
                    </div>
                </div>
            </div>

            <div class="button-area" style="margin-top: 440px;">
                <input id="appeal" type="button" onclick="DoctorAppeal()" value="我要反馈"/>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <input id="back" type="button" onclick="window.close()" value="返回"/>
                &nbsp;&nbsp;&nbsp;&nbsp;
            </div>

        </div>
    </div>

</div>
</body>
<script type="text/javascript">
    function DoctorAppeal(){
        var doctor_table = document.getElementById("doctor_table")
        var doctor_name = doctor_table.rows[0].cells[1].innerHTML
        var doctor_id = doctor_table.rows[0].cells[3].innerHTML
        var doctor_title = doctor_table.rows[1].cells[1].innerHTML
        var dept_name = doctor_table.rows[2].cells[1].innerHTML
        var dept_id = doctor_table.rows[2].cells[3].innerHTML

        var patient_table = document.getElementById("patient_table")
        var patient_name = patient_table.rows[0].cells[1].innerHTML
        var patient_id = patient_table.rows[0].cells[3].innerHTML
        var patient_pres_id = patient_table.rows[1].cells[1].innerHTML
        var visit_id = patient_table.rows[2].cells[1].innerHTML
        var order_no = patient_table.rows[3].cells[1].innerHTML
        var order_sub_no = patient_table.rows[3].cells[3].innerHTML
        var diagnosis = patient_table.rows[4].cells[1].innerHTML

        var info_table = document.getElementById("info_table")
        var drug_lo_name = info_table.rows[0].cells[1].innerHTML
        var drug_lo_id = info_table.rows[0].cells[3].innerHTML
        var problem_type = info_table.rows[1].cells[1].innerHTML
        var problem_level = info_table.rows[1].cells[3].innerHTML
        var prompt = info_table.rows[3].cells[0].innerHTML

        var suggestion = document.getElementById("suggestion4appeal").value
        var refSel = document.getElementById("sel4evi")
        var ref = refSel.options[refSel.selectedIndex].text
        var ref_name = document.getElementById("input4doc").value
        var importanceSel = document.getElementById("importanceSel")
        var importance = importanceSel.options[importanceSel.selectedIndex].text
        var confidenceSel = document.getElementById("reliabilitySel")
        var confidence = confidenceSel.options[confidenceSel.selectedIndex].text

        var data = "<Request FUN='6'>" +
                " <Input DOCTOR_NAME='" +doctor_name+ "' DOCTOR_TITLE='" +doctor_title+ "' DOCTOR_ID='" +doctor_id+ "' DEPT_NAME='" +dept_name+ "' DEPT_ID='" +dept_id+ "'" +
                " PATIENT_NAME='" +patient_name+ "' PATIENT_ID='" +patient_id+ "' VISIT_ID='" +visit_id+ "' PATIENT_PRES_ID='" +patient_pres_id+ "' ORDER_ID='" +order_no+ "' ORDER_SUB_ID='" +order_sub_no+ "' DIAGNOSISES='" +diagnosis+ "' " +
                "DRUG_LO_NAME='" +drug_lo_name+ "' DRUG_LO_ID='" +drug_lo_id+ "' PROMPT='" +prompt+ "' PROBLEM_TYPE='" +problem_type+ "' PROBLEM_LEVEL='" +problem_level+ "'" +
                " IMPORTANCE='" +importance+ "' CONFIDENCE='" +confidence+ "' RESPONSE='" +suggestion+ "' REF='" +ref+ "' STATE='提出反馈' RESPOND_DATE='" +getNowFormatDate()+ "' REF_NAME='" +ref_name+ "' /> " +
                "</Request>"


        var xmlhttp;
        if (window.XMLHttpRequest) {
            //  IE7+, Firefox, Chrome, Opera, Safari
            xmlhttp = new XMLHttpRequest();
        } else {
            // IE6, IE5
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
        xmlhttp.onreadystatechange = function(){
            alert("您的反馈意见已成功提交，请耐心等候！")
            window.close()
        }
        xmlhttp.open("POST", "http://223.3.71.149:8081/service/api/mediator/ClientHelper.do", false);
        xmlhttp.setRequestHeader("Content-type", "text/plain;");
        xmlhttp.send(data);
    }

    function getNowFormatDate() {
        var date = new Date();
        var seperator1 = "-";
        var seperator2 = ":";
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
                + " " + date.getHours() + seperator2 + date.getMinutes()
                + seperator2 + date.getSeconds();
        return currentdate;
    }
</script>
</html>
