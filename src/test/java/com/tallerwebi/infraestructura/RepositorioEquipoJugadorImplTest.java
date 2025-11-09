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
public class RepositorioEquipoJugadorImplTest {
    @Autowired
    private SessionFactory sessionFactory;
    private RepositorioEquipoJugador repositorioEquipoJugador;

    @BeforeEach
    public void setUp() {
        repositorioEquipoJugador = new RepositorioEquipoJugadorImpl(sessionFactory);
    }

    @Test
    @Transactional
    @Rollback
    public void debeGuardarUnEquipoJugador() {
        Usuario creador = new Usuario("nombre", "password", "email@test.com", "username");
        sessionFactory.getCurrentSession().save(creador);

        Usuario jugador = new Usuario("jugador", "password", "jugador@test.com", "jugador");
        sessionFactory.getCurrentSession().save(jugador);

        Equipo equipo = new Equipo("Equipo Test", "Descripción del equipo", creador, LocalDateTime.now());
        sessionFactory.getCurrentSession().save(equipo);

        EquipoJugador equipoJugadorAGuardar = new EquipoJugador(equipo, jugador);

        repositorioEquipoJugador.guardar(equipoJugadorAGuardar);
        sessionFactory.getCurrentSession().flush();

        String hql = "FROM EquipoJugador ej WHERE ej.equipo.id = :equipoId AND ej.usuario.id = :usuarioId";
        Query<EquipoJugador> query = sessionFactory.getCurrentSession().createQuery(hql, EquipoJugador.class);
        query.setParameter("equipoId", equipo.getId());
        query.setParameter("usuarioId", jugador.getId());
        EquipoJugador equipoJugador = query.getSingleResult();

        assertThat(equipoJugador, equalTo(equipoJugadorAGuardar));
    }

    @Test
    @Transactional
    @Rollback
    public void debeBuscarEquipoJugadorPorId() {
        Usuario creador = new Usuario("nombre", "password", "email@test.com", "username");
        sessionFactory.getCurrentSession().save(creador);

        Usuario jugador = new Usuario("jugador", "password", "jugador@test.com", "jugador");
        sessionFactory.getCurrentSession().save(jugador);

        Equipo equipo = new Equipo("Equipo Test", "Descripción del equipo", creador, LocalDateTime.now());
        sessionFactory.getCurrentSession().save(equipo);

        EquipoJugador equipoJugadorAGuardar = new EquipoJugador(equipo, jugador);
        sessionFactory.getCurrentSession().save(equipoJugadorAGuardar);
        sessionFactory.getCurrentSession().flush();

        EquipoJugador equipoJugadorEncontrado = repositorioEquipoJugador.buscarPorId(equipoJugadorAGuardar.getId());

        assertThat(equipoJugadorEncontrado, equalTo(equipoJugadorAGuardar));
    }

    @Test
    @Transactional
    @Rollback
    public void debeEliminarEquipoJugador() {
        Usuario creador = new Usuario("nombre", "password", "email@test.com", "username");
        sessionFactory.getCurrentSession().save(creador);

        Usuario jugador = new Usuario("jugador", "password", "jugador@test.com", "jugador");
        sessionFactory.getCurrentSession().save(jugador);

        Equipo equipo = new Equipo("Equipo Test", "Descripción del equipo", creador, LocalDateTime.now());
        sessionFactory.getCurrentSession().save(equipo);

        EquipoJugador equipoJugadorAGuardar = new EquipoJugador(equipo, jugador);
        sessionFactory.getCurrentSession().save(equipoJugadorAGuardar);
        sessionFactory.getCurrentSession().flush();

        // Verificar que existe antes de eliminar
        EquipoJugador equipoJugadorAntes = repositorioEquipoJugador.buscarPorId(equipoJugadorAGuardar.getId());
        assertThat(equipoJugadorAntes, equalTo(equipoJugadorAGuardar));

        // Eliminar
        repositorioEquipoJugador.eliminar(equipoJugadorAGuardar);
        sessionFactory.getCurrentSession().flush();

        // Verificar que ya no existe
        EquipoJugador equipoJugadorDespues = repositorioEquipoJugador.buscarPorId(equipoJugadorAGuardar.getId());
        assertThat(equipoJugadorDespues, equalTo(null));
    }
}