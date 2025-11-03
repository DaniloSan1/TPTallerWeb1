-- Insert additional users
INSERT INTO Usuario(id, nombre, apellido, email, username, password, rol, activo, posicionFavorita, fotoPerfil) VALUES
(NULL, 'John', 'Doe', 'test@unlam.edu.ar', 'admin', 'test', 'ADMIN', TRUE, 'DELANTERO', NULL),
(NULL, 'Jane', 'Doe', 'jane@unlam.edu.ar', 'janedoe', 'password', 'ROLE_USER', TRUE, 'DEFENSOR', NULL),
(NULL, 'Ricardo', 'Tapia', 'participante1@example.com', 'ricardo', 'password', 'ROLE_USER', TRUE, 'MEDIOCAMPISTA', NULL),
(NULL, 'Bruno', 'Diaz', 'email@example.com', 'bruno', 'password', 'ROLE_USER', TRUE, 'PORTERO', NULL),
(NULL, 'Carlos', 'Garcia', 'carlos.garcia@example.com', 'carlos', 'password', 'ROLE_USER', TRUE, 'DELANTERO', NULL),
(NULL, 'Maria', 'Lopez', 'maria.lopez@example.com', 'maria', 'password', 'ROLE_USER', TRUE, 'DEFENSOR', NULL),
(NULL, 'Pedro', 'Martinez', 'pedro.martinez@example.com', 'pedro', 'password', 'ROLE_USER', TRUE, 'MEDIOCAMPISTA', NULL),
(NULL, 'Ana', 'Rodriguez', 'ana.rodriguez@example.com', 'ana', 'password', 'ROLE_USER', TRUE, 'PORTERO', NULL),
(NULL, 'Luis', 'Hernandez', 'luis.hernandez@example.com', 'luis', 'password', 'ROLE_USER', TRUE, 'DELANTERO', NULL),
(NULL, 'Sofia', 'Fernandez', 'sofia.fernandez@example.com', 'sofia', 'password', 'ROLE_USER', TRUE, 'DEFENSOR', NULL);

-- Insert fields
INSERT INTO Cancha(id, nombre, capacidad, direccion, zona, precio, tipoSuelo) VALUES
(NULL, 'Mega Futbol San Justo', 20, '2875 Rincon, BII, B1754 San Justo, Provincia de Buenos Aires', 'OESTE', 5000.00, 'CÉSPED'),
(NULL, 'Leloir Football Point', 10, 'Martín Fierro 3585, B1715 BRO, Provincia de Buenos Aires, Argentina', 'OESTE', 8000.00, 'CÉSPED'),
(NULL, 'Fútbol 5 Acrópolis', 10, 'Buenos Aires AR, Av. General Enrique Mosconi 639, B1752 CXG, Cdad. Autónoma de Buenos Aires', 'OESTE', 9000.00, 'CÉSPED'),
(NULL, 'Olivos Fútbol', 20, 'B1636GYA, Antonio Malaver 401-499, B1636GYA Vicente López, Provincia de Buenos Aires', 'NORTE', 10000.00, 'CÉSPED'),
(NULL, 'El Puente - Fútbol Club', 10, 'Cnel. Niceto Vega 5432, C1414BFB Cdad. Autónoma de Buenos Aires', 'CENTRO', 8500.00, 'CÉSPED'),
(NULL, 'La Fabrica Futbol',15 , 'Av. Avellaneda 1154, B1876 Bernal Oeste, Provincia de Buenos Aires', 'SUR', 7000.00, 'CÉSPED');

