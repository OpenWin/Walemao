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
	width: 650px;
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
	float: left;
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
	<form:form action="${regUrl}" method="POST" id="loginForm"
		modelAttribute="user">
		<fieldset>
			<legend>
				<strong>注册系统</strong>
			</legend>
			<!-- reg表单 -->
			<span><b>*</b>用户名：</span>
			<form:input type="box" path="username" cssClass="form-control" />
			<br> <span><b>*</b>请设置密码：</span>
			<form:input type="password" path="password" cssclass="form-control" />
			<br> <span><b>*</b>请确认密码：</span>
			<form:input type="password" path="confirmPassword"
				cssclass="form-control" />
			<br> <span><b>*</b>验证手机：</span>
			<form:input path="mobilephone" cssclass="form-control" />
			<br> <span><b>*</b>短信验证码：</span> <input name="authCode"
				type="text" /> <br> <input id="thisCheckbox"
				name="thisCheckbox" checked="checked" type="checkbox" /> <label>我已阅读并同意<a
				href="#">《哇乐猫用户注册协议》</a></label> <br> <input type="submit" value="立即注册"
				class="inputSubmit" />
		</fieldset>
	</form:form>
</body>
<%@ include file="/WEB-INF/views/includes/foot_scripts_links.jspf"%>
<script type="text/javascript">
	
</script>
</html>