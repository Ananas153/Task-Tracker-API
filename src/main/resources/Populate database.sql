INSERT INTO users (name, email, password)
VALUES
('Alice Johnson',  'alice@mail.com',    'password123'),
('Bob Smith',      'bob@mail.com',      'password123'),
('Charlie Brown',  'charlie@mail.com',  'helloWorld'),
('Diana Prince',   'diana@mail.com',    'qwerty123'),
('Ethan Clark',    'ethan@mail.com',    'letmein');

INSERT INTO tasks (idUser, description, status)
VALUES
(1, 'Buy groceries for dinner', 'TODO'),
(1, 'Clean the apartment', 'IN_PROGRESS'),
(1, 'Pay electricity bill', 'DONE'),

(2, 'Prepare for presentation', 'TODO'),
(2, 'Email project report to manager', 'IN_PROGRESS'),

(3, 'Workout at the gym', 'TODO'),
(3, 'Read 30 pages of a book', 'DONE'),

(4, 'Make doctor appointment', 'TODO'),
(4, 'Finish frontend UI task', 'IN_PROGRESS'),

(5, 'Plan weekend trip', 'TODO'),
(5, 'Organize workstation', 'DONE');