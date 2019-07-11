﻿/**
 * Created by on 2017/5/14.
 */
/**
 * 住院医生站ip，端口。
 * 如果是旧接口，即住院和门诊是分开的两个文件，则checkServerIpInHos等同于原来的checkServerIp。
 * checkServerPortInHos等同于原来的checkServerPort。
 * DoctorCheck函数的调用和原来相同。
 * @type {string}
 */
var checkServerIpInHos = "localhost";
var cheServerPortInHos = "80";
/**
 * 门诊医生站ip，端口
 * @type {string}
 */
var checkServerIpOutHos = "localhost";
var cheServerPortOutHos = "80";
//说明书地址
var disUrl = 'http://192.168.11.67:8040/DCStation/home/index?drugCode=@code@';
//医生站超时返回的最长时间(毫秒)
var timeStrapDoc = 5000;
//药师站超时返回的最长时间(毫秒)
var timeStrapPhar = 5000;
//区分昌平药师站，1为昌平，0为其他，默认0
var cpFlag = 0;

var checkServerIp;
var checkServerPort;

function testCheck(tag) {
    var dcdtXml = document.getElementById("dcdt").value;
    DoctorCheck(tag, dcdtXml,1);
}
/**
 *
 * @param tag
 * @param xml
 * @param inHosFlag 住院门诊标识，1住院，0门诊 。默认使用住院医生站。
 * 常德人民医院inHosFlag中，inp住院，onp门诊
 * @constructor
 */
function DoctorCheck(tag, xml, inHosFlag) {
    if(getOS() != "Win7") {
        writeReg();
    }
    if (inHosFlag == undefined) {
        inHosFlag = 1;
    }
    if (inHosFlag == 0) {
        checkServerIp = checkServerIpOutHos;
        checkServerPort = cheServerPortOutHos;
        return sendCheck(tag, xml, checkServerIpOutHos, cheServerPortOutHos);
    } else if (inHosFlag == 1) {
        checkServerIp = checkServerIpInHos;
        checkServerPort = cheServerPortInHos;
        return sendCheck(tag, xml, checkServerIpInHos, cheServerPortInHos);
    } else {
        // alert("error:未识别的住院标识！");
        return -4;
    }
}

