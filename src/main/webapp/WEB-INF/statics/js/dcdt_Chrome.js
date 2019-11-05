/**
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
var cheServerPortInHos = "8080";
/**
 * 门诊医生站ip，端口
 * @type {string}
 */
var checkServerIpOutHos = "localhost";
var cheServerPortOutHos = "8080";

//医生站超时返回的最长时间(毫秒)
var timeStrapDoc = 5000;
//药师站超时返回的最长时间(毫秒)
var timeStrapPhar = 5000;

var checkServerIp;
var checkServerPort;
var disUrl = 'http://192.168.80.2:8010/DCStation/home/index?drugCode=@code@';

var checkResultTemp =
    '<div id="bg" style="display: none;position: absolute;top: 0%;left: 0%;width: 100%;' +
    'height: 100%;background-color: black;z-index: 1001; -moz-opacity: 0.7;opacity: .70;' +
    'filter: alpha(opacity=70);"></div>' +
    '<div id="show" style=" display: none;position: absolute;margin:auto;' +
    'top: 0; left: 0; bottom: 0; right: 0;width: 1000px;' +
    'height: 580px;padding: 8px;border: 8px solid #E8E9F7;background-color: white;z-index: 1002;' +
    '">' +
    '<iframe src="about:blank" name="showPlace" frameborder=0 height=580 width=1000 marginheight=0 marginwidth=0 scrolling=no></iframe>' +
    '</div>' +
    '<a style="display: none" id="checkResultButton" href="@(url)"target="showPlace">在左框中打开链接</a>';

var pharmacistCheckResultTemp =
    '<div id="bg" style="display: none;position: absolute;top: 0%;left: 0%;width: 100%;' +
    'height: 100%;background-color: black;z-index: 1001; -moz-opacity: 0.7;opacity: .70;' +
    'filter: alpha(opacity=70);"></div>' +
    '<div id="show" style=" display: none;position: absolute;margin:auto;' +
    'top: 0; left: 0; bottom: 0; right: 0;width: 1000px;' +
    'height: 700px;padding: 8px;border: 8px solid #E8E9F7;background-color: white;z-index: 1002;' +
    '">' +
    '<iframe src="about:blank" name="showPlace" frameborder=0 height=700 width=1000 marginheight=0 marginwidth=0 scrolling=no></iframe>' +
    '</div>' +
    '<a style="display: none" id="checkResultButton" href="@(url)"target="showPlace">在左框中打开链接</a>';

var global_next_func_name;
var global_next_func_args;
var global_back_func_name;
var global_back_func_args;
var presId = null;
var checkIsQuitState = null;
var t1;

function testCheck(tag) {
    var dcdtXml = document.getElementById("dcdt").value;
    DoctorCheck(tag, dcdtXml, test_next, 1, test_back, 2, 1);
}

function setInHosFlag(inHosFlag) {
    if (inHosFlag == undefined) {
        inHosFlag = 1;
    }
    if (inHosFlag == 0) {
        checkServerIp = checkServerIpOutHos;
        checkServerPort = cheServerPortOutHos;
        return 0;
    } else if (inHosFlag == 1) {
        checkServerIp = checkServerIpInHos;
        checkServerPort = cheServerPortInHos;
        return 0;
    } else {
        // alert("error:未识别的住院标识！");
        return -4;
    }
}

function DoctorCheck(tag, xml, next_func_name, next_fun_args, back_func_name, back_func_args, inHosFlag) {
    if(setInHosFlag(inHosFlag) == -4){
        return -4;
    }else {
        loacl_next_func(next_func_name, next_fun_args, back_func_name, back_func_args);
        return DoctorCheckForChrome(tag, xml);
    }
}

function getRetValUrl() {
    return "http://" + checkServerIp + ":" + checkServerPort + "/DCStation/submit/getRetValue";
}

function getRetValUrl_BZ() {
    return "http://" + checkServerIp + ":" + checkServerPort + "/DCStation/submit/getRetValue_bz";
}

function checkIsQuit() {
    var url = getRetValUrl();
    var data = 'presId=' + presId;
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
    xmlhttp.send(data);

    var checkData = xmlhttp.responseText;

    checkData = eval("(" + checkData + ")");

    if (checkData == 0) {
        window.clearInterval(checkIsQuitState);
        check_for_next();
    } else if (checkData == -1) {
        window.clearInterval(checkIsQuitState);
        check_for_back();
    }
}

