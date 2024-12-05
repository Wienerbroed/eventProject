import express from 'express';
import cors from 'cors';
import fetch from 'node-fetch';
import path from 'path';
import { fileURLToPath } from 'url';

// Helper to get __dirname since it's not available in ES modules
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const app = express();
const PORT = process.env.PORT || 3000;

// Start the server
app.listen(PORT, () => {
    console.log(`Server is running on http://localhost:${PORT}`);
});

// Middleware setup
app.use(cors());
app.use(express.json());
app.use(express.static(path.join(__dirname, '..', 'public'))); // Serve static files from the 'public' folder

// ---------- Event Routes ----------


// Serve `event.html` at the `/events` route
app.get('/events', (req, res) => {
    res.sendFile(path.join(__dirname, '..', 'public', 'event.html'));
});

// Serve `addEvent.html` at the `/events/add` route
app.get('/events/add', (req, res) => {
    res.sendFile(path.join(__dirname, '..', 'public', 'addEvent.html'));
});

// Serve `seeEvent.html` at the `/events/:id` route
app.get('/events/:id', (req, res) => {
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
        res.status(500).send('Error fetching activity: ' + error.message);
    }
});
app.get('/api/events/venue/:venueId', async (req, res) => {
    const venueId = req.params.venueId; // Get the venueId from the URL parameter

    try {
        const response = await fetch(`http://localhost:8080/api/events/venue/${venueId}`);
        const events = await response.json();
        if (!events) {
            return res.status(404).json({ message: "No events found for this venue" });
        }
        res.json(events);
    } catch (error) {
        console.error("Error fetching events by venue:", error);
        res.status(500).json({ message: "Internal server error" });
    }
});


//------------------------login and registre---------------------------


// Serve `loginAndRegisterPage.html` at `/loginAndRegisterPage` route
app.get('/loginAndRegisterPage', (req, res) => {
    res.sendFile(path.join(__dirname, '..', 'public', 'loginAndRegisterPage.html'));
});

// Handle user registration
app.post('/api/register', async (req, res) => {
    try {
        const { username, password, email } = req.body;

        // Forward the data to the Spring Boot API
        const response = await fetch('http://localhost:8080/register', {
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
        const response = await fetch('http://localhost:8080/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(loginData)
        });

        if (response.ok) {
            res.status(200).send('Login successful.');
        } else {
            const errorText = await response.text();
            res.status(401).send('Invalid username or password: ' + errorText);
        }
    } catch (error) {
        res.status(500).send('Error logging in: ' + error.message);
    }
});





// ---------- Venue Routes ----------

// Serve `venues.html` at the `/venues` route
app.get('/venues', (req, res) => {
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








