package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioReserva;
import com.tallerwebi.dominio.Reserva;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class RepositorioReservaEnMemoria implements RepositorioReserva {
    private final Map<Long, Reserva> data = new HashMap<>();
    private final AtomicLong sec = new AtomicLong(1);

    @Override
    public Reserva porId(Long id) {
        return data.get(id);
    }

    @Override
    public void guardar(Reserva reserva) {
        if (reserva.getId() == null) {
            reserva.setId(sec.getAndIncrement());
        }
        data.put(reserva.getId(), reserva);
    }
}
