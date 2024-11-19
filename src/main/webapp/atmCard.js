

document.addEventListener('DOMContentLoaded', function() {
    
    // Fetch ATM card details and update the card display
    function fetchATMCardDetails() {
        fetch('http://localhost:8080/Banking_Project/AtmCardServlet?action=getCard')
            .then(function(response) {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(function(atmCard) {
                console.log(atmCard);

                // Update card details in the DOM
                document.getElementById('userName').textContent = atmCard.cardholderName || "Unknown";
                document.querySelector('.para').textContent = atmCard.cardNumber || "**** **** **** ****";
                document.querySelector('.expiryDate').textContent = atmCard.expirationDate || "MM/YY";

                // Set CVV but keep it hidden initially
                document.querySelector('.cvv').textContent = '...';
                document.querySelector('.cvv').setAttribute('data-cvv', atmCard.cvv);
            })
            .catch(function(error) {
                console.error('Error fetching ATM card details:', error);
            });
    }

    // Toggle CVV visibility when the eye icon is clicked
    function toggleCVVVisibility(event) {
        // Prevent the card from flipping
        event.stopPropagation();
        
        var cvvElement = document.querySelector('.cvv');
        var currentText = cvvElement.textContent;
        var actualCVV = cvvElement.getAttribute('data-cvv');

        if (currentText === '...') {
            // Show the actual CVV
            cvvElement.textContent = actualCVV;
        } else {
            // Hide the CVV
            cvvElement.textContent = '...';
        }
    }
	
	

	function copyCardNumberToClipboard() {
				var cardNumber = document.querySelector('.para').textContent;
				navigator.clipboard.writeText(cardNumber).then(function() {
					//alert('Card number copied to clipboard: ' + cardNumber);
					displayMessageDiv('Card number copied to clipboard: ' + cardNumber);
				}, function(err) {
					console.error('Could not copy text: ', err);
				});
	}
	
	
    // Add event listener to the eye icon to toggle CVV visibility
    document.querySelector('.viewCvv').addEventListener('click', toggleCVVVisibility);
	document.querySelector('.copyNo').addEventListener('click', copyCardNumberToClipboard);


    // Flip the card when clicked, excluding the eye icon
    const card = document.querySelector('.card');
    card.addEventListener('click', function(event) {
        // Check if the eye icon was clicked
        if (!event.target.classList.contains('viewCvv')) {
            card.classList.toggle('flipped');
			document.querySelector('.cvv').textContent='...';
        }
    });

    // Fetch ATM card details on page load
    fetchATMCardDetails();
});

function displayMessageDiv(message){
	let messageDiv = document.getElementById("responseMessageDiv");

	messageDiv.style.visibility = "visible";
	messageDiv.style.opacity = "0.8";
	messageDiv.innerHTML = "";
	
	messageDiv.innerHTML = message;
	messageDiv.classList.add("show");

	
	setTimeout(() => {
		messageDiv.style.opacity = "0"; // Start fade-outx
		setTimeout(() => {
			messageDiv.style.visibility = "hidden"; // Fully hide after fade-out
			//messageDiv.classList.remove("show");
			messageDiv.innerHTML = "";
		}, 500); // Match this delay with the CSS transition time
	}, 3000);
}
