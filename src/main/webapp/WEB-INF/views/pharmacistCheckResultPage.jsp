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

    <script type="text/javascript"
            src="http://localhost:8080/DCStation/js/jquery.min.js"></script>
    <link href="http://localhost:8080/DCStation/css/checkResultPage.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript">
        var checkServerIp = "localhost";
        var cheServerPost = "8080";
        var presId = '${presId}';
        function nextOrBack(val) {
            var xmlhttp;
            if (window.XMLHttpRequest) {
                //  IE7+, Firefox, Chrome, Opera, Safari
                xmlhttp = new XMLHttpRequest();
            } else {
                // IE6, IE5
                xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
            }

            xmlhttp.open("POST", "http://" + checkServerIp + ":" + cheServerPost + "/DCStation/pharmacistSubmit/setRetValue", false);
            xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded;");
            xmlhttp.send('presId=' + presId + '&retVal=' + val);
            window.close();
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
                    <div class="head-info-sm"><p>问题汇总</p></div>
                    <div class="detail-content">

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
                            <th>医院<br>管理</th>
                            <th>处方<br>ID</th>
                        </tr>
                        </thead>
                    </table>
                </div>
                <div class="table-area-content">
                    <table class="main-table">
                        <tbody>
                        <c:forEach var="item" items="${checkResult.advices}">
                            <tr>
                                <td style="width: 132px;">${item.DRUG_LO_NAME}</td>
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

            <div class="info-area">
                <div class="detail-info left-area">
                    <div class="head-info-sm"><p>审方医生与医生沟通结果</p></div>
                    <div class="detail-content" style="padding: 10px;_padding:20px 10px;">

                    </div>
                </div>
                <div class="detail-info right-area">
                    <div class="head-info-sm"><p>干预情况</p></div>
                    <div class="detail-content">

                    </div>
                </div>
            </div>

            <div class="button-area">
                <input id="doctorInfo" type="button"/>
                <input id="patientInfo" type="button"/>
                <input id="orderInfo" type="button"/>
                <input id="help" type="button" />
                <input id="interfere" type="button"/>
                <input id="back" type="button" onclick="nextOrBack(-1)" value="返回"/>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <input id="next" type="button" onclick="nextOrBack(0)" value="下一步"/>
            </div>

        </div>
    </div>

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

        var drug_name = $(".main-table tbody").children().eq(row).children().eq(0).html().replace(' ', '');
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
            $("input[name='next']").attr('disabled', 'disabled');

            $("input[name='next']").css('background-image', 'url(http://localhost:8080/DCStation/image/nextdisabled.png)');
            $("input[name='next']").css('background-repeat', 'no-repeat');
            $("input[name='next']").css('background-position', 'center');
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
