<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>     
<!DOCTYPE>
<html>
<head>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0-rc.2/css/materialize.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
 <!-- Compiled and minified JavaScript -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0-rc.2/js/materialize.min.js"></script>
       
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style>
	#left{
		float: right;
		font-size: 15px;
		overflow: hidden;
	
	}
	#left li{
		list-style: none;
		float: left;
	}
	header{
		
		width: 100%;
		height: 300px;

	}
	header a{
		color: black;
		font-size: 22px;
	}
	
	a{	
		padding-right: 20px;
	
	}
	
	a.logo{
		font-size: 40px;
	}
	.right{
		position: absolute;
		right: 220px;
		top: 1px;
	}
	.right-10{
		position: absolute;
		right: 120px;
		top: 1px;
	}
</style>
<script>
	$(function() {
		$("#logout").on("click", function(e) {
			e.preventDefault();
			var $form = $("<form></form>");
			$form.attr("action","/member/logout");
			$form.attr("method","post");
			$("<input>").attr("type","hidden").attr("name", "${_csrf.parameterName}").val("${_csrf.token}").appendTo($form);
			$form.appendTo("body");
			$form.submit();
		});
		$("#resign").on("click", function(e) {
			e.preventDefault();
			var $form = $("<form></form>");
			$form.attr("action","/member/resign");
			$form.attr("method","post");
			$("<input>").attr("type","hidden").attr("name", "${_csrf.parameterName}").val("${_csrf.token}").appendTo($form);
			$form.appendTo("body");
			$form.submit();
		});
	});
</script>
</head>
<body>
<div id="header-bar" class="header-wrapper">
	<a href="#" class="logo">LOGO</a>
	
	<sec:authorize access="isAnonymous()">
		<ul id="left" class="loginbar">
			<li><a href="/member/idFind">아이디 찾기</a></li>
			<li><a href="/member/pwdFind">비밀번호 찾기</a></li>
		</ul>
	</sec:authorize>
	<sec:authorize access="isAnonymous()">
		<ul id="left" class="loginbar">
		<li><a href="/member/register">회원가입</a></li>
		<li><a href="/member/login">로그인</a></li>
		</ul>
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_USER')">
		<ul id="left" class="login">
			<li class="right"><a href="#">${list.id}님 환영합니다</a></li>
			<li class="right-10"><a href="/member/logout" id="logout">로그아웃</a></li>
		</ul>
	
	</sec:authorize>	
</div>
</body>
</html>