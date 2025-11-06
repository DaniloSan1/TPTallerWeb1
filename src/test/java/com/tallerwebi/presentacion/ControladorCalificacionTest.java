package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ControladorCalificacionTest {

    private ServicioCalificacion servicioCalificacion;
    private ServicioPartido servicioPartido;
    private ServicioUsuario servicioUsuario;
    private ControladorCalificacion controlador;

    private Usuario calificador;
    private Usuario calificado;
    private Partido partido;
    private HttpSession session;
    private RedirectAttributes redirectAttributes;

    @BeforeEach
    public void setUp() {
        servicioCalificacion = mock(ServicioCalificacion.class);
        servicioPartido = mock(ServicioPartido.class);
        servicioUsuario = mock(ServicioUsuario.class);

        controlador = new ControladorCalificacion(servicioCalificacion, servicioPartido, servicioUsuario);

        calificador = new Usuario();
        calificador.setId(1L);

        calificado = new Usuario();
        calificado.setId(2L);

        partido = new Partido();
        partido.setId(3L);

        session = mock(HttpSession.class);
        redirectAttributes = mock(RedirectAttributes.class);
    }

    @Test
    public void AlIntentarCalificarSinUsuarioEnSesionDeberiaRedirigirALoginConError() {
        when(session.getAttribute("USUARIO")).thenReturn(null);

        String vista = controlador.calificarJugador(
                3L, 2L, 5, "Buen partido", session, redirectAttributes);

        assertEquals("redirect:/login", vista);
        verify(redirectAttributes).addFlashAttribute("error", "Debes iniciar sesión para calificar.");
        verifyNoInteractions(servicioCalificacion);
    }

    @Test
    public void AlCalificarJugadorConExitoDeberiaRedirigirAlPartidoConMensajeExito() {
        when(session.getAttribute("USUARIO")).thenReturn(calificador);
        when(servicioPartido.obtenerPorId(3L)).thenReturn(partido);
        when(servicioUsuario.buscarPorId(2L)).thenReturn(calificado);

        String vista = controlador.calificarJugador(
                3L, 2L, 4, "Buen partido", session, redirectAttributes);

        assertEquals("redirect:/partidos/3", vista);
        verify(servicioCalificacion).calificarJugador(calificador, calificado, partido, 4, "Buen partido");
        verify(redirectAttributes).addFlashAttribute("success", "Jugador calificado correctamente.");
    }

    @Test
    public void AlFallarLaCalificacionDeberiaRedirigirAlPartidoConMensajeError() {
        when(session.getAttribute("USUARIO")).thenReturn(calificador);
        when(servicioPartido.obtenerPorId(3L)).thenReturn(partido);
        when(servicioUsuario.buscarPorId(2L)).thenReturn(calificado);

        doThrow(new IllegalArgumentException("Ya calificaste a este jugador"))
                .when(servicioCalificacion).calificarJugador(calificador, calificado, partido, 5, "Repetido");

        String vista = controlador.calificarJugador(
                3L, 2L, 5, "Repetido", session, redirectAttributes);

        assertEquals("redirect:/partidos/3", vista);
        verify(redirectAttributes).addFlashAttribute("error", "Ya calificaste a este jugador");
    }
}