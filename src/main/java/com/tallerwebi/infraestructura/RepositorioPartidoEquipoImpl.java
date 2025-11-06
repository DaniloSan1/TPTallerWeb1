package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.PartidoEquipo;
import com.tallerwebi.dominio.RepositorioPartidoEquipo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("repositorioPartidoEquipo")
@Transactional
public class RepositorioPartidoEquipoImpl implements RepositorioPartidoEquipo {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioPartidoEquipoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardar(PartidoEquipo partidoEquipo) {
        final Session session = sessionFactory.getCurrentSession();
        session.save(partidoEquipo);
    }

    @Override
    public PartidoEquipo buscarPorId(Long id) {
        final Session session = sessionFactory.getCurrentSession();
        return (PartidoEquipo) session.get(PartidoEquipo.class, id);
    }
}