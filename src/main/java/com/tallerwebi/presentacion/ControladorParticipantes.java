package com.tallerwebi.presentacion;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tallerwebi.dominio.Equipo;
import com.tallerwebi.dominio.ServicioEquipo;
import com.tallerwebi.dominio.ServicioEquipoJugador;
import com.tallerwebi.dominio.ServicioUsuario;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.EquipoNoEncontrado;
import com.tallerwebi.dominio.excepcion.ParticipanteNoEncontrado;
import com.tallerwebi.dominio.excepcion.PermisosInsufficientes;

@Controller
@RequestMapping("/participantes")
public class ControladorParticipantes {
    private ServicioEquipo servicioEquipo;
    private ServicioEquipoJugador servicioEquipoJugador;
    private ServicioUsuario servicioUsuario;

    @Autowired
    public ControladorParticipantes(ServicioEquipo servicioEquipo, ServicioEquipoJugador servicioEquipoJugador,
            ServicioUsuario servicioUsuario) {
        this.servicioEquipo = servicioEquipo;
        this.servicioEquipoJugador = servicioEquipoJugador;
        this.servicioUsuario = servicioUsuario;
    }

    @RequestMapping(path = "/{id}/asignacion-equipo", method = RequestMethod.POST)
    public String asignarEquipo(@PathVariable long id, @RequestParam("equipo") long equipoId,
            RedirectAttributes redirectAttributes, HttpServletRequest request)
            throws Exception {
        try {
            String email = (String) request.getSession().getAttribute("EMAIL");
            if (email == null) {
                return "redirect:/login";
            }

            Equipo equipo = servicioEquipo.buscarPorId(equipoId);
            servicioEquipoJugador.actualizarEquipo(id, equipo);

            redirectAttributes.addFlashAttribute("listaParticipantesSuccess", "Equipo asignado correctamente");

        } catch (EquipoNoEncontrado e) {
            System.out.println(e);
            redirectAttributes.addFlashAttribute("listaParticipantesError", e.getMessage());
        } catch (ParticipanteNoEncontrado e) {
            System.out.println(e);
            redirectAttributes.addFlashAttribute("listaParticipantesError", e.getMessage());
        } catch (Exception e) {
            System.out.println(e);
            redirectAttributes.addFlashAttribute("listaParticipantesError", "Error al asignar el equipo");
        }

        String referrer = request.getHeader("referer");
        if (referrer != null) {
            return "redirect:" + referrer;
        } else {
            return "redirect:/home";
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.POST)
    public String eliminarParticipante(@PathVariable long id, RedirectAttributes redirectAttributes,
            HttpServletRequest request) throws Exception {
        try {
            System.out.println("Eliminando participante con ID: " + id);
            String email = (String) request.getSession().getAttribute("EMAIL");
            if (email == null) {
                return "redirect:/login";
            }

            servicioEquipoJugador.eliminarPorId(id);

            redirectAttributes.addFlashAttribute("listaParticipantesSuccess", "Participante eliminado correctamente");

        } catch (ParticipanteNoEncontrado e) {
            System.out.println(e);
            redirectAttributes.addFlashAttribute("listaParticipantesError", e.getMessage());
        } catch (Exception e) {
            System.out.println(e);
            redirectAttributes.addFlashAttribute("listaParticipantesError", "Error al eliminar el participante");
        }

        String referrer = request.getHeader("referer");
        if (referrer != null) {
            return "redirect:" + referrer;
        } else {
            return "redirect:/home";
        }
    }

    @RequestMapping(path = "/{id}/promover-capitan", method = RequestMethod.POST)
    public String promoverCapitan(@PathVariable long id, RedirectAttributes redirectAttributes,
            HttpServletRequest request) {
        try {
            String email = (String) request.getSession().getAttribute("EMAIL");
            if (email == null) {
                return "redirect:/login";
            }

            Usuario usuario = servicioUsuario.buscarPorEmail(email);
            servicioEquipoJugador.promoverCapitan(id, usuario);
            redirectAttributes.addFlashAttribute("listaParticipantesSuccess", "Capitán promovido correctamente");

        } catch (ParticipanteNoEncontrado e) {
            redirectAttributes.addFlashAttribute("listaParticipantesError", e.getMessage());
        } catch (PermisosInsufficientes e) {
            redirectAttributes.addFlashAttribute("listaParticipantesError",
                    "No tienes permisos para promover al capitán");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("listaParticipantesError", "Error al promover al capitán");
        }

        String referrer = request.getHeader("referer");
        if (referrer != null) {
            return "redirect:" + referrer;
        } else {
            return "redirect:/home";
        }
    }
}
