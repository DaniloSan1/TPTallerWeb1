package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.UsuarioNoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

class ControladorEquiposTest {

    @Mock
    private ServicioEquipo servicioEquipoMock;

    @Mock
    private ServicioLogin servicioLoginMock;

    @Mock
    private ServicioEquipoJugador servicioEquipoJugadorMock;

    @Mock
    private ServicioPartido servicioPartidoMock;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @InjectMocks
    private ControladorEquipos controladorEquipos;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void misEquiposDeberiaRetornarVistaMisEquiposConListaDeEquipos() throws UsuarioNoEncontradoException {
        // Given
        String email = "usuario@test.com";
        Usuario usuario = new Usuario("Nombre", "password", "usuario@test.com", "username");
        List<Equipo> equipos = Arrays.asList(
                new Equipo("Equipo 1", "Descripción 1", usuario, java.time.LocalDateTime.now()),
                new Equipo("Equipo 2", "Descripción 2", usuario, java.time.LocalDateTime.now()));

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("EMAIL")).thenReturn(email);
        when(servicioLoginMock.buscarPorEmail(email)).thenReturn(usuario);
        when(servicioEquipoMock.obtenerEquiposDelUsuario(usuario)).thenReturn(equipos);

        // When
        String vista = controladorEquipos.misEquipos(request, new ModelMap());

        // Then
        assertThat(vista, equalTo("mis-equipos"));
        verify(servicioLoginMock).buscarPorEmail(email);
        verify(servicioEquipoMock).obtenerEquiposDelUsuario(usuario);
    }

    @Test
    void misEquiposDeberiaRedirigirALoginSiNoHaySesion() throws UsuarioNoEncontradoException {
        // Given
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("EMAIL")).thenReturn(null);

        // When
        String vista = controladorEquipos.misEquipos(request, new ModelMap());

        // Then
        assertThat(vista, equalTo("redirect:/login"));
        verify(servicioLoginMock, never()).buscarPorEmail(anyString());
        verify(servicioEquipoMock, never()).obtenerEquiposDelUsuario(any());
    }

    @Test
    void misEquiposDeberiaFiltrarEquiposPorNombreCuandoSeProporcionaBusqueda() throws UsuarioNoEncontradoException {
        // Given
        String email = "usuario@test.com";
        String busqueda = "Equipo 1";
        Usuario usuario = new Usuario("Nombre", "password", "usuario@test.com", "username");
        List<Equipo> equiposFiltrados = Arrays.asList(
                new Equipo("Equipo 1", "Descripción 1", usuario, java.time.LocalDateTime.now()));

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("EMAIL")).thenReturn(email);
        when(request.getParameter("busqueda")).thenReturn(busqueda);
        when(servicioLoginMock.buscarPorEmail(email)).thenReturn(usuario);
        when(servicioEquipoMock.obtenerEquiposDelUsuarioConFiltro(usuario, busqueda)).thenReturn(equiposFiltrados);

        // When
        ModelMap model = new ModelMap();
        String vista = controladorEquipos.misEquipos(request, model);

        // Then
        assertThat(vista, equalTo("mis-equipos"));
        assertThat(model.get("busqueda"), equalTo(busqueda));
        verify(servicioLoginMock).buscarPorEmail(email);
        verify(servicioEquipoMock).obtenerEquiposDelUsuarioConFiltro(usuario, busqueda);
        verify(servicioEquipoMock, never()).obtenerEquiposDelUsuario(usuario);
    }
}