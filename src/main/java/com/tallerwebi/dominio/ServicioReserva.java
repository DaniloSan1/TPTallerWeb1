package com.tallerwebi.dominio;
import java.util.List;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
@Service
public interface ServicioReserva {
    Reserva crearReserva(Reserva reserva);
    Reserva cancelarReserva(Long id);
    Reserva obtenerReservaPorId(Long id);
    List<Reserva> obtenerReservasPorUsuario(Usuario usuario);
    Boolean estaDisponible(Horario horario, LocalDateTime fechaReserva);
}