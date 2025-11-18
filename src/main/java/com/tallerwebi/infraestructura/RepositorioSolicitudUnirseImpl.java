package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.EstadoSolicitud;
import com.tallerwebi.dominio.Partido;
import com.tallerwebi.dominio.SolicitudUnirse;
import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class RepositorioSolicitudUnirseImpl implements RepositorioSolicitudUnirse {

    private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioSolicitudUnirseImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardar(SolicitudUnirse solicitud) {
        final Session session = sessionFactory.getCurrentSession();
        session.save(solicitud);
    }

    @Override
    public Optional<SolicitudUnirse> buscarPorToken(String token) {
        final Session session = sessionFactory.getCurrentSession();
        String hql = "FROM SolicitudUnirse s WHERE s.token = :token";
        var query = session.createQuery(hql, SolicitudUnirse.class);
        query.setParameter("token", token);
        List<SolicitudUnirse> resultados = query.list();
        if (resultados == null || resultados.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(resultados.get(0));
    }

    @Override
    public List<SolicitudUnirse> listarPorPartidoYEstado(Partido partido, EstadoSolicitud estado) {
        final Session session = sessionFactory.getCurrentSession();
        String hql = "FROM SolicitudUnirse s WHERE s.partido = :partido AND s.estado = :estado";
        var query = session.createQuery(hql, SolicitudUnirse.class);
        query.setParameter("partido", partido);
        query.setParameter("estado", estado);
        return query.list();
    }

    @Override
    public List<SolicitudUnirse> listarPendientesPorEmailDestino(String emailDestino) {
        final Session session = sessionFactory.getCurrentSession();
        String hql = "FROM SolicitudUnirse s JOIN FETCH s.creador JOIN FETCH s.partido WHERE s.emailDestino = :emailDestino AND s.estado = :estado";
        var query = session.createQuery(hql, SolicitudUnirse.class);
        query.setParameter("emailDestino", emailDestino);
        query.setParameter("estado", EstadoSolicitud.PENDIENTE);
        return query.list();
    }

}
