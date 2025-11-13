package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.EquipoNoEncontrado;
import com.tallerwebi.dominio.excepcion.PermisosInsufficientes;
import com.tallerwebi.infraestructura.RepositorioEquipoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ServicioEquipoImplTest {
    private ServicioEquipo servicioEquipoImpl;
    private RepositorioEquipo repositorioEquipo;

    @BeforeEach
    public void init() {
        repositorioEquipo = mock(RepositorioEquipoImpl.class);
        servicioEquipoImpl = new ServicioEquipoImpl(repositorioEquipo);
    }

    @Test
    public void queSePuedaCrearUnEquipo() {
        Usuario creador = new Usuario("nombre", "password", "email@test.com", "username");
        String nombreEquipo = "Equipo Test";
        String descripcion = "Descripción del equipo";
        String insigniaUrl = "http://example.com/insignia.png";

        Equipo equipoCreado = servicioEquipoImpl.crearEquipo(nombreEquipo, descripcion, insigniaUrl, creador);

        verify(repositorioEquipo).guardar(any(Equipo.class));
        assertThat(equipoCreado.getNombre(), equalTo(nombreEquipo));
        assertThat(equipoCreado.getDescripcion(), equalTo(descripcion));
        assertThat(equipoCreado.getInsigniaUrl(), equalTo(insigniaUrl));
        assertThat(equipoCreado.getCreadoPor(), equalTo(creador));
    }

    @Test
    public void queSePuedaBuscarUnEquipoPorId() throws EquipoNoEncontrado {
        Long id = 1L;
        Equipo equipoEsperado = new Equipo("Equipo Test", "Descripción", new Usuario(), java.time.LocalDateTime.now());
        equipoEsperado.setId(id);

        when(repositorioEquipo.buscarPorId(id)).thenReturn(equipoEsperado);

        Equipo equipoEncontrado = servicioEquipoImpl.buscarPorId(id);

        assertThat(equipoEncontrado, equalTo(equipoEsperado));
    }

    @Test
    public void queLanceExcepcionSiEquipoNoExiste() {
        Long id = 1L;

        when(repositorioEquipo.buscarPorId(id)).thenReturn(null);

        assertThrows(EquipoNoEncontrado.class, () -> {
            servicioEquipoImpl.buscarPorId(id);
        });
    }

    @Test
    public void queSePuedaActualizarElNombreDeUnEquipo() {
        Equipo equipo = new Equipo("Equipo Original", "Descripción", new Usuario(), java.time.LocalDateTime.now());
        String nuevoNombre = "Equipo Modificado";

        servicioEquipoImpl.actualizarNombre(equipo, nuevoNombre);

        verify(repositorioEquipo).modificar(equipo);
        assertThat(equipo.getNombre(), equalTo(nuevoNombre));
    }

    @Test
    public void queSePuedanObtenerLosEquiposDeUnUsuarioConFiltro() {
        Usuario usuario = new Usuario("nombre", "password", "email@test.com", "username");
        String filtro = "Equipo 1";
        List<Equipo> equiposEsperados = Arrays.asList(
                new Equipo("Equipo 1", "Descripción 1", usuario, LocalDateTime.now()));

        when(repositorioEquipo.buscarEquiposPorUsuarioYNombre(usuario, filtro)).thenReturn(equiposEsperados);

        List<Equipo> equiposObtenidos = servicioEquipoImpl.obtenerEquiposDelUsuarioConFiltro(usuario, filtro);

        verify(repositorioEquipo).buscarEquiposPorUsuarioYNombre(usuario, filtro);
        assertThat(equiposObtenidos, equalTo(equiposEsperados));
    }

    @Test
    public void queSePuedaVerificarSiUsuarioEsCreadorDelEquipo() {
        Usuario creador = new Usuario("nombre", "password", "email@test.com", "username");
        creador.setId(1L);
        Equipo equipo = new Equipo("Equipo Test", "Descripción", creador, LocalDateTime.now());
        equipo.setId(1L);

        when(repositorioEquipo.buscarPorId(1L)).thenReturn(equipo);

        boolean esCreador = servicioEquipoImpl.esUsuarioCreador(equipo.getId(), creador);

        assertThat(esCreador, equalTo(true));
    }

    @Test
    public void queSePuedaVerificarSiUsuarioNoEsCreadorDelEquipo() {
        Usuario creador = new Usuario("nombre", "password", "email@test.com", "username");
        creador.setId(1L);
        Usuario otroUsuario = new Usuario("otro", "password", "otro@email.com", "otro");
        otroUsuario.setId(2L);
        Equipo equipo = new Equipo("Equipo Test", "Descripción", creador, LocalDateTime.now());
        equipo.setId(1L);

        when(repositorioEquipo.buscarPorId(1L)).thenReturn(equipo);

        boolean esCreador = servicioEquipoImpl.esUsuarioCreador(equipo.getId(), otroUsuario);

        assertThat(esCreador, equalTo(false));
    }

    @Test
    public void queSePuedaBuscarPorIdYUsuarioSinExcepcion() throws EquipoNoEncontrado {
        Usuario creador = new Usuario("nombre", "password", "email@test.com", "username");
        creador.setId(1L);
        Equipo equipo = new Equipo("Equipo Test", "Descripción", creador, LocalDateTime.now());
        equipo.setId(1L);

        when(repositorioEquipo.buscarPorIdYUsuario(1L, creador)).thenReturn(equipo);

        Equipo result = servicioEquipoImpl.buscarPorIdYUsuario(equipo.getId(), creador);

        assertThat(result, equalTo(equipo));
    }

    @Test
    public void queLanceExcepcionSiUsuarioNoEsCreadorEnBuscarPorIdYUsuario() {
        Usuario creador = new Usuario("nombre", "password", "email@test.com", "username");
        creador.setId(1L);
        Usuario otroUsuario = new Usuario("otro", "password", "otro@email.com", "otro");
        otroUsuario.setId(2L);
        Equipo equipo = new Equipo("Equipo Test", "Descripción", creador, LocalDateTime.now());
        equipo.setId(1L);

        when(repositorioEquipo.buscarPorIdYUsuario(1L, otroUsuario)).thenReturn(null);

        assertThrows(EquipoNoEncontrado.class, () -> {
            servicioEquipoImpl.buscarPorIdYUsuario(equipo.getId(), otroUsuario);
        });
    }

    @Test
    public void queSePuedaActualizarUnEquipo() {
        Equipo equipo = new Equipo("Equipo Original", "Descripción Original", new Usuario(), LocalDateTime.now());
        String nuevoNombre = "Equipo Actualizado";
        String nuevaDescripcion = "Descripción Actualizada";
        String nuevaInsigniaUrl = "http://example.com/insignia.png";

        servicioEquipoImpl.actualizarEquipo(equipo, nuevoNombre, nuevaDescripcion, nuevaInsigniaUrl);

        verify(repositorioEquipo).modificar(equipo);
        assertThat(equipo.getNombre(), equalTo(nuevoNombre));
        assertThat(equipo.getDescripcion(), equalTo(nuevaDescripcion));
        assertThat(equipo.getInsigniaUrl(), equalTo(nuevaInsigniaUrl));
    }
}