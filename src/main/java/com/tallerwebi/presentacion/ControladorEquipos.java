package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Equipo;
import com.tallerwebi.dominio.EquipoJugador;
import com.tallerwebi.dominio.Partido;
import com.tallerwebi.dominio.ServicioEquipo;
import com.tallerwebi.dominio.ServicioEquipoJugador;
import com.tallerwebi.dominio.ServicioImagenes;
import com.tallerwebi.dominio.ServicioLogin;
import com.tallerwebi.dominio.ServicioPartido;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.EquipoNoEncontrado;
import com.tallerwebi.dominio.excepcion.PermisosInsufficientes;
import com.tallerwebi.dominio.excepcion.UsuarioNoEncontradoException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ControladorEquipos {

    private final ServicioEquipo servicioEquipo;
    private final ServicioLogin servicioLogin;
    private final ServicioEquipoJugador servicioEquipoJugador;
    private final ServicioPartido servicioPartido;
    private final ServicioImagenes servicioImagenes;

    @Autowired
    public ControladorEquipos(ServicioEquipo servicioEquipo, ServicioLogin servicioLogin,
            ServicioEquipoJugador servicioEquipoJugador, ServicioPartido servicioPartido,
            ServicioImagenes servicioImagenes) {
        this.servicioEquipo = servicioEquipo;
        this.servicioLogin = servicioLogin;
        this.servicioEquipoJugador = servicioEquipoJugador;
        this.servicioPartido = servicioPartido;
        this.servicioImagenes = servicioImagenes;
    }

    @GetMapping("/equipos/mis-equipos")
    public String misEquipos(HttpServletRequest request, ModelMap model) {
        String email = (String) request.getSession().getAttribute("EMAIL");
        if (email == null) {
            return "redirect:/login";
        }
        try {
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
        } catch (UsuarioNoEncontradoException e) {
            return "redirect:/login";
        }
    }

    @GetMapping("/equipos/{id}")
    public String detalleEquipo(@PathVariable Long id, HttpServletRequest request, ModelMap model)
            throws EquipoNoEncontrado, UsuarioNoEncontradoException {
        String email = (String) request.getSession().getAttribute("EMAIL");
        if (email == null) {
            return "redirect:/login";
        }

        try {
            Usuario usuario = servicioLogin.buscarPorEmail(email);
            Equipo equipo = servicioEquipo.buscarPorId(id);
            Boolean esCreador = servicioEquipo.esUsuarioCreador(equipo.getId(), usuario);
            List<EquipoJugador> jugadores = servicioEquipoJugador.buscarPorEquipo(equipo);
            List<Partido> partidos = servicioPartido.listarPorEquipoConInfoCancha(equipo);
            DetalleEquipo detalleEquipo = new DetalleEquipo(equipo, partidos, jugadores, usuario);

            model.put("equipo", detalleEquipo);
            model.put("esCreador", esCreador);
            model.put("currentPage", "mis-equipos");
            return "equipo";
        } catch (EquipoNoEncontrado e) {
            model.put("error", e.getMessage());
        } catch (UsuarioNoEncontradoException e) {
            return "redirect:/login";
        } catch (Exception e) {
            model.put("error", "Error inesperado al cargar el equipo");
        }

        return "redirect:/equipos/mis-equipos";
    }

    @GetMapping("/equipos/{id}/editar")
    public String editarEquipo(@PathVariable Long id, HttpServletRequest request, ModelMap model)
            throws EquipoNoEncontrado, UsuarioNoEncontradoException {
        String email = (String) request.getSession().getAttribute("EMAIL");
        if (email == null) {
            return "redirect:/login";
        }

        try {
            Usuario usuario = servicioLogin.buscarPorEmail(email);
            servicioEquipo.validarUsuarioEsCreador(id, usuario);
            Equipo equipo = servicioEquipo.buscarPorId(id);

            model.put("equipo", equipo);
            model.put("currentPage", "mis-equipos");
            return "editar-equipo";
        } catch (PermisosInsufficientes e) {
            model.put("error", e.getMessage());
            return "redirect:/equipos/mis-equipos";
        } catch (EquipoNoEncontrado e) {
            model.put("error", e.getMessage());
            return "redirect:/equipos/mis-equipos";
        } catch (UsuarioNoEncontradoException e) {
            return "redirect:/login";
        } catch (Exception e) {
            model.put("error", "Error inesperado al cargar el equipo para editar");
            return "redirect:/equipos/mis-equipos";
        }
    }

    @PostMapping("/equipos/{id}/editar")
    public String actualizarEquipo(@PathVariable Long id,
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam(value = "insignia", required = false) MultipartFile insignia,
            HttpServletRequest request, RedirectAttributes redirectAttributes, ModelMap model)
            throws EquipoNoEncontrado, UsuarioNoEncontradoException {
        String email = (String) request.getSession().getAttribute("EMAIL");
        if (email == null) {
            return "redirect:/login";
        }

        try {
            Usuario usuario = servicioLogin.buscarPorEmail(email);
            servicioEquipo.validarUsuarioEsCreador(id, usuario);
            Equipo equipo = servicioEquipo.buscarPorId(id);

            // Validate required fields
            if (nombre == null || nombre.trim().isEmpty()) {
                model.put("error", "El nombre del equipo es obligatorio");
                model.put("equipo", equipo);
                return "editar-equipo";
            }
            if (descripcion == null || descripcion.trim().isEmpty()) {
                model.put("error", "La descripción del equipo es obligatoria");
                model.put("equipo", equipo);
                return "editar-equipo";
            }

            String insigniaUrl = equipo.getInsigniaUrl();
            if (insignia != null && !insignia.isEmpty()) {
                try {
                    insigniaUrl = servicioImagenes.subirImagen(insignia);
                } catch (Exception e) {
                    model.put("error", "Error al subir la insignia: " + e.getMessage());
                    model.put("equipo", equipo);
                    return "editar-equipo";
                }
            } else if (equipo.getInsigniaUrl() == null || equipo.getInsigniaUrl().isEmpty()) {
                model.put("error", "La insignia del equipo es obligatoria");
                model.put("equipo", equipo);
                return "editar-equipo";
            }

            // Update equipo
            servicioEquipo.actualizarEquipo(equipo, nombre.trim(), descripcion.trim(), insigniaUrl);

            redirectAttributes.addFlashAttribute("success", "Equipo actualizado exitosamente");
            return "redirect:/equipos/" + id;
        } catch (PermisosInsufficientes e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/equipos/mis-equipos";
        } catch (EquipoNoEncontrado e) {
            model.put("error", e.getMessage());
            return "redirect:/equipos/mis-equipos";
        } catch (UsuarioNoEncontradoException e) {
            return "redirect:/login";
        } catch (Exception e) {
            model.put("error", "Error inesperado al actualizar el equipo");
            return "redirect:/equipos/" + id + "/editar";
        }
    }
}