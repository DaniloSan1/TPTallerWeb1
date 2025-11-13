package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ControladorReseniaCanchaTest {

    @Mock
    private ServicioReseniaCancha servicioReseniaCanchaMock;
    @Mock
    private ServicioCancha servicioCanchaMock;
    @Mock
    private ServicioUsuario servicioUsuarioMock;
    @Mock
    private HttpServletRequest requestMock;
    @Mock
    private HttpSession sessionMock;
    @Mock
    private RedirectAttributes redirectAttributesMock;

    @InjectMocks
    private ControladorReseniaCancha controlador;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(requestMock.getSession()).thenReturn(sessionMock);
    }

    @Test
    void mostrarFormularioReseniaConUsuarioYCanchaValidaDebeMostrarVista() {
        Long canchaId = 1L;
        String email = "user@test.com";
        Cancha cancha = new Cancha();
        cancha.setId(canchaId);
        Usuario usuario = new Usuario();

        when(sessionMock.getAttribute("EMAIL")).thenReturn(email);
        when(servicioCanchaMock.obtenerCanchaPorId(canchaId)).thenReturn(cancha);
        when(servicioUsuarioMock.buscarPorEmail(email)).thenReturn(usuario);
        when(servicioReseniaCanchaMock.verificarSiElUsuarioPuedeReseniarEsaCancha(usuario, cancha)).thenReturn(true);

        ModelAndView mav = controlador.mostrarFormularioResenia(canchaId, requestMock, redirectAttributesMock);

        assertThat(mav.getViewName(), is("reseniar-cancha"));
        assertThat(mav.getModel(), hasEntry("cancha", cancha));
        assertThat(mav.getModel(), hasEntry("usuario", usuario));
        assertThat(mav.getModel(), hasEntry("editMode", false));
    }

    @Test
    void mostrarFormularioReseniaSinUsuarioEnSesionDebeRedirigirALogin() {
        when(sessionMock.getAttribute("EMAIL")).thenReturn(null);

        ModelAndView mav = controlador.mostrarFormularioResenia(1L, requestMock, redirectAttributesMock);

        assertThat(mav.getViewName(), is("redirect:/login"));
        verifyNoInteractions(servicioCanchaMock);
    }

    @Test
    void mostrarFormularioReseniaConCanchaInexistenteDebeMostrarError() {
        Long canchaId = 5L;
        String email = "user@test.com";
        when(sessionMock.getAttribute("EMAIL")).thenReturn(email);
        when(servicioCanchaMock.obtenerCanchaPorId(canchaId)).thenReturn(null);

        ModelAndView mav = controlador.mostrarFormularioResenia(canchaId, requestMock, redirectAttributesMock);

        assertThat(mav.getViewName(), is("canchas"));
        assertThat(mav.getModel(), hasEntry("error", "La cancha que quiere reseñar no existe"));
    }

    @Test
    void mostrarFormularioReseniaCuandoUsuarioYaResenioDebeRedirigirAlaCanchaConMensaje() {
        Long canchaId = 2L;
        String email = "user@test.com";
        Usuario usuario = new Usuario();
        Cancha cancha = new Cancha();

        when(sessionMock.getAttribute("EMAIL")).thenReturn(email);
        when(servicioCanchaMock.obtenerCanchaPorId(canchaId)).thenReturn(cancha);
        when(servicioUsuarioMock.buscarPorEmail(email)).thenReturn(usuario);
        when(servicioReseniaCanchaMock.verificarSiElUsuarioPuedeReseniarEsaCancha(usuario, cancha)).thenReturn(false);

        ModelAndView mav = controlador.mostrarFormularioResenia(canchaId, requestMock, redirectAttributesMock);

        assertThat(mav.getViewName(), is("redirect:/cancha/" + canchaId));
        verify(redirectAttributesMock).addFlashAttribute(eq("error"), anyString());
    }


    @Test
    void guardarReseniaDebeAgregarReseniaYRedirigirALaCancha() {
        Long canchaId = 1L;
        String email = "user@test.com";
        Usuario usuario = new Usuario();
        Cancha cancha = new Cancha();
        cancha.setId(canchaId);

        when(sessionMock.getAttribute("EMAIL")).thenReturn(email);
        when(servicioUsuarioMock.buscarPorEmail(email)).thenReturn(usuario);
        when(servicioCanchaMock.obtenerCanchaPorId(canchaId)).thenReturn(cancha);

        ModelMap model = new ModelMap();

        ModelAndView mav = controlador.guardarResenia(canchaId, 5, "Excelente!", model, requestMock,redirectAttributesMock);

        assertThat(mav.getViewName(), is("redirect:/cancha/" + canchaId));
        verify(servicioReseniaCanchaMock).agregarReseniaCancha(any(ReseniaCancha.class));
    }

   
    @Test
    void verHistorialDebeMostrarReseniasDelUsuario() {
        Long usuarioId = 1L;
        String email = "user@test.com";
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        List<ReseniaCancha> resenias = Arrays.asList(new ReseniaCancha(), new ReseniaCancha());

        when(sessionMock.getAttribute("EMAIL")).thenReturn(email);
        when(servicioUsuarioMock.buscarPorEmail(email)).thenReturn(usuario);
        when(servicioReseniaCanchaMock.obtenerReseniasPorUsuario(usuarioId)).thenReturn(resenias);

        ModelAndView mav = controlador.verHistorial(usuarioId, requestMock);

        assertThat(mav.getViewName(), is("historial-resenias"));
        assertThat(mav.getModel(), hasEntry("resenias", resenias));
    }

    @Test
    void verHistorialDeOtroUsuarioDebeRedirigirAPerfil() {
        Long usuarioId = 2L;
        Usuario usuarioSesion = new Usuario();
        usuarioSesion.setId(5L);
        when(sessionMock.getAttribute("EMAIL")).thenReturn("user@test.com");
        when(servicioUsuarioMock.buscarPorEmail(anyString())).thenReturn(usuarioSesion);

        ModelAndView mav = controlador.verHistorial(usuarioId, requestMock);

        assertThat(mav.getViewName(), is("redirect:/perfil/"));
    }

   
    @Test
    void editarReseniaDebeMostrarFormularioConDatos() {
        Long reseniaId = 1L;
        String email = "user@test.com";

        Usuario usuario = new Usuario();
        usuario.setId(10L);

        Cancha cancha = new Cancha();
        ReseniaCancha resenia = new ReseniaCancha();
        resenia.setId(reseniaId);
        resenia.setUsuario(usuario);
        resenia.setCancha(cancha);

        when(sessionMock.getAttribute("EMAIL")).thenReturn(email);
        when(servicioUsuarioMock.buscarPorEmail(email)).thenReturn(usuario);
        when(servicioReseniaCanchaMock.obtenerReseniaCanchaPorId(reseniaId)).thenReturn(resenia);

        ModelAndView mav = controlador.editarResenia(reseniaId, requestMock, redirectAttributesMock);

        assertThat(mav.getViewName(), is("reseniar-cancha"));
        assertThat(mav.getModel(), hasEntry("cancha", cancha));
        assertThat(mav.getModel(), hasEntry("usuario", usuario));
        assertThat(mav.getModel(), hasEntry("resenia", resenia));
        assertThat(mav.getModel(), hasEntry("editMode", true));
    }

    @Test
    void editarReseniaDeOtroUsuarioDebeRedirigirAPerfilConError() {
        Long reseniaId = 1L;
        String email = "user@test.com";
        Usuario usuarioSesion = new Usuario();
        usuarioSesion.setId(2L);

        Usuario usuarioResenia = new Usuario();
        usuarioResenia.setId(5L);

        ReseniaCancha resenia = new ReseniaCancha();
        resenia.setUsuario(usuarioResenia);

        when(sessionMock.getAttribute("EMAIL")).thenReturn(email);
        when(servicioUsuarioMock.buscarPorEmail(email)).thenReturn(usuarioSesion);
        when(servicioReseniaCanchaMock.obtenerReseniaCanchaPorId(reseniaId)).thenReturn(resenia);

        ModelAndView mav = controlador.editarResenia(reseniaId, requestMock, redirectAttributesMock);

        assertThat(mav.getViewName(), is("redirect:/perfil"));
        verify(redirectAttributesMock).addFlashAttribute(eq("error"), anyString());    
}

   

    @Test
    void actualizarReseniaDebeActualizarYRedirigirACancha() {
        Long reseniaId = 1L;
        String email = "user@test.com";
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        Cancha cancha = new Cancha();
        cancha.setId(99L);
        ReseniaCancha resenia = new ReseniaCancha();
        resenia.setId(reseniaId);
        resenia.setUsuario(usuario);
        resenia.setCancha(cancha);

        when(sessionMock.getAttribute("EMAIL")).thenReturn(email);
        when(servicioUsuarioMock.buscarPorEmail(email)).thenReturn(usuario);
        when(servicioReseniaCanchaMock.obtenerReseniaCanchaPorId(reseniaId)).thenReturn(resenia);

        ModelAndView mav = controlador.actualizarResenia(reseniaId, 4, "Buen estado", requestMock,redirectAttributesMock);

        assertThat(mav.getViewName(), is("redirect:/cancha/" + cancha.getId()));
        verify(servicioReseniaCanchaMock).editarReseniaCancha(resenia);
    }

    @Test
    void actualizarReseniaDeOtroUsuarioDebeRedirigirAPerfil() {
        Long reseniaId = 1L;
        String email = "user@test.com";
        Usuario usuarioSesion = new Usuario();
        usuarioSesion.setId(10L);
        Usuario otroUsuario = new Usuario();
        otroUsuario.setId(20L);
        ReseniaCancha resenia = new ReseniaCancha();
        resenia.setUsuario(otroUsuario);

        when(sessionMock.getAttribute("EMAIL")).thenReturn(email);
        when(servicioUsuarioMock.buscarPorEmail(email)).thenReturn(usuarioSesion);
        when(servicioReseniaCanchaMock.obtenerReseniaCanchaPorId(reseniaId)).thenReturn(resenia);

        ModelAndView mav = controlador.actualizarResenia(reseniaId, 5, "No aplica", requestMock,redirectAttributesMock);

        assertThat(mav.getViewName(), is("redirect:/perfil"));
        verify(servicioReseniaCanchaMock, never()).editarReseniaCancha(any());
    }
}
