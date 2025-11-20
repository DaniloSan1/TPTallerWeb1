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
    private RepositorioReserva repositorioReservaMock;
    private RepositorioPartido repositorioPartidoMock;
    private Cancha cancha1Mock;
    private Cancha cancha2Mock;
    


    @BeforeEach
    public void setUp() {
    repositorioCanchaMock = Mockito.mock(RepositorioCancha.class);
    repositorioReservaMock = Mockito.mock(RepositorioReserva.class);
    repositorioPartidoMock = Mockito.mock(RepositorioPartido.class);
    cancha1Mock =    Mockito.mock(Cancha.class);
    cancha2Mock =    Mockito.mock(Cancha.class);  
    }

    @Test
    public void CuandoHayCanchasDisponiblesDeberiaTraerLaListaDeLasMismas() {
        
        Mockito.when(repositorioCanchaMock.MostrarCanchasConHorariosDisponibles(null, null, null, null)).thenReturn(List.of(cancha1Mock, cancha2Mock));
    ServicioCanchaImpl servicio = new ServicioCanchaImpl(repositorioCanchaMock, repositorioReservaMock);

        
        List<Cancha> canchasDisponibles = servicio.obtenerCanchasDisponibles(null, null, 0.0);

        
        assertNotNull(canchasDisponibles);
        assertEquals(2, canchasDisponibles.size());
    }

    @Test
    public void CuandoSeBuscaUnaCanchaPorIdDeberiaRetornarLaCanchaCorrespondiente() {
        
        Mockito.when(repositorioCanchaMock.BuscarCanchaPorId(1L)).thenReturn(cancha1Mock);
    ServicioCanchaImpl servicio = new ServicioCanchaImpl(repositorioCanchaMock, repositorioReservaMock);

        
        Cancha cancha = servicio.obtenerCanchaPorId(1L);

        
        assertNotNull(cancha);
        assertEquals(cancha1Mock, cancha);
    }

    @Test
    public void CuandoSeBuscaUnaCanchaQueNoExisteDeberiaTirarErrorCanchaNoEncontrada() throws CanchaNoEncontrada {
        
        Mockito.when(repositorioCanchaMock.BuscarCanchaPorId(999L)).thenReturn(null);
    ServicioCanchaImpl servicio = new ServicioCanchaImpl(repositorioCanchaMock, repositorioReservaMock);

        
        assertThrows(CanchaNoEncontrada.class, () -> {
            servicio.obtenerCanchaPorId(999L);
        });
    }

    @Test
    public void CuandoNoHayCanchasDisponiblesDeberiaTirarErrorNoHayCanchasDisponibles() {
        
        Mockito.when(repositorioCanchaMock.MostrarCanchasConHorariosDisponibles(null, null, null, null)).thenReturn(List.of());
    ServicioCanchaImpl servicio = new ServicioCanchaImpl(repositorioCanchaMock, repositorioReservaMock);

        
        assertThrows(NoHayCanchasDisponibles.class, () -> {
            servicio.obtenerCanchasDisponibles(null, null, 0.0);
        });
    }
    @Test
    public void CuandoSeBuscaUnaCanchaPorNombreDeberiaRetornarLaCanchaCorrespondiente() {

        Mockito.when(repositorioCanchaMock.MostrarCanchasConHorariosDisponibles("Cancha1",null ,null, null)).thenReturn(List.of(cancha1Mock));
    ServicioCanchaImpl servicio = new ServicioCanchaImpl(repositorioCanchaMock, repositorioReservaMock);


        Cancha cancha = servicio.obtenerCanchasDisponibles("Cancha1", null, 0.0).get(0);

        assertNotNull(cancha);
        assertEquals(cancha1Mock, cancha);
    }
    
    @Test
    public void CuandoSeBuscaUnaCanchaPorZonaDeberiaRetornarLaCanchaCorrespondiente() {

        Zona zonaMock = Mockito.mock(Zona.class);
        Mockito.when(repositorioCanchaMock.MostrarCanchasConHorariosDisponibles(null, zonaMock ,null, null)).thenReturn(List.of(cancha2Mock));
    ServicioCanchaImpl servicio = new ServicioCanchaImpl(repositorioCanchaMock, repositorioReservaMock);

        Cancha cancha = servicio.obtenerCanchasDisponibles(null, zonaMock, 0.0).get(0);

        assertNotNull(cancha);
        assertEquals(cancha2Mock, cancha);
    }
    
    
    @Test
    public void CuandoSeBuscaUnaCanchaPorNombreYZonaDeberiaRetornarLaCanchaCorrespondiente() {

        Zona zonaMock = Mockito.mock(Zona.class);
        Mockito.when(repositorioCanchaMock.MostrarCanchasConHorariosDisponibles("Cancha1", zonaMock ,null, null)).thenReturn(List.of(cancha1Mock));
    ServicioCanchaImpl servicio = new ServicioCanchaImpl(repositorioCanchaMock, repositorioReservaMock);

        Cancha cancha = servicio.obtenerCanchasDisponibles("Cancha1", zonaMock, 0.0).get(0);

        assertNotNull(cancha);
        assertEquals(cancha1Mock, cancha);
    }

    @Test
    public void CuandoSeBuscaUnaCanchaPorRangoDePrecioDeberiaRetornarLaCanchaCorrespondiente() {

        Mockito.when(repositorioCanchaMock.MostrarCanchasConHorariosDisponibles(null, null ,0.0, 1000.0)).thenReturn(List.of(cancha1Mock));
    ServicioCanchaImpl servicio = new ServicioCanchaImpl(repositorioCanchaMock, repositorioReservaMock);

        Cancha cancha = servicio.obtenerCanchasDisponibles(null, null, 1000.00).get(0);

        assertNotNull(cancha);
        assertEquals(cancha1Mock, cancha);
    }

    @Test
    public void CuandoSeBuscaUnaCanchaPorNombreZonaYRangoDePrecioDeberiaRetornarLaCanchaCorrespondiente() {

        Zona zonaMock = Mockito.mock(Zona.class);
        Mockito.when(repositorioCanchaMock.MostrarCanchasConHorariosDisponibles("Cancha1", zonaMock ,0.0, 1000.0)).thenReturn(List.of(cancha1Mock));
    ServicioCanchaImpl servicio = new ServicioCanchaImpl(repositorioCanchaMock, repositorioReservaMock);

        Cancha cancha = servicio.obtenerCanchasDisponibles("Cancha1", zonaMock, 1000.00).get(0);

        assertNotNull(cancha);
        assertEquals(cancha1Mock, cancha);
    }
    @Test
    public void CuandoSeBscaUnaCanchaPorNombreYRangoDePrecioDeberiaRetoenarLaCanchaCorrespondiente() {

        Mockito.when(repositorioCanchaMock.MostrarCanchasConHorariosDisponibles("Cancha1", null ,0.0, 1000.0)).thenReturn(List.of(cancha1Mock));
    ServicioCanchaImpl servicio = new ServicioCanchaImpl(repositorioCanchaMock, repositorioReservaMock);

        Cancha cancha = servicio.obtenerCanchasDisponibles("Cancha1", null, 1000.00).get(0);

        assertNotNull(cancha);
        assertEquals(cancha1Mock, cancha);
    }
    @Test
    public void CuandoSeBuscaUnaCanchaPorZonaYRangoDePrecioDeberiaRetoenarLaCanchaCorrespondiente() {

        Zona zonaMock = Mockito.mock(Zona.class);
        Mockito.when(repositorioCanchaMock.MostrarCanchasConHorariosDisponibles(null, zonaMock ,0.0, 1000.0)).thenReturn(List.of(cancha1Mock));
    ServicioCanchaImpl servicio = new ServicioCanchaImpl(repositorioCanchaMock, repositorioReservaMock);

        Cancha cancha = servicio.obtenerCanchasDisponibles(null, zonaMock, 1000.00).get(0);

        assertNotNull(cancha);
        assertEquals(cancha1Mock, cancha);
    }
}
