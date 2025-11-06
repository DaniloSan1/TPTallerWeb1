package com.tallerwebi.dominio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ServicioCalificacionImplTest {

    @Mock
    private RepositorioCalificacion repositorioCalificacion;

    @InjectMocks
    private ServicioCalificacionImpl servicioCalificacion;

    private Usuario calificador;
    private Usuario calificado;
    private Partido partido;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        calificador = new Usuario();
        calificador.setId(1L);

        calificado = new Usuario();
        calificado.setId(2L);

        partido = new Partido();
        partido.setId(3L);
    }

    @Test
    public void deberiaGuardarCalificacionSiNoExiste() {
        when(repositorioCalificacion.existeCalificacion(1L, 2L, 3L)).thenReturn(false);

        servicioCalificacion.calificarJugador(calificador, calificado, partido, 5, "Buen partido");

        verify(repositorioCalificacion).guardarCalificacion(any(Calificacion.class));
    }

    @Test
    public void noDeberiaGuardarCalificacionSiYaExiste() {
        when(repositorioCalificacion.existeCalificacion(1L, 2L, 3L)).thenReturn(true);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> servicioCalificacion.calificarJugador(calificador, calificado, partido, 4, "Ya existe")
        );

        assertEquals("Ya calificaste a este jugador", ex.getMessage());
        verify(repositorioCalificacion, never()).guardarCalificacion(any());
    }

    @Test
    public void deberiaObtenerCalificacionesPorPartido() {
        Calificacion c1 = new Calificacion(calificador, calificado, partido, 5, "Bien");
        Calificacion c2 = new Calificacion(calificador, calificado, partido, 3, "Regular");

        when(repositorioCalificacion.obtenerPorPartido(3L)).thenReturn(Arrays.asList(c1, c2));

        List<Calificacion> resultado = servicioCalificacion.obtenerCalificacionesPorPartido(3L);

        assertEquals(2, resultado.size());
        assertEquals(5, resultado.get(0).getPuntuacion());
        verify(repositorioCalificacion).obtenerPorPartido(3L);
    }
}