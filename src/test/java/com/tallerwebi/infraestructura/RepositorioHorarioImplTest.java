package com.tallerwebi.infraestructura;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import com.tallerwebi.dominio.Cancha;
import com.tallerwebi.dominio.Horario;
import com.tallerwebi.infraestructura.config.HibernateTestInfraestructuraConfig;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestInfraestructuraConfig.class})
public class RepositorioHorarioImplTest {
    private RepositorioHorarioImpl repositorioHorario;
    @Autowired
    private SessionFactory sessionFactory;
    private Horario horario1;
    private Horario horario2;
    private Horario horario3;
    private Cancha cancha1;

    @BeforeEach
    public void init() {
        this.repositorioHorario = new RepositorioHorarioImpl(this.sessionFactory);
        horario1 = new Horario();
        horario2 = new Horario();
        horario3 = new Horario();
        cancha1 = new Cancha();
        cancha1.setId(1L);
        horario1.setId(1L);
        horario1.setDisponible(true);
        horario1.setCancha(cancha1);
        horario2.setId(2L);
        horario2.setDisponible(false);
        horario2.setCancha(cancha1);
        horario3.setId(3L);
        horario3.setDisponible(true);
        horario3.setCancha(cancha1);

        this.sessionFactory.getCurrentSession().save(cancha1);
        this.sessionFactory.getCurrentSession().save(horario1);
        this.sessionFactory.getCurrentSession().save(horario2);
        this.sessionFactory.getCurrentSession().save(horario3);
    }

    @Test
    @Transactional
    @Rollback
    public void testObtenerPorId() {
        Horario resultado = repositorioHorario.obtenerPorId(1L);
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    @Transactional
    @Rollback
    public void t() {
        List<Horario> resultados = repositorioHorario.obtenerPorCancha(cancha1);
        assertNotNull(resultados);
        assertEquals(3, resultados.size());
    }

    @Test
    @Transactional
    @Rollback
    public void testObtenerDisponiblesPorCancha() {
        List<Horario> resultados = repositorioHorario.obtenerDisponiblesPorCancha(cancha1);
        assertNotNull(resultados);
        assertEquals(2, resultados.size());
        assertTrue(resultados.contains(horario1));
        assertTrue(resultados.contains(horario3));
    }

    @Test
    @Transactional
    @Rollback
    public void testCambiarDisponibilidad() {
        repositorioHorario.cambiarDisponibilidad(2L, true);
        Horario resultado = repositorioHorario.obtenerPorId(2L);
        assertNotNull(resultado);
        assertTrue(resultado.getDisponible());
    }
}
