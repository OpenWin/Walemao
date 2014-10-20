<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE HTML>
<html lang="zh_CN">
<head>
<meta charset="UTF-8">
<title>用户注册</title>
<style type="text/css">
body {
	color: #444;
}

#loginForm {
	margin: 200px auto;
	width: 350px;
	height: 250px;
}

#loginForm fieldset {
	padding: 35px 25px 20px;
	border: dashed 1px #2f6fab;
	border-radius: 3px;
	box-shadow: 3px 3px 6px 2px #999;
}

#loginForm .inputText {
	margin: 8px 0px;
	padding: 5px 7px;
	width: 270px;
	color: #444;
}

#loginForm .inputSubmit {
	float: right;
	padding: 5px 15px;
	cursor: pointer;
	letter-spacing: 5px;
}

#loginForm .msg {
	margin: 15px 0px 0px;
}
</style>
</head>
<body>
	<c:url var="regUrl" value="reg" />
	<form:form action="${regUrl}" method="POST" id="loginForm" modelAttribute="user">
		<fieldset>
			<legend>
				<strong>注册系统</strong>
			</legend>
			<!-- reg表单 -->
			<form:label path="username">用户名 </form:label>
			<form:input path="username" cssClass="form-control" />

			<form:label path="password" style="margin-top: 5px;">密&nbsp;&nbsp;码</form:label>
			<form:input path="password" cssclass="form-control" />

			<form:label path="confirmPassword" style="margin-top: 5px;">确认密码</form:label>
			<form:input path="confirmPassword" cssclass="form-control" />
			
			<input type="submit" value="登录" class="inputSubmit" />
		</fieldset>
	</form:form>
</body>
</html>