<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: sima
  Date: 2017/2/28
  Time: 22:12
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

    <script type="text/javascript"
            src="http://${config.drStationServerIp}:${config.drStationServerPort}/DCStation/js/jquery.min.js"></script>
    <link href="http://${config.drStationServerIp}:${config.drStationServerPort}/DCStation/css/checkResultPage.css"
          rel="stylesheet" type="text/css"/>
    <link href="http://${config.drStationServerIp}:${config.drStationServerPort}/DCStation/css/style.css"
          rel="stylesheet" type="text/css"/>

    <script type="text/javascript">
        var checkServerIp = '${config.drStationServerIp}';
        var cheServerPost = '${config.drStationServerPort}';
        var cheServerPost = '${config.drStationServerPort}';
        //药品说明书链接
        var disUrl = '${config.drugDescriptionURL}';
        var presId = '${presId}';
        var xmlString = '${checkData}';
        var checkStateInterval = '${config.checkInterval}';
        var checkMessageInterval = '${config.checkInterval}';
        var longestWaitTime = '${config.longestWaitTime}';
        var checkInterveneStateURL = '${config.checkInterveneStateURL}';
        var checkInterveneMessageURL = '${config.checkInterveneMessageURL}';
        var interveneFlag = '${config.interveneFlag}';
        var message_no = 1;
        var dr_message_no = 0;
        var directCloseFlag = 1;

        /*查询干预状态时，轮询停止标识。如果查询结果为零，则停止轮询*/
        var stop_flag = false
        //进度条计时器
        var timeout = null;
        //查询药师状态计时器
        var checkInterveneState = null;
        var checkInterveneMessage = null;
        var agreeToNextStrs = ['同意双签字发药', '同意医师观点'];

        var requestMessage = "<Request FUN='@fun@'>" +
                "<Input PATIENT_ID='${checkResult.patient.ID}' " +
                "PRES_ID='@presId@' " +
                "PRES_DATE='${checkResult.advices[0].PRES_DATE}' " +
                "MESSAGE_NO='@message_no@' " +
                "WEB_DC='1' " +
                "MESSAGE_TEXT='@message_text@' " +
                "MESSAGE_TIME='@message_time@' />" +
                "</Request>";

        /********定义iframe模板********/
        var checkResultTemp = getTemplateByName("check_result_template");
        /********定义iframe模板********/

        /**************  进度条模板 *****************/
        var progress_bar = getTemplateByName("progress_bar_template");
        /**************            *****************/

        /**************  对话框模板 *****************/
        var dialog_frame = getTemplateByName('dialog_frame_template');

        var drMessage = '<div class="demo clearfix fr"><span class="triangle"></span>' +
                '<div class="article">@message@</div></div>'

        var phMessage = '<div class="demo clearfix"><span class="triangle"></span>' +
                '<div class="article">@message@</div></div>'
        /**************            *****************/

        function getRequestMessage(fun, message_text) {
            if (message_text == undefined) {
                message_text = "";
            }
            return requestMessage.replace('@fun@', fun).replace("@message_no@", message_no)
                    .replace("@message_text@", message_text);
        }

        function getTemplateByName(name) {
            var url = 'http://${config.drStationServerIp}:${config.drStationServerPort}/DCStation/html/' + name + ".html";
            return sendAjaxRequest(url, '', "GET");
        }
        function sendQuitMessage(val) {
            if (val == -1) {
                sendRealMessage("修改处方");
            } else {
                sendRealMessage("打印处方");
            }
        }

        function changeDirectCloseFlag() {
            directCloseFlag = 0;
        }

        /*
         * val:0下一步,-1返回修改
         * */
        function nextOrBack(val, needSendMessage) {
            if (needSendMessage != undefined && needSendMessage == true) {
                sendQuitMessage(val);
            }
            changeDirectCloseFlag();

            var url = "http://" + checkServerIp + ":" + cheServerPost + "/DCStation/submit/setRetValue";
            var arg = 'presId=' + presId + '&retVal=' + val;
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

            clearAllInterval();
            window.close();
        }

        function drawCheckResultElem(url) {
            var checkResultElem = document.getElementById("checkResult");
            checkResultElem.innerHTML = checkResultTemp.replace('@(url)', url);
            document.getElementById("checkResultButton").click();
            showdiv();
        }
        function showdiv() {
            document.getElementById("bg").style.display = "block";
            document.getElementById("show").style.display = "block";
        }
        function hidediv() {
            document.getElementById("bg").style.display = 'none';
            document.getElementById("show").style.display = 'none';
        }
        function openDiscribLinked(code) {
            var urlTemp = disUrl.replace("@code@", code);
            drawCheckResultElem(urlTemp);
        }

        /*** 查询干预状态 ***/


        function checkState() {
            checkInterveneState = window.setInterval("checkIsPass()", checkStateInterval);
        }

        function beginIntervene() {
            var url = "http://${config.drStationServerIp}:${config.drStationServerPort}/DCStation/submit/beginIntervene";
            var checkData = sendAjaxRequest(url, presId);
            requestMessage = requestMessage.replace("@presId@", checkData);
        }

        function next() {
            if (interveneFlag != 1) {
                nextOrBack(0);
            } else {
                beginIntervene();
                showProgressBar();
                checkState();
            }
        }

        function showProgressBar() {
            var checkResultElem = document.getElementById("checkResult");
            checkResultElem.innerHTML = progress_bar;
            timeout = window.setInterval("run()", longestWaitTime / 100);
            showdiv();
        }

        function sendAjaxRequest(url, args, method) {
            if (method == undefined) {
                method = 'POST';
            }
            var xmlhttp;
            if (window.XMLHttpRequest) {
                //  IE7+, Firefox, Chrome, Opera, Safari
                xmlhttp = new XMLHttpRequest();
            } else {
                // IE6, IE5
                xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
            }
            xmlhttp.open(method, url, false);
            xmlhttp.setRequestHeader("Content-type", "text/html;charset=UTF-8");
            xmlhttp.send(args);

            var checkData = xmlhttp.responseText;
            return checkData;
        }

        function checkIsPass() {
            var args = getRequestMessage('1');
            var checkData = sendAjaxRequest(checkInterveneStateURL, args);

            if (analyzeData(checkData).presState == 0) {
                stop_flag = true;
                window.clearInterval(checkInterveneState);
            } else if (analyzeData(checkData).presState == 3) {
                nextOrBack(0);
            }
        }

        function analyzeDataForState(data) {
            var reg = /[\s\S]* PRES_STATE=['"]([\s\S]*?)['"][\s\S]*/ig;
            return parseInt(data.replace(reg, "$1"));
        }

        //查询药师消息
        function analyzeDataForMessage(data) {
            var dataList = data.split("Output");
            var msgReg = /[\s\S]*MESSAGE_NO=['"]([\s\S]*?)['"][\s\S]*MESSAGE_TEXT=['"]([\s\S]*?)['"][\s\S]*/ig;
            var messageList = [];
            for (index in dataList) {
                var msg = {};
                var messageNo = parseInt(dataList[index].replace(msgReg, "$1"));
                if (!isNaN(messageNo)) {
                    msg.messageNo = messageNo;
                    msg.messageText = dataList[index].replace(msgReg, "$2");
                    messageList.push(msg);
                }
            }
            return messageList;
        }

        function analyzeData(data) {
            var funReg = /[\s\S]*FUN=['"]([\s\S]*?)['"][\s\S]*/ig;
            var funNumber = parseInt(data.replace(funReg, "$1"));
            responseData = {};

            if (isNaN(funNumber)) {
                responseData.errorFlag = true;
            } else {
                switch (funNumber) {
                    case 1:
                        responseData.presState = analyzeDataForState(data);
                        break;
                    case 2:
                        responseData.messageList = analyzeDataForMessage(data);
                        break;
                }
            }

            return responseData;
        }

        function canBeNext(messageText) {
            for (i in agreeToNextStrs) {
                if (messageText == agreeToNextStrs[i]) {
                    $("#second-next").attr('disabled', false);
                    $("#second-next").css('background', 'url(http://${config.drStationServerIp}:${config.drStationServerPort}/DCStation/image/circlerectangle.png) no-repeat center');
                }
            }
        }
        function printPharmMessageList(messageList) {
            for (index in messageList) {
                var temp_message_no = messageList[index].messageNo;
                if (temp_message_no <= dr_message_no) {
                    continue;
                }
                dr_message_no = temp_message_no;
                var realMessage = phMessage.replace('@message@', messageList[index].messageText);

                canBeNext(messageList[index].messageText);
                addMessageToWin(realMessage);
            }
        }
        function checkPharmMessage() {
            var args = getRequestMessage('2');
            var data = sendAjaxRequest(checkInterveneMessageURL, args);
            var messageList = analyzeData(data).messageList;

            printPharmMessageList(messageList);
        }


        function checkMessage() {
            checkInterveneMessage = window.setInterval("checkPharmMessage()", checkMessageInterval);
        }
        var beginTalk = function () {
            var checkResultElem = document.getElementById("checkResult");
            checkResultElem.innerHTML = dialog_frame;
            showdiv();

            checkMessage();
        };

        var sendMessage = function () {
            var message = $("#message-area").val();
            sendRealMessage(message);
            $("#message-area").val('');
        }

        function acceptToDeliverDrug() {
            var message = "请求双签字发药";
            sendRealMessage(message);
        }

        function sendRealMessage(message) {
            //发送消息
            var messageXML = getRequestMessage('3', message);
            sendAjaxRequest(checkInterveneMessageURL, messageXML);
            message_no += 1;

            //添加消息到对话框
            var realMessage = drMessage.replace('@message@', message);
            addMessageToWin(realMessage);
        }

        var addMessageToWin = function (message) {
            message = message.replace(/\r\n/g, "<br>").replace(/\n/g, "<br>");
            $("#talk-win").append($(message));
            $("#talk-win").parent().scrollTop(200000);
        }

        function clearAllInterval() {
            window.clearInterval(timeout);
            window.clearInterval(checkInterveneState);
            window.clearInterval(checkInterveneMessage);
        }
        /*****      进度条js      ****/

        function run() {
            var bar = document.getElementById("bar");
            var total = document.getElementById("total");
            bar.style.width = parseInt(bar.style.width) + 1 + "%";

            if (stop_flag == true) {
                clearAllInterval();
                hidediv();
                beginTalk();

                //禁止点击下一步
                $("#second-next").attr('disabled', true);
                $("#second-next").css('background', 'url(http://${config.drStationServerIp}:${config.drStationServerPort}/DCStation/image/nextdisabled.png) no-repeat center');
                return;
            }

            if (parseInt(bar.style.width) >= 100) {
                clearAllInterval();
                hidediv();
                nextOrBack(0)
                return;
            }
        }
        function isDirectClose() {
            return directCloseFlag == 1;
        }

        function isIntervening() {
            return checkInterveneState != null;
        }

        function isIE() { //ie?
            if (!!window.ActiveXObject || "ActiveXObject" in window)
                return true;
            else
                return false;
        }

        (function myBrowser() {
            if (isIE()) {
                window.onbeforeunload = function () {
                    if (isDirectClose()) {
                        alert("即将返回修改！");
                        if (isIntervening()) {
                            nextOrBack(-1, true);
                        } else {
                            nextOrBack(-1);
                        }
                    }
                }
            }
        })();

    </script>

    <style type="text/css">
        .container {
            width: 450px;
            border: 1px solid #6C9C2C;
            height: 25px;
        }

        #bar {
            background: #06bfc7;
            float: left;
            height: 100%;
            text-align: center;
            line-height: 150%;
        }

        #bg {
            display: none;
            position: absolute;
            top: 0%;
            left: 0%;
            width: 100%;
            height: 100%;
            background-color: black;
            z-index: 1001;
            -moz-opacity: 0.7;
            opacity: .70;
            filter: alpha(opacity=70);
        }

        #show {
            display: none;
            position: absolute;
            top: 25%;
            left: 22%;
            width: 53%;
            height: 49%;
            padding: 8px;
            border: 8px solid #E8E9F7;
            background-color: white;
            z-index: 1002;
            overflow: auto;
        }

        * html {
            background: url(*) fixed;
        }

        * html body {
            margin: 0;
            height: 100%;
        }

        * html .overlay {
            position: absolute;
            left: expression(documentElement.scrollLeft + documentElement.clientWidth - this.offsetWidth);
            top: expression(documentElement.scrollTop + documentElement.clientHeight - this.offsetHeight);
        }
    </style>
