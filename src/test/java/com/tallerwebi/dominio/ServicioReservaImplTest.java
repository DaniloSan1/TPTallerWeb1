package com.tallerwebi.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Collections;

public class ServicioReservaImplTest {
    private ServicioReservaImpl servicioReserva;
    private RepositorioReserva repositorioMock;
    private Horario horarioMock;
    private Usuario usuarioMock;
    private Reserva reservaMock;

    @BeforeEach
    public void setUp() {
        repositorioMock = mock(RepositorioReserva.class);
        servicioReserva = new ServicioReservaImpl(repositorioMock);
        horarioMock = mock(Horario.class);
        when(horarioMock.getId()).thenReturn(4L);
        when(horarioMock.getDiaSemana()).thenReturn(LocalDateTime.now().plusDays(1).getDayOfWeek());
        usuarioMock = mock(Usuario.class);
        when(usuarioMock.getId()).thenReturn(1L);

        reservaMock = mock(Reserva.class);
        when(reservaMock.getId()).thenReturn(1L);
        when(reservaMock.getHorario()).thenReturn(horarioMock);
        when(reservaMock.getUsuario()).thenReturn(usuarioMock);
        when(reservaMock.getFechaReserva()).thenReturn(LocalDateTime.now().plusDays(1));
    }

    @Test
    public void DeberiaCrearUnaReserva() {
        when(repositorioMock.porHorarioYFecha(any(), any())).thenReturn(Collections.emptyList());
        Reserva creada = servicioReserva.crearReserva(reservaMock);
        verify(repositorioMock).guardar(reservaMock);
        assertEquals(reservaMock, creada);
    }

    @Test
    public void AlCrearUnaReservaExistenteDeberiaLanzarExcepcion() {
        when(repositorioMock.porHorarioYFecha(any(), any())).thenReturn(Collections.singletonList(new Reserva()));
        assertThrows(RuntimeException.class, () -> servicioReserva.crearReserva(reservaMock));
    }

    @Test
    public void AlCancelarUnaReservaDeberiaCambiarSuEstado() {
        Reserva reserva = new Reserva(horarioMock, usuarioMock, LocalDateTime.now());
        reserva.setActiva(true);
        reserva.setId(1L);
        when(repositorioMock.porId(1L)).thenReturn(reserva);
        servicioReserva.cancelarReserva(reserva.getId());
        verify(repositorioMock).guardar(reserva);
        assertEquals(false, reserva.getActiva());
    }

    @Test
    public void AlCancelarUnaReservaInactivaDeberiaLanzarExcepcion() {
        Reserva reserva = new Reserva(horarioMock, usuarioMock, LocalDateTime.now());
        reserva.setActiva(false);
        reserva.setId(1L);
        when(repositorioMock.porId(1L)).thenReturn(reserva);
        assertThrows(RuntimeException.class, () -> servicioReserva.cancelarReserva(reserva.getId()));
    }
}