package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.UsernameExistenteException;
import com.tallerwebi.dominio.excepcion.UsuarioNoEncontradoException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

@Controller
@RequestMapping("/perfil")
public class ControladorUsuario {
    private final ServicioUsuario servicioUsuario;
    private ServicioLogin servicioLogin;
    private ServicioAmistad servicioAmistad;
    private ServicioCalificacion servicioCalificacion;

    public ControladorUsuario(ServicioLogin servicioLogin, ServicioUsuario servicioUsuario,
            ServicioAmistad servicioAmistad, ServicioCalificacion servicioCalificacion) {
        this.servicioLogin = servicioLogin;
        this.servicioUsuario = servicioUsuario;
        this.servicioAmistad = servicioAmistad;
        this.servicioCalificacion = servicioCalificacion;
    }

    @GetMapping("/ver/id/{id}")
    public String verPerfilDeOtroJugador(@PathVariable Long id, ModelMap modelo) {
        try {
            Usuario usuarioAVer = servicioLogin.buscarPorId(id);

            if (usuarioAVer == null) {
                return "redirect:/home";
            }

            Double calificacionPromedioUsuario = servicioCalificacion
                    .calcularCalificacionPromedioUsuario(usuarioAVer.getId());
            modelo.addAttribute("calificacionPromedioUsuario", calificacionPromedioUsuario);
            modelo.addAttribute("usuarioAVer", usuarioAVer);
            return "perfilDeOtroJugador";
        } catch (UsuarioNoEncontradoException e) {
            return "redirect:/home";
        }
    }

    @GetMapping("/ver/username/{username}")
    public String verPerfilDeOtroJugador(@PathVariable String username, ModelMap modelo, HttpServletRequest request) {
        Usuario usuarioAVer = servicioLogin.buscarPorUsername(username);

        if (usuarioAVer == null) {
            return "redirect:/home";
        }

        String usernameABuscar = (String) request.getSession().getAttribute("USERNAME");
        Usuario usuarioActual = servicioUsuario.buscarPorUsername(usernameABuscar);
        if (usuarioAVer.getUsername().equals(usernameABuscar)) {
            return "redirect:/perfil";
        }

        Amistad amistad = servicioAmistad.buscarRelacionEntreUsuarios(usuarioActual.getId(), usuarioAVer.getId());
        Double calificacionPromedioUsuario = servicioCalificacion
                .calcularCalificacionPromedioUsuario(usuarioAVer.getId());
        modelo.addAttribute("calificacionPromedioUsuario", calificacionPromedioUsuario);
        modelo.addAttribute("usuarioAVer", usuarioAVer);
        modelo.addAttribute("usuarioActual", usuarioActual);
        modelo.addAttribute("amistad", amistad);
        modelo.addAttribute("estadoAmistad", amistad != null ? amistad.getEstadoDeAmistad() : null);

        return "perfilDeOtroJugador";
    }

    @GetMapping
    public String perfil(ModelMap modelo, HttpServletRequest request) {
        String email = (String) request.getSession().getAttribute("EMAIL");
        if (email != null) {
            try {
                Usuario usuario = servicioLogin.buscarPorEmail(email);
                modelo.addAttribute("usuario", usuario);

                // Obtener calificación promedio del usuario
                Double calificacionPromedio = servicioCalificacion.calcularCalificacionPromedioUsuario(usuario.getId());
                modelo.addAttribute("calificacionPromedio", calificacionPromedio);

                // Obtener amigos
                List<Amistad> relaciones = servicioAmistad.verAmigos(usuario.getId());
                List<Usuario> amigos = new ArrayList<>();
                if (relaciones != null) {
                    for (Amistad a : relaciones) {
                        if (a.getUsuario1() != null && a.getUsuario1().getId().equals(usuario.getId())) {
                            amigos.add(a.getUsuario2());
                        } else {
                            amigos.add(a.getUsuario1());
                        }
                    }
                }
                modelo.addAttribute("amigos", amigos);

                // Obtener cantidad de solicitudes pendientes
                List<Amistad> solicitudesPendientes = servicioAmistad.verSolicitudesPendientes(usuario.getId());
                modelo.addAttribute("solicitudesPendientes",
                        solicitudesPendientes != null ? solicitudesPendientes.size() : 0);

                modelo.put("currentPage", "perfil");
                return "perfil";
            } catch (UsuarioNoEncontradoException e) {
                return "redirect:/login";
            }
        }
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public ModelAndView logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return new ModelAndView("redirect:/login");
    }

    @GetMapping("/editar")
    public String editar(ModelMap modelo, HttpServletRequest request) {
        String email = (String) request.getSession().getAttribute("EMAIL");
        if (email != null) {
            try {
                Usuario usuario = servicioLogin.buscarPorEmail(email);
                modelo.addAttribute("usuario", usuario);
                return "editarPerfil";
            } catch (UsuarioNoEncontradoException e) {
                return "redirect:/login";
            }
        }

        return "redirect:/login";
    }

