package com.tallerwebi.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.tallerwebi.dominio.excepcion.NoHayCupoEnPartido;
import com.tallerwebi.dominio.excepcion.PartidoNoEncontrado;
import com.tallerwebi.dominio.excepcion.YaExisteElParticipante;

public class ServicioPartidoImplTest {
        private RepositorioPartido repositorioPartidoMock;
        private RepositorioReserva repositorioReservaMock;
        private RepositorioUsuario repositorioUsuarioMock;
        private RepositorioPartidoParticipante repositorioPartidoParticipanteMock;
        private Partido partidoMock;
        private Usuario usuarioMock;

        @BeforeEach
        public void init() {
                repositorioPartidoMock = Mockito.mock(RepositorioPartido.class);
                repositorioReservaMock = Mockito.mock(RepositorioReserva.class);
                repositorioUsuarioMock = Mockito.mock(RepositorioUsuario.class);
                repositorioPartidoParticipanteMock = Mockito.mock(RepositorioPartidoParticipante.class);
                partidoMock = Mockito.mock(Partido.class);
                usuarioMock = Mockito.mock(Usuario.class);
        }

        @Test
        public void deberiaDevolverExcepcionCuandoNoSeEncuentraElPartido() throws PartidoNoEncontrado {
                Mockito.when(repositorioPartidoMock.porId(Mockito.anyLong())).thenReturn(null);
                ServicioPartido servicioPartido = new ServicioPartidoImpl(repositorioPartidoMock,
                                repositorioReservaMock,
                                repositorioUsuarioMock, repositorioPartidoParticipanteMock);
                assertThrows(PartidoNoEncontrado.class, () -> servicioPartido.obtenerPorId(1L));
                Mockito.verify(repositorioPartidoMock, Mockito.times(1)).porId(Mockito.anyLong());
        }

        @Test
        public void deberiaDevolverUnPartidoExistente() throws PartidoNoEncontrado {
                Long id = Mockito.anyLong();
                Mockito.when(repositorioPartidoMock.porId(id)).thenReturn(partidoMock);
                Mockito.when(partidoMock.getId()).thenReturn(id);
                Mockito.when(partidoMock.getFecha()).thenReturn(LocalDateTime.now());
                Mockito.when(partidoMock.getCupoMaximo()).thenReturn(10);
                Mockito.when(partidoMock.getZona()).thenReturn(Zona.NORTE);
                Mockito.when(partidoMock.getNivel()).thenReturn(Nivel.INTERMEDIO);
                Mockito.when(partidoMock.getTitulo()).thenReturn("Partido de prueba");
                Mockito.when(partidoMock.getDescripcion()).thenReturn("Descripción del partido");
                PartidoParticipante participanteMock1 = Mockito.mock(PartidoParticipante.class);
                PartidoParticipante participanteMock2 = Mockito.mock(PartidoParticipante.class);
                PartidoParticipante participanteMock3 = Mockito.mock(PartidoParticipante.class);
                PartidoParticipante participanteMock4 = Mockito.mock(PartidoParticipante.class);
                Mockito.when(partidoMock.getParticipantes()).thenReturn(java.util.Set.of(participanteMock1,
                                participanteMock2, participanteMock3, participanteMock4));

                ServicioPartido servicioPartido = new ServicioPartidoImpl(repositorioPartidoMock,
                                repositorioReservaMock,
                                repositorioUsuarioMock, repositorioPartidoParticipanteMock);

                Partido partidoObtenido = servicioPartido.obtenerPorId(id);
                assertEquals(partidoMock, partidoObtenido);
                Mockito.verify(repositorioPartidoMock, Mockito.times(1)).porId(Mockito.anyLong());
        }

