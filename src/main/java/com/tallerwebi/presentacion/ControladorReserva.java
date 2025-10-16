package com.tallerwebi.presentacion;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import com.tallerwebi.dominio.*;

@Controller
@RequestMapping("/reserva")
public class ControladorReserva {

    private final ServicioReserva servicioReserva;
    private final ServicioHorario servicioHorario;
    private final ServicioUsuario servicioUsuario;
    private final ServicioPartido servicioPartido;
    private Horario guardada;

    @Autowired
    public ControladorReserva(ServicioReserva servicioReserva,
            ServicioHorario servicioHorario,
            ServicioUsuario servicioUsuario,
            ServicioPartido servicioPartido) {
        this.servicioReserva = servicioReserva;
        this.servicioHorario = servicioHorario;
        this.servicioUsuario = servicioUsuario;
        this.servicioPartido = servicioPartido;
    }

    @GetMapping("/nueva")
    public String mostrarFormularioReserva(
            @RequestParam Long usuarioId,
            @RequestParam Long horarioId,
            ModelMap model) {

        Horario horario = servicioHorario.obtenerPorId(horarioId);
        Cancha cancha = horario.getCancha();

        model.put("usuarioId", usuarioId);
        model.put("horarioId", horario.getId());
        model.put("cancha", cancha);
        model.put("horario", horario);

        return "reservaForm";
    }

    @PostMapping("/crear")
    public String crearReserva(
            @RequestParam Long horarioId,
            @RequestParam String fechaReserva,
            @RequestParam Long usuarioId,
            @RequestParam String titulo,
            @RequestParam String descripcion,
            @RequestParam Nivel nivel,
            ModelMap model) {

        try {
            if (usuarioId == null)
                throw new RuntimeException("El usuario es nulo");
            if (horarioId == null)
                throw new RuntimeException("El horario es nulo");
            if (fechaReserva == null)
                throw new RuntimeException("La fecha es nula");

            Horario horario = servicioHorario.obtenerPorId(horarioId);
            Usuario usuario = servicioUsuario.buscarPorId(usuarioId);

            LocalDateTime fecha = LocalDate.parse(fechaReserva).atTime(horario.getHoraInicio());
            Reserva reserva = new Reserva(horario, usuario, fecha);

            Reserva reservaCreada = servicioReserva.crearReserva(reserva);
            servicioPartido.crearDesdeReserva(
                    reservaCreada,
                    titulo,
                    descripcion,
                    nivel,
                    0, // que venga de la cancha
                    usuario);

            model.put("mensajeExito", "Reserva creada con éxito para el " + fecha);
            return "redirect:/reserva/" + reservaCreada.getId();

        } catch (Exception e) {
            model.put("error", e.getMessage());
            Horario horario = servicioHorario.obtenerPorId(horarioId);
            Cancha cancha = horario.getCancha();

            model.put("usuarioId", usuarioId);
            model.put("horarioId", horarioId);
            model.put("cancha", cancha);
            model.put("horario", horario);

            return "reservaForm";
        }
    }

    @PostMapping("/cancelar/{id}")
    public String cancelarReserva(
            @PathVariable Long id,
            @RequestParam Long usuarioId,
            @RequestParam Long horarioId,
            ModelMap model) {
        try {
            servicioReserva.cancelarReserva(id);
            model.put("mensajeExito", "Reserva cancelada correctamente");
        } catch (RuntimeException e) {
            model.put("mensajeError", e.getMessage());
        }
        return "redirect:/reserva/" + id;
    }

    @GetMapping("/{id}")
    public String verDetalleReserva(@PathVariable Long id, ModelMap model) {
        Reserva reserva = servicioReserva.obtenerReservaPorId(id);
        model.put("reserva", reserva);
        model.put("cancha", reserva.getHorario().getCancha());
        model.put("horario", reserva.getHorario());
        return "detalleReserva";
    }
}