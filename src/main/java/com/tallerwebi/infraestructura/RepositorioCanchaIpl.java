package com.tallerwebi.infraestructura;


import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.tallerwebi.dominio.Cancha;
import com.tallerwebi.dominio.RepositorioCancha;
import com.tallerwebi.dominio.Zona;

@Repository
@Transactional
public class RepositorioCanchaIpl implements RepositorioCancha {

    @Override
    public List<Cancha> obtenerTodasLasCanchas() {
        String hql = "FROM Cancha";
        return sessionFactory.getCurrentSession().createQuery(hql, Cancha.class).getResultList();
    }
    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioCanchaIpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Cancha> MostrarCanchasConHorariosDisponibles(String busqueda, Zona zona, Double precioMinimo, Double precioMaximo) {
        String query = "SELECT DISTINCT c FROM Cancha c JOIN c.horarios h WHERE h.disponible = true";

        if (busqueda != null && !busqueda.trim().isEmpty()) {
            query += " AND (lower(c.nombre) LIKE :busqueda OR lower(c.direccion) LIKE :busqueda)";
        }
        if (zona != null) {
            query += " AND c.zona = :zona";
        }
        if (precioMinimo != null) {
            query += " AND c.precio >= :precioMinimo";
        }
        if (precioMaximo != null) {
            query += " AND c.precio <= :precioMaximo";
        }

        var queryObject = sessionFactory.getCurrentSession().createQuery(query, Cancha.class);

        if (busqueda != null && !busqueda.trim().isEmpty()) {
            queryObject.setParameter("busqueda", "%" + busqueda.toLowerCase() + "%");
        }
        if (zona != null) {
            queryObject.setParameter("zona", zona);
        }
        if (precioMinimo != null) {
            queryObject.setParameter("precioMinimo", precioMinimo);
        }
        if (precioMaximo != null) {
            queryObject.setParameter("precioMaximo", precioMaximo);
        }

        return queryObject.getResultList();
    }

    @Override
    public Cancha BuscarCanchaPorId(Long id) {
        return sessionFactory.getCurrentSession().get(Cancha.class, id);
    }
}
    
