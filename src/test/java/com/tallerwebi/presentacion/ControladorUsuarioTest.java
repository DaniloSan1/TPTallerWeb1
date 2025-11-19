package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.ServicioAmistad;
import com.tallerwebi.dominio.ServicioCalificacion;
import com.tallerwebi.dominio.ServicioLogin;
import com.tallerwebi.dominio.ServicioUsuario;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.UsuarioNoEncontradoException;
import com.tallerwebi.presentacion.ControladorUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ControladorUsuarioTest {
    private ServicioLogin servicioLoginMock;
    private ServicioUsuario servicioUsuarioMock;
    private ServicioAmistad servicioAmistadMock;
    private ServicioNotificacionDeUsuario servicioNotificacionMock;
    private ServicioPartido servicioPartidoMock;
    private ServicioGoles servicioGolesMock;
    private ServicioCalificacion servicioCalificacionMock;
    private ControladorUsuario controladorUsuario;
    private Usuario usuarioMock;
    private HttpServletRequest httpServletRequestMock;
    private HttpSession sessionMock;
    private HttpServletRequest requestMock;
    
    @BeforeEach
    public void init() {
        servicioLoginMock = mock(ServicioLogin.class);
        servicioUsuarioMock = mock(ServicioUsuario.class);
        servicioAmistadMock = mock(ServicioAmistad.class);
        servicioCalificacionMock = mock(ServicioCalificacion.class);
        servicioGolesMock=mock(ServicioGoles.class);
        servicioPartidoMock=mock(ServicioPartido.class);
        sessionMock = mock(HttpSession.class);
        httpServletRequestMock = mock(HttpServletRequest.class);
        controladorUsuario = new ControladorUsuario(servicioLoginMock, servicioUsuarioMock, servicioAmistadMock, null, servicioCalificacionMock,servicioGolesMock,servicioPartidoMock);

        requestMock = mock(HttpServletRequest.class);
        usuarioMock = mock(Usuario.class);
        when(usuarioMock.getEmail()).thenReturn("usuario@test.com");
        when(usuarioMock.getNombre()).thenReturn("NombreTest");
        when(usuarioMock.getApellido()).thenReturn("ApellidoTest");
        when(usuarioMock.getPosicionFavorita()).thenReturn("DELANTERO");
        when(usuarioMock.getPassword()).thenReturn("12345");
        when(usuarioMock.getUsername()).thenReturn("username");
    }

    @Test
    public void perfilConUsuarioLogueadoDeberiaCargarElPerfil() throws UsuarioNoEncontradoException {
        when(httpServletRequestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("EMAIL")).thenReturn("usuario@test.com");
        when(servicioLoginMock.buscarPorEmail("usuario@test.com")).thenReturn(usuarioMock);
        when(usuarioMock.getId()).thenReturn(1L);
        when(servicioCalificacionMock.calcularCalificacionPromedioUsuario(1L)).thenReturn(4.5);
        when(servicioAmistadMock.verAmigos(1L)).thenReturn(new ArrayList<>());
        when(servicioAmistadMock.verSolicitudesPendientes(1L)).thenReturn(new ArrayList<>());

        ModelMap modelo = new ModelMap();

        String vista = controladorUsuario.perfil(modelo, httpServletRequestMock);

        assertThat(vista, equalToIgnoringCase("perfil"));
        verify(servicioLoginMock, times(1)).buscarPorEmail("usuario@test.com");
    }

    @Test
    public void perfilSinUsuarioLogueadoDeberiaIrAlLogin() throws UsuarioNoEncontradoException {
        when(httpServletRequestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("EMAIL")).thenReturn(null);

        ModelMap modelo = new ModelMap();

        String vista = controladorUsuario.perfil(modelo, httpServletRequestMock);
        assertThat(vista, equalToIgnoringCase("redirect:/login"));
        verify(servicioLoginMock, never()).buscarPorEmail(anyString());
    }

    @Test
    public void queAlApretarCerrarSesionCierreLaSesion() {
        when(httpServletRequestMock.getSession()).thenReturn(sessionMock);

        ModelAndView vista = controladorUsuario.logout(httpServletRequestMock);

        verify(sessionMock, times(1)).invalidate();
        assertThat(vista.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    @Test
    public void queActualiceLosDatosDelUsuarioCorrectamente() throws UsuarioNoEncontradoException {
        Usuario usuarioEnSesion = new Usuario();
        usuarioEnSesion.setId(1L);
        usuarioEnSesion.setEmail("user@example.com");
        usuarioEnSesion.setUsername("viejoUser");

        Usuario usuarioEditado = new Usuario();
        usuarioEditado.setId(1L);
        usuarioEditado.setUsername("nuevoUser");
        usuarioEditado.setNombre("NuevoNombre");
        usuarioEditado.setApellido("NuevoApellido");
        usuarioEditado.setPosicionFavorita("Delantero");

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("EMAIL")).thenReturn("user@example.com");
        when(servicioLoginMock.buscarPorEmail("user@example.com")).thenReturn(usuarioEnSesion);
        when(servicioUsuarioMock.buscarPorUsername("nuevoUser")).thenReturn(null);

        ModelMap modelo = new ModelMap();

        String vista = controladorUsuario.guardarCambios(usuarioEditado, requestMock, modelo);

        assertEquals("redirect:/perfil", vista);
        assertEquals("nuevoUser", usuarioEnSesion.getUsername());
        assertEquals("NuevoNombre", usuarioEnSesion.getNombre());
        assertEquals("NuevoApellido", usuarioEnSesion.getApellido());
        assertEquals("Delantero", usuarioEnSesion.getPosicionFavorita());
        verify(servicioUsuarioMock).modificarUsuario(usuarioEnSesion);
    }

    @Test
    public void queLanceExcepcionCuandoElUsernameYaExiste() throws UsuarioNoEncontradoException {
        Usuario usuarioEnSesion = new Usuario();
        usuarioEnSesion.setId(1L);
        usuarioEnSesion.setEmail("user@example.com");
        usuarioEnSesion.setUsername("actualUser");

        Usuario usuarioEditado = new Usuario();
        usuarioEditado.setId(1L);
        usuarioEditado.setUsername("usernameRepetido");

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(2L);

        when(sessionMock.getAttribute("EMAIL")).thenReturn("user@example.com");
        when(servicioLoginMock.buscarPorEmail("user@example.com")).thenReturn(usuarioEnSesion);
        when(servicioUsuarioMock.buscarPorUsername("usernameRepetido")).thenReturn(usuarioExistente);
        when(requestMock.getSession()).thenReturn(sessionMock);
        ModelMap modelo = new ModelMap();

        String vista = controladorUsuario.guardarCambios(usuarioEditado, requestMock, modelo);

        assertEquals("editarPerfil", vista);
        assertTrue(modelo.containsKey("error"));
        assertTrue(modelo.get("error").toString().toLowerCase().contains("username"));
        verify(servicioUsuarioMock, never()).modificarUsuario(any());
    }
}
