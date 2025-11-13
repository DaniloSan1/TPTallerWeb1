package com.tallerwebi.presentacion;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tallerwebi.dominio.Calificacion;
import com.tallerwebi.dominio.Cancha;
import com.tallerwebi.dominio.ReseniaCancha;
import com.tallerwebi.dominio.ServicioCalificacion;
import com.tallerwebi.dominio.ServicioCancha;
import com.tallerwebi.dominio.ServicioReseniaCancha;
import com.tallerwebi.dominio.ServicioUsuario;
import com.tallerwebi.dominio.Usuario;

@Controller
public class ControladorReseniaCancha {
    private final ServicioReseniaCancha servicioReseniaCancha;
    private final ServicioCancha servicioCancha;
    private final ServicioUsuario servicioUsuario;
    private final ServicioCalificacion servicioCalificacion;
    public ControladorReseniaCancha(ServicioReseniaCancha servicioReseniaCancha,ServicioCancha servicioCancha,ServicioUsuario servicioUsuario,ServicioCalificacion servicioCalificacion) {
        this.servicioReseniaCancha = servicioReseniaCancha;
        this.servicioCancha=servicioCancha;
        this.servicioUsuario=servicioUsuario;
        this.servicioCalificacion=servicioCalificacion;
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
        modelAndView.addObject("editMode", false);
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
        HttpServletRequest request, RedirectAttributes redirectAttributes) {

        String email = (String) request.getSession().getAttribute("EMAIL");
        Usuario usuario = servicioUsuario.buscarPorEmail(email);
        Cancha cancha = servicioCancha.obtenerCanchaPorId(canchaId);
        servicioReseniaCancha.agregarReseniaCancha(
            new ReseniaCancha(calificacion, descripcion, usuario, cancha));

        redirectAttributes.addFlashAttribute("success", "Reseña creada correctamente");
        return new ModelAndView("redirect:/cancha/" + canchaId);
        }   
    
    @GetMapping("/historial-reviews/{usuarioId}")
        public ModelAndView verHistorial(@PathVariable Long usuarioId, HttpServletRequest request){
        ModelAndView modelAndView= new ModelAndView();
        Usuario usuario=servicioUsuario.buscarPorEmail((String)request.getSession().getAttribute("EMAIL"));
        if(usuarioId !=usuario.getId()){
            return new ModelAndView("redirect:/perfil/");
        }
        List<ReseniaCancha> resenias =servicioReseniaCancha.obtenerReseniasPorUsuario(usuarioId);
        List<Calificacion> calificaciones=servicioCalificacion.obtenerCalificacionesPorCalificador(usuarioId);
        modelAndView.addObject("resenias", resenias);
        modelAndView.addObject("calificaciones", calificaciones);
        modelAndView.setViewName("historial-resenias");
        return modelAndView;
    }

    @GetMapping("/editar-resenia/{reseniaCanchaId}")
    public ModelAndView editarResenia(@PathVariable Long reseniaCanchaId, HttpServletRequest request, RedirectAttributes redirectAttributes){
        ModelAndView modelAndView = new ModelAndView();
        String email = (String) request.getSession().getAttribute("EMAIL");
        if (email == null) {
            return new ModelAndView("redirect:/login");
        }

        try {
            ReseniaCancha reseniaAEditar = servicioReseniaCancha.obtenerReseniaCanchaPorId(reseniaCanchaId);
            Usuario usuarioActual = servicioUsuario.buscarPorEmail(email);

            if (usuarioActual == null) {
                return new ModelAndView("redirect:/login");
            }

            
            if (!reseniaAEditar.getUsuario().getId().equals(usuarioActual.getId())) {
                redirectAttributes.addFlashAttribute("error", "No tenés permiso para editar esta reseña.");
                return new ModelAndView("redirect:/perfil");
            }
            modelAndView.setViewName("reseniar-cancha");
            modelAndView.addObject("cancha", reseniaAEditar.getCancha());
            modelAndView.addObject("usuario", usuarioActual);
            modelAndView.addObject("resenia", reseniaAEditar);
            modelAndView.addObject("editMode", true);
            return modelAndView;

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return new ModelAndView("redirect:/perfil");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ocurrió un error al intentar editar la reseña.");
            return new ModelAndView("redirect:/perfil");
        }
    }

    @PostMapping("/resenia/editar")
    public ModelAndView actualizarResenia(
        @RequestParam("reseniaId") Long reseniaId,
        @RequestParam("calificacion") Integer calificacion,
        @RequestParam(value = "descripcion", required = false) String descripcion,
        HttpServletRequest request,RedirectAttributes redirectAttributes) {

        String email = (String) request.getSession().getAttribute("EMAIL");
        Usuario usuario = servicioUsuario.buscarPorEmail(email);

        try {
            ReseniaCancha reseniaAEditar = servicioReseniaCancha.obtenerReseniaCanchaPorId(reseniaId);
            if (!reseniaAEditar.getUsuario().getId().equals(usuario.getId())) {
                return new ModelAndView("redirect:/perfil");
            }
            reseniaAEditar.setCalificacion(calificacion);
            reseniaAEditar.setComentario(descripcion);
            servicioReseniaCancha.editarReseniaCancha(reseniaAEditar);
            redirectAttributes.addFlashAttribute("success", "Reseña editada correctamente");
            return new ModelAndView("redirect:/cancha/" + reseniaAEditar.getCancha().getId());
            
        } catch (IllegalArgumentException e) {
            return new ModelAndView("redirect:/perfil");
        } catch (Exception e) {
            e.printStackTrace();
            return new ModelAndView("redirect:/perfil");
        }
    }
    
    
    
    
    }