<%--
  Created by IntelliJ IDEA.
  User: wtwang
  Date: 2017/12/24
  Time: 10:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>实时审核Web版</title>
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
                <input id="back" type="button" onclick="nextOrBack(-1)" value="返回"/>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <input id="next" type="button" onclick="nextOrBack(0)" value="提交"/>
            </div>

        </div>
    </div>

</div>
</body>
</html>
