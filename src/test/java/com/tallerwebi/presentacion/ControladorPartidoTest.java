package com.tallerwebi.presentacion;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tallerwebi.dominio.Cancha;
import com.tallerwebi.dominio.Equipo;
import com.tallerwebi.dominio.Horario;
import com.tallerwebi.dominio.Nivel;
import com.tallerwebi.dominio.Partido;
import com.tallerwebi.dominio.Reserva;
import com.tallerwebi.dominio.ServicioEquipo;
import com.tallerwebi.dominio.ServicioLogin;
import com.tallerwebi.dominio.ServicioPartido;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.Zona;
import com.tallerwebi.dominio.excepcion.NoHayCupoEnPartido;
import com.tallerwebi.dominio.excepcion.PartidoNoEncontrado;
import com.tallerwebi.dominio.excepcion.YaExisteElParticipante;
import com.tallerwebi.dominio.excepcion.EquipoNoEncontrado;

public class ControladorPartidoTest {
    private ServicioPartido servicioPartidoMock;
    private ServicioLogin servicioLoginMock;
    private Partido partidoMock;
    private ControladorPartido controladorPartido;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    private Usuario usuarioMock;
    private Equipo equipoMock;
    private ServicioEquipo servicioEquipoMock;
    private RedirectAttributes redirectAttributesMock;

    @BeforeEach
    public void init() {
        requestMock = Mockito.mock(HttpServletRequest.class);
        sessionMock = Mockito.mock(javax.servlet.http.HttpSession.class);
        servicioLoginMock = Mockito.mock(ServicioLogin.class);
        servicioPartidoMock = Mockito.mock(ServicioPartido.class);
        servicioEquipoMock = Mockito.mock(ServicioEquipo.class);
        partidoMock = Mockito.mock(Partido.class);
        usuarioMock = Mockito.mock(Usuario.class);
        redirectAttributesMock = Mockito.mock(RedirectAttributes.class);
        equipoMock = Mockito.mock(Equipo.class);
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(requestMock.getHeader("referer")).thenReturn("/partidos/1");

        // Mock the necessary methods that will be called by DetallePartido constructor
        when(partidoMock.getId()).thenReturn(1L);
        when(partidoMock.getTitulo()).thenReturn("Partido de prueba");
        when(partidoMock.getZona()).thenReturn(Zona.NORTE);
        when(partidoMock.getNivel()).thenReturn(Nivel.INTERMEDIO);
        when(partidoMock.getFecha()).thenReturn(LocalDateTime.of(2025, 10, 5, 18, 0));
        when(partidoMock.getCupoMaximo()).thenReturn(10);
        when(partidoMock.getDescripcion()).thenReturn("Descripción del partido");
        when(partidoMock.getCreador()).thenReturn(usuarioMock);
        when(partidoMock.getReserva()).thenReturn(Mockito.mock(Reserva.class));
        when(usuarioMock.getEmail()).thenReturn("usuario1@email.com");
        when(usuarioMock.getId()).thenReturn(1L);
        when(usuarioMock.getNombre()).thenReturn("Usuario Creador");
        when(partidoMock.tieneCupo()).thenReturn(true);
        when(partidoMock.validarParticipanteExistente(Mockito.anyLong())).thenReturn(false);
        when(partidoMock.esCreador(Mockito.anyString())).thenReturn(true);

        // Create mocks for related objects
        Cancha canchaMock = Mockito.mock(Cancha.class);
        when(canchaMock.getId()).thenReturn(1L);
        when(canchaMock.getNombre()).thenReturn("Cancha 1");

        Usuario creadorMock = Mockito.mock(Usuario.class);
        when(creadorMock.getId()).thenReturn(1L);
        when(creadorMock.getNombre()).thenReturn("Usuario Creador");

        // Create mock for Reserva
        Reserva reservaMock = Mockito.mock(Reserva.class);
        when(reservaMock.getCancha()).thenReturn(canchaMock);
        when(reservaMock.getId()).thenReturn(1L);

        // Create mock for Horario
        Horario horarioMock = Mockito.mock(Horario.class);
        when(horarioMock.getId()).thenReturn(1L);
        when(reservaMock.getHorario()).thenReturn(horarioMock);

        // Set up the partido mock with all required relationships
        when(partidoMock.getReserva()).thenReturn(reservaMock);
        when(partidoMock.getCreador()).thenReturn(creadorMock);
        when(partidoMock.cuposDisponibles()).thenReturn(5);

        // Create mock for Equipo
        equipoMock = Mockito.mock(Equipo.class);
        when(equipoMock.getId()).thenReturn(1L);
        try {
            Mockito.when(servicioEquipoMock.buscarPorId(1L)).thenReturn(equipoMock);
        } catch (EquipoNoEncontrado e) {
            // Should not happen in mock
        }

        controladorPartido = new ControladorPartido(servicioPartidoMock, servicioLoginMock, null, null,
                servicioPartidoMock, null, servicioEquipoMock);
    }