function sendCheck(tag, xml, checkServerIp, cheServerPort) {
    var iWidth = '1000px';
    var iHeight = '547px';
    var xmlhttp;
    var data = "xml=" + encodeURIComponent(xml) + '&' + 'tag=' + tag;

    if (window.XMLHttpRequest) {
        //  IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp = new XMLHttpRequest();
    } else {
        // IE6, IE5
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }

    var t1;
    function adduserok() {
        if (t1)
            clearTimeout(t1);
    }
    function connecttoFail() {
        if (xmlhttp)
            xmlhttp.abort();
        // alert("请求服务超时！");
    }
    if(xmlhttp) {
        t1 = setTimeout(connecttoFail, timeStrapDoc);
        try {
            ajax(xmlhttp, "POST", "http://" + checkServerIp + ":" + cheServerPort + "/DCStation/submit/sendCheck", data, adduserok);
        }catch (e){
            return -3;
        }
    }else {
        alert("Init xmlhttprequest fail");
    }



    // xmlhttp.open("POST", "http://" + checkServerIp + ":" + cheServerPort + "/DCStation/submit/sendCheckForTest", false);
    // xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded;");
    // xmlhttp.send(data);

    var checkData = xmlhttp.responseText;
    if(checkData == ""){
        return -3;
    }
    var check = eval("(" + checkData + ")");

    // var url = "http://" + checkServerIp + ":" + cheServerPort + "/DCStation/submit/sendCheck";
    // var checkData;
    // var check;
    //
    // $.ajax({
    //     type: 'POST',
    //     url: "http://" + checkServerIp + ":" + cheServerPort + "/DCStation/submit/sendCheck",
    //     async: true,
    //     data: data,
    //     contentType: 'text/plain',
    //     dataType: 'jsonp',
    //     jsonp: 'callback',
    //     jsonpCallback: 'successCallback',
    //     success: function (rtData) {
    //         alert("a");
    //         checkData = rtData.responseText;
    //         check = eval("(" + checkData + ")");
    //     }
    // });


    /**
     * 沈阳医大一院科室过滤代码
    var start = xml.indexOf("DEPT_CODE=\"");
    var rest = xml.substr(start + 11);
    var stop = rest.indexOf("\"");
    var dept_code = rest.substr(0, stop);

    if (check.hasProblem == 1 && dept_code == '肠道门诊') {
        alert("处方没有问题！");
    }
     **/
    if(check.hasProblem == -2){
        if (xmlhttp)
            xmlhttp.abort();
        // alert("请求中间层服务异常！");
        return -2;
    }
    else if (tag == 2 || check.hasProblem == 0) {
        if(tag == 2){
            if(t1){
                clearTimeout(t1);
            }
        }
        return 0;
    }
    else if (check.hasProblem == 1) {
        var url = "http://" + checkServerIp + ":" + cheServerPort + "/DCStation/submit/checkResultPage?presId=" + check.presId + '&random=' + Math.random();

        if (navigator.userAgent.indexOf("Chrome") > 0) {
            var winOption = "height=" + iHeight + ",width=" + iWidth + "," +
                "top=50,left=50,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,fullscreen=0";
            window.open(url, window, winOption);
        } else {
            window.showModalDialog(url, '',
                'resizable:yes;scroll:yes;status:no;' +
                'dialogWidth=' + iWidth +
                ';dialogHeight=' + iHeight +
                ';center=yes;help=yes');
        }
    }

    xmlhttp.open("POST", "http://" + checkServerIp + ":" + cheServerPort + "/DCStation/submit/getRetValue", false);
    xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded;");
    xmlhttp.send('presId=' + check.presId);

    var data = xmlhttp.responseText;
    data = eval("(" + data + ")");
    return data;
}

function ajax(xmlhttp, _method, _url, _param, _callback) {
    if (typeof xmlhttp == 'undefined')
        return;
    xmlhttp.onreadystatechange = function() {
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            _callback(xmlhttp);
        }
    };
    xmlhttp.open(_method, _url, false);
    if (_method == "POST") {
        xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded;");
        xmlhttp.send(_param);
    } else {
        xmlhttp.send(null);
    }
}


function isDirectClose(data) {
    return data == -2;
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
    window.open(urlTemp, '药品说明书', ' left=0,top=0,width=' + (screen.availWidth - 10) + ',height=' + (screen.availHeight - 50) + ',scrollbars,resizable=yes,toolbar=no');
}

//药师站
function testPharmacistCheck(tag) {
    var patientID = document.getElementById("patientID").value;
    var visitDate = document.getElementById("visitDate").value;
    var pharmacistInfo = document.getElementById("pharmacistInfo").value;
    var dcdtXml = document.getElementById("dcdt").value;
    PharmacistCheck(tag,patientID,visitDate,pharmacistInfo,dcdtXml,1);
}

function testPharmacistCheckSilent(tag) {
    var patientID = document.getElementById("patientID").value;
    var visitDate = document.getElementById("visitDate").value;
    var pharmacistInfo = document.getElementById("pharmacistInfo").value;
    var dcdtXml = document.getElementById("dcdt").value;
    PharmacistCheckSilent(tag,patientID,visitDate,pharmacistInfo,dcdtXml,1);
}

//药师站昌平
function testPharmacistCheck_CP() {
    var visitDate = document.getElementById("visitDate").value;
    var pharmacistInfo = document.getElementById("pharmacistInfo").value;
    var dcdtXml = document.getElementById("dcdt").value;
    PharmacistCheck_CP(visitDate,pharmacistInfo,dcdtXml,1);
}

