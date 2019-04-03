<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: wtwang
  Date: 2018/12/12
  Time: 11:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>实时审核Web版-护士站</title>
    <script type="text/javascript"
            src="http://${config.drStationServerIp}:${config.drStationServerPort}/DCStation/js/jquery.min.js"></script>
    <script type="text/javascript"
            src="http://${config.drStationServerIp}:${config.drStationServerPort}/DCStation/js/jquery.xdomainrequest.min.js"></script>
    <link href="http://${config.drStationServerIp}:${config.drStationServerPort}/DCStation/css/checkResultPage${config.browserFlag}.css"
          rel="stylesheet" type="text/css"/>
    <link href="http://${config.drStationServerIp}:${config.drStationServerPort}/DCStation/css/style.css"
          rel="stylesheet" type="text/css"/>
    <script type="text/javascript">
        var checkServerIp = '${config.drStationServerIp}';
        var checkServerPort = '${config.drStationServerPort}';
        var nurseCheckListJson = ${nurseCheckListJson};
        var nurseCheck = nurseCheckListJson[0];
        var advices = nurseCheck.advices;
        var advice = advices[0];

        /*
         * val:0下一步,-1返回修改
         * */
        function nextOrBack(val) {
            var url = "http://" + checkServerIp + ":" + checkServerPort + "/DCStation/nurseSubmit/setRetValue";
            var arg = 'presId=1&retVal=' + val;
            var xmlhttp;
            if (window.XMLHttpRequest) {
                //  IE7+, Firefox, Chrome, Opera, Safari
                xmlhttp = new XMLHttpRequest();
            } else {
                // IE6, IE5
                xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
            }
            xmlhttp.open("POST", url, false);
            xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded;");
            xmlhttp.send(arg);

//            clearAllInterval();
            window.close();
        }
    </script>
</head>
<body>
<div>
    <div>
        <div class="main-content">
            <div class="inner-main-content" style="overflow-x:hidden">
                <div class="head-top">
                    <div class="head-top left-head"></div>
                    <div class="head-top right-head">|&nbsp;&nbsp;&nbsp;&nbsp;合理用药实时审核Web端</div>
                </div>

                <div class="info-area">
                    <div class="detail-info left-area">
                        <div class="head-info-sm"><p>详细信息</p></div>
                        <div class="detail-content" style="padding: 10px;_padding:20px 10px;">
                            <table id="error_detail">

                            </table>
                        </div>
                    </div>
                    <div class="detail-info right-area">
                        <div class="head-info-sm">
                            <div class="head-info-sm-up>"></div>
                            <p>使用说明</p>
                            <div class="head-info-sm-down>"></div>
                        </div>
                        <div class="detail-content">
                            <table style="_margin-top: 50px">
                                <tr>
                                    <td>1、单击问题类型所在单元，可查看详细信息</td>
                                </tr>
                                <%--<tr>--%>
                                    <%--<td>2、<img--%>
                                            <%--src="http://${config.drStationServerIp}:${config.drStationServerPort}/DCStation/image/common-problem.png"/>--%>
                                        <%--&nbsp;一般警示 &nbsp;&nbsp;--%>
                                        <%--<img src="http://${config.drStationServerIp}:${config.drStationServerPort}/DCStation/image/serious-problem.png"/>--%>
                                        <%--&nbsp;严重警示&nbsp;&nbsp;--%>
                                        <%--<img src="http://${config.drStationServerIp}:${config.drStationServerPort}/DCStation/image/disaster-problem.png"/>--%>
                                        <%--&nbsp;必须处理--%>
                                    <%--</td>--%>
                                <%--</tr>--%>
                                <tr>
                                    <td>2、单击药品名称，可查看说明书</td>
                                </tr>
                                <tr>
                                    <td>问题反馈。电子邮箱：${config.email}。电话：${config.phoneNumber}</td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </div>

                <div class="table-area">
                    <div class="table-area-head">
                        <table class="main-table-head">
                            <thead>
                            <tr>
                                <th>床号</th>
                                <th>姓名</th>
                                <th>下达时间</th>
                                <th>严重程度</th>
                                <th>医嘱内容</th>
                                <th>长/临</th>
                                <th>自</th>
                                <th>剂量</th>
                                <th>单位</th>
                                <th>途径</th>
                                <th>频次</th>
                                <th>医生说明</th>
                                <th>问题类型</th>
                            </tr>
                            </thead>
                        </table>
                    </div>
                    <div class="table-area-content">
                        <table class="main-table">
                            <tbody>
                            <c:forEach var="check" items="${nurseCheckList}">
                                <c:forEach var="advice" items="${check.advices}">
                                    <tr>
                                        <td>${check.patient.BED_NO}</td>
                                        <td>${check.patient.NAME}</td>
                                        <td>${advice.ENTER_DATE_TIME}</td>
                                        <td>${advice.checkInfoList[0].WARNING_LEVEL}</td>
                                        <td>${advice.DRUG_LO_NAME}</td>
                                        <td>${advice.REPEAT}</td>
                                        <td>${advice.SELF_DRUG}</td>
                                        <td>${advice.DOSAGE}</td>
                                        <td>${advice.DOSAGE_UNIT}</td>
                                        <td>${advice.ADMINISTRATION}</td>
                                        <td>${advice.FREQ_COUNT}</td>
                                        <td>${advice.DOCTOR_NAME}</td>
                                        <td><a onclick="openProblemDetail('${advice.checkInfoList[0].NAME}','${advice.checkInfoList[0].WARNING_INFO}','${advice.checkInfoList[0].REF_SOURCE}','${advice.DRUG_LO_NAME}')">${advice.checkInfoList[0].NAME}</a></td>
                                        </tr>
                                </c:forEach>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="button-area">

                    <%--<input id="sort_bedNo" type="button" onclick="sortByBedNo()" value="按床号排序"/>--%>

                    <%--<input id="sort_serious" type="button" onclick="sortBySerious()" value="按严重程度排序"/>--%>

                    <%--<input id="export" type="button" onclick="next()" value="导出word"/>--%>
                    <input id="cancel" type="button" onclick="nextOrBack(-1)" value="取消"/>
                    &nbsp;&nbsp;&nbsp;&nbsp;
                    <input id="confirm" type="button" onclick="nextOrBack(0)" value="确认"/>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    openProblemDetail(advice.checkInfoList[0].NAME,advice.checkInfoList[0].WARNING_INFO,advice.checkInfoList[0].REF_SOURCE,advice.DRUG_LO_NAME);
    function openProblemDetail(name,warning_info,ref_source,drug_name) {
        var temp = "<tr><td>[问题药品]&nbsp;@(drug_name)</td></tr>" +
                "<tr><td id='warning_info'>[警示信息]&nbsp;@(warning_info)</td></tr>" +
                "<tr><td>[参考信息]&nbsp;@(ref_source)</td></tr>";
        var tempHtml = "<thead><tr><th>" + name + "</th></tr></thead>";
        tempHtml += temp.replace('@(drug_name)', drug_name).replace('@(warning_info)', warning_info)
                .replace('@(ref_source)', ref_source);
        $("#error_detail").html(tempHtml);
    }
</script>
</body>
</html>
