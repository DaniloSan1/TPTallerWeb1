package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Equipo;
import com.tallerwebi.dominio.EquipoJugador;
import com.tallerwebi.dominio.ServicioEquipo;
import com.tallerwebi.dominio.ServicioEquipoJugador;
import com.tallerwebi.dominio.ServicioLogin;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.EquipoNoEncontrado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class ControladorEquipos {

    private final ServicioEquipo servicioEquipo;
    private final ServicioLogin servicioLogin;
    private final ServicioEquipoJugador servicioEquipoJugador;

    @Autowired
    public ControladorEquipos(ServicioEquipo servicioEquipo, ServicioLogin servicioLogin,
            ServicioEquipoJugador servicioEquipoJugador) {
        this.servicioEquipo = servicioEquipo;
        this.servicioLogin = servicioLogin;
        this.servicioEquipoJugador = servicioEquipoJugador;
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

    @GetMapping("/equipos/{id}")
    public String detalleEquipo(@PathVariable Long id, HttpServletRequest request, ModelMap model) {
        String email = (String) request.getSession().getAttribute("EMAIL");
        if (email == null) {
            return "redirect:/login";
        }

        try {
            Equipo equipo = servicioEquipo.buscarPorId(id);
            List<EquipoJugador> jugadores = servicioEquipoJugador.buscarPorEquipo(equipo);
            model.put("equipo", equipo);
            model.put("jugadores", jugadores.stream().map(DetalleParticipante::new).collect(Collectors.toList()));
            model.put("currentPage", "mis-equipos");
            return "equipo";
        } catch (EquipoNoEncontrado e) {
            model.put("error", e.getMessage());
            return "redirect:/equipos/mis-equipos";
        }
    }
}