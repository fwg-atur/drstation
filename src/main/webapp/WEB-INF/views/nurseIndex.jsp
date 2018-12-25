<%--
  Created by IntelliJ IDEA.
  User: wtwang
  Date: 2018/12/25
  Time: 9:45
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
</head>
<body>
<div class="container-fluid">
    <div class="row-fluid">
        <div class="span12">
            <div class="page-header" contenteditable="true">
                <h1>实时审核Web版
                    <small> DCDT.XML</small>
                </h1>
            </div>
            <div class="hero-unit" contenteditable="true">
                <h3>

                </h3>
                <form id="queryForm" method="get">
                <textarea id="dcdt" name="dcdt" style="min-height: 400px;width: 100%">
                        &lt;CheckPatients&gt;
                            &lt;CheckPatient PATIENT_ID="001" VISIT_ID="1" PATIENT_PRES_ID="001_01"&gt;
                                &lt;Advices&gt;
                                    &lt;Advice PRES_ID="00101" DRUG_CODE="10001" ORDER_NO="01" ORDER_SUB_NO="1" /&gt;
                                    &lt;Advice PRES_ID="00101" DRUG_CODE="10002" ORDER_NO="01" ORDER_SUB_NO="2" /&gt;
                                    &lt;Advice PRES_ID="00102" DRUG_CODE="10003" ORDER_NO="02" ORDER_SUB_NO="1" /&gt;
                                &lt;/Advices&gt;
                            &lt;/CheckPatient&gt;
                        &lt;CheckPatients&gt;

                </textarea>
                </form>
                <p>
                    <button onclick="testNurseCheck()">检查 »</button>
                </p>
            </div>

        </div>
    </div>
</div>


</body>

<%--<script type="text/javascript" src="${pageContext.servletContext.contextPath}/js/dcdt_Chrome.js"></script>--%>
<script type="text/javascript" src="${pageContext.servletContext.contextPath}/js/dcdt_IE.js"></script>
<%--<script type="text/javascript" src="/DCStation/js/dcdt_IE.js"></script>--%>
<%--<script type="text/javascript"--%>
<script type="text/javascript" src="${pageContext.servletContext.contextPath}/js/jquery.min.js"></script>
<%--<script src="//cdn.rawgit.com/gfdev/javascript-jquery-transport-xdr/master/dist/jquery.transport.xdr.min.js"></script>--%>
<%--<script type="text/javascript" src="${pageContext.servletContext.contextPath}/js/jquery.xdomainrequest.min.js"></script>--%>
<%--src="http://223.3.71.149:8081/${pageContext.servletContext.contextPath}/js/jquery.min.js"></script>--%>
<script type="text/javascript">
    function submitPresc() {
        res = testCheck(1);
    }

</script>

</html>
