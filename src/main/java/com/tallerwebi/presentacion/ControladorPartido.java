package com.tallerwebi.presentacion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
import com.tallerwebi.dominio.EquipoJugador;
import com.tallerwebi.dominio.Gol;
import com.tallerwebi.dominio.Partido;
import com.tallerwebi.dominio.PartidoEquipo;
import com.tallerwebi.dominio.ServicioEquipo;
import com.tallerwebi.dominio.FotoCancha;
import com.tallerwebi.dominio.Horario;
import com.tallerwebi.dominio.Nivel;
import com.tallerwebi.dominio.Partido;
import com.tallerwebi.dominio.Reserva;
import com.tallerwebi.dominio.ServicioFotoCancha;
import com.tallerwebi.dominio.ServicioEquipoJugador;
import com.tallerwebi.dominio.ServicioGoles;
import com.tallerwebi.dominio.ServicioHorario;
import com.tallerwebi.dominio.ServicioLogin;
import com.tallerwebi.dominio.ServicioPartido;
import com.tallerwebi.dominio.ServicioReserva;
import com.tallerwebi.presentacion.EquipoSimple;
import com.tallerwebi.dominio.ServicioUsuario;
import com.tallerwebi.dominio.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;

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
    private ServicioFotoCancha servicioFotoCancha;
    private ServicioGoles servicioGoles;
    private ServicioEquipoJugador servicioEquipoJugador;


    @Autowired
    public ControladorPartido(ServicioPartido servicio, ServicioLogin servicioLogin, ServicioHorario servicioHorario,
            ServicioReserva servicioReserva, ServicioPartido servicioPartido, ServicioUsuario servicioUsuario,
            ServicioEquipo servicioEquipo, ServicioGoles servicioGoles, ServicioEquipoJugador servicioEquipoJugador) {
        this.servicio = servicio;
        this.servicioLogin = servicioLogin;
        this.servicioHorario = servicioHorario;
        this.servicioReserva = servicioReserva;
        this.servicioPartido = servicioPartido;
        this.servicioUsuario = servicioUsuario;
        this.servicioEquipo = servicioEquipo;
        this.servicioFotoCancha = servicioFotoCancha;
        this.servicioGoles = servicioGoles;
        this.servicioEquipoJugador = servicioEquipoJugador;
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

            redirectAttributes.addFlashAttribute("success", "Te has unido al partido correctamente.");
        } catch (Exception e) {
            System.out.println(e);
            redirectAttributes.addFlashAttribute("error", "Ocurrió un error al intentar inscribirte.");
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
    @GetMapping("/mios")
    public ModelAndView misPartidos(HttpServletRequest request, ModelMap model) {
    String email = (String) request.getSession().getAttribute("EMAIL");
    if (email == null) {
        return new ModelAndView("redirect:/login");
    }
    Usuario usuario = servicioLogin.buscarPorEmail(email);
    List<Partido> partidos = servicio.listarPorCreador(usuario);

    List<FotoCancha> fotosCanchas = servicioFotoCancha.insertarFotosAModelPartidos(partidos);

    model.put("partidos", partidos);
    model.put("fotosCanchas", fotosCanchas);
    model.put("currentPage", "mis-partidos");


    return new ModelAndView("mis-partidos", model);
    }
    @GetMapping("/{id}/finalizar-partido")
    public ModelAndView finalizarPartido(@PathVariable long id, HttpServletRequest request) {
        ModelMap modelo = new ModelMap();
        try {
            String email = (String) request.getSession().getAttribute("EMAIL");
            if (email == null) {
                return new ModelAndView("redirect:/login");
            }
            Usuario usuario = servicioLogin.buscarPorEmail(email);
            Partido partido = servicio.obtenerPorId(id);
            if (!partido.esCreador(email)) {
                modelo.put("error", "No tienes permiso.");
                return new ModelAndView("redirect:/partidos/" + id);
            }

            modelo.put("partido", new DetallePartido(partido, usuario));

            Set<PartidoEquipo> partidoEquipos = partido.getEquipos();
            List<EquipoSimple> equiposSimple = new ArrayList<>();
            for (PartidoEquipo pe : partidoEquipos) {
                equiposSimple.add(new EquipoSimple(pe));
            }
            modelo.put("equipos", equiposSimple);

            // Preload players for all teams
            // TODO: Mejorar agregando mapper.
            Map<Long, List<Map<String, Object>>> equipoJugadores = new HashMap<>();
            for (PartidoEquipo partidoEquipo : partidoEquipos) {
                List<EquipoJugador> jugadoresPorqEquipos = servicioEquipoJugador
                        .buscarPorEquipo(partidoEquipo.getEquipo());
                List<Map<String, Object>> jugadores = new ArrayList<>();
                for (EquipoJugador ej : jugadoresPorqEquipos) {
                    Map<String, Object> player = new HashMap<>();
                    player.put("id", ej.getId());
                    player.put("nombre", ej.getUsuario().getNombre() + " " + ej.getUsuario().getApellido());
                    jugadores.add(player);
                }
                equipoJugadores.put(partidoEquipo.getEquipo().getId(), jugadores);
            }
            modelo.put("equipoJugadores", equipoJugadores);
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
        }
        return new ModelAndView("finalizar-partido", modelo);
    }

    @PostMapping("/{id}/finalizar-partido")
    public String procesarFinalizarPartido(@PathVariable long id,
            @RequestParam List<Long> equipoJugadorIds,
            @RequestParam List<Integer> cantidades,
            HttpServletRequest request, RedirectAttributes redirectAttributes) {
        try {
            String email = (String) request.getSession().getAttribute("EMAIL");
            if (email == null) {
                return "redirect:/login";
            }
            Usuario usuario = servicioLogin.buscarPorEmail(email);
            Partido partido = servicio.obtenerPorId(id);
            if (!partido.esCreador(email)) {
                redirectAttributes.addFlashAttribute("error", "No tienes permiso.");
                return "redirect:/partidos/" + id;
            }
            List<Gol> goles = new ArrayList<>();
            for (int i = 0; i < equipoJugadorIds.size(); i++) {
                EquipoJugador equipoJugador = servicioEquipoJugador.buscarPorId(equipoJugadorIds.get(i));
                goles.add(new Gol(partido, equipoJugador, cantidades.get(i)));
            }
            servicioGoles.registrarGoles(partido, goles, usuario);
            redirectAttributes.addFlashAttribute("success", "Partido finalizado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al finalizar el partido.");
        }
        return "redirect:/partidos/" + id;
    }
}
