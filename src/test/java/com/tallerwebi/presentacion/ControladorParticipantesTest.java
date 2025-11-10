package com.tallerwebi.presentacion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tallerwebi.dominio.Equipo;
import com.tallerwebi.dominio.ServicioEquipo;
import com.tallerwebi.dominio.ServicioEquipoJugador;
import com.tallerwebi.dominio.excepcion.ParticipanteNoEncontrado;

public class ControladorParticipantesTest {
    private ServicioEquipoJugador servicioEquipoJugadorMock;
    private ControladorParticipantes controladorParticipantes;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    private RedirectAttributes redirectAttributesMock;
    private ServicioEquipo servicioEquipoMock;
    private Equipo equipoMock;

    @BeforeEach
    public void init() {
        servicioEquipoJugadorMock = Mockito.mock(ServicioEquipoJugador.class);
        servicioEquipoMock = Mockito.mock(ServicioEquipo.class);
        equipoMock = Mockito.mock(Equipo.class);
        controladorParticipantes = new ControladorParticipantes(servicioEquipoMock, servicioEquipoJugadorMock);
        requestMock = Mockito.mock(HttpServletRequest.class);
        sessionMock = Mockito.mock(HttpSession.class);
        redirectAttributesMock = Mockito.mock(RedirectAttributes.class);
        when(requestMock.getSession()).thenReturn(sessionMock);
    }

    @Test
    public void asignarEquipoDeberiaRedirigirALoginSiNoHayEmailEnSesion() throws Exception {
        when(sessionMock.getAttribute("EMAIL")).thenReturn(null);

        String result = controladorParticipantes.asignarEquipo(1L, 1L, redirectAttributesMock,
                requestMock);

        assertEquals("redirect:/login", result);
    }

    @Test
    public void asignarEquipoDeberiaRedirigirConExitoSiAsignacionCorrecta() throws Exception {
        when(sessionMock.getAttribute("EMAIL")).thenReturn("usuario@email.com");
        when(requestMock.getHeader("referer")).thenReturn("http://localhost/spring/partido/1");
        when(servicioEquipoMock.buscarPorId(1L)).thenReturn(equipoMock);

        String result = controladorParticipantes.asignarEquipo(1L, 1L, redirectAttributesMock,
                requestMock);

        assertEquals("redirect:http://localhost/spring/partido/1", result);
        verify(servicioEquipoJugadorMock).actualizarEquipo(1L, equipoMock);
        verify(redirectAttributesMock).addFlashAttribute("listaParticipantesSuccess", "Equipo asignado correctamente");
    }

    @Test
    public void asignarEquipoDeberiaRedirigirAHomeSiNoHayReferrer() throws Exception {
        when(sessionMock.getAttribute("EMAIL")).thenReturn("usuario@email.com");
        when(requestMock.getHeader("referer")).thenReturn(null);
        when(servicioEquipoMock.buscarPorId(1L)).thenReturn(equipoMock);

        String result = controladorParticipantes.asignarEquipo(1L, 1L, redirectAttributesMock,
                requestMock);

        assertEquals("redirect:/home", result);
        verify(servicioEquipoJugadorMock).actualizarEquipo(1L, equipoMock);
        verify(redirectAttributesMock).addFlashAttribute("listaParticipantesSuccess", "Equipo asignado correctamente");
    }

    @Test
    public void asignarEquipoDeberiaRedirigirConErrorSiOcurreExcepcion() throws Exception {
        when(sessionMock.getAttribute("EMAIL")).thenReturn("usuario@email.com");
        when(requestMock.getHeader("referer")).thenReturn("http://localhost/partido/1");
        when(servicioEquipoMock.buscarPorId(1L)).thenReturn(equipoMock);
        doThrow(new RuntimeException("Error")).when(servicioEquipoJugadorMock).actualizarEquipo(1L, equipoMock);

        String result = controladorParticipantes.asignarEquipo(1L, 1L, redirectAttributesMock,
                requestMock);

        assertEquals("redirect:http://localhost/partido/1", result);
        verify(redirectAttributesMock).addFlashAttribute("listaParticipantesError", "Error al asignar el equipo");
    }

    @Test
    public void eliminarParticipanteDeberiaRedirigirALoginSiNoHayEmailEnSesion() throws Exception {
        when(sessionMock.getAttribute("EMAIL")).thenReturn(null);

        String result = controladorParticipantes.eliminarParticipante(1L, redirectAttributesMock, requestMock);

        assertEquals("redirect:/login", result);
    }

