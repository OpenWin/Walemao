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
	width: 650px;
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

#findPasswordVerificationForm .kaptchainputText {
	margin: 8px 0px;
	padding: 5px 7px;
	width: 80px;
	color: #444;
}

#findPasswordVerificationForm .inputSubmit {
	float: left;
	padding: 5px 15px;
	s cursor: pointer;
	letter-spacing: 5px;
}

#findPasswordVerificationForm .msg {
	margin: 15px 0px 0px;
}
</style>
</head>
<body>
	<c:url var="commitUrl" value="verification" />
	<form:form action="${commitUrl}" method="POST"
		id="findPasswordVerificationForm">
		<fieldset>
			<legend>
				<strong>找回密码</strong>
			</legend>
			<!-- 登陆表单 -->
			<label for="emailAddress">账户名：</label> <input type="text"
				value="用户名/邮箱/已验证手机" onfocus="this.value=''"
				onblur="if(this.value==''){this.value='用户名/邮箱/已验证手机'}"
				name="emailAddress" id="emailAddress" class="inputText"
				style="margin-bottom: 15px;" /> <br /> <label for="kaptcha">验证码:</label>
			<input type="text" name="kaptcha" width="20" id="kaptcha"
				class="kaptchainputText" style="margin-bottom: 15px;" /> <img
				src="kaptcha.jpg" id="kaptchaImage" width="120" />看不清？<a href="#"
				id="changeImg">换一张</a> <br /> <input type="submit" value="下一步"
				class="inputSubmit" />
		</fieldset>
	</form:form>
</body>
<%@ include file="/WEB-INF/views/includes/foot_scripts_links.jspf"%>
<script type="text/javascript">
	$('#kaptchaImage').click(function() {
		abc();
	});
	$('#changeImg').click(function() {
		abc();
	});
	function abc() {
		$('#kaptchaImage').hide().attr('src',
				'kaptcha.jpg?' + Math.floor(Math.random() * 100)).fadeIn();
	}
</script>
</html>