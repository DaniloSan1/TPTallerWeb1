package com.tallerwebi.presentacion;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tallerwebi.dominio.*;

@Controller
@RequestMapping("/reserva")
@PropertySource("classpath:application.properties")
public class ControladorReserva {

    private final ServicioReserva servicioReserva;
    private final ServicioHorario servicioHorario;
    private final ServicioUsuario servicioUsuario;
    private final ServicioPartido servicioPartido;
    private final ServicioPago servicioPago;
    private Horario guardada;

    @Value("${mercadopago.public.key}")
    private String publicKey;

    @Autowired
    public ControladorReserva(ServicioReserva servicioReserva,
            ServicioHorario servicioHorario,
            ServicioUsuario servicioUsuario,
            ServicioPartido servicioPartido,
            ServicioPago servicioPago) {
        this.servicioReserva = servicioReserva;
        this.servicioHorario = servicioHorario;
        this.servicioUsuario = servicioUsuario;
        this.servicioPartido = servicioPartido;
        this.servicioPago = servicioPago;
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

            String preferenceId = servicioPago.crearPago(titulo, descripcion,
                    BigDecimal.valueOf(horario.getCancha().getPrecio()));

            model.put("mensajeExito", "Reserva creada con éxito para el " + fecha);
            model.put("preferenceId", preferenceId);
            model.put("publicKey", publicKey);
            System.out.println("Preference ID generated: " + preferenceId);
            System.out.println("MP Public Key: " + publicKey);
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
            RedirectAttributes redirectAttributes,
            ModelMap model) {
        try {
            servicioReserva.cancelarReserva(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Reserva cancelada correctamente");

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        }
        return "redirect:/home";
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