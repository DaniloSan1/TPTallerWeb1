package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Partido;
import com.tallerwebi.dominio.PartidoParticipante;
import com.tallerwebi.dominio.RepositorioPartido;
import com.tallerwebi.dominio.RepositorioPartidoParticipante;
import com.tallerwebi.dominio.Usuario;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository // podés dejar el nombre por defecto, no hace falta ("repositorioPartido")
@Transactional
public class RepositorioPartidoParticipanteImpl implements RepositorioPartidoParticipante {

    private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioPartidoParticipanteImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardar(PartidoParticipante partidoParticipante) {
        this.sessionFactory.getCurrentSession().save(partidoParticipante);
    }
}
