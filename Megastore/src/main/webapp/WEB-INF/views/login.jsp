<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE HTML>
<html lang="zh_CN">
<head>
<meta charset="UTF-8">
<title>用户登录</title>
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
	float: left;
	padding: 5px 15px;
	cursor: pointer;
	letter-spacing: 5px;
}

#loginForm .reg {
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
	<c:url var="loginUrl" value="/login" />
	
	<form:form action="${loginUrl}" method="POST" id="loginForm">
		<fieldset>
			<legend>
				<strong>登录系统</strong>
			</legend>
			<!-- 登陆表单 -->
			<label for="username">邮箱/用户名/已验证手机 </label> <br /> <input
				type="text" name="username" id="username" class="inputText"
				value="${username}" style="margin-bottom: 15px;" /> <label
				for="password" style="margin-top: 5px;">密码 </label> <input
				type="password" name="password" id="password" class="inputText" />
			<br />
			<c:if test="${CREATE_CAPTCHA != null && CREATE_CAPTCHA == 'TRUE'}">
				<div class="form-group">
					<label for="admin-password-input" class="col-sm-4 control-label"><span
						class="glyphicon glyphicon-lock"></span>验证码：</label>
					<div class="col-sm-8">
						<div class="input-group">
							<input type="text" name="j_captcha" class="form-control" /> <span
								class="input-group-addon"><img id="kaptcha" width="95"
								height="34" src="<c:url value="/kaptcha.jpg" />" /></span>
						</div>
					</div>
				</div>
			</c:if>
			<!-- 登陆验证信息 -->
			<!-- 登陆验证信息 -->
			<c:if test="${LOGIN_ERROR_KEY != null}">
				<div class="msg" style="color: #db4a39;">
					<c:if test="${LOGIN_ERROR_KEY == 'PASSWORD_NO_RIGHT'}">
              	 密码错误！
				</c:if>
					<c:if test="${LOGIN_ERROR_KEY == 'USERNAME_NO_FOUND'}">
              	 用户名不存在！
				</c:if>
					<c:if test="${LOGIN_ERROR_KEY == 'USER_LOCKED'}">
              	 账号已被锁定，请20分钟后重试！
				</c:if>
				</div>
			</c:if>

			<c:if test="${ CAPTCHA_ERROR_KEY != null}">
				<div class="msg" style="color: #db4a39;">
					<c:if test="${CAPTCHA_ERROR_KEY == 'CAPTCHA_NO_RIGHT'}">
              	 验证码错误！
				</c:if>
				</div>
			</c:if>
			<c:if test="${param.logout != null}">
				<div class="msg">You have been logged out.</div>
			</c:if>
			<br /> <input type="checkbox" name="_spring_security_remember_me" />自动登录
			 <a href="<c:url value="/safety/findPwd"/>">忘记密码？</a><br /> <input
				type="submit" value="登录" class="inputSubmit" /><br /> <input
				type="button" onclick="location='register'" class="reg" value="免费注册" />
		</fieldset>
	</form:form>
</body>
</html>