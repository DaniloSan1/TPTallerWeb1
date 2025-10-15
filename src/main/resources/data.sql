
-- Keep the original admin user
INSERT INTO Usuario(id, email, password, rol, activo) VALUES(null, 'test@unlam.edu.ar', 'test', 'ADMIN', true);
-- Insert Usuario (equivalent to: Usuario creador = new Usuario("usuario1", "password", "email@example.com"))
INSERT INTO Usuario(id, nombre, email, password, rol, activo) VALUES(null, 'credor1', 'credor1@example.com', 'password', 'ROLE_USER', true);
INSERT INTO Usuario(id, nombre, email, password, rol, activo) VALUES(null, 'participante1', 'participante1@example.com', 'password', 'ROLE_USER', true);
-- Insert Cancha (equivalent to: Cancha cancha = new Cancha("Cancha 1", null, null, "Direccion 1", Zona.NORTE))
INSERT INTO Cancha(id, nombre, capacidad, direccion, zona) VALUES(null, 'Cancha 1', null, 'Direccion 1', 'NORTE'),
(null, 'Cancha 2', null, 'Direccion 2', 'SUR'),
(null, 'Cancha 3', null, 'Direccion 3', 'ESTE');

-- Insert Horario (equivalent to: Horario horario = new Horario(cancha, DayOfWeek.MONDAY, LocalTime.now(), LocalTime.now().plusHours(1)))
INSERT INTO Horario(id, cancha_id, diaSemana, horaInicio, horaFin, disponible) VALUES(null, 1, 'MONDAY', '14:00:00', '15:00:00', true);
INSERT INTO Horario(id, cancha_id, diaSemana, horaInicio, horaFin, disponible) VALUES(null, 3, 'TUESDAY', '14:00:00', '15:00:00', true);

INSERT INTO Reserva(id, horario_id, usuario_id, fechaReserva) VALUES(null, 1, 2, '2024-10-07 14:00:00');
INSERT INTO Reserva(id, horario_id, usuario_id, fechaReserva) VALUES(null, 2, 2, '2024-10-08 14:00:00');

-- Insert Partido (equivalent to: Partido nuevoPartido = new Partido("Partido de prueba", "Descripción del partido", Zona.NORTE, Nivel.INTERMEDIO, LocalDateTime.now(), 10, horario, creador))
INSERT INTO Partido(id, titulo, descripcion, zona, nivel, cupoMaximo, reserva_id, creador_id) VALUES(null, 'Partido de prueba 1', 'Descripción del partido 1', 'NORTE', 'INTERMEDIO', 10, 1, 2);
INSERT INTO Partido(id, titulo, descripcion, zona, nivel, cupoMaximo, reserva_id, creador_id) VALUES(null, 'Partido de prueba 2', 'Descripción del partido 2', 'NORTE', 'INTERMEDIO', 1, 2, 2);

INSERT INTO PartidoParticipante(id, partido_id, usuario_id) VALUES(null, 2, 3);

-- Insert Usuario (equivalent to: Usuario creador = new Usuario("usuario1", "password", "email@example.com"))
INSERT INTO Usuario(id, nombre, email, password, rol, activo) VALUES(null, 'usuario1', 'email@example.com', 'password', 'ROLE_USER', true);

-- Insert Cancha (equivalent to: Cancha cancha = new Cancha("Cancha 1", null, null, "Direccion 1", Zona.NORTE))
INSERT INTO Cancha(id, nombre, capacidad, tipoSuelo, zona) VALUES
(NULL, 'Cancha 1', NULL, 'Direccion 1', 'NORTE'),
(NULL, 'Cancha 2', NULL, 'Direccion 2', 'SUR'),
(NULL, 'Cancha 3', NULL, 'Direccion 3', 'ESTE'),
(NULL, 'Cancha 4', NULL, 'Direccion 4', 'OESTE'),
(NULL, 'Cancha 5', NULL, 'Direccion 5', 'CENTRO');

-- Insert Horario (equivalent to: Horario horario = new Horario(cancha, DayOfWeek.MONDAY, LocalTime.now(), LocalTime.now().plusHours(1)))
INSERT INTO Horario(id, cancha_id, diaSemana, horaInicio, horaFin, disponible) VALUES
(NULL, 1, 'MONDAY',    '14:00:00', '15:00:00', true),
(NULL, 2, 'TUESDAY',   '15:00:00', '16:00:00', true),
(NULL, 3, 'WEDNESDAY', '16:00:00', '17:00:00', true),
(NULL, 4, 'THURSDAY',  '17:00:00', '18:00:00', true),
(NULL, 5, 'FRIDAY',    '18:00:00', '19:00:00', true);

INSERT INTO Reserva(id, horario_id, usuario_id, fechaReserva) VALUES
(NULL, 1, 1, '2024-10-07 14:00:00'),
(NULL, 2, 1, '2024-10-08 15:00:00'),
(NULL, 3, 1, '2024-10-09 16:00:00'),
(NULL, 4, 1, '2024-10-10 17:00:00'),
(NULL, 5, 1, '2024-10-11 18:00:00');


-- Insert Partido (equivalent to: Partido nuevoPartido = new Partido("Partido de prueba", "Descripción del partido", Zona.NORTE, Nivel.INTERMEDIO, LocalDateTime.now(), 10, horario, creador))
INSERT INTO Partido(id, titulo, descripcion, zona, nivel, cupoMaximo, reserva_id, creador_id) VALUES
(NULL, 'Partido de prueba', 'Descripción del partido', 'NORTE', 'INTERMEDIO', 10,
       (SELECT r.id FROM Reserva r WHERE r.horario_id = 1 AND r.usuario_id = 1 ORDER BY r.id DESC LIMIT 1), 1),
(NULL, 'Partido avanzado', 'Descripción del partido avanzado', 'SUR', 'AVANZADO', 8,
       (SELECT r.id FROM Reserva r WHERE r.horario_id = 2 AND r.usuario_id = 1 ORDER BY r.id DESC LIMIT 1), 1),
(NULL, 'Partido para principiantes', 'Descripción del partido para principiantes', 'ESTE', 'PRINCIPIANTE', 12,
       (SELECT r.id FROM Reserva r WHERE r.horario_id = 3 AND r.usuario_id = 1 ORDER BY r.id DESC LIMIT 1), 1),
(NULL, 'Partido mixto', 'Descripción del partido 4', 'OESTE', 'AVANZADO', 10,
       (SELECT r.id FROM Reserva r WHERE r.horario_id = 4 AND r.usuario_id = 1 ORDER BY r.id DESC LIMIT 1), 1),
(NULL, 'Partido nocturno', 'Descripción del partido nocturno', 'CENTRO', 'INTERMEDIO', 6,
       (SELECT r.id FROM Reserva r WHERE r.horario_id = 5 AND r.usuario_id = 1 ORDER BY r.id DESC LIMIT 1), 1);