package com.tallerwebi.dominio;

import com.tallerwebi.infraestructura.RepositorioEquipoJugadorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

public class ServicioEquipoJugadorImplTest {
    private ServicioEquipoJugador servicioEquipoJugadorImpl;
    private RepositorioEquipoJugador repositorioEquipoJugador;

    @BeforeEach
    public void init() {
        repositorioEquipoJugador = mock(RepositorioEquipoJugadorImpl.class);
        servicioEquipoJugadorImpl = new ServicioEquipoJugadorImpl(repositorioEquipoJugador);
    }

    @Test
    public void queSePuedaCrearUnEquipoJugador() {
        Usuario creador = new Usuario();
        Equipo equipo = new Equipo("Equipo Test", creador, java.time.LocalDateTime.now());
        Usuario jugador = new Usuario();

        EquipoJugador equipoJugadorCreado = servicioEquipoJugadorImpl.crearEquipoJugador(equipo, jugador);

        verify(repositorioEquipoJugador).guardar(any(EquipoJugador.class));
        assertThat(equipoJugadorCreado.getEquipo(), equalTo(equipo));
        assertThat(equipoJugadorCreado.getUsuario(), equalTo(jugador));
    }

    @Test
    public void queSePuedaBuscarEquipoJugadorPorEquipoYUsuario() {
        Usuario creador = new Usuario();
        Equipo equipo = new Equipo("Equipo Test", creador, java.time.LocalDateTime.now());
        Usuario jugador = new Usuario();
        EquipoJugador equipoJugador = new EquipoJugador(equipo, jugador);

        when(repositorioEquipoJugador.buscarPorEquipoYUsuario(equipo, jugador)).thenReturn(equipoJugador);

        EquipoJugador resultado = servicioEquipoJugadorImpl.buscarPorEquipoYUsuario(equipo, jugador);

        verify(repositorioEquipoJugador).buscarPorEquipoYUsuario(equipo, jugador);
        assertThat(resultado, equalTo(equipoJugador));
    }

    @Test
    public void queSePuedaEliminarEquipoJugadorPorId() {
        Long id = 1L;
        Usuario creador = new Usuario();
        Equipo equipo = new Equipo("Equipo Test", creador, java.time.LocalDateTime.now());
        Usuario jugador = new Usuario();
        EquipoJugador equipoJugador = new EquipoJugador(equipo, jugador);

        when(repositorioEquipoJugador.buscarPorId(id)).thenReturn(equipoJugador);

        servicioEquipoJugadorImpl.eliminarPorId(id);

        verify(repositorioEquipoJugador).buscarPorId(id);
        verify(repositorioEquipoJugador).eliminar(equipoJugador);
    }
}