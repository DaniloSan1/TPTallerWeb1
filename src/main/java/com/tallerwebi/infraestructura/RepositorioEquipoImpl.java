package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Equipo;
import com.tallerwebi.dominio.RepositorioEquipo;
import com.tallerwebi.dominio.Usuario;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("repositorioEquipo")
@Transactional
public class RepositorioEquipoImpl implements RepositorioEquipo {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioEquipoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardar(Equipo equipo) {
        final Session session = sessionFactory.getCurrentSession();
        session.save(equipo);
    }

    @Override
    public Equipo buscarPorId(Long id) {
        final Session session = sessionFactory.getCurrentSession();
        return (Equipo) session.get(Equipo.class, id);
    }

    @Override
    public void modificar(Equipo equipo) {
        final Session session = sessionFactory.getCurrentSession();
        session.update(equipo);
    }

    @Override
    public List<Equipo> buscarEquiposPorUsuario(Usuario usuario) {
        final Session session = sessionFactory.getCurrentSession();
        return session
                .createQuery("SELECT e FROM Equipo e JOIN e.jugadores ej WHERE ej.usuario = :usuario", Equipo.class)
                .setParameter("usuario", usuario)
                .getResultList();
    }

    @Override
    public List<Equipo> buscarEquiposPorUsuarioYNombre(Usuario usuario, String nombre) {
        final Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT DISTINCT e FROM Equipo e LEFT JOIN e.jugadores ej WHERE (e.creadoPor = :usuario OR ej.usuario = :usuario)";
        if (nombre != null && !nombre.trim().isEmpty()) {
            hql += " AND LOWER(e.nombre) LIKE LOWER(:nombre)";
        }

        var query = session.createQuery(hql, Equipo.class)
                .setParameter("usuario", usuario);

        if (nombre != null && !nombre.trim().isEmpty()) {
            query.setParameter("nombre", "%" + nombre.trim() + "%");
        }

        return query.getResultList();
    }

    @Override
    public Equipo buscarPorIdYUsuario(Long id, Usuario usuario) {
        final Session session = sessionFactory.getCurrentSession();
        return (Equipo) session
                .createQuery("SELECT e FROM Equipo e WHERE e.id = :id AND e.creadoPor = :usuario", Equipo.class)
                .setParameter("id", id)
                .setParameter("usuario", usuario)
                .uniqueResult();
    }

    @Override
    public List<Equipo> listarTodos() {
        final Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT e FROM Equipo e", Equipo.class).getResultList();
    }

    @Override
    public Equipo buscarEquipoDelUsuario(Usuario usuario) {
        final Session session = sessionFactory.getCurrentSession();
        return (Equipo) session
                .createQuery("SELECT e FROM Equipo e JOIN e.jugadores ej WHERE ej.usuario = :usuario", Equipo.class)
                .setParameter("usuario", usuario)
                .uniqueResult();
    }
}