<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE HTML>
<html lang="zh_CN">
<head>
<meta charset="UTF-8">
<title>找回密码</title>
<style type="text/css">
body {
	color: #444;
}

#findPasswordVerificationForm {
	margin: 200px auto;
	width: 350px;
	height: 250px;
}

#findPasswordVerificationForm fieldset {
	padding: 35px 25px 20px;
	border: dashed 1px #2f6fab;
	border-radius: 3px;
	box-shadow: 3px 3px 6px 2px #999;
}

#findPasswordVerificationForm .inputText {
	margin: 8px 0px;
	padding: 5px 7px;
	width: 270px;
	color: #444;
}

#findPasswordVerificationForm .inputSubmit {
	float: right;
	padding: 5px 15px;s
	cursor: pointer;
	letter-spacing: 5px;
}

#findPasswordVerificationForm .msg {
	margin: 15px 0px 0px;
}
</style>
</head>
<body>
	<c:url var="commitUrl" value="verification" />
	<form:form action="${commitUrl}" method="POST" id="findPasswordVerificationForm">
		<fieldset>
			<legend>
				<strong>邮箱验证</strong>
			</legend>
			<!-- 登陆表单 -->
			<label for="emailAddress">邮箱</label> <input type="text" name="emailAddress" id="emailAddress" class="inputText" style="margin-bottom: 15px;" />
			 <input type="submit" value="确定" class="inputSubmit" />
		</fieldset>
	</form:form>
</body>
</html>