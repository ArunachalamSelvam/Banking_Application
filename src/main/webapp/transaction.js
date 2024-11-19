document.addEventListener("DOMContentLoaded", () => {
    const methodSelect = document.getElementById("method");
    const mobileNoInput = document.getElementById("mobileNoInput");
    const accountNoInput = document.getElementById("accountNoInput");
    const sendMoneyForm = document.getElementById("sendMoneyForm");

    // Check if all elements are properly retrieved
    if (!methodSelect || !mobileNoInput || !accountNoInput || !sendMoneyForm) {
        console.error("One or more elements are missing from the DOM.");
        return;
    }

    // Toggle input fields based on selected method
    methodSelect.addEventListener("change", () => {
        if (methodSelect.value === "mobileNo") {
            mobileNoInput.classList.remove("hidden");
            accountNoInput.classList.add("hidden");
        } else if (methodSelect.value === "accountNo") {
            mobileNoInput.classList.add("hidden");
            accountNoInput.classList.remove("hidden");
        }
    });

    sendMoneyForm.addEventListener("submit", (event) => {
        event.preventDefault();

        const method = methodSelect.value; // Get the selected method (mobileNo or accountNo)
        const amount = document.getElementById("amount").value;
		//const password = document.getElementById("password").value;
        const data = {
			method : method,
            amount: amount,
			//password : password
        };

        if (method === "mobileNo") {
            data.mobileNo = document.getElementById("mobileNo").value;
        } else if (method === "accountNo") {
            data.receiverAccountNo = document.getElementById("receiverAccountNo").value;
        }

        fetch('http://localhost:8080/Banking_Project/api/TransactionServlet?action=transaction', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
        .then(response => {
			
			if (!response.ok) {
			            throw new Error('Failed to send money.');
			 }
			return response.json();
			
		})
        .then(result => {
			var userName = sessionStorage.getItem('userName');
            if (result.ok) {
				console.log("success");
                alert(result.message);
				
				window.location.href = `dashboard.html?userName=${encodeURIComponent(userName)}`;
            } else {
				console.log("success");
                alert(result.message);
				window.location.href = `dashboard.html?userName=${encodeURIComponent(userName)}`;
            }
        })
        .catch(error => {
            alert(error.message);
			window.location.href = `dashboard.html?userName=${encodeURIComponent(userName)}`;

        });
    });
});
