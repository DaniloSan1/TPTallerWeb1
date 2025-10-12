-- Insert Usuario (equivalent to: Usuario creador = new Usuario("usuario1", "password", "email@example.com"))
INSERT INTO Usuario(id, nombre, email, password, rol, activo) VALUES(null, 'usuario1', 'email@example.com', 'password', 'ROLE_USER', true);

-- Insert Cancha (equivalent to: Cancha cancha = new Cancha("Cancha 1", null, null, "Direccion 1", Zona.NORTE))
INSERT INTO Cancha(id, nombre, capacidad, tipoSuelo, zona) VALUES(null, 'Cancha 1', null, 'Direccion 1', 'NORTE');

-- Insert Horario (equivalent to: Horario horario = new Horario(cancha, DayOfWeek.MONDAY, LocalTime.now(), LocalTime.now().plusHours(1)))
INSERT INTO Horario(id, cancha_id, diaSemana, horaInicio, horaFin, disponible) VALUES(null, 1, 'MONDAY', '14:00:00', '15:00:00', true);

-- Insert Partido (equivalent to: Partido nuevoPartido = new Partido("Partido de prueba", "Descripción del partido", Zona.NORTE, Nivel.INTERMEDIO, LocalDateTime.now(), 10, horario, creador))
INSERT INTO Partido(id, titulo, descripcion, zona, nivel, fecha, cupoMaximo, horario_id, creador_id) VALUES(null, 'Partido de prueba', 'Descripción del partido', 'NORTE', 'INTERMEDIO', '2024-10-07 14:00:00', 10, 1, 1);

-- Keep the original admin user
INSERT INTO Usuario(id, email, password, rol, activo) VALUES(null, 'test@unlam.edu.ar', 'test', 'ADMIN', true);
