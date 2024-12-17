// Get URL parameters
const urlParams = new URLSearchParams(window.location.search);
const eventId = urlParams.get('id'); // Use the event ID from the URL parameters

// Get modal elements
const scheduleModal = document.getElementById('scheduleModal');
const expenseModal = document.getElementById('expenseModal');
const requirementModal = document.getElementById('requirementModal');
const addScheduleBtn = document.getElementById('addScheduleBtn');
const addExpenseBtn = document.getElementById('addExpenseBtn');
const addRequirementBtn = document.getElementById('addRequirementBtn');
const closeBtns = document.querySelectorAll('.close');

// Open modal on button click
addScheduleBtn.onclick = function () {
    scheduleModal.style.display = 'block';
};

addExpenseBtn.onclick = function () {
    expenseModal.style.display = 'block';
};

addRequirementBtn.onclick = function () {
    requirementModal.style.display = 'block';
};

// Close modal on close button click
closeBtns.forEach(btn => {
    btn.onclick = function () {
        scheduleModal.style.display = 'none';
        expenseModal.style.display = 'none';
        requirementModal.style.display = 'none';
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
    if (event.target === requirementModal) {
        requirementModal.style.display = 'none';
    }
};

// Fetch event details, schedule, expenses, requirements
async function fetchEventDetails(eventId) {
    try {
        const response = await fetch(`http://localhost:3000/api/events/${eventId}`);
        const event = await response.json();
        if (event) {
            displayEventDetails(event);
            fetchEventSchedule(eventId);
            fetchEventExpenses(eventId);
            fetchEventRequirements(eventId); // Fetch requirements as well
        }
    } catch (error) {
        console.error('Error fetching event details:', error);
    }
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

    // Set the eventRoomId hidden field
    document.getElementById('eventRoomId').value = event.eventRoomId;

    // Display Event Room Name and Address if needed
    document.getElementById('eventRoomName').value = event.eventRoom?.eventRoomName || '';
    document.getElementById('eventRoomAddress').value = event.eventRoom?.eventRoomAddress || '';

    // Handle warnings
    const warnings = event.warnings ? event.warnings.split(',') : [];
    warnings.forEach(w => {
        const normalized = w.trim().toLowerCase();
        if (normalized === 'wheelchair') document.getElementById('wheelchair').checked = true;
        if (normalized === 'strobelights') document.getElementById('strobelights').checked = true;
        if (normalized === 'loud noises') document.getElementById('loudNoises').checked = true;
    });
}



async function updateEvent(event) {
    event.preventDefault();

    const updatedEvent = {
        eventId: document.getElementById('eventId').value,
        title: document.getElementById('title').value,
        eventCreator: document.getElementById('eventCreator').value,
        eventResponsible: document.getElementById('eventResponsible').value,
        eventControl: document.getElementById('eventControl').value,
        eventType: document.getElementById('eventType').value,
        description: document.getElementById('description').value,
        maxParticipants: document.getElementById('maxParticipants').value,
        maxAudience: document.getElementById('maxAudience').value,
        conguideDk: document.getElementById('conguideDk').value,
        conguideEn: document.getElementById('conguideEn').value,
        warnings: Array.from(document.querySelectorAll('input[name="warnings"]:checked'))
            .map(checkbox => checkbox.value).join(','),
        eventRoomId: document.getElementById('eventRoomId').value // Include eventRoomId
    };

    try {
        const response = await fetch(`http://localhost:3000/api/events/${updatedEvent.eventId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(updatedEvent)
        });

        if (response.ok) {
            alert('Event updated successfully');
            fetchEventDetails(updatedEvent.eventId); // Refresh updated details
        } else {
            throw new Error('Failed to update event');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('An error occurred while updating the event.');
    }
}


document.getElementById('updateEventForm').addEventListener('submit', updateEvent);

// Fetch and display event schedule
async function fetchEventSchedule(eventId) {
    try {
        const response = await fetch(`http://localhost:3000/api/events/${eventId}/schedule`);
        const schedule = await response.json();
        displayEventSchedule(schedule);
    } catch (error) {
        console.error('Error fetching event schedule:', error);
    }
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
            fetchEventSchedule(eventId); // Refresh the event schedule after deletion
        } else {
            alert('Failed to delete schedule.');
        }
    } catch (error) {
        console.error('Error:', error);
    }
}

// Fetch and display event expenses
async function fetchEventExpenses(eventId) {
    try {
        const response = await fetch(`http://localhost:3000/api/events/${eventId}/expenses`);
        const expenses = await response.json();
        displayEventExpenses(expenses);
    } catch (error) {
        console.error('Error fetching event expenses:', error);
    }
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
        const response = await fetch(`http://localhost:3000/api/events/expense/${expenseId}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            alert('Expense deleted successfully!');
            fetchEventExpenses(eventId); // Refresh the event expenses after deletion
        } else {
            alert('Failed to delete expense.');
        }
    } catch (error) {
        console.error('Error:', error);
    }
}

// Add schedule
document.getElementById('scheduleForm').addEventListener('submit', function (event) {
    event.preventDefault();
    const scheduleData = {
        scheduleDate: document.getElementById('scheduleDate').value,
        startTime: document.getElementById('startTime').value,
        endTime: document.getElementById('endTime').value,
    };

    fetch(`http://localhost:3000/api/events/addSchedule/${eventId}`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(scheduleData),
    })
        .then(response => {
            if (response.ok) {
                alert('Schedule added successfully!');
                scheduleModal.style.display = 'none';
                fetchEventSchedule(eventId); // Refresh the schedule list
            } else {
                alert('Failed to add schedule.');
            }
        })
        .catch(error => console.error('Error:', error));
});