        @Test
        public void deberiaLanzarExcepcionAlAnotarParticipanteSiNoHayCupo() throws NoHayCupoEnPartido {
                Mockito.when(repositorioPartidoMock.porId(Mockito.anyLong())).thenReturn(partidoMock);
                Mockito.when(partidoMock.validarCupo()).thenReturn(false);

                ServicioPartido servicioPartido = new ServicioPartidoImpl(repositorioPartidoMock,
                                repositorioReservaMock,
                                repositorioUsuarioMock, repositorioPartidoParticipanteMock);

                assertThrows(NoHayCupoEnPartido.class, () -> servicioPartido.anotarParticipante(1L, usuarioMock));
                Mockito.verify(repositorioPartidoMock, Mockito.times(1)).porId(Mockito.anyLong());
                Mockito.verify(repositorioPartidoParticipanteMock, Mockito.times(0)).guardar(Mockito.any());
        }

        @Test
        public void deberiaLanzarExcepcionAlAnotarParticipanteEnPartidoInexistente() throws PartidoNoEncontrado {
                Mockito.when(repositorioPartidoMock.porId(Mockito.anyLong())).thenReturn(null);

                ServicioPartido servicioPartido = new ServicioPartidoImpl(repositorioPartidoMock,
                                repositorioReservaMock,
                                repositorioUsuarioMock, repositorioPartidoParticipanteMock);

                assertThrows(PartidoNoEncontrado.class, () -> servicioPartido.anotarParticipante(1L, usuarioMock));
                Mockito.verify(repositorioPartidoMock, Mockito.times(1)).porId(Mockito.anyLong());
                Mockito.verify(repositorioPartidoParticipanteMock, Mockito.times(0)).guardar(Mockito.any());
        }

        @Test
        public void deberiaLanzarExcepcionAlAnotarParticipanteSiYaEstaAnotado() throws YaExisteElParticipante {
                Mockito.when(repositorioPartidoMock.porId(Mockito.anyLong())).thenReturn(partidoMock);
                Mockito.when(partidoMock.validarCupo()).thenReturn(true);
                Mockito.when(partidoMock.validarParticipanteExistente(Mockito.anyLong())).thenReturn(true);

                ServicioPartido servicioPartido = new ServicioPartidoImpl(repositorioPartidoMock,
                                repositorioReservaMock,
                                repositorioUsuarioMock, repositorioPartidoParticipanteMock);

                assertThrows(YaExisteElParticipante.class, () -> servicioPartido.anotarParticipante(1L, usuarioMock));
                Mockito.verify(repositorioPartidoMock, Mockito.times(1)).porId(Mockito.anyLong());
                Mockito.verify(repositorioPartidoParticipanteMock, Mockito.times(0)).guardar(Mockito.any());
        }

        @Test
        public void deberiaAnotarParticipanteSiHayCupo() {
                Mockito.when(repositorioPartidoMock.porId(Mockito.anyLong())).thenReturn(partidoMock);
                Mockito.when(partidoMock.validarCupo()).thenReturn(true);
                Mockito.when(partidoMock.validarParticipanteExistente(Mockito.anyLong())).thenReturn(false);

                ServicioPartido servicioPartido = new ServicioPartidoImpl(repositorioPartidoMock,
                                repositorioReservaMock,
                                repositorioUsuarioMock, repositorioPartidoParticipanteMock);

                servicioPartido.anotarParticipante(1L, usuarioMock);

                Mockito.verify(repositorioPartidoMock, Mockito.times(1)).porId(Mockito.anyLong());
                Mockito.verify(repositorioPartidoParticipanteMock, Mockito.times(1)).guardar(Mockito.any());
        }

