package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tallerwebi.dominio.RepositorioReserva;
import com.tallerwebi.dominio.RepositorioHorario;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ServicioReservaImpl implements ServicioReserva {

    private final RepositorioReserva repositorioReserva;
    private final RepositorioHorario repositorioHorario;

    @Autowired
    public ServicioReservaImpl(RepositorioReserva repositorioReserva, RepositorioHorario repositorioHorario) {
        this.repositorioReserva = repositorioReserva;
        this.repositorioHorario = repositorioHorario;
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

        // if (!this.estaDisponible(reserva.getHorario(), reserva.getFechaReserva())) {
        // throw new RuntimeException("La cancha no está disponible en el horario
        // seleccionado");
        // }
        java.time.LocalDate hoy = java.time.LocalDate.now();
        java.time.LocalDate diaSemana = reserva.getFechaReserva().toLocalDate();

        if (diaSemana.isBefore(hoy)) {
            throw new RuntimeException("No se puede reservar una fecha pasada. Elegí desde hoy en adelante.");
        }

        java.time.DayOfWeek diaSemanaDow = diaSemana.getDayOfWeek();
        java.time.DayOfWeek diaHorario = reserva.getHorario().getDiaSemana();

        if (!diaSemanaDow.equals(diaHorario)) {
            throw new RuntimeException("La fecha elegida " + diaSemanaDow +
                    " no coincide con el día del horario " + diaHorario + ".");
        }
        Horario horario = reserva.getHorario();
        reserva.setFechaCreacion(LocalDateTime.now());
        reserva.setActiva(true);
        this.repositorioHorario.cambiarDisponibilidad(horario.getId(), false);
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

        this.repositorioHorario.cambiarDisponibilidad(reserva.getHorario().getId(), true);
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
    public List<Reserva> obtenerReservasPorUsuarioTodas(Usuario usuario) {
        if (usuario == null) {
            throw new RuntimeException("El usuario es nulo");
        }
        return this.repositorioReserva.porUsuarioTodas(usuario);
    }

    @Override
    public Boolean estaDisponible(Horario horario, LocalDateTime fechaReserva) {
        if (horario == null || fechaReserva == null) {
            throw new RuntimeException("El horario o la fecha de reserva son nulos");
        }
        return this.repositorioReserva.porHorarioYFecha(horario, fechaReserva).isEmpty();
    }
}