-- Insert schedules
INSERT INTO Horario(id, cancha_id, diaSemana, horaInicio, horaFin, disponible) VALUES
(NULL, 1, 'MONDAY', '10:00:00', '11:00:00', TRUE),
(NULL, 1, 'MONDAY', '11:00:00', '12:00:00', TRUE),
(NULL, 1, 'MONDAY', '12:00:00', '13:00:00', TRUE),
(NULL, 1, 'MONDAY', '13:00:00', '14:00:00', TRUE),
(NULL, 1, 'MONDAY', '14:00:00', '15:00:00', TRUE),
(NULL, 1, 'MONDAY', '15:00:00', '16:00:00', FALSE),
(NULL, 1, 'MONDAY', '16:00:00', '17:00:00', TRUE),
(NULL, 1, 'MONDAY', '17:00:00', '18:00:00', TRUE),
(NULL, 1, 'MONDAY', '18:00:00', '19:00:00', TRUE),
(NULL, 1, 'MONDAY', '19:00:00', '20:00:00', TRUE),
(NULL, 1, 'TUESDAY', '10:00:00', '11:00:00', TRUE),
(NULL, 1, 'TUESDAY', '11:00:00', '12:00:00', TRUE),
(NULL, 1, 'TUESDAY', '12:00:00', '13:00:00', TRUE),
(NULL, 1, 'TUESDAY', '13:00:00', '14:00:00', TRUE),
(NULL, 1, 'TUESDAY', '14:00:00', '15:00:00', TRUE),
(NULL, 1, 'TUESDAY', '15:00:00', '16:00:00', TRUE),
(NULL, 1, 'TUESDAY', '16:00:00', '17:00:00', TRUE),
(NULL, 1, 'TUESDAY', '17:00:00', '18:00:00', TRUE),
(NULL, 1, 'TUESDAY', '18:00:00', '19:00:00', TRUE),
(NULL, 1, 'TUESDAY', '19:00:00', '20:00:00', TRUE),
(NULL, 1, 'WEDNESDAY', '10:00:00', '11:00:00', TRUE),
(NULL, 1, 'WEDNESDAY', '11:00:00', '12:00:00', TRUE),
(NULL, 1, 'WEDNESDAY', '12:00:00', '13:00:00', TRUE),
(NULL, 1, 'WEDNESDAY', '13:00:00', '14:00:00', TRUE),
(NULL, 1, 'WEDNESDAY', '14:00:00', '15:00:00', TRUE),
(NULL, 1, 'WEDNESDAY', '15:00:00', '16:00:00', TRUE),
(NULL, 1, 'WEDNESDAY', '16:00:00', '17:00:00', TRUE),
(NULL, 1, 'WEDNESDAY', '17:00:00', '18:00:00', TRUE),
(NULL, 1, 'WEDNESDAY', '18:00:00', '19:00:00', TRUE),
(NULL, 1, 'WEDNESDAY', '19:00:00', '20:00:00', TRUE),
(NULL, 1, 'THURSDAY', '10:00:00', '11:00:00', TRUE),
(NULL, 1, 'THURSDAY', '11:00:00', '12:00:00', TRUE),
(NULL, 1, 'THURSDAY', '12:00:00', '13:00:00', TRUE),
(NULL, 1, 'THURSDAY', '13:00:00', '14:00:00', TRUE),
(NULL, 1, 'THURSDAY', '14:00:00', '15:00:00', TRUE),
(NULL, 1, 'THURSDAY', '15:00:00', '16:00:00', TRUE),
(NULL, 1, 'THURSDAY', '16:00:00', '17:00:00', TRUE),
(NULL, 1, 'THURSDAY', '17:00:00', '18:00:00', TRUE),
(NULL, 1, 'THURSDAY', '18:00:00', '19:00:00', TRUE),
(NULL, 1, 'THURSDAY', '19:00:00', '20:00:00', TRUE),
(NULL, 1, 'FRIDAY', '10:00:00', '11:00:00', TRUE),
(NULL, 1, 'FRIDAY', '11:00:00', '12:00:00', TRUE),
(NULL, 1, 'FRIDAY', '12:00:00', '13:00:00', TRUE),
(NULL, 1, 'FRIDAY', '13:00:00', '14:00:00', TRUE),
(NULL, 1, 'FRIDAY', '14:00:00', '15:00:00', TRUE),
(NULL, 1, 'FRIDAY', '15:00:00', '16:00:00', TRUE),
(NULL, 1, 'FRIDAY', '16:00:00', '17:00:00', TRUE),
(NULL, 1, 'FRIDAY', '17:00:00', '18:00:00', TRUE),
(NULL, 1, 'FRIDAY', '18:00:00', '19:00:00', TRUE),
(NULL, 1, 'FRIDAY', '19:00:00', '20:00:00', TRUE),
(NULL, 1, 'SATURDAY', '10:00:00', '11:00:00', TRUE),
(NULL, 1, 'SATURDAY', '11:00:00', '12:00:00', TRUE),
(NULL, 1, 'SATURDAY', '12:00:00', '13:00:00', TRUE),
(NULL, 1, 'SATURDAY', '13:00:00', '14:00:00', TRUE),
(NULL, 1, 'SATURDAY', '14:00:00', '15:00:00', TRUE),
(NULL, 1, 'SATURDAY', '15:00:00', '16:00:00', TRUE),
(NULL, 1, 'SATURDAY', '16:00:00', '17:00:00', TRUE),
(NULL, 1, 'SATURDAY', '17:00:00', '18:00:00', TRUE),
(NULL, 1, 'SATURDAY', '18:00:00', '19:00:00', TRUE),
(NULL, 1, 'SATURDAY', '19:00:00', '20:00:00', TRUE);

