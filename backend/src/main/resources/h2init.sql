-- Drop dependent tables first
DROP TABLE IF EXISTS Events;
DROP TABLE IF EXISTS EventRoom;

-- Venue Table
DROP TABLE IF EXISTS venue;
CREATE TABLE IF NOT EXISTS venue (
                                     venue_id INT AUTO_INCREMENT PRIMARY KEY,
                                     venue_name VARCHAR(50) NOT NULL,
    venue_address VARCHAR(50) NOT NULL
    );

-- Insert data into Venue Table
INSERT INTO venue (venue_id, venue_name, venue_address) VALUES
                                                            (1, 'Conference Center', '123 Main St'),
                                                            (2, 'Expo Hall', '456 Elm St'),
                                                            (3, 'Art Gallery', '789 Oak St'),
                                                            (4, 'Innovation Hub', '101 Pine St'),
                                                            (5, 'Community Center', '202 Maple St');

-- Event Room Table
CREATE TABLE IF NOT EXISTS EventRoom (
                                         event_room_id INT AUTO_INCREMENT PRIMARY KEY,
                                         event_room_name VARCHAR(50) NOT NULL,
    event_room_capacity INT NOT NULL,
    venue_id INT DEFAULT NULL,
    FOREIGN KEY (venue_id) REFERENCES venue(venue_id) ON DELETE SET NULL
    );

-- Insert data into EventRoom Table
INSERT INTO EventRoom (event_room_id, event_room_name, event_room_capacity, venue_id) VALUES
                                                                                          (1, 'Room A', 20, 1),
                                                                                          (2, 'Room B', 30, 2),
                                                                                          (3, 'Room C', 40, 3),
                                                                                          (4, 'Room D', 50, 4),
                                                                                          (5, 'Room E', 60, 5);

-- Events Table
CREATE TABLE IF NOT EXISTS Events (
                                      event_id INT AUTO_INCREMENT PRIMARY KEY,
                                      title VARCHAR(255) NOT NULL,
    event_creator VARCHAR(255) NOT NULL,
    event_responsible VARCHAR(255) NOT NULL,
    event_control VARCHAR(255) NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    max_participants INT NOT NULL,
    max_audience INT NOT NULL,
    conguide_dk TEXT NOT NULL,
    conguide_en TEXT NOT NULL,
    venue_id INT DEFAULT NULL,
    warnings VARCHAR(255),
    event_room_id INT DEFAULT NULL,
    FOREIGN KEY (event_room_id) REFERENCES EventRoom(event_room_id) ON DELETE SET NULL,
    FOREIGN KEY (venue_id) REFERENCES venue(venue_id) ON DELETE SET NULL
    );

-- Insert data into Events Table
INSERT INTO Events (event_id, title, event_creator, event_responsible, event_control, event_type, description,
                    max_participants, max_audience, conguide_dk, conguide_en, venue_id, warnings, event_room_id)
VALUES
    (1, 'Annual Tech Conference', 'Alice Smith', 'Bob Johnson', 'Charlie Lee', 'Conference',
     'A day-long tech event with keynote speeches and workshops.', 500, 1000,
     'Teknisk program DK', 'Technical program EN', 1, 'loud noises', 1);