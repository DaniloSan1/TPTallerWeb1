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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { HibernateTestInfraestructuraConfig.class })
public class RepositorioGolesImplTest {
    @Autowired
    private SessionFactory sessionFactory;
    private RepositorioGoles repositorioGoles;

    @BeforeEach
    public void setUp() {
        repositorioGoles = new RepositorioGolesImpl(sessionFactory);
    }

    @Test
    @Transactional
    @Rollback
    public void debeGuardarUnGol() {
        // Crear entidades relacionadas
        Usuario usuario = new Usuario("nombre", "password", "email@test.com", "username");
        sessionFactory.getCurrentSession().save(usuario);

        Cancha cancha = new Cancha();
        cancha.setNombre("Cancha Test");
        cancha.setDireccion("Dirección Test");
        cancha.setCapacidad(10);
        cancha.setPrecio(100.0);
        cancha.setTipoSuelo("Césped");
        cancha.setZona(Zona.CENTRO);
        sessionFactory.getCurrentSession().save(cancha);

        Horario horario = new Horario();
        horario.setCancha(cancha);
        horario.setDiaSemana(java.time.DayOfWeek.MONDAY);
        horario.setHoraInicio(java.time.LocalTime.of(10, 0));
        horario.setHoraFin(java.time.LocalTime.of(12, 0));
        horario.setDisponible(true);
        sessionFactory.getCurrentSession().save(horario);

        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setHorario(horario);
        reserva.setFechaReserva(LocalDateTime.now().plusDays(1));
        reserva.setActiva(true);
        sessionFactory.getCurrentSession().save(reserva);

        Equipo equipo = new Equipo("Equipo Test", "Descripción", usuario, LocalDateTime.now());
        sessionFactory.getCurrentSession().save(equipo);

        EquipoJugador equipoJugador = new EquipoJugador();
        equipoJugador.setUsuario(usuario);
        equipoJugador.setEquipo(equipo);
        equipoJugador.setFechaUnion(LocalDateTime.now());
        sessionFactory.getCurrentSession().save(equipoJugador);

        Partido partido = new Partido();
        partido.setTitulo("Partido Test");
        partido.setCreador(usuario);
        partido.setReserva(reserva);
        partido.setCupoMaximo(10);
        sessionFactory.getCurrentSession().save(partido);

        Gol gol = new Gol(partido, equipoJugador, 2);

        repositorioGoles.guardar(gol);
        sessionFactory.getCurrentSession().flush();

        assertThat(gol.getId(), org.hamcrest.Matchers.notNullValue());
        assertThat(gol.getCantidad(), equalTo(2));
        assertThat(gol.getPartido(), equalTo(partido));
        assertThat(gol.getEquipoJugador(), equalTo(equipoJugador));
    }

    @Test
    @Transactional
    @Rollback
    public void debeBuscarGolesPorPartido() {
        // Crear entidades relacionadas
        Usuario usuario = new Usuario("nombre", "password", "email@test.com", "username");
        sessionFactory.getCurrentSession().save(usuario);

        Cancha cancha = new Cancha();
        cancha.setNombre("Cancha Test");
        cancha.setDireccion("Dirección Test");
        cancha.setCapacidad(10);
        cancha.setPrecio(100.0);
        cancha.setTipoSuelo("Césped");
        cancha.setZona(Zona.CENTRO);
        sessionFactory.getCurrentSession().save(cancha);

        Horario horario = new Horario();
        horario.setCancha(cancha);
        horario.setDiaSemana(java.time.DayOfWeek.MONDAY);
        horario.setHoraInicio(java.time.LocalTime.of(10, 0));
        horario.setHoraFin(java.time.LocalTime.of(12, 0));
        horario.setDisponible(true);
        sessionFactory.getCurrentSession().save(horario);

        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setHorario(horario);
        reserva.setFechaReserva(LocalDateTime.now().plusDays(1));
        reserva.setActiva(true);
        sessionFactory.getCurrentSession().save(reserva);

        Equipo equipo = new Equipo("Equipo Test", "Descripción", usuario, LocalDateTime.now());
        sessionFactory.getCurrentSession().save(equipo);

        EquipoJugador equipoJugador = new EquipoJugador();
        equipoJugador.setUsuario(usuario);
        equipoJugador.setEquipo(equipo);
        equipoJugador.setFechaUnion(LocalDateTime.now());
        sessionFactory.getCurrentSession().save(equipoJugador);

        Partido partido = new Partido();
        partido.setTitulo("Partido Test");
        partido.setCreador(usuario);
        partido.setReserva(reserva);
        partido.setCupoMaximo(10);
        sessionFactory.getCurrentSession().save(partido);

        Gol gol1 = new Gol(partido, equipoJugador, 1);
        Gol gol2 = new Gol(partido, equipoJugador, 2);
        repositorioGoles.guardar(gol1);
        repositorioGoles.guardar(gol2);
        sessionFactory.getCurrentSession().flush();

        List<Gol> goles = repositorioGoles.buscarPorPartido(partido);

        assertThat(goles.size(), equalTo(2));
        assertTrue(goles.contains(gol1));
        assertTrue(goles.contains(gol2));
    }

