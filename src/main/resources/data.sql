-- Fixed SQL syntax issues while keeping the full Lorem Ipsum text intact

-- Insert additional users
INSERT INTO Usuario(id, nombre, apellido, email, username, password, rol, activo, posicionFavorita) VALUES
(NULL, 'John', 'Doe', 'test@unlam.edu.ar', 'john', 'test', 'ADMIN', TRUE, 'DELANTERO'),
(NULL, 'Jane', 'Doe', 'jane.doe@example.com', 'jane', 'password', 'ROLE_USER', TRUE, 'DEFENSOR'),
(NULL, 'Ricardo', 'Tapia', 'participante1@example.com', 'ricardo', 'password', 'ROLE_USER', TRUE, 'MEDIOCAMPISTA'),
(NULL, 'Bruno', 'Diaz', 'email@example.com', 'bruno', 'password', 'ROLE_USER', TRUE, 'PORTERO'),
(NULL, 'Carlos', 'Garcia', 'carlos.garcia@example.com', 'carlos', 'password', 'ROLE_USER', TRUE, 'DELANTERO'),
(NULL, 'Maria', 'Lopez', 'maria.lopez@example.com', 'maria', 'password', 'ROLE_USER', TRUE, 'DEFENSOR'),
(NULL, 'Pedro', 'Martinez', 'pedro.martinez@example.com', 'pedro', 'password', 'ROLE_USER', TRUE, 'MEDIOCAMPISTA'),
(NULL, 'Ana', 'Rodriguez', 'ana.rodriguez@example.com', 'ana', 'password', 'ROLE_USER', TRUE, 'PORTERO'),
(NULL, 'Luis', 'Hernandez', 'luis.hernandez@example.com', 'luis', 'password', 'ROLE_USER', TRUE, 'DELANTERO'),
(NULL, 'Sofia', 'Fernandez', 'sofia.fernandez@example.com', 'sofia', 'password', 'ROLE_USER', TRUE, 'DEFENSOR');

-- Insert fields
INSERT INTO Cancha(id, nombre, capacidad, direccion, zona, precio, tipoSuelo) VALUES
(NULL, 'Cancha 1', 20, 'Direccion 1', 'NORTE', 5000.00, 'CÉSPED'),
(NULL, 'Leloir Football Point', 10, 'Martín Fierro 3585, B1715 BRO, Provincia de Buenos Aires, Argentina', 'OESTE', 8000.00, 'CÉSPED'),
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
(NULL, 'Partido para principiantes', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.', 'PRINCIPIANTE', 12, (SELECT r.id FROM Reserva r WHERE r.horario_id = 3 AND r.usuario_id = 1 ORDER BY r.id DESC LIMIT 1), 1),
(NULL, 'Partido mixto', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.', 'AVANZADO', 10, (SELECT r.id FROM Reserva r WHERE r.horario_id = 4 AND r.usuario_id = 1 ORDER BY r.id DESC LIMIT 1), 3),
(NULL, 'Partido nocturno', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.', 'INTERMEDIO', 6, (SELECT r.id FROM Reserva r WHERE r.horario_id = 5 AND r.usuario_id = 1 ORDER BY r.id DESC LIMIT 1), 4);

-- Insert participants for the first partido
INSERT INTO PartidoParticipante(partido_id, usuario_id, fecha_union, equipo) VALUES
(1, 1, NOW(), "EQUIPO_1"),
(1, 2, NOW(), "EQUIPO_1"),
(1, 3, NOW(), "EQUIPO_1"),
(1, 4, NOW(), "EQUIPO_1"),
(1, 5, NOW(), "EQUIPO_2"),
(1, 6, NOW(), "EQUIPO_2"),
(1, 7, NOW(), "EQUIPO_2"),
(1, 8, NOW(), "EQUIPO_2"),
(1, 9, NOW(), "SIN_EQUIPO"),
(1, 10, NOW(), "SIN_EQUIPO");

