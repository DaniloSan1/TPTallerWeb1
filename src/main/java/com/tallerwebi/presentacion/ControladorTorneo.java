package com.tallerwebi.presentacion;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
@RequestMapping("/torneos")
@PropertySource("classpath:application.properties")
public class ControladorTorneo {

    private final ServicioTorneo servicioTorneo;
    private final ServicioCancha servicioCancha;
    private final ServicioLogin servicioLogin;
    private final ServicioPago servicioPago;

    @Value("${mercadopago.public.key}")
    private String publicKey;

    @Autowired
    public ControladorTorneo(ServicioTorneo servicioTorneo,
                             ServicioCancha servicioCancha,
                             ServicioLogin servicioLogin,
                             ServicioPago servicioPago) {
        this.servicioTorneo = servicioTorneo;
        this.servicioCancha = servicioCancha;
        this.servicioLogin = servicioLogin;
        this.servicioPago = servicioPago;
    }

    // Mostrar formulario de creación de torneo
    @GetMapping("/crear/{canchaId}")
    public ModelAndView mostrarFormulario(@PathVariable Long canchaId) {
        ModelAndView mav = new ModelAndView("formulario-torneo");
        Cancha cancha = servicioCancha.obtenerCanchaPorId(canchaId);
        mav.addObject("torneo", new Torneo());
        mav.addObject("cancha", cancha);
        return mav;
    }

    // Crear torneo con pago pendiente
    @PostMapping("/crear")
    public ModelAndView crearTorneo(@RequestParam Long canchaId,
                                    @RequestParam String nombre,
                                    @RequestParam String fecha,
                                    HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        try {
            String email = (String) request.getSession().getAttribute("EMAIL");
            if (email == null) {
                return new ModelAndView("redirect:/login");
            }

            Usuario organizador = servicioLogin.buscarPorEmail(email);
            Cancha cancha = servicioCancha.obtenerCanchaPorId(canchaId);

            LocalDate fechaTorneo = LocalDate.parse(fecha);

            Torneo torneo = new Torneo();
            torneo.setNombre(nombre);
            torneo.setFecha(fechaTorneo);
            torneo.setCancha(cancha);
            torneo.setOrganizador(organizador);

            // Crear torneo en servicio (validaciones de disponibilidad y precio)
            Torneo torneoCreado = servicioTorneo.crearTorneo(torneo);

            // Generar preferencia de pago en MercadoPago
            BigDecimal precioTotal = torneoCreado.getPrecio();
            String preferenceId = servicioPago.crearPago(
                    "Torneo en " + cancha.getNombre(),
                    "Pago de inscripción al torneo",
                    precioTotal,
                    torneoCreado.getId()
            );

            // Guardar pago en estado pendiente
            servicioPago.guardarPagoTorneo(torneoCreado, organizador, preferenceId, precioTotal.doubleValue());

            mav.setViewName("detalleTorneo");
            mav.addObject("torneo", torneoCreado);
            mav.addObject("cancha", cancha);
            mav.addObject("publicKey", publicKey);
            mav.addObject("preferenceId", preferenceId);
            mav.addObject("mensajeExito", "Torneo creado. Completá el pago para confirmarlo.");

        } catch (Exception e) {
            e.printStackTrace();
            mav.setViewName("formulario-torneo");
            mav.addObject("mensajeError", "Error al crear el torneo: " + e.getMessage());
            Cancha cancha = servicioCancha.obtenerCanchaPorId(canchaId);
            mav.addObject("cancha", cancha);
        }
        return mav;
    }

    // Listar torneos disponibles
    @GetMapping("/listar")
    public ModelAndView listarTorneos() {
        List<Torneo> torneos = servicioTorneo.listarTorneosDisponibles();
        ModelAndView mav = new ModelAndView("torneos-disponibles");
        mav.addObject("torneos", torneos);
        return mav;
    }

    // Ver detalle de torneo
    @GetMapping("/{id}")
    public String verDetalleTorneo(@PathVariable Long id, ModelMap model) {
        Torneo torneo = servicioTorneo.obtenerPorId(id);
        model.put("torneo", torneo);
        model.put("cancha", torneo.getCancha());
        return "detalleTorneo";
    }

    // Cancelar torneo
    @PostMapping("/cancelar/{id}")
    public String cancelarTorneo(@PathVariable Long id,
                                 RedirectAttributes redirectAttributes) {
        try {
            servicioTorneo.cancelarTorneo(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Torneo cancelado correctamente.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        }
        return "redirect:/home";
    }
}