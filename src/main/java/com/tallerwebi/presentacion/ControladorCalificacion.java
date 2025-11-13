package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;

import java.security.Principal;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/calificaciones")
public class ControladorCalificacion {

    private final ServicioCalificacion servicioCalificacion;
    private final ServicioPartido servicioPartido;
    private final ServicioUsuario servicioUsuario;

    @Autowired
    public ControladorCalificacion(ServicioCalificacion servicioCalificacion, ServicioPartido servicioPartido, ServicioUsuario servicioUsuario) {
        this.servicioCalificacion = servicioCalificacion;
        this.servicioPartido = servicioPartido;
        this.servicioUsuario = servicioUsuario;
    }

    @PostMapping("/partido/{partidoId}/jugador/{calificadoId}")
public String calificarJugador(
        @PathVariable Long partidoId,
        @PathVariable Long calificadoId,
        @RequestParam Integer puntuacion,
        @RequestParam(required = false) String comentario,
        HttpSession session,
        RedirectAttributes redirectAttributes) {

    Usuario calificador = (Usuario) session.getAttribute("USUARIO");
    if (calificador == null) {
        redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para calificar.");
        return "redirect:/login";
    }

    Partido partido = servicioPartido.obtenerPorId(partidoId);
    Usuario calificado = servicioUsuario.buscarPorId(calificadoId);

    try {
        servicioCalificacion.calificarJugador(calificador, calificado, partido, puntuacion, comentario);
        redirectAttributes.addFlashAttribute("success", "Jugador calificado correctamente.");
    } catch (RuntimeException e) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
    }

    return "redirect:/partidos/" + partidoId;
}
    
}