function testPharmacistCheckSilent_CP() {
    var dcdtXml = document.getElementById("dcdt").value;
    PharmacistCheckSilent_CP(dcdtXml,1);
}

function PharmacistCheck(tag,patientID,visitDate,pharmacistInfo,xml,inHosFlag) {
    if (inHosFlag == undefined) {
        inHosFlag = 1;
    }
    if(cpFlag == 1){
        if (inHosFlag == 0) {
            return sendPharmacistCheck_CP(visitDate, pharmacistInfo, xml, checkServerIpOutHos, cheServerPortOutHos);
        } else if (inHosFlag == 1) {
            return sendPharmacistCheck_CP(visitDate, pharmacistInfo, xml, checkServerIpInHos, cheServerPortInHos);
        } else {
            // alert("error:未识别的住院标识！");
            return -4;
        }
    }else {
        if (inHosFlag == 0) {
            return sendPharmacistCheck(tag, patientID, visitDate, pharmacistInfo, xml, checkServerIpOutHos, cheServerPortOutHos);
        } else if (inHosFlag == 1) {
            return sendPharmacistCheck(tag, patientID, visitDate, pharmacistInfo, xml, checkServerIpInHos, cheServerPortInHos);
        } else {
            // alert("error:未识别的住院标识！");
            return -4;
        }
    }
}

function PharmacistCheckSilent(tag,patientID,visitDate,pharmacistInfo,xml,inHosFlag) {
    if (inHosFlag == undefined) {
        inHosFlag = 1;
    }
    if (inHosFlag == 0) {
        return sendPharmacistCheckSilent(tag, patientID, visitDate, pharmacistInfo, xml, checkServerIpOutHos, cheServerPortOutHos);
    } else if (inHosFlag == 1) {
        return sendPharmacistCheckSilent(tag, patientID, visitDate, pharmacistInfo, xml, checkServerIpInHos, cheServerPortInHos);
    } else {
        // alert("error:未识别的住院标识！");
        return -4;
    }
}

function sendPharmacistCheck(tag,patientID,visitDate,pharmacistInfo,xml,checkServerIp, checkServerPort){
    var iWidth = '1000px';
    var iHeight = '700px';
    var xmlhttp;
    var data = "xml=" + encodeURIComponent(xml) + '&' + 'tag=' + tag + '&' + 'patientID=' + patientID + '&' + 'visitDate=' + visitDate + '&' + 'pharmacistInfo=' + pharmacistInfo;
    if (window.XMLHttpRequest) {
        //  IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp = new XMLHttpRequest();
    } else {
        // IE6, IE5
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }

    var t1;
    function adduserok(xmlhttp) {
        if (t1)
            clearTimeout(t1);
    }
    function connecttoFail() {
        if (xmlhttp)
            xmlhttp.abort();
        // alert("请求服务超时！");
    }
    if(xmlhttp) {
        t1 = setTimeout(connecttoFail, timeStrapPhar);
        try {
            ajax(xmlhttp, "POST", "http://" + checkServerIp + ":" + checkServerPort + "/DCStation/pharmacistSubmit/sendPharmacistCheck", data, adduserok);
        }catch (e){
            return -3;
        }
    }else {
        alert("Init xmlhttprequest fail");
    }

    // xmlhttp.open("POST", "http://" + checkServerIp + ":" + checkServerPort + "/DCStation/pharmacistSubmit/sendPharmacistCheck", false);
    //     xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded;");
    // xmlhttp.send(data);

    var checkData = xmlhttp.responseText;
    if(checkData == ""){
        return -3;
    }
    var check = eval("(" + checkData + ")");
    if(check.hasProblem == -2){
        // alert("请求中间层服务异常！");
        return -2;
    } else if (check.hasProblem == 0) {
        if(t1){
            clearTimeout(t1);
        }
        return 0;
    }else {
        var url = "http://" + checkServerIp + ":" + checkServerPort + "/DCStation/pharmacistSubmit/pharmacistCheckResultPage?presId=" + check.presId + '&random=' + Math.random();

        if(navigator.userAgent.indexOf("Chrome") >0 ){
            var winOption = "height="+iHeight+",width="+iWidth+"," +
                "top=50,left=50,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,fullscreen=0";
            window.open(url,window, winOption);
        } else {
            window.showModalDialog(url, '',
                'resizable:yes;scroll:yes;status:no;' +
                'dialogWidth=' + iWidth +
                ';dialogHeight=' + iHeight +
                ';center=yes;help=yes');
        }
        if(t1){
            clearTimeout(t1);
        }
    }

    xmlhttp.open("POST", "http://" + checkServerIp + ":" + checkServerPort + "/DCStation/pharmacistSubmit/getRetValue", false);
    xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded;");
    xmlhttp.send('presId=' + check.presId);

    var data = xmlhttp.responseText;
    data = eval("(" + data + ")");
    return data;
}

