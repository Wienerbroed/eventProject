import express from 'express';
import cors from 'cors';
import fetch from 'node-fetch';
import path from 'path';
import { fileURLToPath } from 'url';
import cookieParser from 'cookie-parser';
import jwt from 'jsonwebtoken';

// Helper to get __dirname since it's not available in ES modules
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const app = express();
const PORT = process.env.PORT || 3000;
const SECRET_KEY = 'BabskiOgJagger'; // Replace with your actual secret key
const expiresIn = '1h'; // Token expiration time

// Start the server
app.listen(PORT, () => {
    console.log(`Server is running on http://localhost:${PORT}`);
});

// Middleware setup
app.use(cors());
app.use(express.json());
app.use(express.static(path.join(__dirname, '..', 'public'))); // Serve static files from the 'public' folder
app.use(cookieParser());


// Middleware to check authentication
const authenticateToken = (req, res, next) => {
    const token = req.cookies.token || req.headers['authorization'];

    if (!token) {
        res.redirect('/loginAndRegisterPage');
        return res.status(401).send('Access Denied');
    }

    try {
        const verified = jwt.verify(token, SECRET_KEY);
        req.user = verified;
        next();
    } catch (err) {
        res.status(400).send('Invalid Token');
    }
};


// ---------- Event Routes ----------


// Serve `event.html` at the `/events` route
app.get('/events', authenticateToken, (req, res) => {
    res.sendFile(path.join(__dirname, '..', 'public', 'event.html'));
});

// Serve `addEvent.html` at the `/events/add` route
app.get('/events/add', authenticateToken, (req, res) => {
    res.sendFile(path.join(__dirname, '..', 'public', 'addEvent.html'));
});

// Serve `seeEvent.html` at the `/events/:id` route
app.get('/events/:id', authenticateToken, (req, res) => {
    res.sendFile(path.join(__dirname, '..', 'public', 'seeEvent.html'));
});


//------------------------EVENT---------------------------

app.get('/addEventSchedule', (req, res) => {
    const eventId = req.query.eventId;  // Access the eventId from the query string
    console.log('Event ID:', eventId);  // You can use this ID to fetch event details or perform other actions
    res.sendFile(path.join(__dirname, '..', 'public', 'addEventSchedule.html'));
});



// Fetch events from the backend API
app.get('/api/events', async (req, res) => {
    try {
        const response = await fetch('http://localhost:8080/api/events');
        const events = await response.json();
        res.json(events);
    } catch (error) {
        res.status(500).send('Error fetching events: ' + error.message);
    }
});

// Fetch events from the backend API
app.get('/api/events', async (req, res) => {
    try {
        const response = await fetch('http://localhost:8080/api/events');
        const events = await response.json();
        res.json(events);
    } catch (error) {
        res.status(500).send('Error fetching events: ' + error.message);
    }
});



// Fetch a specific event by ID
app.get('/api/events/:id', async (req, res) => {
    const eventId = req.params.id;
    try {
        const response = await fetch(`http://localhost:8080/api/events/${eventId}`);
        const event = await response.json();
        res.json(event);
    } catch (error) {
        res.status(500).send('Error fetching activity: ' + error.message);
    }
});
// Add a new event
app.post('/api/events/add', async (req, res) => {
    try {
        const eventData = req.body;
        const postResponse = await fetch('http://localhost:8080/api/events/add', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(eventData)
        });

        if (postResponse.ok) {
            res.status(201).send('Event added successfully.');
        } else {
            res.status(postResponse.status).send('Failed to add Event: ' + postResponse.statusText);
        }
    } catch (error) {
        res.status(500).send('Error adding activity: ' + error.message);
    }
});
// Delete an event
app.delete('/api/events/:id', async (req, res) => {
    try {
        const eventId = req.params.id;
        const deleteResponse = await fetch(`http://localhost:8080/api/events/${eventId}`, { method: 'DELETE' });

        if (deleteResponse.ok) {
            res.status(204).send('Event deleted successfully.');
        } else {
            res.status(deleteResponse.status).send('Failed to delete event: ' + deleteResponse.statusText);
        }
    } catch (error) {
        res.status(500).send('Error deleting event: ' + error.message);
    }
});
// Delete a schedule
app.delete('/api/events/schedule/:scheduleId', async (req, res) => {
    try {
        const scheduleId = req.params.scheduleId;
        const deleteResponse = await fetch(`http://localhost:8080/api/events/schedule/${scheduleId}`, { method: 'DELETE' });

        if (deleteResponse.ok) {
            res.status(204).send('Schedule deleted successfully.');
        } else {
            res.status(deleteResponse.status).send('Failed to delete schedule: ' + deleteResponse.statusText);
        }
    } catch (error) {
        res.status(500).send('Error deleting schedule: ' + error.message);
    }
});

