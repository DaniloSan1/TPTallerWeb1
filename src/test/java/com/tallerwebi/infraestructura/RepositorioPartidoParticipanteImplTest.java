package com.tallerwebi.infraestructura;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.tallerwebi.dominio.Cancha;
import com.tallerwebi.dominio.Equipo;
import com.tallerwebi.dominio.Horario;
import com.tallerwebi.dominio.Nivel;
import com.tallerwebi.dominio.Partido;
import com.tallerwebi.dominio.PartidoParticipante;
import com.tallerwebi.dominio.RepositorioPartidoParticipante;
import com.tallerwebi.dominio.Reserva;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.Zona;
import com.tallerwebi.infraestructura.config.HibernateTestInfraestructuraConfig;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { HibernateTestInfraestructuraConfig.class })
public class RepositorioPartidoParticipanteImplTest {
    @Autowired
    private SessionFactory sessionFactory;
    private RepositorioPartidoParticipante repositorioPartidoParticipante;

    @BeforeEach
    public void init() {
        this.repositorioPartidoParticipante = new RepositorioPartidoParticipanteImpl(this.sessionFactory);
    }

    @Test
    @Transactional
    @Rollback
    public void deberiaGuardarUnNuevoParticipantePartido() {
        Cancha cancha = new Cancha("Cancha 1", null, null, "Direccion 1", Zona.NORTE);
        this.sessionFactory.getCurrentSession().save(cancha);

        Horario horario = new Horario(cancha, DayOfWeek.MONDAY, LocalTime.now(), LocalTime.now().plusHours(1));
        this.sessionFactory.getCurrentSession().save(horario);

        Usuario creador = new Usuario("usuario1", "password", "email@example.com", "usernameCreador");
        this.sessionFactory.getCurrentSession().save(creador);

        Reserva reserva = new Reserva(horario, creador, LocalDateTime.now().plusDays(1));
        this.sessionFactory.getCurrentSession().save(reserva);

        Partido nuevoPartido = new Partido(null, "Partido de prueba", "Descripción del partido",
                Nivel.INTERMEDIO,
                10,
                reserva, creador);
        this.sessionFactory.getCurrentSession().save(nuevoPartido);

        Usuario participante = new Usuario("participante1", "password", "participante1@example.com",
                "participanteUsername1");
        this.sessionFactory.getCurrentSession().save(participante);
        this.sessionFactory.getCurrentSession().flush();

        PartidoParticipante partidoParticipante = new PartidoParticipante(nuevoPartido, participante, Equipo.EQUIPO_1);
        this.repositorioPartidoParticipante.guardar(partidoParticipante);
        PartidoParticipante partidoParticipanteObtenido = (PartidoParticipante) this.sessionFactory.getCurrentSession()
                .createCriteria(PartidoParticipante.class)
                .add(Restrictions.eq("partido.id", nuevoPartido.getId()))
                .add(Restrictions.eq("usuario.id", participante.getId()))
                .uniqueResult();
        assertThat(partidoParticipanteObtenido, is(equalTo(partidoParticipante)));
    }

    @Test
    @Transactional
    @Rollback
    public void deberiaBuscarUnParticipantePartidoPorId() {
        Cancha cancha = new Cancha("Cancha 1", null, null, "Direccion 1", Zona.NORTE);
        this.sessionFactory.getCurrentSession().save(cancha);

        Horario horario = new Horario(cancha, DayOfWeek.MONDAY, LocalTime.now(), LocalTime.now().plusHours(1));
        this.sessionFactory.getCurrentSession().save(horario);

        Usuario creador = new Usuario("usuario1", "password", "email@example.com", "usernameCreador");
        this.sessionFactory.getCurrentSession().save(creador);

        Reserva reserva = new Reserva(horario, creador, LocalDateTime.now().plusDays(1));
        this.sessionFactory.getCurrentSession().save(reserva);

        Partido nuevoPartido = new Partido(null, "Partido de prueba", "Descripción del partido",
                Nivel.INTERMEDIO,
                10,
                reserva, creador);
        this.sessionFactory.getCurrentSession().save(nuevoPartido);

        Usuario participante = new Usuario("participante1", "password", "participante1@example.com",
                "participanteUsername1");
        this.sessionFactory.getCurrentSession().save(participante);

        PartidoParticipante partidoParticipante = new PartidoParticipante(nuevoPartido, participante, Equipo.EQUIPO_1);
        this.repositorioPartidoParticipante.guardar(partidoParticipante);
        this.sessionFactory.getCurrentSession().flush();

        PartidoParticipante partidoParticipanteEncontrado = this.repositorioPartidoParticipante
                .buscarPorId(partidoParticipante.getId());

        assertThat(partidoParticipanteEncontrado, is(equalTo(partidoParticipante)));
    }

    @Test
    @Transactional
    @Rollback
    public void deberiaEliminarUnParticipantePartido() {
        Cancha cancha = new Cancha("Cancha 1", null, null, "Direccion 1", Zona.NORTE);
        this.sessionFactory.getCurrentSession().save(cancha);

        Horario horario = new Horario(cancha, DayOfWeek.MONDAY, LocalTime.now(), LocalTime.now().plusHours(1));
        this.sessionFactory.getCurrentSession().save(horario);

        Usuario creador = new Usuario("usuario1", "password", "email@example.com", "usernameCreador");
        this.sessionFactory.getCurrentSession().save(creador);

        Reserva reserva = new Reserva(horario, creador, LocalDateTime.now().plusDays(1));
        this.sessionFactory.getCurrentSession().save(reserva);

        Partido nuevoPartido = new Partido(null, "Partido de prueba", "Descripción del partido",
                Nivel.INTERMEDIO,
                10,
                reserva, creador);
        this.sessionFactory.getCurrentSession().save(nuevoPartido);

        Usuario participante = new Usuario("participante1", "password", "participante1@example.com",
                "participanteUsername1");
        this.sessionFactory.getCurrentSession().save(participante);

        PartidoParticipante partidoParticipante = new PartidoParticipante(nuevoPartido, participante, Equipo.EQUIPO_1);
        this.repositorioPartidoParticipante.guardar(partidoParticipante);
        this.sessionFactory.getCurrentSession().flush();

        PartidoParticipante existente = this.repositorioPartidoParticipante.buscarPorId(partidoParticipante.getId());
        assertThat(existente, is(equalTo(partidoParticipante)));

        this.repositorioPartidoParticipante.eliminar(partidoParticipante);
        this.sessionFactory.getCurrentSession().flush();

        PartidoParticipante eliminado = this.repositorioPartidoParticipante.buscarPorId(partidoParticipante.getId());
        assertThat(eliminado, is(equalTo(null)));
    }
}
