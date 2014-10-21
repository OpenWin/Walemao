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
  您好，${username}  欢迎来到哇乐猫商城，<a href="<c:url value="/login.html" />">请登录!</a>
</div>
</body>
</html>