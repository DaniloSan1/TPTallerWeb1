package com.tallerwebi.presentacion;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tallerwebi.dominio.ServicioPartidoParticipante;

public class ControladorParticipantesTest {
    private ServicioPartidoParticipante servicioPartidoParticipanteMock;
    private ControladorParticipantes controladorParticipantes;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    private RedirectAttributes redirectAttributesMock;

    @BeforeEach
    public void init() {
        servicioPartidoParticipanteMock = Mockito.mock(ServicioPartidoParticipante.class);
        controladorParticipantes = new ControladorParticipantes(servicioPartidoParticipanteMock);
        requestMock = Mockito.mock(HttpServletRequest.class);
        sessionMock = Mockito.mock(HttpSession.class);
        redirectAttributesMock = Mockito.mock(RedirectAttributes.class);
        when(requestMock.getSession()).thenReturn(sessionMock);
    }

    @Test
    public void asignarEquipoDeberiaRedirigirALoginSiNoHayEmailEnSesion() throws Exception {
        when(sessionMock.getAttribute("EMAIL")).thenReturn(null);

        String result = controladorParticipantes.asignarEquipo(1L, "EQUIPO_1", redirectAttributesMock, requestMock);

        assertEquals("redirect:/login", result);
    }

    @Test
    public void asignarEquipoDeberiaRedirigirConExitoSiAsignacionCorrecta() throws Exception {
        when(sessionMock.getAttribute("EMAIL")).thenReturn("usuario@email.com");
        when(requestMock.getHeader("referer")).thenReturn("http://localhost/spring/partido/1");

        String result = controladorParticipantes.asignarEquipo(1L, "EQUIPO_1", redirectAttributesMock, requestMock);

        assertEquals("redirect:http://localhost/spring/partido/1", result);
        verify(servicioPartidoParticipanteMock).actualizarEquipo(1L, "EQUIPO_1");
        verify(redirectAttributesMock).addFlashAttribute("listaParticipantesSuccess", "Equipo asignado correctamente");
    }

    @Test
    public void asignarEquipoDeberiaRedirigirAHomeSiNoHayReferrer() throws Exception {
        when(sessionMock.getAttribute("EMAIL")).thenReturn("usuario@email.com");
        when(requestMock.getHeader("referer")).thenReturn(null);

        String result = controladorParticipantes.asignarEquipo(1L, "EQUIPO_1", redirectAttributesMock, requestMock);

        assertEquals("redirect:/home", result);
        verify(servicioPartidoParticipanteMock).actualizarEquipo(1L, "EQUIPO_1");
        verify(redirectAttributesMock).addFlashAttribute("listaParticipantesSuccess", "Equipo asignado correctamente");
    }

    @Test
    public void asignarEquipoDeberiaRedirigirConErrorSiOcurreExcepcion() throws Exception {
        when(sessionMock.getAttribute("EMAIL")).thenReturn("usuario@email.com");
        when(requestMock.getHeader("referer")).thenReturn("http://localhost/partido/1");
        doThrow(new RuntimeException("Error")).when(servicioPartidoParticipanteMock).actualizarEquipo(1L, "EQUIPO_1");

        String result = controladorParticipantes.asignarEquipo(1L, "EQUIPO_1", redirectAttributesMock, requestMock);

        assertEquals("redirect:http://localhost/partido/1", result);
        verify(redirectAttributesMock).addFlashAttribute("listaParticipantesError", "Error al asignar el equipo");
    }
}