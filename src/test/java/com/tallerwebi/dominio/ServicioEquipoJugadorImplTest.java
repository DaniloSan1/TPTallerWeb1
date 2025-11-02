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
}