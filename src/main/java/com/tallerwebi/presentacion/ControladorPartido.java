package com.tallerwebi.presentacion;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tallerwebi.dominio.Equipo;
import com.tallerwebi.dominio.Partido;
import com.tallerwebi.dominio.ServicioEquipo;
import com.tallerwebi.dominio.ServicioHorario;
import com.tallerwebi.dominio.ServicioLogin;
import com.tallerwebi.dominio.ServicioPartido;
import com.tallerwebi.dominio.ServicioReserva;
import com.tallerwebi.dominio.ServicioUsuario;
import com.tallerwebi.dominio.Usuario;

@Controller
@RequestMapping("/partidos")
public class ControladorPartido {
    private ServicioPartido servicio;
    private ServicioLogin servicioLogin;
    private ServicioPartido servicioPartido;
    private ServicioReserva servicioReserva;
    private ServicioUsuario servicioUsuario;
    private ServicioHorario servicioHorario;
    private ServicioEquipo servicioEquipo;

    @Autowired
    public ControladorPartido(ServicioPartido servicio, ServicioLogin servicioLogin, ServicioHorario servicioHorario,
            ServicioReserva servicioReserva, ServicioPartido servicioPartido, ServicioUsuario servicioUsuario,
            ServicioEquipo servicioEquipo) {
        this.servicio = servicio;
        this.servicioLogin = servicioLogin;
        this.servicioReserva = servicioReserva;
        this.servicioPartido = servicioPartido;
        this.servicioUsuario = servicioUsuario;
        this.servicioEquipo = servicioEquipo;
    }

    @GetMapping("/{id}")
    public ModelAndView detalle(@PathVariable long id, HttpServletRequest request) {
        ModelMap modelo = new ModelMap();
        try {
            String email = (String) request.getSession().getAttribute("EMAIL");
            if (email == null) {
                return new ModelAndView("redirect:/login");
            }

            Usuario usuario = servicioLogin.buscarPorEmail(request.getSession().getAttribute("EMAIL").toString());
            Partido partido = servicio.obtenerPorId(id);
            modelo.put("partido", new DetallePartido(partido, usuario));

        } catch (Exception e) {
            modelo.put("error", e.getMessage());
        }

        return new ModelAndView("detalle-partido", modelo);
    }

    @GetMapping("/{id}/editar-partido")
    public ModelAndView editar(@PathVariable long id, HttpServletRequest request) {
        ModelMap modelo = new ModelMap();
        try {
            String email = (String) request.getSession().getAttribute("EMAIL");
            if (email == null) {
                return new ModelAndView("redirect:/login");
            }

            Partido partido = servicio.obtenerPorId(id);

            if (!partido.esCreador(email)) {
                modelo.put("error", "No tienes permiso para editar este partido.");
                return new ModelAndView("redirect:/partidos/" + id);
            }

            modelo.put("partido", partido);
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
        }

        return new ModelAndView("editar-partido", modelo);
    }

    @PostMapping("/{id}/editar")
    public String update(@PathVariable long id, @RequestParam String titulo, @RequestParam String descripcion,
            HttpServletRequest request) {
        try {
            String email = (String) request.getSession().getAttribute("EMAIL");
            if (email == null) {
                return "redirect:/login";
            }

            Usuario usuario = servicioLogin.buscarPorEmail(email);
            servicio.actualizarPartido(id, titulo, descripcion, usuario);
        } catch (Exception e) {
            // For now, ignore, or could add flash attribute
        }
        return "redirect:/partidos/" + id;
    }

    @PostMapping("/{id}/join")
    public ModelAndView inscripcion(@PathVariable long id, @RequestParam("equipo") long equipoId,
            HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception {
        try {
            String email = (String) request.getSession().getAttribute("EMAIL");
            if (email == null) {
                return new ModelAndView("redirect:/login");
            }

            Usuario usuario = servicioLogin.buscarPorEmail(request.getSession().getAttribute("EMAIL").toString());
            Equipo equipo = servicioEquipo.buscarPorId(equipoId);
            Partido partido = servicio.obtenerPorId(id);
            partido = servicio.anotarParticipante(partido, equipo, usuario);

            redirectAttributes.addFlashAttribute("success", "Equipo asignado correctamente");
        } catch (Exception e) {
            System.out.println(e);
            redirectAttributes.addFlashAttribute("error", "Error al asignar el equipo");
        }

        String referrer = request.getHeader("referer");
        if (referrer != null) {
            return new ModelAndView("redirect:" + referrer);
        } else {
            return new ModelAndView("redirect:/home");
        }
    }

    @PostMapping("/{id}/leave")
    public String abandonarPartido(@PathVariable Long id, HttpServletRequest request) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("USUARIO");
        if (usuario == null) {
            return "redirect:/login";
        }

        servicio.abandonarPartido(id, usuario);
        return "redirect:/home";
    }
}
