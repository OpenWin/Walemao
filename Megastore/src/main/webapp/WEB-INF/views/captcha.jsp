<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE HTML>
<html lang="zh_CN">
<head>
<meta charset="UTF-8">
<title>megastore</title>
<style type="text/css">
body {
	color: #444;
}

#captchaForm {
	margin: 200px auto;
	width: 350px;
	height: 250px;
}

#captchaForm fieldset {
	padding: 35px 25px 20px;
	border: dashed 1px #2f6fab;
	border-radius: 3px;
	box-shadow: 3px 3px 6px 2px #999;
}

#captchaForm .inputText {
	margin: 8px 0px;
	padding: 5px 7px;
	width: 270px;
	color: #444;
}

#captchaForm .inputSubmit {
	float: right;
	padding: 5px 15px;s
	cursor: pointer;
	letter-spacing: 5px;
}

#captchaForm .msg {
	margin: 15px 0px 0px;
}
</style>
</head>
<body>
	<c:url var="commitUrl" value="check" />
	<form:form action="${commitUrl}" method="POST" id="captchaForm">
		<fieldset>
			<legend>
				<strong>验证码</strong>
			</legend>
			<!-- 表单 -->
			<label for="kaptcha">验证码</label> <input type="text" name="kaptcha" id="kaptcha" class="inputText" style="margin-bottom: 15px;" />
			<img src="kaptcha.jpg" id="kaptchaImage"/>
			 <input type="submit" value="确定" class="inputSubmit" />
		</fieldset>
	</form:form>
</body>

<script type="text/javascript">
	$(function() {
		$('#kaptchaImage').click(
				function() {
					$(this).hide().attr('src',
							'kaptcha.jpg?' + Math.floor(Math.random() * 100)).fadeIn();
				});
	});
</script>
</html>

