package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Cancha;
import com.tallerwebi.dominio.Torneo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RepositorioTorneoImplTest {

    private SessionFactory sessionFactory;
    private Session session;
    private Query<Torneo> queryTorneo;
    private Query<Long> queryCount;
    private RepositorioTorneoImpl repositorio;

    private Torneo torneo;
    private Cancha cancha;

    @BeforeEach
    void setUp() {
        sessionFactory = mock(SessionFactory.class);
        session = mock(Session.class);
        queryTorneo = mock(Query.class);
        queryCount = mock(Query.class);
        repositorio = new RepositorioTorneoImpl(sessionFactory);

        cancha = new Cancha();
        cancha.setId(1L);
        cancha.setNombre("Cancha Test");

        torneo = new Torneo();
        torneo.setId(10L);
        torneo.setCancha(cancha);
        torneo.setNombre("Torneo Test");
        torneo.setFecha(LocalDate.now().plusDays(3));

        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    void alCrearTorneoDeberiaGuardarEnSesion() {
        repositorio.crearTorneo(torneo);
        verify(session).save(torneo);
    }

    @Test
    void alListarTorneosDeberiaRetornarListaDeTorneos() {
        List<Torneo> torneosEsperados = Arrays.asList(torneo);
        when(session.createQuery("FROM Torneo t", Torneo.class)).thenReturn(queryTorneo);
        when(queryTorneo.getResultList()).thenReturn(torneosEsperados);

        List<Torneo> resultado = repositorio.listarTorneos();

        assertEquals(1, resultado.size());
        assertEquals(torneo, resultado.get(0));
    }

    @Test
    void alBuscarTorneosFuturosDeberiaFiltrarPorFecha() {
        LocalDate hoy = LocalDate.now();
        when(session.createQuery("FROM Torneo t WHERE t.fecha > :fecha", Torneo.class)).thenReturn(queryTorneo);
        when(queryTorneo.setParameter("fecha", hoy)).thenReturn(queryTorneo);
        when(queryTorneo.getResultList()).thenReturn(Collections.singletonList(torneo));

        List<Torneo> resultado = repositorio.torneoFuturo(hoy);

        assertEquals(1, resultado.size());
        assertEquals(torneo, resultado.get(0));
        verify(queryTorneo).setParameter("fecha", hoy);
    }

    @Test
void alVerificarExistenciaPorCanchaYFechaDeberiaRetornarTrueSiExiste() {
    LocalDate fecha = LocalDate.now().plusDays(1);

    when(session.createQuery(
            "SELECT COUNT(t) FROM Torneo t WHERE t.cancha = :cancha AND t.fecha = :fecha and t.estado = 'CONFIRMADO'",
            Long.class))
        .thenReturn(queryCount);

    when(queryCount.setParameter("cancha", cancha)).thenReturn(queryCount);
    when(queryCount.setParameter("fecha", fecha)).thenReturn(queryCount);
    when(queryCount.getSingleResult()).thenReturn(2L);

    Boolean existe = repositorio.existeCanchaYFecha(cancha, fecha);

    assertTrue(existe);
    verify(queryCount).setParameter("cancha", cancha);
    verify(queryCount).setParameter("fecha", fecha);
}
    @Test
    void alVerificarExistenciaPorCanchaYFechaDeberiaRetornarFalseSiNoExiste() {
        LocalDate fecha = LocalDate.now().plusDays(1);
        when(session.createQuery("SELECT COUNT(t) FROM Torneo t WHERE t.cancha = :cancha AND t.fecha = :fecha and t.estado = 'CONFIRMADO'",
            Long.class))
                .thenReturn(queryCount);
        when(queryCount.setParameter("cancha", cancha)).thenReturn(queryCount);
        when(queryCount.setParameter("fecha", fecha)).thenReturn(queryCount);
        when(queryCount.getSingleResult()).thenReturn(0L);

        Boolean existe = repositorio.existeCanchaYFecha(cancha, fecha);

        assertFalse(existe);
    }

    @Test
    void alBuscarPorCanchaDeberiaRetornarTorneosAsociados() {
        when(session.createQuery("FROM Torneo t WHERE t.cancha = :cancha", Torneo.class)).thenReturn(queryTorneo);
        when(queryTorneo.setParameter("cancha", cancha)).thenReturn(queryTorneo);
        when(queryTorneo.getResultList()).thenReturn(Arrays.asList(torneo));

        List<Torneo> resultado = repositorio.porCancha(cancha);

        assertEquals(1, resultado.size());
        assertEquals(torneo, resultado.get(0));
        verify(queryTorneo).setParameter("cancha", cancha);
    }

    @Test
    void alBuscarPorIdDeberiaRetornarTorneoCorrecto() {
        when(session.get(Torneo.class, 10L)).thenReturn(torneo);

        Torneo resultado = repositorio.porId(10L);

        assertEquals(torneo, resultado);
        verify(session).get(Torneo.class, 10L);
    }

    @Test
    void alBuscarPorCanchaYFechaDeberiaRetornarTorneosFiltrados() {
        LocalDate fecha = LocalDate.now();
        when(session.createQuery("FROM Torneo t WHERE t.cancha = :cancha AND t.fecha = :fecha", Torneo.class))
                .thenReturn(queryTorneo);
        when(queryTorneo.setParameter("cancha", cancha)).thenReturn(queryTorneo);
        when(queryTorneo.setParameter("fecha", fecha)).thenReturn(queryTorneo);
        when(queryTorneo.getResultList()).thenReturn(Collections.singletonList(torneo));

        List<Torneo> resultado = repositorio.porCanchaYFecha(cancha, fecha);

        assertEquals(1, resultado.size());
        assertEquals(torneo, resultado.get(0));
        verify(queryTorneo).setParameter("cancha", cancha);
        verify(queryTorneo).setParameter("fecha", fecha);
    }

    @Test
    void alActualizarTorneoDeberiaInvocarUpdateEnSesion() {
        repositorio.actualizarTorneo(torneo);
        verify(session).update(torneo);
    }
}
