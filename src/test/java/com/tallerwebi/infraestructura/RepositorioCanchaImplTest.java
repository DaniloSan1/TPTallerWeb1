package com.tallerwebi.infraestructura;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;


import javax.transaction.Transactional;

import com.tallerwebi.dominio.Cancha;
import com.tallerwebi.dominio.Horario;
import com.tallerwebi.infraestructura.config.HibernateTestInfraestructuraConfig;
import com.tallerwebi.dominio.RepositorioCancha;
import com.tallerwebi.dominio.Zona;

import org.hibernate.SessionFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestInfraestructuraConfig.class})
public class RepositorioCanchaImplTest {
    @Autowired
    private SessionFactory sessionFactory;
    private RepositorioCancha repositorioCancha;
    private Cancha cancha1;
    private Cancha cancha2;
    private Cancha cancha3;
    private Horario horario1;
    private Horario horario2;

    @BeforeEach
    public void init() {
    RepositorioCanchaIpl repo = new RepositorioCanchaIpl(this.sessionFactory);
    this.repositorioCancha = repo;
        cancha1 = new Cancha();
        cancha2 = new Cancha();
        cancha3 = new Cancha();
        horario1 = new Horario();
        horario2 = new Horario();
        cancha1.setId(1L);
        cancha1.setNombre("Cancha 1");
        cancha1.setDireccion("Direccion 1");
        cancha1.setCapacidad(10);
        cancha1.setPrecio(800.0);
        cancha1.setZona(Zona.NORTE);

        
        cancha2.setId(2L);
        cancha2.setNombre("Cancha 2");
        cancha2.setDireccion("Direccion 2");
        cancha2.setCapacidad(20);
        cancha2.setPrecio(3000.00);
        cancha2.setZona(Zona.SUR);

        cancha3.setId(3L);
        cancha3.setNombre("Cancha 3");
        cancha3.setDireccion("Direccion 3");
        cancha3.setCapacidad(30);

        
        horario1.setId(1L);
        horario1.setCancha(cancha1);
        horario1.setDiaSemana(DayOfWeek.MONDAY);
        horario1.setHoraInicio(LocalTime.now());
        horario1.setHoraFin(LocalTime.now().plusHours(1));

       
        horario2.setId(2L);
        horario2.setCancha(cancha3);
        horario2.setDiaSemana(DayOfWeek.MONDAY);
        horario2.setHoraInicio(LocalTime.now());
        horario2.setHoraFin(LocalTime.now().plusHours(1));


        this.sessionFactory.getCurrentSession().save(cancha1);
        this.sessionFactory.getCurrentSession().save(cancha2);
        this.sessionFactory.getCurrentSession().save(cancha3);
        this.sessionFactory.getCurrentSession().save(horario1);
        this.sessionFactory.getCurrentSession().save(horario2);
    }

    @Test
    @Transactional
    @Rollback
    public void MostrarSoloDisponibles() {
       //Ejecucion
        List<Cancha> canchasDisponibles = repositorioCancha.MostrarCanchasConHorariosDisponibles(null, null, null, null);

       //Verificacion
        assertEquals(2, canchasDisponibles.size());
        assertTrue(canchasDisponibles.contains(cancha1));
        assertTrue(canchasDisponibles.contains(cancha3));
        assertFalse(canchasDisponibles.contains(cancha2));
    }
    
    
    @Test
    @Transactional
    @Rollback
    public void dadoQueHayUnaCanchaGuardadaBuscarlaPorId() {
        // Preparación
        Cancha cancha = new Cancha("Cancha 1", "Calle", null, null, null);
        this.sessionFactory.getCurrentSession().save(cancha);
        this.sessionFactory.getCurrentSession().flush();
        // Ejecución
        Cancha canchaEncontrada = this.repositorioCancha.BuscarCanchaPorId(cancha.getId());

        // Verificación
        assertEquals(cancha.getId(), canchaEncontrada.getId());
        assertEquals(cancha.getNombre(), canchaEncontrada.getNombre());
        assertEquals(cancha.getDireccion(), canchaEncontrada.getDireccion());
    }

