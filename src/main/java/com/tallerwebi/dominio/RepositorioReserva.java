package com.tallerwebi.dominio;
import java.util.List;
import java.time.LocalDateTime;

public interface RepositorioReserva {
    Reserva porId(Long id);
    void guardar(Reserva reserva);
    List<Reserva> porHorarioYFecha(Horario horario, LocalDateTime fechaReserva);
    List<Reserva> porUsuario(Usuario usuario);
    List<Reserva> porUsuarioTodas(Usuario usuario);
}
