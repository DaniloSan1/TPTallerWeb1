package com.tallerwebi.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.tallerwebi.dominio.excepcion.ParticipanteNoEncontrado;

public class ServicioPartidoParticipanteImplTest {
    private ServicioPartidoParticipanteImpl servicioPartidoParticipanteImpl;
    private RepositorioPartidoParticipante repositorioMock;
    private Usuario usuarioMock;
    private Partido partidoMock;

    @BeforeEach
    public void setUp() {
        repositorioMock = mock(RepositorioPartidoParticipante.class);
        servicioPartidoParticipanteImpl = new ServicioPartidoParticipanteImpl(repositorioMock);
        usuarioMock = mock(Usuario.class);
        partidoMock = mock(Partido.class);
    }

    @Test
    public void deberiaActualizarElEquipoDeUnParticipanteExistente() throws ParticipanteNoEncontrado {
        PartidoParticipante partidoParticipanteMock = new PartidoParticipante(partidoMock, usuarioMock,
                Equipo.EQUIPO_1);
        partidoParticipanteMock.setId(1L);
        when(repositorioMock.buscarPorId(Mockito.anyLong()))
                .thenReturn(partidoParticipanteMock);
        PartidoParticipante partidoParticipante = servicioPartidoParticipanteImpl.actualizarEquipo(Mockito.anyLong(),
                Equipo.EQUIPO_2.toString());
        assertEquals(1L, partidoParticipante.getId());
        assertEquals(Equipo.EQUIPO_2, partidoParticipante.getEquipo());
    }

    @Test
    public void deberiaDarErrorSiSeIntentaActualizarElEquipoDeUnParticipanteInexistente()
            throws ParticipanteNoEncontrado {
        when(repositorioMock.buscarPorId(Mockito.anyLong()))
                .thenReturn(null);
        assertThrows(ParticipanteNoEncontrado.class, () -> {
            servicioPartidoParticipanteImpl.actualizarEquipo(Mockito.anyLong(),
                    Equipo.EQUIPO_2.toString());
        });
    }

    @Test
    public void deberiaEliminarUnParticipanteExistente() throws ParticipanteNoEncontrado {
        PartidoParticipante partidoParticipanteMock = new PartidoParticipante(partidoMock, usuarioMock,
                Equipo.EQUIPO_1);
        partidoParticipanteMock.setId(1L);
        when(repositorioMock.buscarPorId(Mockito.anyLong()))
                .thenReturn(partidoParticipanteMock);
        servicioPartidoParticipanteImpl.eliminar(Mockito.anyLong());
    }

    @Test
    public void deberiaDarErrorSiSeIntentaEliminarUnParticipanteInexistente() throws ParticipanteNoEncontrado {
        when(repositorioMock.buscarPorId(Mockito.anyLong()))
                .thenReturn(null);
        assertThrows(ParticipanteNoEncontrado.class, () -> {
            servicioPartidoParticipanteImpl.eliminar(Mockito.anyLong());
        });
    }
}