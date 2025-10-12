package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Partido;
import com.tallerwebi.dominio.RepositorioPartido;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository // podés dejar el nombre por defecto, no hace falta ("repositorioPartido")
@Transactional
public class RepositorioPartidoImpl implements RepositorioPartido {

    private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioPartidoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
   public Partido porId(Long id) {
        return this.sessionFactory.getCurrentSession().get(Partido.class, id);
    }

    @Override
    public List<Partido> todos() {
        final Session session = sessionFactory.getCurrentSession();
        return (List<Partido>) session.createCriteria(Partido.class).list();

    }

    @Override
    public void guardar(Partido p) {
        sessionFactory.getCurrentSession().save(p);
    }
}
