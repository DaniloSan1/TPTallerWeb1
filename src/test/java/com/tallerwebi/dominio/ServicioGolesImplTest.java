package com.tallerwebi.dominio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.*;

public class ServicioGolesImplTest {
    private ServicioGolesImpl servicioGoles;
    private RepositorioGoles repositorioGoles;
    private ServicioPartido servicioPartido;
    private ServicioPartidoEquipo servicioPartidoEquipo;
    private ServicioUsuario servicioUsuario;

    @BeforeEach
    public void init() {
        repositorioGoles = mock(RepositorioGoles.class);
        servicioPartido = mock(ServicioPartido.class);
        servicioPartidoEquipo = mock(ServicioPartidoEquipo.class);
        servicioGoles = new ServicioGolesImpl(repositorioGoles, servicioPartido, servicioPartidoEquipo,servicioUsuario);
    }

    @Test
    public void queSePuedanRegistrarGolesYActualizarPartidoEquipo() {
        // Arrange
        Partido partido = mock(Partido.class);
        Usuario usuario = mock(Usuario.class);
        EquipoJugador ej1 = mock(EquipoJugador.class);
        Equipo equipo1 = mock(Equipo.class);
        when(ej1.getEquipo()).thenReturn(equipo1);
        when(equipo1.getId()).thenReturn(1L);
        Gol gol1 = new Gol(partido, ej1, 2);

        EquipoJugador ej2 = mock(EquipoJugador.class);
        Equipo equipo2 = mock(Equipo.class);
        when(ej2.getEquipo()).thenReturn(equipo2);
        when(equipo2.getId()).thenReturn(2L);
        Gol gol2 = new Gol(partido, ej2, 1);

        List<Gol> goles = Arrays.asList(gol1, gol2);

        PartidoEquipo pe1 = mock(PartidoEquipo.class);
        when(pe1.getEquipo()).thenReturn(equipo1);
        PartidoEquipo pe2 = mock(PartidoEquipo.class);
        when(pe2.getEquipo()).thenReturn(equipo2);

        when(partido.getEquipos()).thenReturn(new HashSet<>(Arrays.asList(pe1, pe2)));
        when(repositorioGoles.buscarPorPartido(partido)).thenReturn(goles);

        // Act
        servicioGoles.registrarGoles(partido, goles, usuario);

        // Assert
        verify(servicioPartido).finalizarPartido(partido, usuario);
        verify(repositorioGoles, times(2)).guardar(any(Gol.class));
        verify(servicioPartidoEquipo).actualizarGolesPorEquipo(partido);
    }
}