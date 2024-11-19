/**
 * 
 */

let freezeButton = document.getElementById("freezeBtn");
let sleepButton = document.getElementById("sleepBtn");
let blockButton = document.getElementById("blockBtn");
let messageDiv = document.getElementById("responseMessageDiv");
let confirmationDiv = document.getElementById("confirmationDiv");
let confirmBtn = document.getElementById("confirmBtn");
let cancelBtn = document.getElementById("cancelBtn");
let confirmationMessageElement = document.getElementById("question");



let isFreeze = false;
let isSleep = false;
let isBlock = false;

document.addEventListener("DOMContentLoaded", function() {
	console.log("inside getFreezeStatus");
	let freezeStatus = sessionStorage.getItem("freezeStatus")
	if(sessionStorage.getItem("freezeStatus")!=null){
		if(freezeStatus=="true"){
			freezeButton.classList.add("active");
			isFreeze = true;
			console.log("inside freezs status true session set method.");
		}
		
		console.log("inside freezs status session set method.");
		
	}
	else{
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
			if (data.isFreeze) {
				
				freezeButton.classList.add("active");
			}
			
			isFreeze = data.isFreeze;
			sessionStorage.setItem("freezeStatus",data.isFreeze);
		})
	
	}
	
	if(sessionStorage.getItem("sleepStatus")!=null){
		let sleepStatus = sessionStorage.getItem("sleepStatus");
		if(sleepStatus=="true"){
			sleepButton.classList.add("active");
			isSleep = true;
			console.log("inside freezs status true session set method.");
		}
		
		console.log("inside freezs status session set method.");
		
	}
	
	else{
	
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
			if (data.isSleep) {
				
				sleepButton.classList.add("active");
			}
			
			isSleep = data.isSleep;
			
			sessionStorage.setItem("sleepStatus",data.isSleep);
		})
	}
	
})

freezeButton.addEventListener('click', function() {
	debugger
	if(!this.classList.contains("active")){
			confirmationMessageElement.innerText = "Would you like to freeze the card?";
	        confirmationDiv.style.display = "flex";
	        container.classList.toggle("blur");
	        isFreeze = true;

	    }else{
	        isFreeze = false;
			
			freezeFetchMethod(false);
			/*sleepButton.style.filter = blur(${1}px);
			sleepButton.style.pointerEvents = "none";*/
	    }

	
});

confirmBtn.addEventListener('click',function(){
	debugger
    if(isFreeze){
        // alert("Freeze confirmed");
        confirmationDiv.style.display = "none";
         container.classList.toggle("blur");
		 
		 freezeFetchMethod(true);
		 
		confirmationMessageElement.innerText ="";

    }
	else if(isSleep){
		confirmationDiv.style.display = "none";
		        container.classList.toggle("blur");
			 
			 sleepStatusFetchMethod(true);
			 
			confirmationMessageElement.innerText ="";
	}
});



cancelBtn.addEventListener('click', function(){
    if(isFreeze && !isSleep){
        confirmationDiv.style.display = "none";
        //freezeButton.classList.toggle('active');
        isFreeze=false;
         container.classList.toggle("blur");
		 confirmationMessageElement.innerText ="";
    }
	else if(isSleep){
		confirmationDiv.style.display = "none";
		        //freezeButton.classList.toggle('active');
		        isSleep=false;
		         container.classList.toggle("blur");
				 confirmationMessageElement.innerText ="";
	}
	
	
})


sleepButton.addEventListener('click', function() {
	debugger
	if(isFreeze && !isSleep){
		displayMessageDiv("Please unfreeze the card to put it to sleep.");
		return;
	}
	
	if(!this.classList.contains("active")){
				confirmationMessageElement.innerText = "Put Card to sleep?";
		        confirmationDiv.style.display = "flex";
		        container.classList.toggle("blur");
		        isSleep = true;

		    }else{
		        isSleep = false;
				
				sleepStatusFetchMethod(false);
				/*sleepButton.style.filter = blur(${1}px);
				sleepButton.style.pointerEvents = "none";*/
		    }
	
	
	//toggle(sleepButton);
});

function blockStatusFetchMethod(blockStatus){
	console.log("inside freeze confirm");
		fetch('http://localhost:8080/Banking_Project/AtmCardServlet?action=blockStatus', {
			method: 'PUT',
			headers: {
				'Content-Type': 'application/json'
			},

		}).then(response => {
			if (!response.ok) {
				// Handle HTTP errors
				throw new Error('Network response was not ok.');
			}
			return response.json(); // Return parsed JSON response
		})
			.then(data => {
				console.log('Success:', data); 
				toggle(blockButton);
				displayMessageDiv(data.message);
				sessionStorage.setItem("freezeStatus",blockStatus);
			

			})
			.catch(error => {
				console.error('Error:', error); // Log error for debugging
				alert('An error occurred. Please try again.');
			});
		
}

function freezeFetchMethod(freezeStatus){
	console.log("inside freeze confirm");
		fetch('http://localhost:8080/Banking_Project/AtmCardServlet?action=freezeStatus', {
			method: 'PUT',
			headers: {
				'Content-Type': 'application/json'
			},

		}).then(response => {
			if (!response.ok) {
				// Handle HTTP errors
				throw new Error('Network response was not ok.');
			}
			return response.json(); // Return parsed JSON response
		})
			.then(data => {
				console.log('Success:', data); 
				toggle(freezeButton);
				displayMessageDiv(data.message);
				sessionStorage.setItem("freezeStatus",freezeStatus);
			

			})
			.catch(error => {
				console.error('Error:', error); // Log error for debugging
				alert('An error occurred. Please try again.');
			});
		
}

function sleepStatusFetchMethod(sleepStatus){
	console.log("inside freeze confirm");
		fetch('http://localhost:8080/Banking_Project/AtmCardServlet?action=sleepStatus', {
			method: 'PUT',
			headers: {
				'Content-Type': 'application/json'
			},

		}).then(response => {
			if (!response.ok) {
				// Handle HTTP errors
				throw new Error('Network response was not ok.');
			}
			return response.json(); // Return parsed JSON response
		})
			.then(data => {
				console.log('Success:', data); 
				toggle(sleepButton);
				displayMessageDiv(data.message);
				sessionStorage.setItem("sleepStatus",sleepStatus);

			})
			.catch(error => {
				console.error('Error:', error); // Log error for debugging
				alert('An error occurred. Please try again.');
			});
		
}


function displayMessageDiv(message){
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

blockButton.addEventListener('click', function(){
	toggle(this);
})
function toggle(button) {
	button.classList.toggle("active");
}