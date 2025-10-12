package com.tallerwebi.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.cglib.core.Local;

import com.tallerwebi.dominio.excepcion.PartidoNoEncontrado;

public class ServicioPartidoImplTest {
    private RepositorioPartido repositorioPartidoMock;
    private RepositorioReserva repositorioReservaMock;

    @BeforeEach
    public void init() {
        repositorioPartidoMock = Mockito.mock(RepositorioPartido.class);
        repositorioReservaMock = Mockito.mock(RepositorioReserva.class);
    }

    @Test
    public void deberiaDevolverExcepcionCuandoNoSeEncuentraElPartido() throws PartidoNoEncontrado {
        Mockito.when(repositorioPartidoMock.porId(Mockito.anyLong())).thenReturn(null);
        ServicioPartido servicioPartido = new ServicioPartidoImpl(repositorioPartidoMock, repositorioReservaMock);
        assertThrows(PartidoNoEncontrado.class, () -> servicioPartido.obtenerPorId(1L));
        Mockito.verify(repositorioPartidoMock, Mockito.times(1)).porId(Mockito.anyLong());
    }

    @Test
    public void deberiaDevolverUnPartidoExistente() throws PartidoNoEncontrado {
        Cancha cancha = new Cancha("Cancha 1", null, null, "Direccion 1", Zona.NORTE);
        Horario horario = new Horario(cancha, DayOfWeek.MONDAY, LocalTime.now(), LocalTime.now().plusHours(1));
        Usuario creador = new Usuario("usuario1", "password", "email@example.com");
        Partido partidoEsperado = new Partido(null,"Partido de prueba", "Descripción del partido", Zona.NORTE,
                Nivel.INTERMEDIO, LocalDateTime.now(),
                10, horario, creador);

        Long id = Mockito.anyLong();
        Mockito.when(repositorioPartidoMock.porId(id)).thenReturn(partidoEsperado);

        ServicioPartido servicioPartido = new ServicioPartidoImpl(repositorioPartidoMock, repositorioReservaMock);
        Partido partidoObtenido = servicioPartido.obtenerPorId(id);
        assertEquals(partidoEsperado, partidoObtenido);
        Mockito.verify(repositorioPartidoMock, Mockito.times(1)).porId(Mockito.anyLong());
    }
}
