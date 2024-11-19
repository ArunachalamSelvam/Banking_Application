
document.addEventListener('DOMContentLoaded', function() {

	let emailInput = document.getElementById("email");

	let btn = document.getElementById("btn");

	btn.addEventListener("click", function(event) {
		event.preventDefault();  

		
		let emailValue = emailInput.value;

		let xhr = new XMLHttpRequest();

		
		xhr.open('POST', 'http://localhost:8080/Banking_Project/OtpServlet', true);
		xhr.setRequestHeader('Content-Type', 'application/json');

		
		xhr.onreadystatechange = function() {
			if (xhr.readyState === 4) { // Request is complete
				if (xhr.status === 200) { // HTTP status code 200 (OK)
					const response = JSON.parse(xhr.responseText);
					if (response.redirect) {
						window.location.href = response.redirect; // Redirect to the specified URL
					} else if(response.error){
						
						alert(response.error); // Show any error messages
					}
				} else {
					console.error('Error:', xhr.statusText);
					alert('An error occurred. Please try again.');
				}
			}
		};

		// Send the request with the email and action as JSON
		xhr.send(JSON.stringify({
			action: 'sendOtpForgotPassword',
			email: emailValue,
			otp: ""
		}));
	});
});	/**
 * 
 */