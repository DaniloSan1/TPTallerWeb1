package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.EquipoJugador;
import com.tallerwebi.dominio.RepositorioEquipoJugador;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("repositorioEquipoJugador")
@Transactional
public class RepositorioEquipoJugadorImpl implements RepositorioEquipoJugador {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioEquipoJugadorImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardar(EquipoJugador equipoJugador) {
        final Session session = sessionFactory.getCurrentSession();
        session.save(equipoJugador);
    }

    @Override
    public EquipoJugador buscarPorId(Long id) {
        final Session session = sessionFactory.getCurrentSession();
        return (EquipoJugador) session.get(EquipoJugador.class, id);
    }
}