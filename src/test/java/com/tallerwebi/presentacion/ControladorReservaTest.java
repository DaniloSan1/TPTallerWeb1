package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
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
    private ServicioPago servicioPago;
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
        servicioPago = mock(ServicioPago.class);

        controlador = new ControladorReserva(servicioReserva, servicioHorario, servicioUsuario, servicioPartido, servicioPago);

        cancha = new Cancha();
        cancha.setId(1L);
        cancha.setNombre("Cancha 1");
        cancha.setPrecio(100.0);

        horario = new Horario();
        horario.setId(10L);
        horario.setCancha(cancha);
        horario.setHoraInicio(LocalTime.of(18, 0));

        usuario = new Usuario();
        usuario.setId(5L);
    }

    @Test
    public void AlCrearReservaConExito_DeberiaMostrarDetalleYCrearPago() throws Exception {
        // Arrange
        when(servicioHorario.obtenerPorId(10L)).thenReturn(horario);
        when(servicioUsuario.buscarPorId(5L)).thenReturn(usuario);

        Reserva reserva = new Reserva(horario, usuario, LocalDate.now().atTime(horario.getHoraInicio()));
        reserva.setId(99L);

        when(servicioReserva.crearReserva(any())).thenReturn(reserva);
        when(servicioPago.crearPago(anyString(), anyString(), any(BigDecimal.class), eq(99L)))
                .thenReturn("pref-12345");

        // Act
        ModelAndView mav = controlador.crearReserva(
                10L,
                LocalDate.now().toString(),
                5L,
                "Título",
                "Descripción",
                Nivel.PRINCIPIANTE
        );

        // Assert
        assertEquals("detalleReserva", mav.getViewName());
        assertEquals(reserva, mav.getModel().get("reserva"));
        assertEquals(cancha, mav.getModel().get("cancha"));
        assertEquals(horario, mav.getModel().get("horario"));
        assertEquals("pref-12345", mav.getModel().get("preferenceId"));
        assertTrue(((String) mav.getModel().get("mensajeExito")).contains("Completá el pago"));

        verify(servicioReserva).crearReserva(any(Reserva.class));
        verify(servicioPartido).crearDesdeReserva(eq(reserva), anyString(), anyString(), any(), anyInt(), eq(usuario));
        verify(servicioPago).guardarPago(eq(reserva), eq(usuario), eq("pref-12345"), eq(100.0));
    }

    @Test
    public void AlFallarLaCreacionDeReserva_DeberiaVolverAlFormularioConError() throws Exception {
        when(servicioHorario.obtenerPorId(10L)).thenReturn(horario);
        when(servicioUsuario.buscarPorId(5L)).thenReturn(usuario);
        when(servicioReserva.crearReserva(any())).thenThrow(new RuntimeException("Fallo interno"));

        ModelAndView mav = controlador.crearReserva(
                10L,
                LocalDate.now().toString(),
                5L,
                "Título",
                "Descripción",
                Nivel.INTERMEDIO
        );

        assertEquals("reservaForm", mav.getViewName());
        assertTrue(((String) mav.getModel().get("error")).contains("Fallo interno"));
    }

    @Test
    public void AlVerDetalleReserva_DeberiaMostrarInformacionCorrecta() {
        Reserva reserva = new Reserva(horario, usuario, LocalDateTime.now());
        reserva.setId(42L);
        when(servicioReserva.obtenerReservaPorId(42L)).thenReturn(reserva);

        ModelMap model = new ModelMap();
        String vista = controlador.verDetalleReserva(42L, model);

        assertEquals("detalleReserva", vista);
        assertEquals(reserva, model.get("reserva"));
        assertEquals(cancha, model.get("cancha"));
        assertEquals(horario, model.get("horario"));
    }

    @Test
    public void AlCancelarReservaConExito_DeberiaRedirigirHomeConMensaje() {
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        String vista = controlador.cancelarReserva(88L, 5L, 10L, redirectAttributes);

        assertEquals("redirect:/home", vista);
        verify(servicioReserva).cancelarReserva(88L);
        verify(redirectAttributes).addFlashAttribute(eq("mensajeExito"), anyString());
    }

    @Test
    public void AlCancelarReservaConError_DeberiaMostrarMensajeDeError() {
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        doThrow(new RuntimeException("No se pudo cancelar")).when(servicioReserva).cancelarReserva(88L);

        String vista = controlador.cancelarReserva(88L, 5L, 10L, redirectAttributes);

        assertEquals("redirect:/home", vista);
        verify(redirectAttributes).addFlashAttribute("mensajeError", "No se pudo cancelar");
    }
}
