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