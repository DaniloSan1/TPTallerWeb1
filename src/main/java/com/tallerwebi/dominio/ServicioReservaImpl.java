package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;
import java.util.List;

public class ServicioReservaImpl implements ServicioReserva {

    private final RepositorioReserva repositorioReserva;

    @Autowired
    public ServicioReservaImpl(RepositorioReserva repositorioReserva) {
        this.repositorioReserva = repositorioReserva;
    }

    @Override
    public Reserva crearReserva(Reserva reserva) {
        if (reserva == null) {
            throw new RuntimeException("La reserva no puede ser nula");
        }
        if (reserva.getHorario() == null || reserva.getUsuario() == null) {
            throw new RuntimeException("El horario o el usuario son nulos");
        }
        if (reserva.getFechaReserva() == null) {
            throw new RuntimeException("La fecha de la reserva no puede ser nula");
        }

        if (!this.estaDisponible(reserva.getHorario(), reserva.getFechaReserva())) {
            throw new RuntimeException("La cancha no está disponible en el horario seleccionado");
        }

        reserva.setFechaCreacion(LocalDateTime.now());
        reserva.setActiva(true);
        this.repositorioReserva.guardar(reserva);

        return reserva;
    }

    @Override
    public Reserva cancelarReserva(Long id) {
        if (id == null) {
            throw new RuntimeException("El id de la reserva no puede ser nulo");
        }

        Reserva reserva = this.obtenerReservaPorId(id);
        if (reserva == null) {
            throw new RuntimeException("No se encontró la reserva con el id especificado");
        }
        if (!reserva.getActiva()) {
            throw new RuntimeException("La reserva ya está cancelada");
        }

        reserva.cancelar();
        this.repositorioReserva.guardar(reserva);
        return reserva;
    }

    @Override
    public Reserva obtenerReservaPorId(Long id) {
        if (id == null) {
            throw new RuntimeException("El id es nulo");
        }
        return this.repositorioReserva.porId(id);
    }

    @Override
    public List<Reserva> obtenerReservasPorUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new RuntimeException("El usuario es nulo");
        }
        return this.repositorioReserva.porUsuario(usuario);
    }

    @Override
    public Boolean estaDisponible(Horario horario, LocalDateTime fechaReserva) {
        if (horario == null || fechaReserva == null) {
            throw new RuntimeException("El horario o la fecha de reserva son nulos");
        }
        return this.repositorioReserva.porHorarioYFecha(horario, fechaReserva).isEmpty();
    }
}
