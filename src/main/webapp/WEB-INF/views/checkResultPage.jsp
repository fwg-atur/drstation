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
    <script type="text/javascript"
            src="http://${config.drStationServerIp}:${config.drStationServerPort}/DCStation/js/dcdt_${config.browserFlag}.js"></script>
    <script type="text/javascript"
            src="http://${config.drStationServerIp}:${config.drStationServerPort}/DCStation/js/jquery.xdomainrequest.min.js"></script>

    <link href="http://${config.drStationServerIp}:${config.drStationServerPort}/DCStation/css/checkResultPage${config.browserFlag}.css"
          rel="stylesheet" type="text/css"/>
    <link href="http://${config.drStationServerIp}:${config.drStationServerPort}/DCStation/css/style.css"
          rel="stylesheet" type="text/css"/>

    <script type="text/javascript">
        var checkServerIp = '${config.drStationServerIp}';
        var checkServerPort = '${config.drStationServerPort}';
        var bz_flag = '${config.bz_flag}';
        //药品说明书链接
        var disUrl = '${config.drugDescriptionURL}';
        var presId = '${presId}';
        var xmlString = '${checkData}';
        var checkStateInterval = '${config.checkInterval}';
        var checkMessageInterval = '${config.checkInterval}';
        var longestWaitTime = '${config.longestWaitTime}';
        var waitTimeSeconds = longestWaitTime / 1000;
        var checkInterveneStateURL = '${config.checkInterveneStateURL}';
        var checkInterveneMessageURL = '${config.checkInterveneMessageURL}';
        var interveneFlag = '${config.interveneFlag}';
        var highestWarningLevel = ${checkResult.HIGHEST_WARNING_LEVEL};
        var message_no = 1;
        var dr_message_no = 0;
        var directCloseFlag = 1;

        var antiCheckNumber = 0;
        var checkResultJson = ${checkResultJson};
        var advises = checkResultJson.advices;
        var patient = checkResultJson.patient;
        var doctor = checkResultJson.doctor;
        var pharmacistCode;
        var pharmacistName;

        /*查询干预状态时，轮询停止标识。如果查询结果为零，则停止轮询*/
        var stop_flag = false;
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
        /********定义药品说明书模板********/
        var drug_specification = getTemplateByName("drug_specification_template");
        /********定义药品说明书模板********/

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

        /**************  抗菌药登记模板 *****************/
        var antiCheckInInfo = getTemplateByName("anti_check_in_template");

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
            if (val == -1 || val == -2) {
                sendRealMessage("修改处方");
            } else {
                sendRealMessage("打印处方");
            }
        }

        function changeDirectCloseFlag() {
            directCloseFlag = 0;
        }

        function saveCheckMessage(message) {
            var url = "http://" + checkServerIp + ":" + checkServerPort + "/DCStation/submit/setRetValue_bz";
            var arg = 'presId=' + presId + '&message=' + message;
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
        }

        function drawCheckResultElem(url) {
            // var checkResultElem = document.getElementById("checkResult");
            // checkResultElem.innerHTML = checkResultTemp.replace('@(url)', url);
            // document.getElementById("checkResultButton").click();
            // showdiv();
            var fn = function (checkResultElem) {
                checkResultElem.innerHTML = drug_specification.replace('@(url)', url);
                document.getElementById("checkResultButton").click();
            }
            showDrugSpecResult(fn);
        }

        function showDrugSpecResult(fn) {
            var checkResultElem = document.getElementById("checkResult");
            fn(checkResultElem);
        }

        function showCheckResult(fn) {
            var checkResultElem = document.getElementById("checkResult");
            fn(checkResultElem);
            showdiv();
        }

        function showdiv() {
            document.getElementById("bg").style.display = "block";
            document.getElementById("show").style.display = "block";
//             document.getElementById("closeButton").style.display = "block";
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
            checkResultElem.innerHTML = progress_bar.replace('@{longestWaitTime}',waitTimeSeconds);
            timeout = window.setInterval('run()', 1000);
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
            var presState = analyzeData(checkData).presState;
            if (0 == presState) {
                stop_flag = true;
                window.clearInterval(checkInterveneState);
            }
            //presState=3,医生点击下一步，但没有分配给审方药师审核
            //presState=4,审方药师审核超时
            //presState=5,允许发药
            else if (3 == presState || 4 == presState || 5 == presState) {
                //bz_flag值为1表示是滨医附院
                if(1 == bz_flag){
                    pharmacistCode = analyzeData(checkData).checkPharmacistCode;
                    pharmacistName = analyzeData(checkData).checkPharmacistName;
                    var retXml;
                    if (3 == presState) {
                        retXml = "<CheckResult STATE=\"0\" STYLE=\"Machine\" CHECK_PHARMACIST_CODE=\"\" CHECK_PHARMACIST_NAME=\"\" CHECK_STATE=\"\" TAG=\"\" />";
                    } else if (4 == presState) {
                        retXml = "<CheckResult STATE=\"0\" STYLE=\"ManualTimeOut\" CHECK_PHARMACIST_CODE=\""+pharmacistCode+"\" CHECK_PHARMACIST_NAME=\""+pharmacistName+"\" CHECK_STATE=\"\" TAG=\"\" />"
                    } else if (5 == presState) {
                        retXml = "<CheckResult STATE=\"0\" STYLE=\"ManualNormal\" CHECK_PHARMACIST_CODE=\""+pharmacistCode+"\" CHECK_PHARMACIST_NAME=\""+pharmacistName+"\" CHECK_STATE=\"未沟通发药\" TAG=\"\"  />"
                    }
                    saveCheckMessage(retXml);
                }else {
                    nextOrBack(0);
                }

            }


        }

        function analyzeDataForState(data) {
            var reg = /[\s\S]*PRES_STATE=['"]([\s\S]*?)['"][\s\S]*/ig;
            return parseInt(data.replace(reg, "$1"));
        }

        function analyzeDataForPharmacistCode(data) {
            var reg = /[\s\S]*CHECK_PHARMACIST_CODE=['"]([\s\S]*?)['"][\s\S]*/ig;
            return data.replace(reg, "$1");
        }

        function analyzeDataForPharmacistName(data) {
            var reg = /[\s\S]*CHECK_PHARMACIST_NAME=['"]([\s\S]*?)['"][\s\S]*/ig;
            return data.replace(reg, "$1");
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

        //从药师多条消息中获取PresState
        function analyzeDataForStateFromMessage(data) {
            var dataList = data.split("Output");
            var msgReg = /[\s\S]*PRES_STATE=['"]([\s\S]*?)['"][\s\S]*/ig;
            for (index in dataList) {
                var presState = parseInt(dataList[index].replace(msgReg, "$1"));
                if (!isNaN(presState)) {
                    return presState;
                }
            }
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
                        pharmacistCode = analyzeDataForPharmacistCode(data);
                        responseData.checkPharmacistCode = pharmacistCode;
                        pharmacistName = analyzeDataForPharmacistName(data)
                        responseData.checkPharmacistName = pharmacistName;
                        break;
                    case 2:
                        responseData.presState = analyzeDataForStateFromMessage(data);
                        responseData.checkPharmacistCode = pharmacistCode;
                        responseData.checkPharmacistName = pharmacistName;
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
            var retData = analyzeData(data);
            var messageList = retData.messageList;
            var presState = retData.presState;
            var pharmacistCode = retData.checkPharmacistCode;
            var pharmacistName = retData.checkPharmacistName;
            var retXml;
            if(1 == bz_flag) {
                //6表示医生双签字，7表示药师同意医生观点
                if (6 == presState || 7 == presState) {
                    if (6 == presState) {
                        retXml = "<CheckResult STATE=\"0\" STYLE=\"ManualNormal\" CHECK_PHARMACIST_CODE=\"" + pharmacistCode + "\" CHECK_PHARMACIST_NAME=\"" + pharmacistName + "\" CHECK_STATE=\"" + "双签字" + "\" TAG=\"\" />";
                    } else if (7 == presState) {
                        retXml = "<CheckResult STATE=\"0\" STYLE=\"ManualNormal\" CHECK_PHARMACIST_CODE=\"" + pharmacistCode + "\" CHECK_PHARMACIST_NAME=\"" + pharmacistName + "\" CHECK_STATE=\"" + "同意医生" + "\" TAG=\"\" />";
                    }
                    saveCheckMessage(retXml);
                    nextOrBack(0,true);
                }
            }
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
            var message = $("#message-area").val().trim();
            if(message != null && message != '') {
                sendRealMessage(message);
            }
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
//            var total = document.getElementById("total");
            if(waitTimeSeconds < 1){
                waitTimeSeconds = 1;
            }
            bar.style.width = parseFloat(bar.style.width) + (100/waitTimeSeconds) + "%";

            var currentWaitTime = document.getElementById("currentWaitTime");
            currentWaitTime.innerText = parseInt(currentWaitTime.innerText) + 1;

            if (stop_flag == true) {
                clearAllInterval();
                hidediv();
                beginTalk();

                //禁止点击下一步
                $("#second-next").attr('disabled', true);
                $("#second-next").css('background', 'url(http://${config.drStationServerIp}:${config.drStationServerPort}/DCStation/image/nextdisabled.png) no-repeat center');
                return;
            }

            if (parseFloat(bar.style.width) >= 99) {
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

        function sendSQLToServer(args) {
            var url = '${config.antiCheckInURL}';
            sendAjaxRequest(url, args);
        }

        function changeAntiState() {
            var drugId = $("input[name='drugId']").eq(0).val();
            antiCheckedInValue[drugId] = true;
            $chooseTd.attr('class', $chooseTd.attr('class').replace('@', ''));
            $chooseTd.html("");
            antiCheckNumber--;
            checkCanBeNext();
        }

        var preMediatorSql = "<Check>" +
                "INSERT INTO ANTI_DRUG_USE_REC_YF " +
                "(PATIENT_ID,VISIT_ID,DEPT_CODE,DEPT_NAME,DOCTOR_NAME,NAME,SEX,AGE," +
                "WEIGHT,DRUG_CODE,DRUG_NAME,DOSAGE,DOSAGE_UNITS,ADMINISTRATION,OPERTOR_DATE," +
                "OPERTOR_NAME,OPERTOR_TYPE,OPERTOR_USE_TIME,DRUG_USE_TIME,GMS," +
                "BTGMS,WXYS,KNDZBJ,KLJLANJL,YF_USE_DRUG_YJ,TSYQ,REC_DATE," +
                "IS_EXCEEDING,IS_OPERATION_DRUG,ORDER_NO,ORDER_SUB_NO," +
                "REPEAT_INDICATOR,ORDER_START_TIME,ORDER_STOP_TIME,INPATIENT) " +
                "VALUES('" + patient.ID + "','" + patient.VISIT_ID + "','" + doctor.DEPT_CODE + "','" + doctor.DEPT_NAME +
                "','" + doctor.NAME + "','" + patient.NAME +
                "','" + patient.GENDER + "','patient.BIRTH','" + patient.WEIGHT +
                "','@{DRUG_CODE}','@{DRUG_NAME}','@{DOSAGE}','@{DOSAGE_UNITS}','@{ADMINISTRATION}','@{OPERTOR_DATE}'," +
                "'@{OPERTOR_NAME}','@{OPERTOR_TYPE}','@{OPERTOR_USE_TIME}','@{DRUG_USE_TIME}','@{GMS}'," +
                "'@{BTGMS}','@{WXYS}','@{KNDZBJ}','@{KLJLANJL}','@{YF_USE_DRUG_YJ}','@{TSYQ}','@{REC_DATE}'," +
                "'@{IS_EXCEEDING}','@{IS_OPERATION_DRUG}','@{ORDER_NO}','@{ORDER_SUB_NO}'," +
                "'@{REPEAT_INDICATOR}',to_date('@{ORDER_START_TIME}','yyyymmdd'),to_date('@{ORDER_STOP_TIME}','yyyymmdd'),'@{INPATIENT}')" +
                "</Check>";

        function getTodayDate() {
            var date = new Date();
            var REC_DATE = date.getFullYear() + '-' +
                    (date.getMonth() + 1) + '-' + date.getDate();
            return REC_DATE;
        }

        function getPrevMedicSQL() {
            var advice = checkInAdvice;
            var opName = $('input[name="opName"]').val();
            var opertor_type = $('#opertor_type option:selected').text();
            var opertor_use_time = '';
            var drug_use_time = '';
            var GMS = $('input[name="GMS"]').val();
            var BTGMS = $('input[name="BTGMS"]').val();
            var WXYS = $('input[name="WXYS"]').val();
            var YF_USE_DRUG_YJ = $('input[name="YF_USE_DRUG_YJ"]').val();
            var KNDZBJ = '';
            var KLJLANJL = '';
            var TSYQ = '';
            var REC_DATE = getTodayDate();
            var IS_EXCEEDING = 1;
            var IS_OPERATION_DRUG = 0;
            var INPATIENT = ${config.inHosFlag};
            var sql = preMediatorSql.replace('@{DRUG_CODE}', advice.DRUG_LO_ID).replace('@{DRUG_NAME}', advice.DRUG_LO_NAME)
                    .replace('@{DOSAGE}', advice.DOSAGE).replace('@{DOSAGE_UNITS}', advice.DOSAGE_UNIT)
                    .replace('@{ADMINISTRATION}', advice.ADMINISTRATION).replace('@{OPERTOR_DATE}', '')
                    .replace('@{OPERTOR_NAME}', opName).replace('@{OPERTOR_TYPE}', opertor_type)
                    .replace('@{OPERTOR_USE_TIME}', opertor_use_time).replace('@{DRUG_USE_TIME}', drug_use_time)
                    .replace('@{GMS}', GMS).replace('@{BTGMS}', BTGMS).replace('@{WXYS}', WXYS)
                    .replace('@{KNDZBJ}', KNDZBJ).replace('@{KLJLANJL}', KLJLANJL)
                    .replace('@{YF_USE_DRUG_YJ}', YF_USE_DRUG_YJ).replace('@{TSYQ}', TSYQ)
                    .replace('@{REC_DATE}', REC_DATE).replace('@{IS_EXCEEDING}', IS_EXCEEDING)
                    .replace('@{IS_OPERATION_DRUG}', IS_OPERATION_DRUG).replace('@{ORDER_NO}', advice.ORDER_NO)
                    .replace('@{ORDER_SUB_NO}', advice.ORDER_SUB_NO).replace('@{REPEAT_INDICATOR}', advice.REPEAT)
                    .replace('@{ORDER_START_TIME}', advice.START_DAY).replace('@{ORDER_STOP_TIME}', advice.END_DAY)
                    .replace('@{INPATIENT}', INPATIENT);
            return sql;
        }

        var therapMediatorSql = "<Check>" +
                "INSERT INTO ANTI_DRUG_USE_REC_ZL " +
                "(PATIENT_ID,VISIT_ID,DEPT_CODE,DEPT_NAME,DOCTOR_NAME,NAME,SEX,AGE," +
                "WEIGHT,YYLY,ZLFL,GRBW,ZDYJ,KNDZBJ," +
                "DRUG_CODE,DRUG_NAME,DOSAGE,DOSAGE_UNITS,ADMINISTRATION," +
                "REC_DATE,TWDATE,TWVALUE,TWOK,XXDATE,XXVALUE,CVALUE,XXOK," +
                "IS_EXCEEDING,ORDER_NO,ORDER_SUB_NO," +
                "REPEAT_INDICATOR,ORDER_START_TIME,ORDER_STOP_TIME,INPATIENT) " +
                "VALUES('" + patient.ID + "','" + patient.VISIT_ID + "','" + doctor.DEPT_CODE + "','" + doctor.DEPT_NAME +
                "','" + doctor.NAME + "','" + patient.NAME +
                "','" + patient.GENDER + "','" + patient.BIRTH + "','" + patient.WEIGHT +
                "','@{YYLY}','@{ZLFL}','@{GRBW}','@{ZDYJ}','@{KNDZBJ}'," +
                "'@{DRUG_CODE}','@{DRUG_NAME}','@{DOSAGE}','@{DOSAGE_UNITS}','@{ADMINISTRATION}'," +
                "'@{REC_DATE}','@{TWDATE}','@{TWVALUE}','@{TWOK}','@{XXDATE}','@{XXVALUE}','@{CVALUE}','@{XXOK}'," +
                "'@{IS_EXCEEDING}','@{ORDER_NO}','@{ORDER_SUB_NO}'," +
                "'@{REPEAT_INDICATOR}',to_date('@{ORDER_START_TIME}','yyyymmdd'),to_date('@{ORDER_STOP_TIME}','yyyymmdd'),'@{INPATIENT}')" +
                "</Check>";

        function getTherapMedicSQL() {
            var advice = checkInAdvice;
            var YYLY = $('input[name="YYLY"]').val();
            var ZLFL = $('input[name="ZLFL"]').val();
            var GRBW = $('input[name="GRBW"]').val();
            var ZDYJ = $('input[name="ZDYJ"]').val();
            var TWDATE = $('input[name="TWDATE"]').val();
            var TWVALUE = $('input[name="TWVALUE"]').val();
            var TWOK = 0;
            if ($('input[name="TWOK"]').attr('checked')) {
                TWOK = 1;
            }

            var XXDATE = $('input[name="XXDATE"]').val();
            var XXVALUE = $('input[name="XXVALUE"]').val();
            var CVALUE = $('input[name="CVALUE"]').val();
            var XXOK = 0;
            if ($('input[name="XXOK"]').attr('checked')) {
                XXOK = 1;
            }
            var ZXLVALUE = $('input[name="ZXLVALUE"]').val();
            var KNDZBJ = '';
            var REC_DATE = getTodayDate();
            var IS_EXCEEDING = 1;
            var INPATIENT = ${config.inHosFlag};
            var sql = therapMediatorSql.replace('@{YYLY}', YYLY).replace('@{ZLFL}', ZLFL).replace('@{GRBW}', GRBW)
                    .replace('@{ZDYJ}', ZDYJ).replace('@{KNDZBJ}', KNDZBJ)
                    .replace('@{DRUG_CODE}', advice.DRUG_LO_ID).replace('@{DRUG_NAME}', advice.DRUG_LO_NAME)
                    .replace('@{DOSAGE}', advice.DOSAGE).replace('@{DOSAGE_UNITS}', advice.DOSAGE_UNIT)
                    .replace('@{ADMINISTRATION}', advice.ADMINISTRATION)
                    .replace('@{TWDATE}', TWDATE).replace('@{TWVALUE}', TWVALUE)
                    .replace('@{TWOK}', TWOK).replace('@{XXDATE}', XXDATE)
                    .replace('@{XXVALUE}', XXVALUE).replace('@{CVALUE}', CVALUE)
                    .replace('@{XXOK}', XXOK)
                    .replace('@{REC_DATE}', REC_DATE).replace('@{IS_EXCEEDING}', IS_EXCEEDING)
                    .replace('@{ORDER_NO}', advice.ORDER_NO).replace('@{ZXLVALUE}', ZXLVALUE)
                    .replace('@{ORDER_SUB_NO}', advice.ORDER_SUB_NO).replace('@{REPEAT_INDICATOR}', advice.REPEAT)
                    .replace('@{ORDER_START_TIME}', advice.START_DAY).replace('@{ORDER_STOP_TIME}', advice.END_DAY)
                    .replace('@{INPATIENT}', INPATIENT);
            return sql;

        }

        function submitPrevMedic() {
            if ($("input[name='opName']").val() == '' ||
                    $("input[name='YF_USE_DRUG_YJ']").eq(0).val() == '') {
                alert("必须填写拟施手术、用药理由！");

                return;
            }
            var args = getPrevMedicSQL();
            sendSQLToServer(args);
            hidediv();

            changeAntiState();
        }

        function submitTherapMedic() {
            if ($("input[name='YYLY']").eq(1).val() == ''
                    || $("input[name='GRBW']").val() == ''
                    || $("input[name='ZDYJ']").val() == '') {

                alert("必须填写用药理由、感染部位、诊断依据！");
                return;
            }

            var args = getTherapMedicSQL();
            sendSQLToServer(args);
            hidediv();

            changeAntiState();
        }

    </script>

    <style type="text/css">
        .container {
            width: 450px;
            border: 1px solid #6C9C2C;
            text-align: center;height:45px;line-height:45px;
            font-size:30px;font-weight:bold;
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
            <div class="inner-main-content" style="overflow-x:hidden">
                <div class="head-top">
                    <div class="head-top left-head"></div>
                    <div class="head-top right-head">|&nbsp;&nbsp;&nbsp;合理用药实时审核Web端</div>
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
                                    <td id="warning_info">[警示信息]&nbsp;@(warning_info)</td>
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
                            <table>
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
                                <th style="width: 10px;_width: 10px"></th>
                                <th id="drug_name_th" style="width: 190px;_width: 190px">药品名称</th>
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
                                <th>医保<br>审核</th>
                            </tr>
                            </thead>
                        </table>
                    </div>
                    <div class="table-area-content">
                        <table class="main-table">
                            <tbody>
                            <c:forEach var="item" items="${checkResult.advices}">
                                <tr>
                                    <td style="width:10px;_width:10px">${item.kh}</td>
                                    <td id="drug_name_td" style="width:190px;_width: 190px">
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
                                    <td></td><!-- 120px-->
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
                    <input id="back" type="button" onclick="nextOrBack(-1)" value="返回修改"/>
                    &nbsp;&nbsp;&nbsp;&nbsp;
                    <input id="next" type="button" onclick="next()" value="继续保存"/>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="dialog">
</div>
<div id="checkResult">
</div>
<script type="text/javascript">
    var problemType = ['适应症', '禁用症慎用症', '用法用量', '重复用药', '相互作用', '配伍禁忌', '特殊人群', '药敏', '医院管理', '用药监测', '医保审核'];
    var problemLevelClassName = ['disaster-problem', 'common-problem', 'common-problem', 'serious-problem'];
    var error_detail = $('#error_detail').html();
    $('#error_detail').html('');

    /**
     * 判断是否需要抗菌药登记
     * @param checkInfo
     * @returns {boolean}
     */
    function needAntiCheckIn(checkInfo, advise) {
        var flag = ('${config.antiCheckInFlag}' == 1)
                && advise.anti_drug_register == 1
                && checkInfo.NAME == '医院管理';
        if (flag) {
            antiCheckNumber++;
        }
        return flag;
    }

    /**
     * 隐藏背景图片。在class name后添加@的方式。登记后删除@，就能恢复背景图片
     * @param $chooseTd
     */
    function hideBackground($chooseTd) {
        $chooseTd.attr('class', $chooseTd.attr('class') + '@');
    }

    /**
     * 设置抗菌药登记入口样式
     * @param $chooseTd
     */
    function setAntiCheckInInfo($chooseTd) {
        $chooseTd.html($chooseTd.html() + "未登记");
        hideBackground($chooseTd);
    }

    var checkInAdvice = null;

    function showAntiCheckInInfo(advise) {
        checkInAdvice = advise;
        var fn = function (checkResultElem) {
            checkResultElem.innerHTML =
                    antiCheckInInfo.replace(/@\(drugId\)/g, advise.DRUG_LO_ID)
                            .replace(/@\(drugName\)/g, advise.DRUG_LO_NAME);
        };
        showCheckResult(fn);
    }

    var highestLevel = -2;
    for (var i = 0; i < advises.length; i++) {
        var advise = advises[i];
        var checkInfoList = advise.checkInfoList;
        for (var k = 0; k < problemType.length; k++) {
            var curProblemLevel = -1;
            for (var j = 0; j < checkInfoList.length; j++) {
                var checkInfo = checkInfoList[j];
                if (checkInfo.NAME == problemType[k]) {
                    var problemLevel = parseInt(checkInfo.REGULAR_WARNING_LEVEL) + 1;
                    var $chooseTd = $(".main-table tbody").children().eq(i).children().eq(k + 2);
                    //如果问题等级是-1（拦截）或者问题等级大于当前等级，则更改图标
                    if (problemLevel == 0 || curProblemLevel < problemLevel) {
                        var className = problemLevelClassName[problemLevel];
                        $chooseTd.attr('class', className);
                        curProblemLevel = problemLevel == 0 ? 10 : problemLevel;
                    }

                    var antiCheckInFlag = needAntiCheckIn(checkInfo, advise);
                    if (antiCheckInFlag) {
                        setAntiCheckInInfo($chooseTd);
                    }
                    $chooseTd.click(
                            {row: i, col: k, antiCheckInFlag: antiCheckInFlag, obj: $chooseTd},
                            function (event) {
                                showProblemDetail(event.data)
                            });

                    if(curProblemLevel > highestLevel){
                        highestLevel = curProblemLevel;
                        $chooseTd.click();
                    }
                }
            }
        }
    }

    var $chooseTd = null;
    var antiCheckedInValue = {};

    function showProblemDetail(data) {
        String.prototype.trim = function () {
            return this.replace(/(^\s*)|(\s*$)/g, "");
        }
        var row = data.row;
        var col = data.col;
        var antiCheckInFlag = data.antiCheckInFlag;
        $chooseTd = data.obj;
        var drug_name = $(".main-table tbody").children().eq(row).children().eq(1).children().html().replace(' ', '').trim();
        var error_name = problemType[col];
        $("#error_detail").html('');
        var tempHtml = "<thead><tr><th>" + error_name + "</th></tr></thead>";
        var advise = advises[row];
        var checkInfoList = advise.checkInfoList;
        for (var j = 0; j < checkInfoList.length; j++) {
            var tag = 0;
            var checkInfo = checkInfoList[j];
            if (checkInfo.NAME != error_name) {
                continue;
            }
            var temp = error_detail;
            tempHtml += temp.replace('@(drug_name)', drug_name).replace('@(warning_info)', checkInfo.WARNING_INFO)
                    .replace('@(ref_source)', checkInfo.REF_SOURCE);

            var problemLevel = parseInt(checkInfo.REGULAR_WARNING_LEVEL) + 1;
            var className = problemLevelClassName[problemLevel];
            if(className == 'disaster-problem' || className == 'serious-problem') {
                tag = 1;
            }
            if(tag == 1){
                tempHtml = tempHtml.replace('warning_info','serious');
            }else{
                tempHtml = tempHtml.replace('warning_info','common')
            }
        }
        $("#error_detail").html(tempHtml);
        $("#serious").css("color","red");

        if (antiCheckInFlag && !antiCheckedInValue[advise.DRUG_LO_ID]) {
            showAntiCheckInInfo(advise);
        }
        showAppealBtn(drug_name, error_name, '${presId}');
    }

    /*
     * val:0下一步,-1返回修改
     * */
    function nextOrBack(val, needSendMessage) {
        if (needSendMessage != undefined && needSendMessage == true) {
            sendQuitMessage(val);
        }
        changeDirectCloseFlag();
        var url;
        var arg;
        if(1 == bz_flag && -2 == val) {
            var retXml = "<CheckResult STATE=\"-1\" STYLE=\"ManualNormal\" CHECK_PHARMACIST_CODE=\"" + pharmacistCode + "\" CHECK_PHARMACIST_NAME=\"" + pharmacistName + "\" CHECK_STATE=\"" + "干预成功" + "\" TAG=\"\" />";
            saveCheckMessage(retXml);
        }else if(1 == bz_flag && -1 == val){
            var retXml = "<CheckResult STATE=\"-1\" STYLE=\"\" CHECK_PHARMACIST_CODE=\"\" CHECK_PHARMACIST_NAME=\"\" CHECK_STATE=\"\" />";
            saveCheckMessage(retXml);
        }else if(1 == bz_flag && 0 == val){
//            var retXml = "<CheckResult STATE=\"2\" STYLE=\"ManualNormal\" CHECK_PHARMACIST_CODE=\"" + pharmacistCode + "\" CHECK_PHARMACIST_NAME=\"" + pharmacistName + "\" CHECK_STATE=\"\" TAG=\"\" />";
//            saveCheckMessage(retXml);
        }else{
            if(0 == bz_flag && -2 == val){
                val = -1;
            }
            url = "http://" + checkServerIp + ":" + checkServerPort + "/DCStation/submit/setRetValue";
            arg = 'presId=' + presId + '&retVal=' + val;
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
        }
        clearAllInterval();
        window.close();
    }


    function checkCanBeNext() {
        if (highestWarningLevel == -1 || antiCheckNumber > 0) {
            $("#next").attr('disabled', 'disabled');
            $("#next").css('background-image', 'url(http://${config.drStationServerIp}:${config.drStationServerPort}/DCStation/image/nextdisabled.png)');
            $("#next").css('background-repeat', 'no-repeat');
            $("#next").css('background-position', 'center');
        }
    }

    (function isDisabled() {
        checkCanBeNext();
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
        $("#appealBtn").unbind();
        //important
        $("#appealBtn").bind('click', function () {
            window.open("http://${config.drStationServerIp}:${config.drStationServerPort}/DCStation/appeal/appeal?presId=" + presId + "&drugName=" + drugName + "&errorName=" + errorName);
        })
    }

</script>

<%--<script>--%>
<%--window.onload = function () {--%>
<%--alert("start");--%>
<%--var width_th = document.getElementById("drug_name_th").style.width;--%>
<%--var width_td = document.getElementById("drug_name_td").style.width;--%>
<%--alert("th:" + width_th + "\n" + "td:" + width_td);--%>
<%--}--%>
<%--</script>--%>

</body>
</html>