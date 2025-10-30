package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Cancha;
import com.tallerwebi.dominio.Horario;
import com.tallerwebi.dominio.Reserva;
import com.tallerwebi.dominio.ServicioHorario;
import com.tallerwebi.dominio.ServicioPago;
import com.tallerwebi.dominio.ServicioReserva;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/pago")
public class ControladorPago {

    private final ServicioReserva servicioReserva;
    private final ServicioPago servicioPago;
    private final ServicioHorario servicioHorario;

    @Autowired
    public ControladorPago(ServicioReserva servicioReserva, ServicioPago servicioPago, ServicioHorario servicioHorario) {
        this.servicioReserva = servicioReserva;
        this.servicioPago = servicioPago;
        this.servicioHorario = servicioHorario;
    }

    @GetMapping("/iniciar/{reservaId}")
    public String iniciarPago(@PathVariable Long reservaId, ModelMap model) {
        try {
            Reserva reserva = servicioReserva.obtenerReservaPorId(reservaId);
            if (reserva == null) throw new RuntimeException("Reserva no encontrada");
            Horario horario = servicioHorario.obtenerPorId(reserva.getHorario().getId());
            Cancha cancha = horario.getCancha();
            BigDecimal monto = BigDecimal.valueOf(cancha.getPrecio());

            String preferenceId = servicioPago.crearPago(
                    "Reserva " + cancha.getNombre(),
                    "Pago de reserva de cancha",
                    monto
            );

            model.put("reserva", reserva);
            model.put("cancha", cancha);
            model.put("horario", reserva.getHorario());
            model.put("preferenceId", preferenceId);

            return "detalleReserva";
        } catch (Exception e) {
            e.printStackTrace();
            Reserva reserva = servicioReserva.obtenerReservaPorId(reservaId);
            model.put("error", e.getMessage());
            if (reserva != null) {
                model.put("reserva", reserva);
                model.put("cancha", reserva.getHorario().getCancha());
                model.put("horario", reserva.getHorario());
            }
            return "detalleReserva";
        }
    }

    @GetMapping("/exito/{reservaId}")
    public String pagoExitoso(@PathVariable Long reservaId, ModelMap model) {
        Reserva reserva = servicioReserva.obtenerReservaPorId(reservaId);
        model.put("reserva", reserva);
        model.put("cancha", reserva.getHorario().getCancha());
        model.put("horario", reserva.getHorario());
        model.put("mensajeExito", "¡Pago realizado con éxito!");
        return "detalleReserva";
    }

    @GetMapping("/error/{reservaId}")
    public String pagoFallido(@PathVariable Long reservaId, ModelMap model) {
        Reserva reserva = servicioReserva.obtenerReservaPorId(reservaId);
        model.put("reserva", reserva);
        model.put("cancha", reserva.getHorario().getCancha());
        model.put("horario", reserva.getHorario());
        model.put("error", "Error al procesar el pago");
        return "detalleReserva";
    }

    @GetMapping("/pendiente/{reservaId}")
    public String pagoPendiente(@PathVariable Long reservaId, ModelMap model) {
        Reserva reserva = servicioReserva.obtenerReservaPorId(reservaId);
        model.put("reserva", reserva);
        model.put("cancha", reserva.getHorario().getCancha());
        model.put("horario", reserva.getHorario());
        model.put("error", "El pago está pendiente de confirmación");
        return "detalleReserva";
    }
}