INSERT INTO Horario (cancha_id, diaSemana, horaInicio, horaFin, disponible)
SELECT 2, diaSemana, horaInicio, horaFin, disponible FROM Horario WHERE cancha_id = 1;

INSERT INTO Horario (cancha_id, diaSemana, horaInicio, horaFin, disponible)
SELECT 3, diaSemana, horaInicio, horaFin, disponible FROM Horario WHERE cancha_id = 1;

INSERT INTO Horario (cancha_id, diaSemana, horaInicio, horaFin, disponible)
SELECT 4, diaSemana, horaInicio, horaFin, disponible FROM Horario WHERE cancha_id = 1;

INSERT INTO Horario (cancha_id, diaSemana, horaInicio, horaFin, disponible)
SELECT 5, diaSemana, horaInicio, horaFin, disponible FROM Horario WHERE cancha_id = 1;

INSERT INTO Horario (cancha_id, diaSemana, horaInicio, horaFin, disponible)
SELECT 6, diaSemana, horaInicio, horaFin, disponible FROM Horario WHERE cancha_id = 1;


-- Insert reservations
INSERT INTO Reserva(id, horario_id, usuario_id, fechaReserva, fechaCreacion, activa) VALUES
(NULL, 1, 2, '2024-11-07 14:00:00', NOW(), TRUE),
(NULL, 180, 2, '2024-11-08 14:00:00', NOW(), TRUE),
(NULL, 80, 1, '2024-11-07 14:00:00', NOW(), TRUE),
(NULL, 30, 1, '2024-11-08 15:00:00', NOW(), TRUE),
(NULL, 40, 1, '2024-11-09 16:00:00', NOW(), TRUE),
(NULL, 50, 1, '2024-11-10 17:00:00', NOW(), TRUE),
(NULL, 60, 1, '2024-11-11 18:00:00', NOW(), TRUE);

