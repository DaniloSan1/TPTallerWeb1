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
    public void deberiaLanzarExcepcionAlAnotarParticipante() throws NoHayCupoEnPartido {
        Mockito.when(repositorioPartidoMock.porId(Mockito.anyLong())).thenReturn(partidoMock);
        Mockito.when(partidoMock.validarCupo()).thenReturn(false);

        ServicioPartido servicioPartido = new ServicioPartidoImpl(repositorioPartidoMock, repositorioReservaMock,
                repositorioUsuarioMock, repositorioPartidoParticipanteMock);

        assertThrows(NoHayCupoEnPartido.class, () -> servicioPartido.anotarParticipante(1L, "usuario1@email.com"));
        Mockito.verify(repositorioPartidoMock, Mockito.times(1)).porId(Mockito.anyLong());
        Mockito.verify(repositorioUsuarioMock, Mockito.times(0)).buscar(Mockito.anyString());
        Mockito.verify(repositorioPartidoParticipanteMock, Mockito.times(0)).guardar(Mockito.any());
    }

    @Test
    public void deberiaLanzarExcepcionAlAnotarParticipanteEnPartidoInexistente() throws PartidoNoEncontrado {
        Mockito.when(repositorioPartidoMock.porId(Mockito.anyLong())).thenReturn(null);

        ServicioPartido servicioPartido = new ServicioPartidoImpl(repositorioPartidoMock, repositorioReservaMock,
                repositorioUsuarioMock, repositorioPartidoParticipanteMock);

        assertThrows(PartidoNoEncontrado.class, () -> servicioPartido.anotarParticipante(1L, "usuario1@email.com"));
        Mockito.verify(repositorioPartidoMock, Mockito.times(1)).porId(Mockito.anyLong());
        Mockito.verify(repositorioUsuarioMock, Mockito.times(0)).buscar(Mockito.anyString());
        Mockito.verify(repositorioPartidoParticipanteMock, Mockito.times(0)).guardar(Mockito.any());
    }

    @Test
    public void deberiaLanzarExcepcionSiElParticipanteYaEstaAnotado() throws YaExisteElParticipante {
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
}
