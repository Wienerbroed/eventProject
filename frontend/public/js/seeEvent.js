// JavaScript code for handling modals, form submissions, and fetching data

// Get modal elements
const scheduleModal = document.getElementById('scheduleModal');
const expenseModal = document.getElementById('expenseModal');
const addScheduleBtn = document.getElementById('addScheduleBtn');
const addExpenseBtn = document.getElementById('addExpenseBtn');
const closeBtns = document.querySelectorAll('.close');

// Open modal on button click
addScheduleBtn.onclick = function () {
    scheduleModal.style.display = 'block';
};

addExpenseBtn.onclick = function () {
    expenseModal.style.display = 'block';
};

// Close modal on close button click
closeBtns.forEach(btn => {
    btn.onclick = function () {
        scheduleModal.style.display = 'none';
        expenseModal.style.display = 'none';
    };
});

// Close modal when clicking outside of it
window.onclick = function (event) {
    if (event.target === scheduleModal) {
        scheduleModal.style.display = 'none';
    }
    if (event.target === expenseModal) {
        expenseModal.style.display = 'none';
    }
};

// Handle schedule form submission
document.getElementById('scheduleForm').addEventListener('submit', function (event) {
    event.preventDefault();

    const eventId = urlParams.get('id'); // Use the event ID from the URL parameters
    const scheduleData = {
        scheduleDate: document.getElementById('scheduleDate').value,
        startTime: document.getElementById('startTime').value,
        endTime: document.getElementById('endTime').value,
    };

    fetch(`http://localhost:3000/api/events/addSchedule/${eventId}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(scheduleData),
    })
        .then((response) => {
            if (response.ok) {
                alert('Schedule added successfully!');
                scheduleModal.style.display = 'none';
                fetchEventSchedule(eventId);
            } else {
                alert('Failed to add schedule.');
            }
        })
        .catch((error) => console.error('Error:', error));
});

// Handle expense form submission
document.getElementById('expenseForm').addEventListener('submit', function (event) {
    event.preventDefault();

    const eventId = urlParams.get('id'); // Use the event ID from the URL parameters
    const expenseData = {
        time: document.getElementById('expenseTime').value,
        prize: document.getElementById('expensePrize').value,
        cost: document.getElementById('expenseCost').value,
    };

    fetch(`http://localhost:3000/api/events/${eventId}/addExpense`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(expenseData),
    })
        .then((response) => {
            if (response.ok) {
                alert('Expense added successfully!');
                expenseModal.style.display = 'none';
                fetchEventExpenses(eventId);
            } else {
                alert('Failed to add expense.');
            }
        })
        .catch((error) => console.error('Error:', error));
});

function fetchEventDetails(eventId) {
    fetch(`http://localhost:3000/api/events/${eventId}`)
        .then((response) => response.json())
        .then((event) => displayEventDetails(event))
        .catch((error) => console.error('Error:', error));
}

function displayEventDetails(event) {
    document.getElementById('eventId').value = event.eventId;
    document.getElementById('title').value = event.title;
    document.getElementById('eventCreator').value = event.eventCreator;
    document.getElementById('eventResponsible').value = event.eventResponsible;
    document.getElementById('eventControl').value = event.eventControl;
    document.getElementById('eventType').value = event.eventType;
    document.getElementById('description').value = event.description;
    document.getElementById('maxParticipants').value = event.maxParticipants;
    document.getElementById('maxAudience').value = event.maxAudience;
    document.getElementById('conguideDk').value = event.conguideDk;
    document.getElementById('conguideEn').value = event.conguideEn;
}

