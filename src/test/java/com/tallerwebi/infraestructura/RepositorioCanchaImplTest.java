package com.tallerwebi.infraestructura;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;


import javax.transaction.Transactional;

import com.tallerwebi.dominio.Cancha;
import com.tallerwebi.dominio.Horario;
import com.tallerwebi.infraestructura.config.HibernateTestInfraestructuraConfig;
import com.tallerwebi.dominio.RepositorioCancha;
import com.tallerwebi.infraestructura.RepositorioCanchaIpl;

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

    @BeforeEach
    public void init() {
    this.repositorioCancha = new RepositorioCanchaIpl(this.sessionFactory);
    }

    @Test
    @Transactional
    @Rollback
    public void MostrarSoloDisponibles() {
        //Preparacion
        Cancha cancha1 = new Cancha();
        cancha1.setId(1L);
        cancha1.setNombre("Cancha 1");
        cancha1.setDireccion("Direccion 1");
        cancha1.setCapacidad(10);

        Cancha cancha2 = new Cancha();
        cancha2.setId(2L);
        cancha2.setNombre("Cancha 2");
        cancha2.setDireccion("Direccion 2");
        cancha2.setCapacidad(20);

        Cancha cancha3 = new Cancha();
        cancha3.setId(3L);
        cancha3.setNombre("Cancha 3");
        cancha3.setDireccion("Direccion 3");
        cancha3.setCapacidad(30);
        
        Horario horario1 = new Horario();
        horario1.setId(1L);
        horario1.setCancha(cancha1);
        horario1.setDiaSemana(DayOfWeek.MONDAY);
        horario1.setHoraInicio(LocalTime.now());
        horario1.setHoraFin(LocalTime.now().plusHours(1));


        Horario horario2 = new Horario();
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
        
        
        //Ejecucion
        List<Cancha> canchasDisponibles = repositorioCancha.MostrarCanchasConHorariosDisponibles();

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
}