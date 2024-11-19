<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<style>

.container{

	width:80%;
	heigth : 400px;
	margin: auto;
	
}
a{

	width : 100px;
	height : auto;
	padding : 10px;
	

}
</style>

</head>
<body>
<%
	String atmPin = String.valueOf(request.getAttribute("atmPin"));
%>

<div class = "container">
	<p> Thank you for selecting our banking service.</p>
	<h2>This is your temporary atm pin : <%= atmPin%></h2>
	<p>Please change your pin after you logged in.</p>
	<p>Click here to login</p><a href="login.html">login</a>
	
	
</div>
</body>
</html>