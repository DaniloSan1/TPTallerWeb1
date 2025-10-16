package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioLogin;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.presentacion.ControladorUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.mockito.Mockito.*;

public class ControladorUsuarioTest {
    private ServicioLogin servicioLoginMock;
    private ControladorUsuario controladorUsuario;
    private Usuario usuarioMock;
    private HttpServletRequest httpServletRequestMock;
    private HttpSession sessionMock;

    @BeforeEach
    public void init() {
        servicioLoginMock = mock(ServicioLogin.class);
        sessionMock = mock(HttpSession.class);
        httpServletRequestMock = mock(HttpServletRequest.class);
        controladorUsuario = new ControladorUsuario(servicioLoginMock);

        usuarioMock = mock(Usuario.class);
        when(usuarioMock.getEmail()).thenReturn("usuario@test.com");
        when(usuarioMock.getNombre()).thenReturn("NombreTest");
        when(usuarioMock.getApellido()).thenReturn("ApellidoTest");
        when(usuarioMock.getPosicionFavorita()).thenReturn("DELANTERO");
    }

    @Test
    public void perfilConUsuarioLogueadoDeberiaCargarElPerfil() {
        when(httpServletRequestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("EMAIL")).thenReturn("usuario@test.com");
        when(servicioLoginMock.buscarPorEmail("usuario@test.com")).thenReturn(usuarioMock);

        ModelMap modelo = new ModelMap();

        String vista = controladorUsuario.perfil(modelo, httpServletRequestMock);

        assertThat(vista, equalToIgnoringCase("perfil"));
        verify(servicioLoginMock, times(1)).buscarPorEmail("usuario@test.com");
    }

    @Test
    public void perfilSinUsuarioLogueadoDeberiaIrAlLogin() {
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
}
