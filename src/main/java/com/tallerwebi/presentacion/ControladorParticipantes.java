package com.tallerwebi.presentacion;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tallerwebi.dominio.ServicioEquipo;
import com.tallerwebi.dominio.ServicioEquipoJugador;
import com.tallerwebi.dominio.ServicioPartidoParticipante;
import com.tallerwebi.dominio.excepcion.ParticipanteNoEncontrado;

@Controller
@RequestMapping("/participantes")
public class ControladorParticipantes {
    private ServicioPartidoParticipante servicioPartidoParticipante;
    private ServicioEquipoJugador servicioEquipoJugador;

    @Autowired
    public ControladorParticipantes(ServicioPartidoParticipante servicioPartidoParticipante,
            ServicioEquipoJugador servicioEquipoJugador) {

        this.servicioPartidoParticipante = servicioPartidoParticipante;
        this.servicioEquipoJugador = servicioEquipoJugador;
    }

    @RequestMapping(path = "/{id}/asignacion-equipo", method = RequestMethod.POST)
    public String asignarEquipo(@PathVariable long id, @RequestParam("equipo") String equipo,
            RedirectAttributes redirectAttributes, HttpServletRequest request)
            throws Exception {
        try {
            String email = (String) request.getSession().getAttribute("EMAIL");
            if (email == null) {
                return "redirect:/login";
            }

            servicioPartidoParticipante.actualizarEquipo(id, equipo);

            redirectAttributes.addFlashAttribute("listaParticipantesSuccess", "Equipo asignado correctamente");

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
}
