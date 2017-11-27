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
            src="http://localhost:8080/DCStation/js/jquery.min.js"></script>
    <link href="http://localhost:8080/DCStation/css/checkResultPage.css" rel="stylesheet" type="text/css"/>

    <script type="text/javascript">
        var checkServerIp = "localhost";
        var cheServerPost = "8080";
        //药品说明书链接
        var disUrl = 'http://localhost:8080/DCStation/home/index?drugCode=@code@';

        var presId = '${presId}';

        /********定义iframe模板********/
        var checkResultTemp =
                '<div id="bg" style="display: none;position: absolute;top: 0%;left: 0%;width: 90%;' +
                'height: 90%;background-color: black;z-index: 1001; -moz-opacity: 0.7;opacity: .70;' +
                'filter: alpha(opacity=70);"></div>' +
                '<div id="show" style=" display: none;position: absolute;margin:auto;' +
                'top: 0; left: 0; bottom: 0; right: 0;width: 900px;' +
                'height: 500px;padding: 20px 35px;border: 8px solid #E8E9F7;background-color: white;z-index: 1002;' +
                '">' +
                '<iframe src="about:blank" name="showPlace" frameborder=0 height=500 width=890 marginheight=0 marginwidth=0 scrolling=yes></iframe>' +
                '<div class="button-area"><input type="button" value="关闭" onclick="hidediv()"/></div>' +
                '</div>' +
                '<a style="display: none" id="checkResultButton" href="@(url)"target="showPlace">在左框中打开链接</a>';
        /********定义iframe模板********/

        function nextOrBack(val) {
            var xmlhttp;
            if (window.XMLHttpRequest) {
                //  IE7+, Firefox, Chrome, Opera, Safari
                xmlhttp = new XMLHttpRequest();
            } else {
                // IE6, IE5
                xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
            }

            xmlhttp.open("POST", "http://" + checkServerIp + ":" + cheServerPost + "/DCStation/submit/setRetValue", false);
            xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded;");
            xmlhttp.send('presId=' + presId + '&retVal=' + val);
            window.close();
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
            drawCheckResultElem(urlTemp);
//            window.open("http://localhost:8080/DCStation/home/index?drugCode=" + code, "mywin", "");
        }
    </script>

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
            <div class="head-info-lg">
                <p>审查结果</p>
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
                                <td>[警示信息]&nbsp;@(warning_info)</td>
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
                        <table style="_margin-top: 50px">
                            <tr>
                                <td>1、单击问题所在单元，可查看详细信息</td>
                            </tr>
                            <tr>
                                <td>2、<img src="http://localhost:8080/DCStation/image/common-problem.png"/>
                                    &nbsp;一般警示 &nbsp;&nbsp;
                                    <img src="http://localhost:8080/DCStation/image/serious-problem.png"/>
                                    &nbsp;严重警示&nbsp;&nbsp;
                                    <img src="http://localhost:8080/DCStation/image/disaster-problem.png"/>
                                    &nbsp;必须处理
                                </td>
                            </tr>
                            <tr>
                                <td>3、单击药品名称，可查看说明书</td>
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
                            <th style="width: 140px">药品名称</th>
                            <th>适应症</th>
                            <th>禁忌症<br>慎用症</th>
                            <th>用法<br>用量</th>
                            <th>重复<br>用药</th>
                            <th>相互<br>作用</th>
                            <th>配伍<br>禁忌</th>
                            <th>特殊<br>人群</th>
                            <th>药敏</th>
                            <th>用药<br>管理</th>
                            <th>用药<br>监测</th>
                        </tr>
                        </thead>
                    </table>
                </div>
                <div class="table-area-content">
                    <table class="main-table">
                        <tbody>
                        <c:forEach var="item" items="${checkResult.advices}">
                            <tr>
                                <td style="width: 132px;">
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
                                <td></td>
                                <td></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="button-area">
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
    var checkResultJson = ${checkResultJson};
    var advises = checkResultJson.advices;
    var problemType = ['适应症', '禁忌症慎用症', '用法用量', '重复用药', '相互作用', '配伍禁忌', '特殊人群', '药敏', '医院管理', '用药监测'];
    var problemLevel = ['disaster-problem', 'common-problem', 'common-problem', 'serious-problem'];
    var error_detail = $('#error_detail').html();
    $('#error_detail').html('');

    for (var i = 0; i < advises.length; i++) {
        var advise = advises[i];
        var checkInfoList = advise.checkInfoList;
        for (var j = 0; j < checkInfoList.length; j++) {
            var checkInfo = checkInfoList[j];
            for (var k = 0; k < problemType.length; k++) {
                if (checkInfo.NAME == problemType[k]) {
                    var className = problemLevel[parseInt(checkInfo.REGULAR_WARNING_LEVEL) + 1];
                    var $chooseTd = $(".main-table tbody").children().eq(i).children().eq(k + 1);

                    $chooseTd.attr('class', className);
                    $chooseTd.click({row: i, col: k}, function (event) {
                        showProblemDetail(event.data)
                    });
                }
            }
        }
    }

    function showProblemDetail(data) {
        var row = data.row;
        var col = data.col;

        var drug_name = $(".main-table tbody").children().eq(row).children().eq(0).children().html().replace(' ', '');
        var error_name = problemType[col];
        $("#error_detail").html('');
        var tempHtml = "<thead><tr><th>" + error_name + "</th></tr></thead>";

        for (var i = 0; i < advises.length; i++) {
            var advise = advises[i];
            if (advise.DRUG_LO_NAME != drug_name) {
                continue;
            }
            var checkInfoList = advise.checkInfoList;
            for (var j = 0; j < checkInfoList.length; j++) {
                var checkInfo = checkInfoList[j];
                if (checkInfo.NAME != error_name) {
                    continue;
                }
                var temp = error_detail;
                tempHtml += temp.replace('@(drug_name)', drug_name).replace('@(warning_info)', checkInfo.WARNING_INFO)
                        .replace('@(ref_source)', checkInfo.REF_SOURCE);
            }
        }
        $("#error_detail").html(tempHtml);
    }

    (function isDisabled() {
        var t = ${checkResult.HIGHEST_WARNING_LEVEL};
        if (t == -1) {
            $("#next").attr('disabled', 'disabled');

            $("#next").css('background-image', 'url(http://localhost:8080/DCStation/image/nextdisabled.png)');
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
</html>
