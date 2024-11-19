/*document.getElementById('resetForm').addEventListener('input', function(event) {
            // Get the password values
            const newPassword = document.getElementById('newPassword').value;
            const confirmPassword = document.getElementById('confirmpassword').value;

            // Check if passwords match
            if (newPassword !== confirmPassword) {
                // If not, prevent the form submission
                event.preventDefault();
                
                // Show the error message
                document.getElementById('errorMessage').style.display = 'block';
            } else {
                // Hide the error message if passwords match
                document.getElementById('errorMessage').style.display = 'none';
            }
        });
	*/	
		const newPasswordInput = document.getElementById('newPassword');
		        const confirmPasswordInput = document.getElementById('confirmpassword');
		        const errorMessage = document.getElementById('errorMessage');
		        const submitButton = document.getElementById('submitButton');

		        newPasswordInput.addEventListener('input', validatePasswords);
		        confirmPasswordInput.addEventListener('input', validatePasswords);

		        function validatePasswords() {
					
		            const newPassword = newPasswordInput.value;
		            const confirmPassword = confirmPasswordInput.value;

		            if (newPassword !== confirmPassword) {
		                errorMessage.style.display = 'block';  
		                submitButton.disabled = true; 
		            } else {
		                errorMessage.style.display = 'none';  
		                submitButton.disabled = false;  
		            }
					
				}	