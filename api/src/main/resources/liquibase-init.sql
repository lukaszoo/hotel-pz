CREATE SEQUENCE ID_SEQUENCE START WITH 1 INCREMENT BY 1;

CREATE TABLE userRole(
    id INTEGER NOT NULL,
    name VARCHAR(256) NOT NULL,
    description VARCHAR(256),
    CONSTRAINT PK_USER_ROLE PRIMARY KEY(id)
);

CREATE TABLE user(
    id INTEGER NOT NULL,
    username VARCHAR(256) NOT NULL,
    password VARCHAR(256),
    CONSTRAINT PK_USER PRIMARY KEY(id)
);

CREATE TABLE userRoleAssignment(
  userId INTEGER NOT NULL,
  roleId INTEGER NOT NULL,

  CONSTRAINT PK_FK_ASSIGNMENT PRIMARY KEY(userId, roleId),
  FOREIGN KEY (userId) REFERENCES user(id),
  FOREIGN KEY (roleId) REFERENCES userRole(id)
);

CREATE TABLE feature(
    id INTEGER NOT NULL,
    name VARCHAR(256) NOT NULL,
    description VARCHAR(256),
    CONSTRAINT PK_FEATURE PRIMARY KEY(id)
);

CREATE TABLE apartment(
  id INTEGER NOT NULL,
  floor INTEGER NOT NULL,
  capacity INTEGER NOT NULL,
  cost FLOAT NOT NULL,

  CONSTRAINT PK_APARTMENT PRIMARY KEY(id)
);

CREATE TABLE equipment(
  apartmentId INTEGER NOT NULL,
  featureId INTEGER NOT NULL,

  CONSTRAINT PK_FK_EQUIPMENT PRIMARY KEY(apartmentId, featureId),
  FOREIGN KEY (apartmentId) REFERENCES apartment(id),
  FOREIGN KEY (featureId) REFERENCES feature(id)
);

CREATE TABLE client(
  id INTEGER NOT NULL,
  email VARCHAR(256) NOT NULL,
  name VARCHAR(256),
  surname VARCHAR(256) NOT NULL,
  address VARCHAR(256),
  postalCode VARCHAR(256),
  city VARCHAR(256),

  CONSTRAINT PK_CLIENT PRIMARY KEY(id)
);

CREATE TABLE booking(
  id INTEGER NOT NULL,
  apartmentId INTEGER NOT NULL,
  clientId INTEGER NOT NULL,
  startDate DATE NOT NULL,
  endDate DATE NOT NULL,
  cost FLOAT,
  paid BOOL DEFAULT FALSE,

  CONSTRAINT PK_FK_APARTMENT PRIMARY KEY(id),
  FOREIGN KEY (apartmentId) REFERENCES apartment(id),
  FOREIGN KEY (clientId) REFERENCES client(id)
);

-- create default users
INSERT INTO userRole(id, name, description) VALUES (1,'ADMIN','System administrator');
INSERT INTO user(id, username, password) VALUES (1,'root','5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8');
INSERT INTO userRoleAssignment(userId, roleId) VALUES (1,1);

