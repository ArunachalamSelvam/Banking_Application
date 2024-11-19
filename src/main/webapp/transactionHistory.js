// Function to fetch transaction history based on filter type
function fetchTransactionHistory(filterType) {
    let startDate, endDate;

    if (filterType === '7-days') {
        startDate = new Date();
        endDate = new Date();
        startDate.setDate(endDate.getDate() - 7);
    } else if (filterType === '30-days') {
        startDate = new Date();
        endDate = new Date();
        startDate.setDate(endDate.getDate() - 30);
    } else if (filterType === 'custom') {
        startDate = new Date(document.getElementById('startDatePicker').value);
        endDate = new Date(document.getElementById('endDatePicker').value);
    }

	const formattedStartDate = formatDate(startDate);
	    const formattedEndDate = formatDate(endDate);

	    const url = `http://localhost:8080/Banking_Project/api/TransactionServlet?startDate=${formattedStartDate}&endDate=${formattedEndDate}`;

	    fetch(url, {
	        method: 'GET',
	        headers: {
	            'Content-Type': 'application/json'
	        }
	    })
		
		.then(response => {
			/*if(response.redirected){
				console.log(response.url);
				window.location.href= response.url;
			}*/
			return response.json();
		})
		.then(data => {
	        populateTable(data.transactions);
	    })
	    .catch(error => {
	        console.error('Error fetching transaction history:', error);
	    });
}

// Function to show custom date picker
function showCustomDatePicker() {
    document.getElementById('customDatePicker').style.display = 'block';
}

// Function to apply custom date range
function applyCustomDateRange() {
    fetchTransactionHistory('custom');
    document.getElementById('customDatePicker').style.display = 'none';
}

// Function to populate table with transaction data
function populateTable(transactions) {
    const tableBody = document.getElementById('transactionTableBody');
    tableBody.innerHTML = '';

    transactions.forEach(transaction => {
		
		const description = transaction.description ? transaction.description : '';
		const amount = transaction.amount < 0 ? transaction.amount+'' : `+${transaction.amount}`;

        const row = document.createElement('tr');
		
        row.innerHTML = `
            <td>${transaction.transactionDate}</td>
            <td>${description}</td>
            <td>${amount}</td>
        `;
        tableBody.appendChild(row);
    });
}

// Utility function to format date as YYYY-MM-DD HH:MM:SS
function formatDate(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');
    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

// Event listener for the filter dropdown
document.getElementById('filterSelect').addEventListener('change', function() {
    const filterType = this.value;
    if (filterType === 'custom') {
        showCustomDatePicker();
    } else {
        fetchTransactionHistory(filterType);
    }
});

// Event listener for the custom date range apply button
document.getElementById('applyCustomDateRange').addEventListener('click', function() {
    fetchTransactionHistory('custom');
});

// Initialize page with last 7 days transaction history on load
document.addEventListener('DOMContentLoaded', function() {
    fetchTransactionHistory('7-days');
});


