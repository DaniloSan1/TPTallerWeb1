package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ControladorReservaTest {

    private ServicioReserva servicioReserva;
    private ServicioHorario servicioHorario;
    private ServicioUsuario servicioUsuario;
    private ServicioPartido servicioPartido;
    private ControladorReserva controlador;

    private Horario horario;
    private Usuario usuario;
    private Cancha cancha;

    @BeforeEach
    public void setUp() {
        servicioReserva = mock(ServicioReserva.class);
        servicioHorario = mock(ServicioHorario.class);
        servicioUsuario = mock(ServicioUsuario.class);
        servicioPartido = mock(ServicioPartido.class);

        controlador = new ControladorReserva(servicioReserva, servicioHorario, servicioUsuario, servicioPartido);

        cancha = new Cancha();
        cancha.setId(1L);
        cancha.setNombre("Cancha 1");

        horario = new Horario();
        horario.setId(10L);
        horario.setCancha(cancha);
        horario.setHoraInicio(LocalTime.of(18, 0));

        usuario = new Usuario();
        usuario.setId(5L);
    }

    @Test
    public void AlMostrarUnFormularioDeReservaDeberiaCargarDatosEnElModelo() {
        when(servicioHorario.obtenerPorId(10L)).thenReturn(horario);

        ModelMap model = new ModelMap();
        String vista = controlador.mostrarFormularioReserva(5L, 10L, model);

        assertEquals("reservaForm", vista);
        assertEquals(5L, model.get("usuarioId"));
        assertEquals(10L, model.get("horarioId"));
        assertEquals(cancha, model.get("cancha"));
        assertEquals(horario, model.get("horario"));
    }

    @Test
    public void AlCrearUnaReservaConExitoDeberiaRedirigirADetalle() {
        when(servicioHorario.obtenerPorId(10L)).thenReturn(horario);
        when(servicioUsuario.buscarPorId(5L)).thenReturn(usuario);

        Reserva reserva = new Reserva(horario, usuario,
                LocalDate.now().atTime(horario.getHoraInicio()));
        reserva.setId(99L);

        when(servicioReserva.crearReserva(any())).thenReturn(reserva);

        ModelMap model = new ModelMap();
        String vista = controlador.crearReserva(
                10L,
                LocalDate.now().toString(),
                5L,
                "Título",
                "Descripción",
                Nivel.PRINCIPIANTE,
                model
        );

        assertTrue(vista.startsWith("redirect:/reserva/"));
        verify(servicioReserva).crearReserva(any(Reserva.class));
        verify(servicioPartido).crearDesdeReserva(eq(reserva), anyString(), anyString(), any(), anyInt(), eq(usuario));
    }

    @Test
    public void AlCrearUnaReservaConErrorDeberiaVolverAlFormulario() {
        when(servicioHorario.obtenerPorId(10L)).thenReturn(horario);
        when(servicioUsuario.buscarPorId(5L)).thenReturn(usuario);
        when(servicioReserva.crearReserva(any())).thenThrow(new RuntimeException("Error al crear"));

        ModelMap model = new ModelMap();
        String vista = controlador.crearReserva(
                10L,
                LocalDate.now().toString(),
                5L,
                "Título",
                "Descripción",
                Nivel.PRINCIPIANTE,
                model
        );

        assertEquals("reservaForm", vista);
        assertEquals("Error al crear", model.get("error"));
        assertEquals(horario, model.get("horario"));
        assertEquals(cancha, model.get("cancha"));
    }

    @Test
    public void AlCancelarUnaReservaConExitoDeberiaRedirigirHomeConMensajeExito() {
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        String vista = controlador.cancelarReserva(99L, 5L, 10L, redirectAttributes, new ModelMap());

        assertEquals("redirect:/home", vista);
        verify(servicioReserva).cancelarReserva(99L);
        verify(redirectAttributes).addFlashAttribute(eq("mensajeExito"), anyString());
    }

    @Test
    public void AlCancelarUnaReservaConErrorDeberiaRedirigirHomeConMensajeError() {
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        doThrow(new RuntimeException("No se pudo cancelar")).when(servicioReserva).cancelarReserva(99L);

        String vista = controlador.cancelarReserva(99L, 5L, 10L, redirectAttributes, new ModelMap());

        assertEquals("redirect:/home", vista);
        verify(redirectAttributes).addFlashAttribute("mensajeError", "No se pudo cancelar");
    }

    @Test
    public void AlVerUnDetalleDeReservaDeberiaCargarReservaEnElModelo() {
        Reserva reserva = new Reserva(horario, usuario, LocalDateTime.now());
        reserva.setId(77L);

        when(servicioReserva.obtenerReservaPorId(77L)).thenReturn(reserva);

        ModelMap model = new ModelMap();
        String vista = controlador.verDetalleReserva(77L, model);

        assertEquals("detalleReserva", vista);
        assertEquals(reserva, model.get("reserva"));
        assertEquals(horario, model.get("horario"));
        assertEquals(cancha, model.get("cancha"));
    }
}
