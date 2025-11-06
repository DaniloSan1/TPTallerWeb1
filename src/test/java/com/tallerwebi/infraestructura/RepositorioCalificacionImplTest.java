package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import com.tallerwebi.infraestructura.config.HibernateTestInfraestructuraConfig;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestInfraestructuraConfig.class})
public class RepositorioCalificacionImplTest {

    @Autowired
    private SessionFactory sessionFactory;

    private RepositorioCalificacion repositorioCalificacion;

    private Usuario calificador;
    private Usuario calificado;
    private Partido partido;

    @BeforeEach
    public void init() {
        this.repositorioCalificacion = new RepositorioCalificacionImpl(this.sessionFactory);

        calificador = new Usuario();
        calificador.setNombre("Carlos");
        calificador.setUsername("carlos");

        calificado = new Usuario();
        calificado.setNombre("Pedro");
        calificado.setUsername("pedro");

        partido = new Partido();
        partido.setTitulo("Partido de prueba");

        sessionFactory.getCurrentSession().save(calificador);
        sessionFactory.getCurrentSession().save(calificado);
        sessionFactory.getCurrentSession().save(partido);
    }

    @Test
    @Transactional
    @Rollback
    public void deberiaGuardarYRecuperarCalificacionPorPartido() {
        Calificacion calificacion = new Calificacion();
        calificacion.setCalificador(calificador);
        calificacion.setCalificado(calificado);
        calificacion.setPartido(partido);
        calificacion.setPuntuacion(4);
        calificacion.setComentario("Buen desempeño");

        repositorioCalificacion.guardarCalificacion(calificacion);
        sessionFactory.getCurrentSession().flush();
        sessionFactory.getCurrentSession().clear();

        List<Calificacion> resultado = repositorioCalificacion.obtenerPorPartido(partido.getId());

        assertEquals(1, resultado.size());
        assertEquals(4, resultado.get(0).getPuntuacion());
        assertEquals("Buen desempeño", resultado.get(0).getComentario());
    }

    @Test
    @Transactional
    @Rollback
    public void deberiaDetectarExistenciaDeCalificacion() {
        Calificacion calificacion = new Calificacion();
        calificacion.setCalificador(calificador);
        calificacion.setCalificado(calificado);
        calificacion.setPartido(partido);
        calificacion.setPuntuacion(5);

        sessionFactory.getCurrentSession().save(calificacion);

        Boolean existe = repositorioCalificacion.existeCalificacion(
                calificador.getId(),
                calificado.getId(),
                partido.getId()
        );

        assertTrue(existe);
    }

    @Test
    @Transactional
    @Rollback
    public void noDeberiaDetectarCalificacionInexistente() {
        Boolean existe = repositorioCalificacion.existeCalificacion(
                calificador.getId(),
                calificado.getId(),
                partido.getId()
        );

        assertFalse(existe);
    }
}