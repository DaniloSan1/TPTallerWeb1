package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Gol;
import com.tallerwebi.dominio.Partido;
import com.tallerwebi.dominio.RepositorioGoles;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class RepositorioGolesImpl implements RepositorioGoles {

    private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioGolesImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardar(Gol gol) {
        sessionFactory.getCurrentSession().save(gol);
    }

    @Override
    public List<Gol> buscarPorPartido(Partido partido) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Gol WHERE partido = :partido", Gol.class)
                .setParameter("partido", partido)
                .list();
    }
}