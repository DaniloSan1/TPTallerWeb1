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
                                  @RequestParam(required = false) Long amigoId,
                                  @RequestParam(required = false) String emailDestino,
                                  HttpServletRequest request,
                                  RedirectAttributes ra) {
        String email = (String) request.getSession().getAttribute("EMAIL");
        if (email == null) return "redirect:/login";
        Usuario yo;

        try {
            yo = servicioLogin.buscarPorEmail(email);
            String destino = emailDestino;

            if (destino == null && amigoId != null) {
                // Validar que sean amigos
                boolean esAmigo = servicioAmistad.verAmigos(yo.getId()).stream().anyMatch(a ->
                        (a.getUsuario1() != null && a.getUsuario1().getId().equals(amigoId)) ||
                                (a.getUsuario2() != null && a.getUsuario2().getId().equals(amigoId))
                );
                if (!esAmigo) {
                    ra.addFlashAttribute("mensajeError", "Solo puedes invitar a tus amigos a un partido.");
                    return "redirect:/partidos/" + partidoId;
                }

                Usuario amigo = servicioLogin.buscarPorId(amigoId);
                if (amigo == null) {
                    ra.addFlashAttribute("mensajeError", "No se encontró el usuario a invitar.");
                    return "redirect:/partidos/" + partidoId;
                }
                destino = amigo.getEmail();
            }

            if (destino == null) {
                ra.addFlashAttribute("mensajeError", "Debe indicar un amigo o un email destino.");
                return "redirect:/partidos/" + partidoId;
            }

            String link = servicio.crearInvitacion(partidoId, yo, destino);
            if (link == null) {
                ra.addFlashAttribute("mensajeError", "No se pudo generar la invitación.");
            } else {
                ra.addFlashAttribute("mensajeExito", "Invitación creada. Copiá y compartí este link: " + link);
            }

        } catch (RuntimeException ex) {
            ra.addFlashAttribute("mensajeError", ex.getMessage() != null ? ex.getMessage() : "Error al crear la invitación.");
        } catch (Exception e) {
            ra.addFlashAttribute("mensajeError", "Error al crear la invitación");
        }

        return "redirect:/partidos/" + partidoId;
    }

    // El amigo abre este link ya logueado y se une
    @GetMapping("/{token}")
    public String aceptar(@PathVariable String token,
            HttpServletRequest request,
            RedirectAttributes ra) {
        try {
            String email = (String) request.getSession().getAttribute("EMAIL");
            if (email == null)
                return "redirect:/login";
            Usuario yo = servicioLogin.buscarPorEmail(email);

            var res = servicio.aceptarPorToken(token, yo);
            if (res.ok) {
                ra.addFlashAttribute("mensajeExito", res.mensaje);
            } else {
                ra.addFlashAttribute("mensajeError", res.mensaje);
            }
        } catch (Exception e) {
            ra.addFlashAttribute("mensajeError", "Error al aceptar la invitación");
        }
        // no sabemos el partidoId acá sin consultar, pero la service ya resolvió.
        // Podrías
        // redirigir a /home como fallback:
        return "redirect:/home";
    }

    // (Opcional) listar invitaciones en un partido del creador para mostrarlas
    @GetMapping("/partido/{partidoId}")
    public String listar(@PathVariable Long partidoId, ModelMap model) {
        // Proveer a la vista la lista de invitaciones pendientes para este partido
        model.addAttribute("invitacionesPartidoPendientes", servicio.listarPorPartido(partidoId));
        // Reusar la plantilla de solicitudes (amistad + invitaciones)
        return "solicitudes-amistad";
    }

}

