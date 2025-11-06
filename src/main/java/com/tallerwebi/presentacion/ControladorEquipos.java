package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioEquipo;
import com.tallerwebi.dominio.ServicioLogin;
import com.tallerwebi.dominio.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ControladorEquipos {

    private final ServicioEquipo servicioEquipo;
    private final ServicioLogin servicioLogin;

    @Autowired
    public ControladorEquipos(ServicioEquipo servicioEquipo, ServicioLogin servicioLogin) {
        this.servicioEquipo = servicioEquipo;
        this.servicioLogin = servicioLogin;
    }

    @GetMapping("/equipos/mis-equipos")
    public String misEquipos(HttpServletRequest request, ModelMap model) {
        String email = (String) request.getSession().getAttribute("EMAIL");
        if (email == null) {
            return "redirect:/login";
        }

        Usuario usuario = servicioLogin.buscarPorEmail(email);
        String busqueda = request.getParameter("busqueda");
        List<com.tallerwebi.dominio.Equipo> equipos;

        if (busqueda != null && !busqueda.trim().isEmpty()) {
            equipos = servicioEquipo.obtenerEquiposDelUsuarioConFiltro(usuario, busqueda);
        } else {
            equipos = servicioEquipo.obtenerEquiposDelUsuario(usuario);
        }

        model.put("equipos", equipos);
        model.put("busqueda", busqueda != null ? busqueda : "");
        model.put("currentPage", "mis-equipos");

        return "mis-equipos";
    }
}