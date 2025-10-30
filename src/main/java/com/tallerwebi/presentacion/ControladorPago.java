package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pago")
public class ControladorPago {

    private final ServicioPago servicioPago;
    private final ServicioReserva servicioReserva;

    @Autowired
    public ControladorPago(ServicioPago servicioPago, ServicioReserva servicioReserva) {
        this.servicioPago = servicioPago;
        this.servicioReserva = servicioReserva;
    }

    // ✅ MercadoPago redirige acá cuando el pago fue exitoso
    @GetMapping("/exito")
    public String pagoExitoso(
            @RequestParam(name = "preference_id") String preferenceId,
            @RequestParam(name = "status", required = false) String status,
            RedirectAttributes redirectAttributes) {

        try {
            // Buscar el pago por su preferencia
            Pago pago = servicioPago.obtenerPorPreferencia(preferenceId);

            if (pago == null) {
                redirectAttributes.addFlashAttribute("mensajeError", "No se encontró el pago.");
                return "redirect:/home";
            }

            // Marcar el pago como aprobado
            pago.setEstado("Aprobado");
            servicioPago.actualizarPago(pago);

            // Activar la reserva asociada
            Reserva reserva = pago.getReserva();
            reserva.setActiva(true);
            servicioReserva.crearReserva(reserva); // guarda actualización

            redirectAttributes.addFlashAttribute("mensajeExito",
                    "✅ Pago confirmado y reserva activada con éxito.");
            return "redirect:/home";

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("mensajeError", "Error al confirmar el pago: " + e.getMessage());
            return "redirect:/home";
        }
    }

    // 🚧 En caso de fallo de pago
    @GetMapping("/error")
    public String pagoFallido(
            @RequestParam(name = "preference_id", required = false) String preferenceId,
            RedirectAttributes redirectAttributes) {

        if (preferenceId != null) {
            Pago pago = servicioPago.obtenerPorPreferencia(preferenceId);
            if (pago != null) {
                pago.setEstado("Fallido");
                servicioPago.actualizarPago(pago);
            }
        }

        redirectAttributes.addFlashAttribute("mensajeError", "❌ El pago fue cancelado o falló.");
        return "redirect:/home";
    }

    // 🕓 En caso de pago pendiente
    @GetMapping("/pendiente")
    public String pagoPendiente(
            @RequestParam(name = "preference_id", required = false) String preferenceId,
            RedirectAttributes redirectAttributes) {

        if (preferenceId != null) {
            Pago pago = servicioPago.obtenerPorPreferencia(preferenceId);
            if (pago != null) {
                pago.setEstado("Pendiente");
                servicioPago.actualizarPago(pago);
            }
        }

        redirectAttributes.addFlashAttribute("mensajeError", "⚠️ El pago está pendiente de confirmación.");
        return "redirect:/home";
    }
}
