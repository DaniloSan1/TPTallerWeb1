package com.tallerwebi.dominio;
import java.util.List;
import java.time.LocalDateTime;
public interface ServicioReserva {
    Reserva crearReserva(Reserva reserva);
    Boolean cancelarReserva(Long id);
    Reserva obtenerReservaPorId(Long id);
    List<Reserva> obtenerReservasPorUsuario(Usuario usuario);
    Boolean estaDisponible(Horario horario, LocalDateTime fechaReserva);
}