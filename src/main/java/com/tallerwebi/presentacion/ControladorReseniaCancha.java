package com.tallerwebi.presentacion;

import javax.servlet.http.HttpServletRequest;

import org.apache.maven.model.Model;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tallerwebi.dominio.Cancha;
import com.tallerwebi.dominio.ReseniaCancha;
import com.tallerwebi.dominio.ServicioCancha;
import com.tallerwebi.dominio.ServicioReseniaCancha;
import com.tallerwebi.dominio.ServicioUsuario;
import com.tallerwebi.dominio.Usuario;

@Controller
public class ControladorReseniaCancha {
    private final ServicioReseniaCancha servicioReseniaCancha;
    private final ServicioCancha servicioCancha;
    private final ServicioUsuario servicioUsuario;
    public ControladorReseniaCancha(ServicioReseniaCancha servicioReseniaCancha,ServicioCancha servicioCancha,ServicioUsuario servicioUsuario) {
        this.servicioReseniaCancha = servicioReseniaCancha;
        this.servicioCancha=servicioCancha;
        this.servicioUsuario=servicioUsuario;
    }

    @GetMapping("/reseniar-cancha/{canchaId}")

    public ModelAndView mostrarFormularioResenia(@PathVariable Long canchaId, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView("reseniar-cancha");    
        try {
            String email = (String) request.getSession().getAttribute("EMAIL");
            if (email == null) {
                return new ModelAndView("redirect:/login");
            }
        Cancha cancha=servicioCancha.obtenerCanchaPorId(canchaId);  
        if (cancha==null){
            modelAndView.setViewName("canchas");
            modelAndView.addObject("error", "La cancha que quiere reseñar no existe");
            return modelAndView;
        }
        Usuario usuario = this.servicioUsuario.buscarPorEmail(email);
        Boolean puedeReseniar =this.servicioReseniaCancha.verificarSiElUsuarioPuedeReseniarEsaCancha(usuario, cancha);
            if (puedeReseniar == false) {
                String mensaje = "ya reseño esta cancha";
                redirectAttributes.addFlashAttribute("error", mensaje);
                return new ModelAndView("redirect:/cancha/" + canchaId);
        }  
        modelAndView.addObject("cancha", cancha);
        modelAndView.addObject("usuario", usuario);
            } catch (Exception e) {
                e.printStackTrace();
            }
        return modelAndView;
}

    @PostMapping("/resenia/guardar")
        public ModelAndView guardarResenia(
        @RequestParam("canchaId") Long canchaId,
        @RequestParam("calificacion") Integer calificacion,
        @RequestParam(value = "descripcion", required = false) String descripcion,
        ModelMap model,
        HttpServletRequest request) {

        String email = (String) request.getSession().getAttribute("EMAIL");
        Usuario usuario = servicioUsuario.buscarPorEmail(email);
        Cancha cancha = servicioCancha.obtenerCanchaPorId(canchaId);
        servicioReseniaCancha.agregarReseniaCancha(
            new ReseniaCancha(calificacion, descripcion, usuario, cancha));

        return new ModelAndView("redirect:/cancha/" + canchaId);
}   
    
    
    
    
    }


    

