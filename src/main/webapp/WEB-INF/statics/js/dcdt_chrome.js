/**
 * Created by on 2017/5/14.
 */
var checkServerIp = "localhost";
var cheServerPost = "8080";
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

function testCheck(tag) {
    var dcdtXml = document.getElementById("dcdt").value;
    DoctorCheck(tag, dcdtXml, 'test_next', '1', 'test_back', '2');
}

function DoctorCheck(tag, xml, next_func_name, next_fun_args, back_func_name, back_func_args) {
    loacl_next_func(next_func_name, next_fun_args, back_func_name, back_func_args);
    DoctorCheckForChrome(tag, xml);
}

function DoctorCheckForChrome(tag, xml) {
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
        drawCheckResultElem(url);
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



