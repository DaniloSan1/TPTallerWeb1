package com.tallerwebi.dominio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.*;

public class ServicioPartidoEquipoImplTest {
    private ServicioPartidoEquipoImpl servicioPartidoEquipo;
    private RepositorioGoles repositorioGoles;
    private RepositorioPartidoEquipo repositorioPartidoEquipo;

    @BeforeEach
    public void init() {
        repositorioGoles = mock(RepositorioGoles.class);
        repositorioPartidoEquipo = mock(RepositorioPartidoEquipo.class);
        servicioPartidoEquipo = new ServicioPartidoEquipoImpl(repositorioGoles, repositorioPartidoEquipo);
    }

    @Test
    public void queSeActualicenGolesPorEquipoCorrectamente() {
        // Arrange
        Partido partido = mock(Partido.class);
        EquipoJugador ej1 = mock(EquipoJugador.class);
        Equipo equipo1 = mock(Equipo.class);
        when(ej1.getEquipo()).thenReturn(equipo1);
        when(equipo1.getId()).thenReturn(1L);
        Gol gol1 = new Gol(partido, ej1, 2);

        List<Gol> goles = Arrays.asList(gol1);

        PartidoEquipo pe1 = mock(PartidoEquipo.class);
        when(pe1.getEquipo()).thenReturn(equipo1);

        when(partido.getEquipos()).thenReturn(new HashSet<>(Arrays.asList(pe1)));
        when(repositorioGoles.buscarPorPartido(partido)).thenReturn(goles);

        // Act
        servicioPartidoEquipo.actualizarGolesPorEquipo(partido);

        // Assert
        verify(pe1).setGoles(2);
        verify(repositorioPartidoEquipo).guardar(pe1);
    }
}