<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglibs.jspf"%>
<!DOCTYPE HTML>
<html lang="zh_CN">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>首页</title>
</head>
<body>
<div>
  <c:if test="${islogin ne 1}">
     您好，<c:out value="${username}"/> 欢迎来到哇乐猫商城！ <a href="<c:url value="/login.html" />">请登录!</a>
  </c:if>
  
  <c:if test="${islogin == 1}">
     您好，<c:out value="${username}"/> 欢迎来到哇乐猫商城！已登录！
     <a href="javascript:$('#logout-Form').submit();">退出</a>
     <c:url value="/logout" var="adminLogout"/>
	   <form:form action="${adminLogout}" method="POST" id="logout-Form" />
  </c:if>
  
</div>
</body>
<%@ include file="/WEB-INF/views/includes/foot_scripts_links.jspf"%>
</html>