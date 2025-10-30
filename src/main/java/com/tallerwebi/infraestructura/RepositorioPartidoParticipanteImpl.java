package com.tallerwebi.infraestructura;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tallerwebi.dominio.PartidoParticipante;
import com.tallerwebi.dominio.RepositorioPartidoParticipante;

@Repository // podés dejar el nombre por defecto, no hace falta ("repositorioPartido")
@Transactional
public class RepositorioPartidoParticipanteImpl implements RepositorioPartidoParticipante {

    private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioPartidoParticipanteImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public PartidoParticipante buscarPorId(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(PartidoParticipante.class, id);
    }

    @Override
    public void guardar(PartidoParticipante partidoParticipante) {
        this.sessionFactory.getCurrentSession().save(partidoParticipante);
    }

    @Override
    public void eliminar(PartidoParticipante partidoParticipante) {
        this.sessionFactory.getCurrentSession().delete(partidoParticipante);
    }
}
