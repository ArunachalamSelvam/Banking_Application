function validateForm() {
    let isValid = true;

    // Clear previous error messages
    document.querySelectorAll('.errorMessage').forEach((element) => {
        element.textContent = '';
    });

    // First Name Validation
    const firstName = document.getElementById('firstName').value.trim();
    if (!firstName) {
        document.getElementById('firstNameError').textContent = 'First Name is required.';
        isValid = false;
    }

    // Last Name Validation
    const lastName = document.getElementById('lastName').value.trim();
    if (!lastName) {
        document.getElementById('lastNameError').textContent = 'Last Name is required.';
        isValid = false;
    }

    // Father's Name Validation
    const fatherName = document.getElementById('fatherName').value.trim();
    if (!fatherName) {
        document.getElementById('fatherNameError').textContent = "Father's Name is required.";
        isValid = false;
    }

    // Mother's Name Validation
    const motherName = document.getElementById('motherName').value.trim();
    if (!motherName) {
        document.getElementById('motherNameError').textContent = "Mother's Name is required.";
        isValid = false;
    }

    // Gender Validation
    const gender = document.getElementById('gender').value;
    if (!gender) {
        document.getElementById('genderError').textContent = 'Gender is required.';
        isValid = false;
    }

    // Mobile Number Validation (e.g., 10 digits)
    const mobileNumber = document.getElementById('mobileNumber').value.trim();
    const mobilePattern = /^[0-9]{10}$/;
    if (!mobilePattern.test(mobileNumber)) {
        document.getElementById('mobileNoError').textContent = 'Enter a valid 10-digit Mobile Number.';
        isValid = false;
    }

    // Aadhar Number Validation (e.g., 12 digits)
    const aadharNumber = document.getElementById('adharNumber').value.trim();
    const aadharPattern = /^[0-9]{12}$/;
    if (!aadharPattern.test(aadharNumber)) {
        document.getElementById('adharNoError').textContent = 'Enter a valid 12-digit Aadhar Number.';
        isValid = false;
    }

    // PAN Number Validation (e.g., format ABCDE1234F)
    const panNumber = document.getElementById('panNumber').value.trim();
    const panPattern = /^[A-Z]{5}[0-9]{4}[A-Z]{1}$/;
    if (!panPattern.test(panNumber)) {
        document.getElementById('panNoError').textContent = 'Enter a valid PAN Number (ABCDE1234F).';
        isValid = false;
    }

    // Password Validation (at least 6 characters)
	const passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;

	if (!passwordPattern.test(password)) {
	    passwordError.textContent = 'Password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character.';
	    isValid = false;
	} else {
	    passwordError.textContent = ''; // Clear any previous error message
	}

    // Address Validation
    const address = document.getElementById('address').value.trim();
    if (!address) {
        document.getElementById('addressError').textContent = 'Address is required.';
        isValid = false;
    }

    // Country and State Validation
    const countryId = document.getElementById('countryId').value;
    if (!countryId) {
        document.getElementById('countryError').textContent = 'Country selection is required.';
        isValid = false;
    }

    const stateId = document.getElementById('stateId').value;
    if (!stateId) {
        document.getElementById('stateError').textContent = 'State selection is required.';
        isValid = false;
    }

    return isValid;
}

// Event listener for form submission
/*document.getElementById('signUpForm').addEventListener('submit', (event) => {
    if (!validateForm()) {
        event.preventDefault(); // Prevent form submission if validation fails
		return;
    }
});*/


