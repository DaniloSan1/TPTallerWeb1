package com.tallerwebi.infraestructura;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.tallerwebi.dominio.RepositorioCalificacion;
import com.tallerwebi.dominio.Calificacion;

@Repository
@Transactional
public class RepositorioCalificacionImpl implements RepositorioCalificacion {
    
    private final SessionFactory sessionFactory;
    
    @Autowired
    public RepositorioCalificacionImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardarCalificacion(Calificacion calificacion) {
        if (calificacion.getId() == null) {
            sessionFactory.getCurrentSession().save(calificacion);
        } else {
            sessionFactory.getCurrentSession().merge(calificacion);
        }
    }
    @Override
    public List <Calificacion> obtenerPorPartido(Long idPartido) {
        return sessionFactory.getCurrentSession()
        .createQuery("from Calificacion c where c.partido.id = :idPartido", Calificacion.class)
        .setParameter("idPartido", idPartido)
        .list();
    }
    @Override
    public Boolean existeCalificacion(Long calificadorId, Long calificadoId, Long partidoId) {
      Long contador = sessionFactory.getCurrentSession()
      .createQuery("select count(*) from Calificacion c where c.calificador.id = :calificadorId and c.calificado.id = :calificadoId and c.partido.id = :partidoId", Long.class)
      .setParameter("calificadorId", calificadorId)
      .setParameter("calificadoId", calificadoId)
      .setParameter("partidoId", partidoId)
      .getSingleResult();
      return contador != null && contador > 0;  
    }

    @Override
    public int contarCalificacionesDelUsuario(Long calificadoId){
        String hql ="SELECT COUNT(c) FROM Calificacion c WHERE c.calificado.id = :calificadoId";
     return ((Long) this.sessionFactory.getCurrentSession().createQuery(hql)
                .setParameter("calificadoId",calificadoId)
                .getSingleResult()).intValue();
    }

    @Override
    public List<Calificacion> obtenerPorCalificado(Long calificadoId){
        String hql ="FROM Calificacion c WHERE c.calificado.id = :calificadoId";
        return sessionFactory.getCurrentSession().createQuery(hql,Calificacion.class)
        .setParameter("calificadoId", calificadoId)
        .getResultList();
    }
    
    @Override 
    public List<Calificacion>obtenerPorCalificador(Long calificadorId){
        String hql ="FROM Calificacion c WHERE c.calificador.id = :calificadorId";
        return sessionFactory.getCurrentSession().createQuery(hql,Calificacion.class)
        .setParameter("calificadorId", calificadorId)
        .getResultList(); 
    }

    @Override
    public Calificacion obtenerPorId(Long calificacionId) {
        return sessionFactory.getCurrentSession().get(Calificacion.class, calificacionId);
    }
}