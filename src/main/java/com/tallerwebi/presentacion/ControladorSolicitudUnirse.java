package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioLogin;
import com.tallerwebi.dominio.ServicioSolicitudUnirse;
import com.tallerwebi.dominio.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/solicitudes-partido")
public class ControladorSolicitudUnirse {

    private final ServicioSolicitudUnirse servicio;
    private final ServicioLogin servicioLogin;

    public ControladorSolicitudUnirse(ServicioSolicitudUnirse servicio, ServicioLogin servicioLogin) {
        this.servicio = servicio;
        this.servicioLogin = servicioLogin;
    }

    @PostMapping("/crear")
    public String crearInvitacion(@RequestParam Long partidoId,
                                  @RequestParam String emailDestino,
                                  HttpServletRequest request,
                                  RedirectAttributes ra) {
        String email = (String) request.getSession().getAttribute("EMAIL");
        if (email == null) return "redirect:/login";
        Usuario yo = servicioLogin.buscarPorEmail(email);

        String link = servicio.crearInvitacion(partidoId, yo, emailDestino);
        if (link == null) {
            ra.addFlashAttribute("mensajeError", "No se pudo generar la invitación.");
        } else {
            // Enviar por email = tarea aparte. Por ahora mostramos para copiar:
            ra.addFlashAttribute("mensajeExito", "Invitación creada. Copiá y compartí este link: " + link);
        }
        return "redirect:/partidos/" + partidoId;
    }

    // El amigo abre este link ya logueado y se une
    @GetMapping("/{token}")
    public String aceptar(@PathVariable String token,
                          HttpServletRequest request,
                          RedirectAttributes ra) {
        String email = (String) request.getSession().getAttribute("EMAIL");
        if (email == null) return "redirect:/login";
        Usuario yo = servicioLogin.buscarPorEmail(email);

        var res = servicio.aceptarPorToken(token, yo);
        if (res.ok) {
            ra.addFlashAttribute("mensajeExito", res.mensaje);
        } else {
            ra.addFlashAttribute("mensajeError", res.mensaje);
        }
        // no sabemos el partidoId acá sin consultar, pero la service ya resolvió. Podrías
        // redirigir a /home como fallback:
        return "redirect:/home";
    }

    // (Opcional) listar invitaciones en un partido del creador para mostrarlas
    @GetMapping("/partido/{partidoId}")
    public String listar(@PathVariable Long partidoId, ModelMap model) {
        model.put("solicitudes", servicio.listarPorPartido(partidoId));
        return "solicitudes-partido"; // si querés una vista aparte
    }
}