// Add expense
document.getElementById('expenseForm').addEventListener('submit', function (event) {
    event.preventDefault();
    const expenseData = {
        time: document.getElementById('expenseTime').value,
        prize: document.getElementById('expensePrize').value,
        cost: document.getElementById('expenseCost').value,
    };

    fetch(`http://localhost:3000/api/events/addExpense/${eventId}`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(expenseData),
    })
        .then(response => {
            if (response.ok) {
                alert('Expense added successfully!');
                expenseModal.style.display = 'none';
                fetchEventExpenses(eventId); // Refresh the expense list
            } else {
                alert('Failed to add expense.');
            }
        })
        .catch(error => console.error('Error:', error));
});

// Add Requirement
document.getElementById('requirementForm').addEventListener('submit', function (event) {
    event.preventDefault();
    const requirementData = {
        praktiskeKrav: document.getElementById('praktiskeKrav').value,
        tekniskeKrav: document.getElementById('tekniskeKrav').value,
        materialebehov: document.getElementById('materialebehov').value,
        gopherbehov: document.getElementById('gopherbehov').value,
    };

    fetch(`http://localhost:3000/api/events/addRequirement/${eventId}`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(requirementData),
    })
        .then(response => {
            if (response.ok) {
                alert('Requirement added successfully!');
                requirementModal.style.display = 'none';
                fetchEventRequirements(eventId); // Refresh requirements list
            } else {
                alert('Failed to add requirement.');
            }
        })
        .catch(error => console.error('Error:', error));
});

// Fetch and display event requirements
async function fetchEventRequirements(eventId) {
    try {
        const response = await fetch(`http://localhost:3000/api/events/${eventId}/requirements`);
        const requirements = await response.json();
        displayEventRequirements(requirements);
    } catch (error) {
        console.error('Error fetching event requirements:', error);
    }
}

function displayEventRequirements(requirements) {
    const requirementsList = document.getElementById('requirementsList');
    requirementsList.innerHTML = ''; // Clear existing requirements

    requirements.forEach(item => {
        const requirementItem = document.createElement('div');
        requirementItem.className = 'requirement-item';
        requirementItem.innerHTML = `
            <p><strong>Praktiske Krav:</strong> ${item.praktiskeKrav}</p>
            <p><strong>Tekniske Krav:</strong> ${item.tekniskeKrav}</p>
            <p><strong>Materialebehov:</strong> ${item.materialebehov}</p>
            <p><strong>Gopherbehov:</strong> ${item.gopherbehov}</p>
            <button class="delete-btn" onclick="deleteRequirement(${item.requirementId})">Delete</button>
        `;
        requirementsList.appendChild(requirementItem);
    });
}

async function deleteRequirement(requirementId) {
    const confirmation = confirm("Are you sure you want to delete this requirement?");
    if (!confirmation) return;

    try {
        const response = await fetch(`http://localhost:3000/api/events/requirement/${eventId}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            alert('Requirement deleted successfully!');
            fetchEventRequirements(eventId); // Refresh requirements after deletion
        } else {
            alert('Failed to delete requirement.');
        }
    } catch (error) {
        console.error('Error:', error);
    }
}

// Initialize data on page load
if (eventId) {
    fetchEventDetails(eventId);
}