document.addEventListener('DOMContentLoaded', function() {	

	
	
	const signUpForm = document.querySelector('form');
	const stateDropdown = document.getElementById('stateId');
	const countryDropdown = document.getElementById('countryId');
	const branchDropdown = document.getElementById('branchId');
	const atmCardNeededDropdown = document.getElementById('atmCardNeeded'); // Ensure this ID matches the HTML

	const atmPinInput = document.getElementById('atmPin');

	//const video = document.getElementById('video');
	// const captureButton = document.getElementById('captureButton');
	//const canvas = document.getElementById('canvas');
	//const photoDataInput = document.getElementById('photoData');

	// Fetch countries
	fetch('http://localhost:8080/Banking_Project/countryServlet')
		.then(response => response.json())
		.then(countries => {
			countries.forEach(country => {
				const option = document.createElement('option');
				option.value = country.countryId;
				option.textContent = country.countryName;
				countryDropdown.appendChild(option);
			});
		});

	countryDropdown.addEventListener('change', function() {
		const selectedCountryId = this.value;

		fetch(`http://localhost:8080/Banking_Project/stateServlet?countryId=${selectedCountryId}`)
			.then(response => response.json())
			.then(states => {
				stateDropdown.innerHTML = ''; 
				states.forEach(state => {
					const option = document.createElement('option');
					option.value = state.stateId;
					option.textContent = state.stateName;
					stateDropdown.appendChild(option);
				});
			})
			.catch(error => console.error('Error fetching states:', error));
	});

	// Fetch branches
	/*fetch('/api/branches')
		.then(response => response.json())
		.then(branches => {
			branches.forEach(branch => {
				const option = document.createElement('option');
				option.value = branch.branchId;
				option.textContent = branch.branchName;
				branchDropdown.appendChild(option);
			});
		});*/
		
	/*	
	function loadSecurityQuestions(){
		fetch('http://localhost:8080/Banking_Project/QuestionServlet')
		.then(response => response.json()) // Convert response to JSON
			.then(data =>{
				const select1 = document.getElementById("question1");
				const select2 = document.getElementById("question2");
				
				data.forEach(question =>{
					const option1 = document.createElement('option');
					option1.value = data.questionId;
					option1.text = data.question;
					
					select1.appendChild('option1');
					
					const option2 = document.createElement('option'); 
					option2.value = data.questionId;
					option2.value = data.question;
					select2.appendChild('option2');
					
					
				});
			})
			.catch(error => console.error("Error while fetching questions :", error));
	}	
	
	window.onload(loadSecurityQuestions());  */
	

		
/*
	atmCardNeededDropdown.addEventListener('change', function() {
		if (this.value === 'true') {
			atmPinInput.style.display = 'block';
			atmPinInput.setAttribute('required', 'true');
		} else {
			atmPinInput.style.display = 'none';
			atmPinInput.removeAttribute('required');
		}
	});

*/	
	const addPhotoButton = document.getElementById('addPhotoButton');
	const video = document.getElementById('video');
	const canvas = document.getElementById('canvas');
	const photoData = document.getElementById('photoData');

	let isCameraOn = false;

	addPhotoButton.addEventListener('click', function() {
	    if (!isCameraOn) {
	        // Start the camera
	        navigator.mediaDevices.getUserMedia({ video: true })
	            .then(function(stream) {
	                video.srcObject = stream;
	                video.style.display = 'block';
	                canvas.style.display = 'none';
	                addPhotoButton.textContent = 'Capture Photo';
	                isCameraOn = true;
	            })
	            .catch(function(error) {
	                console.error('Error accessing the camera: ', error);
	            });
	    } else {
	        // Capture the photo
			video.style.display = 'none';
		    canvas.style.display = 'block';
	        const context = canvas.getContext('2d');
	        context.drawImage(video, 0, 0, canvas.width, canvas.height);

	        // Get the image data as base64
	        const imageData = canvas.toDataURL('image/png');
	        photoData.value = imageData; // Store the base64 data in the hidden input
	        addPhotoButton.textContent = 'Photo Captured';
			
			console.log('Photo Data:', photoData.value); // Check if it's correctly populated

	        
	        // Stop the camera
	        let stream = video.srcObject;
	        let tracks = stream.getTracks();
	        tracks.forEach(track => track.stop());
	        //video.style.display = 'none';
	    }
	});

	// Access the user's camera
	/* navigator.mediaDevices.getUserMedia({ video: true })
		 .then(stream => {
			 video.srcObject = stream;
		 })
		 .catch(error => {
			 console.error('Error accessing camera:', error);
			 alert('Unable to access camera. Please ensure you have granted permission.');
		 });
 
	 // Capture photo
	 captureButton.addEventListener('click', function() {
		 const context = canvas.getContext('2d');
		 canvas.width = video.videoWidth;
		 canvas.height = video.videoHeight;
		 context.drawImage(video, 0, 0, canvas.width, canvas.height);
 
		 // Convert the captured image to Base64
		 const photoData = canvas.toDataURL('image/png');
		 photoDataInput.value = photoData;
 
		 // Optionally display the captured image
		 canvas.style.display = 'block';
	 });
 */
/*
	let canvas = document.querySelector("#canvas");
	let context = canvas.getContext("2d");
	let video = document.querySelector("#video");
	if (navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
		navigator.mediaDevices.getUserMedia({ video: true }).then((stream) => {
			video.srcObject = stream;
			video.play();
		});
	}
	document.getElementById("snap").addEventListener("click", () => {
		context.drawImage(video, 0, 0, 640, 480);

		canvas.toBlob(function(blob) {
			console.log(typeof (blob))
			var blobUrl = URL.createObjectURL(blob);
			var link = document.createElement("a");
			link.href = blobUrl;
			link.download = "image.jpg";
			link.innerHTML = "Click here to download the file";
			document.body.appendChild(link);
			document.querySelector('a').click()
		}, 'image/jpeg', 1);

	});
	
*/	
	// Handle form submission
	signUpForm.addEventListener('submit', function(event) {
		event.preventDefault();
		if (!validateForm()) {
				return;
		}

		// Collect form data
		const formData = new FormData(signUpForm);
		formData.append('photoData', photoData.value); // Ensure 'photoData' matches the name in servlet

		const data = {
			firstName: formData.get('firstName').trim(),
			middleName: formData.get('middleName').trim(),
			lastName: formData.get('lastName').trim(),
			fatherName: formData.get('fatherName').trim(),
			motherName: formData.get('motherName').trim(),
			gender: formData.get('gender').trim(),
			mobileNumber: formData.get('mobileNumber'),
			//emailId: formData.get('emailId').trim(),
			adharNumber: formData.get('adharNumber'),
			panNumber: formData.get('panNumber').trim(),
			password: formData.get('password'),
			address: formData.get('address').trim(),
			stateId: formData.get("stateId"),
			countryId: formData.get("countryId"),
			branchId: 1,
			isAtmCardNeeded: formData.get('atmCardNeeded') === 'true' || false,
			atmPin: formData.get('atmPin') != null ? formData.get('atmPin') : "",
			photoData: formData.get('photoData'), // Include captured photo data
			
			//question1Id : formData.get('question1'),
			//question2Id : formData.get('question2'),
			//answer1: formData.get('answer1'),
			//answer2 : formData.get('answer2')
			
		};

		// Make the API call
		fetch('http://localhost:8080/Banking_Project/signUpServlet', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(data)
		})
			.then(response => {
				if (!response.ok) {
					// Handle HTTP errors
					throw new Error('Network response was not ok.');
				}
				return response.json(); // Return parsed JSON response
			})
			.then(data => {
				console.log('Success:', data); // Log success data
				//sessionStorage.setItem('userName', data.firstName);
				//sessionStorage.setItem('emailId', data.emailId);
				alert("SignUp Successful");
				// Redirect or perform further actions
				window.location.href = 'http://localhost:8080/Banking_Project/success.jsp';
			})
			.catch(error => {
				console.error('Error:', error); // Log error for debugging
				alert('An error occurred. Please try again.');
			}); 
	});
	//const select1 = document.getElementById("question1");
	   //const select2 = document.getElementById("question2");
	// Event listener for the first select
	   /* select1.addEventListener('change', function() {
	        const selectedValue = this.value;

	        // Remove the selected question from the second select
	        Array.from(select2.options).forEach(option => {
	            if (option.value === selectedValue) {
	                option.remove();
	            }
	        });
	    });

	    // Event listener for the second select (optional)
	    select2.addEventListener('change', function() {
	        const selectedValue = this.value;

	        // Optionally, you might want to add back the selected question in the first select
	        Array.from(select1.options).forEach(option => {
	            if (option.value === selectedValue) {
	                // If you want to add it back, you can recreate the option or manage state accordingly
	                const newOption = document.createElement('option');
	                newOption.value = selectedValue;
	                newOption.text = option.text;
	                select2.appendChild(newOption);
	            }
	        });
	    });*/
});
