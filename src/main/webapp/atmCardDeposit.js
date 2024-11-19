/**
 * 
 */

function validateForm() {
    // Get input values and error span elements
    const cardNumber = document.getElementById("cardNumber").value;
    const cvv = document.getElementById("cvv").value;
    const expiryDate = document.getElementById("expiryDate").value;
    const cardHolderName = document.getElementById("cardHolderName").value;
    const pin = document.getElementById("pin").value;
    const amount = document.getElementById("amount").value;

    const cardNumberError = document.getElementById("cardNumberError");
    const cvvError = document.getElementById("cvvError");
    const expiryDateError = document.getElementById("expiryDateError");
    const cardHolderNameError = document.getElementById("cardHolderNameError");
    const pinError = document.getElementById("pinError");
    const amountError = document.getElementById("amountError");

    let isValid = true;

    // Reset error messages
    cardNumberError.innerText = "";
    cvvError.innerText = "";
    expiryDateError.innerText = "";
    cardHolderNameError.innerText = "";
    pinError.innerText = "";
    amountError.innerText = "";

    // Validate card number
    if (!/^\d{16}$/.test(cardNumber)) {
        cardNumberError.innerText = "Card number must be a 16-digit number.";
        isValid = false;
    }

    // Validate CVV
    if (!/^\d{3}$/.test(cvv)) {
        cvvError.innerText = "CVV must be a 3-digit number.";
        isValid = false;
    }

    // Validate expiry date
    if (!/^(0[1-9]|1[0-2])\/\d{2}$/.test(expiryDate)) {
        expiryDateError.innerText = "Expiry date must be in the format MM/YY.";
        isValid = false;
    }

    // Validate card holder name
    if (!/^[a-zA-Z ]+$/.test(cardHolderName)) {
        cardHolderNameError.innerText = "Card holder name can only contain letters and spaces.";
        isValid = false;
    }

    // Validate pin
    if (!/^\d{4}$/.test(pin)) {
        pinError.innerText = "PIN must be a 4-digit number.";
        isValid = false;
    }

    // Validate amount
    if (isNaN(amount) || amount <= 0 || !/^[1-9]\d*$/.test(amount)) {
        amountError.innerText = "Amount must be greater than zero and a whole number.";
        isValid = false;
    }

    return isValid;
}

let errorPage = document.getElementById("errorPage");
let errorPageMessage = document.querySelector("#errorPage h2");
let formContainer = document.querySelector(".form-container");
document.addEventListener('DOMContentLoaded', function(){
	let isFreeze = sessionStorage.getItem("freezeStatus");
	
	if(isFreeze !=null){
		if(isFreeze == "true"){
			errorPageMessage.innerText = "Your Atm card is temporarily freezed.\nTo continue transaction unfreeze the card."
			formContainer.style.display = "none";
			errorPage.style.display = "flex";
		}else{
			errorPage.style.display = "none";
			formContainer.style.display = "block";
			errorPageMessage.innerText ="";
		}
	}
})

// Attach validateForm to the form's submit event
document.getElementById("atmCardForm").addEventListener("submit", function(event) {
    if (!validateForm()) {
        event.preventDefault();  // Prevent form submission if validation fails
    }
});


//let amount = document.getElementById("amount");
document.getElementById("amount").addEventListener('input',function(){
	let amount = document.getElementById("amount").value;
	const amountError = document.getElementById("amountError");
	console.log(amountError);
	if((amount<=0 && amount.length>0)|| amount != Math.floor(amount)){
		amountError.style.display = "inline";
		//amount=amount.replace('-',"");
	}else{
		amountError.style.display = "none";
	}
})

document.getElementById('atmCardForm').addEventListener('submit', function(event) {
	console.log("inside atmcard submission");
	//let amountInput = document.getElementById('amount').value;
	       /* if (amount<=0  || amount != Math.floor(amount)) {
	            event.preventDefault(); // Stop form submission
	            alert("Please enter a valid amount greater than zero.");
				return;
	        }*/
    event.preventDefault();  
	
	if (!validateForm()) {
	        event.preventDefault();  // Prevent form submission if validation fails
			return;
	}

   
    const cardNo = document.getElementById('cardNumber').value;
    const cvv = document.getElementById('cvv').value;
    const expiryDate = document.getElementById('expiryDate').value;
    const cardHolderName = document.getElementById('cardHolderName').value;
    const amount = document.getElementById('amount').value;
	const pin = document.getElementById('pin').value;


    // Create the request payload
    const atmCardTransactionRequest = {
        cardNo: cardNo,
        cvv: cvv,
        expiryDate: expiryDate,
        cardHolderName: cardHolderName,
        amount: amount, 
		pin : pin
    };

    // Send the AJAX request using Fetch API
    fetch('http://localhost:8080/Banking_Project/AtmCardServlet?action=deposit', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(atmCardTransactionRequest)
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            // Handle success response
            alert(data.message || "Deposit successful!");
            // Optionally, you can reset the form or redirect the user
            document.getElementById('atmCardForm').reset();
        } else {
            // Handle error response
            alert(data.message || "Deposit failed. Please try again.");
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert("An error occurred while processing your request.");
    });
});