        @Test
        public void deberiaFiltrarPartidosPorZona() {
                Partido partidoNorte = Mockito.mock(Partido.class);
                Partido partidoSur = Mockito.mock(Partido.class);

                Mockito.when(partidoNorte.getZona()).thenReturn(Zona.NORTE);
                Mockito.when(partidoSur.getZona()).thenReturn(Zona.SUR);

                Mockito.when(repositorioPartidoMock.listar(null, Zona.NORTE, null)).thenReturn(java.util.List.of(partidoNorte));

                ServicioPartido servicioPartido = new ServicioPartidoImpl(
                                repositorioPartidoMock,
                                repositorioReservaMock,
                                repositorioUsuarioMock,
                                repositorioPartidoParticipanteMock);

                var partidosFiltrados = servicioPartido.listarTodos(null,Zona.NORTE, null);

                assertEquals(1, partidosFiltrados.size());
                assertEquals(Zona.NORTE, partidosFiltrados.get(0).getZona());
                Mockito.verify(repositorioPartidoMock, Mockito.times(1)).listar(null, Zona.NORTE, null);
        }

        @Test
        public void deberiaFiltrarPartidosPorNivel() {
                Partido partidoAvanzado = Mockito.mock(Partido.class);
                Partido partidoPrincipiante = Mockito.mock(Partido.class);

                Mockito.when(partidoAvanzado.getNivel()).thenReturn(Nivel.AVANZADO);
                Mockito.when(partidoPrincipiante.getNivel()).thenReturn(Nivel.PRINCIPIANTE);

                Mockito.when(repositorioPartidoMock.listar(null, null, Nivel.PRINCIPIANTE))
                                .thenReturn(java.util.List.of(partidoPrincipiante));

                ServicioPartido servicioPartido = new ServicioPartidoImpl(
                                repositorioPartidoMock,
                                repositorioReservaMock,
                                repositorioUsuarioMock,
                                repositorioPartidoParticipanteMock);

                var partidosFiltrados = servicioPartido.listarTodos(null, null,Nivel.PRINCIPIANTE);

                assertEquals(1, partidosFiltrados.size());
                assertEquals(Nivel.PRINCIPIANTE, partidosFiltrados.get(0).getNivel());
                Mockito.verify(repositorioPartidoMock, Mockito.times(1)).listar(null, null, Nivel.PRINCIPIANTE);
        }

        @Test
        public void abandonarPartidoDeberiaEliminarAlUsuarioDeLaListaDeParticipantes() {
                Usuario usuario = new Usuario("usuario", "123", "email@mail.com","username");
                usuario.setId(1L);
                Partido partido = new Partido();
                partido.setId(1L);
                PartidoParticipante participante = new PartidoParticipante(partido, usuario, Equipo.EQUIPO_1);
                partido.getParticipantes().add(participante);

                Mockito.when(repositorioPartidoMock.porId(1L)).thenReturn(partido);

                ServicioPartido servicioPartido = new ServicioPartidoImpl(
                                repositorioPartidoMock,
                                repositorioReservaMock,
                                repositorioUsuarioMock,
                                repositorioPartidoParticipanteMock);

                servicioPartido.abandonarPartido(1L, 1L);

                assertEquals(0, partido.getParticipantes().size());
                Mockito.verify(repositorioPartidoMock, Mockito.times(1)).porId(1L);
        }

        @Test
        public void deberiaLanzarExcepcionAlAbandonarPartidoYElPartidoNoExiste() {
                Mockito.when(repositorioPartidoMock.porId(1L)).thenReturn(null);
                ServicioPartido servicioPartido = new ServicioPartidoImpl(
                                repositorioPartidoMock,
                                repositorioReservaMock,
                                repositorioUsuarioMock,
                                repositorioPartidoParticipanteMock);

                assertThrows(PartidoNoEncontrado.class, () -> servicioPartido.abandonarPartido(1L, 1L));
                Mockito.verify(repositorioPartidoMock, Mockito.times(1)).porId(1L);
        }

