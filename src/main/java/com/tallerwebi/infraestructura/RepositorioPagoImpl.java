package com.tallerwebi.infraestructura;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tallerwebi.dominio.Pago;
import com.tallerwebi.dominio.RepositorioPago;

@Repository
public class RepositorioPagoImpl implements RepositorioPago {
    
   private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioPagoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardarPago(Pago pago) {
        sessionFactory.getCurrentSession().save(pago);
    }
    
    @Override
    public Pago buscarPagoPorId(Long id) {
        return sessionFactory.getCurrentSession().get(Pago.class, id);
    }
}