</head>
<body>
<div>
    <div>
        <div class="main-content">
            <div class="head-top">
            </div>
            <div class="head-info-lg">
                <p>审查结果</p>
            </div>
            <div class="info-area">
                <div class="detail-info left-area">
                    <div class="head-info-sm"><p>详细信息</p></div>
                    <div class="detail-content" style="padding: 10px;_padding:20px 10px;">
                        <table id="error_detail">
                            <tr>
                                <td>[问题药品]&nbsp;@(drug_name)</td>
                            </tr>
                            <tr>
                                <td>[警示信息]&nbsp;@(warning_info)</td>
                            </tr>
                            <tr>
                                <td>[参考信息]&nbsp;@(ref_source)</td>
                            </tr>
                        </table>
                    </div>
                </div>
                <div class="detail-info right-area">
                    <div class="head-info-sm"><p>使用说明</p></div>
                    <div class="detail-content">
                        <table style="_margin-top: 50px">
                            <tr>
                                <td>1、单击问题所在单元，可查看详细信息</td>
                            </tr>
                            <tr>
                                <td>2、<img
                                        src="http://${config.drStationServerIp}:${config.drStationServerPort}/DCStation/image/common-problem.png"/>
                                    &nbsp;一般警示 &nbsp;&nbsp;
                                    <img src="http://${config.drStationServerIp}:${config.drStationServerPort}/DCStation/image/serious-problem.png"/>
                                    &nbsp;严重警示&nbsp;&nbsp;
                                    <img src="http://${config.drStationServerIp}:${config.drStationServerPort}/DCStation/image/disaster-problem.png"/>
                                    &nbsp;必须处理
                                </td>
                            </tr>
                            <tr>
                                <td>3、单击药品名称，可查看说明书</td>
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
                            <th style="width: 140px">药品名称</th>
                            <th>适应症</th>
                            <th>禁用症<br>慎用症</th>
                            <th>用法<br>用量</th>
                            <th>重复<br>用药</th>
                            <th>相互<br>作用</th>
                            <th>配伍<br>禁忌</th>
                            <th>特殊<br>人群</th>
                            <th>药敏</th>
                            <th>用药<br>管理</th>
                            <th>用药<br>监测</th>
                        </tr>
                        </thead>
                    </table>
                </div>
                <div class="table-area-content">
                    <table class="main-table">
                        <tbody>
                        <c:forEach var="item" items="${checkResult.advices}">
                            <tr>
                                <td style="width: 132px;">
                                    <a onclick="openDiscribLinked('${item.DRUG_LO_ID}')">
                                            ${item.DRUG_LO_NAME}
                                    </a>
                                </td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="button-area">
                <%--<input id="appealBtn" type="button" style="display: none" value="我要反馈"/>--%>
                <%--&nbsp;&nbsp;&nbsp;&nbsp;--%>
                <input id="back" type="button" onclick="nextOrBack(-1)" value="返回"/>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <input id="next" type="button" onclick="next()" value="下一步"/>
            </div>

        </div>
    </div>

