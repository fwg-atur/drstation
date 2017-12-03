<%--
  Created by IntelliJ IDEA.
  User: sima
  Date: 2017/11/27
  Time: 10:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="http://localhost:8080/DCStation/css/checkResultPage.css" rel="stylesheet" type="text/css"/>
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
                        <table >
                            <tr>
                                <td><b>医生姓名：</b></td><td>${checkResult.doctor.NAME}</td>
                                <td><b>医生ID:</b></td><td>${checkResult.doctor.USER_ID}</td>
                            </tr>
                            <tr>
                                <td><b>医生职称：</b></td><td>${checkResult.doctor.POSITION}</td>
                            </tr>
                            <tr>
                                <td><b>科室名称:</b></td><td>${checkResult.doctor.DEPT_NAME}</td>
                                <td><b>科室ID:</b></td><td>${checkResult.doctor.DEPT_CODE}</td>
                            </tr>

                        </table>
                    </div>
                </div>
                <div class="detail-info right-area">
                    <div class="head-info-sm"><p>患者信息</p></div>
                    <div class="detail-content" style="padding-top: 20px">
                        <table >
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
                        <table >
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
                        <table >
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
                <input id="appeal" type="button" onclick="appeal('${presId}')" value="我要反馈"/>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <input id="back" type="button" onclick="window.close()" value="返回"/>
                &nbsp;&nbsp;&nbsp;&nbsp;
            </div>

        </div>
    </div>

</div>
</body>
</html>
