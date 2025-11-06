package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioUsuario;
import com.tallerwebi.dominio.Usuario;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Primary
@Repository("repositorioUsuario")
@Transactional
public class RepositorioUsuarioImpl implements RepositorioUsuario {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioUsuarioImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public List<Usuario> filtrarPorUsername(String username) {
        String hql = "FROM Usuario u WHERE u.username LIKE :username";
        return sessionFactory.getCurrentSession().createQuery(hql, Usuario.class)
            .setParameter("username", "%" + username + "%") //%username%
            .getResultList();
    }


    @Override
    public Usuario buscarUsuario(String email, String password) {
        final Session session = sessionFactory.getCurrentSession();
        String hql = "FROM Usuario WHERE email = :email AND password = :password";
        Query<Usuario> query = session.createQuery(hql, Usuario.class);
        query.setParameter("email", email);
        query.setParameter("password", password);
        return query.uniqueResult();
    }

    @Override
    public void guardar(Usuario usuario) {
        sessionFactory.getCurrentSession().save(usuario);
    }

    @Override
    public Usuario buscar(String email) {
        String hql = "FROM Usuario WHERE email = :email";
        Query<Usuario> query = sessionFactory.getCurrentSession().createQuery(hql, Usuario.class);
        query.setParameter("email", email);
        return query.uniqueResult();
    }

    @Override
    public void modificar(Usuario usuario) {
        sessionFactory.getCurrentSession().update(usuario);
    }

    @Override
    public Usuario buscarPorId(Long id) {
        String hql = "FROM Usuario WHERE id = :id";
        Query<Usuario> query = sessionFactory.getCurrentSession().createQuery(hql, Usuario.class);
        query.setParameter("id", id);
        return query.uniqueResult();
    }

    @Override
    public Usuario buscarPorUsername(String username) {
        String hql = "FROM Usuario WHERE username = :username";
        Query<Usuario> query = sessionFactory.getCurrentSession().createQuery(hql, Usuario.class);
        query.setParameter("username", username);
        return query.uniqueResult();
    }
}