</div>
<div class="dialog">
</div>


<div id="checkResult">
</div>

<script type="text/javascript">
    var checkResultJson = ${checkResultJson};
    var advises = checkResultJson.advices;
    var problemType = ['适应症', '禁用症慎用症', '用法用量', '重复用药', '相互作用', '配伍禁忌', '特殊人群', '药敏', '医院管理', '用药监测'];
    var problemLevelClassName = ['disaster-problem', 'common-problem', 'common-problem', 'serious-problem'];
    var error_detail = $('#error_detail').html();
    $('#error_detail').html('');
    for (var i = 0; i < advises.length; i++) {
        var advise = advises[i];
        var checkInfoList = advise.checkInfoList;
        for (var j = 0; j < checkInfoList.length; j++) {
            var checkInfo = checkInfoList[j];
            var curProblemLevel = -1;
            for (var k = 0; k < problemType.length; k++) {
                if (checkInfo.NAME == problemType[k]) {
                    var problemLevel = parseInt(checkInfo.REGULAR_WARNING_LEVEL) + 1;
                    var $chooseTd = $(".main-table tbody").children().eq(i).children().eq(k + 1);

                    //如果问题等级是-1（拦截）或者问题等级大于当前等级，则更改图标
                    if (problemLevel == -1 || curProblemLevel < problemLevel) {
                        var className = problemLevelClassName[problemLevel];
                        $chooseTd.attr('class', className);
                        curProblemLevel = problemLevel == -1 ? 10 : problemLevel;
                    }

                    $chooseTd.click({row: i, col: k}, function (event) {
                        showProblemDetail(event.data)
                    });
                }
            }
        }
    }

    function showProblemDetail(data) {
        var row = data.row;
        var col = data.col;

        var drug_name = $(".main-table tbody").children().eq(row).children().eq(0).children().html().replace(' ', '');
        var error_name = problemType[col];
        $("#error_detail").html('');
        var tempHtml = "<thead><tr><th>" + error_name + "</th></tr></thead>";
        var advise = advises[row];
        var checkInfoList = advise.checkInfoList;
        for (var j = 0; j < checkInfoList.length; j++) {
            var checkInfo = checkInfoList[j];
            if (checkInfo.NAME != error_name) {
                continue;
            }
            var temp = error_detail;
            tempHtml += temp.replace('@(drug_name)', drug_name).replace('@(warning_info)', checkInfo.WARNING_INFO)
                    .replace('@(ref_source)', checkInfo.REF_SOURCE);
        }
        $("#error_detail").html(tempHtml);
        showAppealBtn(drug_name, error_name, '${presId}');
    }
    (function isDisabled() {
        var t = ${checkResult.HIGHEST_WARNING_LEVEL};
        if (t == -1) {
            $("#next").attr('disabled', 'disabled');
            $("#next").css('background-image', 'url(http://${config.drStationServerIp}:${config.drStationServerPort}/DCStation/image/nextdisabled.png)');
            $("#next").css('background-repeat', 'no-repeat');
            $("#next").css('background-position', 'center');
        }
    })();

    //    (function myBrowser() {
    //        var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
    //        if (userAgent.indexOf("Chrome") > -1) {
    //            $("#back").click({val: -1}, function (event) {
    //                nextForChrome(event.data.val);
    //            });
    //            $("#next").click({val: 0}, function (event) {
    //                nextForChrome(event.data.val);
    //            });
    //        }
    //    })();
    //    function nextForChrome(val) {
    //        if (val == 0) {
    //            parent.check_for_next();
    //        } else if (val == -1) {
    //            parent.check_for_back();
    //        }
    //    }

    function showAppealBtn(drugName, errorName, presId) {
        $("#appealBtn").show();
        $("#appealBtn").unbind();//important
        $("#appealBtn").bind('click', function () {
            window.open("http://${config.drStationServerIp}:${config.drStationServerPort}/DCStation/appeal/appeal?presId=" + presId + "&drugName=" + drugName + "&errorName=" + errorName);
        });
    }

</script>
</body>
</html>