package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Cancha;
import com.tallerwebi.dominio.Horario;
import com.tallerwebi.dominio.RepositorioHorario;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RepositorioHorarioImpl implements RepositorioHorario {

    private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioHorarioImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Horario obtenerPorId(Long id) {
        return sessionFactory.getCurrentSession().get(Horario.class, id);
    }

    @Override
    public List<Horario> obtenerPorCancha(Cancha cancha) {
        String hql = "FROM Horario h WHERE h.cancha = :cancha";
        return sessionFactory.getCurrentSession()
                .createQuery(hql, Horario.class)
                .setParameter("cancha", cancha)
                .getResultList();
    }

    @Override
    public List<Horario> obtenerDisponiblesPorCancha(Cancha cancha) {
        String hql = "FROM Horario h WHERE h.cancha = :cancha AND h.disponible = true";
        return sessionFactory.getCurrentSession()
                .createQuery(hql, Horario.class)
                .setParameter("cancha", cancha)
                .getResultList();
    }
    @Override
    public void cambiarDisponibilidad(Long id, Boolean disponible) {
        Horario horario = obtenerPorId(id);
        if (horario != null) {
            horario.setDisponible(disponible);
            sessionFactory.getCurrentSession().update(horario);
        }
    }
}