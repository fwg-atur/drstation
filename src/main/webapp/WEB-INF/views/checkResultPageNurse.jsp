<%--
  Created by IntelliJ IDEA.
  User: wtwang
  Date: 2018/12/12
  Time: 11:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>实时审核Web版-护士站</title>
</head>
<body>
<div>
    <div>
        <div class="main-content">
            <div class="inner-main-content" style="overflow-x:hidden">
                <div class="head-top">
                    <div class="head-top left-head"></div>
                    <div class="head-top right-head">|&nbsp;&nbsp;&nbsp;&nbsp;合理用药实时审核Web端</div>
                </div>

                <div class="table-area">
                    <div class="table-area-head">
                        <table class="main-table-head">
                            <thead>
                            <tr>
                                <th>床号</th>
                                <th>姓名</th>
                                <th>下达时间</th>
                                <th>严重程度</th>
                                <th>医嘱内容</th>
                                <th>长/临</th>
                                <th>自</th>
                                <th>剂量</th>
                                <th>单位</th>
                                <th>途径</th>
                                <th>频次</th>
                                <th>医生说明</th>
                                <th>问题类型</th>
                            </tr>
                            </thead>
                        </table>
                    </div>
                    <div class="table-area-content">
                        <table class="main-table">
                            <tbody>
                            <c:forEach var="item" items="${checkResult.advices}">
                                <tr>
                                    <td style="width:10px">${item.kh}</td>
                                    <td id="drug_name_td" style="width:190px;_width:127px">
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
                                    <td></td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="button-area">

                    <input id="sort_bedNo" type="button" onclick="nextOrBack(-1)" value="按床号排序"/>

                    <input id="sort_serious" type="button" onclick="nextOrBack(-1)" value="按严重程度排序"/>

                    <input id="export" type="button" onclick="next()" value="导出word"/>

                    <input id="confirm" type="button" onclick="next()" value="确认"/>

                    <input id="cancel" type="button" onclick="next()" value="取消"/>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