   @Test
    @Transactional
    @Rollback
    public void queCuandoBuscoLosGolesDeUnUsuarioSoloMeTraigaEsos() {
 
    Usuario usuario1 = new Usuario("nombre", "password", "email@test.com", "username");
    Usuario usuario2 = new Usuario("nombre2", "password", "emailardo@test.com", "username2");

    sessionFactory.getCurrentSession().save(usuario1);
    sessionFactory.getCurrentSession().save(usuario2);

   
    Cancha cancha = new Cancha();
    cancha.setNombre("Cancha Test");
    cancha.setDireccion("Dirección Test");
    cancha.setCapacidad(10);
    cancha.setPrecio(100.0);
    cancha.setTipoSuelo("Césped");
    cancha.setZona(Zona.CENTRO);
    sessionFactory.getCurrentSession().save(cancha);

  
    Horario horario = new Horario();
    horario.setCancha(cancha);
    horario.setDiaSemana(java.time.DayOfWeek.MONDAY);
    horario.setHoraInicio(java.time.LocalTime.of(10, 0));
    horario.setHoraFin(java.time.LocalTime.of(12, 0));
    horario.setDisponible(true);
    sessionFactory.getCurrentSession().save(horario);

    
    Reserva reserva = new Reserva();
    reserva.setUsuario(usuario1);
    reserva.setHorario(horario);
    reserva.setFechaReserva(LocalDateTime.now().plusDays(1));
    reserva.setActiva(true);
    sessionFactory.getCurrentSession().save(reserva);

   
    Equipo equipo = new Equipo("Equipo Test", "Descripción", usuario1, LocalDateTime.now());
    sessionFactory.getCurrentSession().save(equipo);

   
    EquipoJugador jugador1 = new EquipoJugador();
    jugador1.setUsuario(usuario1);
    jugador1.setEquipo(equipo);
    jugador1.setFechaUnion(LocalDateTime.now());
    sessionFactory.getCurrentSession().save(jugador1);

    EquipoJugador jugador2 = new EquipoJugador();
    jugador2.setUsuario(usuario2);
    jugador2.setEquipo(equipo);
    jugador2.setFechaUnion(LocalDateTime.now());
    sessionFactory.getCurrentSession().save(jugador2);

    
    Partido partido = new Partido();
    partido.setTitulo("Partido Test");
    partido.setCreador(usuario1);
    partido.setReserva(reserva);
    partido.setCupoMaximo(10);
    sessionFactory.getCurrentSession().save(partido);

    
    Gol gol1 = new Gol(partido, jugador1, 1);
    Gol gol2 = new Gol(partido, jugador2, 2);
    Gol gol3 = new Gol(partido, jugador1, 5);

    sessionFactory.getCurrentSession().save(gol1);
    sessionFactory.getCurrentSession().save(gol2);
    sessionFactory.getCurrentSession().save(gol3);

   
    sessionFactory.getCurrentSession().flush();

    
    List<Gol> golesUsuario1 = repositorioGoles.buscarPorUsuario(usuario1.getId());

    
    assertThat(golesUsuario1.size(), equalTo(2));
    assertTrue(golesUsuario1.contains(gol1));
    assertTrue(golesUsuario1.contains(gol3));
    assertFalse(golesUsuario1.contains(gol2));
}


}