function checkIsQuit_BZ() {
    var url = getRetValUrl_BZ();
    var data = 'presId=' + presId;
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
    xmlhttp.send(data);

    var checkData = xmlhttp.responseText;
    // alert(checkData);

    // var check = eval("(" + checkData + ")");

    if(checkData.indexOf("STATE=\"-1\"") != -1){
        window.clearInterval(checkIsQuitState);
        check_for_back();
    } else if (checkData.indexOf("STATE=\"0\"") != -1) {
        window.clearInterval(checkIsQuitState);
        check_for_next();
    }
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

function sendAjaxRequestForDoc(data, url) {
    var xmlhttp;
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
        t1 = setTimeout(connecttoFail, timeStrapDoc);
        try {
            ajax(xmlhttp, "POST", url, data, adduserok);
        }catch (e){
            return -3;
        }
    }else {
        alert("Init xmlhttprequest fail");
    }

    // xmlhttp.open("POST", url, false);
    // xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded;");
    // xmlhttp.send(data);

    var checkData = xmlhttp.responseText;
    return checkData;
}

function sendAjaxRequestForPhar(data, url) {
    var xmlhttp;
    if (window.XMLHttpRequest) {
        //  IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp = new XMLHttpRequest();
    } else {
        // IE6, IE5
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }

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
            ajax(xmlhttp, "POST", url, data, adduserok);
        }catch (e){
            return -3;
        }
    }else {
        alert("Init xmlhttprequest fail");
    }

    // xmlhttp.open("POST", url, false);
    // xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded;");
    // xmlhttp.send(data);

    var checkData = xmlhttp.responseText;
    return checkData;
}

function getSendCheckUrl() {
    return "http://" + checkServerIp + ":" + checkServerPort + "/DCStation/submit/sendCheck";
}

function getSendCheckUrl_BZ() {
    return "http://" + checkServerIp + ":" + checkServerPort + "/DCStation/submit/sendCheck_BZ";
}

function getCheckResultPageUrl(check) {
    return "http://" + checkServerIp + ":" + checkServerPort + "/DCStation/submit/checkResultPage?presId=" + check.presId + '&random=' + Math.random();
}

function DoctorCheckForChrome(tag, xml) {
    var data = "xml=" + encodeURIComponent(xml) + '&' + 'tag=' + tag;
    var url = getSendCheckUrl();
    var checkData = sendAjaxRequestForDoc(data, url);
    if(checkData == -3){
        return -3;
    }

    var check = eval("(" + checkData + ")");
    if(check.hasProblem == -2){
        // alert("请求中间层服务异常！");
        return -2;
    }else if (tag == 2) {
        if(t1){
            clearTimeout(t1);
        }
        return 0;
    } else if (check.hasProblem == 0) {
        if(t1){
            clearTimeout(t1);
        }
        global_next_func_name(global_next_func_args);
    } else if (check.hasProblem == 1) {
        if(t1){
            clearTimeout(t1);
        }
        var url = getCheckResultPageUrl(check);
        presId = check.presId;
        drawCheckResultElem(url);
        checkIsQuitState = window.setInterval("checkIsQuit()", 500);
    }
}

function showdiv() {
    document.getElementById("bg").style.display = "block";
    document.getElementById("show").style.display = "block";
}

function hidediv() {
    // document.getElementById("checkResult").style.display = "none";
    document.getElementById("bg").style.display = "none";
    document.getElementById("show").style.display = "none";
}


function drawCheckResultElem(url) {
    var checkResultElem = document.getElementById("checkResult");
    checkResultElem.innerHTML = checkResultTemp.replace('@(url)', url);
    document.getElementById("checkResultButton").click();
    showdiv();
}

function check_for_next() {
    hidediv();
    global_next_func_name(global_next_func_args);
}

function check_for_back() {
    hidediv();
    global_back_func_name(global_back_func_args);
}

function loacl_next_func(next_func_name, next_func_args, back_func_name, back_func_args) {
    global_next_func_name = eval(next_func_name);
    global_next_func_args = next_func_args;
    global_back_func_name = eval(back_func_name);
    global_back_func_args = back_func_args;
}

function test_next(val) {
    alert("next:" + val);
}

function test_back(val) {
    alert("back:" + val);
}