        @Test
        public void deberiaLanzarExcepcionAlAbandonarPartidoSiElUsuarioNoEstaAnotado() {
                Partido partido = new Partido();
                partido.setId(1L);

                Mockito.when(repositorioPartidoMock.porId(1L)).thenReturn(partido);

                ServicioPartido servicioPartido = new ServicioPartidoImpl(
                                repositorioPartidoMock,
                                repositorioReservaMock,
                                repositorioUsuarioMock,
                                repositorioPartidoParticipanteMock);

                assertThrows(RuntimeException.class, () -> servicioPartido.abandonarPartido(1L, 1L));
                Mockito.verify(repositorioPartidoMock, Mockito.times(1)).porId(1L);
                Mockito.verify(repositorioPartidoMock, Mockito.never()).guardar(Mockito.any());
        }

        @Test
        public void deberiaEliminarSoloAlUsuarioCorrecto() {
                Usuario usuario1 = new Usuario("user1", "123", "email1@email.com","username2");
                usuario1.setId(1L);

                Usuario usuario2 = new Usuario("user2", "123", "email2@email.com","username2");
                usuario2.setId(2L);

                Partido partido = new Partido();
                partido.setId(1L);
                partido.getParticipantes().add(new PartidoParticipante(partido, usuario1, Equipo.EQUIPO_1));
                partido.getParticipantes().add(new PartidoParticipante(partido, usuario2, Equipo.EQUIPO_2));

                Mockito.when(repositorioPartidoMock.porId(1L)).thenReturn(partido);

                ServicioPartido servicioPartido = new ServicioPartidoImpl(
                                repositorioPartidoMock,
                                repositorioReservaMock,
                                repositorioUsuarioMock,
                                repositorioPartidoParticipanteMock);

                servicioPartido.abandonarPartido(1L, 1L);

                assertEquals(1, partido.getParticipantes().size());
                assertEquals(2L, partido.getParticipantes().iterator().next().getUsuario().getId());
        }

        @Test
        public void crearDesdeReservaDeberiaCrearUnPartidoCorrectamente() {
                RepositorioPartido repositorioPartidoMock = Mockito.mock(RepositorioPartido.class);
                RepositorioReserva repositorioReservaMock = Mockito.mock(RepositorioReserva.class);
                RepositorioUsuario repositorioUsuarioMock = Mockito.mock(RepositorioUsuario.class);
                RepositorioPartidoParticipante repositorioPartidoParticipanteMock = Mockito
                                .mock(RepositorioPartidoParticipante.class);

                ServicioPartido servicioPartido = new ServicioPartidoImpl(
                                repositorioPartidoMock,
                                repositorioReservaMock,
                                repositorioUsuarioMock,
                                repositorioPartidoParticipanteMock);

                Cancha cancha = new Cancha("Cancha 1", null, null, "Dirección 1", Zona.NORTE);
                cancha.setCapacidad(12);

                Horario horario = new Horario(cancha, DayOfWeek.MONDAY, LocalTime.of(18, 0), LocalTime.of(19, 0));
                Usuario creadorReserva = new Usuario("creador", "123", "c@c.com","usernameCreadorReserva");

                Reserva reserva = new Reserva(horario, creadorReserva, LocalDateTime.now().plusDays(1));
                reserva.setId(1L);
                reserva.setActiva(true);

                Usuario usuarioCreadorPartido = new Usuario("mora", "123", "mora@unlam.edu.ar","usernameMora");

                Partido partidoCreado = servicioPartido.crearDesdeReserva(
                                reserva,
                                "Título X",
                                "Descripción X",
                                Nivel.PRINCIPIANTE,
                                8,
                                usuarioCreadorPartido);

                // Assert (validamos el estado del objeto devuelto)
                assertEquals("Título X", partidoCreado.getTitulo());
                assertEquals("Descripción X", partidoCreado.getDescripcion());
                assertEquals(Nivel.PRINCIPIANTE, partidoCreado.getNivel());
                assertEquals(8, partidoCreado.getCupoMaximo());
                assertSame(reserva, partidoCreado.getReserva());
                assertSame(usuarioCreadorPartido, partidoCreado.getCreador());

                Mockito.verify(repositorioPartidoMock, Mockito.times(1)).guardar(Mockito.same(partidoCreado));
        }

}
