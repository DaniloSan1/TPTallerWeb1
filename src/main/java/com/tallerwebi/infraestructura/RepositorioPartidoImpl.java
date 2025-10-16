package com.tallerwebi.infraestructura;

import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tallerwebi.dominio.Partido;
import com.tallerwebi.dominio.RepositorioPartido;

@Repository 
@Transactional
public class RepositorioPartidoImpl implements RepositorioPartido {

    private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioPartidoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Partido porId(Long id) {
        final Session session = sessionFactory.getCurrentSession();
        return session.get(Partido.class, id, LockMode.PESSIMISTIC_WRITE);
    }

    @Override
    public List<Partido> todos() {
        final Session session = sessionFactory.getCurrentSession();
        String hql = "FROM Partido p " +
                     "WHERE p.reserva.activa = true";
        return session.createQuery(hql, Partido.class).list();
    }

    @Override
    public void guardar(Partido p) {
        final Session session = sessionFactory.getCurrentSession();
        session.save(p);
    }
}
