window.onload = function() {
    fetch('financial-dashboard/data') // Adjust this URL based on your deployment
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.text();
        })
        .then(data => {
            const lines = data.split('\n').filter(line => line.trim() !== '');
            document.getElementById('total-transactions').innerText = lines[0] ? lines[0].split(': ')[1] : 'N/A';
            document.getElementById('total-amount').innerText = lines[1] ? lines[1].split(': ')[1] : 'N/A';
            document.getElementById('total-fraudulent').innerText = lines[2] ? lines[2].split(': ')[1] : 'N/A';
            document.getElementById('average-amount').innerText = lines[3] ? lines[3].split(': ')[1] : 'N/A';
            document.getElementById('fraud-rate').innerText = lines[4] ? lines[4].split(': ')[1] : 'N/A';
        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
            document.getElementById('total-transactions').innerText = 'Error fetching data';
            document.getElementById('total-amount').innerText = 'Error fetching data';
            document.getElementById('total-fraudulent').innerText = 'Error fetching data';
            document.getElementById('average-amount').innerText = 'Error fetching data';
            document.getElementById('fraud-rate').innerText = 'Error fetching data';
        });
};
