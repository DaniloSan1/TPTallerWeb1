package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.EquipoNoEncontrado;
import com.tallerwebi.dominio.excepcion.PermisosInsufficientes;
import com.tallerwebi.dominio.excepcion.UsuarioNoEncontradoException;
import com.tallerwebi.dominio.excepcion.YaExisteElParticipante;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
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
    private ServicioImagenes servicioImagenesMock;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @Mock
    private MultipartFile multipartFileMock;

    @Mock
    private RedirectAttributes redirectAttributesMock;

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
        when(servicioEquipoMock.obtenerEquiposDelUsuarioConFiltro(usuario, null)).thenReturn(equipos);

        // When
        String vista = controladorEquipos.misEquipos(request, new ModelMap());

        // Then
        assertThat(vista, equalTo("mis-equipos"));
        verify(servicioLoginMock).buscarPorEmail(email);
        verify(servicioEquipoMock).obtenerEquiposDelUsuarioConFiltro(usuario, null);
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
    }

    @Test
    void crearEquipoDeberiaRetornarVistaCrearEquipoCuandoHaySesion() {
        // Given
        String email = "usuario@test.com";
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("EMAIL")).thenReturn(email);

        // When
        ModelMap model = new ModelMap();
        String vista = controladorEquipos.crearEquipo(request, model);

        // Then
        assertThat(vista, equalTo("crear-equipo"));
        assertThat(model.get("currentPage"), equalTo("mis-equipos"));
    }

    @Test
    void crearEquipoDeberiaRedirigirALoginCuandoNoHaySesion() {
        // Given
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("EMAIL")).thenReturn(null);

        // When
        ModelMap model = new ModelMap();
        String vista = controladorEquipos.crearEquipo(request, model);

        // Then
        assertThat(vista, equalTo("redirect:/login"));
    }

    @Test
    void editarEquipoDeberiaRetornarVistaEditarEquipoCuandoUsuarioEsCreador()
            throws UsuarioNoEncontradoException, EquipoNoEncontrado {
        // Given
        String email = "usuario@test.com";
        Long equipoId = 1L;
        Usuario usuario = new Usuario("Nombre", "password", "usuario@test.com", "username");
        Equipo equipo = new Equipo("Equipo Test", "Descripción", usuario, LocalDateTime.now());

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("EMAIL")).thenReturn(email);
        when(servicioLoginMock.buscarPorEmail(email)).thenReturn(usuario);
        when(servicioEquipoMock.buscarPorIdYUsuario(equipoId, usuario)).thenReturn(equipo);

        // When
        ModelMap model = new ModelMap();
        String vista = controladorEquipos.editarEquipo(equipoId, request, model);

        // Then
        assertThat(vista, equalTo("editar-equipo"));
        assertThat(model.get("equipo"), equalTo(equipo));
        assertThat(model.get("currentPage"), equalTo("mis-equipos"));
    }

    @Test
    void editarEquipoDeberiaRedirigirALoginCuandoNoHaySesion() throws UsuarioNoEncontradoException, EquipoNoEncontrado {
        // Given
        Long equipoId = 1L;
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("EMAIL")).thenReturn(null);

        // When
        ModelMap model = new ModelMap();
        String vista = controladorEquipos.editarEquipo(equipoId, request, model);

        // Then
        assertThat(vista, equalTo("redirect:/login"));
    }

    @Test
    void editarEquipoDeberiaRedirigirCuandoUsuarioNoTienePermisos()
            throws UsuarioNoEncontradoException, EquipoNoEncontrado {
        // Given
        String email = "usuario@test.com";
        Long equipoId = 1L;
        Usuario usuario = new Usuario("Nombre", "password", "usuario@test.com", "username");

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("EMAIL")).thenReturn(email);
        when(servicioLoginMock.buscarPorEmail(email)).thenReturn(usuario);
        when(servicioEquipoMock.buscarPorIdYUsuario(equipoId, usuario)).thenThrow(new PermisosInsufficientes());

        // When
        ModelMap model = new ModelMap();
        String vista = controladorEquipos.editarEquipo(equipoId, request, model);

        // Then
        assertThat(vista, equalTo("redirect:/equipos/mis-equipos"));
        assertThat(model.get("error"), equalTo("El usuario no tiene permisos suficientes para realizar esta acción."));
    }

    @Test
    void actualizarEquipoDeberiaActualizarEquipoExitosamente()
            throws UsuarioNoEncontradoException, EquipoNoEncontrado, Exception {
        // Given
        String email = "usuario@test.com";
        Long equipoId = 1L;
        String nombre = "Nuevo Nombre";
        String descripcion = "Nueva Descripción";
        Usuario usuario = new Usuario("Nombre", "password", "usuario@test.com", "username");
        Equipo equipo = new Equipo("Equipo Test", "Descripción", usuario, LocalDateTime.now());
        equipo.setInsigniaUrl("url-existente");

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("EMAIL")).thenReturn(email);
        when(servicioLoginMock.buscarPorEmail(email)).thenReturn(usuario);
        when(servicioEquipoMock.buscarPorIdYUsuario(equipoId, usuario)).thenReturn(equipo);
        when(multipartFileMock.isEmpty()).thenReturn(true);

        // When
        ModelMap model = new ModelMap();
        String vista = controladorEquipos.actualizarEquipo(equipoId, nombre, descripcion, multipartFileMock, request,
                redirectAttributesMock, model);

        // Then
        assertThat(vista, equalTo("redirect:/equipos/" + equipoId));
        verify(servicioEquipoMock).actualizarEquipo(equipo, nombre, descripcion, "url-existente");
        verify(redirectAttributesMock).addFlashAttribute("success", "Equipo actualizado exitosamente");
    }

    @Test
    void actualizarEquipoDeberiaValidarNombreRequerido() throws UsuarioNoEncontradoException, EquipoNoEncontrado {
        // Given
        String email = "usuario@test.com";
        Long equipoId = 1L;
        String nombre = "";
        String descripcion = "Nueva Descripción";
        Usuario usuario = new Usuario("Nombre", "password", "usuario@test.com", "username");
        Equipo equipo = new Equipo("Equipo Test", "Descripción", usuario, LocalDateTime.now());

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("EMAIL")).thenReturn(email);
        when(servicioLoginMock.buscarPorEmail(email)).thenReturn(usuario);
        when(servicioEquipoMock.buscarPorIdYUsuario(equipoId, usuario)).thenReturn(equipo);

        // When
        ModelMap model = new ModelMap();
        String vista = controladorEquipos.actualizarEquipo(equipoId, nombre, descripcion, multipartFileMock, request,
                redirectAttributesMock, model);

        // Then
        assertThat(vista, equalTo("editar-equipo"));
        assertThat(model.get("error"), equalTo("El nombre del equipo es obligatorio"));
        assertThat(model.get("equipo"), equalTo(equipo));
    }

    @Test
    void actualizarEquipoDeberiaRedirigirALoginCuandoNoHaySesion()
            throws UsuarioNoEncontradoException, EquipoNoEncontrado {
        // Given
        Long equipoId = 1L;
        String nombre = "Nuevo Nombre";
        String descripcion = "Nueva Descripción";

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("EMAIL")).thenReturn(null);

        // When
        ModelMap model = new ModelMap();
        String vista = controladorEquipos.actualizarEquipo(equipoId, nombre, descripcion, multipartFileMock, request,
                redirectAttributesMock, model);

        // Then
        assertThat(vista, equalTo("redirect:/login"));
    }

    @Test
    void guardarEquipoDeberiaCrearEquipoExitosamente() throws UsuarioNoEncontradoException, Exception {
        // Given
        String email = "usuario@test.com";
        String nombre = "Nuevo Equipo";
        String descripcion = "Descripción del equipo";
        Usuario usuario = new Usuario("Nombre", "password", "usuario@test.com", "username");
        Equipo equipoCreado = new Equipo(nombre, descripcion, usuario, LocalDateTime.now());
        equipoCreado.setId(1L);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("EMAIL")).thenReturn(email);
        when(servicioLoginMock.buscarPorEmail(email)).thenReturn(usuario);
        when(multipartFileMock.isEmpty()).thenReturn(false);
        when(servicioImagenesMock.subirImagen(multipartFileMock)).thenReturn("url-insignia");
        when(servicioEquipoMock.crearEquipo(nombre, descripcion, "url-insignia", usuario, TipoEquipo.PRIVADO)).thenReturn(equipoCreado);

        // When
        ModelMap model = new ModelMap();
        String vista = controladorEquipos.guardarEquipo(nombre, descripcion, multipartFileMock, request,
                redirectAttributesMock, model);

        // Then
        assertThat(vista, equalTo("redirect:/equipos/" + equipoCreado.getId()));
        verify(servicioEquipoMock).crearEquipo(nombre, descripcion, "url-insignia", usuario, TipoEquipo.PRIVADO);
        verify(redirectAttributesMock).addFlashAttribute("success", "Equipo creado exitosamente");
    }

    @Test
    void guardarEquipoDeberiaValidarNombreRequerido() throws UsuarioNoEncontradoException {
        // Given
        String email = "usuario@test.com";
        String nombre = "";
        String descripcion = "Descripción del equipo";
        Usuario usuario = new Usuario("Nombre", "password", "usuario@test.com", "username");

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("EMAIL")).thenReturn(email);
        when(servicioLoginMock.buscarPorEmail(email)).thenReturn(usuario);

        // When
        ModelMap model = new ModelMap();
        String vista = controladorEquipos.guardarEquipo(nombre, descripcion, multipartFileMock, request,
                redirectAttributesMock, model);

        // Then
        assertThat(vista, equalTo("crear-equipo"));
        assertThat(model.get("error"), equalTo("El nombre del equipo es obligatorio"));
    }

    @Test
    void guardarEquipoDeberiaValidarInsigniaRequerida() throws UsuarioNoEncontradoException {
        // Given
        String email = "usuario@test.com";
        String nombre = "Nuevo Equipo";
        String descripcion = "Descripción del equipo";
        Usuario usuario = new Usuario("Nombre", "password", "usuario@test.com", "username");

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("EMAIL")).thenReturn(email);
        when(servicioLoginMock.buscarPorEmail(email)).thenReturn(usuario);
        when(multipartFileMock.isEmpty()).thenReturn(true);

        // When
        ModelMap model = new ModelMap();
        String vista = controladorEquipos.guardarEquipo(nombre, descripcion, multipartFileMock, request,
                redirectAttributesMock, model);

        // Then
        assertThat(vista, equalTo("crear-equipo"));
        assertThat(model.get("error"), equalTo("La insignia del equipo es obligatoria"));
    }

    @Test
    void guardarEquipoDeberiaRedirigirALoginCuandoNoHaySesion() throws UsuarioNoEncontradoException {
        // Given
        String nombre = "Nuevo Equipo";
        String descripcion = "Descripción del equipo";

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("EMAIL")).thenReturn(null);

        // When
        ModelMap model = new ModelMap();
        String vista = controladorEquipos.guardarEquipo(nombre, descripcion, multipartFileMock, request,
                redirectAttributesMock, model);

        // Then
        assertThat(vista, equalTo("redirect:/login"));
    }

    @Test
    void detalleEquipoDeberiaMostrarDetalleDelEquipo() throws UsuarioNoEncontradoException, EquipoNoEncontrado {
        // Given
        String email = "usuario@test.com";
        Long equipoId = 1L;
        Usuario usuario = new Usuario("Nombre", "password", "usuario@test.com", "username");
        Equipo equipo = new Equipo("Equipo Test", "Descripción", usuario, LocalDateTime.now());
        equipo.setId(equipoId);
        List<EquipoJugador> jugadores = Arrays.asList();
        List<Partido> partidos = Arrays.asList();

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("EMAIL")).thenReturn(email);
        when(servicioLoginMock.buscarPorEmail(email)).thenReturn(usuario);
        when(servicioEquipoMock.buscarPorId(equipoId)).thenReturn(equipo);
        when(servicioEquipoMock.esUsuarioCreador(equipoId, usuario)).thenReturn(true);
        when(servicioEquipoJugadorMock.buscarPorEquipo(equipo)).thenReturn(jugadores);
        when(servicioPartidoMock.listarPorEquipoConInfoCancha(equipo)).thenReturn(partidos);

        // When
        ModelMap model = new ModelMap();
        String vista = controladorEquipos.detalleEquipo(equipoId, request, model);

        // Then
        assertThat(vista, equalTo("equipo"));
        assertThat(model.get("esCreador"), equalTo(true));
        assertThat(model.get("currentPage"), equalTo("mis-equipos"));
        assertThat(model.containsKey("equipo"), equalTo(true));
    }

    @Test
    void detalleEquipoDeberiaRedirigirALoginCuandoNoHaySesion()
            throws UsuarioNoEncontradoException, EquipoNoEncontrado {
        // Given
        Long equipoId = 1L;
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("EMAIL")).thenReturn(null);

        // When
        ModelMap model = new ModelMap();
        String vista = controladorEquipos.detalleEquipo(equipoId, request, model);

        // Then
        assertThat(vista, equalTo("redirect:/login"));
    }

    @Test
    void detalleEquipoDeberiaManejarEquipoNoEncontrado() throws UsuarioNoEncontradoException, EquipoNoEncontrado {
        // Given
        String email = "usuario@test.com";
        Long equipoId = 1L;
        Usuario usuario = new Usuario("Nombre", "password", "usuario@test.com", "username");

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("EMAIL")).thenReturn(email);
        when(servicioLoginMock.buscarPorEmail(email)).thenReturn(usuario);
        when(servicioEquipoMock.buscarPorId(equipoId)).thenThrow(new EquipoNoEncontrado());

        // When
        ModelMap model = new ModelMap();
        String vista = controladorEquipos.detalleEquipo(equipoId, request, model);

        // Then
        assertThat(vista, equalTo("redirect:/equipos/mis-equipos"));
        assertThat(model.get("error"), equalTo("Equipo no encontrado"));
    }

    @Test
    void agregarJugadorDeberiaAgregarJugadorExitosamenteYRedirigirAEquipo() throws Exception {
        // Given
        Long equipoId = 1L;
        Long jugadorId = 2L;
        String email = "usuario@test.com";
        Usuario usuario = new Usuario("Nombre", "password", email, "username");
        Equipo equipo = new Equipo("Equipo Test", "Descripción", usuario, LocalDateTime.now());
        Usuario jugador = new Usuario("Jugador", "password", "jugador@test.com", "jugador");

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("EMAIL")).thenReturn(email);
        when(servicioLoginMock.buscarPorEmail(email)).thenReturn(usuario);
        when(servicioEquipoMock.buscarPorIdYUsuario(equipoId, usuario)).thenReturn(equipo);
        when(servicioLoginMock.buscarPorId(jugadorId)).thenReturn(jugador);

        // When
        String vista = controladorEquipos.agregarJugador(equipoId, jugadorId, request, redirectAttributesMock);

        // Then
        assertThat(vista, equalTo("redirect:/equipos/" + equipoId));
        verify(servicioEquipoJugadorMock).crearEquipoJugador(equipo, jugador);
        verify(redirectAttributesMock).addFlashAttribute("success", "Jugador agregado exitosamente al equipo");
    }

    @Test
    void agregarJugadorDeberiaRedirigirALoginSiNoHaySesion() throws Exception {
        // Given
        Long equipoId = 1L;
        Long jugadorId = 2L;

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("EMAIL")).thenReturn(null);

        // When
        String vista = controladorEquipos.agregarJugador(equipoId, jugadorId, request, redirectAttributesMock);

        // Then
        assertThat(vista, equalTo("redirect:/login"));
        verify(servicioLoginMock, never()).buscarPorEmail(anyString());
        verify(servicioEquipoMock, never()).buscarPorIdYUsuario(anyLong(), any());
    }

    @Test
    void agregarJugadorDeberiaRedirigirConErrorSiNoTienePermisos() throws Exception {
        // Given
        Long equipoId = 1L;
        Long jugadorId = 2L;
        String email = "usuario@test.com";
        Usuario usuario = new Usuario("Nombre", "password", email, "username");

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("EMAIL")).thenReturn(email);
        when(servicioLoginMock.buscarPorEmail(email)).thenReturn(usuario);
        when(servicioEquipoMock.buscarPorIdYUsuario(equipoId, usuario)).thenThrow(new PermisosInsufficientes());

        // When
        String vista = controladorEquipos.agregarJugador(equipoId, jugadorId, request, redirectAttributesMock);

        // Then
        assertThat(vista, equalTo("redirect:/equipos/mis-equipos"));
        verify(redirectAttributesMock).addFlashAttribute("error",
                "No tienes permisos para agregar jugadores a este equipo");
    }

    @Test
    void agregarJugadorDeberiaRedirigirConErrorSiEquipoNoEncontrado() throws Exception {
        // Given
        Long equipoId = 1L;
        Long jugadorId = 2L;
        String email = "usuario@test.com";
        Usuario usuario = new Usuario("Nombre", "password", email, "username");

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("EMAIL")).thenReturn(email);
        when(servicioLoginMock.buscarPorEmail(email)).thenReturn(usuario);
        when(servicioEquipoMock.buscarPorIdYUsuario(equipoId, usuario)).thenThrow(new EquipoNoEncontrado());

        // When
        String vista = controladorEquipos.agregarJugador(equipoId, jugadorId, request, redirectAttributesMock);

        // Then
        assertThat(vista, equalTo("redirect:/equipos/mis-equipos"));
        verify(redirectAttributesMock).addFlashAttribute("error", "Equipo no encontrado");
    }

    @Test
    void agregarJugadorDeberiaRedirigirConErrorSiJugadorNoEncontrado() throws Exception {
        // Given
        Long equipoId = 1L;
        Long jugadorId = 2L;
        String email = "usuario@test.com";
        Usuario usuario = new Usuario("Nombre", "password", email, "username");
        Equipo equipo = new Equipo("Equipo Test", "Descripción", usuario, LocalDateTime.now());

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("EMAIL")).thenReturn(email);
        when(servicioLoginMock.buscarPorEmail(email)).thenReturn(usuario);
        when(servicioEquipoMock.buscarPorIdYUsuario(equipoId, usuario)).thenReturn(equipo);
        when(servicioLoginMock.buscarPorId(jugadorId)).thenThrow(new UsuarioNoEncontradoException());

        // When
        String vista = controladorEquipos.agregarJugador(equipoId, jugadorId, request, redirectAttributesMock);

        // Then
        assertThat(vista, equalTo("redirect:/equipos/mis-equipos"));
        verify(redirectAttributesMock).addFlashAttribute("error", "Usuario no encontrado");
    }

    @Test
    void agregarJugadorDeberiaRedirigirConErrorSiJugadorYaExiste() throws Exception {
        // Given
        Long equipoId = 1L;
        Long jugadorId = 2L;
        String email = "usuario@test.com";
        Usuario usuario = new Usuario("Nombre", "password", email, "username");
        Equipo equipo = new Equipo("Equipo Test", "Descripción", usuario, LocalDateTime.now());
        Usuario jugador = new Usuario("Jugador", "password", "jugador@test.com", "jugador");

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("EMAIL")).thenReturn(email);
        when(servicioLoginMock.buscarPorEmail(email)).thenReturn(usuario);
        when(servicioEquipoMock.buscarPorIdYUsuario(equipoId, usuario)).thenReturn(equipo);
        when(servicioLoginMock.buscarPorId(jugadorId)).thenReturn(jugador);
        doThrow(new YaExisteElParticipante()).when(servicioEquipoJugadorMock).crearEquipoJugador(equipo, jugador);

        // When
        String vista = controladorEquipos.agregarJugador(equipoId, jugadorId, request, redirectAttributesMock);

        // Then
        assertThat(vista, equalTo("redirect:/equipos/" + equipoId));
        verify(redirectAttributesMock).addFlashAttribute("error", "El jugador ya pertenece al equipo");
    }

    @Test
    void agregarJugadorDeberiaRedirigirConErrorGenericoEnCasoDeExcepcionInesperada() throws Exception {
        // Given
        Long equipoId = 1L;
        Long jugadorId = 2L;
        String email = "usuario@test.com";
        Usuario usuario = new Usuario("Nombre", "password", email, "username");
        Equipo equipo = new Equipo("Equipo Test", "Descripción", usuario, LocalDateTime.now());
        Usuario jugador = new Usuario("Jugador", "password", "jugador@test.com", "jugador");

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("EMAIL")).thenReturn(email);
        when(servicioLoginMock.buscarPorEmail(email)).thenReturn(usuario);
        when(servicioEquipoMock.buscarPorIdYUsuario(equipoId, usuario)).thenReturn(equipo);
        when(servicioLoginMock.buscarPorId(jugadorId)).thenReturn(jugador);
        doThrow(new RuntimeException("Error inesperado")).when(servicioEquipoJugadorMock).crearEquipoJugador(equipo,
                jugador);

        // When
        String vista = controladorEquipos.agregarJugador(equipoId, jugadorId, request, redirectAttributesMock);

        // Then
        assertThat(vista, equalTo("redirect:/equipos/" + equipoId));
        verify(redirectAttributesMock).addFlashAttribute("error", "Error inesperado al agregar el jugador");
    }
}