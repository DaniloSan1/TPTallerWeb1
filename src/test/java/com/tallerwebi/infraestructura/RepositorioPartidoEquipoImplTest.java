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
public class RepositorioPartidoEquipoImplTest {
    @Autowired
    private SessionFactory sessionFactory;
    private RepositorioPartidoEquipo repositorioPartidoEquipo;

    @BeforeEach
    public void setUp() {
        repositorioPartidoEquipo = new RepositorioPartidoEquipoImpl(sessionFactory);
    }

    @Test
    @Transactional
    @Rollback
    public void debeGuardarUnPartidoEquipo() {
        Usuario creador = new Usuario("nombre", "password", "email@test.com", "username");
        sessionFactory.getCurrentSession().save(creador);

        Equipo equipo = new Equipo("Equipo Test", "Descripción del equipo", creador, LocalDateTime.now());
        sessionFactory.getCurrentSession().save(equipo);

        // Need to create a Reserva and Cancha for Partido
        Cancha cancha = new Cancha();
        cancha.setNombre("Cancha Test");
        sessionFactory.getCurrentSession().save(cancha);

        Horario horario = new Horario(cancha, java.time.DayOfWeek.MONDAY, java.time.LocalTime.of(10, 0),
                java.time.LocalTime.of(11, 0));
        sessionFactory.getCurrentSession().save(horario);

        Reserva reserva = new Reserva(horario, creador, LocalDateTime.now());
        sessionFactory.getCurrentSession().save(reserva);

        Partido partido = new Partido(null, "Partido Test", "Descripcion", Nivel.PRINCIPIANTE, 10, reserva, creador);
        sessionFactory.getCurrentSession().save(partido);

        PartidoEquipo partidoEquipoAGuardar = new PartidoEquipo(partido, equipo);

        repositorioPartidoEquipo.guardar(partidoEquipoAGuardar);
        sessionFactory.getCurrentSession().flush();

        String hql = "FROM PartidoEquipo pe WHERE pe.partido.id = :partidoId AND pe.equipo.id = :equipoId";
        Query<PartidoEquipo> query = sessionFactory.getCurrentSession().createQuery(hql, PartidoEquipo.class);
        query.setParameter("partidoId", partido.getId());
        query.setParameter("equipoId", equipo.getId());
        PartidoEquipo partidoEquipo = query.getSingleResult();

        assertThat(partidoEquipo, equalTo(partidoEquipoAGuardar));
    }

    @Test
    @Transactional
    @Rollback
    public void debeBuscarPartidoEquipoPorId() {
        Usuario creador = new Usuario("nombre", "password", "email@test.com", "username");
        sessionFactory.getCurrentSession().save(creador);

        Equipo equipo = new Equipo("Equipo Test", "Descripción del equipo", creador, LocalDateTime.now());
        sessionFactory.getCurrentSession().save(equipo);

        // Need to create a Reserva and Cancha for Partido
        Cancha cancha = new Cancha();
        cancha.setNombre("Cancha Test");
        sessionFactory.getCurrentSession().save(cancha);

        Horario horario = new Horario(cancha, java.time.DayOfWeek.MONDAY, java.time.LocalTime.of(10, 0),
                java.time.LocalTime.of(11, 0));
        sessionFactory.getCurrentSession().save(horario);

        Reserva reserva = new Reserva(horario, creador, LocalDateTime.now());
        sessionFactory.getCurrentSession().save(reserva);

        Partido partido = new Partido(null, "Partido Test", "Descripcion", Nivel.PRINCIPIANTE, 10, reserva, creador);
        sessionFactory.getCurrentSession().save(partido);

        PartidoEquipo partidoEquipoAGuardar = new PartidoEquipo(partido, equipo);
        sessionFactory.getCurrentSession().save(partidoEquipoAGuardar);
        sessionFactory.getCurrentSession().flush();

        PartidoEquipo partidoEquipoEncontrado = repositorioPartidoEquipo.buscarPorId(partidoEquipoAGuardar.getId());

        assertThat(partidoEquipoEncontrado, equalTo(partidoEquipoAGuardar));
    }
}