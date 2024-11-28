import express from 'express';
import cors from 'cors';
import fetch from 'node-fetch';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const app = express();
const PORT = process.env.PORT || 3000;

// Middleware setup
app.use(cors());
app.use(express.json());
app.use(express.static(path.join(__dirname, '..', 'public'))); // Serve static files

// Mock database for demonstration (replace with actual database logic)
const favoritesDb = new Map(); // Map to store user favorites (userId -> Set of eventIds)

// Start the server
app.listen(PORT, () => {
    console.log(`Server is running on http://localhost:${PORT}`);
});

// ---------- Static Pages ----------
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

// Serve `mypage.html` at the `/mypage` route
app.get('/mypage', (req, res) => {
    res.sendFile(path.join(__dirname, '..', 'public', 'mypage.html'));
});

// ---------- Event Management Routes ----------
// Fetch all events
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
        res.status(500).send('Error fetching event: ' + error.message);
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
            res.status(postResponse.status).send('Failed to add event: ' + postResponse.statusText);
        }
    } catch (error) {
        res.status(500).send('Error adding event: ' + error.message);
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

// Update an event
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

// ---------- Favorite Management Routes ----------
// Check if an event is favorited
app.get('/api/events/:eventId/isFavorited', (req, res) => {
    const { eventId } = req.params;
    const { userId } = req.query;

    if (!favoritesDb.has(userId)) {
        return res.json(false);
    }

    const userFavorites = favoritesDb.get(userId);
    res.json(userFavorites.has(eventId));
});

// Favorite an event
app.post('/api/events/:eventId/favorite', (req, res) => {
    const { eventId } = req.params;
    const { userId } = req.body;

    if (!favoritesDb.has(userId)) {
        favoritesDb.set(userId, new Set());
    }

    const userFavorites = favoritesDb.get(userId);
    userFavorites.add(eventId);

    res.status(200).send('Event favorited successfully.');
});

// Unfavorite an event
app.delete('/api/events/:eventId/favorite', (req, res) => {
    const { eventId } = req.params;
    const { userId } = req.body;

    if (!favoritesDb.has(userId)) {
        return res.status(404).send('No favorites found for this user.');
    }

    const userFavorites = favoritesDb.get(userId);
    if (!userFavorites.has(eventId)) {
        return res.status(404).send('Event is not in favorites.');
    }

    userFavorites.delete(eventId);
    res.status(200).send('Event unfavorited successfully.');
});

// Fetch all favorited events for a user
app.get('/api/events/mypage/favorites', async (req, res) => {
    const { userId } = req.query;

    if (!favoritesDb.has(userId)) {
        return res.json([]);
    }

    const userFavorites = favoritesDb.get(userId);
    const events = [];

    for (const eventId of userFavorites) {
        try {
            const response = await fetch(`http://localhost:8080/api/events/${eventId}`);
            if (response.ok) {
                const event = await response.json();
                events.push(event);
            }
        } catch (error) {
            console.error(`Error fetching event ${eventId}:`, error.message);
        }
    }

    res.json(events);
});