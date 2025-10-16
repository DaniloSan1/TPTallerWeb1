-- Fixed SQL syntax issues while keeping the full Lorem Ipsum text intact

-- Keep the original admin user
INSERT INTO Usuario(id, email, password, rol, activo) VALUES(NULL, 'test@unlam.edu.ar', 'test', 'ADMIN', TRUE);

-- Insert additional users
INSERT INTO Usuario(id, nombre, email, password, rol, activo) VALUES
(NULL, 'credor1', 'credor1@example.com', 'password', 'ROLE_USER', TRUE),
(NULL, 'participante1', 'participante1@example.com', 'password', 'ROLE_USER', TRUE),
(NULL, 'usuario1', 'email@example.com', 'password', 'ROLE_USER', TRUE);

-- Insert fields
INSERT INTO Cancha(id, nombre, capacidad, direccion, zona, precio, tipoSuelo) VALUES
(NULL, 'Cancha 1', 20, 'Direccion 1', 'NORTE', 5000.00, 'CÉSPED'),
(NULL, 'Cancha 2', 10, 'Direccion 2', 'SUR', 8000.00, 'CÉSPED'),
(NULL, 'Cancha 3', 14, 'Direccion 3', 'ESTE', 9000.00, 'CÉSPED'),
(NULL, 'Cancha 4', 20, 'Direccion 4', 'OESTE', 1000.00, 'CÉSPED'),
(NULL, 'Cancha 5', 10, 'Direccion 5', 'CENTRO', 8500.00, 'CÉSPED');

-- Insert schedules
INSERT INTO Horario(id, cancha_id, diaSemana, horaInicio, horaFin, disponible) VALUES
(NULL, 1, 'MONDAY', '14:00:00', '15:00:00', TRUE),
(NULL, 1, 'MONDAY', '15:00:00', '16:00:00', FALSE),
(NULL, 2, 'TUESDAY', '15:00:00', '16:00:00', TRUE),
(NULL, 3, 'WEDNESDAY', '16:00:00', '17:00:00', TRUE),
(NULL, 4, 'THURSDAY', '17:00:00', '18:00:00', TRUE),
(NULL, 5, 'FRIDAY', '18:00:00', '19:00:00', TRUE);

-- Insert reservations
INSERT INTO Reserva(id, horario_id, usuario_id, fechaReserva, fechaCreacion, activa) VALUES
(NULL, 1, 2, '2024-11-07 14:00:00', NOW(), TRUE),
(NULL, 2, 2, '2024-11-08 14:00:00', NOW(), TRUE),
(NULL, 1, 1, '2024-11-07 14:00:00', NOW(), TRUE),
(NULL, 2, 1, '2024-11-08 15:00:00', NOW(), TRUE),
(NULL, 3, 1, '2024-11-09 16:00:00', NOW(), TRUE),
(NULL, 4, 1, '2024-11-10 17:00:00', NOW(), TRUE),
(NULL, 5, 1, '2024-11-11 18:00:00', NOW(), TRUE);

-- Insert matches
INSERT INTO Partido(id, titulo, descripcion, nivel, cupoMaximo, reserva_id, creador_id) VALUES
(NULL, 'Partido para principiantes', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.', 'PRINCIPIANTE', 12, (SELECT r.id FROM Reserva r WHERE r.horario_id = 3 AND r.usuario_id = 1 ORDER BY r.id DESC LIMIT 1), 2),
(NULL, 'Partido mixto', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.', 'AVANZADO', 10, (SELECT r.id FROM Reserva r WHERE r.horario_id = 4 AND r.usuario_id = 1 ORDER BY r.id DESC LIMIT 1), 2),
(NULL, 'Partido nocturno', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.', 'INTERMEDIO', 6, (SELECT r.id FROM Reserva r WHERE r.horario_id = 5 AND r.usuario_id = 1 ORDER BY r.id DESC LIMIT 1), 2);

