<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE HTML>
<html lang="zh_CN">
<head>
<meta http-equiv="Content-Type" content="text/plain" charset="UTF-8">
<meta name="_csrf" content="${_csrf.token}" />
<!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf_header" content="${_csrf.headerName}" />
<title>用户注册</title>
<style type="text/css">
body {
	color: #444;
}

#regForm {
	margin: 200px auto;
	width: 650px;
	height: 250px;
}

#regForm fieldset {
	padding: 35px 25px 20px;
	border: dashed 1px #2f6fab;
	border-radius: 3px;
	box-shadow: 3px 3px 6px 2px #999;
}

#regForm .inputText {
	margin: 8px 0px;
	padding: 5px 7px;
	width: 270px;
	color: #444;
}

#regForm .inputSubmit {
	float: left;
	padding: 5px 15px;
	cursor: pointer;
	letter-spacing: 5px;
}

#regForm .msg {
	margin: 15px 0px 0px;
}

#regForm table {
	width: 100%;
}

#regForm table td {
	padding: 5px;
}
</style>
</head>
<body>
	<c:url var="regUrl" value="/register" />
	<form:form action="${regUrl}" method="POST" id="regForm"
		modelAttribute="user">
		<fieldset>
			<legend>
				<strong>注册系统</strong>
			</legend>
			<!-- reg表单 -->
			<table>
				<tr>
					<td><b>*</b>用户名：</td>
					<td><form:input path="username" /></td>
					<td></td>
				</tr>

				<tr>
					<td><b>*</b>请设置密码：</td>
					<td><form:password path="password" /></td>
					<td></td>
				</tr>

				<tr>
					<td><b>*</b>请确认密码：</td>
					<td><form:password path="confirmPassword" /></td>
					<td></td>
				</tr>

				<tr>
					<td><b>*</b>邮箱：</td>
					<td><form:input id="email" path="email" /></td>
					<td><input id="zemail" type="button" value=" 发送邮箱验证码 "
						onClick="get_email_code();"></td>
				</tr>

				<tr>
					<td><b>*</b>验证手机：</td>
					<td><form:input id="phone" path="mobilephone" /></td>
					<td><input id="zphone" type="button" value=" 发送手机验证码 "
						onClick="get_mobile_code();"></td>
				</tr>

				<tr>
					<td><b>*</b>短信/邮箱验证码：</td>
					<td><input type="text" name="authCode" /></td>
					<td></td>
				</tr>

				<tr>
					<td align="right"><input id="thisCheckbox" name="thisCheckbox"
						checked="checked" type="checkbox" /></td>
					<td colspan="2">我已阅读并同意<a href="#">《哇乐猫用户注册协议》</a></td>
				</tr>

				<tr>
					<td><input type="submit" value="立即注册" class="inputSubmit" /></td>
				</tr>
			</table>

		</fieldset>
	</form:form>
</body>
<%@ include file="/WEB-INF/views/includes/foot_scripts_links.jspf"%>
<script type="text/javascript">
	
	function get_mobile_code() {
		$.post('<c:url value="/sendMpCode?${_csrf.parameterName}=${_csrf.token}" />', {
			mobilephone : $('#phone').val()
		}, function(msg) {
			alert(jQuery.trim(msg));
			if (msg == '已发送验证码') {
				RemainTime();
			}
		});
	};
	
	function get_email_code() {
		$.post('<c:url value="/sendEmCode?${_csrf.parameterName}=${_csrf.token}" />', {
			emailAddress : $('#email').val()
		}, function(msg) {
			alert(msg);
			if (msg == '已发送验证码') {
				RemainTime();
			}
		});
	};
	
	var iTime = 59;
	var Account;
	function RemainTime() {
		document.getElementById('zphone').disabled = true;
		var iSecond, sSecond = "", sTime = "";
		if (iTime >= 0) {
			iSecond = parseInt(iTime % 60);
			iMinute = parseInt(iTime / 60);
			if (iSecond >= 0) {
				if (iMinute > 0) {
					sSecond = iMinute + "分" + iSecond + "秒";
				} else {
					sSecond = iSecond + "秒";
				}
			}
			sTime = sSecond;
			if (iTime == 0) {
				clearTimeout(Account);
				sTime = '获取手机验证码';
				iTime = 59;
				document.getElementById('zphone').disabled = false;
			} else {
				Account = setTimeout("RemainTime()", 1000);
				iTime = iTime - 1;
			}
		} else {
			sTime = '没有倒计时';
		}
		document.getElementById('zphone').value = sTime;
	}
</script>
</html>