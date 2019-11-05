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
    <base target="_self"/>
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
        //电子病历链接
        var medicalRecordURL = '${config.medicalRecordURL}';

        //返回数据 orderList
        var pharmacistOrderListJson = '${pharmacistOrderListJson}';

        //药师信息
        var pharmacistInfo = ${pharmacistInfo};
        <%--var pharmacistCheckResultJson = ${pharmacistCheckResultJson};--%>
        <%--//医生信息--%>
        <%--var doctorInfo = pharmacistCheckResultJson.checkPresInput.doctor;--%>
        <%--//患者信息--%>
        <%--var patientInfo = pharmacistCheckResultJson.checkPresInput.patient;--%>
        <%--//处方信息--%>
        <%--var orders = pharmacistCheckResultJson.checkPresInput.advices;--%>
        var pharmacistPresId = '${presId}';
        <%--var date = '${date}';--%>

        var problemNameList = new Array();
        var case_type_pharmacist;
        var case_description;
        var case_type_engin;
        var pharmacist_opinion;
        var orderInfoNext;

        var interfereInputXML = "";



        /********定义药品说明书模板********/
        var drug_specification = getTemplateByName("drug_specification_template");
        /********定义药品说明书模板********/



        /********定义interfereInfo_BZ模板********/
        var interfereInfoTemp_BZ = getTemplateByName("interfere_info_template_bz");
        /********定义interfereInfo_BZ模板********/


        /********定义个人详情模板********/
        var personInfo_BZ = getTemplateByName("personInfo_bz");
        /********定义interfereInfo_BZ模板********/

        /*-------------------add  by yikmat 开始----------------*/
        //给药品进行排序
        function sortDrug(type) {
            var url = "http://" + checkServerIp + ":" + checkServerPort + "/DCStation/pharmacistSubmit/pharmacistCheckResultPage_BZ?presId=" + pharmacistPresId + '&type=' + type+'&random=' + Math.random();
//            window.location.href = url;

                var link = document.createElement("a");
                link.href = url;
                document.body.appendChild(link);
                link.click();

        }

        //查看问题详情，打开新的页面
        function openProblemDetail(obj,patient_id,drug_name,order_id) {
            //  1.改变颜色，改变当前点击行的背景颜色
            $(obj).parent().parent().find("td").css("background","#edebec"); //red为颜色，你也可以输入颜色值#00000等等

            var orderJson = "";

            var pharmacistOrderList = JSON.parse(pharmacistOrderListJson);
            for (var i = 0; i < pharmacistOrderList.length; i++) {
                if(""!=orderJson){
                    break;
                }
                var orderList = pharmacistOrderList[i];
                for (var k = 0; k < orderList.orderList.length; k++) {
                    order = orderList.orderList[k];
                    if(patient_id==order.PATIENT_ID||drug_name==order.PATIENT_NAME||order_id==order.prescInfo.order_id){
                        orderJson = JSON.stringify(order);
                        break;
                    }
                }
            }
            //  2.打开新的一页
            drawInterfereInfoElem(orderJson);
        }

         // 将新页面画出来---------
        function drawInterfereInfoElem(item) {
            orderInfoNext =  JSON.parse(item);
            var interfereInfoElem = document.getElementById("checkResult");
            interfereInfoElem.innerHTML = interfereInfoTemp_BZ;
            var temp = interfereInfoTemp_BZ;
            temp = temp.replace('@(DOCTOR_NAME)',orderInfoNext.DOCTOR_NAME);
            temp = temp.replace('@(PHARMACIST_NAME)',pharmacistInfo.pharmacist_name);
            temp = temp.replace('@(PHARMACIST_ID)',pharmacistInfo.pharmacist_id);
            temp = temp.replace('@(PATIENT_ID)',orderInfoNext.PATIENT_ID);
            temp = temp.replace('@(VISIT_ID)',orderInfoNext.VISIT_ID);
            temp = temp.replace('@(PATIENT_NAME)',orderInfoNext.PATIENT_NAME);
            temp = temp.replace('@(PATIENT_PHONE)',pharmacistInfo.telephone);


            temp = temp.replace('PATIENT_ID_CLICK',orderInfoNext.PATIENT_ID);
            temp = temp.replace('VISIT_ID_CLICK',orderInfoNext.VISIT_ID);

            interfereInfoElem.innerHTML = temp;
            $("#problemDescribe").html($("#sum_errors").html());

            var error_detail = $('#error_detail').html();
            $('#error_detail').html('');
            var checkInfoList = orderInfoNext.prescInfo.checkInfos;
            var tempHtml  ="";
            for (var j = 0; j < checkInfoList.length; j++) {
                var checkInfo = checkInfoList[j];
                var temp = error_detail;
                tempHtml += temp.replace('@(drug_name)', orderInfoNext.DRUG_NAME).replace('@(warning_info)', checkInfo.WARNING_INFO)
                    .replace('@(ref_source)', checkInfo.REF_SOURCE);
            }
            $("#error_detail").html(tempHtml);


            showdiv();
        }

        //点击改变表格内容<发药、不发药、待定>
        function changeTdHtml(obj,orderGroupId) {
            //选出下一个要显示的文字
            var value = $(obj).html();
            var newValue = "";
            if("发药"==value){
                newValue = "不发药";
            }else if("不发药"==value){
                newValue = "待定";
            }else if("待定"==value){
                newValue = "发药";
            }

            //找到同一组的td,并切换文字
            var drugTableBody = document.getElementById("drugTableBody");//获取第一个表格
            var array = drugTableBody.getElementsByTagName("tr");//所有tr
            for (var i = 0; i < array.length; i++) {
                if (array[i].cells[15].innerText == orderGroupId){
                    array[i].cells[14].firstChild.innerText = newValue;
                }
            }


        }

        //获取当前时间------------
        function getNowFormatDate() {
            var date = new Date();
            var seperator1 = "-";
            var seperator2 = ":";
            var month = getNewDate(date.getMonth() + 1);
            var day = getNewDate(date.getDate());
            var hours = getNewDate(date.getHours());
            var minutes = getNewDate(date.getMinutes());
            var seconds = getNewDate(date.getSeconds());
            //统一格式为两位数
            function getNewDate(date) {
                if (date <= 9) {
                    date = "0" + date;
                }
                return date;
            }

            var currentDate = date.getFullYear() +""+ month +""+ day;
            return currentDate ;
        }

        //获取当前时间------------
        function getNowFormatTime() {
            var date = new Date();
            var seperator1 = "-";
            var seperator2 = ":";
            var month = getNewDate(date.getMonth() + 1);
            var day = getNewDate(date.getDate());
            var hours = getNewDate(date.getHours());
            var minutes = getNewDate(date.getMinutes());
            var seconds = getNewDate(date.getSeconds());
            //统一格式为两位数
            function getNewDate(date) {
                if (date <= 9) {
                    date = "0" + date;
                }
                return date;
            }

            var currentDate = date.getFullYear() + seperator1 + month + seperator1 + day
                + " " + hours + seperator2 + minutes + seperator2 + seconds;
            return currentDate;
        }

         //打开药品说明书------------
        function openDiscribLinked(code) {
            var urlTemp = disUrl.replace("@code@", code);
            drawCheckResultElem(urlTemp);
        }

        //打开电子病历--------------
        function openEMRLinked(patient_id,visit_id) {
            var urlTemp = medicalRecordURL.replace("PATIENT_ID", patient_id);
            urlTemp = urlTemp.replace("VISIT_ID", visit_id);
//            drawPersonInfoElem(urlTemp);
            window.open(urlTemp, "_blank", "scrollbars=yes,resizable=1,modal=false,alwaysRaised=yes");
//            window.open(urlTemp);
        }

        //发送干预  拼接干预的字符串
        function sendIntefere() {

            //----药师意见
            pharmacist_opinion = $("#problemDescribe").val();
            //去掉空格
            pharmacist_opinion = pharmacist_opinion.replace(/\ +/g,"");
            //去掉回车换行
            pharmacist_opinion = pharmacist_opinion.replace(/[\r\n]/g,"");

            //-----问题详情 以及  问题名称
            var trs = document.getElementsByClassName("errorDetailDiv");
            case_type_engin = "";
            case_description = "";
            for (var i = 0; i < trs.length; i++) {
                if (trs[i].innerHTML!=""){
                    var description = trs[i].innerHTML.replace("&nbsp;","").replace(/[\r\n]/g,"").replace(/\ +/g,"");
                    case_description += description;
                }
            }
            for(var j = 0; j < orderInfoNext.prescInfo.checkInfos.length; j++) {
                if (case_type_engin.indexOf(orderInfoNext.prescInfo.checkInfos[j].NAME) == -1) {
                    case_type_engin += orderInfoNext.prescInfo.checkInfos[j].NAME + ";"
                }
            }

            //----拼接字符串
            interfereInputXML = "<Request FUN='1'>" +
                "<Input DOCTOR_ID='"+orderInfoNext.DOCTOR_ID+"'" +
                " DOCTOR_NAME='"+orderInfoNext.DOCTOR_NAME+"' PHARMACIST_ID='"+ pharmacistInfo.pharmacist_id +"' PHARMACIST_NAME='"+ pharmacistInfo.pharmacist_name +"'" +
                " PHARMACIST_PHONE='"+ pharmacistInfo.telephone +"' PATIENT_ID='"+orderInfoNext.PATIENT_ID+"' PATIENT_NAME='"+orderInfoNext.PATIENT_NAME+"'" +
                " PRES_ID='"+orderInfoNext.PATIENT_ID+"' APPLY_DATE='"+getNowFormatDate()+"' PHARMACIST_OPINION='"+pharmacist_opinion+"' INPATIENT_NO='"+orderInfoNext.INPATIENT_NO+"'" +
                " CASE_DESCRIPTION='"+case_description+"' CASE_TYPE_ENGIN='"+case_type_engin+"' INTERVENTION_RESULT='0'/>" +
                "</Request>";

            //发送
            sendPharmacistInterfere(interfereInputXML,2)
            //使用table来存储时使用
//             for (var i = 0; i < trs.rows.length; i++) {
//                 if (trs.rows[i].cells[1].innerText.indexOf(problemNameList[j]) >= 0){
//                     case_type_pharmacist += trs.rows[i].cells[1].innerText;
//                     trs.rows[i].cells[0].children[0].checked = true;
//                 }
//                 for(var j = 0; j<trs.rows[i].cells.length; j++ ){    // 遍历该行的 td
//                     alert("第"+(i+1)+"行，第"+(j+1)+"个td的值："+rows[i].cells[j].innerHTML+"。");           // 输出每个td的内容
//                 }
//             }
//         alert(interfereInputXML)
        }

        //显示弹框
        function showdiv() {
            document.getElementById("bg").style.display = "block";
            document.getElementById("show").style.display = "block";
        }

        //隐藏弹框
        function hidediv() {
            document.getElementById("bg").style.display = 'none';
            document.getElementById("show").style.display = 'none';
        }


        //打开个人档案
        function drawPersonInfoElem(url) {
            var fn = function (personInfoElem) {
                personInfoElem.innerHTML = personInfo_BZ.replace('@(url)', url);
                document.getElementById("personInfoButton").click();
            };
            showPersonInfo(fn);
        }

        function showPersonInfo(fn) {
            var personInfoElem = document.getElementById("checkResult");
            fn(personInfoElem);
        }

        //打开药品说明书
        function drawCheckResultElem(url) {
            var fn = function (checkResultElem) {
                checkResultElem.innerHTML = drug_specification.replace('@(url)', url);
                document.getElementById("checkResultButton").click();
            };
            showDrugSpecResult(fn);
        }

        function showDrugSpecResult(fn) {
            var checkResultElem = document.getElementById("checkResult");
            fn(checkResultElem);
        }

        /*-------------------add  by   yikmat   结束----------------*/
        function getTemplateByName(name) {
            var url = 'http://${config.drStationServerIp}:${config.drStationServerPort}/DCStation/html/' + name + ".html";
            return sendAjaxRequest(url, '', "GET");
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
//            var total = document.getElementById("total");
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
                nextOrBackPharmacist(0);
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
         * 发药，继续进行
         * */
        function nextPharmacist() {
            //根据页面情况，直接赋值

            var returnXml = "<OrderList STATE=\"1\">";
            // 拼接字符串
            var drugTableBody = document.getElementById("drugTableBody");//获取第一个表格
            var array = drugTableBody.getElementsByTagName("tr");//所有tr
            for (var i = 0; i < array.length; i++) {
                var orderPlate = "<Order PATIENT_ID=@PATIENT_ID@ VISIT_ID=@VISIT_ID@ ORDER_NO=@ORDER_NO@ ORDER_SUB_NO=@ORDER_SUB_NO@ GROUP_ID=@GROUP_ID@ DISPENSE_STATE=@DISPENSE_STATE@ />";
                orderPlate =  orderPlate.replace("@VISIT_ID@",array[i].cells[1].firstChild.innerText);
                orderPlate = orderPlate.replace("@PATIENT_ID@",array[i].cells[16].innerText);
                orderPlate = orderPlate.replace("@ORDER_NO@",array[i].cells[17].innerText);
                orderPlate = orderPlate.replace("@ORDER_SUB_NO@",array[i].cells[18].innerText);
                orderPlate = orderPlate.replace("@GROUP_ID@",array[i].cells[19].innerText);

                if (array[i].cells[14].firstChild.innerText == "待定"){
                    orderPlate = orderPlate.replace("@DISPENSE_STATE@",0);
                }else if (array[i].cells[14].firstChild.innerText == "发药"){
                    orderPlate = orderPlate.replace("@DISPENSE_STATE@",1);
                }else if (array[i].cells[14].firstChild.innerText == "不发药"){
                    orderPlate = orderPlate.replace("@DISPENSE_STATE@",-1);
                }
                returnXml += orderPlate ;
            }
            returnXml += "</OrderList>" ;

            var url = "http://" + checkServerIp + ":" + checkServerPort + "/DCStation/pharmacistSubmit/setRetValue_bz";
            var arg = 'presId=' + pharmacistPresId + '&message=' + returnXml;
            var xmlhttp;
            if (window.XMLHttpRequest){
                //  IE7+, Firefox, Chrome, Opera, Safari
                xmlhttp = new XMLHttpRequest();
            } else{
                // IE6, IE5
                xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
            }
            xmlhttp.open("POST", url, false);
            xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded;");
            xmlhttp.send(arg);


            window.close();
        }

        /*
         * 不发药，返回修改
         * */
        function backPharmacist() {
            //根据页面情况，直接赋值

            var returnXml = "<OrderList STATE=\"-1\" />";

            var url = "http://" + checkServerIp + ":" + checkServerPort + "/DCStation/pharmacistSubmit/setRetValue_bz";
            var arg = 'presId=' + pharmacistPresId + '&message=' + returnXml;
            var xmlhttp;
            if (window.XMLHttpRequest){
                //  IE7+, Firefox, Chrome, Opera, Safari
                xmlhttp = new XMLHttpRequest();
            } else{
                // IE6, IE5
                xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
            }
            xmlhttp.open("POST", url, false);
            xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded;");
            xmlhttp.send(arg);

         window.close();
        }

    </script>

    <script type="text/javascript"
            src="${pageContext.servletContext.contextPath}/js/jquery.min.js"></script>
    <script type="text/javascript" src="${pageContext.servletContext.contextPath}/js/dcdt_${config.browserFlag}.js"></script>
    <script type="text/javascript"
            src="http://${config.drStationServerIp}:${config.drStationServerPort}/DCStation/js/jquery.xdomainrequest.min.js"></script>

    <link href="${pageContext.servletContext.contextPath}/css/pharmacistCheckResultPage${config.browserFlag}.css" rel="stylesheet" type="text/css"/>
    <style type="text/css">
        .main-table tbody tr:nth-child(even) td {
            background-color: #ffffff;
            color: black;
        }

        .main-table tbody tr:nth-child(odd) td {
            background-color: #ffffff;
            color: black;
        }

        #drugTable th{
            font-size: 14px;
            background-color:steelblue ;
        }

        #drugTable  td{
            font-size: 14px;
            background-color: white;
            border: 1px solid #d0d2d1;

        }

    </style>

