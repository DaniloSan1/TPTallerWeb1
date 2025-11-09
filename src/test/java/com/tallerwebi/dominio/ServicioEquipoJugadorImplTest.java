package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.ParticipanteNoEncontrado;
import com.tallerwebi.infraestructura.RepositorioEquipoJugadorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;

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
        Equipo equipo = new Equipo("Equipo Test", "Descripción", creador, java.time.LocalDateTime.now());
        Usuario jugador = new Usuario();

        EquipoJugador equipoJugadorCreado = servicioEquipoJugadorImpl.crearEquipoJugador(equipo, jugador);

        verify(repositorioEquipoJugador).guardar(any(EquipoJugador.class));
        assertThat(equipoJugadorCreado.getEquipo(), equalTo(equipo));
        assertThat(equipoJugadorCreado.getUsuario(), equalTo(jugador));
    }

    @Test
    public void queSePuedaBuscarEquipoJugadorPorEquipoYUsuario() {
        Usuario creador = new Usuario();
        Equipo equipo = new Equipo("Equipo Test", "Descripción", creador, java.time.LocalDateTime.now());
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
        Equipo equipo = new Equipo("Equipo Test", "Descripción", creador, java.time.LocalDateTime.now());
        Usuario jugador = new Usuario();
        EquipoJugador equipoJugador = new EquipoJugador(equipo, jugador);

        when(repositorioEquipoJugador.buscarPorId(id)).thenReturn(equipoJugador);

        servicioEquipoJugadorImpl.eliminarPorId(id);

        verify(repositorioEquipoJugador).buscarPorId(id);
        verify(repositorioEquipoJugador).eliminar(equipoJugador);
    }

    @Test
    public void queSePuedaPromoverCapitan() throws ParticipanteNoEncontrado {
        // Arrange
        Long idNuevoCapitan = 1L;
        Usuario creador = new Usuario();
        Equipo equipo = new Equipo("Equipo Test", "Descripción", creador, java.time.LocalDateTime.now());
        Usuario jugador1 = new Usuario();
        Usuario jugador2 = new Usuario();
        EquipoJugador nuevoCapitan = new EquipoJugador(equipo, jugador1);
        nuevoCapitan.setId(idNuevoCapitan);
        EquipoJugador capitanActual = new EquipoJugador(equipo, jugador2);
        capitanActual.setEsCapitan(true);
        List<EquipoJugador> jugadores = Arrays.asList(nuevoCapitan, capitanActual);

        when(repositorioEquipoJugador.buscarPorId(idNuevoCapitan)).thenReturn(nuevoCapitan);
        when(repositorioEquipoJugador.buscarPorEquipo(equipo)).thenReturn(jugadores);

        // Act
        servicioEquipoJugadorImpl.promoverCapitan(idNuevoCapitan);

        // Assert
        verify(repositorioEquipoJugador).buscarPorId(idNuevoCapitan);
        verify(repositorioEquipoJugador).buscarPorEquipo(equipo);
        assertThat(capitanActual.isEsCapitan(), equalTo(false));
        assertThat(nuevoCapitan.isEsCapitan(), equalTo(true));
    }

    @Test
    public void queLanceExcepcionSiParticipanteNoEncontradoAlPromoverCapitan() {
        // Arrange
        Long idInexistente = 999L;
        when(repositorioEquipoJugador.buscarPorId(idInexistente)).thenReturn(null);

        // Act & Assert
        assertThrows(ParticipanteNoEncontrado.class, () -> {
            servicioEquipoJugadorImpl.promoverCapitan(idInexistente);
        });
        verify(repositorioEquipoJugador).buscarPorId(idInexistente);
    }
}