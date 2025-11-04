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
        private ServicioEquipoJugador servicioEquipoJugadorMock;
        private Partido partidoMock;
        private Usuario usuarioMock;
        private Equipo equipoMock;

        @BeforeEach
        public void init() {
                repositorioPartidoMock = Mockito.mock(RepositorioPartido.class);
                repositorioReservaMock = Mockito.mock(RepositorioReserva.class);
                repositorioUsuarioMock = Mockito.mock(RepositorioUsuario.class);
                repositorioPartidoParticipanteMock = Mockito.mock(RepositorioPartidoParticipante.class);
                servicioEquipoJugadorMock = Mockito.mock(ServicioEquipoJugador.class);
                partidoMock = Mockito.mock(Partido.class);
                usuarioMock = Mockito.mock(Usuario.class);
                equipoMock = Mockito.mock(Equipo.class);
        }

        @Test
        public void deberiaDevolverExcepcionCuandoNoSeEncuentraElPartido() throws PartidoNoEncontrado {
                Mockito.when(repositorioPartidoMock.porId(Mockito.anyLong())).thenReturn(null);
                ServicioPartido servicioPartido = new ServicioPartidoImpl(repositorioPartidoMock,
                                repositorioReservaMock,
                                repositorioUsuarioMock, servicioEquipoJugadorMock,
                                repositorioPartidoParticipanteMock);
                assertThrows(PartidoNoEncontrado.class, () -> servicioPartido.obtenerPorId(1L));
                Mockito.verify(repositorioPartidoMock, Mockito.times(1)).porId(Mockito.anyLong());
        }

        @Test
        public void deberiaDevolverUnPartidoExistente() throws PartidoNoEncontrado {
                Long id = 1L;
                Mockito.when(repositorioPartidoMock.porId(id)).thenReturn(partidoMock);

                ServicioPartido servicioPartido = new ServicioPartidoImpl(repositorioPartidoMock,
                                repositorioReservaMock,
                                repositorioUsuarioMock, servicioEquipoJugadorMock,
                                repositorioPartidoParticipanteMock);

                Partido partidoObtenido = servicioPartido.obtenerPorId(id);
                assertEquals(partidoMock, partidoObtenido);
                Mockito.verify(repositorioPartidoMock, Mockito.times(1)).porId(id);
        }

        @Test
        public void deberiaLanzarExcepcionAlAnotarParticipanteSiNoHayCupo() throws NoHayCupoEnPartido {
                Mockito.when(partidoMock.validarCupo()).thenReturn(false);

                ServicioPartido servicioPartido = new ServicioPartidoImpl(repositorioPartidoMock,
                                repositorioReservaMock,
                                repositorioUsuarioMock, servicioEquipoJugadorMock,
                                repositorioPartidoParticipanteMock);

                assertThrows(NoHayCupoEnPartido.class,
                                () -> servicioPartido.anotarParticipante(partidoMock, equipoMock, usuarioMock));
                Mockito.verify(repositorioPartidoParticipanteMock, Mockito.times(0)).guardar(Mockito.any());
        }

        @Test
        public void deberiaLanzarExcepcionAlAnotarParticipanteEnPartidoInexistente() throws PartidoNoEncontrado {

                ServicioPartido servicioPartido = new ServicioPartidoImpl(repositorioPartidoMock,
                                repositorioReservaMock,
                                repositorioUsuarioMock, servicioEquipoJugadorMock,
                                repositorioPartidoParticipanteMock);

                assertThrows(NullPointerException.class,
                                () -> servicioPartido.anotarParticipante(null, equipoMock, usuarioMock));
                Mockito.verify(repositorioPartidoParticipanteMock, Mockito.times(0)).guardar(Mockito.any());
        }

        @Test
        public void deberiaLanzarExcepcionAlAnotarParticipanteSiYaEstaAnotado() throws YaExisteElParticipante {
                Mockito.when(partidoMock.validarCupo()).thenReturn(true);
                Mockito.when(servicioEquipoJugadorMock.crearEquipoJugador(equipoMock, usuarioMock))
                                .thenThrow(new YaExisteElParticipante());

                ServicioPartido servicioPartido = new ServicioPartidoImpl(repositorioPartidoMock,
                                repositorioReservaMock,
                                repositorioUsuarioMock, servicioEquipoJugadorMock,
                                repositorioPartidoParticipanteMock);

                assertThrows(YaExisteElParticipante.class,
                                () -> servicioPartido.anotarParticipante(partidoMock, equipoMock, usuarioMock));
        }

        @Test
        public void deberiaAnotarParticipanteSiHayCupo() {
                Mockito.when(partidoMock.validarCupo()).thenReturn(true);
                Mockito.when(partidoMock.validarParticipanteExistente(Mockito.anyLong())).thenReturn(false);
                EquipoJugador equipoJugadorMock = Mockito.mock(EquipoJugador.class);
                Mockito.when(servicioEquipoJugadorMock.crearEquipoJugador(equipoMock, usuarioMock))
                                .thenReturn(equipoJugadorMock);

                ServicioPartido servicioPartido = new ServicioPartidoImpl(repositorioPartidoMock,
                                repositorioReservaMock,
                                repositorioUsuarioMock, servicioEquipoJugadorMock,
                                repositorioPartidoParticipanteMock);

                servicioPartido.anotarParticipante(partidoMock, equipoMock, usuarioMock);

                Mockito.verify(servicioEquipoJugadorMock, Mockito.times(1)).crearEquipoJugador(equipoMock, usuarioMock);
                Mockito.verify(partidoMock, Mockito.times(1)).agregarParticipante(equipoJugadorMock);
        }

        @Test
        public void deberiaFiltrarPartidosPorZona() {
                Partido partidoNorte = Mockito.mock(Partido.class);
                Partido partidoSur = Mockito.mock(Partido.class);

                Mockito.when(partidoNorte.getZona()).thenReturn(Zona.NORTE);
                Mockito.when(partidoSur.getZona()).thenReturn(Zona.SUR);

                Mockito.when(repositorioPartidoMock.listar(null, Zona.NORTE, null))
                                .thenReturn(java.util.List.of(partidoNorte));

                ServicioPartido servicioPartido = new ServicioPartidoImpl(
                                repositorioPartidoMock,
                                repositorioReservaMock,
                                repositorioUsuarioMock, servicioEquipoJugadorMock,
                                repositorioPartidoParticipanteMock);

                var partidosFiltrados = servicioPartido.listarTodos(null, Zona.NORTE, null);

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
                                repositorioUsuarioMock, servicioEquipoJugadorMock,
                                repositorioPartidoParticipanteMock);

                var partidosFiltrados = servicioPartido.listarTodos(null, null, Nivel.PRINCIPIANTE);

                assertEquals(1, partidosFiltrados.size());
                assertEquals(Nivel.PRINCIPIANTE, partidosFiltrados.get(0).getNivel());
                Mockito.verify(repositorioPartidoMock, Mockito.times(1)).listar(null, null, Nivel.PRINCIPIANTE);
        }

        @Test
        public void abandonarPartidoDeberiaEliminarAlUsuarioDeLaListaDeParticipantes() {
                Usuario usuario = new Usuario("usuario", "123", "email@mail.com", "username");
                usuario.setId(1L);
                Partido partido = Mockito.mock(Partido.class);
                partido.setId(1L);
                EquipoJugador equipoJugador = Mockito.mock(EquipoJugador.class);
                Mockito.when(equipoJugador.getId()).thenReturn(1L);
                Mockito.when(partido.buscarJugador(1L)).thenReturn(equipoJugador);

                Mockito.when(repositorioPartidoMock.porId(1L)).thenReturn(partido);

                ServicioPartido servicioPartido = new ServicioPartidoImpl(
                                repositorioPartidoMock,
                                repositorioReservaMock,
                                repositorioUsuarioMock, servicioEquipoJugadorMock,
                                repositorioPartidoParticipanteMock);

                servicioPartido.abandonarPartido(1L, usuario);

                Mockito.verify(servicioEquipoJugadorMock, Mockito.times(1)).eliminarPorId(1L);
                Mockito.verify(repositorioPartidoMock, Mockito.times(1)).porId(1L);
        }

        @Test
        public void deberiaLanzarExcepcionAlAbandonarPartidoYElPartidoNoExiste() {
                Usuario usuario = new Usuario("usuario", "123", "email@mail.com", "username");
                usuario.setId(1L);
                Mockito.when(repositorioPartidoMock.porId(1L)).thenReturn(null);
                ServicioPartido servicioPartido = new ServicioPartidoImpl(
                                repositorioPartidoMock,
                                repositorioReservaMock,
                                repositorioUsuarioMock, servicioEquipoJugadorMock,
                                repositorioPartidoParticipanteMock);

                assertThrows(PartidoNoEncontrado.class, () -> servicioPartido.abandonarPartido(1L, usuario));
                Mockito.verify(repositorioPartidoMock, Mockito.times(1)).porId(1L);
        }

        @Test
        public void deberiaLanzarExcepcionAlAbandonarPartidoSiElUsuarioNoEstaAnotado() {
                Usuario usuario = new Usuario("usuario", "123", "email@mail.com", "username");
                usuario.setId(1L);
                Partido partido = new Partido();
                partido.setId(1L);

                Mockito.when(repositorioPartidoMock.porId(1L)).thenReturn(partido);

                ServicioPartido servicioPartido = new ServicioPartidoImpl(
                                repositorioPartidoMock,
                                repositorioReservaMock,
                                repositorioUsuarioMock, servicioEquipoJugadorMock,
                                repositorioPartidoParticipanteMock);

                assertThrows(RuntimeException.class, () -> servicioPartido.abandonarPartido(1L, usuario));
                Mockito.verify(repositorioPartidoMock, Mockito.times(1)).porId(1L);
                Mockito.verify(repositorioPartidoMock, Mockito.never()).guardar(Mockito.any());
        }

        @Test
        public void deberiaEliminarSoloAlUsuarioCorrecto() {
                Usuario usuario1 = new Usuario("user1", "123", "email1@email.com", "username2");
                usuario1.setId(1L);

                Usuario usuario2 = new Usuario("user2", "123", "email2@email.com", "username2");
                usuario2.setId(2L);

                Partido partido = Mockito.mock(Partido.class);
                EquipoJugador equipoJugador1 = Mockito.mock(EquipoJugador.class);
                Mockito.when(equipoJugador1.getId()).thenReturn(1L);
                EquipoJugador equipoJugador2 = Mockito.mock(EquipoJugador.class);
                Mockito.when(equipoJugador2.getId()).thenReturn(2L);
                Mockito.when(partido.buscarJugador(1L)).thenReturn(equipoJugador1);
                Mockito.when(partido.buscarJugador(2L)).thenReturn(equipoJugador2);

                Mockito.when(repositorioPartidoMock.porId(1L)).thenReturn(partido);

                ServicioPartido servicioPartido = new ServicioPartidoImpl(
                                repositorioPartidoMock,
                                repositorioReservaMock,
                                repositorioUsuarioMock, servicioEquipoJugadorMock,
                                repositorioPartidoParticipanteMock);

                servicioPartido.abandonarPartido(1L, usuario1);

                Mockito.verify(servicioEquipoJugadorMock, Mockito.times(1)).eliminarPorId(1L);
                Mockito.verify(servicioEquipoJugadorMock, Mockito.never()).eliminarPorId(2L);
        }

        @Test
        public void crearDesdeReservaDeberiaCrearUnPartidoCorrectamente() {
                RepositorioPartido repositorioPartidoMock = Mockito.mock(RepositorioPartido.class);
                RepositorioReserva repositorioReservaMock = Mockito.mock(RepositorioReserva.class);
                RepositorioUsuario repositorioUsuarioMock = Mockito.mock(RepositorioUsuario.class);
                RepositorioPartidoParticipante repositorioPartidoParticipanteMock = Mockito
                                .mock(RepositorioPartidoParticipante.class);
                ServicioEquipoJugador servicioEquipoJugadorMock = Mockito.mock(ServicioEquipoJugador.class);

                ServicioPartido servicioPartido = new ServicioPartidoImpl(
                                repositorioPartidoMock,
                                repositorioReservaMock,
                                repositorioUsuarioMock, servicioEquipoJugadorMock,
                                repositorioPartidoParticipanteMock);

                Cancha cancha = new Cancha("Cancha 1", null, null, "Dirección 1", Zona.NORTE);
                cancha.setCapacidad(12);

                Horario horario = new Horario(cancha, DayOfWeek.MONDAY, LocalTime.of(18, 0), LocalTime.of(19, 0));
                Usuario creadorReserva = new Usuario("creador", "123", "c@c.com", "usernameCreadorReserva");

                Reserva reserva = new Reserva(horario, creadorReserva, LocalDateTime.now().plusDays(1));
                reserva.setId(1L);
                reserva.setActiva(true);

                Usuario usuarioCreadorPartido = new Usuario("mora", "123", "mora@unlam.edu.ar", "usernameMora");

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
                assertEquals(12, partidoCreado.getCupoMaximo());
                assertSame(reserva, partidoCreado.getReserva());
                assertSame(usuarioCreadorPartido, partidoCreado.getCreador());

                Mockito.verify(repositorioPartidoMock, Mockito.times(1)).guardar(Mockito.same(partidoCreado));
        }

}