INSERT INTO userRole(id, name, description) VALUES (2,'USER','System user');
INSERT INTO user(id, username, password) VALUES (2,'user1','5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8');
INSERT INTO userRoleAssignment(userId, roleId) VALUES (2,2);
-- apartment's features
INSERT INTO feature(id, name) VALUES (1, 'TV');
INSERT INTO feature(id, name) VALUES (2, 'Jacuzzi');
INSERT INTO feature(id, name) VALUES (3, 'Balcony');
INSERT INTO feature(id, name) VALUES (4, 'WiFi');
INSERT INTO feature(id, name) VALUES (5, 'Separate bathroom');
-- 1st floor apartments
INSERT INTO apartment(id, floor, capacity, cost) VALUES(1,1,1,100.00);
INSERT INTO equipment(apartmentId, featureId) VALUES (1,1);
INSERT INTO equipment(apartmentId, featureId) VALUES (1,4);
INSERT INTO equipment(apartmentId, featureId) VALUES (1,5);
INSERT INTO apartment(id, floor, capacity, cost) VALUES(2,1,1,100.00);
INSERT INTO equipment(apartmentId, featureId) VALUES (2,1);
INSERT INTO equipment(apartmentId, featureId) VALUES (2,2);
INSERT INTO equipment(apartmentId, featureId) VALUES (2,4);
INSERT INTO equipment(apartmentId, featureId) VALUES (2,5);
INSERT INTO apartment(id, floor, capacity, cost) VALUES(3,1,2,180.00);
INSERT INTO equipment(apartmentId, featureId) VALUES (3,1);
INSERT INTO equipment(apartmentId, featureId) VALUES (3,2);
INSERT INTO equipment(apartmentId, featureId) VALUES (3,4);
INSERT INTO equipment(apartmentId, featureId) VALUES (3,5);
INSERT INTO apartment(id, floor, capacity, cost) VALUES(4,1,2,170.00);
INSERT INTO equipment(apartmentId, featureId) VALUES (4,1);
INSERT INTO equipment(apartmentId, featureId) VALUES (4,2);
INSERT INTO equipment(apartmentId, featureId) VALUES (4,4);
INSERT INTO equipment(apartmentId, featureId) VALUES (4,5);
-- 2nd floor apartments
INSERT INTO apartment(id, floor, capacity, cost) VALUES(5,2,1,90.00);
INSERT INTO equipment(apartmentId, featureId) VALUES (5,1);
INSERT INTO equipment(apartmentId, featureId) VALUES (5,3);
INSERT INTO equipment(apartmentId, featureId) VALUES (5,4);
INSERT INTO apartment(id, floor, capacity, cost) VALUES(6,2,1,90.00);
INSERT INTO equipment(apartmentId, featureId) VALUES (6,1);
INSERT INTO equipment(apartmentId, featureId) VALUES (6,3);
INSERT INTO equipment(apartmentId, featureId) VALUES (6,4);
INSERT INTO equipment(apartmentId, featureId) VALUES (6,5);
INSERT INTO apartment(id, floor, capacity, cost) VALUES(7,2,3,270.00);
INSERT INTO equipment(apartmentId, featureId) VALUES (7,1);
INSERT INTO equipment(apartmentId, featureId) VALUES (7,2);
INSERT INTO equipment(apartmentId, featureId) VALUES (7,3);
INSERT INTO equipment(apartmentId, featureId) VALUES (7,4);
INSERT INTO equipment(apartmentId, featureId) VALUES (7,5);
INSERT INTO apartment(id, floor, capacity, cost) VALUES(8,2,3,270.00);
INSERT INTO equipment(apartmentId, featureId) VALUES (8,1);
INSERT INTO equipment(apartmentId, featureId) VALUES (8,2);
INSERT INTO equipment(apartmentId, featureId) VALUES (8,3);
INSERT INTO equipment(apartmentId, featureId) VALUES (8,4);
INSERT INTO equipment(apartmentId, featureId) VALUES (8,5);
-- 3rd floor apartments
INSERT INTO apartment(id, floor, capacity, cost) VALUES(9,3,2,170.00);
INSERT INTO equipment(apartmentId, featureId) VALUES (9,1);
INSERT INTO equipment(apartmentId, featureId) VALUES (9,2);
INSERT INTO equipment(apartmentId, featureId) VALUES (9,3);
INSERT INTO equipment(apartmentId, featureId) VALUES (9,4);
INSERT INTO apartment(id, floor, capacity, cost) VALUES(10,3,3,260.00);
INSERT INTO equipment(apartmentId, featureId) VALUES (10,1);
INSERT INTO equipment(apartmentId, featureId) VALUES (10,2);
INSERT INTO equipment(apartmentId, featureId) VALUES (10,3);
INSERT INTO equipment(apartmentId, featureId) VALUES (10,4);
-- example clients
INSERT INTO client(id, email, name, surname, address, postalCode, city)
  VALUES (1, 'john.smith@example.com','John','Smith','546 Newcastle Ave.','IL 60110','Carpentersville');
INSERT INTO client(id, email, name, surname, address, postalCode, city)
  VALUES (2, 'jan.kowalski@example.com','Jan','Kowalski','8971 Market St.','OR 97124','Hillsboro');
-- example booking
INSERT INTO booking(id, clientId, apartmentId, startDate, endDate, cost, paid )
  VALUES(1, 1, 1,TO_DATE('31-12-2018','dd-MM-yyyy'),TO_DATE('01-01-2019','dd-MM-yyyy'), 200.00, TRUE);