    @Test
    public void eliminarParticipanteDeberiaRedirigirConExitoSiEliminacionCorrecta() throws Exception {
        when(sessionMock.getAttribute("EMAIL")).thenReturn("usuario@email.com");
        when(requestMock.getHeader("referer")).thenReturn("http://localhost/spring/partido/1");
        String result = controladorParticipantes.eliminarParticipante(1L, redirectAttributesMock, requestMock);
        assertEquals("redirect:http://localhost/spring/partido/1", result);
        verify(servicioEquipoJugadorMock).eliminarPorId(1L);
        verify(redirectAttributesMock).addFlashAttribute("listaParticipantesSuccess",
                "Participante eliminado correctamente");
    }

    @Test
    public void eliminarParticipanteDeberiaRedirigirAHomeSiNoHayReferrer() throws Exception {
        when(sessionMock.getAttribute("EMAIL")).thenReturn("usuario@email.com");
        when(requestMock.getHeader("referer")).thenReturn(null);

        String result = controladorParticipantes.eliminarParticipante(1L, redirectAttributesMock, requestMock);

        assertEquals("redirect:/home", result);
        verify(servicioEquipoJugadorMock).eliminarPorId(1L);
        verify(redirectAttributesMock).addFlashAttribute("listaParticipantesSuccess",
                "Participante eliminado correctamente");
    }

    @Test
    public void eliminarParticipanteDeberiaRedirigirConErrorSiOcurreExcepcion() throws Exception {
        when(sessionMock.getAttribute("EMAIL")).thenReturn("usuario@email.com");
        when(requestMock.getHeader("referer")).thenReturn("http://localhost/partido/1");
        doThrow(new RuntimeException("Error")).when(servicioEquipoJugadorMock).eliminarPorId(1L);

        String result = controladorParticipantes.eliminarParticipante(1L, redirectAttributesMock, requestMock);

        assertEquals("redirect:http://localhost/partido/1", result);
        verify(redirectAttributesMock).addFlashAttribute("listaParticipantesError",
                "Error al eliminar el participante");
    }

    @Test
    public void promoverCapitanDeberiaRedirigirALoginSiNoHayEmailEnSesion() {
        when(sessionMock.getAttribute("EMAIL")).thenReturn(null);

        String result = controladorParticipantes.promoverCapitan(1L, redirectAttributesMock, requestMock);

        assertEquals("redirect:/login", result);
    }

    @Test
    public void promoverCapitanDeberiaRedirigirConExitoSiPromocionCorrecta() {
        when(sessionMock.getAttribute("EMAIL")).thenReturn("usuario@email.com");
        when(requestMock.getHeader("referer")).thenReturn("http://localhost/spring/partido/1");

        String result = controladorParticipantes.promoverCapitan(1L, redirectAttributesMock, requestMock);

        assertEquals("redirect:http://localhost/spring/partido/1", result);
        verify(servicioEquipoJugadorMock).promoverCapitan(1L);
        verify(redirectAttributesMock).addFlashAttribute("listaParticipantesSuccess",
                "Capitán promovido correctamente");
    }

    @Test
    public void promoverCapitanDeberiaRedirigirAHomeSiNoHayReferrer() {
        when(sessionMock.getAttribute("EMAIL")).thenReturn("usuario@email.com");
        when(requestMock.getHeader("referer")).thenReturn(null);

        String result = controladorParticipantes.promoverCapitan(1L, redirectAttributesMock, requestMock);

        assertEquals("redirect:/home", result);
        verify(servicioEquipoJugadorMock).promoverCapitan(1L);
        verify(redirectAttributesMock).addFlashAttribute("listaParticipantesSuccess",
                "Capitán promovido correctamente");
    }

    @Test
    public void promoverCapitanDeberiaRedirigirConErrorSiParticipanteNoEncontrado() {
        when(sessionMock.getAttribute("EMAIL")).thenReturn("usuario@email.com");
        when(requestMock.getHeader("referer")).thenReturn("http://localhost/partido/1");
        doThrow(new ParticipanteNoEncontrado()).when(servicioEquipoJugadorMock).promoverCapitan(1L);

        String result = controladorParticipantes.promoverCapitan(1L, redirectAttributesMock, requestMock);

        assertEquals("redirect:http://localhost/partido/1", result);
        verify(redirectAttributesMock).addFlashAttribute("listaParticipantesError", "No se encontró el participante");
    }

    @Test
    public void promoverCapitanDeberiaRedirigirConErrorSiOcurreExcepcionGeneral() {
        when(sessionMock.getAttribute("EMAIL")).thenReturn("usuario@email.com");
        when(requestMock.getHeader("referer")).thenReturn("http://localhost/partido/1");
        doThrow(new RuntimeException("Error general")).when(servicioEquipoJugadorMock).promoverCapitan(1L);

        String result = controladorParticipantes.promoverCapitan(1L, redirectAttributesMock, requestMock);

        assertEquals("redirect:http://localhost/partido/1", result);
        verify(redirectAttributesMock).addFlashAttribute("listaParticipantesError", "Error al promover al capitán");
    }
}