function sendPharmacistCheckSilent(tag,patientID,visitDate,pharmacistInfo,xml,checkServerIp, checkServerPort){
    var xmlhttp;
    var data = "xml=" + encodeURIComponent(xml) + '&' + 'tag=' + tag + '&' + 'patientID=' + patientID + '&' + 'visitDate=' + visitDate + '&' + 'pharmacistInfo=' + pharmacistInfo;
    if (window.XMLHttpRequest) {
        //  IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp = new XMLHttpRequest();
    } else {
        // IE6, IE5
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }

    var t1;
    function adduserok(xmlhttp) {
        if (t1)
            clearTimeout(t1);
    }
    function connecttoFail() {
        if (xmlhttp)
            xmlhttp.abort();
        // alert("请求服务超时！");
    }
    if(xmlhttp) {
        t1 = setTimeout(connecttoFail, timeStrapPhar);
        try {
            ajax(xmlhttp, "POST", "http://" + checkServerIp + ":" + checkServerPort + "/DCStation/pharmacistSubmit/sendPharmacistCheck", data, adduserok);
        }catch (e){
            return -3;
        }
    }else {
        alert("Init xmlhttprequest fail");
    }

    // xmlhttp.open("POST", "http://" + checkServerIp + ":" + checkServerPort + "/DCStation/pharmacistSubmit/sendPharmacistCheck", false);
    // xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded;");
    // xmlhttp.send(data);

    var checkData = xmlhttp.responseText;
    if(checkData == ""){
        return -3;
    }
    var check = eval("(" + checkData + ")");
    if(check.hasProblem == -2){
        // alert("请求中间层服务异常！");
        return -2;
    }else if (check.hasProblem == 0) {
        // alert("返回值为：0");
        if(t1){
            clearTimeout(t1);
        }
        return 0;
    }else if(check.hasProblem == 1) {
        // alert("返回值为：1");
        if(t1){
            clearTimeout(t1);
        }
        return 1;
    }
    else if(check.hasProblem == 2) {
        // alert("返回值为：2");
        if(t1){
            clearTimeout(t1);
        }
        return 2;
    }
    else if (check.hasProblem == -1) {
        // alert("返回值为：-1");
        if(t1){
            clearTimeout(t1);
        }
        return -1;
    }
}

function sendPharmacistInterfere(xml){
    var data = "xml=" + encodeURIComponent(xml);
    if (window.XMLHttpRequest) {
        //  IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp = new XMLHttpRequest();
    } else {
        // IE6, IE5
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }

    xmlhttp.open("POST", "http://" + checkServerIp + ":" + checkServerPort + "/DCStation/pharmacistSubmit/sendPharmacistInterfere", false);
    xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded;");
    xmlhttp.send(data);

    var checkData = xmlhttp.responseText;
    if(checkData != null || checkData != ""){
        alert("干预成功！");
        hidediv();
        //禁止点击下一步
        $("#next").attr('disabled', 'disabled');
        $("#next").css('background-image', 'url(http://' + checkServerIp +':'+ checkServerPort + '/DCStation/image/nextdisabled.png)');
        $("#next").css('background-repeat', 'no-repeat');
        $("#next").css('background-position', 'center');
    }
    return checkData;
}

