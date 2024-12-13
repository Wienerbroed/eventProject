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
    document.getElementById('venueId').value = event.venueId; // Set the venueId

    // Handle warnings
    const warnings = event.warnings ? event.warnings.split(',') : [];
    warnings.forEach(w => {
        const normalized = w.trim().toLowerCase();
        if (normalized === 'wheelchair') {
            document.getElementById('wheelchair').checked = true;
        } else if (normalized === 'strobelights') {
            document.getElementById('strobelights').checked = true;
        } else if (normalized === 'loud noises') {
            document.getElementById('loudNoises').checked = true;
        }
    });
}

async function updateEvent(event) {
    event.preventDefault();
    const eventId = document.getElementById('eventId').value;

    const titleElement = document.getElementById('title');
    const eventCreatorElement = document.getElementById('eventCreator');
    const eventResponsibleElement = document.getElementById('eventResponsible');
    const eventControlElement = document.getElementById('eventControl');
    const eventTypeElement = document.getElementById('eventType');
    const descriptionElement = document.getElementById('description');
    const maxParticipantsElement = document.getElementById('maxParticipants');
    const maxAudienceElement = document.getElementById('maxAudience');
    const conguideDkElement = document.getElementById('conguideDk');
    const conguideEnElement = document.getElementById('conguideEn');
    const venueIdElement = document.getElementById('venueId');

    if (!titleElement || !eventCreatorElement || !eventResponsibleElement || !eventControlElement ||
        !eventTypeElement || !descriptionElement || !maxParticipantsElement || !maxAudienceElement ||
        !conguideDkElement || !conguideEnElement || !venueIdElement) {
        console.error('One or more elements are missing in the DOM');
        return;
    }

    // Gather warnings
    const selectedWarnings = Array.from(document.querySelectorAll('input[name="warnings"]:checked'))
        .map(checkbox => checkbox.value)
        .join(',');

    const updatedEvent = {
        title: titleElement.value,
        eventCreator: eventCreatorElement.value,
        eventResponsible: eventResponsibleElement.value,
        eventControl: eventControlElement.value,
        eventType: eventTypeElement.value,
        description: descriptionElement.value,
        maxParticipants: maxParticipantsElement.value,
        maxAudience: maxAudienceElement.value,
        conguideDk: conguideDkElement.value,
        conguideEn: conguideEnElement.value,
        venue: {
            venueId: venueIdElement.value,
        },
        warnings: selectedWarnings // Include updated warnings
    };

    try {
        const response = await fetch(`http://localhost:3000/api/events/${eventId}`, {
            method: 'PUT',
            headers: {'Content-Type': 'application/json'},
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