// Update an existing event
app.post('/api/events/:id', async (req, res) => {
    try {
        const eventId = req.params.id;
        const eventData = req.body;

        const postResponse = await fetch(`http://localhost:8080/api/events/${eventId}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(eventData)
        });

        if (postResponse.ok) {
            res.status(200).send('Event updated successfully.');
        } else {
            res.status(postResponse.status).send('Failed to update event: ' + postResponse.statusText);
        }
    } catch (error) {
        res.status(500).send('Error updating event: ' + error.message);
    }
});
// Fetch a specific event by ID
app.get('/api/events/:id', async (req, res) => {
    const eventId = req.params.id;
    try {
        const response = await fetch(`http://localhost:8080/api/events/${eventId}`);
        const event = await response.json();
        res.json(event);
    } catch (error) {
        res.status(500).send('Error fetching event: ' + error.message);
    }
});
// Fetch a specific event schedule by ID
app.get('/api/events/:eventId/schedule', async (req, res) => {
    const eventId = req.params.eventId;
    try {
        const response = await fetch(`http://localhost:8080/api/events/${eventId}/schedule`);
        const eventSchedule = await response.json();
        res.json(eventSchedule);
    } catch (error) {
        res.status(500).send('Error fetching event schedule: ' + error.message);
    }
});

// Fetch event schedules from the backend API
app.get('/api/events/schedules', async (req, res) => {
    try {
        const response = await fetch('http://localhost:8080/api/events/schedules');
        const events = await response.json();
        res.json(events);
    } catch (error) {
        res.status(500).send('Error fetching event schedules: ' + error.message);
    }
});

app.post('/api/events/addSchedule/:eventId', async (req, res) => {
    try {
        const eventId = req.params.eventId; // Get the eventId from the URL
        const scheduleData = req.body; // Get the schedule data from the request body

        const postResponse = await fetch(`http://localhost:8080/api/events/addSchedule/${eventId}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(scheduleData),
        });

        if (postResponse.ok) {
            res.status(201).send('Schedule added successfully.');
        } else {
            const errorText = await postResponse.text();
            res.status(postResponse.status).send('Failed to add schedule: ' + errorText);
        }
    } catch (error) {
        res.status(500).send('Error adding schedule: ' + error.message);
    }
});





// Fetch events by venue ID
app.get('/api/events/venue/:venueId', async (req, res) => {
    const venueId = req.params.venueId;

    try {
        const response = await fetch(`http://localhost:8080/api/events/venue/${venueId}`);

        if (!response.ok) {
            throw new Error(`Failed to fetch events for venue ${venueId}: ${response.statusText}`);
        }

        const events = await response.json();

        if (!Array.isArray(events)) {
            throw new Error('Received invalid data format. Expected an array of events.');
        }

        res.json(events); // Return the events in the response
    } catch (error) {
        console.error('Error fetching events by venue:', error.message);
        res.status(500).send('Error fetching events by venue: ' + error.message); // Return a server error response
    }
});


app.put('/api/events/:id', async (req, res) => {
    try {
        const eventId = req.params.id;
        const eventData = req.body;

        const putResponse = await fetch(`http://localhost:8080/api/events/${eventId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(eventData)
        });

        if (putResponse.ok) {
            res.status(200).send('Event updated successfully.');
        } else {
            const errorText = await putResponse.text();
            res.status(putResponse.status).send('Failed to update event: ' + errorText);
        }
    } catch (error) {
        res.status(500).send('Error updating event: ' + error.message);
    }
});





// Update an event schedule
app.put('/api/events/schedule/:scheduleId', async (req, res) => {
    const scheduleId = req.params.scheduleId;
    const updatedSchedule = req.body;

    try {
        const response = await fetch(`http://localhost:8080/api/events/schedule/${scheduleId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(updatedSchedule)
        });

        if (response.ok) {
            res.status(200).send('Event updated successfully.');
        } else {
            const errorText = await response.text();
            res.status(response.status).send('Error updating event: ' + errorText);
        }
    } catch (error) {
        res.status(500).send('Error updating event: ' + error.message);
    }
});





// Add a new event expense
app.post('/api/events/addExpense/:eventId', async (req, res) => {
    try {
        const eventId = req.params.eventId; // Get the eventId from the URL
        const expenseData = req.body; // Get the expense data from the request body

        const postResponse = await fetch(`http://localhost:8080/api/events/addExpense/${eventId}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(expenseData),
        });

        if (postResponse.ok) {
            res.status(201).send('Expense added successfully.');
        } else {
            const errorText = await postResponse.text();
            res.status(postResponse.status).send('Failed to add expense: ' + errorText);
        }
    } catch (error) {
        res.status(500).send('Error adding expense: ' + error.message);
    }
});

// Fetch expenses for a specific event by ID
app.get('/api/events/:eventId/expenses', async (req, res) => {
    const eventId = req.params.eventId; // Get the eventId from the URL
    try {
        const response = await fetch(`http://localhost:8080/api/events/${eventId}/expenses`);
        const expenses = await response.json();
        res.json(expenses);
    } catch (error) {
        res.status(500).send('Error fetching expenses: ' + error.message);
    }
});

// Delete an expense
app.delete('/api/events/expense/:expenseId', async (req, res) => {
    try {
        const expenseId = req.params.expenseId;
        const deleteResponse = await fetch(`http://localhost:8080/api/events/expense/${expenseId}`, { method: 'DELETE' });

        if (deleteResponse.ok) {
            res.status(204).send('Expense deleted successfully.');
        } else {
            res.status(deleteResponse.status).send('Failed to delete expense: ' + deleteResponse.statusText);
        }
    } catch (error) {
        res.status(500).send('Error deleting expense: ' + error.message);
    }
});


// Add a new event requirement
app.post('/api/events/addRequirement/:eventId', async (req, res) => {
    try {
        const eventId = req.params.eventId; // Get the eventId from the URL
        const requirementData = req.body; // Get the requirement data from the request body

        const postResponse = await fetch(`http://localhost:8080/api/events/addRequirement/${eventId}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(requirementData),
        });

        if (postResponse.ok) {
            res.status(201).send('Requirement added successfully.');
        } else {
            const errorText = await postResponse.text();
            res.status(postResponse.status).send('Failed to add requirement: ' + errorText);
        }
    } catch (error) {
        res.status(500).send('Error adding requirement: ' + error.message);
    }
});

// Fetch requirements for a specific event by ID
app.get('/api/events/:eventId/requirements', async (req, res) => {
    const eventId = req.params.eventId; // Get the eventId from the URL
    try {
        const response = await fetch(`http://localhost:8080/api/events/${eventId}/requirements`);
        const requirements = await response.json();
        res.json(requirements);
    } catch (error) {
        res.status(500).send('Error fetching requirements: ' + error.message);
    }
});







//------------------------login and registre---------------------------


// Serve `loginAndRegisterPage.html` at `/loginAndRegisterPage` route
app.get('/login', (req, res) => {
    res.sendFile(path.join(__dirname, '..', 'public', 'loginAndRegisterPage.html'));
});

// Handle user registration
app.post('/api/register', async (req, res) => {
    try {
        const { username, password, email } = req.body;

        // Forward the data to the Spring Boot API
        const response = await fetch('http://localhost:8080/api/loginAndRegister/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password, email })
        });

        if (response.ok) {
            res.status(201).send('User registered successfully.');
        } else {
            const errorText = await response.text();
            res.status(response.status).send('Failed to register user: ' + errorText);
        }
    } catch (error) {
        console.error('Error registering user:', error);
        res.status(500).send('Error registering user: ' + error.message);
    }
});

// Handle user login
app.post('/api/login', async (req, res) => {
    try {
        const loginData = req.body;
        const response = await fetch('http://localhost:8080/api/loginAndRegister/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(loginData)
        });

        if (response.ok) {
            const token = jwt.sign({ username: loginData.username }, SECRET_KEY, { expiresIn: '1h' });
            res.cookie('token', token, { httpOnly: true });
            res.status(200).send('Login successful.');
        } else {
            const errorText = await response.text();
            res.status(401).send('Invalid username or password: ' + errorText);
        }
    } catch (error) {
        res.status(500).send('Error logging in: ' + error.message);
    }
});

//Handle USer logout
app.get('/api/logout', (req, res) => {
    res.clearCookie('token');
    res.status(200).send('User logged out successfully.');
});



// ---------- Venue Routes ----------

// Serve `venues.html` at the `/venues` route
app.get('/venues', authenticateToken, (req, res) => {
    res.sendFile(path.join(__dirname, '..', 'public', 'venues.html'));
});


app.get('/venues/:id', authenticateToken, (req, res) => {
    res.sendFile(path.join(__dirname, '..', 'public', 'venues.html'));
});



// --------------------- Venue  --------------

// Fetch events from the backend API
app.get('/api/venues', async (req, res) => {
    try {
        const response = await fetch('http://localhost:8080/api/venues');
        const venues = await response.json();
        res.json(venues);
    } catch (error) {
        res.status(500).send('Error fetching venues: ' + error.message);
    }
});

// Fetch a specific event by ID'
app.get('/api/venues/:id', async (req, res) => {
    const venueId = req.params.id;
    try {
        const response = await fetch(`http://localhost:8080/api/venues/${venueId}`);
        const venue = await response.json();
        res.json(venue);
    } catch (error) {
        res.status(500).send('Error fetching venue: ' + error.message);
    }
});

// Add a new venue
app.post('/api/venues/add', async (req, res) => {
    try {
        const venueData = req.body;
        const postResponse = await fetch('http://localhost:8080/api/venues/add', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(venueData)
        });

        if (postResponse.ok) {
            const jsonResponse = await postResponse.json(); // Ensure the response is parsed as JSON
            res.status(201).json({ message: 'Venue added successfully', data: jsonResponse });
        } else {
            const errorText = await postResponse.text();
            res.status(postResponse.status).json({ error: 'Failed to add venue', message: errorText });
        }
    } catch (error) {
        res.status(500).json({ error: 'Error adding venue', message: error.message });
    }
});

// Delete an venue
app.delete('/api/venues/:id', async (req, res) => {
    try {
        const venueId = req.params.id;
        const deleteResponse = await fetch(`http://localhost:8080/api/venues/${venueId}`, { method: 'DELETE' });

        if (deleteResponse.ok) {
            res.status(204).send('Venue deleted successfully.');
        } else {
            res.status(deleteResponse.status).send('Failed to delete venue: ' + deleteResponse.statusText);
        }
    } catch (error) {
        res.status(500).send('Error deleting venue: ' + error.message);
    }
});



// calender route
app.get('/eventCalender', (req, res) => {
    res.sendFile(path.join(__dirname, '..', 'public', 'eventCalender.html'));
});

// calender -----------------




