package com.tallerwebi.infraestructura;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import com.tallerwebi.dominio.FotoCancha;
import com.tallerwebi.dominio.RepositorioFotoCancha;

@Repository("repositorioFotoCancha")
@Transactional
public class RepositorioFotoCanchaImpl implements RepositorioFotoCancha {
    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioFotoCanchaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<FotoCancha> obtenerFotosCancha(Long canchaId) {
        String hql = "FROM FotoCancha f WHERE f.cancha.id = :canchaId";
        return this.sessionFactory.getCurrentSession().createQuery(hql, FotoCancha.class)
                .setParameter("canchaId", canchaId)
                .getResultList();
    }

    @Override
    public FotoCancha obtenerPrimeraFotoCancha(Long canchaId) {
        String hql = "FROM FotoCancha f WHERE f.cancha.id = :canchaId";
        return this.sessionFactory.getCurrentSession().createQuery(hql, FotoCancha.class)
                .setParameter("canchaId", canchaId)
                .setMaxResults(1)
                .uniqueResult();
    }
}
