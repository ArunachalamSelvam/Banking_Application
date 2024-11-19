/**
 * 
 */

function startCountdown() {
	let countdownTime = 30; 
	let countdownTimer;
	countdownTimer = setInterval(() => {
		if (countdownTime < 0) {
			clearInterval(countdownTimer);
			deleteOtp();
			document.getElementById('countdown').innerHTML = "You can resend the OTP now.";
			document.getElementById('resendBtn').style.display = 'block'; 
		} else {
			document.getElementById('countdown').innerHTML = "Resend OTP in " + countdownTime + " seconds.";
			countdownTime--;
		}
	}, 1000); 
}

window.onload = function() {
	startCountdown();
	//deleteOtp();
};

function resendOtp() {

	document.getElementById('resendBtn').style.display = 'none';
	let emailValue = document.getElementById("email").value;

	/*  let countdownTime = 60; 
	 let countdownTimer; */

	startCountdown();

	fetch('http://localhost:8080/Banking_Project/OtpServlet', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({
			action: 'sendOtp',
			email: emailValue,
			otp: ""
		})
	})
		.then(response => response.json())  
		.then(data => {
			console.log(data.response);
			alert(data.response);
		})
		.catch(error => {
			console.error('Error:', error);
		});
}

function deleteOtp() {

	fetch('http://localhost:8080/Banking_Project/OtpServlet', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({
			action: 'delete',
			email: "",
			otp: ""
		})
	})
		.then(response => response.json()) 
		.then(data => {
			//console.log(data.response);
			//alert(data.response); 
		})
		.catch(error => {
			console.error('Error:', error);
		});
}

/*
function verifyOtp() {
	let emailValue = document.getElementById("email").value;
	let otp = document.getElementById("otp").value;
	console.log("inside otp verification");
	fetch('http://localhost:8080/Banking_Project/OtpServlet', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({
			action: 'verifyOtp',
			email: emailValue,
			otp: otp

		})
	})
	.then(response => {
	    if (response.redirected) {
	        window.location.href = response.url; // Handle redirect on client side
	    } else {
	        return response.json();
	    }
	})
	.then(data => {
	    if (data.response) {
	        alert(data.response);  // Show error message for invalid OTP
	    }
	})
		
		.catch(error => {
			console.error('Error:', error);
		});
}
*/