/**
 * Created by on 2017/5/14.
 */
var checkServerIp = "localhost";
var cheServerPost = "8080";
//说明书地址
var disUrl = 'http://localhost:8080/DCStation/home/index?drugCode=@code@';


function testCheck(tag) {
    var dcdtXml = document.getElementById("dcdt").value;
    DoctorCheck(tag, dcdtXml);
}

function DoctorCheck(tag, xml) {
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

    xmlhttp.open("POST", "http://" + checkServerIp + ":" + cheServerPost + "/DCStation/submit/sendCheck", false);
    xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded;");
    xmlhttp.send(data);

    var checkData = xmlhttp.responseText;
    var check = eval("(" + checkData + ")");
    if (tag == 2 || check.hasProblem == 0) {
        return 0;
    }
    else if (check.hasProblem == 1) {
        var url = "http://" + checkServerIp + ":" + cheServerPost + "/DCStation/submit/checkResultPage?presId=" + check.presId + '&random=' + Math.random();

        window.showModalDialog(url, '',
            'resizable:yes;scroll:yes;status:no;' +
            'dialogWidth=' + iWidth +
            ';dialogHeight=' + iHeight +
            ';center=yes;help=no');
    }

    xmlhttp.open("POST", "http://" + checkServerIp + ":" + cheServerPost + "/DCStation/submit/getRetValue", false);
    xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded;");
    xmlhttp.send('presId=' + check.presId);

    var data = xmlhttp.responseText;
    data = eval("(" + data + ")");
    return data;
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
    var patientId = document.getElementById("patientId").value;
    var presDate = document.getElementById("presDate").value;
    var dcdtXml = document.getElementById("dcdt").value;
    PharmacistCheck(tag,patientId,presDate,dcdtXml);
}

function PharmacistCheck(tag,patientId,presDate,xml) {
    var iWidth = '1000px';
    var iHeight = '650px';
    var xmlhttp;
    var data = "xml=" + encodeURIComponent(xml) + '&' + 'tag=' + tag + '&' + 'patientId=' + patientId + '&' + 'presDate=' + presDate;
    if (window.XMLHttpRequest) {
        //  IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp = new XMLHttpRequest();
    } else {
        // IE6, IE5
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }

    xmlhttp.open("POST", "http://" + checkServerIp + ":" + cheServerPost + "/DCStation/pharmacistSubmit/sendPharmacistCheck", false);
    xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded;");
    xmlhttp.send(data);

    var checkData = xmlhttp.responseText;
    var check = eval("(" + checkData + ")");
    if (tag == 2 || check.hasProblem == 0) {
        return 0;
    }
    else if (check.hasProblem == 1) {
        var url = "http://" + checkServerIp + ":" + cheServerPost + "/DCStation/pharmacistSubmit/pharmacistCheckResultPage?presId=" + check.presId + '&random=' + Math.random();

        window.showModalDialog(url, '',
            'resizable:yes;scroll:yes;status:no;' +
            'dialogWidth=' + iWidth +
            ';dialogHeight=' + iHeight +
            ';center=yes;help=no');
    }

    xmlhttp.open("POST", "http://" + checkServerIp + ":" + cheServerPost + "/DCStation/pharmacistSubmit/getRetValue", false);
    xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded;");
    xmlhttp.send('presId=' + check.presId);

    var data = xmlhttp.responseText;
    data = eval("(" + data + ")");
    return data;
}

