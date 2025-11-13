package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.UsuarioNoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ControladorTorneoTest {

    private ServicioTorneo servicioTorneo;
    private ServicioCancha servicioCancha;
    private ServicioLogin servicioLogin;
    private ServicioPago servicioPago;
    private ServicioInscripcion servicioInscripcion;
    private ServicioEquipo servicioEquipo;
    private ControladorTorneo controlador;

    private HttpServletRequest request;
    private RedirectAttributes redirectAttributes;

    private Torneo torneo;
    private Cancha cancha;
    private Usuario usuario;

    @BeforeEach
    public void setUp() {
        servicioTorneo = mock(ServicioTorneo.class);
        servicioCancha = mock(ServicioCancha.class);
        servicioLogin = mock(ServicioLogin.class);
        servicioPago = mock(ServicioPago.class);
        servicioInscripcion = mock(ServicioInscripcion.class);
        servicioEquipo = mock(ServicioEquipo.class);

        controlador = new ControladorTorneo(
                servicioTorneo,
                servicioCancha,
                servicioLogin,
                servicioPago,
                servicioInscripcion,
                servicioEquipo
        );

        cancha = new Cancha();
        cancha.setId(1L);
        cancha.setNombre("Cancha 1");

        usuario = new Usuario();
        usuario.setId(10L);
        usuario.setNombre("Juan");

        torneo = new Torneo();
        torneo.setId(5L);
        torneo.setCancha(cancha);
        torneo.setNombre("Torneo Test");
        torneo.setPrecio(BigDecimal.valueOf(500));

        request = mock(HttpServletRequest.class);
        redirectAttributes = mock(RedirectAttributes.class);
    }

    @Test
    public void AlMostrarFormularioDeberiaCargarCanchaYModelo() {
        when(servicioCancha.obtenerCanchaPorId(1L)).thenReturn(cancha);

        ModelAndView mav = controlador.mostrarFormulario(1L);

        assertEquals("formulario-torneo", mav.getViewName());
        assertTrue(mav.getModel().containsKey("torneo"));
        assertEquals(cancha, mav.getModel().get("cancha"));
    }

    @Test
    public void AlCrearTorneoConExitoDeberiaMostrarDetalleYCrearPago() throws Exception {
        when(request.getSession()).thenReturn(mock(javax.servlet.http.HttpSession.class));
        when(request.getSession().getAttribute("EMAIL")).thenReturn("juan@test.com");

        when(servicioLogin.buscarPorEmail("juan@test.com")).thenReturn(usuario);
        when(servicioCancha.obtenerCanchaPorId(1L)).thenReturn(cancha);
        when(servicioTorneo.crearTorneo(any(Torneo.class))).thenReturn(torneo);
        when(servicioPago.crearPago(anyString(), anyString(), any(BigDecimal.class), eq(5L)))
                .thenReturn("pref-123");

        ModelAndView mav = controlador.crearTorneo(1L, "Torneo Test", LocalDate.now().toString(), request);

        assertEquals("detalleTorneo", mav.getViewName());
        assertEquals(torneo, mav.getModel().get("torneo"));
        assertEquals(cancha, mav.getModel().get("cancha"));
        assertEquals("pref-123", mav.getModel().get("preferenceId"));
        assertTrue(((String) mav.getModel().get("mensajeExito")).contains("Torneo creado"));

        verify(servicioTorneo).crearTorneo(any(Torneo.class));
        verify(servicioPago).crearPago(anyString(), anyString(), any(BigDecimal.class), eq(5L));
        verify(servicioPago).guardarPagoTorneo(eq(torneo), eq(usuario), eq("pref-123"), anyDouble());
    }

    @Test
    public void AlFallarCreacionDeTorneoDeberiaVolverAlFormularioConError() throws Exception {
        when(request.getSession()).thenReturn(mock(javax.servlet.http.HttpSession.class));
        when(request.getSession().getAttribute("EMAIL")).thenReturn("juan@test.com");

        when(servicioLogin.buscarPorEmail("juan@test.com")).thenReturn(usuario);
        when(servicioCancha.obtenerCanchaPorId(1L)).thenReturn(cancha);
        when(servicioTorneo.crearTorneo(any(Torneo.class))).thenThrow(new RuntimeException("Error interno"));

        ModelAndView mav = controlador.crearTorneo(1L, "Torneo Test", LocalDate.now().toString(), request);

        assertEquals("formulario-torneo", mav.getViewName());
        assertTrue(((String) mav.getModel().get("mensajeError")).contains("Error interno"));
    }

    @Test
    public void AlListarTorneosDisponiblesDeberiaDevolverListaEnModelo() {
        when(servicioTorneo.listarTorneosDisponibles()).thenReturn(List.of(torneo));

        ModelAndView mav = controlador.listarTorneos();

        assertEquals("torneos-disponibles", mav.getViewName());
        assertEquals(1, ((List<?>) mav.getModel().get("torneos")).size());
    }

    @Test
    public void AlVerDetalleTorneoDeberiaMostrarInformacionCorrecta() {
        when(servicioTorneo.obtenerPorId(5L)).thenReturn(torneo);

        ModelMap model = new ModelMap();
        String vista = controlador.verDetalleTorneo(5L, model);

        assertEquals("detalleTorneo", vista);
        assertEquals(torneo, model.get("torneo"));
        assertEquals(cancha, model.get("cancha"));
    }

    @Test
    public void AlCancelarTorneoConExitoDeberiaRedirigirHomeConMensajeExito() {
        String vista = controlador.cancelarTorneo(5L, redirectAttributes);

        assertEquals("redirect:/home", vista);
        verify(servicioTorneo).cancelarTorneo(5L);
        verify(redirectAttributes).addFlashAttribute(eq("mensajeExito"), anyString());
    }

    @Test
    public void AlCancelarTorneoConErrorDeberiaMostrarMensajeError() {
        doThrow(new RuntimeException("No se pudo cancelar")).when(servicioTorneo).cancelarTorneo(5L);

        String vista = controlador.cancelarTorneo(5L, redirectAttributes);

        assertEquals("redirect:/home", vista);
        verify(redirectAttributes).addFlashAttribute("mensajeError", "No se pudo cancelar");
    }

    @Test
    public void AlInscribirEquipoConExitoDeberiaMostrarMensajeExito() throws Exception {
        when(request.getSession()).thenReturn(mock(javax.servlet.http.HttpSession.class));
        when(request.getSession().getAttribute("EMAIL")).thenReturn("juan@test.com");
        when(servicioLogin.buscarPorEmail("juan@test.com")).thenReturn(usuario);

        String vista = controlador.inscribirEquipo(5L, 9L, request, redirectAttributes);

        assertEquals("redirect:/torneos/disponible/5", vista);
        verify(servicioInscripcion).inscribirEquipo(5L, 9L);
        verify(redirectAttributes).addFlashAttribute(eq("mensajeExito"), anyString());
    }

    @Test
    public void AlInscribirEquipoConErrorDeberiaMostrarMensajeError() throws Exception {
        when(request.getSession()).thenReturn(mock(javax.servlet.http.HttpSession.class));
        when(request.getSession().getAttribute("EMAIL")).thenReturn("juan@test.com");
        when(servicioLogin.buscarPorEmail("juan@test.com")).thenReturn(usuario);

        doThrow(new RuntimeException("Error al inscribir")).when(servicioInscripcion).inscribirEquipo(5L, 9L);

        String vista = controlador.inscribirEquipo(5L, 9L, request, redirectAttributes);

        assertEquals("redirect:/torneos/disponible/5", vista);
        verify(redirectAttributes).addFlashAttribute("mensajeError", "Error al inscribir");
    }

    @Test
    public void AlCancelarInscripcionConExitoDeberiaMostrarMensajeExito() {
        String vista = controlador.cancelarInscripcion(5L, 99L, redirectAttributes);

        assertEquals("redirect:/torneos/disponible/5", vista);
        verify(servicioInscripcion).cancelarInscripcion(99L);
        verify(redirectAttributes).addFlashAttribute(eq("mensajeExito"), anyString());
    }

    @Test
    public void AlCancelarInscripcionConErrorDeberiaMostrarMensajeError() {
        doThrow(new RuntimeException("Error al cancelar")).when(servicioInscripcion).cancelarInscripcion(99L);

        String vista = controlador.cancelarInscripcion(5L, 99L, redirectAttributes);

        assertEquals("redirect:/torneos/disponible/5", vista);
        verify(redirectAttributes).addFlashAttribute("mensajeError", "Error al cancelar");
    }
}
