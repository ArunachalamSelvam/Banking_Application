<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Error</title>
<!--     <link rel="stylesheet" href="styles.css"> Optional: Link to an external CSS file
 -->    <style>
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
            color: #e74c3c;
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
            background-color: #e74c3c;
            text-decoration: none;
            border-radius: 5px;
            margin-top: 20px;
        }
        .button:hover {
            background-color: #c0392b;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Error</h1>
        <p>Oops! Something went wrong.</p>
        <p>We're sorry, but we couldn't process your request at this time.</p>
        <a href="login.html" class="button">Return Home</a> <!-- Link to your home page -->
    </div>
</body>
</html>
