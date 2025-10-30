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
import org.springframework.web.servlet.ModelAndView;
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
    public ModelAndView crearReserva(
            @RequestParam Long horarioId,
            @RequestParam String fechaReserva,
            @RequestParam Long usuarioId,
            @RequestParam String titulo,
            @RequestParam String descripcion,
            @RequestParam Nivel nivel) {

        ModelAndView mav = new ModelAndView();

        try {
            Horario horario = servicioHorario.obtenerPorId(horarioId);
            Usuario usuario = servicioUsuario.buscarPorId(usuarioId);

            LocalDateTime fecha = LocalDate.parse(fechaReserva).atTime(horario.getHoraInicio());

            // La reserva NO se activa hasta que se pague
            Reserva reserva = new Reserva(horario, usuario, fecha);
            reserva.setActiva(false);

            Reserva reservaCreada = servicioReserva.crearReserva(reserva);

            servicioPartido.crearDesdeReserva(
                    reservaCreada,
                    titulo,
                    descripcion,
                    nivel,
                    0,
                    usuario);

            // Crear preferencia de pago en Mercado Pago
            String preferenceId = servicioPago.crearPago(
                    "Reserva en " + horario.getCancha().getNombre(),
                    "Pago de reserva de cancha",
                    BigDecimal.valueOf(horario.getCancha().getPrecio()),
                    reservaCreada.getId());

            // Guardar el pago en base con estado "Pendiente"
            servicioPago.guardarPago(reservaCreada, usuario, preferenceId, horario.getCancha().getPrecio());

            mav.setViewName("detalleReserva");
            mav.addObject("reserva", reservaCreada);
            mav.addObject("cancha", horario.getCancha());
            mav.addObject("horario", horario);
            mav.addObject("publicKey", publicKey);
            mav.addObject("preferenceId", preferenceId);
            mav.addObject("mensajeExito", "Reserva creada. Completá el pago para activarla.");

            System.out.println(" Preferencia creada: " + preferenceId);

        } catch (Exception e) {
            e.printStackTrace();
            mav.setViewName("reservaForm");
            mav.addObject("error", "Error al crear la reserva: " + e.getMessage());
            Horario horario = servicioHorario.obtenerPorId(horarioId);
            Cancha cancha = horario.getCancha();
            mav.addObject("cancha", cancha);
            mav.addObject("horario", horario);
            mav.addObject("usuarioId", usuarioId);
            mav.addObject("horarioId", horarioId);
        }

        return mav;
    }

    @GetMapping("/{id}")
    public String verDetalleReserva(@PathVariable Long id, ModelMap model) {
        Reserva reserva = servicioReserva.obtenerReservaPorId(id);
        model.put("reserva", reserva);
        model.put("cancha", reserva.getHorario().getCancha());
        model.put("horario", reserva.getHorario());
        return "detalleReserva";
    }

    @PostMapping("/cancelar/{id}")
    public String cancelarReserva(
            @PathVariable Long id,
            @RequestParam Long usuarioId,
            @RequestParam Long horarioId,
            RedirectAttributes redirectAttributes) {
        try {
            servicioReserva.cancelarReserva(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Reserva cancelada correctamente.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        }
        return "redirect:/home";
    }
}
