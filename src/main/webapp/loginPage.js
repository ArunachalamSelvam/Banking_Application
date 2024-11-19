document.addEventListener('DOMContentLoaded', function() {
    console.log("inside login.js");

    const loginButton = document.querySelector('.loginButton');
    const loginForm = document.getElementById('loginForm');
    const signupLink = document.querySelector('.signup-button');

    // Prevent form submission on enter key or input focus
    loginForm.addEventListener('submit', function(event) {
        event.preventDefault(); // Prevent the form from submitting automatically
    });

    // Handle login button click
    loginButton.addEventListener('click', function(event) {
        event.preventDefault(); // Prevent default anchor behavior

        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;

        if (username && password) {
            // Perform login using fetch
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
                    alert('Login successful!');
                    console.log(data.userName);
                    
                    sessionStorage.setItem('userName', data.userName);

                    window.location.href = `dashboard.html?userName=${encodeURIComponent(data.userName)}`;
                } else {
                    alert('Login failed: ' + data.message);
                }
            })
            .catch(error => {
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