    @Test
    @Transactional
    @Rollback
    public void MostrarSoloDisponiblesConFiltroNombre() {
        //Ejecucion
        List<Cancha> canchasDisponibles = repositorioCancha.MostrarCanchasConHorariosDisponibles("Cancha 1", null, null, null);

        //Verificacion
        assertEquals(1, canchasDisponibles.size());
        assertTrue(canchasDisponibles.contains(cancha1));
        assertFalse(canchasDisponibles.contains(cancha2));
    }

    @Test
    @Transactional
    @Rollback
    public void MostrarSoloDisponiblesConFiltroPrecio() {
        //Ejecucion
        List<Cancha> canchasDisponibles = repositorioCancha.MostrarCanchasConHorariosDisponibles(null, null, 0.0, 1000.0);

        //Verificacion
        assertEquals(1, canchasDisponibles.size());
        assertTrue(canchasDisponibles.contains(cancha1));
        assertFalse(canchasDisponibles.contains(cancha2));
    }

    @Test
    @Transactional
    @Rollback
    public void MostrarSoloDisponiblesConFiltroZona() {
        //Ejecucion
        List<Cancha> canchasDisponibles = repositorioCancha.MostrarCanchasConHorariosDisponibles(null, Zona.NORTE, null, null);

        //Verificacion
        assertEquals(1, canchasDisponibles.size());
        assertTrue(canchasDisponibles.contains(cancha1));
        assertFalse(canchasDisponibles.contains(cancha2));
    }

    @Test
    @Transactional
    @Rollback
    public void MostrarSoloDisponiblesConTodosLosFiltrosCombinados() {
        //Ejecucion
        List<Cancha> canchasDisponibles = repositorioCancha.MostrarCanchasConHorariosDisponibles("Cancha 1", Zona.NORTE, 0.0, 1000.0);

        //Verificacion
        assertEquals(1, canchasDisponibles.size());
        assertTrue(canchasDisponibles.contains(cancha1));
        assertFalse(canchasDisponibles.contains(cancha2));
    }

    @Test
    @Transactional
    @Rollback
    public void NoDeberiaEncontrarCanchasDisponiblesConFiltrosQueNoCoinciden() {
        //Ejecucion
        List<Cancha> canchasDisponibles = repositorioCancha.MostrarCanchasConHorariosDisponibles("Cancha Inexistente", Zona.SUR, 0.0, 1000.0);

        //Verificacion
        assertEquals(0, canchasDisponibles.size());
        assertFalse(canchasDisponibles.contains(cancha1));
        assertFalse(canchasDisponibles.contains(cancha2));
    }

    @Test
    @Transactional
    @Rollback
    public void dadoQueNoHayCanchasGuardadasBuscarUnaPorIdDeberiaRetornarNull() {
        // Ejecución
        Cancha canchaEncontrada = this.repositorioCancha.BuscarCanchaPorId(999L);

        // Verificación
        assertEquals(null, canchaEncontrada);
    }

    @Test
    @Transactional
    @Rollback
    public void MostrarSoloDisponiblesConFiltroDePrecioYZona() {
        //Ejecucion
        List<Cancha> canchasDisponibles = repositorioCancha.MostrarCanchasConHorariosDisponibles(null, Zona.NORTE, 0.0, 1000.0);

        //Verificacion
        assertEquals(1, canchasDisponibles.size());
        assertTrue(canchasDisponibles.contains(cancha1));
        assertFalse(canchasDisponibles.contains(cancha2));
    }

    @Test
    @Transactional
    @Rollback
    public void MostrarSoloDisponiblesConFiltroDeNombreYPrecio() {
        //Ejecucion
        List<Cancha> canchasDisponibles = repositorioCancha.MostrarCanchasConHorariosDisponibles("Cancha 1", null, 0.0, 1000.0);

        //Verificacion
        assertEquals(1, canchasDisponibles.size());
        assertTrue(canchasDisponibles.contains(cancha1));
        assertFalse(canchasDisponibles.contains(cancha2));  
    }

    @Test
    @Transactional
    @Rollback
    public void MostrarSoloDisponiblesConFiltroDeNombreYZona() {
        //Ejecucion
        List<Cancha> canchasDisponibles = repositorioCancha.MostrarCanchasConHorariosDisponibles("Cancha 1", Zona.NORTE, null, null);

        //Verificacion
        assertEquals(1, canchasDisponibles.size());
        assertTrue(canchasDisponibles.contains(cancha1));
        assertFalse(canchasDisponibles.contains(cancha2));  
    }
}