package com.tallerwebi.dominio;

import static org.mockito.ArgumentMatchers.anyLong;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ServicioFotoCanchaTest {
    RepositorioFotoCancha repositorioFotoCanchaMock;
    FotoCancha fotoCancha1Mock;
    FotoCancha fotoCancha2Mock;
    FotoCancha fotoCancha3Mock;

    @BeforeEach
    public void init() {
        repositorioFotoCanchaMock = Mockito.mock(RepositorioFotoCancha.class);
        fotoCancha1Mock = Mockito.mock(FotoCancha.class);
        fotoCancha2Mock = Mockito.mock(FotoCancha.class);
        fotoCancha3Mock = Mockito.mock(FotoCancha.class);
    }

    
    @Test
    public void queCuandoObtengoFotosDeCanchaMeRetornaLaListaCorrecta() {
        Long canchaIdBuscado = 1L;
        Mockito.when(repositorioFotoCanchaMock.obtenerFotosCancha(canchaIdBuscado))
               .thenReturn(List.of(fotoCancha1Mock, fotoCancha2Mock));

        ServicioFotoCancha servicioFotoCancha = new ServicioFotoCanchaImpl(repositorioFotoCanchaMock);
        var resultados = servicioFotoCancha.obtenerFotosCancha(canchaIdBuscado);

        Assertions.assertEquals(2, resultados.size());
        Assertions.assertTrue(resultados.contains(fotoCancha1Mock));
        Assertions.assertTrue(resultados.contains(fotoCancha2Mock));
    }


    @Test
    public void queCuandoObtengoPrimeraFotoDeCanchaMeRetornaLaFotoCorrecta() {
        Long canchaIdBuscado = 1L;
        Mockito.when(repositorioFotoCanchaMock.obtenerPrimeraFotoCancha(canchaIdBuscado))
               .thenReturn(fotoCancha3Mock);

        ServicioFotoCancha servicioFotoCancha = new ServicioFotoCanchaImpl(repositorioFotoCanchaMock);
        FotoCancha resultado = servicioFotoCancha.obtenerPrimeraFotoCancha(canchaIdBuscado);

        Assertions.assertEquals(fotoCancha3Mock, resultado);
    }


    @Test
    public void queCuandoInsertoFotosAModelCanchasMeRetornaLaListaCorrecta() {
        Cancha cancha1Mock = Mockito.mock(Cancha.class);
        Cancha cancha2Mock = Mockito.mock(Cancha.class);
        List<Cancha> canchas = List.of(cancha1Mock, cancha2Mock);
        List<FotoCancha> fotosCancha = List.of(fotoCancha1Mock, fotoCancha2Mock);
        Mockito.when(repositorioFotoCanchaMock.obtenerFotosCancha(anyLong()))
               .thenReturn(fotosCancha);
        ServicioFotoCancha servicioFotoCancha = new ServicioFotoCanchaImpl(repositorioFotoCanchaMock);
        var resultados = servicioFotoCancha.insertarFotosAModelCanchas(canchas);

        Assertions.assertEquals(2, resultados.size());

    }


    @Test
    public void queCuandoInsertoFotosAlModelPartidosMeRetornaLaListaCorrecta() {
        Partido partido1Mock = Mockito.mock(Partido.class);
        Partido partido2Mock = Mockito.mock(Partido.class);

        Mockito.when(partido1Mock.getCancha()).thenReturn(Mockito.mock(Cancha.class));
        Mockito.when(partido2Mock.getCancha()).thenReturn(Mockito.mock(Cancha.class));

        List<Partido> partidos = List.of(partido1Mock, partido2Mock);

        Mockito.when(repositorioFotoCanchaMock.obtenerPrimeraFotoCancha(partido1Mock.getCancha().getId()))
               .thenReturn(fotoCancha1Mock);
        Mockito.when(repositorioFotoCanchaMock.obtenerPrimeraFotoCancha(partido2Mock.getCancha().getId()))
               .thenReturn(fotoCancha2Mock);

        ServicioFotoCancha servicioFotoCancha = new ServicioFotoCanchaImpl(repositorioFotoCanchaMock);
        List<FotoCancha> resultados = servicioFotoCancha.insertarFotosAModelPartidos(partidos);

        Assertions.assertEquals(2, resultados.size());
    }
    
}
