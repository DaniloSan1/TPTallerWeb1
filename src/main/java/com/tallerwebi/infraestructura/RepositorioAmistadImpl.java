package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Amistad;
import com.tallerwebi.dominio.RepositorioAmistad;
import com.tallerwebi.dominio.Usuario;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RepositorioAmistadImpl implements RepositorioAmistad {


    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void guardar(Amistad amistad) {
        sessionFactory.getCurrentSession().save(amistad);
    }

    @Override
    public Amistad buscarPorUsuarios(Usuario usuario1, Usuario usuario2) {
        String hql = "FROM Amistad a WHERE a.usuario1 = :u1 AND a.usuario2 = :u2";
        return (Amistad) sessionFactory.getCurrentSession()
            .createQuery(hql)
            .setParameter("u1", usuario1)
            .setParameter("u2", usuario2)
            .uniqueResult();
    }

    @Override
    public Amistad buscarPorId(Long id) {
        return sessionFactory.getCurrentSession().get(Amistad.class, id);
    }

    @Override
    public List<Amistad> buscarPendientes(Usuario usuario) {
        String hql = "FROM Amistad a WHERE a.usuario2 = :usuario AND a.estadoDeAmistad = 'PENDIENTE'";
        return sessionFactory.getCurrentSession()
            .createQuery(hql)
            .setParameter("usuario", usuario)
            .list();
    }

    @Override
    public List<Amistad> buscarAceptadas(Usuario usuario) {
        String hql = "FROM Amistad a WHERE (a.usuario1 = :usuario OR a.usuario2 = :usuario) AND a.estadoDeAmistad = 'ACEPTADA'";
        return sessionFactory.getCurrentSession()
            .createQuery(hql)
            .setParameter("usuario", usuario)
            .list();
    }

    @Override
    public void eliminar(Amistad amistad) {
        sessionFactory.getCurrentSession().delete(amistad);
    }


}
