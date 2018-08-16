<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: wtwang
  Date: 2017/11/29
  Time: 10:01
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

    <script type="text/javascript">
        var checkServerIp = '${config.drStationServerIp}';
        var checkServerPort = '${config.drStationServerPort}';
        //药品说明书链接
        var disUrl = '${config.drugDescriptionURL}';
        var presId = '${presId}';
        //药师信息
        var pharmacistInfo = ${pharmacistInfo};
        var pharmacistCheckResultJson = ${pharmacistCheckResultJson};
        //医生信息
        var doctorInfo = pharmacistCheckResultJson.checkPresInput.doctor;
        //患者信息
        var patientInfo = pharmacistCheckResultJson.checkPresInput.patient;
        //处方信息
        var orders = pharmacistCheckResultJson.checkPresInput.advices;

        var date = '${date}'

        var problemNameList = new Array();
        var case_type_pharmacist;
        var case_description;
        var case_type_engin;

        var interfereInputXML = "";

        /********定义iframe模板********/
        var checkResultTemp = getTemplateByName("check_result_template");
        /********定义iframe模板********/

        /********定义药品说明书模板********/
        var drug_specification = getTemplateByName("drug_specification_template");
        /********定义药品说明书模板********/

        /********定义doctorInfo模板********/
        var doctorInfoTemp = getTemplateByName("doctor_info_template");
        /********定义doctorInfo模板********/

        /********定义patientInfo模板********/
        var patientInfoTemp = getTemplateByName("patient_info_template");
        /********定义patientInfo模板********/

        /********定义ordersInfo模板********/
        var ordersInfoTemp = getTemplateByName("orders_info_template");
        /********定义ordersInfo模板********/

        /********定义interfereInfo模板********/
        var interfereInfoTemp = getTemplateByName("interfere_info_template");
        /********定义interfereInfo模板********/


        function getTemplateByName(name) {
            var url = 'http://${config.drStationServerIp}:${config.drStationServerPort}/DCStation/html/' + name + ".html";
            return sendAjaxRequest(url, '', "GET");
        }

        function drawCheckResultElem(url) {
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

        function drawDoctorInfoELem() {
            var doctorInfoElem = document.getElementById("checkResult");
            var temp = doctorInfoTemp;
            for(var i in doctorInfo){
                temp = temp.replace('@(doctor.'+i+')',doctorInfo[i]);
            }
            doctorInfoElem.innerHTML = temp;
//            doctorInfoElem.innerHTML = doctorInfoTemp.replace('@(doctor.NAME)',doctorInfo.NAME).replace('@(doctor.POSITION)',doctorInfo.POSITION).replace('@(doctor.USER_ID)',doctorInfo.USER_ID).replace('@(doctor.DEPT_NAME)',doctorInfo.DEPT_NAME).replace('@(doctor.DEPT_CODE)',doctorInfo.DEPT_CODE)
            showdiv();
        }

        function drawPatientInfoElem() {
            var patientInfoElem = document.getElementById("checkResult");
            var temp = patientInfoTemp;
            for(var i in patientInfo){
                temp = temp.replace('@(patient.'+i+')',patientInfo[i]);
            }
            patientInfoElem.innerHTML = temp;
//            patientInfoElem.innerHTML = patientInfoTemp.replace('@(patient.NAME)',patientInfo.NAME).replace('@(patient.GENDER)',patientInfo.GENDER).replace('@(patient.BIRTH)',patientInfo.BIRTH).replace('@(patient.ID)',patientInfo.ID).replace('@(patient.VISIT_ID)',patientInfo.VISIT_ID).replace('@(patient.HEIGHT)',patientInfo.HEIGHT).replace('@(patient.WEIGHT)',patientInfo.WEIGHT).replace('@(patient.ALERGY_DRUGS)',patientInfo.ALERGY_DRUGS).replace('@(patient.PREGNANT)',patientInfo.PREGNANT).replace('@(patient.LACT)',patientInfo.LACT).replace('@(patient.HEPATICAL)',patientInfo.HEPATICAL).replace('@(patient.RENAL)',patientInfo.RENAL).replace('@(patient.PANCREAS)',patientInfo.PANCREAS);
            showdiv();
        }

        function drawOrdersInfoElem() {
            var ordersInfoElem = document.getElementById("checkResult");
            ordersInfoElem.innerHTML = ordersInfoTemp;
            var ordersTableBodyElem = document.getElementById("ordersTableBody");
            var temp = "";
            for(var i = 0;i < orders.length;i++){
                order = orders[i];
                temp += ordersTableBodyElem.innerHTML;
                var tb = document.getElementById('ordersTableBody');
                var rows = tb.rows;
                for(var j=0;j<rows.length;j++){
                    for(var k=0;k<rows[j].cells.length;k++){
                        temp = temp.replace(rows[j].cells[k].innerHTML,order[rows[j].cells[k].innerHTML]);
                    }
                }
            }
            var explorer = window.navigator.userAgent.toLowerCase();
            var type = "unknow";
            var version = "unknow";
            if (explorer.indexOf("msie") >= 0) {
                type = "IE";
                version = explorer.match(/msie ([\d.]+)/)[1];
            }else if (explorer.indexOf("chrome") >= 0) {
                type = "Chrome";
                version = explorer.match(/chrome\/([\d.]+)/)[1];
            }

            if(type == 'IE' && version == '8.0') {
                setTBodyInnerHTML(ordersTableBodyElem, temp);
            }else {
                ordersTableBodyElem.innerHTML = temp;
            }
            showdiv();
        }

        function setTBodyInnerHTML(tbody, html) {
            var div = document.createElement('div')
            div.innerHTML = '<table>' + html + '</table>'
            tbody.parentNode.replaceChild(div.firstChild.firstChild, tbody)
        }

        function getExplorerInfo() {
            var explorer = window.navigator.userAgent.toLowerCase();
            //ie
            if (explorer.indexOf("msie") >= 0) {
                var ver = explorer.match(/msie ([\d.]+)/)[1];
                return { type: "IE", version: ver };
            }
            //firefox
            else if (explorer.indexOf("firefox") >= 0) {
                var ver = explorer.match(/firefox\/([\d.]+)/)[1];
                return { type: "Firefox", version: ver };
            }
            //Chrome

            //Opera
            else if (explorer.indexOf("opera") >= 0) {
                var ver = explorer.match(/opera.([\d.]+)/)[1];
                return { type: "Opera", version: ver };
            }
            //Safari
            else if (explorer.indexOf("Safari") >= 0) {
                var ver = explorer.match(/version\/([\d.]+)/)[1];
                return { type: "Safari", version: ver };
            }
        }

        function drawInterfereInfoElem() {
            var interfereInfoElem = document.getElementById("checkResult");
            interfereInfoElem.innerHTML = interfereInfoTemp;
            var temp = interfereInfoTemp;
            temp = temp.replace('@(DOCTOR_NAME)',doctorInfo.NAME);
            temp = temp.replace('@(PATIENT_ID)',patientInfo.ID);
            temp = temp.replace('@(PATIENT_NAME)',patientInfo.NAME);
            temp = temp.replace('@(PHARMACIST_ID)',pharmacistInfo.pharmacist_id);
            temp = temp.replace('@(PHARMACIST_NAME)',pharmacistInfo.pharmacist_name);
            temp = temp.replace('@(TELEPHONE)',pharmacistInfo.telephone);
            interfereInfoElem.innerHTML = temp;
            $("#problemDescribe").html($("#sum_errors").html());
            case_description = $("#problemDescribe").text();
            //去掉空格
            case_description = case_description.replace(/\ +/g,"");
            //去掉回车换行
            case_description = case_description.replace(/[\r\n]/g,"");
            checkForProblemType();

            interfereInputXML = "<Request FUN='1'>" +
                    "<Input DOCTOR_ID='"+doctorInfo.USER_ID+"'" +
                    " DOCTOR_NAME='"+doctorInfo.NAME+"' PHARMACIST_ID='"+ pharmacistInfo.pharmacist_id +"' PHARMACIST_NAME='"+ pharmacistInfo.pharmacist_name +"'" +
                    " PHARMACIST_PHONE='"+ pharmacistInfo.telephone +"'PATIENT_ID='"+patientInfo.ID+"'PATIENT_NAME='"+patientInfo.NAME+"'" +
                    " PRES_ID='"+presId+"' APPLY_DATE='"+date+"' CASE_TYPE_PHARMACIST='"+case_type_pharmacist+"'" +
                    " CASE_DESCRIPTION='"+case_description+"' CASE_TYPE_ENGIN='"+case_type_engin+"'/>" +
                    "</Request>";
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

        function openDoctorInfo() {
            drawDoctorInfoELem();
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

        function sendQuitMessage(val) {
            if (val == -1) {
                sendRealMessage("修改处方");
            } else {
                sendRealMessage("下一步");
            }
        }

        function changeDirectCloseFlag() {
            directCloseFlag = 0;
        }

        /*
         * val:0下一步,-1返回修改
         * */
        function nextOrBack(val) {
            var url = "http://" + checkServerIp + ":" + checkServerPort + "/DCStation/pharmacistSubmit/setRetValue";
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

//            clearAllInterval();
            window.close();
        }


    </script>

    <script type="text/javascript"
            src="${pageContext.servletContext.contextPath}/js/jquery.min.js"></script>
    <link href="${pageContext.servletContext.contextPath}/css/pharmacistCheckResultPage${config.browserFlag}.css" rel="stylesheet" type="text/css"/>

    <style type="text/css">
    </style>
</head>
<body>
<div>
    <div>
        <div class="main-content">
            <div class="head-top">
                <%--<p></p>--%>
            </div>
            <%--<div class="head-info-lg">--%>
                <%--<p>审查结果</p>--%>
            <%--</div>--%>
            <div class="info-area">
                <div class="detail-info left-area">
                    <div class="head-info-sm"><p>详细信息</p></div>
                    <div class="detail-content-left" style="padding: 10px;_padding:20px 10px;">
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
                    <div class="head-info-sm"><p>问题汇总</p></div>
                    <div class="detail-content-right" id="sum_errors">

                    </div>
                </div>
            </div>

            <div class="button-area">
                <input id="sum" type="button" value="汇总" style="margin-top: 30px" onclick="sumUpProblems()"/>
                <input id="clear" type="button" value="清空" style="margin-top: 30px" onclick="clearProblems()"/>
            </div>

            <div class="table-area">
                <div class="table-area-head">
                    <table class="main-table-head">
                        <thead>
                        <tr>
                            <th style="width: 140px">药品名称</th>
                            <th>适应症</th>
                            <th>禁忌症<br>慎用症</th>
                            <th>用法<br>用量</th>
                            <th>重复<br>用药</th>
                            <th>相互<br>作用</th>
                            <th>配伍<br>禁忌</th>
                            <th>特殊<br>人群</th>
                            <th>药敏</th>
                            <th>医院<br>管理</th>
                            <th style="width: 82px">处方<br>ID</th>
                        </tr>
                        </thead>
                    </table>
                </div>
                <div class="table-area-content">
                    <table class="main-table">
                        <tbody>
                        <c:forEach var="item" items="${pharmacistCheckResult.checkPresOutput.prescInfos}">
                            <tr>
                                <td style="width: 132px;">
                                    <a onclick="openDiscribLinked('${item.drug_lo_id}')">${item.drug_lo_name}</a>
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
                                <td style="width: 80px">${item.order_id}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="info-area">
                <div class="down-detail-info left-area">
                    <div class="head-info-sm"><p>干预操作说明</p></div>
                    <div class="down-detail-content" style="margin-top: -30px">
                        实施干预步骤：1）鼠标左键单击要干预的药品的问题；2）点击“汇总”按钮；3）如果有多个问题要干预，则重复步骤1）和2）；4）点击“实施干预”按钮。
                    </div>
                </div>
                <div class="down-detail-info right-area">
                    <div class="head-info-sm"><p>干预情况</p></div>
                    <div class="down-detail-content" >
                        ${checkPharmacist.pharmacistCheck}
                    </div>
                </div>
            </div>

            <div class="button-area">
                <input id="doctorInfo" type="button" value="医生信息" onclick="drawDoctorInfoELem()"/>

                <input id="patientInfo" type="button" value="患者信息" onclick="drawPatientInfoElem()"/>

                <input id="orderInfo" type="button" value="处方信息" onclick="drawOrdersInfoElem()"/>


                <input id="help" type="button" value="帮助"/>
                <input id="interfere" type="button" value="实施干预" onclick="drawInterfereInfoElem()"/>
                <input id="back" type="button" onclick="nextOrBack(-1)" value="返回"/>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <input id="next" type="button" onclick="nextOrBack(0)" value="下一步"/>
            </div>

        </div>
    </div>

</div>

<div id="checkResult">
</div>

<script type="text/javascript">
    var pharmacistCheckResultJson = ${pharmacistCheckResultJson};
    var checkPresInput = pharmacistCheckResultJson.checkPresInput;
    var checkPresOutput = pharmacistCheckResultJson.checkPresOutput;
    var prescInfos = checkPresOutput.prescInfos;
    var problemType = ['适应症', '禁忌症慎用症', '用法用量', '重复用药', '相互作用', '配伍禁忌', '特殊人群', '药敏', '医院管理'];
    var problemLevelClassName = ['disaster-problem', 'common-problem', 'common-problem', 'serious-problem'];
    var error_detail = $('#error_detail').html();
    $('#error_detail').html('');

    var highestLevel = -2;
    for (var i = 0; i < prescInfos.length; i++) {
        var prescInfo = prescInfos[i];
        var checkInfoList = prescInfo.checkInfos;
        if (null != checkInfoList) {
            for (var j = 0; j < checkInfoList.length; j++) {
                var tag = 0;
                var checkInfo = checkInfoList[j];
                var curProblemLevel = -1;
                for (var k = 0; k < problemType.length; k++) {
                    if (checkInfo.NAME == problemType[k]) {
                        var problemLevel = parseInt(checkInfo.REGULAR_WARNING_LEVEL) + 1;
                        var $chooseTd = $(".main-table tbody").children().eq(i).children().eq(k + 1);
                        //如果问题等级是-1（拦截）或者问题等级大于当前等级，则更改图标
                        if (problemLevel == 0 || curProblemLevel < problemLevel) {
                            var className = problemLevelClassName[problemLevel];
                            $chooseTd.attr('class', className);
                            curProblemLevel = problemLevel == 0 ? 10 : problemLevel;
                        }
                        $chooseTd.click({row: i, col: k}, function (event) {
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
    }

    function showProblemDetail(data) {
        String.prototype.trim = function () {
            return this.replace(/(^\s*)|(\s*$)/g, "");
        }
        var row = data.row;
        var col = data.col;

        var drug_name = $(".main-table tbody").children().eq(row).children().eq(0).children().html().replace(' ', '');
        var error_name = problemType[col];
        $("#error_detail").html('');
        var tempHtml = "<thead id='problemName'><tr><th>" + error_name + "</th></tr></thead>";

        for (var i = 0; i < prescInfos.length; i++) {
            var prescInfo = prescInfos[i];
            if (prescInfo.drug_lo_name != drug_name) {
                continue;
            }
            var checkInfoList = prescInfo.checkInfos;
            for (var j = 0; j < checkInfoList.length; j++) {
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
        }
        $("#error_detail").html(tempHtml);
        $("#serious").css("color","red");
    }


    function sumUpProblems() {
        var error_detail = $("#error_detail").html();
        var sum_errors = $("#sum_errors").html();
        var problemName = document.getElementById("problemName").innerText;
        //去掉空格
        problemName = problemName.replace(/\ +/g,"");
        //去掉回车换行
        problemName = problemName.replace(/[\r\n]/g,"");
        problemNameList.push(problemName);
        sum_errors += error_detail;
        $("#sum_errors").html(sum_errors);
    }

    function clearProblems() {
        $("#sum_errors").html('');
        problemNameList.length = 0;
    }

    function checkForProblemType() {
        var trs = document.getElementById("problemTypeTbody");
        case_type_engin = "";
        case_type_pharmacist = "";
        for(var j=0;j<problemNameList.length;j++) {
            case_type_engin += problemNameList[j] + ";";
            for (var i = 0; i < trs.rows.length; i++) {
                if (trs.rows[i].cells[1].innerText.indexOf(problemNameList[j]) >= 0){
                    case_type_pharmacist += trs.rows[i].cells[1].innerText;
                    trs.rows[i].cells[0].children[0].checked = true;
                }
            }
        }
    }


    (function isDisabled() {
        var t = '${checkResult.HIGHEST_WARNING_LEVEL}';
        if (t == -1) {
            $("#next").attr('disabled', 'disabled');
            $("#next").css('background-image', 'url(http://${config.drStationServerIp}:${config.drStationServerPort}/DCStation/image/nextdisabled.png)');
            $("#next").css('background-repeat', 'no-repeat');
            $("#next").css('background-position', 'center');
        }
    })();


    (function myBrowser() {
        var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
        if (userAgent.indexOf("Chrome") > -1) {
            $("#back").click({val: -1}, function (event) {
                nextForChrome(event.data.val);
            });
            $("#next").click({val: 0}, function (event) {
                nextForChrome(event.data.val);
            });
        }
    })();

    function nextForChrome(val) {
        if (val == 0) {
            parent.check_for_next();
        } else if (val == -1) {
            parent.check_for_back();
        }

    }
</script>
</body>
<script type="text/javascript" src="${pageContext.servletContext.contextPath}/js/dcdt_Chrome.js"></script>
</html>