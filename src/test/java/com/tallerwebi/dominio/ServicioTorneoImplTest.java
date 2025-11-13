package com.tallerwebi.dominio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ServicioTorneoImplTest {

    private RepositorioTorneo repositorioTorneo;
    private RepositorioReserva repositorioReserva;
    private ServicioTorneoImpl servicioTorneo;

    private Torneo torneo;
    private Cancha cancha;
    private Usuario organizador;

    @BeforeEach
    void setUp() {
        repositorioTorneo = mock(RepositorioTorneo.class);
        repositorioReserva = mock(RepositorioReserva.class);
        servicioTorneo = new ServicioTorneoImpl(repositorioTorneo, repositorioReserva);

        cancha = new Cancha();
        cancha.setId(1L);
        cancha.setNombre("Cancha Central");
        cancha.setPrecio(100.0);

        organizador = new Usuario();
        organizador.setId(2L);

        torneo = new Torneo();
        torneo.setId(10L);
        torneo.setCancha(cancha);
        torneo.setOrganizador(organizador);
        torneo.setFecha(LocalDate.now().plusDays(5));
        torneo.setNombre("Torneo Primavera");
    }

    @Test
    void alCrearTorneoConDatosValidosDeberiaGuardarConPrecioYEstadoPendiente() {
        when(repositorioTorneo.existeCanchaYFecha(cancha, torneo.getFecha())).thenReturn(false);
        when(repositorioReserva.porCanchaYFecha(cancha, torneo.getFecha())).thenReturn(Collections.emptyList());

        Torneo resultado = servicioTorneo.crearTorneo(torneo);

        assertEquals("PENDIENTE", resultado.getEstado());
        assertEquals(BigDecimal.valueOf(cancha.getPrecio() * 12), resultado.getPrecio());
        verify(repositorioTorneo).crearTorneo(torneo);
    }

    @Test
    void alCrearTorneoConDatosNulosDeberiaLanzarExcepcion() {
        Torneo torneoInvalido = new Torneo();

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                servicioTorneo.crearTorneo(torneoInvalido)
        );

        assertTrue(ex.getMessage().contains("Datos del torneo incompletos"));
        verify(repositorioTorneo, never()).crearTorneo(any());
    }

    @Test
    void alCrearTorneoConFechaPasadaDeberiaLanzarExcepcion() {
        torneo.setFecha(LocalDate.now().minusDays(1));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                servicioTorneo.crearTorneo(torneo)
        );

        assertTrue(ex.getMessage().contains("no puede ser nula ni pasada"));
    }

    @Test
    void alCrearTorneoConCanchaYaOcupadaDeberiaLanzarExcepcion() {
        when(repositorioTorneo.existeCanchaYFecha(cancha, torneo.getFecha())).thenReturn(true);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
                servicioTorneo.crearTorneo(torneo)
        );

        assertEquals("Ya existe un torneo en esa cancha y fecha.", ex.getMessage());
    }

    @Test
    void alCrearTorneoConReservasEnLaCanchaDeberiaLanzarExcepcion() {
        when(repositorioTorneo.existeCanchaYFecha(cancha, torneo.getFecha())).thenReturn(false);
        when(repositorioReserva.porCanchaYFecha(cancha, torneo.getFecha())).thenReturn(List.of(mock(Reserva.class)));

        IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
                servicioTorneo.crearTorneo(torneo)
        );

        assertEquals("La cancha ya tiene reservas en esa fecha.", ex.getMessage());
    }

    @Test
    void alActualizarTorneoDeberiaInvocarRepositorio() {
        servicioTorneo.actualizarTorneo(torneo);
        verify(repositorioTorneo).actualizarTorneo(torneo);
    }

    @Test
    void alListarTorneosDeberiaRetornarListaDelRepositorio() {
        when(repositorioTorneo.listarTorneos()).thenReturn(List.of(torneo));

        List<Torneo> resultado = servicioTorneo.listarTorneos();

        assertEquals(1, resultado.size());
        assertEquals(torneo, resultado.get(0));
        verify(repositorioTorneo).listarTorneos();
    }

    @Test
    void alListarTorneosDisponibles_DeberiaFiltrarPorFechaActual() {
        when(repositorioTorneo.torneoFuturo(any(LocalDate.class))).thenReturn(List.of(torneo));

        List<Torneo> resultado = servicioTorneo.listarTorneosDisponibles();

        assertFalse(resultado.isEmpty());
        verify(repositorioTorneo).torneoFuturo(any(LocalDate.class));
    }

    @Test
    void alObtenerPorIdDeberiaRetornarTorneoCorrecto() {
        when(repositorioTorneo.porId(10L)).thenReturn(torneo);

        Torneo resultado = servicioTorneo.obtenerPorId(10L);

        assertEquals(torneo, resultado);
        verify(repositorioTorneo).porId(10L);
    }

    @Test
    void alCancelarTorneoExistenteDeberiaActualizarEstadoACancelado() {
        when(repositorioTorneo.porId(10L)).thenReturn(torneo);

        servicioTorneo.cancelarTorneo(10L);

        assertEquals("CANCELADO", torneo.getEstado());
        verify(repositorioTorneo).crearTorneo(torneo);
    }

    @Test
    void alCancelarTorneoInexistenteDeberiaLanzarExcepcion() {
        when(repositorioTorneo.porId(99L)).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                servicioTorneo.cancelarTorneo(99L)
        );

        assertTrue(ex.getMessage().contains("No se encontró el torneo"));
    }

    @Test
    void alVerificarSiExisteTorneoEnFechaDeberiaRetornarValorDelRepositorio() {
        when(repositorioTorneo.existeCanchaYFecha(cancha, torneo.getFecha())).thenReturn(true);

        boolean existe = servicioTorneo.existeTorneoEnFecha(cancha, torneo.getFecha());

        assertTrue(existe);
        verify(repositorioTorneo).existeCanchaYFecha(cancha, torneo.getFecha());
    }
}