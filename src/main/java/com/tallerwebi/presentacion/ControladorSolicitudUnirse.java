package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioLogin;
import com.tallerwebi.dominio.ServicioSolicitudUnirse;
import com.tallerwebi.dominio.ServicioAmistad;
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
    private final ServicioAmistad servicioAmistad;

    public ControladorSolicitudUnirse(ServicioSolicitudUnirse servicio, ServicioLogin servicioLogin, ServicioAmistad servicioAmistad) {
        this.servicio = servicio;
        this.servicioLogin = servicioLogin;
        this.servicioAmistad = servicioAmistad;
    }

    @PostMapping("/crear")
    public String crearInvitacion(@RequestParam Long partidoId,
                                  @RequestParam Long amigoId,
                                  HttpServletRequest request,
                                  RedirectAttributes ra) {
        String email = (String) request.getSession().getAttribute("EMAIL");
        if (email == null) return "redirect:/login";
        Usuario yo = servicioLogin.buscarPorEmail(email);
        // Validar que sean amigos
        boolean esAmigo = servicioAmistad.verAmigos(yo.getId()).stream().anyMatch(a ->
            (a.getUsuario1().getId().equals(amigoId) || a.getUsuario2().getId().equals(amigoId))
        );
        if (!esAmigo) {
            ra.addFlashAttribute("mensajeError", "Solo puedes invitar a tus amigos a un partido.");
            return "redirect:/partidos/" + partidoId;
        }
        // Buscar email del amigo
        Usuario amigo = servicioLogin.buscarPorId(amigoId);
        if (amigo == null) {
            ra.addFlashAttribute("mensajeError", "No se encontró el usuario a invitar.");
            return "redirect:/partidos/" + partidoId;
        }
        try {
            String link = servicio.crearInvitacion(partidoId, yo, amigo.getEmail());
            if (link == null) {
                ra.addFlashAttribute("mensajeError", "No se pudo generar la invitación.");
            } else {
                ra.addFlashAttribute("mensajeExito", "Invitación creada. Copiá y compartí este link: " + link);
            }
        } catch (RuntimeException ex) {
            // Capturamos errores del servicio (por ejemplo: usuario no es creador/participante)
            ra.addFlashAttribute("mensajeError", ex.getMessage() != null ? ex.getMessage() : "Error al crear la invitación.");
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
