package com.tallerwebi.infraestructura;

import java.util.List;
import java.time.LocalDate;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tallerwebi.dominio.RepositorioTorneo;
import com.tallerwebi.dominio.Torneo;
import com.tallerwebi.dominio.Cancha;

@Repository
@Transactional
public class RepositorioTorneoImpl implements RepositorioTorneo {
    private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioTorneoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    @Override
    public void crearTorneo(Torneo torneo) {
        sessionFactory.getCurrentSession().save(torneo);
    }
    @Override
    public List<Torneo> listarTorneos() {
        String hql = "FROM Torneo t";
        return sessionFactory.getCurrentSession()
                .createQuery(hql, Torneo.class)
                .getResultList();
    }
    @Override
    public List<Torneo> torneoFuturo(LocalDate fecha) {
        String hql = "FROM Torneo t WHERE t.fecha > :fecha";
        return sessionFactory.getCurrentSession()
                .createQuery(hql, Torneo.class)
                .setParameter("fecha", fecha)
                .getResultList();
    }

    @Override
    public Boolean existeCanchaYFecha(Cancha cancha, LocalDate fecha) {
        String hql = "SELECT COUNT(t) FROM Torneo t WHERE t.cancha = :cancha AND t.fecha = :fecha";
        Long count = sessionFactory.getCurrentSession()
                .createQuery(hql, Long.class)
                .setParameter("cancha", cancha)
                .setParameter("fecha", fecha)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public List<Torneo> porCancha(Cancha cancha) {
        String hql = "FROM Torneo t WHERE t.cancha = :cancha";
        return sessionFactory.getCurrentSession()
                .createQuery(hql, Torneo.class)
                .setParameter("cancha", cancha)
                .getResultList();
    }
    @Override
    public Torneo porId(Long id) {
        return sessionFactory.getCurrentSession().get(Torneo.class, id);
    }

    @Override
    public List<Torneo> porCanchaYFecha(Cancha cancha, LocalDate fecha) {
        String hql = "FROM Torneo t WHERE t.cancha = :cancha AND t.fecha = :fecha";
        return sessionFactory.getCurrentSession()
                .createQuery(hql, Torneo.class)
                .setParameter("cancha", cancha)
                .setParameter("fecha", fecha)
                .getResultList();
    }
    @Override
    public void actualizarTorneo(Torneo torneo) {
        sessionFactory.getCurrentSession().update(torneo);
    }
}
