package com.tallerwebi.dominio;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.tallerwebi.dominio.excepcion.CanchaNoEncontrada;
import com.tallerwebi.dominio.excepcion.NoHayCanchasDisponibles;

public class ServicioCanchaImplTest {
    private RepositorioCancha repositorioCanchaMock;
    private Cancha cancha1Mock;
    private Cancha cancha2Mock;
    


    @BeforeEach
    public void setUp() {
        repositorioCanchaMock = Mockito.mock(RepositorioCancha.class);
        cancha1Mock =    Mockito.mock(Cancha.class);
        cancha2Mock =    Mockito.mock(Cancha.class);  
    }

    @Test
    public void CuandoHayCanchasDisponiblesDeberiaTraerLaListaDeLasMismas() {
        
        Mockito.when(repositorioCanchaMock.MostrarCanchasConHorariosDisponibles(null, null, null, null)).thenReturn(List.of(cancha1Mock, cancha2Mock));
        ServicioCanchaImpl servicio = new ServicioCanchaImpl(repositorioCanchaMock);

        
        List<Cancha> canchasDisponibles = servicio.obtenerCanchasDisponibles(null, null, 0.0);

        
        assertNotNull(canchasDisponibles);
        assertEquals(2, canchasDisponibles.size());
    }

    @Test
    public void CuandoSeBuscaUnaCanchaPorIdDeberiaRetornarLaCanchaCorrespondiente() {
        
        Mockito.when(repositorioCanchaMock.BuscarCanchaPorId(1L)).thenReturn(cancha1Mock);
        ServicioCanchaImpl servicio = new ServicioCanchaImpl(repositorioCanchaMock);

        
        Cancha cancha = servicio.obtenerCanchaPorId(1L);

        
        assertNotNull(cancha);
        assertEquals(cancha1Mock, cancha);
    }

    @Test
    public void CuandoSeBuscaUnaCanchaQueNoExisteDeberiaTirarErrorCanchaNoEncontrada() throws CanchaNoEncontrada {
        
        Mockito.when(repositorioCanchaMock.BuscarCanchaPorId(999L)).thenReturn(null);
        ServicioCanchaImpl servicio = new ServicioCanchaImpl(repositorioCanchaMock);

        
        assertThrows(CanchaNoEncontrada.class, () -> {
            servicio.obtenerCanchaPorId(999L);
        });
    }

    @Test
    public void CuandoNoHayCanchasDisponiblesDeberiaTirarErrorNoHayCanchasDisponibles() {
        
        Mockito.when(repositorioCanchaMock.MostrarCanchasConHorariosDisponibles(null, null, null, null)).thenReturn(List.of());
        ServicioCanchaImpl servicio = new ServicioCanchaImpl(repositorioCanchaMock);

        
        assertThrows(NoHayCanchasDisponibles.class, () -> {
            servicio.obtenerCanchasDisponibles(null, null, 0.0);
        });
    }
}