    @Test
    public void detalleDeberiallevarALoginSiNoExisteEMAILEnSesion() {
        when(sessionMock.getAttribute("EMAIL")).thenReturn(null);

        ModelAndView modelAndView = controladorPartido.detalle(1L, requestMock);

        assertNotNull(modelAndView);
        assertEquals("redirect:/login", modelAndView.getViewName());
    }

    @Test
    public void detalleDeberiallevarADetallePartido() {
        when(sessionMock.getAttribute("EMAIL")).thenReturn("usuario1@email.com");
        Mockito.doReturn(partidoMock).when(servicioPartidoMock).obtenerPorId(Mockito.any(Long.class));
        when(servicioLoginMock.buscarPorEmail(Mockito.anyString())).thenReturn(usuarioMock);

        ModelAndView modelAndView = controladorPartido.detalle(1L, requestMock);

        assertNotNull(modelAndView);
        assertEquals("detalle-partido", modelAndView.getViewName());

        DetallePartido partidoEnModelo = (DetallePartido) modelAndView.getModel().get("partido");
        assertNotNull(partidoEnModelo);
        assertEquals(partidoMock.getId(), partidoEnModelo.getId());
        assertEquals(partidoMock.getTitulo(), partidoEnModelo.getTitulo());
        assertEquals(partidoMock.getDescripcion(), partidoEnModelo.getDescripcion());
        assertEquals(partidoMock.getZona(), partidoEnModelo.getZona());
        assertEquals(partidoMock.getNivel(), partidoEnModelo.getNivel());
        assertEquals(partidoMock.getCupoMaximo(), partidoEnModelo.getCupoMaximo());
        assertEquals(partidoMock.getCreador().getNombreCompleto(), partidoEnModelo.getCreador());
        assertEquals(partidoMock.getReserva().getCancha().getNombre(), partidoEnModelo.getCancha());
        assertEquals(partidoMock.getFecha(), partidoEnModelo.getFecha());
        assertEquals(partidoMock.tieneCupo(), partidoEnModelo.getHayCupo());
        assertEquals(partidoMock.validarParticipanteExistente(usuarioMock.getId()), partidoEnModelo.getYaParticipa());
        assertEquals(partidoMock.esCreador(usuarioMock.getEmail()), partidoEnModelo.getEsCreador());
        assertNull(modelAndView.getModel().get("error"));
    }

    @Test
    public void detalleDeberiallevarADetallePartidoYDevolverUnErrorAlNoEncontrarElPartido() {
        when(sessionMock.getAttribute("EMAIL")).thenReturn("usuario1@email.com");
        when(servicioLoginMock.buscarPorEmail(Mockito.anyString())).thenReturn(usuarioMock);
        when(servicioPartidoMock.obtenerPorId(Mockito.anyLong()))
                .thenThrow(new PartidoNoEncontrado());

        ModelAndView modelAndView = controladorPartido.detalle(1L, requestMock);

        assertNotNull(modelAndView);
        assertEquals("detalle-partido", modelAndView.getViewName());
        assertNotNull(modelAndView.getModel().get("error"));
        assertNull(modelAndView.getModel().get("partido"));
    }

