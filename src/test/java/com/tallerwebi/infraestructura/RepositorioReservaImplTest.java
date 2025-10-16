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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestInfraestructuraConfig.class})
public class RepositorioReservaImplTest {

    @Autowired
    private SessionFactory sessionFactory;

    private RepositorioReserva repositorioReserva;

    private Usuario usuario;
    private Horario horario;

    @BeforeEach
    public void init() {
        this.repositorioReserva = new RepositorioReservaImpl(this.sessionFactory);

        usuario = new Usuario();
        usuario.setNombre("Juan");

        Cancha cancha = new Cancha();
        cancha.setNombre("Cancha 1");
        cancha.setDireccion("Calle 123");
        cancha.setCapacidad(10);

        horario = new Horario();
        horario.setCancha(cancha);
        horario.setDiaSemana(DayOfWeek.THURSDAY);
        horario.setHoraInicio(LocalTime.of(18, 0));
        horario.setHoraFin(LocalTime.of(19, 0));

        sessionFactory.getCurrentSession().save(usuario);
        sessionFactory.getCurrentSession().save(cancha);
        sessionFactory.getCurrentSession().save(horario);
    }

    @Test
    @Transactional
    @Rollback
    public void deberiaGuardarYRecuperarReservaPorId() {
        Reserva reserva = new Reserva(horario, usuario, LocalDate.of(2025, 10, 20).atTime(18, 0));
        reserva.setActiva(true);

        repositorioReserva.guardar(reserva);
        sessionFactory.getCurrentSession().flush();
        sessionFactory.getCurrentSession().clear();

        Reserva encontrada = repositorioReserva.porId(reserva.getId());

        assertNotNull(encontrada);
        assertEquals(usuario.getId(), encontrada.getUsuario().getId());
        assertEquals(horario.getId(), encontrada.getHorario().getId());
    }

    @Test
    @Transactional
    @Rollback
    public void deberiaEncontrarReservaPorHorarioYFecha() {
        LocalDateTime fecha = LocalDateTime.of(2025, 10, 20, 18, 0);

        Reserva reserva = new Reserva(horario, usuario, fecha);
        reserva.setActiva(true);
        sessionFactory.getCurrentSession().save(reserva);

        List<Reserva> resultado = repositorioReserva.porHorarioYFecha(horario, fecha);

        assertEquals(1, resultado.size());
        assertEquals(reserva.getId(), resultado.get(0).getId());
    }

    @Test
    @Transactional
    @Rollback
    public void deberiaEncontrarReservasPorUsuario() {
        LocalDateTime fecha = LocalDateTime.of(2025, 10, 20, 18, 0);

        Reserva reserva = new Reserva(horario, usuario, fecha);
        reserva.setActiva(true);
        sessionFactory.getCurrentSession().save(reserva);

        List<Reserva> resultado = repositorioReserva.porUsuario(usuario);

        assertEquals(1, resultado.size());
        assertEquals(usuario.getId(), resultado.get(0).getUsuario().getId());
    }

    @Test
    @Transactional
    @Rollback
    public void noDeberiaDevolverReservasInactivas() {
        LocalDateTime fecha = LocalDateTime.of(2025, 10, 20, 18, 0);

        Reserva reserva = new Reserva(horario, usuario, fecha);
        reserva.setActiva(false);
        sessionFactory.getCurrentSession().save(reserva);

        List<Reserva> resultado = repositorioReserva.porUsuario(usuario);

        assertTrue(resultado.isEmpty());
    }
}