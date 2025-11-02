package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Equipo;
import com.tallerwebi.dominio.RepositorioEquipo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("repositorioEquipo")
@Transactional
public class RepositorioEquipoImpl implements RepositorioEquipo {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioEquipoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardar(Equipo equipo) {
        final Session session = sessionFactory.getCurrentSession();
        session.save(equipo);
    }

    @Override
    public Equipo buscarPorId(Long id) {
        final Session session = sessionFactory.getCurrentSession();
        return (Equipo) session.get(Equipo.class, id);
    }

    @Override
    public void modificar(Equipo equipo) {
        final Session session = sessionFactory.getCurrentSession();
        session.update(equipo);
    }
}