package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.UsuarioNoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

class ControladorCanchaTest {

    @Mock
    private ServicioCancha servicioCanchaMock;

    @Mock
    private ServicioHorario servicioHorarioMock;

    @Mock
    private ServicioLogin servicioLoginMock;

    @Mock
    private ServicioFotoCancha servicioFotoCanchaMock;

    @Mock
    private ServicioReseniaCancha servicioReseniaCanchaMock;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @InjectMocks
    private ControladorCancha controladorCancha;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Por defecto devolver 0.0 para la calificación promedio en las pruebas
        when(servicioReseniaCanchaMock.calcularCalificacionPromedioCancha(anyLong())).thenReturn(0.0);
    }

    @Test
    void listarCanchasDeberiaRetornarVistaCanchasConLista() {

        List<Cancha> canchas = Arrays.asList(new Cancha(), new Cancha());
        when(servicioCanchaMock.obtenerCanchasDisponibles(null, null, 0.0)).thenReturn(canchas);
        ModelMap model = new ModelMap();

        String vista = controladorCancha.listarCanchas(model, request);

        assertThat(vista, is("canchas"));
        assertThat(model, hasEntry("canchas", canchas));
        assertThat(model, hasEntry("currentPage", "canchas-disponibles"));
        verify(servicioCanchaMock, times(1)).obtenerCanchasDisponibles(null, null, 0.0);
    }

    @Test
    void listarCanchasDeberiaManejarExcepcionYAgregarErrorAlModel() {
        // given
        when(servicioCanchaMock.obtenerCanchasDisponibles(null, null, 0.0))
                .thenThrow(new RuntimeException("Error inesperado"));
        ModelMap model = new ModelMap();

        // when
        String vista = controladorCancha.listarCanchas(model, request);

        // then
        assertThat(vista, is("canchas"));
        assertThat(model, hasEntry("error", "Error inesperado"));
    }

    @Test
    void verCanchaConUnUsuarioLogueadoDeberiaMostrarVistaCancha() throws UsuarioNoEncontradoException {
        // given
        Long canchaId = 1L;
        String email = "test@correo.com";

        Usuario usuario = new Usuario();
        usuario.setId(5L);

        Cancha cancha = new Cancha();
        cancha.setId(canchaId);

        Horario horario = new Horario();
        List<Horario> horarios = List.of(horario);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("EMAIL")).thenReturn(email);

        when(servicioLoginMock.buscarPorEmail(email)).thenReturn(usuario);
        when(servicioCanchaMock.obtenerCanchaPorId(canchaId)).thenReturn(cancha);
        when(servicioHorarioMock.obtenerPorCancha(cancha)).thenReturn(horarios);

        ModelMap model = new ModelMap();

        // when
        ModelAndView mav = controladorCancha.verCancha(canchaId, model, request);

        // then
        assertThat(mav.getViewName(), is("cancha"));
        assertThat(mav.getModel(), hasEntry("cancha", cancha));
        assertThat(mav.getModel(), hasEntry("horarios", horarios));
        assertThat(mav.getModel(), hasEntry("usuarioId", usuario.getId()));
    }

    @Test
    void verCanchaSinUsuarioEnSesionDebeRedirigirALogin() {
        // given
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("EMAIL")).thenReturn(null);
        ModelMap model = new ModelMap();

        // when
        ModelAndView mav = controladorCancha.verCancha(1L, model, request);

        // then
        assertThat(mav.getViewName(), is("redirect:/login"));
        verifyNoInteractions(servicioCanchaMock, servicioHorarioMock, servicioLoginMock);
    }

    @Test
    void verCanchaDeberiaManejarExcepcionYAgregarError() throws UsuarioNoEncontradoException {
        // given
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("EMAIL")).thenReturn("usuario@correo.com");
        when(servicioLoginMock.buscarPorEmail(anyString())).thenThrow(new RuntimeException("Falla al obtener usuario"));

        ModelMap model = new ModelMap();

        // when
        ModelAndView mav = controladorCancha.verCancha(1L, model, request);

        // then
        assertThat(mav.getViewName(), is("cancha"));
        assertThat(mav.getModel(), hasEntry("error", "Falla al obtener usuario"));
    }

    @Test
    void listarCanchasDeberiaAgregarFotosAlModelo() {
        // given
        List<Cancha> canchas = Arrays.asList(new Cancha(), new Cancha());
        List<FotoCancha> fotos = Arrays.asList(new FotoCancha(), new FotoCancha());

        when(servicioCanchaMock.obtenerCanchasDisponibles(null, null, 0.0)).thenReturn(canchas);

        when(servicioFotoCanchaMock.insertarFotosAModelCanchas(canchas)).thenReturn(fotos);

        ModelMap model = new ModelMap();

        // when
        String vista = controladorCancha.listarCanchas(model, request);

        // then
        assertThat(vista, is("canchas"));
        assertThat(model, hasEntry("fotosCanchas", fotos));
        verify(servicioFotoCanchaMock, times(1)).insertarFotosAModelCanchas(canchas);
    }
}