function PharmacistCheck_CP(visitDate,pharmacistInfo,xml,inHosFlag) {
    if (inHosFlag == undefined) {
        inHosFlag = 1;
    }
    if (inHosFlag == 0) {
        return sendPharmacistCheck_CP(visitDate, pharmacistInfo, xml, checkServerIpOutHos, cheServerPortOutHos);
    } else if (inHosFlag == 1) {
        return sendPharmacistCheck_CP(visitDate, pharmacistInfo, xml, checkServerIpInHos, cheServerPortInHos);
    } else {
        // alert("error:未识别的住院标识！");
        return -4;
    }
}

function PharmacistCheckSilent_CP(xml,inHosFlag) {
    if (inHosFlag == undefined) {
        inHosFlag = 1;
    }
    if (inHosFlag == 0) {
        return sendPharmacistCheckSilent_CP(xml, checkServerIpOutHos, cheServerPortOutHos);
    } else if (inHosFlag == 1) {
        return sendPharmacistCheckSilent_CP(xml, checkServerIpInHos, cheServerPortInHos);
    } else {
        // alert("error:未识别的住院标识！");
        return -4;
    }
}

function sendPharmacistCheck_CP(visitDate,pharmacistInfo,xml,checkServerIp, checkServerPort) {
    var iWidth = '1000px';
    var iHeight = '700px';
    var xmlhttp;
    var data = "xml=" + encodeURIComponent(xml)+ '&' + 'visitDate=' + visitDate + '&' + 'pharmacistInfo=' + pharmacistInfo;
    if (window.XMLHttpRequest) {
        //  IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp = new XMLHttpRequest();
    } else {
        // IE6, IE5
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }

    var t1;
    function adduserok(xmlhttp) {
        if (t1)
            clearTimeout(t1);
    }
    function connecttoFail() {
        if (xmlhttp)
            xmlhttp.abort();
        // alert("请求服务超时！");
    }
    if(xmlhttp) {
        t1 = setTimeout(connecttoFail, timeStrapPhar);
        try {
            ajax(xmlhttp, "POST", "http://" + checkServerIp + ":" + checkServerPort + "/DCStation/pharmacistSubmit/sendPharmacistCheck_CP", data, adduserok);
        }catch (e){
            return -3;
        }
    }else {
        alert("Init xmlhttprequest fail");
    }

    // xmlhttp.open("POST", "http://" + checkServerIp + ":" + checkServerPort + "/DCStation/pharmacistSubmit/sendPharmacistCheck", false);
    //     xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded;");
    // xmlhttp.send(data);

    var checkData = xmlhttp.responseText;
    if(checkData == ""){
        return -3;
    }
    var check = eval("(" + checkData + ")");
    if(check.hasProblem == -2){
        // alert("请求中间层服务异常！");
        return -2;
    }else if (check.hasProblem == 0) {
        if(t1){
            clearTimeout(t1);
        }
        return 0;
    }else {
        var url = "http://" + checkServerIp + ":" + checkServerPort + "/DCStation/pharmacistSubmit/pharmacistCheckResultPage?presId=" + check.presId + '&random=' + Math.random();

        if(navigator.userAgent.indexOf("Chrome") >0 ){
            var winOption = "height="+iHeight+",width="+iWidth+"," +
                "top=50,left=50,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,fullscreen=0";
            window.open(url,window, winOption);
        } else {
            window.showModalDialog(url, '',
                'resizable:yes;scroll:yes;status:no;' +
                'dialogWidth=' + iWidth +
                ';dialogHeight=' + iHeight +
                ';center=yes;help=yes');
        }
        if(t1){
            clearTimeout(t1);
        }
    }

    xmlhttp.open("POST", "http://" + checkServerIp + ":" + checkServerPort + "/DCStation/pharmacistSubmit/getRetValue", false);
    xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded;");
    xmlhttp.send('presId=' + check.presId);

    var data = xmlhttp.responseText;
    data = eval("(" + data + ")");
    return data;
}

