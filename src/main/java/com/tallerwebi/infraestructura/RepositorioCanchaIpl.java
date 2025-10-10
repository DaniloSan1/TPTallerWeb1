package com.tallerwebi.infraestructura;


import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.tallerwebi.dominio.Cancha;
import com.tallerwebi.dominio.RepositorioCancha;

@Repository("repositorioCancha")
@Transactional
public class RepositorioCanchaIpl implements RepositorioCancha {
    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioCanchaIpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Cancha> MostrarCanchasDisponibles() {
        return sessionFactory.getCurrentSession()
                .createQuery("SELECT DISTINCT c FROM Cancha c JOIN c.horarios h WHERE h.disponible = true", Cancha.class)
                .getResultList();
    }

    @Override
    public Cancha BuscarCanchaPorId(Long id) {
        return sessionFactory.getCurrentSession().get(Cancha.class, id);
    }

}
