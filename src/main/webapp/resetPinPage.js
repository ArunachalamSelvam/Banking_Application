// resetPin.js
document.getElementById('confirmPin').addEventListener('input', function() {
    const newPin = document.getElementById('newPin').value;
    const confirmPin = document.getElementById('confirmPin').value;

    if (confirmPin !== newPin) {
        document.getElementById('message').innerText = 'New PIN and Confirm PIN do not match.';
    } else {
        document.getElementById('message').innerText = ''; // Clear message if they match
    }
});


document.getElementById('resetPinForm').addEventListener('submit', function(event) {
	
	
	
	
    event.preventDefault();
    
    const oldPin = document.getElementById('oldPin').value;
    const newPin = document.getElementById('newPin').value;
    const confirmPin = document.getElementById('confirmPin').value;
    
    if (newPin !== confirmPin) {
        document.getElementById('message').innerText = 'New PIN and confirm PIN should be same.';
        return;
    }
    
    fetch('http://localhost:8080/Banking_Project/AtmCardServlet?action=resetPin', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            oldPin: oldPin,
            newPin: newPin
        })
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            //document.getElementById('message').innerText = 'PIN reset successfully.';
			alert('PIN reset successfully.');
        } else {
			alert(data.error);
            //document.getElementById('message').innerText = data.error;
        }
		//window.location.href('http://localhost:8080/Banking_Project/atmCard.html');
		window.history.back();

    })
    .catch(error => {
        console.error('Error:', error);
        document.getElementById('message').innerText = 'An error occurred while resetting PIN.';
    });
});
