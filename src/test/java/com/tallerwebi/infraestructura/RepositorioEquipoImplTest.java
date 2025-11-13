package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import com.tallerwebi.infraestructura.config.HibernateTestInfraestructuraConfig;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { HibernateTestInfraestructuraConfig.class })
public class RepositorioEquipoImplTest {
    @Autowired
    private SessionFactory sessionFactory;
    private RepositorioEquipo repositorioEquipo;

    @BeforeEach
    public void setUp() {
        repositorioEquipo = new RepositorioEquipoImpl(sessionFactory);
    }

    @Test
    @Transactional
    @Rollback
    public void debeGuardarUnEquipo() {
        Usuario creador = new Usuario("nombre", "password", "email@test.com", "username");
        sessionFactory.getCurrentSession().save(creador);

        Equipo equipoAGuardar = new Equipo("Equipo Test", "Descripción del equipo", creador, LocalDateTime.now());

        repositorioEquipo.guardar(equipoAGuardar);
        sessionFactory.getCurrentSession().flush();

        String hql = "FROM Equipo e WHERE e.nombre = :nombre";
        Query<Equipo> query = sessionFactory.getCurrentSession().createQuery(hql, Equipo.class);
        query.setParameter("nombre", "Equipo Test");
        Equipo equipo = (Equipo) query.getSingleResult();

        assertThat(equipo, equalTo(equipoAGuardar));
    }

    @Test
    @Transactional
    @Rollback
    public void debeBuscarEquipoPorId() {
        Usuario creador = new Usuario("nombre", "password", "email@test.com", "username");
        sessionFactory.getCurrentSession().save(creador);

        Equipo equipoAGuardar = new Equipo("Equipo Test", "Descripción del equipo", creador, LocalDateTime.now());
        sessionFactory.getCurrentSession().save(equipoAGuardar);
        sessionFactory.getCurrentSession().flush();

        Equipo equipoEncontrado = repositorioEquipo.buscarPorId(equipoAGuardar.getId());

        assertThat(equipoEncontrado, equalTo(equipoAGuardar));
    }

    @Test
    @Transactional
    @Rollback
    public void debeModificarUnEquipo() {
        Usuario creador = new Usuario("nombre", "password", "email@test.com", "username");
        sessionFactory.getCurrentSession().save(creador);

        Equipo equipoAGuardar = new Equipo("Equipo Test", "Descripción del equipo", creador, LocalDateTime.now());
        sessionFactory.getCurrentSession().save(equipoAGuardar);
        sessionFactory.getCurrentSession().flush();

        equipoAGuardar.setNombre("Equipo Modificado");
        repositorioEquipo.modificar(equipoAGuardar);
        sessionFactory.getCurrentSession().flush();

        Equipo equipoModificado = repositorioEquipo.buscarPorId(equipoAGuardar.getId());

        assertThat(equipoModificado.getNombre(), equalTo("Equipo Modificado"));
    }
}