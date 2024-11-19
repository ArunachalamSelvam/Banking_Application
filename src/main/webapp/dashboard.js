/**
 * 
 */
document.addEventListener('DOMContentLoaded', function() {
    // Assume 'userName' and 'balance' are stored in sessionStorage from login
	const urlParams = new URLSearchParams(window.location.search);
	//const userName = urlParams.get('userName');
	const userName = sessionStorage.getItem('userName');
	console.log(userName);
    // Display user name
    if (userName) {
       document.getElementById('userName').textContent = userName;
    } else {
        document.getElementById('userName').textContent = "User";
    }

    // Fetch and display balance
	document.getElementById('checkBalance').addEventListener('click', function(event) {
	        event.preventDefault();
	        fetchBalance();
	});

	    function fetchBalance() {
	        fetch('http://localhost:8080/Banking_Project/SavingsAccountServlet?action=getBalance')
	            .then(response => response.json())
	            .then(data => {
	                if (data.success) {
	                    document.getElementById('balance').textContent = data.balance;
	                } else {
	                    document.getElementById('balance').textContent = "N/A";
	                }
	            })
	            .catch(error => {
	                console.error('Error fetching balance:', error);
	                document.getElementById('balance').textContent = "Error";
	            });
	    }

    // Add event listeners to buttons (e.g., for editing profile, viewing transactions, etc.)
    //document.getElementById('editProfile').addEventListener('click', function(event) {
   //     event.preventDefault();
        // Redirect to edit profile page
   //     window.location.href = 'editProfile.html';
   // });

   /* document.getElementById('sendMoney').addEventListener('click', function(event) {
        event.preventDefault();
        // Redirect to view transactions page
        window.location.href = 'transaction.html';
    });

    document.getElementById('transactionHistory').addEventListener('click', function(event) {
        event.preventDefault();
		
        window.location.href = 'transactionHistory.html';
    });*/
	/*
	document.getElementById('atmCard').addEventListener('click', function(event) {
	    event.preventDefault();

	    // AJAX request to check ATM card availability in session
	    fetch('http://localhost:8080/Banking_Project/CheckAtmCardStatusServlet', { method: 'GET' })
	        .then(response => response.json())
	        .then(data => {
	            if (data.isAtmCardAvailable) {
	                // If ATM card is available, redirect to atmCard.html
	                window.location.href = "atmCard.html";
	            } else {
	                // Show alert if ATM card is not available
	                alert("You do not have an ATM card available. Please request one.");
	            }
	        })
	        .catch(error => {
	            console.error('Error:', error);
	            alert("An error occurred while checking your ATM card status.");
	        });
	});
*/
	
	document.getElementById('logout').addEventListener('click', function(event) {
		    event.preventDefault();

		    // AJAX request to check ATM card availability in session
		    fetch('http://localhost:8080/Banking_Project/logoutServlet', { method: 'GET' })
			.then(response => {
			            if (!response.ok) {
			                throw new Error('Network response was not ok');
			            }
			            return response.json();
			        })
			        .then(data => {
			            if (data.success) {  // Assuming the server sends a success field in JSON
			                alert("Logout successful!");
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
		});

	   
	//document.getElementById('logout').addEventListener('click', function(event){
	//	event.preventDefault();
	//	logout();
	//});
	
	//	function logout(){
			
	//	}   
});
