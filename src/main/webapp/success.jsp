<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="refresh" content="5;url=login.html">
    <title>Success</title>
    <link rel="stylesheet" href="styles.css"> <!-- Optional: Link to an external CSS file -->
    <style>
        /* Basic inline styles, can be moved to an external stylesheet */
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
        }
        .container {
            max-width: 600px;
            margin: 100px auto;
            background-color: #fff;
            padding: 20px;
            text-align: center;
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
            border-radius: 5px;
        }
        h1 {
            color: #4CAF50;
        }
        p {
            font-size: 16px;
            color: #333;
        }
        .button {
            display: inline-block;
            padding: 10px 20px;
            font-size: 16px;
            color: #fff;
            background-color: #4CAF50;
            text-decoration: none;
            border-radius: 5px;
            margin-top: 20px;
        }
        .button:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
<%@ page session="true" %>
<%
    
    String message = (String) session.getAttribute("message");

    
    
   
%>
    <div class="container">
        <h1>Success!</h1>
        <p><%=message%></p>
        <a href="login.html" class="button">Go to Login Page</a> <!-- Link to the user's dashboard -->
    </div>
</body>
</html>
