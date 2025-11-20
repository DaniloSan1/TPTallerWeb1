package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;

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

    @GetMapping("/editar-calificacion/{calificacionId}")
    public ModelAndView editarCalificacion(@PathVariable Long calificacionId, HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("USUARIO");
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para editar calificaciones.");
            return new ModelAndView("redirect:/login");
        }

        Calificacion calificacion = servicioCalificacion.obtenerPorId(calificacionId);
        if (calificacion == null || !calificacion.getCalificador().getId().equals(usuario.getId())) {
            redirectAttributes.addFlashAttribute("error", "No tienes permiso para editar esta calificación.");
            return new ModelAndView("redirect:/partidos");
        }

        ModelAndView mav = new ModelAndView("editar-calificacion");
        mav.addObject("calificacion", calificacion);
        mav.addObject("editMode", true);
        return mav;
    }

    @PostMapping("/guardar")
    public String guardarCalificacion(
            @RequestParam Long usuarioACalificarId,
            @RequestParam Integer puntuacion,
            @RequestParam(required = false) String comentario,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario calificador = (Usuario) session.getAttribute("USUARIO");
        if (calificador == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para calificar.");
            return "redirect:/login";
        }

        Usuario usuarioACalificar = servicioUsuario.buscarPorId(usuarioACalificarId);
        if (usuarioACalificar == null) {
            redirectAttributes.addFlashAttribute("error", "El usuario a calificar no existe.");
            return "redirect:/perfil";
        }

        try {
            // Crear y guardar una calificación sin partido
            servicioCalificacion.guardarCalificacionSinPartido(calificador, usuarioACalificar, puntuacion, comentario);
            redirectAttributes.addFlashAttribute("success", "Calificación guardada correctamente.");
            return "redirect:/perfil/ver/username/" + usuarioACalificar.getUsername();
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/perfil";
        }
    }

    @PostMapping("/editar")
    public String actualizarCalificacion(
            @RequestParam Long calificacionId,
            @RequestParam Integer puntuacion,
            @RequestParam(required = false) String comentario,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = (Usuario) session.getAttribute("USUARIO");
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para editar calificaciones.");
            return "redirect:/login";
        }

        Calificacion calificacion = servicioCalificacion.obtenerPorId(calificacionId);
        if (calificacion == null || !calificacion.getCalificador().getId().equals(usuario.getId())) {
            redirectAttributes.addFlashAttribute("error", "No tienes permiso para editar esta calificación.");
            return "redirect:/partidos";
        }

        try {
            calificacion.setPuntuacion(puntuacion);
            calificacion.setComentario(comentario);
            servicioCalificacion.actualizarCalificacion(calificacion);
            redirectAttributes.addFlashAttribute("success", "Calificación actualizada correctamente.");
            return "redirect:/historial-reviews/" + usuario.getId();
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/partidos";
        }
    }
    
}