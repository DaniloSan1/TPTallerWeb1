package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.InscripcionTorneo;
import com.tallerwebi.dominio.RepositorioInscripcionTorneo;
import com.tallerwebi.dominio.Torneo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class RepositorioInscripcionTorneoImpl implements RepositorioInscripcionTorneo {

    private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioInscripcionTorneoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void guardar(InscripcionTorneo inscripcionTorneo) {
        getSession().save(inscripcionTorneo);
    }

    @Override
    public InscripcionTorneo buscarPorId(Long id) {
        return getSession().get(InscripcionTorneo.class, id);
    }

    @Override
    public void eliminar(InscripcionTorneo inscripcionTorneo) {
        getSession().delete(inscripcionTorneo);
    }

    @Override
    public List<InscripcionTorneo> buscarPorTorneo(Long idTorneo) {
        return getSession()
                .createQuery("FROM InscripcionTorneo i WHERE i.torneo.id = :idTorneo", InscripcionTorneo.class)
                .setParameter("idTorneo", idTorneo)
                .getResultList();
    }
}