-- Insert matches
INSERT INTO Partido(id, titulo, descripcion, nivel, cupoMaximo, reserva_id, creador_id) VALUES
(NULL, 'Partido para principiantes', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.', 'PRINCIPIANTE', 12, 1, 1),
(NULL, 'Partido mixto', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.', 'AVANZADO', 10, 2, 3),
(NULL, 'Partido nocturno', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.', 'INTERMEDIO', 6, 3, 4);

INSERT INTO FotoCancha(id, cancha_id, url) VALUES
(NULL, 1, 'https://pbs.twimg.com/profile_images/945747881037791233/8OidypRn_400x400.jpg'),
(NULL, 2, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSprbaunUh7Oijgn4i5YrVSWGJ5p2pW_sPOxQ&s'),
(NULL, 3, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSybeYob_2z8_yCnmRE0OMH8u-20u9H8_8bGQ&s'),
(NULL, 4, 'https://lh3.googleusercontent.com/gps-cs-s/AG0ilSwZY0Jy7PTpxLmlPZQLMqjoGpx410gz8-rH28stBAofst9VRNT6uvOn72dqsx5B5eo_BHVB7WxZOwictFv2ygTIotXGkgIGdNFq4Zw2NHUf9YC658l-b98ATYwIbC_c2LID5B9vNg=s1360-w1360-h1020-rw'),
(NULL, 5, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRDNR9_AfwO6kKtHZWJsvQiZhl2hIkpdAPNxg&s'),
(NULL, 6, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR2yuiJDyLI93stVf9dBqBRFUKLE-ZhxAo4dw&s');

-- Insert equipos for partidos
INSERT INTO Equipo(id, nombre, creado_por_id, fechaCreacion) VALUES
(NULL, 'EQUIPO_1', (SELECT creador_id FROM Partido WHERE id = 1), NOW()),
(NULL, 'EQUIPO_2', (SELECT creador_id FROM Partido WHERE id = 1), NOW()),
(NULL, 'SIN_EQUIPO', (SELECT creador_id FROM Partido WHERE id = 1), NOW());

-- Insert partido_equipo
INSERT INTO PartidoEquipo(id, partido_id, equipo_id, points) VALUES
(NULL, 1, (SELECT id FROM Equipo WHERE nombre = 'EQUIPO_1' AND creado_por_id = (SELECT creador_id FROM Partido WHERE id = 1)), 0),
(NULL, 1, (SELECT id FROM Equipo WHERE nombre = 'EQUIPO_2' AND creado_por_id = (SELECT creador_id FROM Partido WHERE id = 1)), 0),
(NULL, 1, (SELECT id FROM Equipo WHERE nombre = 'SIN_EQUIPO' AND creado_por_id = (SELECT creador_id FROM Partido WHERE id = 1)), 0);

-- Insert equipo_jugador
INSERT INTO EquipoJugador(id, equipo_id, usuario_id, fecha_union) VALUES
(NULL, (SELECT id FROM Equipo WHERE nombre = 'EQUIPO_1' AND creado_por_id = (SELECT creador_id FROM Partido WHERE id = 1)), 1, NOW()),
(NULL, (SELECT id FROM Equipo WHERE nombre = 'EQUIPO_1' AND creado_por_id = (SELECT creador_id FROM Partido WHERE id = 1)), 2, NOW()),
(NULL, (SELECT id FROM Equipo WHERE nombre = 'EQUIPO_1' AND creado_por_id = (SELECT creador_id FROM Partido WHERE id = 1)), 3, NOW()),
(NULL, (SELECT id FROM Equipo WHERE nombre = 'EQUIPO_1' AND creado_por_id = (SELECT creador_id FROM Partido WHERE id = 1)), 4, NOW()),
(NULL, (SELECT id FROM Equipo WHERE nombre = 'EQUIPO_2' AND creado_por_id = (SELECT creador_id FROM Partido WHERE id = 1)), 5, NOW()),
(NULL, (SELECT id FROM Equipo WHERE nombre = 'EQUIPO_2' AND creado_por_id = (SELECT creador_id FROM Partido WHERE id = 1)), 6, NOW()),
(NULL, (SELECT id FROM Equipo WHERE nombre = 'EQUIPO_2' AND creado_por_id = (SELECT creador_id FROM Partido WHERE id = 1)), 7, NOW()),
(NULL, (SELECT id FROM Equipo WHERE nombre = 'EQUIPO_2' AND creado_por_id = (SELECT creador_id FROM Partido WHERE id = 1)), 8, NOW()),
(NULL, (SELECT id FROM Equipo WHERE nombre = 'SIN_EQUIPO' AND creado_por_id = (SELECT creador_id FROM Partido WHERE id = 1)), 9, NOW()),
(NULL, (SELECT id FROM Equipo WHERE nombre = 'SIN_EQUIPO' AND creado_por_id = (SELECT creador_id FROM Partido WHERE id = 1)), 10, NOW());

