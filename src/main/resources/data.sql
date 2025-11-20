-- Insert additional users
INSERT INTO Usuario(id, nombre, apellido, email, username, password, rol, activo, posicionFavorita, fotoPerfil) VALUES
(NULL, 'Admin', 'Admin', 'admin@unlam.edu.ar', 'admin', 'test', 'ADMIN', TRUE, 'DELANTERO', 'https://taller-web-1-416711641372-us-east-2.s3.us-east-2.amazonaws.com/ejemplos/pibardo1.jpg'),
(NULL, 'John', 'Doe', 'test@unlam.edu.ar', 'john', 'test', 'ROLE_USER', TRUE, 'DELANTERO', 'https://taller-web-1-416711641372-us-east-2.s3.us-east-2.amazonaws.com/ejemplos/pibardo1.jpg'),
(NULL, 'Jane', 'Doe', 'jane@unlam.edu.ar', 'janedoe', 'password', 'ROLE_USER', TRUE, 'DEFENSOR', 'https://taller-web-1-416711641372-us-east-2.s3.us-east-2.amazonaws.com/ejemplos/pibarda1.jpg'),
(NULL, 'Ricardo', 'Tapia', 'participante1@example.com', 'ricardo', 'password', 'ROLE_USER', TRUE, 'MEDIOCAMPISTA','https://taller-web-1-416711641372-us-east-2.s3.us-east-2.amazonaws.com/ejemplos/ricardo-tapia.png'),
(NULL, 'Bruno', 'Diaz', 'email@example.com', 'bruno', 'password', 'ROLE_USER', TRUE, 'PORTERO', 'https://taller-web-1-416711641372-us-east-2.s3.us-east-2.amazonaws.com/ejemplos/bruno.png'),
(NULL, 'Carlos', 'Garcia', 'carlos.garcia@example.com', 'carlos', 'password', 'ROLE_USER', TRUE, 'DELANTERO', 'https://taller-web-1-416711641372-us-east-2.s3.us-east-2.amazonaws.com/ejemplos/pibardo2.jpg'),
(NULL, 'Maria', 'Lopez', 'maria.lopez@example.com', 'maria', 'password', 'ROLE_USER', TRUE, 'DEFENSOR', 'https://taller-web-1-416711641372-us-east-2.s3.us-east-2.amazonaws.com/ejemplos/pibarda2.jpg'),
(NULL, 'Pedro', 'Martinez', 'pedro.martinez@example.com', 'pedro', 'password', 'ROLE_USER', TRUE, 'MEDIOCAMPISTA', 'https://taller-web-1-416711641372-us-east-2.s3.us-east-2.amazonaws.com/ejemplos/pibardo3.jpg'),
(NULL, 'Ana', 'Rodriguez', 'ana.rodriguez@example.com', 'ana', 'password', 'ROLE_USER', TRUE, 'PORTERO', 'https://taller-web-1-416711641372-us-east-2.s3.us-east-2.amazonaws.com/ejemplos/pibarda3.jpg'),
(NULL, 'Luis', 'Hernandez', 'luis.hernandez@example.com', 'luis', 'password', 'ROLE_USER', TRUE, 'DELANTERO', 'https://taller-web-1-416711641372-us-east-2.s3.us-east-2.amazonaws.com/ejemplos/pibardo4.jpg'),
(NULL, 'Sofia', 'Fernandez', 'sofia.fernandez@example.com', 'sofia', 'password', 'ROLE_USER', TRUE, 'DEFENSOR', 'https://taller-web-1-416711641372-us-east-2.s3.us-east-2.amazonaws.com/ejemplos/pibarda4.jpg');

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
(NULL, 6, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR2yuiJDyLI93stVf9dBqBRFUKLE-ZhxAo4dw&s'),
(NULL, 1, 'https://lh3.googleusercontent.com/p/AF1QipMbcsyuOvy0-4AlKYxTR7YeMH0TiMGzO_WV7mZ2=s1360-w1360-h1020-rw');

-- Insert equipos for partidos
INSERT INTO Equipo(id, nombre, creado_por_id, fechaCreacion, descripcion, insignia_url, tipo) VALUES
(NULL, 'Equipo 1', (SELECT creador_id FROM Partido WHERE id = 1), NOW(), 'Equipo formado para el primer partido. Buscamos jugadores comprometidos y con ganas de pasarla bien.', 'https://taller-web-1-416711641372-us-east-2.s3.us-east-2.amazonaws.com/insignia-default.png', 'PUBLICO'),
(NULL, 'Equipo 2', (SELECT creador_id FROM Partido WHERE id = 1), NOW(), 'Segundo equipo del partido. Unidos por la pasión del fútbol.', 'https://taller-web-1-416711641372-us-east-2.s3.us-east-2.amazonaws.com/insignia-default.png', 'PUBLICO'),
(NULL, 'Equipo 1', (SELECT creador_id FROM Partido WHERE id = 2), NOW(), 'Equipo de nivel avanzado. Jugamos limpio y con estrategia.', 'https://taller-web-1-416711641372-us-east-2.s3.us-east-2.amazonaws.com/insignia-default.png', 'PUBLICO'),
(NULL, 'Equipo 2', (SELECT creador_id FROM Partido WHERE id = 2), NOW(), 'Buscamos la victoria siempre con fair play.', 'https://taller-web-1-416711641372-us-east-2.s3.us-east-2.amazonaws.com/insignia-default.png', 'PUBLICO'),
(NULL, 'Equipo 1', (SELECT creador_id FROM Partido WHERE id = 3), NOW(), 'Equipo nocturno. Nos gusta el fútbol bajo las estrellas.', 'https://taller-web-1-416711641372-us-east-2.s3.us-east-2.amazonaws.com/insignia-default.png', 'PUBLICO'),
(NULL, 'Equipo 2', (SELECT creador_id FROM Partido WHERE id = 3), NOW(), 'Segundo equipo del partido nocturno. Juego limpio y diversión.', 'https://taller-web-1-416711641372-us-east-2.s3.us-east-2.amazonaws.com/insignia-default.png', 'PUBLICO');
-- Insert partido_equipo
INSERT INTO PartidoEquipo(id, partido_id, equipo_id, goles) VALUES
(NULL, 1, (SELECT id FROM Equipo WHERE nombre = 'Equipo 1' AND creado_por_id = (SELECT creador_id FROM Partido WHERE id = 1)), 0),
(NULL, 1, (SELECT id FROM Equipo WHERE nombre = 'Equipo 2' AND creado_por_id = (SELECT creador_id FROM Partido WHERE id = 1)), 0),
(NULL, 2, (SELECT id FROM Equipo WHERE nombre = 'Equipo 1' AND creado_por_id = (SELECT creador_id FROM Partido WHERE id = 2)), 0),
(NULL, 2, (SELECT id FROM Equipo WHERE nombre = 'Equipo 2' AND creado_por_id = (SELECT creador_id FROM Partido WHERE id = 2)), 0),
(NULL, 3, (SELECT id FROM Equipo WHERE nombre = 'Equipo 1' AND creado_por_id = (SELECT creador_id FROM Partido WHERE id = 3)), 0),
(NULL, 3, (SELECT id FROM Equipo WHERE nombre = 'Equipo 2' AND creado_por_id = (SELECT creador_id FROM Partido WHERE id = 3)), 0);
-- Insert equipo_jugador
INSERT INTO EquipoJugador(id, equipo_id, usuario_id, fecha_union, es_capitan) VALUES
(NULL, (SELECT id FROM Equipo WHERE nombre = 'Equipo 1' AND creado_por_id = (SELECT creador_id FROM Partido WHERE id = 1)), 1, NOW(), false),
(NULL, (SELECT id FROM Equipo WHERE nombre = 'Equipo 1' AND creado_por_id = (SELECT creador_id FROM Partido WHERE id = 1)), 2, NOW(), true),
(NULL, (SELECT id FROM Equipo WHERE nombre = 'Equipo 1' AND creado_por_id = (SELECT creador_id FROM Partido WHERE id = 1)), 3, NOW(), false),
(NULL, (SELECT id FROM Equipo WHERE nombre = 'Equipo 1' AND creado_por_id = (SELECT creador_id FROM Partido WHERE id = 1)), 4, NOW(), false),
(NULL, (SELECT id FROM Equipo WHERE nombre = 'Equipo 2' AND creado_por_id = (SELECT creador_id FROM Partido WHERE id = 1)), 5, NOW(), false),
(NULL, (SELECT id FROM Equipo WHERE nombre = 'Equipo 2' AND creado_por_id = (SELECT creador_id FROM Partido WHERE id = 1)), 6, NOW(), false),
(NULL, (SELECT id FROM Equipo WHERE nombre = 'Equipo 2' AND creado_por_id = (SELECT creador_id FROM Partido WHERE id = 1)), 7, NOW(), false),
(NULL, (SELECT id FROM Equipo WHERE nombre = 'Equipo 2' AND creado_por_id = (SELECT creador_id FROM Partido WHERE id = 1)), 8, NOW(), true);

-- Insertar amistades
INSERT INTO Amistad (usuario_1_id, usuario_2_id, estadoDeAmistad, fechaSolicitud) VALUES
-- Ricardo (3) y Bruno (4) son amigos
(3, 4, 'ACEPTADA', CURRENT_DATE()),
-- Carlos (5) y Maria (6) son amigos
(5, 6, 'ACEPTADA', CURRENT_DATE()),
-- Pedro (7) envió solicitud a Ana (8)
(7, 8, 'PENDIENTE', CURRENT_DATE()),
-- Luis (9) envió solicitud a Sofia (10)
(9, 10, 'PENDIENTE', CURRENT_DATE()),
-- Jane (2) y Maria (6) son amigas
(2, 6, 'ACEPTADA', CURRENT_DATE()),
-- Bruno (4) y Carlos (5) son amigos
(4, 5, 'ACEPTADA', CURRENT_DATE()),
-- Sofia (10) y Ana (8) son amigas
(10, 8, 'ACEPTADA', CURRENT_DATE()),
-- John (1) envió solicitud a Pedro (7)
(1, 7, 'PENDIENTE', CURRENT_DATE());

-- Más amistades para John (id = 1) — ahora John tiene varios amigos aceptados
INSERT INTO Amistad (usuario_1_id, usuario_2_id, estadoDeAmistad, fechaSolicitud) VALUES
	(1, 6, 'ACEPTADA', CURRENT_DATE()),
	(1, 8, 'ACEPTADA', CURRENT_DATE()),
	(1, 9, 'ACEPTADA', CURRENT_DATE()),
	(1, 10, 'ACEPTADA', CURRENT_DATE());

-- Pending requests targeted to John (id = 1) for testing accept flow
INSERT INTO Amistad (usuario_1_id, usuario_2_id, estadoDeAmistad, fechaSolicitud) VALUES
	(2, 1, 'PENDIENTE', CURRENT_DATE()),
	(3, 1, 'PENDIENTE', CURRENT_DATE()),
	(4, 1, 'PENDIENTE', CURRENT_DATE()),
	(5, 1, 'PENDIENTE', CURRENT_DATE());

-- Insertar varias solicitudes dirigidas a John (email: test@unlam.edu.ar)
INSERT INTO SolicitudUnirse (partido_id, creador_id, token, emailDestino, estado, creada, vence) VALUES
	(1, 2, 'token-john-1', 'test@unlam.edu.ar', 'PENDIENTE', NOW(), DATE_ADD(NOW(), INTERVAL 2 DAY)),
	(2, 3, 'token-john-2', 'test@unlam.edu.ar', 'PENDIENTE', NOW(), DATE_ADD(NOW(), INTERVAL 2 DAY)),
	(3, 4, 'token-john-3', 'test@unlam.edu.ar', 'PENDIENTE', NOW(), DATE_ADD(NOW(), INTERVAL 2 DAY)),
	(1, 5, 'token-john-4', 'test@unlam.edu.ar', 'PENDIENTE', NOW(), DATE_ADD(NOW(), INTERVAL 2 DAY)),
	(2, 6, 'token-john-5', 'test@unlam.edu.ar', 'PENDIENTE', NOW(), DATE_ADD(NOW(), INTERVAL 2 DAY));

INSERT INTO Torneo (id, nombre, fecha, finalizado, precio, estado, cancha_id, ganador_id, goleador_id, organizador_id) VALUES
(1, 'Torneo Primavera', '2025-11-23', 0, 5000, 'CONFIRMADO', 1, null, null, 1),
(2, 'Torneo Verano', '2025-12-10', 0, 7000, 'CONFIRMADO', 2, null, null, 2);

-- Insert equipos for torneos
INSERT INTO Equipo(id, nombre, creado_por_id, fechaCreacion, descripcion, insignia_url, tipo) VALUES
(NULL, 'Equipo Torneo A', 2, NOW(), 'Equipo privado para el Torneo Primavera. Jugadores experimentados.', 'https://taller-web-1-416711641372-us-east-2.s3.us-east-2.amazonaws.com/insignia-default.png', 'PRIVADO'),
(NULL, 'Equipo Torneo B', 3, NOW(), 'Equipo privado para el Torneo Primavera. Buscamos diversión y buen juego.', 'https://taller-web-1-416711641372-us-east-2.s3.us-east-2.amazonaws.com/insignia-default.png', 'PRIVADO'),
(NULL, 'Equipo Torneo C', 4, NOW(), 'Equipo privado para el Torneo Verano. Pasión por el fútbol.', 'https://taller-web-1-416711641372-us-east-2.s3.us-east-2.amazonaws.com/insignia-default.png', 'PRIVADO');
