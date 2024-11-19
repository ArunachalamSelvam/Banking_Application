/**
 * 
 */

document.getElementById("arrow").addEventListener('click',function(){
	console.log("inside");
	if(this.classList.contains("rotate")){
		this.classList.remove("rotate");
	}else{
		this.classList.add("rotate");
	}
	let historyDiv = document.getElementById("transactionHistory");
	console.log(historyDiv.children);
	if(Array.from(historyDiv.children).length>0){
	    console.log("inside if function");
	        historyDiv.innerHTML = "";
	}else{
		console.log("inside else function")
		historyDiv.innerHTML = "<iframe src='transactionHistory.html' width = '100%' height='100%' title='transactionHistory'></iframe>"
	}
})

document.addEventListener('DOMContentLoaded',function fetchBalance() {
	        fetch('http://localhost:8080/Banking_Project/SavingsAccountServlet?action=getBalance')
	            .then(response => response.json())
	            .then(data => {
	                if (data.success) {
	                    document.getElementById('balance').textContent = "RS." +data.balance;
	                } else {
	                    document.getElementById('balance').textContent = "N/A";
	                }
	            })
	            .catch(error => {
	                console.error('Error fetching balance:', error);
	                document.getElementById('balance').textContent = "Error";
	            });
	    })
