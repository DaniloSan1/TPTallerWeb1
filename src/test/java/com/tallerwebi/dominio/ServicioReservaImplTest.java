package com.tallerwebi.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
public class ServicioReservaImplTest {
    private ServicioReservaImpl servicioReserva;
    private RepositorioReserva repositorioMock;
    private Horario horario;
    private Usuario usuario;
    
    @BeforeEach
    public void setUp() {
        repositorioMock = mock(RepositorioReserva.class);
        servicioReserva = new ServicioReservaImpl(repositorioMock);
        horario = new Horario();
        horario.setId(1L);
        usuario = new Usuario();
        usuario.setId(1L);
    }
    @Test
    public void DeberiaCrearUnaReserva() {
        when(repositorioMock.porHorarioYFecha(any(), any())).thenReturn(Collections.emptyList());
        Reserva reserva = new Reserva(horario, usuario, LocalDateTime.now());
        Reserva creada = servicioReserva.crearReserva(reserva);
        verify(repositorioMock).guardar(reserva);
        assertEquals(reserva, creada);
    }
    @Test
    public void AlCrearUnaReservaExistenteDeberiaLanzarExcepcion() {
        when(repositorioMock.porHorarioYFecha(any(), any())).thenReturn(Collections.singletonList(new Reserva()));
        Reserva reserva = new Reserva(horario, usuario, LocalDateTime.now());
        assertThrows(RuntimeException.class, () -> servicioReserva.crearReserva(reserva));
    }
    @Test
    public void AlCancelarUnaReservaDeberiaCambiarSuEstado() {
        Reserva reserva = new Reserva(horario, usuario, LocalDateTime.now());
        reserva.setActiva(true);
        reserva.setId(1L);
        when(repositorioMock.porId(1L)).thenReturn(reserva);
        servicioReserva.cancelarReserva(reserva.getId());
        verify(repositorioMock).guardar(reserva);
        assertEquals(false, reserva.getActiva());
    }
    @Test
    public void AlCancelarUnaReservaInactivaDeberiaLanzarExcepcion() {
        Reserva reserva = new Reserva(horario, usuario, LocalDateTime.now());
        reserva.setActiva(false);
        reserva.setId(1L);
        when(repositorioMock.porId(1L)).thenReturn(reserva);
        assertThrows(RuntimeException.class, () -> servicioReserva.cancelarReserva(reserva.getId()));
    }
}