    @Test
    public void inscripcionDeberiaDarErrorSiNoExisteElPartido() throws Exception {
        Long partidoId = 1L;
        Mockito.when(sessionMock.getAttribute("EMAIL")).thenReturn("usuario1@email.com");
        Mockito.when(servicioLoginMock.buscarPorEmail("usuario1@email.com")).thenReturn(usuarioMock);
        Mockito.doReturn(partidoMock).when(servicioPartidoMock).obtenerPorId(1L);
        Mockito.doThrow(new PartidoNoEncontrado())
                .when(servicioPartidoMock)
                .anotarParticipante(Mockito.any(Partido.class), Mockito.any(Equipo.class), Mockito.any(Usuario.class));

        ModelAndView respuesta = controladorPartido.inscripcion(partidoId, 1L, requestMock, redirectAttributesMock);
        assertEquals("redirect:/partidos/1", respuesta.getViewName());

        Mockito.verify(redirectAttributesMock).addFlashAttribute("error", "Ocurrió un error al intentar inscribirte.");
        Mockito.verify(servicioPartidoMock, Mockito.times(1)).anotarParticipante(Mockito.any(Partido.class),
                Mockito.any(Equipo.class), Mockito.any(Usuario.class));
    }

    @Test
    public void inscripcionDeberiaDarErrorSiNoHayCupoEnElPartido() throws Exception {
        Long partidoId = 1L;
        Mockito.when(sessionMock.getAttribute("EMAIL")).thenReturn("usuario1@email.com");
        Mockito.when(servicioLoginMock.buscarPorEmail("usuario1@email.com")).thenReturn(usuarioMock);
        Mockito.doReturn(partidoMock).when(servicioPartidoMock).obtenerPorId(1L);
        Mockito.doThrow(new NoHayCupoEnPartido())
                .when(servicioPartidoMock)
                .anotarParticipante(Mockito.any(Partido.class), Mockito.any(Equipo.class), Mockito.any(Usuario.class));

        ModelAndView respuesta = controladorPartido.inscripcion(partidoId, 1L, requestMock, redirectAttributesMock);
        assertEquals("redirect:/partidos/1", respuesta.getViewName());

        Mockito.verify(redirectAttributesMock).addFlashAttribute("error", "Ocurrió un error al intentar inscribirte.");
        Mockito.verify(servicioPartidoMock, Mockito.times(1)).anotarParticipante(Mockito.any(Partido.class),
                Mockito.any(Equipo.class), Mockito.any(Usuario.class));
    }

    @Test
    public void inscripcionDeberiaDarErrorSiYaEstaInscriptoElParticipante() throws Exception {
        Long partidoId = 1L;
        Mockito.when(sessionMock.getAttribute("EMAIL")).thenReturn("usuario1@email.com");
        Mockito.when(servicioLoginMock.buscarPorEmail("usuario1@email.com")).thenReturn(usuarioMock);
        Mockito.doReturn(partidoMock).when(servicioPartidoMock).obtenerPorId(1L);
        Mockito.doThrow(new YaExisteElParticipante())
                .when(servicioPartidoMock)
                .anotarParticipante(Mockito.any(Partido.class), Mockito.any(Equipo.class), Mockito.any(Usuario.class));

        ModelAndView respuesta = controladorPartido.inscripcion(partidoId, 1L, requestMock, redirectAttributesMock);
        assertEquals("redirect:/partidos/1", respuesta.getViewName());

        Mockito.verify(redirectAttributesMock).addFlashAttribute("error", "Ocurrió un error al intentar inscribirte.");
        Mockito.verify(servicioPartidoMock, Mockito.times(1)).anotarParticipante(Mockito.any(Partido.class),
                Mockito.any(Equipo.class), Mockito.any(Usuario.class));
    }

    @Test
    public void inscripcionDeberiaLlamarAlServicioParaAnotarParticipante() throws Exception {
        Long partidoId = 1L;
        Mockito.when(sessionMock.getAttribute("EMAIL")).thenReturn("usuario1@email.com");
        Mockito.when(servicioLoginMock.buscarPorEmail("usuario1@email.com")).thenReturn(usuarioMock);
        Mockito.doReturn(partidoMock).when(servicioPartidoMock).obtenerPorId(1L);
        Mockito.when(servicioPartidoMock.anotarParticipante(Mockito.any(Partido.class), Mockito.any(Equipo.class),
                Mockito.any(Usuario.class))).thenReturn(partidoMock);

        ModelAndView respuesta = controladorPartido.inscripcion(partidoId, 1L, requestMock, redirectAttributesMock);
        assertEquals("redirect:/partidos/1", respuesta.getViewName());

        Mockito.verify(redirectAttributesMock).addFlashAttribute("success", "Te has unido al partido correctamente.");
        Mockito.verify(servicioPartidoMock, Mockito.times(1)).anotarParticipante(Mockito.any(Partido.class),
                Mockito.any(Equipo.class), Mockito.any(Usuario.class));
    }
}
