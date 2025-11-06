package com.tallerwebi.infraestructura;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tallerwebi.dominio.ReseniaCancha;
import com.tallerwebi.dominio.RespositorioReseniaCancha;

@Repository
@Transactional
public class RepositorioReseniaCanchaImpl implements RespositorioReseniaCancha {
       private final SessionFactory sessionFactory;
    @Autowired
    public RepositorioReseniaCanchaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardar(ReseniaCancha reseniaCancha) {
        this.sessionFactory.getCurrentSession().save(reseniaCancha);
    }

    @Override
    public List<ReseniaCancha> obtenerReseniasPorCancha(Long canchaId) {
        String hql = "FROM ReseniaCancha r WHERE r.cancha.id = :canchaId";
        return this.sessionFactory.getCurrentSession().createQuery(hql, ReseniaCancha.class)
                .setParameter("canchaId", canchaId)
                .getResultList();
    }

    @Override
    public List<ReseniaCancha> obtenerReseniasPorUsuario(Long usuarioId) {
        String hql = "FROM ReseniaCancha r WHERE r.usuario.id = :usuarioId";
        return this.sessionFactory.getCurrentSession().createQuery(hql, ReseniaCancha.class)
                .setParameter("usuarioId", usuarioId)
                .getResultList();
    }

    @Override
    public int contarReseniasPorCancha(Long canchaId) {
        String hql = "SELECT COUNT(r) FROM ReseniaCancha r WHERE r.cancha.id = :canchaId";
        return ((Long) this.sessionFactory.getCurrentSession().createQuery(hql)
                .setParameter("canchaId", canchaId)
                .getSingleResult()).intValue();
    }

    @Override
    public List<ReseniaCancha> buscarReseniaPreviaDelUsuarioAUnaCanchaDeterminada(Long usuarioId,Long canchaId){
    
        String hql = "FROM ReseniaCancha r WHERE r.usuario.id = :usuarioId AND r.cancha.id = :canchaId";
        return this.sessionFactory.getCurrentSession()
        .createQuery(hql, ReseniaCancha.class)
        .setParameter("usuarioId", usuarioId)
        .setParameter("canchaId", canchaId)
        .setMaxResults(1)
        .getResultList();

    }

}
