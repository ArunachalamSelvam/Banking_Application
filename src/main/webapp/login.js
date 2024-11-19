/*document.getElementById('login-btn').addEventListener('click', async () => {
       const username = document.getElementById('username').value;
       const password = document.getElementById('password').value;
       const errorMessageElement = document.getElementById('error-message');

       try {
           const response = await fetch('/login', {
               method: 'POST',
               headers: {
                   'Content-Type': 'application/json',
               },
               body: JSON.stringify({ username, password }),
           });

           if (response.ok) {
               const data = await response.json();
               // Update UI or state based on successful login
               alert('Login successful!');
               // Here you could update the UI instead of redirecting
               errorMessageElement.textContent = '';
           } else {
               const errorData = await response.json();
               errorMessageElement.textContent = errorData.message || 'Login failed!';
           }
       } catch (error) {
           console.error('Error:', error);
           errorMessageElement.textContent = 'An error occurred. Please try again later.';
       }
   });*/
   
   
   document.addEventListener('DOMContentLoaded', function() {
		console.log("inside login.js");
       const loginForm = document.querySelector('form');
       const signupLink = document.querySelector('.signup-button');

       // Handle form submission
       loginForm.addEventListener('submit', function(event) {
           event.preventDefault(); // Prevent the default form submission

           const username = loginForm.querySelector('input[name="username"]').value;
           const password = loginForm.querySelector('input[name="password"]').value;

           // Perform login using fetch
           if (username && password) {
               fetch('http://localhost:8080/Banking_Project/loginServlet', {
                   method: 'POST',
                   headers: {
                       'Content-Type': 'application/json'
                   },
                   body: JSON.stringify({
                       username: username,
                       password: password
                   })
               })
               .then(response => response.json())
               .then(data => {
                   if (data.success) {
                       // Handle successful login
                       //alert('Login successful!');
					   console.log(data.userName);
					   
					   sessionStorage.setItem('userName', data.userName);

                       // Redirect to another page if needed
					   //window.location.href = `dashboard.html`;
					  window.location.href = `home.html`;
                   } else {
                       // Handle login failure
                       alert('Login failed: ' + data.message);
                   }
               })
               .catch(error => {
                   // Handle network errors
                   console.error('Error:', error);
                   alert('An error occurred while trying to log in.');
               });
           } else {
               alert('Please enter both username and password.');
           }
       });

       // Redirect to signup page
       signupLink.addEventListener('click', function(event) {
           event.preventDefault(); // Prevent default link behavior
           window.location.href = 'EmailVerificationPage.html'; // Redirect to signup page
       });
   });
