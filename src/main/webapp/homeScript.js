
let confirmationDiv = document.getElementById("confirmationDiv");
let confirmBtn = document.getElementById("confirmBtn");
let cancelBtn = document.getElementById("cancelBtn");
let confirmationMessageElement = document.getElementById("question");
let container = document.querySelector(".container");

document.addEventListener('DOMContentLoaded',function(){
	

	//document.getElementById("account").addEventListener('click', function() {
		
		
		console.log("transaction");
		let iframe = "<iframe src='balancePage.html' height='100%' width='100%' title='account'></iframe>";

		document.getElementById("pages").innerHTML = iframe;

		

		/*toggle();*/

		document.getElementById("account").classList.add("focus");
	//});
	
	
	
	
		console.log("set the FreezeStatus");
		
		if(sessionStorage.getItem("freezeStatus")!=null){
			
			
			console.log("inside freezs status session set method.");
			return;
		}
		fetch('http://localhost:8080/Banking_Project/AtmCardServlet?action=getFreezeStatus', {
			method: 'GET',
			headers: {
				'Content-Type': 'application/json'
			},

		}).then(response => {
			if (!response.ok) {
				// Handle HTTP errors
				throw new Error('Network response was not ok.');
			}
			return response.json(); // Return parsed JSON response
		}).then(data => {
			console.log(data);
			
		
			sessionStorage.setItem("freezeStatus",data.isFreeze);
		})
		
		
		
		if(sessionStorage.getItem("sleepStatus")!=null){
			let sleepStatus = sessionStorage.getItem("sleepStatus");
			
			console.log("inside freezs status session set method.");
			return;
		}
		
		
		fetch('http://localhost:8080/Banking_Project/AtmCardServlet?action=getSleepStatus', {
			method: 'GET',
			headers: {
				'Content-Type': 'application/json'
			},

		}).then(response => {
			if (!response.ok) {
				// Handle HTTP errors
				throw new Error('Network response was not ok.');
			}
			return response.json(); // Return parsed JSON response
		}).then(data => {
			console.log(data);
		
			
			sessionStorage.setItem("sleepStatus",data.isSleep);
		})
		
		


	
	
})


document.getElementById("account").addEventListener('click', function() {
	
	
	console.log("transaction");
	let iframe = "<iframe src='balancePage.html' height='100%' width='100%' title='account'></iframe>";

	document.getElementById("pages").innerHTML = iframe;

	

	toggle();

	document.getElementById("account").classList.add("focus");
})


document.getElementById("transaction").addEventListener('click', function() {
	
	console.log("transaction");
	let iframe = "<iframe src='transaction.html' height='100%' width='100%' title='transaction'></iframe>";

	document.getElementById("pages").innerHTML = iframe;


	toggle();

	document.getElementById("transaction").classList.add("focus");
})

document.getElementById("transactionHistory").addEventListener('click', function() {
	console.log("transactionHistory");

	let iframe = "<iframe src='transactionHistory.html' height='100%' width='100%' title='transactionHistory'></iframe>";

	document.getElementById("pages").innerHTML = iframe;

	toggle();

	document.getElementById("transactionHistory").classList.add("focus");
})

document.getElementById("debitCard").addEventListener('click', function() {
	console.log("debitCard");
	let iframe = "<iframe src='atmCard.html' height='100%' width='100%' title='debitCard'></iframe>";

	document.getElementById("pages").innerHTML = iframe;

	toggle();

	document.getElementById("debitCard").classList.add("focus");
});

document.getElementById("depositByDebit").addEventListener('click', function() {
	console.log("debitCard");
	let iframe = "<iframe src='atmCardDeposit.html' height='100%' width='100%' title='depositByDebit'></iframe>";

	document.getElementById("pages").innerHTML = iframe;

	toggle();

	document.getElementById("depositByDebit").classList.add("focus");
});




document.getElementById('logout').addEventListener('click', function(event) {
	event.preventDefault();
	confirmationMessageElement.innerText = "Do you want to logout?";
	confirmationDiv.style.display = "flex";
	container.classList.toggle("blur");
	
});


confirmBtn.addEventListener('click',function(){
	
        // alert("Freeze confirmed");
        confirmationDiv.style.display = "none";
		confirmationMessageElement.innerText ="";
         container.classList.toggle("blur");
		 
		 logOutMethod();
		 
		
})

cancelBtn.addEventListener('click', function(){
	confirmationDiv.style.display = "none";
	confirmationMessageElement.innerText ="";
	container.classList.toggle("blur");
})

function toggle() {
	let classes = document.getElementsByClassName("nav");

	for (let i = 0; i < classes.length; i++) {

		if (classes[i].classList.contains("focus")) {
			classes[i].classList.remove("focus");
			break;

		}
	}
}

function logOutMethod(){

	fetch('http://localhost:8080/Banking_Project/logoutServlet', { method: 'GET' })
		.then(response => {
			if (!response.ok) {
				throw new Error('Network response was not ok');
			}
			return response.json();
		})
		.then(data => {
			if (data.success) {  
				
				sessionStorage.clear();
				// Redirect to login page or homepage after successful logout
				window.location.href = "login.html";
			} else {
				alert("Logout failed. Please try again.");
			}
		})
		.catch(error => {
			console.error('Error:', error);
			alert("An error occurred during the logout process.");
		});
}