import express from 'express';
import cors from 'cors';
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

// Mock user roles for demonstration (replace with actual user role management)
const mockUsers = {
    1: { role: 'admin' },
    2: { role: 'afvikler' },
    3: { role: 'user' }
};

// Mock database for events and favorites
const eventsDb = new Map();
const favoritesDb = new Map(); // Map to store user favorites (userId -> Set of eventIds)

// Middleware to get user role (mock implementation)
app.use((req, res, next) => {
    const userId = req.query.userId || 1; // Simulate user ID from session or token
    req.userRole = mockUsers[userId]?.role || 'user'; // Default to "user" if no role found
    next();
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

// ---------- User Role API ----------
// Fetch user role
app.get('/api/user/role', (req, res) => {
    const role = req.userRole;
    res.json({ role });
});

// ---------- Event Management Routes ----------
// Fetch all events
app.get('/api/events', async (req, res) => {
    try {
        const events = Array.from(eventsDb.values());
        res.json(events);
    } catch (error) {
        res.status(500).send('Error fetching events: ' + error.message);
    }
});

// Fetch a specific event by ID
app.get('/api/events/:id', async (req, res) => {
    const eventId = req.params.id;
    try {
        if (eventsDb.has(eventId)) {
            res.json(eventsDb.get(eventId));
        } else {
            res.status(404).send('Event not found');
        }
    } catch (error) {
        res.status(500).send('Error fetching event: ' + error.message);
    }
});

// Add a new event (Admins and Afviklers only)
app.post('/api/events/add', async (req, res) => {
    if (req.userRole !== 'admin' && req.userRole !== 'afvikler') {
        return res.status(403).send('Unauthorized: Only admins and afviklers can add events.');
    }

    try {
        const eventData = req.body;
        const eventId = `event_${Date.now()}`;
        eventData.eventId = eventId;
        eventsDb.set(eventId, eventData);
        res.status(201).send('Event added successfully.');
    } catch (error) {
        res.status(500).send('Error adding event: ' + error.message);
    }
});

// Delete an event (Admins only)
app.delete('/api/events/:id', async (req, res) => {
    if (req.userRole !== 'admin') {
        return res.status(403).send('Unauthorized: Only admins can delete events.');
    }

    const eventId = req.params.id;
    try {
        if (eventsDb.has(eventId)) {
            eventsDb.delete(eventId);
            res.status(204).send('Event deleted successfully.');
        } else {
            res.status(404).send('Event not found');
        }
    } catch (error) {
        res.status(500).send('Error deleting event: ' + error.message);
    }
});

// Update an event (Admins and Afviklers only)
app.post('/api/events/:id', async (req, res) => {
    if (req.userRole !== 'admin' && req.userRole !== 'afvikler') {
        return res.status(403).send('Unauthorized: Only admins and afviklers can update events.');
    }

    const eventId = req.params.id;
    try {
        if (eventsDb.has(eventId)) {
            const eventData = req.body;
            eventsDb.set(eventId, { ...eventsDb.get(eventId), ...eventData });
            res.status(200).send('Event updated successfully.');
        } else {
            res.status(404).send('Event not found');
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
        if (eventsDb.has(eventId)) {
            events.push(eventsDb.get(eventId));
        }
    }

    res.json(events);
});

// ---------- Server Start ----------
app.listen(PORT, () => {
    console.log(`Server is running on http://localhost:${PORT}`);
});