function sendPharmacistCheckSilent_CP(xml,checkServerIp, checkServerPort) {
    var xmlhttp;
    var data = "xml=" + encodeURIComponent(xml);
    if (window.XMLHttpRequest) {
        //  IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp = new XMLHttpRequest();
    } else {
        // IE6, IE5
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }

    var t1;
    function adduserok(xmlhttp) {
        if (t1)
            clearTimeout(t1);
    }
    function connecttoFail() {
        if (xmlhttp)
            xmlhttp.abort();
        // alert("请求服务超时！");
    }
    if(xmlhttp) {
        t1 = setTimeout(connecttoFail, timeStrapPhar);
        try {
            ajax(xmlhttp, "POST", "http://" + checkServerIp + ":" + checkServerPort + "/DCStation/pharmacistSubmit/sendPharmacistCheckSilent_CP", data, adduserok);
        }catch (e){
            return -3;
        }
    }else {
        alert("Init xmlhttprequest fail");
    }

    // xmlhttp.open("POST", "http://" + checkServerIp + ":" + checkServerPort + "/DCStation/pharmacistSubmit/sendPharmacistCheck", false);
    // xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded;");
    // xmlhttp.send(data);

    var checkData = xmlhttp.responseText;
    if(checkData == ""){
        return -3;
    }
    var check = eval("(" + checkData + ")");
    if(check.hasProblem == -2){
        // alert("请求中间层服务异常！");
        return -2;
    }else if (check.hasProblem == 0) {
        if(t1){
            clearTimeout(t1);
        }
        // alert("返回值为：0");
        return 0;
    }else if(check.hasProblem == 1) {
        if(t1){
            clearTimeout(t1);
        }
        // alert("返回值为：1");
        return 1;
    }
    else if(check.hasProblem == 2) {
        if(t1){
            clearTimeout(t1);
        }
        // alert("返回值为：2");
        return 2;
    }
    else if (check.hasProblem == -1) {
        if(t1){
            clearTimeout(t1);
        }
        // alert("返回值为：-1");
        return -1;
    }
}

function testNurseCheck() {
    var dcdtXml = document.getElementById("dcdt").value;
    NurseCheck(dcdtXml,1);
}
/**
 *
 * @param xml
 * @param inHosFlag 住院门诊标识，1住院，0门诊 。默认使用住院医生站。
 * @constructor
 */
function NurseCheck(xml, inHosFlag) {
    if (inHosFlag == undefined) {
        inHosFlag = 1;
    }
    if (inHosFlag == 0) {
        return sendNurseCheck(xml, checkServerIpOutHos, cheServerPortOutHos);
    } else if (inHosFlag == 1) {
        return sendNurseCheck(xml, checkServerIpInHos, cheServerPortInHos);
    } else {
        // alert("error:未识别的住院标识！");
        return -4;
    }
}

