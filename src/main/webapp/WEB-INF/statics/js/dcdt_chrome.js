/**
 * Created by on 2017/5/14.
 */
var disUrl = 'http://116.90.80.66:8221/drugs/@code@?source=dcdt_web&hospital_id=cdsdyrmyy&show_navbar=true';

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
var cheServerPortOutHos = "8090";

/**
 * 存放临时变量
 * @type {string}
 */
var checkServerIpTemp;
var cheServerPortTemp;

var checkResultTemp =
    '<div id="bg" style="display: none;position: absolute;top: 0%;left: 0%;width: 100%;' +
    'height: 100%;background-color: black;z-index: 1001; -moz-opacity: 0.7;opacity: .70;' +
    'filter: alpha(opacity=70);"></div>' +
    '<div id="show" style=" display: none;position: absolute;margin:auto;' +
    'top: 0; left: 0; bottom: 0; right: 0;width: 1000px;' +
    'height: 580px;padding: 8px;border: 8px solid #E8E9F7;background-color: white;z-index: 1002;' +
    '">' +
    '<iframe src="about:blank" name="showPlace" frameborder=0 height=600 width=1000 marginheight=0 marginwidth=0 scrolling=no></iframe>' +
    '</div></div>' +
    '<a style="display: none" id="checkResultButton" href="@(url)"target="showPlace">在左框中打开链接</a>';

var global_next_func_name;
var global_next_func_args;
var global_back_func_name;
var global_back_func_args;
var presId = null;
var checkIsQuitState = null;

function testCheck(tag) {
    var dcdtXml = document.getElementById("dcdt").value;
    DoctorCheck(tag, dcdtXml, test_next, 1, test_back, 2, 1);
}

function setInHosFlag(inHosFlag) {
    if (inHosFlag == undefined) {
        inHosFlag = 1;
    }
    if (inHosFlag == 0) {
        checkServerIpTemp = checkServerIpOutHos;
        cheServerPortTemp = cheServerPortOutHos;
    } else if (inHosFlag == 1) {
        checkServerIpTemp = checkServerIpInHos;
        cheServerPortTemp = cheServerPortInHos;
    } else {
        alert("error:未识别的住院标识！");
    }
}

function DoctorCheck(tag, xml, next_func_name, next_fun_args, back_func_name, back_func_args, inHosFlag) {
    setInHosFlag(inHosFlag);
    loacl_next_func(next_func_name, next_fun_args, back_func_name, back_func_args);
    DoctorCheckForChrome(tag, xml);
}

function getRetValUrl() {
    return "http://" + checkServerIpTemp + ":" + cheServerPortTemp + "/DCStation/submit/getRetValue";
}

function checkIsQuit() {
    var url = getRetValUrl();
    var data = 'presId=' + presId;
    var checkData = sendAjaxRequest(data, url);

    checkData = eval("(" + checkData + ")");

    if (checkData == 0) {
        window.clearInterval(checkIsQuitState);
        check_for_next()
    } else if (checkData == -1) {
        window.clearInterval(checkIsQuitState);
        check_for_back();
    }
}

function sendAjaxRequest(data, url) {
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
    return checkData;
}

function getSendCheckUrl() {
    return "http://" + checkServerIpTemp + ":" + cheServerPortTemp + "/DCStation/submit/sendCheckForTest";
}

function getCheckResultPageUrl(check) {
    return "http://" + checkServerIpTemp + ":" + cheServerPortTemp + "/DCStation/submit/checkResultPage?presId=" + check.presId + '&random=' + Math.random();
}

function DoctorCheckForChrome(tag, xml) {
    var data = "xml=" + encodeURIComponent(xml) + '&' + 'tag=' + tag;
    var url = getSendCheckUrl();
    var checkData = sendAjaxRequest(data, url);

    var check = eval("(" + checkData + ")");
    if (tag == 2) {
        return 0;
    }
    if (check.hasProblem == 0) {
        global_next_func_name(global_next_func_args);
    } else if (check.hasProblem == 1) {
        var url = getCheckResultPageUrl(check);
        presId = check.presId;
        drawCheckResultElem(url);
        checkIsQuitState = window.setInterval("checkIsQuit()", 500);
    }
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



