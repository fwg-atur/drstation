<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>
        实时审核Web版
    </title>

    <script type="text/javascript"
            src="${pageContext.servletContext.contextPath}/js/jquery.min.js"></script>
    <link href="${pageContext.servletContext.contextPath}/css/checkResultPage.css" rel="stylesheet" type="text/css"/>
    <title>实时审核Web版</title>
    <style type="text/css">
        .selected {
            background:#FF6500;
            color:#fff;
        }
    </style>

    <script type="javascript">
        $(document).ready(function() {
            /**
             * 表格行被单击的时候改变背景色
             */
            $("#table tr:gt(0)").click(function() {
                if ($(this).hasClass("selected")){
                    $(this).removeClass("selected").find(":checkbox").attr("checked",false);//选中移除样式
                }
                else{
                    $(this).addClass("selected").find(":checkbox").attr("checked",true);//未选中添加样式
                }
            });
        });
    </script>
</head>
<body>
<div>
    <div>
        <div class="main-content">
            <div class="head-top">
                <%--<p></p>--%>
            </div>

            <div class="info-area">
                <label>医生姓名
                    <input type="text" style="width: 90px" readonly="readonly" name="doctorName" value="">
                </label>
                <label>患者ID
                    <input type="text" style="width: 90px" readonly="readonly" name="patientId" value="">
                </label>
                <label>患者姓名
                    <input type="text" style="width: 90px" readonly="readonly" name="patientName" value="">
                </label>

                <br>

                <label>药师姓名
                    <input type="text" style="width: 90px" readonly="readonly" name="pharmacistName" value="">
                </label>
                <label>药师工号
                    <input type="text" style="width: 90px" readonly="readonly" name="pharmacistID" value="">
                </label>
                <label>联系电话
                    <input type="text" style="width: 90px" readonly="readonly" name="telephone" value="">
                </label>
            </div>

            <div class="info-area">
                <div style="float: left;width: 45%">
                   <label>问题分类</label>
                    <div style="overflow-x: auto;overflow-y: auto;height: 400px;width: 400px;">
                       <table class="table" align="left" width="500px" height="500px">
                           <thead align="left">
                           <tr>
                               <th width="40px">ID</th>
                               <th>问题分类</th>
                           </tr>
                           </thead>
                           <tbody align="left">
                           <tr>
                               <th><input type="checkbox" name="checkbox1">1</th>
                               <th>应注明皮试结果，未注明；</th>
                           </tr>
                           <tr>
                               <th><input type="checkbox" name="checkbox2">2</th>
                               <th>药品超剂量使用未注明原因和再次签名；</th>
                           </tr>
                           <tr>
                               <th><input type="checkbox" name="checkbox3">3</th>
                               <th>开具处方未写临床诊断或临床诊断书写不全；</th>
                           </tr>
                           <tr>
                               <th><input type="checkbox" name="checkbox4">4</th>
                               <th>门急诊处方超疗程（急三慢七，慢病延长）；</th>
                           </tr>
                           <tr>
                               <th><input type="checkbox" name="checkbox5">5</th>
                               <th>遴选的药品不适宜；</th>
                           </tr>
                           <tr>
                               <th><input type="checkbox" name="checkbox6">6</th>
                               <th>特殊人群禁忌使用；</th>
                           </tr>
                           <tr>
                               <th><input type="checkbox" name="checkbox7">7</th>
                               <th>用药与性别不符；</th>
                           </tr>
                           <tr>
                               <th><input type="checkbox" name="checkbox8">8</th>
                               <th>禁忌症；</th>
                           </tr>
                           <tr>
                               <th><input type="checkbox" name="checkbox9">9</th>
                               <th>重复给药；</th>
                           </tr>
                           <tr>
                               <th><input type="checkbox" name="checkbox10">10</th>
                               <th>用法、用量不适宜（包括剂型、给药途径、用药途径）；</th>
                           </tr>
                           <tr>
                               <th><input type="checkbox" name="checkbox11">11</th>
                               <th>联合用药不适宜或者不良相互作用；</th>
                           </tr>
                           <tr>
                               <th><input type="checkbox" name="checkbox12">12</th>
                               <th>有配伍禁忌或者溶媒选择不适宜；</th>
                           </tr>
                           </tbody>
                       </table>
                    </div>
                </div>
                <div style="float: right;height:400px;width: 45%">
                    <label>问题描述</label>

                </div>
            </div>



            <div class="button-area" style="clear: both">
                <input id="back" type="button" onclick="nextOrBack(-1)" value="返回"/>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <input id="next" type="button" onclick="nextOrBack(0)" value="提交"/>
            </div>

        </div>
    </div>

</div>
</body>
</html>


