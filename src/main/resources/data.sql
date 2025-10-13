
-- Keep the original admin user
INSERT INTO Usuario(id, email, password, rol, activo) VALUES(null, 'test@unlam.edu.ar', 'test', 'ADMIN', true);
-- Insert Usuario (equivalent to: Usuario creador = new Usuario("usuario1", "password", "email@example.com"))
INSERT INTO Usuario(id, nombre, email, password, rol, activo) VALUES(null, 'credor1', 'credor1@example.com', 'password', 'ROLE_USER', true);
INSERT INTO Usuario(id, nombre, email, password, rol, activo) VALUES(null, 'participante1', 'participante1@example.com', 'password', 'ROLE_USER', true);
-- Insert Cancha (equivalent to: Cancha cancha = new Cancha("Cancha 1", null, null, "Direccion 1", Zona.NORTE))
INSERT INTO Cancha(id, nombre, capacidad, tipoSuelo, zona) VALUES(null, 'Cancha 1', null, 'Direccion 1', 'NORTE');

-- Insert Horario (equivalent to: Horario horario = new Horario(cancha, DayOfWeek.MONDAY, LocalTime.now(), LocalTime.now().plusHours(1)))
INSERT INTO Horario(id, cancha_id, diaSemana, horaInicio, horaFin, disponible) VALUES(null, 1, 'MONDAY', '14:00:00', '15:00:00', true);
INSERT INTO Horario(id, cancha_id, diaSemana, horaInicio, horaFin, disponible) VALUES(null, 1, 'TUESDAY', '14:00:00', '15:00:00', true);

INSERT INTO Reserva(id, horario_id, usuario_id, fechaReserva) VALUES(null, 1, 2, '2024-10-07 14:00:00');
INSERT INTO Reserva(id, horario_id, usuario_id, fechaReserva) VALUES(null, 2, 2, '2024-10-08 14:00:00');

-- Insert Partido (equivalent to: Partido nuevoPartido = new Partido("Partido de prueba", "Descripción del partido", Zona.NORTE, Nivel.INTERMEDIO, LocalDateTime.now(), 10, horario, creador))
INSERT INTO Partido(id, titulo, descripcion, zona, nivel, cupoMaximo, reserva_id, creador_id) VALUES(null, 'Partido de prueba 1', 'Descripción del partido 1', 'NORTE', 'INTERMEDIO', 10, 1, 2);
INSERT INTO Partido(id, titulo, descripcion, zona, nivel, cupoMaximo, reserva_id, creador_id) VALUES(null, 'Partido de prueba 2', 'Descripción del partido 2', 'NORTE', 'INTERMEDIO', 1, 2, 2);

INSERT INTO PartidoParticipante(id, partido_id, usuario_id) VALUES(null, 2, 3);