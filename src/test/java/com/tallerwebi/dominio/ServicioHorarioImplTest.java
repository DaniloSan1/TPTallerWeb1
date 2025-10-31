package com.tallerwebi.dominio;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ServicioHorarioImplTest {
    RepositorioHorario repositorioHorarioMock;
    Horario horario1Mock;
    Horario horario2Mock;

    @BeforeEach
    public void setUp() {
        repositorioHorarioMock = Mockito.mock(RepositorioHorario.class);
        horario1Mock = Mockito.mock(Horario.class);
        horario2Mock = Mockito.mock(Horario.class);
    }

    @Test
    public void queCuandoObtengoUnHorarioPorIdMeRetornaElHorarioCorrecto() {
        Long idBuscado = 1L;
        Mockito.when(repositorioHorarioMock.obtenerPorId(idBuscado)).thenReturn(horario1Mock);

        ServicioHorario servicioHorario = new ServicioHorarioImpl(repositorioHorarioMock);
        Horario resultado = servicioHorario.obtenerPorId(idBuscado);

        Assertions.assertEquals(horario1Mock, resultado);
    }

    @Test
    public void queCuandoObtengoUnHorarioPorIdNoExistenteMeRetornaNull() {
        Long idBuscado = 2L;
        Mockito.when(repositorioHorarioMock.obtenerPorId(idBuscado)).thenReturn(null);

        ServicioHorario servicioHorario = new ServicioHorarioImpl(repositorioHorarioMock);
        Horario resultado = servicioHorario.obtenerPorId(idBuscado);

        Assertions.assertNull(resultado);
    }

    @Test
    public void queCuandoObtengoHorariosPorCanchaMeRetornaLaListaCorrecta(){
        Cancha canchaMock = Mockito.mock(Cancha.class);
        Mockito.when(repositorioHorarioMock.obtenerPorCancha(canchaMock))
               .thenReturn(List.of(horario1Mock, horario2Mock));

        ServicioHorario servicioHorario = new ServicioHorarioImpl(repositorioHorarioMock);
        var resultados = servicioHorario.obtenerPorCancha(canchaMock);

        Assertions.assertEquals(2, resultados.size());
        Assertions.assertTrue(resultados.contains(horario1Mock));
        Assertions.assertTrue(resultados.contains(horario2Mock));
    }

    @Test
    public void queCuandoObtengoHorariosDisponiblesPorCanchaMeRetornaLaListaCorrecta(){
        Cancha canchaMock = Mockito.mock(Cancha.class);
        Mockito.when(horario1Mock.getDisponible()).thenReturn(true);
        Mockito.when(repositorioHorarioMock.obtenerDisponiblesPorCancha(canchaMock))
               .thenReturn(List.of(horario1Mock));

        ServicioHorario servicioHorario = new ServicioHorarioImpl(repositorioHorarioMock);
        var resultados = servicioHorario.obtenerDisponiblesPorCancha(canchaMock);

        Assertions.assertEquals(1, resultados.size());
        Assertions.assertTrue(resultados.contains(horario1Mock));
        Assertions.assertTrue(horario1Mock.getDisponible());
    }
}