function test(tag) {
    var inHosFlag = 1;
    var dcdtXml = '<CheckInput INPATIENT="否" TAG="1">' +
        '<Doctor NAME="王**" POSITION="住院医师" USER_ID="3947" DEPT_NAME="麻醉科手术室" DEPT_CODE="126" />' +
        '<Patient NAME="王**" ID="1902964" VISIT_ID="" PATIENT_PRES_ID="2018010500**5" BIRTH="20151101" HEIGHT="" ' +
        'WEIGHT="" GENDER="女" PREGNANT="" LACT="" HEPATICAL="" RENAL="" PANCREAS="" ALERGY_DRUGS="" IDENTITY_TYPE="" ' +
        'FEE_TYPE="" SCR="" SCR_UNIT="umol/L" GESTATION_AGE="" PRETERM_BIRTH="" DRUG_HISTORY="" FAMILY_DISEASE_HISTORY="" ' +
        'GENETIC_DISEASE="" MEDICARE_01="" MEDICARE_02="" MEDICARE_03="" MEDICARE_04="" MEDICARE_05="" />' +
        '<Diagnosises DIAGNOSISES="病毒性感冒" />' +
        '<Advices><Advice DRUG_LO_ID="1602" DRUG_LO_NAME="硫酸氨基葡萄糖钾胶囊" ' +
        'ADMINISTRATION=" po" DOSAGE="0.25" DOSAGE_UNIT="g" FREQ_COUNT="2" FREQ_INTERVAL="1" FREQ_INTERVAL_UNIT="日" ' +
        'START_DAY="20180105" END_DAY="" REPEAT="" ORDER_NO="1" ORDER_SUB_NO="1" DEPT_CODE="126" DOCTOR_NAME="王健" ' +
        'TITLE="住院医师" AUTHORITY_LEVELS="" ALERT_LEVELS="" GROUP_ID="1" USER_ID="3947" PRES_ID="2018010500655" ' +
        'PRES_DATE="20180105" PRES_SEQ_ID="" PK_ORDER_NO="" COURSE="10" PKG_COUNT="20" PKG_UNIT="粒" BAK_01="" BAK_02="" ' +
        'BAK_03="胶囊剂" BAK_04="0.25g*20" BAK_05="山西康宝生物制品股分有限公司" />' +
        '</Advices></CheckInput>"';
    DoctorCheck(tag, dcdtXml, test_next, 1, test_back, 2, inHosFlag);
}

function openDiscribLinked(code) {
    var urlTemp = disUrl.replace("@code@", code);
    window.open(urlTemp, '药品说明书', ' left=0,top=0,width=' + (screen.availWidth - 10) + ',height=' + (screen.availHeight - 50) + ',scrollbars,resizable=yes,toolbar=no');
}

function testPharmacistCheck(tag) {
    var patientID = document.getElementById("patientID").value;
    var visitDate = document.getElementById("visitDate").value;
    var pharmacistInfo = document.getElementById("pharmacistInfo").value;
    var dcdtXml = document.getElementById("dcdt").value;
    PharmacistCheck(tag, patientID, visitDate, pharmacistInfo, dcdtXml, test_pharmacistNext, 1, test_pharmacistBack, 2, 1);
}
function testPharmacistCheckSilent(tag) {
    var patientID = document.getElementById("patientID").value;
    var visitDate = document.getElementById("visitDate").value;
    var pharmacistInfo = document.getElementById("pharmacistInfo").value;
    var dcdtXml = document.getElementById("dcdt").value;
    PharmacistCheckSilent(tag, patientID, visitDate,pharmacistInfo, dcdtXml, test_pharmacistNext, 1, test_pharmacistBack, 2, 1);
}

function PharmacistCheck(tag, patientID, visitDate, pharmacistInfo, xml, next_func_name, next_fun_args, back_func_name, back_func_args, inHosFlag) {
    if (setInHosFlag(inHosFlag) == -4) {
        return -4;
    } else {
        local_next_func_pharmacist(next_func_name, next_fun_args, back_func_name, back_func_args);
        return PharmacistCheckForChrome(tag, patientID, visitDate, pharmacistInfo, xml);
    }
}

function PharmacistCheckSilent(tag, patientID, visitDate, pharmacistInfo, xml, next_func_name, next_fun_args, back_func_name, back_func_args, inHosFlag) {
    if(setInHosFlag(inHosFlag) == -4){
        return -4;
    }else {
        local_next_func_pharmacist(next_func_name, next_fun_args, back_func_name, back_func_args);
        return PharmacistCheckSilentForChrome(tag, patientID, visitDate, pharmacistInfo, xml);
    }
}

