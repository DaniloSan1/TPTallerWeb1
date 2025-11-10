package com.tallerwebi.infraestructura;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tallerwebi.dominio.Nivel;
import com.tallerwebi.dominio.Partido;
import com.tallerwebi.dominio.PartidoEquipo;
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
    public Partido obtenerPorIdConJugadores(Long id) {
        final Session session = sessionFactory.getCurrentSession();
        String hql = "FROM Partido p LEFT JOIN FETCH p.equipos pe LEFT JOIN FETCH pe.equipo LEFT JOIN FETCH pe.equipo.jugadores ej LEFT JOIN FETCH ej.usuario WHERE p.id = :id";
        var query = session.createQuery(hql, Partido.class);
        query.setParameter("id", id);
        Partido partido = query.uniqueResult();
        if (partido != null) {
            for (PartidoEquipo pe : partido.getEquipos()) {
                Hibernate.initialize(pe.getEquipo().getJugadores());
            }
        }
        return partido;
    }

    @Override
    public List<Partido> listar(String busqueda, Zona filtroZona, Nivel filtroNivel) {
        final Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT DISTINCT p " +
                "FROM Partido p " +
                "JOIN p.reserva r " +
                "JOIN r.horario h " +
                "JOIN h.cancha c " +
                "WHERE r.activa = true";
        if (filtroZona != null) {
            hql += " AND c.zona = :zona";
        }
        if (filtroNivel != null) {
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

    @Override
    public List<Partido> listarPorCreador(Long idCreador) {
        final Session session = sessionFactory.getCurrentSession();
        // Consulta HQL compatible con Java 11 y la estructura de la entidad Partido
        String hql = "FROM Partido p WHERE p.creador.id = :idCreador";

        var query = session.createQuery(hql, Partido.class);
        query.setParameter("idCreador", idCreador);
        return query.list();
    }

    @Override
    public void actualizar(Partido p) {
        final Session session = sessionFactory.getCurrentSession();
        session.update(p);
    }

    @Override
    public List<Partido> listarPorEquipoConInfoCancha(Long idEquipo) {
        final Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT DISTINCT p FROM Partido p JOIN p.equipos pe LEFT JOIN FETCH p.reserva r LEFT JOIN FETCH r.horario h LEFT JOIN FETCH h.cancha c LEFT JOIN FETCH c.fotos WHERE pe.equipo.id = :idEquipo";
        var query = session.createQuery(hql, Partido.class);
        query.setParameter("idEquipo", idEquipo);
        return query.list();
    }

}