</head>

<body>
<div>
    <div>
        <div class="main-content">
            <div class="inner-main-content" style="height: 700px;">
                <div class="head-top">
                    <div class="head-top left-head"></div>
                    <div class="head-top right-head">|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;合理用药实时审核Web端</div>
                </div>
                <div class="info-area">

                    <%--第一模块：按钮模块--%>
                    <div class="detail-info left-area" style="width: 480px;;background: #f3f7ff;border: none;height: 150px">
                        <div class="detail-content-left" style="padding: 10px;_padding:20px 10px;">
                            <table id="button-part">

                                    <div style="height: 60px;line-height: 60px;padding-left: 20px;">

                                    <input style="" id="" class="input_style" type="button" value="按床号升序排列" onclick="sortDrug(1)"  target=""/>
                                    <%--<a style="" id="" class="input_style" type="button" value="按床号升序排列" href=sortDrug(1)  target=""/>--%>

                                    <input id="" class="input_style" type="button" value="按姓名升序排列" onclick="sortDrug(2)"/>

                                    <input style="width: 36%" id="" class="input_style" type="button" value="按问题严重程度升序排列" onclick="sortDrug(5)"/>
                                    </div>

                                    <div style="height: 60px;line-height: 60px;padding-left: 20px;">
                                    <input id="" class="input_style" type="button" value="按床号降序排列" onclick="sortDrug(2)"/>
                                    <input id="" class="input_style" type="button" onclick="sortDrug(4)" value="按姓名降序排列"/>
                                    <input  style="width: 36%" id="" class="input_style" type="button" onclick="sortDrug(6)" value="按问题严重程度降序排列"/>
                                    </div>
                            </table>
                        </div>
                    </div>

                    <%--第二模块：文字指示模块--%>
                    <div class="detail-info right-area" style="width: 480px;background: #f3f7ff;border: none; font-size: 13px;color: blue;height: 150px">
                        1.医嘱组是排序的最小单位，问题最严重的医嘱排在前面。并且第一条医嘱代表该组是否发药。<br>
                        2.医嘱是用药干预的单位。<br>
                        3.鼠标左键单击住院号，可以调阅患者的电子病历。<br>
                        4.鼠标左键单击药品名称，可以调阅药品说明书。<br>
                        5.鼠标左键点击问题详情，可以查看问题详情，并进行用药干预。白色背景表示未读，浅灰色背景表示已读。<br>
                        6.鼠标左键单击是否发药，可以按顺序切换它所在的医嘱组发药、不发药、待定。<br>
                        <%--<div class="detail-content-right" style="padding-bottom: 20px;margin-top: -20px" id="sum_errors">--%>
                            <%----%>

                        <%--</div>--%>
                    </div>
                </div>


                <%--<div class="table-area">
                    <div class="table-area-head">--%>
                <div style="width: 100%;overflow-y: auto;overflow-x: hidden;height: 440px">
                        <table id="drugTable" class="main-table-head" style="background-color: steelblue;margin-left: 0px;width: 100%;">
                            <thead>
                            <tr>
                                <th style="width:5%;color: white">床号</th>
                                <th style="width:5%;color: white">住院号</th>
                                <th style="width:7%;color: white">姓名</th>
                                <th style="width:13%;color: white">药品名称</th>
                                <th style="width:5%;color: white">规格</th>
                                <th style="width:5%;color: white">医嘱<br>类别</th>
                                <th style="width:5%;color: white">开始时间</th>
                                <th style="width:5%;color: white">给药<br>途径</th>
                                <th style="width:5%;color: white">给药<br>频次</th>
                                <th style="width:5%;color: white">单次<br>剂量</th>
                                <th style="width:10%;color: white">服药时间</th>
                                <th style="width:5%;color: white">问题类型</th>
                                <th style="width:10%;color: white">问题级别</th>
                                <th style="width:5%;color: white">问题详情</th>
                                <th style="width:10%;width: 120px;color: white">是否发药</th>
                            </tr>
                            </thead>

                            <tbody id="drugTableBody">
                            <c:forEach var="orderInfo" items="${pharmacistOrderList}">
                                <c:forEach items="${orderInfo.orderList}" var="item">
                                    <tr>
                                        <td style="width:5%;">${item.BED_NO}</td>
                                        <td style="width:5%;cursor:pointer;"><a onclick="openEMRLinked('${item.PATIENT_ID}','${item.VISIT_ID}')">${item.VISIT_ID}</a></td>
                                        <td style="width:7%;">${item.PATIENT_NAME}</td>
                                        <td style="width:13%;cursor:pointer;">  <a onclick="openDiscribLinked('${item.prescInfo.drug_lo_id}')">${item.prescInfo.kh}${item.DRUG_NAME}</a></td>
                                        <td style="width:5%;">${item.DRUG_SPEC}</td>
                                        <td style="width:5%;">${item.ORDER_TYPE}</td>
                                        <td style="width:5%;">${item.START_TIME}</td>
                                        <td style="width:5%;">${item.ADMINISTRATION}</td>
                                        <td style="width:5%;">${item.FREQUENCY}</td>
                                        <td style="width:5%;">${item.DOSAGE}</td>
                                        <td style="width:10%;">${item.USE_TIME}</td>
                                        <td style="width:5%;">${item.PROBLEM_TYPE}</td>
                                        <td style="width:10%;">${item.PROBLEM_LEVEL}</td>
                                        <td style="width:5%;cursor:pointer;"> <a   onclick="openProblemDetail(this,'${item.PATIENT_ID}','${item.DRUG_NAME}','${item.prescInfo.order_id}')">问题详情</a></td>
                                        <td  style="width:10%;cursor:pointer;"><a   onclick="changeTdHtml(this,'${orderInfo.orderGroupId}')">发药</a></td>
                                        <td style="display: none" >${orderInfo.orderGroupId}</td>
                                        <td style="display: none" >${item.PATIENT_ID}</td>
                                        <td style="display: none" >${item.prescInfo.order_id}</td>
                                        <td style="display: none" >${item.prescInfo.order_sub_id}</td>
                                        <td style="display: none" >${item.prescInfo.group_id}</td>
                                    </tr>
                                </c:forEach>
                            </c:forEach>
                            </tbody>
                        </table>
            </div>
                   <%-- </div>
                </div>--%>

                <div class="button-area" style="height: 36px;line-height: 36px;">
                    <%--<input id="doctorInfo" class="long" type="button" value="医生信息" onclick="drawDoctorInfoELem()"/>--%>

                    <%--<input id="patientInfo" class="long" type="button" value="患者信息" onclick="drawPatientInfoElem()"/>--%>

                    <%--<input id="orderInfo" class="long" type="button" value="处方信息" onclick="drawOrdersInfoElem()"/>--%>


                    <%--&lt;%&ndash;<input id="help" type="button" value="帮助"/>&ndash;%&gt;--%>
                    <%--<input id="interfere" class="long" type="button" value="实施干预" onclick="drawInterfereInfoElem()"/>--%>
                    <input id="back" class="long" type="button" onclick="backPharmacist()" value="取消"/>
                    &nbsp;&nbsp;&nbsp;&nbsp;
                    <input id="next" class="long" type="button" onclick="nextPharmacist()" value="确定"/>
                </div>


            </div>
        </div>
    </div>

</div>

<div id="checkResult">
</div>









<script type="text/javascript">



    //将表格中的问题等级显示成不同的颜色
    var drugTableBody = document.getElementById("drugTableBody");//获取第一个表格
    var array = drugTableBody.getElementsByTagName("tr");//所有tr
    for (var i = 0; i < array.length; i++) {
        if (array[i].cells[12].innerText == "强制阻断"||array[i].cells[12].innerText == "强制登记"){
            array[i].cells[12].setAttribute('style', 'color: red !important');
        }else if(array[i].cells[12].innerText == "慎用"||array[i].cells[12].innerText == "提示"){
            array[i].cells[12].setAttribute('style', 'color: #CC9900 !important');
        }else if(array[i].cells[12].innerText == "禁忌"||array[i].cells[12].innerText == "禁用"){
            array[i].cells[12].setAttribute('style', 'color: #FF9900 !important');
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
//
//    function nextForChrome(val) {
//        if (val == 0) {
//            parent.check_for_next();
//        } else if (val == -1) {
//            parent.check_for_back();
//        }
//    }





</script>
</body>
</html>