function PharmacistCheckForChrome(tag, patientID, visitDate, pharmacistInfo, xml) {
    var data = "xml=" + encodeURIComponent(xml) + '&' + 'tag=' + tag + '&' + 'patientID=' + patientID + '&' + 'visitDate=' + visitDate + '&' + 'pharmacistInfo=' + pharmacistInfo;
    var url = "http://" + checkServerIp + ":" + checkServerPort + "/DCStation/pharmacistSubmit/sendPharmacistCheck";
    var checkData = sendAjaxRequestForPhar(data, url);
    if(checkData == -3){
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
        check_for_next();
    } else {
        if(t1){
            clearTimeout(t1);
        }
        var url = "http://" + checkServerIp + ":" + checkServerPort + "/DCStation/pharmacistSubmit/pharmacistCheckResultPage?presId=" + check.presId + '&random=' + Math.random();
        pharmacist_presId = check.presId;
        drawPharmacistCheckResultElem(url);
        pharmacistCheckIsQuitState = window.setInterval("pharmacistCheckIsQuit()", 500);
    }
}

function PharmacistCheckSilentForChrome(tag, patientID, visitDate, pharmacistInfo, xml) {
    var data = "xml=" + encodeURIComponent(xml) + '&' + 'tag=' + tag + '&' + 'patientID=' + patientID + '&' + 'visitDate=' + visitDate + '&' + 'pharmacistInfo=' + pharmacistInfo;
    var url = "http://" + checkServerIp + ":" + checkServerPort + "/DCStation/pharmacistSubmit/sendPharmacistCheck";
    var checkData = sendAjaxRequestForPhar(data, url);
    if(checkData == -3){
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
    }else if(check.hasProblem == 1) {
        if(t1){
            clearTimeout(t1);
        }
        return 1;
    }
    else if(check.hasProblem == 2) {
        if(t1){
            clearTimeout(t1);
        }
        return 2;
    }
    else if (check.hasProblem == -1) {
        if(t1){
            clearTimeout(t1);
        }
        return -1;
    }
}

function drawPharmacistCheckResultElem(url) {
    var checkResultElem = document.getElementById("checkResult");
    checkResultElem.innerHTML = pharmacistCheckResultTemp.replace('@(url)', url);
    document.getElementById("checkResultButton").click();
    showdiv();
}

function pharmacistCheckIsQuit() {
    var url = "http://" + checkServerIp + ":" + checkServerPort + "/DCStation/pharmacistSubmit/getRetValue";
    var data = 'presId=' + pharmacist_presId;
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
    xmlhttp.send(data);

    var checkData = xmlhttp.responseText;
    checkData = eval("(" + checkData + ")");

    if (checkData == 0) {
        window.clearInterval(pharmacistCheckIsQuitState);
        pharmacistCheck_for_next()
    } else if (checkData == -1) {
        window.clearInterval(pharmacistCheckIsQuitState);
        pharmacistCheck_for_back();
    }
}

function pharmacistCheck_for_next() {
    hidediv();
    pharmacist_global_next_func_name(pharmacist_global_next_func_args);
}

function pharmacistCheck_for_back() {
    hidediv();
    pharmacist_global_back_func_name(pharmacist_global_back_func_args);
}

function local_next_func_pharmacist(next_func_name, next_func_args, back_func_name, back_func_args) {
    pharmacist_global_next_func_name = eval(next_func_name);
    pharmacist_global_next_func_args = next_func_args;
    pharmacist_global_back_func_name = eval(back_func_name);
    pharmacist_global_back_func_args = back_func_args;
}

function test_pharmacistNext(val) {
    alert("pharmacistNext:" + val);
}

function test_pharmacistBack(val) {
    alert("pharmacistBack:" + val);
}

/**
 * type等于1，是药师站；type等于2，是滨州药师站
 */
function sendPharmacistInterfere(xml,type) {
    var data = "xml=" + encodeURIComponent(xml);
    var url = "http://" + checkServerIp + ":" + checkServerPort + "/DCStation/pharmacistSubmit/sendPharmacistInterfere";
    var checkData = sendAjaxRequestForPhar(data, url);
    if(checkData == -3){
        return -3;
    }
    if(checkData != null || checkData != ""){
        alert("干预成功！");
        hidediv();
        //禁止点击下一步
        if(type==1) {
            $("#next").attr('disabled', 'disabled');
            $("#next").css('background-image', 'url(http://' + checkServerIp + ':' + checkServerPort + '/DCStation/image/nextdisabled.png)');
            $("#next").css('background-repeat', 'no-repeat');
            $("#next").css('background-position', 'center');
        }
    }
}

