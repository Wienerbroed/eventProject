CREATE DATABASE IF NOT EXISTS eventDatabase;
USE eventDatabase;

-- Venue Table
CREATE TABLE IF NOT EXISTS venue (
                                     venue_id INT AUTO_INCREMENT PRIMARY KEY,
                                     venue_name VARCHAR(50) NOT NULL,
                                     venue_address VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS EventRoom (
                                         event_room_id INT AUTO_INCREMENT PRIMARY KEY, -- Consistent naming
                                         event_room_name VARCHAR(50) NOT NULL,
                                         event_room_capacity INT NOT NULL,
                                         venue_id INT DEFAULT NULL,
                                         FOREIGN KEY (venue_id) REFERENCES venue(venue_id) ON DELETE SET NULL
);

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
                                      event_room_id INT DEFAULT NULL, -- Nullable for ON DELETE SET NULL
                                      FOREIGN KEY (event_room_id) REFERENCES EventRoom(event_room_id) ON DELETE SET NULL
);

-- Event Expenses Table
CREATE TABLE IF NOT EXISTS EventExpenses (
                                             expense_id INT AUTO_INCREMENT PRIMARY KEY,
                                             event_id INT NOT NULL,
                                             time TEXT,
                                             prize TEXT,
                                             cost TEXT,
                                             FOREIGN KEY (event_id) REFERENCES Events(event_id)
);

-- Event Requirements Table
CREATE TABLE IF NOT EXISTS EventRequirements (
                                                 event_id INT PRIMARY KEY,
                                                 praktiske_krav TEXT,
                                                 tekniske_krav TEXT,
                                                 materialebehov TEXT,
                                                 gopherbehov TEXT,
                                                 FOREIGN KEY (event_id) REFERENCES Events(event_id) ON DELETE CASCADE
);

-- Event Schedule Table
CREATE TABLE IF NOT EXISTS EventSchedule (
                                             schedule_id INT AUTO_INCREMENT PRIMARY KEY,
                                             event_id INT NOT NULL,
                                             schedule_date DATE,
                                             start_time TIME,
                                             end_time TIME,
                                             FOREIGN KEY (event_id) REFERENCES Events(event_id)
);

-- Login Table
CREATE TABLE IF NOT EXISTS login (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     username VARCHAR(30) NOT NULL UNIQUE,
                                     password VARCHAR(30) NOT NULL,
                                     email VARCHAR(50) NOT NULL UNIQUE,
                                     role VARCHAR(20) NOT NULL DEFAULT 'USER'
);
