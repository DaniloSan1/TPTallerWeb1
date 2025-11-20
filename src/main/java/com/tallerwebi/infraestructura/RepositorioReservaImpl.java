package com.tallerwebi.infraestructura;
import com.tallerwebi.dominio.Cancha;
import com.tallerwebi.dominio.Horario;
import com.tallerwebi.dominio.RepositorioReserva;
import com.tallerwebi.dominio.Reserva;
import com.tallerwebi.dominio.Usuario;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class RepositorioReservaImpl implements RepositorioReserva {

    private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioReservaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Reserva porId(Long id) {
        return sessionFactory.getCurrentSession()
                .get(Reserva.class, id);
    }

    @Override
    public void guardar(Reserva reserva) {
        sessionFactory.getCurrentSession().saveOrUpdate(reserva);
    }

    @Override
    public List<Reserva> porHorarioYFecha(Horario horario, LocalDateTime fechaReserva) {
        String hql = "FROM Reserva r " +
                     "WHERE r.horario = :horario " +
                     "AND r.fechaReserva = :fechaReserva " +
                     "AND r.activa = true";

        return sessionFactory.getCurrentSession()
                .createQuery(hql, Reserva.class)
                .setParameter("horario", horario)
                .setParameter("fechaReserva", fechaReserva)
                .getResultList();
    }

    @Override
    public List<Reserva> porUsuario(Usuario usuario) {
        String hql = "FROM Reserva r " +
                     "WHERE r.usuario = :usuario " +
                     "AND r.activa = true";

        return sessionFactory.getCurrentSession()
                .createQuery(hql, Reserva.class)
                .setParameter("usuario", usuario)
                .getResultList();
    }
    
    @Override
    public List<Reserva> porUsuarioTodas(Usuario usuario) {
        String hql = "FROM Reserva r " +
                     "WHERE r.usuario = :usuario ";

        return sessionFactory.getCurrentSession()
                .createQuery(hql, Reserva.class)
                .setParameter("usuario", usuario)
                .getResultList();
    }
    
    @Override
    public List<Reserva> porCanchaYFecha(Cancha cancha, LocalDate fecha) {
        LocalDateTime inicioDelDia = fecha.atStartOfDay();
        LocalDateTime finDelDia = fecha.atTime(23, 59, 59);

        String hql = "FROM Reserva r " +
                     "WHERE r.horario.cancha = :cancha " +
                     "AND r.activa = true " +
                     "AND r.fechaReserva BETWEEN :inicio AND :fin";

        return sessionFactory.getCurrentSession()
                .createQuery(hql, Reserva.class)
                .setParameter("cancha", cancha)
                .setParameter("inicio", inicioDelDia)
                .setParameter("fin", finDelDia)
                .getResultList();
    }

    @Override
    public List<Reserva> porCancha(Cancha cancha) {
        String hql = "FROM Reserva r WHERE r.horario.cancha = :cancha";
        return sessionFactory.getCurrentSession()
                .createQuery(hql, Reserva.class)
                .setParameter("cancha", cancha)
                .getResultList();
    }

    public void eliminar(Reserva reserva) {
        sessionFactory.getCurrentSession().delete(reserva);
    }
}
