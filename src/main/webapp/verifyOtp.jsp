<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
 <style>
        body {
            font-family: 'Arial', sans-serif;
            background-color: #f4f4f4;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        .container {
            background-color: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            width: 400px;
            text-align: center;
        }

        h1 {
            color: #333;
            font-size: 22px;
            margin-bottom: 20px;
        }

        label {
            display: block;
            font-size: 16px;
            margin-bottom: 10px;
        }

        input[type="text"] {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 14px;
            margin-bottom: 20px;
            box-sizing: border-box;
        }

        button {
            background-color: #007BFF;
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 5px;
            font-size: 16px;
            cursor: pointer;
            width: 100%;
            margin-bottom: 10px;
        }

        button:hover {
            background-color: #0056b3;
        }

        #resendBtn {
            background-color: #28a745;/* Hide the button initially */
        }

        #resendBtn:hover {
            background-color: #218838;
        }

        #countdown {
            font-size: 14px;
            color: #dc3545;
            margin-top: 10px;
        }

        .hidden {
            display: none;
        }
    </style>
    <script src="verifyOtp.js"></script>
</head>
<body>
<%@ page session="true" %>
<%
    
    String email = (String) session.getAttribute("email");
    
    
    String encodeEmail = "Not Available";
    if (email != null) {
        int index = email.indexOf("@");
        
        encodeEmail = email.substring(0, index - 5) + "*****" + email.substring(index);
    }
%>

    <div class="container">
        <h1>OTP sent to <%= encodeEmail %></h1>
        <form method="post" action="VerifyOtpServlet?action=verifyOtp">
            <input type="hidden" name="email" id="email" value="<%=email%>"> 
            <input type="text" name="otp" id="otp" required>
            <button type="submit">Verify OTP</button>
        </form>

        <button id="resendBtn" style="display:none" onclick="resendOtp()">Resend OTP</button>
        <div id="countdown">Resend OTP in 60 seconds.</div>
    </div>


</body>
</html>