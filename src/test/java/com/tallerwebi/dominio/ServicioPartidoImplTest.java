package com.tallerwebi.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.cglib.core.Local;

import com.tallerwebi.dominio.excepcion.NoExisteElUsuario;
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
        ServicioPartido servicioPartido = new ServicioPartidoImpl(repositorioPartidoMock, repositorioReservaMock,
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

        ServicioPartido servicioPartido = new ServicioPartidoImpl(repositorioPartidoMock, repositorioReservaMock,
                repositorioUsuarioMock, repositorioPartidoParticipanteMock);
        Partido partidoObtenido = servicioPartido.obtenerPorId(id);
        assertEquals(partidoMock, partidoObtenido);
        Mockito.verify(repositorioPartidoMock, Mockito.times(1)).porId(Mockito.anyLong());
    }

    @Test
    public void deberiaLanzarExcepcionAlAnotarParticipanteInexistente() throws NoExisteElUsuario {
        Mockito.when(repositorioPartidoMock.porId(Mockito.anyLong())).thenReturn(partidoMock);
        Mockito.when(partidoMock.validarCupo()).thenReturn(false);
        Mockito.when(repositorioUsuarioMock.buscar(Mockito.anyString()))
                .thenReturn(null);

        ServicioPartido servicioPartido = new ServicioPartidoImpl(repositorioPartidoMock, repositorioReservaMock,
                repositorioUsuarioMock, repositorioPartidoParticipanteMock);

        assertThrows(NoExisteElUsuario.class, () -> servicioPartido.anotarParticipante(1L, "usuario1@email.com"));
        Mockito.verify(repositorioUsuarioMock, Mockito.times(1)).buscar(Mockito.anyString());
        Mockito.verify(repositorioPartidoMock, Mockito.times(0)).porId(Mockito.anyLong());
        Mockito.verify(repositorioPartidoParticipanteMock, Mockito.times(0)).guardar(Mockito.any());
    }

    @Test
    public void deberiaLanzarExcepcionAlAnotarParticipanteSiNoHayCupo() throws NoHayCupoEnPartido {
        Mockito.when(repositorioPartidoMock.porId(Mockito.anyLong())).thenReturn(partidoMock);
        Mockito.when(partidoMock.validarCupo()).thenReturn(false);
        Mockito.when(repositorioUsuarioMock.buscar(Mockito.anyString()))
                .thenReturn(usuarioMock);
        Mockito.when(usuarioMock.getId()).thenReturn(1L);

        ServicioPartido servicioPartido = new ServicioPartidoImpl(repositorioPartidoMock, repositorioReservaMock,
                repositorioUsuarioMock, repositorioPartidoParticipanteMock);

        assertThrows(NoHayCupoEnPartido.class, () -> servicioPartido.anotarParticipante(1L, "usuario1@email.com"));
        Mockito.verify(repositorioPartidoMock, Mockito.times(1)).porId(Mockito.anyLong());
        Mockito.verify(repositorioUsuarioMock, Mockito.times(1)).buscar(Mockito.anyString());
        Mockito.verify(repositorioPartidoParticipanteMock, Mockito.times(0)).guardar(Mockito.any());
    }

    @Test
    public void deberiaLanzarExcepcionAlAnotarParticipanteEnPartidoInexistente() throws PartidoNoEncontrado {
        Mockito.when(repositorioPartidoMock.porId(Mockito.anyLong())).thenReturn(null);
        Mockito.when(repositorioUsuarioMock.buscar(Mockito.anyString()))
                .thenReturn(usuarioMock);
        Mockito.when(usuarioMock.getId()).thenReturn(1L);

        ServicioPartido servicioPartido = new ServicioPartidoImpl(repositorioPartidoMock, repositorioReservaMock,
                repositorioUsuarioMock, repositorioPartidoParticipanteMock);

        assertThrows(PartidoNoEncontrado.class, () -> servicioPartido.anotarParticipante(1L, "usuario1@email.com"));
        Mockito.verify(repositorioPartidoMock, Mockito.times(1)).porId(Mockito.anyLong());
        Mockito.verify(repositorioUsuarioMock, Mockito.times(1)).buscar(Mockito.anyString());
        Mockito.verify(repositorioPartidoParticipanteMock, Mockito.times(0)).guardar(Mockito.any());
    }

    @Test
    public void deberiaLanzarExcepcionAlAnotarParticipanteSiYaEstaAnotado() throws YaExisteElParticipante {
        Mockito.when(repositorioPartidoMock.porId(Mockito.anyLong())).thenReturn(partidoMock);
        Mockito.when(partidoMock.validarCupo()).thenReturn(true);
        Mockito.when(partidoMock.validarParticipanteExistente(Mockito.anyLong())).thenReturn(true);
        Mockito.when(repositorioUsuarioMock.buscar(Mockito.anyString()))
                .thenReturn(usuarioMock);
        Mockito.when(usuarioMock.getId()).thenReturn(1L);

        ServicioPartido servicioPartido = new ServicioPartidoImpl(repositorioPartidoMock, repositorioReservaMock,
                repositorioUsuarioMock, repositorioPartidoParticipanteMock);

        assertThrows(YaExisteElParticipante.class, () -> servicioPartido.anotarParticipante(1L, "usuario1@email.com"));
        Mockito.verify(repositorioPartidoMock, Mockito.times(1)).porId(Mockito.anyLong());
        Mockito.verify(repositorioUsuarioMock, Mockito.times(1)).buscar(Mockito.anyString());
        Mockito.verify(repositorioPartidoParticipanteMock, Mockito.times(0)).guardar(Mockito.any());
    }

    @Test
    public void deberiaAnotarParticipanteSiHayCupo() {
        Mockito.when(repositorioPartidoMock.porId(Mockito.anyLong())).thenReturn(partidoMock);
        Mockito.when(partidoMock.validarCupo()).thenReturn(true);
        Mockito.when(partidoMock.validarParticipanteExistente(Mockito.anyLong())).thenReturn(false);
        Mockito.when(repositorioUsuarioMock.buscar(Mockito.anyString()))
                .thenReturn(usuarioMock);
        Mockito.when(usuarioMock.getId()).thenReturn(1L);

        ServicioPartido servicioPartido = new ServicioPartidoImpl(repositorioPartidoMock, repositorioReservaMock,
                repositorioUsuarioMock, repositorioPartidoParticipanteMock);

        servicioPartido.anotarParticipante(1L, "usuario1@email.com");

        Mockito.verify(repositorioPartidoMock, Mockito.times(1)).porId(Mockito.anyLong());
        Mockito.verify(repositorioUsuarioMock, Mockito.times(1)).buscar(Mockito.anyString());
        Mockito.verify(repositorioPartidoParticipanteMock, Mockito.times(1)).guardar(Mockito.any());
    }

    @Test
    public void deberiaFiltrarPartidosPorZona() {
        Partido partidoNorte = Mockito.mock(Partido.class);
        Partido partidoSur = Mockito.mock(Partido.class);

        Mockito.when(partidoNorte.getZona()).thenReturn(Zona.NORTE);
        Mockito.when(partidoSur.getZona()).thenReturn(Zona.SUR);

        Mockito.when(repositorioPartidoMock.todos()).thenReturn(java.util.List.of(partidoNorte, partidoSur));

        ServicioPartido servicioPartido = new ServicioPartidoImpl(
                repositorioPartidoMock,
                repositorioReservaMock,
                repositorioUsuarioMock,
                repositorioPartidoParticipanteMock
        );

        var partidosFiltrados = servicioPartido.buscar(Zona.NORTE, null, false);

        assertEquals(1, partidosFiltrados.size());
        assertEquals(Zona.NORTE, partidosFiltrados.get(0).getZona());
        Mockito.verify(repositorioPartidoMock, Mockito.times(1)).todos();
    }

    @Test
    public void deberiaFiltrarPartidosPorNivel() {
        Partido partidoAvanzado = Mockito.mock(Partido.class);
        Partido partidoPrincipiante = Mockito.mock(Partido.class);

        Mockito.when(partidoAvanzado.getNivel()).thenReturn(Nivel.AVANZADO);
        Mockito.when(partidoPrincipiante.getNivel()).thenReturn(Nivel.PRINCIPIANTE);

        Mockito.when(repositorioPartidoMock.todos()).thenReturn(java.util.List.of(partidoAvanzado, partidoPrincipiante));

        ServicioPartido servicioPartido = new ServicioPartidoImpl(
                repositorioPartidoMock,
                repositorioReservaMock,
                repositorioUsuarioMock,
                repositorioPartidoParticipanteMock
        );

        var partidosFiltrados = servicioPartido.buscar(null, Nivel.AVANZADO, false);

        assertEquals(1, partidosFiltrados.size());
        assertEquals(Nivel.AVANZADO, partidosFiltrados.get(0).getNivel());
        Mockito.verify(repositorioPartidoMock, Mockito.times(1)).todos();
    }

    @Test
    public void abandonarPartidoDeberiaEliminarAlUsuarioDeLaListaDeParticipantes() {
        Usuario usuario = new Usuario("usuario", "123", "email@mail.com");
        usuario.setId(1L);
        Partido partido = new Partido();
        partido.setId(1L);
        PartidoParticipante participante = new PartidoParticipante(partido, usuario);
        partido.getParticipantes().add(participante);

        Mockito.when(repositorioPartidoMock.porId(1L)).thenReturn(partido);

        ServicioPartido servicioPartido = new ServicioPartidoImpl(
                repositorioPartidoMock,
                repositorioReservaMock,
                repositorioUsuarioMock,
                repositorioPartidoParticipanteMock
        );

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
                repositorioPartidoParticipanteMock
        );

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
                repositorioPartidoParticipanteMock
        );

        assertThrows(RuntimeException.class, () -> servicioPartido.abandonarPartido(1L, 1L));
        Mockito.verify(repositorioPartidoMock, Mockito.times(1)).porId(1L);
        Mockito.verify(repositorioPartidoMock, Mockito.never()).guardar(Mockito.any());
    }

    @Test
    public void deberiaEliminarSoloAlUsuarioCorrecto() {
        Usuario usuario1 = new Usuario("user1", "123", "email1@email.com");
        usuario1.setId(1L);

        Usuario usuario2 = new Usuario("user2", "123", "email2@email.com");
        usuario2.setId(2L);

        Partido partido = new Partido();
        partido.setId(1L);
        partido.getParticipantes().add(new PartidoParticipante(partido, usuario1));
        partido.getParticipantes().add(new PartidoParticipante(partido, usuario2));

        Mockito.when(repositorioPartidoMock.porId(1L)).thenReturn(partido);

        ServicioPartido servicioPartido = new ServicioPartidoImpl(
                repositorioPartidoMock,
                repositorioReservaMock,
                repositorioUsuarioMock,
                repositorioPartidoParticipanteMock
        );

        servicioPartido.abandonarPartido(1L, 1L);

        assertEquals(1, partido.getParticipantes().size());
        assertEquals(2L, partido.getParticipantes().iterator().next().getUsuario().getId());
    }


}
