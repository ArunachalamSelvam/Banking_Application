<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="styleSheet" href="forgetPassword.css">
</head>
<body>
	<div class="container">
		
		<form action="forgetPasswordServlet" method="post">
		<div class="inputDiv">
		
			<label for="email">Email</label>
			<input type="email" name="email" required="">
		
		</div>
		<div class="inputDiv">
		
			<label for="oldPassword">OldPassword</label>
			<input type="password" name="oldPassword" required="">
		
		</div>
		
		<div class="inputDiv">
		
			<label for="newPassword">NewPassword</label>
			<input type="password" name="newPassword" required="">
		
		</div>
		
		<div class="inputDiv">
		
			<label for="confirmPassword">ConfirmPassword</label>
			<input type="password" name="confirmPassword" required="">
		
		</div>
		
		<button type="submit">Change Password</button>
		</form>
		
		
	
	</div>
	
</body>
</html>