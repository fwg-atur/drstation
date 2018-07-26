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
var cheServerPortInHos = "80";
/**
 * 门诊医生站ip，端口
 * @type {string}
 */
var checkServerIpOutHos = "localhost";
var cheServerPortOutHos = "80";
//说明书地址
var disUrl = 'http://localhost:80/DCStation/home/index?drugCode=@code@';


function testCheck(tag) {
    var dcdtXml = document.getElementById("dcdt").value;
    DoctorCheck(tag, dcdtXml,1);
}
/**
 *
 * @param tag
 * @param xml
 * @param inHosFlag 住院门诊标识，1住院，0门诊 。默认使用住院医生站。
 * @constructor
 */
function DoctorCheck(tag, xml, inHosFlag) {
    if (inHosFlag == undefined) {
        inHosFlag = 1;
    }
    if (inHosFlag == 0) {
        return sendCheck(tag, xml, checkServerIpOutHos, cheServerPortOutHos);
    } else if (inHosFlag == 1) {
        return sendCheck(tag, xml, checkServerIpInHos, cheServerPortInHos);
    } else {
        alert("error:未识别的住院标识！");
    }
}

function sendCheck(tag, xml, checkServerIp, cheServerPort) {
    var iWidth = '1000px';
    var iHeight = '650px';
    var xmlhttp;
    var data = "xml=" + encodeURIComponent(xml) + '&' + 'tag=' + tag;

    if (window.XMLHttpRequest) {
        //  IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp = new XMLHttpRequest();
    } else {
        // IE6, IE5
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }

    xmlhttp.open("POST", "http://" + checkServerIp + ":" + cheServerPort + "/DCStation/submit/sendCheck", false);
    xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded;");
    xmlhttp.send(data);

    var checkData = xmlhttp.responseText;
    var check = eval("(" + checkData + ")");

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

    if (tag == 2 || check.hasProblem == 0) {
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

    // if (isDirectClose(data)) return -1;
    return data;
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
    pharmacistCheck(tag,patientID,visitDate,pharmacistInfo,dcdtXml,1);
}

function testPharmacistCheckSilent(tag) {
    var patientID = document.getElementById("patientID").value;
    var visitDate = document.getElementById("visitDate").value;
    var pharmacistInfo = document.getElementById("pharmacistInfo").value;
    var dcdtXml = document.getElementById("dcdt").value;
    pharmacistCheckSilent(tag,patientID,visitDate,pharmacistInfo,dcdtXml,1);
}

function pharmacistCheck(tag,patientID,visitDate,pharmacistInfo,xml,inHosFlag) {
    if (inHosFlag == undefined) {
        inHosFlag = 1;
    }
    if (inHosFlag == 0) {
        return sendPharmacistCheck(tag, patientID, visitDate,pharmacistInfo, xml, checkServerIpOutHos, cheServerPortOutHos);
    } else if (inHosFlag == 1) {
        return sendPharmacistCheck(tag, patientID, visitDate,pharmacistInfo, xml, checkServerIpInHos, cheServerPortInHos);
    } else {
        alert("error:未识别的住院标识！");
    }
}

function pharmacistCheckSilent(tag,patientID,visitDate,pharmacistInfo,xml,inHosFlag) {
    if (inHosFlag == undefined) {
        inHosFlag = 1;
    }
    if (inHosFlag == 0) {
        return sendPharmacistCheckSilent(tag, patientID, visitDate, pharmacistInfo, xml, checkServerIpOutHos, cheServerPortOutHos);
    } else if (inHosFlag == 1) {
        return sendPharmacistCheckSilent(tag, patientID, visitDate, pharmacistInfo, xml, checkServerIpOutHos, cheServerPortOutHos);
    } else {
        alert("error:未识别的住院标识！");
    }
}

function sendPharmacistCheck(tag,patientID,visitDate,pharmacistInfo,xml,checkServerIp, checkServerPort){
    var iWidth = '1000px';
    var iHeight = '650px';
    var xmlhttp;
    var data = "xml=" + encodeURIComponent(xml) + '&' + 'tag=' + tag + '&' + 'patientID=' + patientID + '&' + 'visitDate=' + visitDate + '&' + 'pharmacistInfo=' + pharmacistInfo;
    if (window.XMLHttpRequest) {
        //  IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp = new XMLHttpRequest();
    } else {
        // IE6, IE5
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }

    xmlhttp.open("POST", "http://" + checkServerIp + ":" + checkServerPort + "/DCStation/pharmacistSubmit/sendPharmacistCheck", false);
    xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded;");
    xmlhttp.send(data);

    var checkData = xmlhttp.responseText;
    var check = eval("(" + checkData + ")");
    if (tag == 2 || check.hasProblem == 0) {
        return 0;
    }
    else if (check.hasProblem == 1) {
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

    xmlhttp.open("POST", "http://" + checkServerIp + ":" + checkServerPort + "/DCStation/pharmacistSubmit/sendPharmacistCheck", false);
    xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded;");
    xmlhttp.send(data);

    var checkData = xmlhttp.responseText;
    var check = eval("(" + checkData + ")");
    if (tag == 2 || check.hasProblem == 0) {
        return 0;
    }
    else if (check.hasProblem == 1) {
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
    return checkData;
}