    @PostMapping("/editar")
    public String guardarCambios(@ModelAttribute("usuario") Usuario usuarioEditado, HttpServletRequest request,
            ModelMap modelo) {
        String email = (String) request.getSession().getAttribute("EMAIL");
        if (email != null) {
            try {
                Usuario usuario = servicioLogin.buscarPorEmail(email);
                Usuario usuarioExistente = servicioUsuario.buscarPorUsername(usuarioEditado.getUsername());
                if (usuarioExistente != null && !usuarioExistente.getId().equals(usuarioEditado.getId())) {
                    throw new UsernameExistenteException();
                }

                usuario.setUsername(usuarioEditado.getUsername());
                usuario.setNombre(usuarioEditado.getNombre());
                usuario.setApellido(usuarioEditado.getApellido());
                usuario.setPosicionFavorita(usuarioEditado.getPosicionFavorita());
                servicioUsuario.modificarUsuario(usuario);

                return "redirect:/perfil";

            } catch (UsernameExistenteException e) {
                modelo.addAttribute("usuario", usuarioEditado);
                modelo.addAttribute("error", e.getMessage());
                return "editarPerfil";
            } catch (UsuarioNoEncontradoException e) {
                return "redirect:/login";
            }
        }
        return "redirect:/login";
    }

    @GetMapping("/buscar")
    public String buscarPorUsername(@RequestParam("username") String username, ModelMap modelo) {
        if (username == null || username.trim().isEmpty()) {
            modelo.addAttribute("error", "Por favor ingrese un nombre de usuario para buscar");
            return "usuariosBuscados";
        }

        List<Usuario> usuarios = servicioUsuario.filtrarPorUsername(username.trim());
        modelo.addAttribute("usuarios", usuarios);
        modelo.addAttribute("terminoBusqueda", username);

        if (usuarios.isEmpty()) {
            modelo.addAttribute("mensaje", "No se encontraron usuarios con ese nombre");
        }

        return "usuariosBuscados";
    }

    @PostMapping("/amistad/enviar/{idReceptor}")
    public String enviarSolicitud(@PathVariable Long idReceptor, HttpServletRequest request) {
        try {
            String email = (String) request.getSession().getAttribute("EMAIL");
            Usuario remitente = servicioLogin.buscarPorEmail(email);
            Usuario receptor = servicioLogin.buscarPorId(idReceptor);

            if (remitente != null) {
                servicioAmistad.enviarSolicitud(remitente.getId(), idReceptor);
            }

            return "redirect:/perfil/ver/username/" + receptor.getUsername();
        } catch (Exception e) {
            return "redirect:/home";
        }
    }

    @PostMapping("/amistad/aceptar/{idAmistad}")
    public String aceptarSolicitud(@PathVariable Long idAmistad) {
        servicioAmistad.aceptarSolicitud(idAmistad);
        return "redirect:/perfil/solicitudes";
    }

    @PostMapping("/amistad/rechazar/{idAmistad}")
    public String rechazarSolicitud(@PathVariable Long idAmistad) {
        servicioAmistad.rechazarSolicitud(idAmistad);
        return "redirect:/perfil/solicitudes";
    }

    @PostMapping("/amigos/eliminar")
    public String eliminarAmigo(@RequestParam Long amigoId, HttpServletRequest request) {
        try {
            String email = (String) request.getSession().getAttribute("EMAIL");
            if (email == null)
                return "redirect:/login";
            Usuario usuario = servicioLogin.buscarPorEmail(email);

            Amistad amistad = servicioAmistad.buscarRelacionEntreUsuarios(usuario.getId(), amigoId);
            if (amistad != null) {
                // eliminar físicamente la relación de amistad
                servicioAmistad.eliminarAmistad(amistad.getIdAmistad());
            }
            return "redirect:/perfil";
        } catch (UsuarioNoEncontradoException e) {
            return "redirect:/login";
        }
    }

    @GetMapping("/solicitudes")
    public String verSolicitudesAmistad(ModelMap modelo, HttpServletRequest request) {
        try {
            String email = (String) request.getSession().getAttribute("EMAIL");
            if (email == null)
                return "redirect:/login";

            Usuario usuario = servicioLogin.buscarPorEmail(email);
            List<Amistad> solicitudesPendientes = servicioAmistad.verSolicitudesPendientes(usuario.getId());
            modelo.addAttribute("solicitudesPendientes", solicitudesPendientes);

            return "solicitudes-amistad";
        } catch (UsuarioNoEncontradoException e) {
            return "redirect:/login";
        }
    }

}
