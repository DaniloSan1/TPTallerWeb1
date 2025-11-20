package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Cancha;
import com.tallerwebi.dominio.Horario;
import com.tallerwebi.dominio.RepositorioHorario;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
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
        if (cancha == null || cancha.getId() == null) {
            throw new IllegalArgumentException("La cancha no puede ser nula y debe tener un id");
        }
        String hql = "FROM Horario h WHERE h.cancha.id = :canchaId";
        return sessionFactory.getCurrentSession()
                .createQuery(hql, Horario.class)
                .setParameter("canchaId", cancha.getId())
                .getResultList();
    }

    @Override
    public List<Horario> obtenerDisponiblesPorCancha(Cancha cancha) {
        if (cancha == null || cancha.getId() == null) {
            throw new IllegalArgumentException("La cancha no puede ser nula y debe tener un id");
        }
        String hql = "FROM Horario h WHERE h.cancha.id = :canchaId AND h.disponible = true";
        return sessionFactory.getCurrentSession()
                .createQuery(hql, Horario.class)
                .setParameter("canchaId", cancha.getId())
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

    @Override
    public void guardar(Horario horario) {
        sessionFactory.getCurrentSession().save(horario);
    }

    @Override
    @Transactional
    public void actualizarHorarios(Horario horarios) {
        sessionFactory.getCurrentSession().update(horarios);
    }

    @Override
    public void eliminarPorId(Long id) {
        Horario horario = obtenerPorId(id);
        if (horario != null) {
            sessionFactory.getCurrentSession().delete(horario);
        }
    }
}