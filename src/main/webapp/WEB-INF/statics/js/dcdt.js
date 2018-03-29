/**
 * Created by on 2017/5/14.
 */
var checkServerIp = "localhost";
var checkServerPort = "8090";
//说明书地址
var disUrl = 'http://localhost:8090/DCStation/home/index?drugCode=@code@';


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

    xmlhttp.open("POST", "http://" + checkServerIp + ":" + checkServerPort + "/DCStation/submit/sendCheck", false);
    xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded;");
    xmlhttp.send(data);

    var checkData = xmlhttp.responseText;
    var check = eval("(" + checkData + ")");
    if (tag == 2 || check.hasProblem == 0) {
        return 0;
    }
    else if (check.hasProblem == 1) {
        var url = "http://" + checkServerIp + ":" + checkServerPort + "/DCStation/submit/checkResultPage?presId=" + check.presId + '&random=' + Math.random();

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

    xmlhttp.open("POST", "http://" + checkServerIp + ":" + checkServerPort + "/DCStation/submit/getRetValue", false);
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
    var dcdtXml = document.getElementById("dcdt").value;
    PharmacistCheck(tag,patientID,visitDate,dcdtXml);
}

function PharmacistCheck(tag,patientID,visitDate,xml) {
    var iWidth = '1000px';
    var iHeight = '650px';
    var xmlhttp;
    var data = "xml=" + encodeURIComponent(xml) + '&' + 'tag=' + tag + '&' + 'patientID=' + patientID + '&' + 'visitDate=' + visitDate;
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

