package com.tallerwebi.infraestructura;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tallerwebi.dominio.Partido;
import com.tallerwebi.dominio.RepositorioPartido;

@Repository("repositorioPartido")
@Transactional
public class RepositorioPartidoImpl implements RepositorioPartido {
    private SessionFactory sessionFactory;

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
        throw new UnsupportedOperationException("Unimplemented method 'todos'");
    }

    @Override
    public void guardar(Partido partido) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'guardar'");
    }
}