function testNurseCheck() {
    var dcdtXml = document.getElementById("dcdt").value;
    NurseCheck(dcdtXml, test_next, 1, test_back, 2, 1);
}

function NurseCheck(xml, next_func_name, next_fun_args, back_func_name, back_func_args, inHosFlag) {
    if(setInHosFlag(inHosFlag) == -4){
        return -4;
    }else {
        loacl_next_func(next_func_name, next_fun_args, back_func_name, back_func_args);
        return NurseCheckForChrome(xml);
    }
}

function NurseCheckForChrome(xml) {
    var data = "xml=" + encodeURIComponent(xml);
    var url = "http://" + checkServerIp + ":" + checkServerPort + "/DCStation/nurseSubmit/sendNurseCheck";
    var checkData = sendAjaxRequestForPhar(data, url);
    if(checkData == -3){
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
        check_for_next();
    } else {
        if(t1){
            clearTimeout(t1);
        }
        var url = "http://" + checkServerIp + ":" + checkServerPort + "/DCStation/nurseSubmit/nurseCheckResultPage?presId=" + check.presId + '&random=' + Math.random();
        nurse_presId = check.presId;
        drawNurseCheckResultElem(url);
        nurseCheckIsQuitState = window.setInterval("nurseCheckIsQuit()", 500);
    }
}

function drawNurseCheckResultElem(url) {
    var checkResultElem = document.getElementById("checkResult");
    checkResultElem.innerHTML = checkResultTemp.replace('@(url)', url);
    document.getElementById("checkResultButton").click();
    showdiv();
}

function nurseCheckIsQuit() {
    var url = "http://" + checkServerIp + ":" + checkServerPort + "/DCStation/nurseSubmit/getRetValue";
    var data = 'presId=' + nurse_presId;
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
    xmlhttp.send(data);

    var checkData = xmlhttp.responseText;
    checkData = eval("(" + checkData + ")");

    if (checkData == 0) {
        window.clearInterval(nurseCheckIsQuitState);
        pharmacistCheck_for_next()
    } else if (checkData == -1) {
        window.clearInterval(nurseCheckIsQuitState);
        pharmacistCheck_for_back();
    }
}


/**
 * ***********************************************************    滨医bs药师站接口开始     **********************************************************
 */
var pharmacist_global_next_func_name_bz ;
var pharmacist_global_next_func_args_bz ;
var pharmacist_global_back_func_name_bz ;
var pharmacist_global_back_func_args_bz ;


function testPharmacistCheckBZ(tag) {
    var dcdtXml = document.getElementById("dcdt").value;
    var pharmacistInfo = document.getElementById("pharmacistInfo").value;
    CheckWingBZ(dcdtXml,pharmacistInfo,test_pharmacistNext_bz, 1, test_pharmacistBack_bz, 2);
}



function CheckWingBZ(xml, pharmacistInfo, next_func_name, next_fun_args, back_func_name, back_func_args) {
        local_next_func_pharmacist_bz(next_func_name, next_fun_args, back_func_name, back_func_args);
        return PharmacistCheckForChrome_BZ(xml, pharmacistInfo);
}


function PharmacistCheckForChrome_BZ(xml, pharmacistInfo) {
    var data = "xml=" + encodeURIComponent(xml) + '&' + 'pharmacistInfo=' + pharmacistInfo;
    var url = "http://" + checkServerIpInHos + ":" + cheServerPortInHos + "/DCStation/pharmacistSubmit/sendPharmacistCheck_BZ";
    var checkData = sendAjaxRequestForPhar(data, url);


    if(checkData == -3||checkData ==""){
        result = "<OrderList STATE=\"-3\" />";
        return result;
    }
    var check = eval("(" + checkData + ")");
    if(check.hasProblem == -2){
        // alert("请求中间层服务异常！");
        result = "<OrderList STATE=\"-2\" />";
        return result;
    } else if (check.hasProblem == 0){
        if(t1){
            clearTimeout(t1);
        }
        // result = "<OrderList STATE=\"0\" />";
        // alert(result);
        // return result;
        pharmacistCheck_for_next_bz();
    }else {
        if(t1){
            clearTimeout(t1);
        }
        var url = "http://" + checkServerIpInHos + ":" + cheServerPortInHos + "/DCStation/pharmacistSubmit/pharmacistCheckResultPage_BZ?presId=" + check.presId + '&type=3&random=' + Math.random();
        pharmacist_presId_bz = check.presId;
        drawPharmacistCheckResultElem_bz(url);
        pharmacistCheckIsQuitState = window.setInterval("pharmacistCheckIsQuit_bz()", 500);
    }

}


