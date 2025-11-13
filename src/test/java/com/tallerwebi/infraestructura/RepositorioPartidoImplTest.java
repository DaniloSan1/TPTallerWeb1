package com.tallerwebi.infraestructura;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.tallerwebi.dominio.Cancha;
import com.tallerwebi.dominio.Equipo;
import com.tallerwebi.dominio.Horario;
import com.tallerwebi.dominio.Nivel;
import com.tallerwebi.dominio.Partido;
import com.tallerwebi.dominio.PartidoEquipo;
import com.tallerwebi.dominio.RepositorioPartido;
import com.tallerwebi.dominio.Reserva;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.Zona;
import com.tallerwebi.infraestructura.config.HibernateTestInfraestructuraConfig;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.equalTo;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { HibernateTestInfraestructuraConfig.class })
public class RepositorioPartidoImplTest {
    @Autowired
    private SessionFactory sessionFactory;
    private RepositorioPartido repositorioPartido;

    @BeforeEach
    public void init() {
        this.repositorioPartido = new RepositorioPartidoImpl(this.sessionFactory);
    }

    @Test
    @Transactional
    @Rollback
    public void deberiaObtenerUnPartidoPorId() {
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

        // Implementar el test para obtener un partido por ID.
        Partido partido = this.repositorioPartido.porId(nuevoPartido.getId());

        assertThat(partido, is(equalTo(nuevoPartido)));
    }

    @Test
    @Transactional
    @Rollback
    public void deberiaListarPartidosPorEquipoConInfoCancha() {
        // Crear cancha
        Cancha cancha = new Cancha("Cancha 1", null, null, "Direccion 1", Zona.NORTE);
        this.sessionFactory.getCurrentSession().save(cancha);

        // Crear horario
        Horario horario = new Horario(cancha, DayOfWeek.MONDAY, LocalTime.now(), LocalTime.now().plusHours(1));
        this.sessionFactory.getCurrentSession().save(horario);

        // Crear usuario creador
        Usuario creador = new Usuario("usuario1", "password", "email@example.com", "usernameCreador");
        this.sessionFactory.getCurrentSession().save(creador);

        // Crear reserva
        Reserva reserva = new Reserva(horario, creador, LocalDateTime.now().plusDays(1));
        this.sessionFactory.getCurrentSession().save(reserva);

        // Crear equipo
        Equipo equipo = new Equipo("Equipo Test", "Descripción", creador, LocalDateTime.now());
        this.sessionFactory.getCurrentSession().save(equipo);

        // Crear partido
        Partido partido = new Partido(null, "Partido de prueba", "Descripción del partido",
                Nivel.INTERMEDIO, 10, reserva, creador);
        this.sessionFactory.getCurrentSession().save(partido);

        // Crear PartidoEquipo para asociar partido y equipo
        PartidoEquipo partidoEquipo = new PartidoEquipo(partido, equipo);
        this.sessionFactory.getCurrentSession().save(partidoEquipo);

        // Ejecutar el método
        List<Partido> partidos = this.repositorioPartido.listarPorEquipoConInfoCancha(equipo.getId());

        // Verificar
        assertThat(partidos.size(), is(1));
        assertThat(partidos.get(0), is(equalTo(partido)));
    }
}