function sendNurseCheck(xml, checkServerIp, checkServerPort) {
    var iWidth = '1000px';
    var iHeight = '550px';
    var xmlhttp;
    var data = "xml=" + encodeURIComponent(xml);
    if (window.XMLHttpRequest) {
        //  IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp = new XMLHttpRequest();
    } else {
        // IE6, IE5
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }

    var t1;
    function adduserok(xmlhttp) {
        if (t1)
            clearTimeout(t1);
    }
    function connecttoFail() {
        if (xmlhttp)
            xmlhttp.abort();
        // alert("请求服务超时！");
    }
    if(xmlhttp) {
        t1 = setTimeout(connecttoFail, timeStrapPhar);
        try {
            ajax(xmlhttp, "POST", "http://" + checkServerIp + ":" + checkServerPort + "/DCStation/nurseSubmit/sendNurseCheckForTest", data, adduserok);
        }catch (e){
            return -3;
        }
    }else {
        alert("Init xmlhttprequest fail");
    }

    var checkData = xmlhttp.responseText;
    if(checkData == ""){
        return -3;
    }
    var check = eval("(" + checkData + ")");
    if(check.hasProblem == -2){
        // alert("请求中间层服务异常！");
        return -2;
    } else if (check.hasProblem == 0) {
        if(t1){
            clearTimeout(t1);
        }
        return 0;
    }else {
        var url = "http://" + checkServerIp + ":" + checkServerPort + "/DCStation/nurseSubmit/nurseCheckResultPage?random=" + Math.random();

        if(navigator.userAgent.indexOf("Chrome") >0 ){
            var winOption = "height="+iHeight+",width="+iWidth+"," +
                "top=50,left=50,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,fullscreen=0";
            window.open(url,window, winOption);
        } else {
            window.showModalDialog(url, '',
                'resizable:yes;scroll:yes;status:no;' +
                'dialogWidth=' + iWidth +
                ';dialogHeight=' + iHeight +
                ';center=yes;help=yes');
        }
        if(t1){
            clearTimeout(t1);
        }
    }

    xmlhttp.open("POST", "http://" + checkServerIp + ":" + checkServerPort + "/DCStation/nurseSubmit/getRetValue", false);
    xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded;");
    xmlhttp.send('presId=1');

    var data = xmlhttp.responseText;
    data = eval("(" + data + ")");
    return data;
}

function writeReg(){
    var WshShell=new ActiveXObject("WScript.Shell");
    for(var i=1;i<=4;++i) {
        var path = "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\\Zones\\"+i+"\\1406";
        // var curValue = WshShell.RegRead(path);
        // // alert(curValue);
        // if (curValue != 0) {
        WshShell.RegWrite(path, "0", "REG_DWORD");
        // }
    }
}

function getOS() {
    var sUserAgent = navigator.userAgent;
    var isWin = (navigator.platform == "Win32") || (navigator.platform == "Windows");
    var isMac = (navigator.platform == "Mac68K") || (navigator.platform == "MacPPC") || (navigator.platform == "Macintosh") || (navigator.platform == "MacIntel");
    if (isMac) return "Mac";
    var isUnix = (navigator.platform == "X11") && !isWin && !isMac;
    if (isUnix) return "Unix";
    var isLinux = (String(navigator.platform).indexOf("Linux") > -1);
    if (isLinux) return "Linux";
    if (isWin) {
        var isWin2K = sUserAgent.indexOf("Windows NT 5.0") > -1 || sUserAgent.indexOf("Windows 2000") > -1;
        if (isWin2K) return "Win2000";
        var isWinXP = sUserAgent.indexOf("Windows NT 5.1") > -1 || sUserAgent.indexOf("Windows XP") > -1;
        if (isWinXP) return "WinXP";
        var isWin2003 = sUserAgent.indexOf("Windows NT 5.2") > -1 || sUserAgent.indexOf("Windows 2003") > -1;
        if (isWin2003) return "Win2003";
        var isWinVista= sUserAgent.indexOf("Windows NT 6.0") > -1 || sUserAgent.indexOf("Windows Vista") > -1;
        if (isWinVista) return "WinVista";
        var isWin7 = sUserAgent.indexOf("Windows NT 6.1") > -1 || sUserAgent.indexOf("Windows 7") > -1;
        if (isWin7) return "Win7";
        var isWin10 = sUserAgent.indexOf("Windows NT 10") > -1 || sUserAgent.indexOf("Windows 10") > -1;
        if (isWin10) return "Win10";
    }
    return "other";
}
// function sortByBedNo() {
//
// }
//
// function sortBySerious() {
//
// }





