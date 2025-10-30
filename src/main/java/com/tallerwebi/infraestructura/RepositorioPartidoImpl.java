package com.tallerwebi.infraestructura;

import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tallerwebi.dominio.Nivel;
import com.tallerwebi.dominio.Partido;
import com.tallerwebi.dominio.RepositorioPartido;
import com.tallerwebi.dominio.Zona;

@Repository 
@Transactional
public class RepositorioPartidoImpl implements RepositorioPartido {

    private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioPartidoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Partido porId(Long id) {
        final Session session = sessionFactory.getCurrentSession();
        return session.get(Partido.class, id, LockMode.PESSIMISTIC_WRITE);
    }

    @Override
    public List<Partido> listar(String busqueda, Zona filtroZona, Nivel filtroNivel) {
        final Session session = sessionFactory.getCurrentSession();
        String hql =  "SELECT DISTINCT p " +
        "FROM Partido p " +
        "JOIN p.reserva r " +
        "JOIN r.horario h " +   
        "JOIN h.cancha c " +    
        "WHERE r.activa = true";
        if (filtroZona != null) {
            hql += " AND c.zona = :zona";
        }
        if (filtroNivel != null ) {
            hql += " AND p.nivel = :nivel";
        }
        if (busqueda != null && !busqueda.isEmpty()) {
            hql += " AND p.titulo LIKE :busqueda ";
        }
        var query = session.createQuery(hql, Partido.class);
        if (filtroZona != null) {
            query.setParameter("zona", filtroZona);       
        }
        if (filtroNivel != null) {
            query.setParameter("nivel", filtroNivel);
        } 
        if (busqueda != null && !busqueda.isEmpty()) {
            query.setParameter("busqueda", "%" + busqueda + "%");
        }
        return query.list();
    }

    @Override
    public void guardar(Partido p) {
        final Session session = sessionFactory.getCurrentSession();
        session.save(p);
    }

}

