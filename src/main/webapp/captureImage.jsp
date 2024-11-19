<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Camera Capture</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            background-color: #f0f0f0;
            font-family: Arial, sans-serif;
        }

        .container {
            text-align: center;
            background-color: #fff;
            padding: 20px;
            border-radius: 15px;
            box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
        }

        #video {
            border-radius: 50%;
            width: 320px;
            height: 320px;
            object-fit: cover;
            margin-bottom: 20px;
            border: 8px solid #4CAF50;
            box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
        }

        #snap {
            background-color: #4CAF50;
            color: white;
            padding: 10px 20px;
            font-size: 16px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s ease;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
        }

        #snap:hover {
            background-color: #45a049;
        }

        #snap:active {
            background-color: #3e8e41;
        }

        #canvas {
            display: none;
        }
    </style>
</head>
<body>
    <div class="container">
        <video id="video" autoplay></video>
        <br>
        <button id="snap">Take Photo</button>
        <canvas id="canvas" width="480" height="480"></canvas>
    </div>

    <script>
        document.addEventListener("DOMContentLoaded", function () {
            // Fetch emailId from server session (embedded in JSP)
            const emailId = '<%= (String) session.getAttribute("emailId")%>';
           
            const userName = '<%= (String) session.getAttribute("userName") %>';
            
            console.log("Email ID: " + emailId);
            console.log("User Name: " + userName);

            const video = document.querySelector("#video");
            const canvas = document.querySelector("#canvas");
            const context = canvas.getContext("2d");

            // Access the webcam
            if (navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
                navigator.mediaDevices.getUserMedia({ video: true })
                    .then(function (stream) {
                        video.srcObject = stream;
                        video.play();
                    })
                    .catch(function (err) {
                        console.error("Error accessing the camera: " + err);
                        alert("Your browser does not support camera access.");
                    });
            } else {
                alert("Your browser does not support camera access.");
            }

            // Take a snapshot when the button is clicked
            document.getElementById("snap").addEventListener("click", function () {
                // Draw the video frame onto the canvas
                context.drawImage(video, 0, 0, 480, 480);

                // Convert the canvas to a Blob and send it with user data
                canvas.toBlob(function (blob) {
                    const formData = new FormData();
                    formData.append('image', blob, emailId + '.jpg');  // Image named with the email ID

                    // Send the Blob and user information to the servlet
                    fetch('http://localhost:8080/Banking_Project/uploadImageServlet', {
                        method: 'POST',
                        body: formData
                    })
                    .then(response => response.text())
                    .then(result => {
                        console.log('Success:', result);
                        alert("Photo uploaded successfully!");
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        alert("Error uploading photo. Please try again.");
                    });
                }, 'image/jpeg', 1);
            });
        });
    </script>
</body>
</html>