function drawPharmacistCheckResultElem_bz(url) {
    var checkResultElem = document.getElementById("checkResult");
    checkResultElem.innerHTML = pharmacistCheckResultTemp.replace('@(url)', url);
    document.getElementById("checkResultButton").click();
    showdiv();
}

function pharmacistCheckIsQuit_bz() {
    var url = "http://" + checkServerIpInHos + ":" + cheServerPortInHos + "/DCStation/pharmacistSubmit/getRetValue_bz";
    var data = 'presId=' + pharmacist_presId_bz;
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
    xmlhttp.send(data);

    var checkData = xmlhttp.responseText;
    // checkData = eval("(" + checkData + ")");

    if (checkData == "<OrderList STATE=\"-1\" />") {
        window.clearInterval(pharmacistCheckIsQuitState);
        pharmacistCheck_for_back_bz();
    } else if (checkData != "<OrderList STATE=\"-2\" />") {
        window.clearInterval(pharmacistCheckIsQuitState);
        pharmacistCheck_for_next_bz();
    }

}

function pharmacistCheck_for_next_bz(){
    hidediv();
    pharmacist_global_next_func_name_bz(pharmacist_global_next_func_args_bz);
}

function pharmacistCheck_for_back_bz(){
    hidediv();
    pharmacist_global_back_func_name_bz(pharmacist_global_back_func_args_bz);
}

function local_next_func_pharmacist_bz(next_func_name, next_func_args, back_func_name, back_func_args) {
    pharmacist_global_next_func_name_bz = eval(next_func_name);
    pharmacist_global_next_func_args_bz = next_func_args;
    pharmacist_global_back_func_name_bz = eval(back_func_name);
    pharmacist_global_back_func_args_bz = back_func_args;
}


function test_pharmacistNext_bz(val) {
    alert("pharmacistNext:" + val);
}

function test_pharmacistBack_bz(val) {
    alert("pharmacistBack:" + val);
}



/**
 * *************************************   滨医bs药师站接口结束   *************************
 */
/*****************************      滨医医生站接口开始       **************************
 *
 */
//DoctorCheck函数和之前的函数一样，复用之前的函数
//inHosFlag为门诊住院标识，0为门诊，1为住院
// function DoctorCheck() {
//
// }
function testBZ(tag) {
    var dcdtXml = document.getElementById("dcdt").value;
    var ret = Check_BZRM(tag,dcdtXml,test_next,1,test_back,2,0);
    // alert(ret);
}

function Check_BZRM(tag,xml,next_func_name,next_func_args,back_func_name,back_func_args,inHosFlag) {
    if(setInHosFlag(inHosFlag) == -4){
        return -4;
    }else {
        loacl_next_func(next_func_name, next_func_args, back_func_name, back_func_args);
        return DoctorCheckForChrome_BZ(tag, xml);
    }
}

function DoctorCheckForChrome_BZ(tag,xml) {
    var data = "xml=" + encodeURIComponent(xml) + '&' + 'tag=' + tag;
    var url = getSendCheckUrl_BZ();
    var checkData = sendAjaxRequestForDoc(data, url);

    var check = eval("(" + checkData + ")");
    if (check.hasProblem == 0) {
        if(t1){
            clearTimeout(t1);
        }
        global_next_func_name(global_next_func_args);
        return check.retXml;
    } else if (check.hasProblem == 1) {
        if(t1){
            clearTimeout(t1);
        }
        var url = getCheckResultPageUrl(check);
        presId = check.presId;
        drawCheckResultElem(url);
        checkIsQuitState = window.setInterval("checkIsQuit_BZ()", 500);
        // return check.retXml;
    }

}

function CheckSingle(xml,next_func_name,next_func_args,back_func_name,back_func_args,inHosFlag) {
    var res = DoctorCheck("1",xml,next_func_name,next_func_args,back_func_name,back_func_args,inHosFlag);
    if(res == 0){
        DoctorCheck("2",xml,next_func_name,next_func_args,back_func_name,back_func_args,inHosFlag);
        return 0;
    }else{
        return res;
    }
}

//说明书函数已经实现，可以直接调用
// function openDiscribLinked() {
//
// }
/*****************************      滨医医生站接口结束       **************************
 *
 */