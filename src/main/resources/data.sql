
-- Keep the original admin user
INSERT INTO Usuario(id, email, password, rol, activo) VALUES(null, 'test@unlam.edu.ar', 'test', 'ADMIN', true);
-- Insert Usuario (equivalent to: Usuario creador = new Usuario("usuario1", "password", "email@example.com"))
INSERT INTO Usuario(id, nombre, email, password, rol, activo) VALUES(null, 'credor1', 'credor1@example.com', 'password', 'ROLE_USER', true);
INSERT INTO Usuario(id, nombre, email, password, rol, activo) VALUES(null, 'participante1', 'participante1@example.com', 'password', 'ROLE_USER', true);

<<<<<<< HEAD
=======

>>>>>>> origin/develop

-- Insert Usuario (equivalent to: Usuario creador = new Usuario("usuario1", "password", "email@example.com"))
INSERT INTO Usuario(id, nombre, email, password, rol, activo) VALUES(null, 'usuario1', 'email@example.com', 'password', 'ROLE_USER', true);

-- Insert Cancha (equivalent to: Cancha cancha = new Cancha("Cancha 1", null, null, "Direccion 1", Zona.NORTE))
INSERT INTO Cancha(id, nombre, capacidad, direccion,zona,precio,tipoSuelo) VALUES
(NULL, 'Cancha 1', 20, 'Direccion 1', 'NORTE', 5000.00, 'CÉSPED'),
(NULL, 'Cancha 2', 10, 'Direccion 2', 'SUR', 8000.00, 'CÉSPED'),
(NULL, 'Cancha 3', 14, 'Direccion 3', 'ESTE', 9000.00, 'CÉSPED'),
(NULL, 'Cancha 4', 20, 'Direccion 4', 'OESTE', 1000.00, 'CÉSPED'),
(NULL, 'Cancha 5', 10, 'Direccion 5', 'CENTRO', 8500.00, 'CÉSPED');

-- Insert Horario (equivalent to: Horario horario = new Horario(cancha, DayOfWeek.MONDAY, LocalTime.now(), LocalTime.now().plusHours(1)))
INSERT INTO Horario(id, cancha_id, diaSemana, horaInicio, horaFin, disponible) VALUES
(NULL, 1, 'MONDAY',    '14:00:00', '15:00:00', true),
(NULL, 1, 'MONDAY',    '15:00:00', '16:00:00', false),
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
INSERT INTO Partido(id, titulo, descripcion, nivel, cupoMaximo, reserva_id, creador_id) VALUES
(NULL, 'Partido de prueba', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.', 'INTERMEDIO', 10,
       (SELECT r.id FROM Reserva r WHERE r.horario_id = 1 AND r.usuario_id = 1 ORDER BY r.id DESC LIMIT 1), 2),
(NULL, 'Partido avanzado', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.', 'AVANZADO', 8,
       (SELECT r.id FROM Reserva r WHERE r.horario_id = 2 AND r.usuario_id = 1 ORDER BY r.id DESC LIMIT 1), 2),
(NULL, 'Partido para principiantes', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.', 'PRINCIPIANTE', 12,
       (SELECT r.id FROM Reserva r WHERE r.horario_id = 3 AND r.usuario_id = 1 ORDER BY r.id DESC LIMIT 1), 2),
(NULL, 'Partido mixto', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.', 'AVANZADO', 10,
       (SELECT r.id FROM Reserva r WHERE r.horario_id = 4 AND r.usuario_id = 1 ORDER BY r.id DESC LIMIT 1), 2),
(NULL, 'Partido nocturno', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.', 'INTERMEDIO', 6,
       (SELECT r.id FROM Reserva r WHERE r.horario_id = 5 AND r.usuario_id = 1 ORDER BY r.id DESC LIMIT 1), 2);