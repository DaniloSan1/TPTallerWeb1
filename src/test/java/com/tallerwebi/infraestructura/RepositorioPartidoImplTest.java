package com.tallerwebi.infraestructura;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.tallerwebi.dominio.Cancha;
import com.tallerwebi.dominio.Horario;
import com.tallerwebi.dominio.Nivel;
import com.tallerwebi.dominio.Partido;
import com.tallerwebi.dominio.PartidoParticipante;
import com.tallerwebi.dominio.RepositorioPartido;
import com.tallerwebi.dominio.Reserva;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.Zona;
import com.tallerwebi.infraestructura.config.HibernateTestInfraestructuraConfig;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

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

        Usuario creador = new Usuario("usuario1", "password", "email@example.com");
        this.sessionFactory.getCurrentSession().save(creador);

        Reserva reserva = new Reserva(horario, creador, LocalDateTime.now().plusDays(1));
        this.sessionFactory.getCurrentSession().save(reserva);

        Partido nuevoPartido = new Partido(null, "Partido de prueba", "Descripción del partido",
                Nivel.INTERMEDIO,
                10,
                reserva, creador);
        this.sessionFactory.getCurrentSession().save(nuevoPartido);

        Usuario participante = new Usuario("usuario2", "password2", "email2@example.com");
        this.sessionFactory.getCurrentSession().save(participante);

        PartidoParticipante partidoParticipante = new PartidoParticipante(nuevoPartido, participante);
        this.sessionFactory.getCurrentSession().save(partidoParticipante);

        // Implementar el test para obtener un partido por ID.
        Partido partido = this.repositorioPartido.porId(nuevoPartido.getId());

        assertThat(partido, is(equalTo(nuevoPartido)));
    }
}