async function updateEvent(event) {
    event.preventDefault();
    const eventId = document.getElementById('eventId').value;
    const updatedEvent = {
        title: document.getElementById('title').value,
        eventCreator: document.getElementById('eventCreator').value,
        eventResponsible: document.getElementById('eventResponsible').value,
        eventControl: document.getElementById('eventControl').value,
        eventType: document.getElementById('eventType').value,
        description: document.getElementById('description').value,
        maxParticipants: document.getElementById('maxParticipants').value,
        maxAudience: document.getElementById('maxAudience').value,
        conguideDk: document.getElementById('conguideDk').value,
        conguideEn: document.getElementById('conguideEn').value
    };

    try {
        const response = await fetch(`http://localhost:3000/api/events/${eventId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(updatedEvent)
        });

        if (!response.ok) {
            const errorMessage = await response.text();
            console.error('Error:', errorMessage);
            throw new Error('Failed to update event: ' + errorMessage);
        }
        alert('Event updated successfully');
        fetchEventDetails(eventId); // Fetch and display the updated event details
    } catch (error) {
        console.error('Error:', error);
    }
}

// Add event listener for the update event form submission
document.getElementById('updateEventForm').addEventListener('submit', updateEvent);

function fetchEventSchedule(eventId) {
    fetch(`http://localhost:3000/api/events/${eventId}/schedule`)
        .then((response) => response.json())
        .then((schedule) => displayEventSchedule(schedule))
        .catch((error) => console.error('Error:', error));
}

function displayEventSchedule(schedule) {
    const scheduleList = document.getElementById('scheduleList');
    scheduleList.innerHTML = ''; // Clear existing schedule

    schedule.forEach(item => {
        const scheduleItem = document.createElement('div');
        scheduleItem.className = 'schedule-item';
        scheduleItem.innerHTML = `
        <p><strong>Date:</strong> ${item.scheduleDate}</p>
        <p><strong>Start Time:</strong> ${item.startTime}</p>
        <p><strong>End Time:</strong> ${item.endTime}</p>
        <button class="delete-btn" onclick="deleteSchedule(${item.scheduleId})">Delete</button>
    `;
        scheduleList.appendChild(scheduleItem);
    });
}

async function deleteSchedule(scheduleId) {
    const confirmation = confirm("Are you sure you want to delete this schedule?");
    if (!confirmation) return;

    try {
        const response = await fetch(`http://localhost:3000/api/events/schedule/${scheduleId}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            alert('Schedule deleted successfully!');
            fetchEventSchedule(eventId); // Refresh the event list after deletion
        } else {
            alert('Failed to delete schedule.');
        }
    } catch (error) {
        console.error('Error:', error);
    }
}

function fetchEventExpenses(eventId) {
    fetch(`http://localhost:3000/api/events/${eventId}/expenses`)
        .then((response) => response.json())
        .then((expenses) => displayEventExpenses(expenses))
        .catch((error) => console.error('Error:', error));
}

function displayEventExpenses(expenses) {
    const expenseList = document.getElementById('expenseList');
    expenseList.innerHTML = ''; // Clear existing expenses

    expenses.forEach(item => {
        const expenseItem = document.createElement('div');
        expenseItem.className = 'expense-item';
        expenseItem.innerHTML = `
        <p><strong>Time:</strong> ${item.time}</p>
        <p><strong>Prize:</strong> ${item.prize}</p>
        <p><strong>Cost:</strong> ${item.cost}</p>
        <button class="delete-btn" onclick="deleteExpense(${item.expenseId})">Delete</button>
    `;
        expenseList.appendChild(expenseItem);
    });
}

async function deleteExpense(expenseId) {
    const confirmation = confirm("Are you sure you want to delete this expense?");
    if (!confirmation) return;

    try {
        const response = await fetch(`http://localhost:3000/api/events/expenses/${expenseId}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            alert('Expense deleted successfully!');
            fetchEventExpenses(eventId); // Refresh the expense list after deletion
        } else {
            alert('Failed to delete expense.');
        }
    } catch (error) {
        console.error('Error:', error);
    }
}

const urlParams = new URLSearchParams(window.location.search);
const eventId = urlParams.get('id');

if (eventId) {
    fetchEventDetails(eventId);
    fetchEventSchedule(eventId);
    fetchEventExpenses(eventId);
}