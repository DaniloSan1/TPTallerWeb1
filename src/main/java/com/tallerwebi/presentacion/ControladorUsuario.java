package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.UsernameExistenteException;
import com.tallerwebi.dominio.excepcion.UsuarioNoEncontradoException;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.ArrayList;

@Controller
@RequestMapping("/perfil")
public class ControladorUsuario {
    private ServicioUsuario servicioUsuario;
    private ServicioLogin servicioLogin;
    private ServicioAmistad servicioAmistad;
    private ServicioSolicitudUnirse servicioSolicitudUnirse;
    private ServicioCalificacion servicioCalificacion;
    private ServicioNotificacionDeUsuario servicioNotificacionDeUsuario;
    private ServicioGoles servicioGoles;

    // Single constructor with all dependencies to avoid ambiguity during injection
    @Autowired
    public ControladorUsuario(ServicioLogin servicioLogin,
                              ServicioUsuario servicioUsuario,
                              ServicioAmistad servicioAmistad,
                              ServicioSolicitudUnirse servicioSolicitudUnirse,
                              ServicioCalificacion servicioCalificacion,
                              ServicioNotificacionDeUsuario servicioNotificacionDeUsuario,ServicioGoles servicioGoles) {
        this.servicioLogin = servicioLogin;
        this.servicioUsuario = servicioUsuario;
        this.servicioAmistad = servicioAmistad;
        this.servicioSolicitudUnirse = servicioSolicitudUnirse;
        this.servicioCalificacion = servicioCalificacion;
        this.servicioNotificacionDeUsuario = servicioNotificacionDeUsuario;
        this.servicioGoles=servicioGoles;
    }

    // Backwards-compatible constructor used in tests or contexts where ServicioNotificacionDeUsuario
    // isn't provided (keeps the original parameter order used by existing tests)
    public ControladorUsuario(ServicioLogin servicioLogin,
                              ServicioUsuario servicioUsuario,
                              ServicioAmistad servicioAmistad,
                              ServicioNotificacionDeUsuario servicioNotificacionDeUsuario,
                              ServicioCalificacion servicioCalificacion,ServicioGoles servicioGoles) {
        // Keep servicioSolicitudUnirse as null for backwards compatibility
        this(servicioLogin, servicioUsuario, servicioAmistad, null, servicioCalificacion, servicioNotificacionDeUsuario,servicioGoles);
    }

     @GetMapping("/ver/id/{id}")
    public String verPerfilDeOtroJugador(@PathVariable Long id, ModelMap modelo, HttpServletRequest request) {
        try {
            Usuario usuarioAVer = servicioLogin.buscarPorId(id);

            if (usuarioAVer == null) {
                return "redirect:/home";
            }

        String usernameABuscar = (String) request.getSession().getAttribute("USERNAME");
        if (usernameABuscar != null) {
            Usuario usuarioActual = servicioUsuario.buscarPorUsername(usernameABuscar);
            if (usuarioActual != null) {
                if (usuarioAVer.getUsername().equals(usernameABuscar)) {
                    return "redirect:/perfil";
                }

                Amistad amistad = servicioAmistad.buscarRelacionEntreUsuarios(usuarioActual.getId(), usuarioAVer.getId());
                modelo.addAttribute("usuarioActual", usuarioActual);
                modelo.addAttribute("amistad", amistad);
                modelo.addAttribute("estadoAmistad", amistad != null ? amistad.getEstadoDeAmistad() : null);
            }
        }
            Double calificacionPromedioUsuario = servicioCalificacion.calcularCalificacionPromedioUsuario(usuarioAVer.getId());
            int golesTotalesUsuario=servicioGoles.devolverCantidadTotalDeGolesDelUsuario(usuarioAVer.getId());
            Double golesPromedioUsuario=servicioGoles.devolverGolesPromedioPorPartidoDelUsuario(usuarioAVer.getId());
            modelo.addAttribute("calificacionPromedioUsuario", calificacionPromedioUsuario);
            modelo.addAttribute("golesPromedioUsuario", golesPromedioUsuario);
            modelo.addAttribute("golesTotalesUsuario",golesTotalesUsuario);
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
        Double calificacionPromedioUsuario = servicioCalificacion.calcularCalificacionPromedioUsuario(usuarioAVer.getId());
        int golesTotalesUsuario=servicioGoles.devolverCantidadTotalDeGolesDelUsuario(usuarioAVer.getId());
        Double golesPromedioUsuario=servicioGoles.devolverGolesPromedioPorPartidoDelUsuario(usuarioAVer.getId());
        modelo.addAttribute("golesTotalesUsuario",golesTotalesUsuario);
        modelo.addAttribute("golesPromedioUsuario", golesPromedioUsuario);
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
        if (email == null) {
            return "redirect:/login";
        }

        try {
            Usuario usuario = servicioLogin.buscarPorEmail(email);
            modelo.addAttribute("usuario", usuario);

            // Calificación promedio
            Double calificacionPromedio = servicioCalificacion.calcularCalificacionPromedioUsuario(usuario.getId());
            int golesTotales=servicioGoles.devolverCantidadTotalDeGolesDelUsuario(usuario.getId());
            Double golesPromedio=servicioGoles.devolverGolesPromedioPorPartidoDelUsuario(usuario.getId());
            modelo.addAttribute("golesTotales", golesTotales);
            modelo.addAttribute("calificacionPromedio", calificacionPromedio);
            modelo.addAttribute("golesPromedio", golesPromedio);

            // Amigos
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
                modelo.addAttribute("amigos", amigos);}

            // Solicitudes pendientes (amistad + invitaciones a partidos)
            List<Amistad> solicitudesPendientes = servicioAmistad.verSolicitudesPendientes(usuario.getId());
            List<SolicitudUnirse> invitacionesPartidoPendientes = new ArrayList<>();
            if (servicioSolicitudUnirse != null) {
                invitacionesPartidoPendientes = servicioSolicitudUnirse.listarPendientesPorEmailDestino(email);
            }
            int cantidadAmistad = solicitudesPendientes != null ? solicitudesPendientes.size() : 0;
            int cantidadInvitaciones = invitacionesPartidoPendientes != null ? invitacionesPartidoPendientes.size() : 0;
            modelo.addAttribute("solicitudesPendientes", cantidadAmistad + cantidadInvitaciones);

            modelo.put("currentPage", "perfil");
            return "perfil";
        } catch (UsuarioNoEncontradoException e) {
            return "redirect:/login";
        }
    }

        @GetMapping("/logout")
        public ModelAndView logout (HttpServletRequest request){
            request.getSession().invalidate();
            return new ModelAndView("redirect:/login");
        }

        @GetMapping("/editar")
        public String editar (ModelMap modelo, HttpServletRequest request){
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
        public String guardarCambios (@ModelAttribute("usuario") Usuario usuarioEditado, HttpServletRequest request,
            ModelMap modelo){
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
        public String buscarPorUsername (@RequestParam("username") String username, ModelMap modelo){
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
        public String enviarSolicitud (@PathVariable Long idReceptor, HttpServletRequest request){
            try {
                String email = (String) request.getSession().getAttribute("EMAIL");
                Usuario remitente = servicioLogin.buscarPorEmail(email);
                Usuario receptor = servicioLogin.buscarPorId(idReceptor);

                if (remitente != null) {
                    servicioAmistad.enviarSolicitud(remitente.getId(), idReceptor);
                    String mensaje = "El usuario " + remitente.getUsername() + " te ha enviado una solicitud de amistad."; //crea el msj para la noti
                    servicioNotificacionDeUsuario.crearNotificacion(receptor, mensaje); //crea la noti

                }

                return "redirect:/perfil/ver/username/" + receptor.getUsername();
            } catch (Exception e) {
                return "redirect:/home";
            }
        }

        @PostMapping("/amistad/aceptar/{idAmistad}")
        public String aceptarSolicitud (@PathVariable Long idAmistad){
            servicioAmistad.aceptarSolicitud(idAmistad);
            return "redirect:/perfil/solicitudes";
        }

        @PostMapping("/amistad/rechazar/{idAmistad}")
        public String rechazarSolicitud (@PathVariable Long idAmistad){
            servicioAmistad.rechazarSolicitud(idAmistad);
            return "redirect:/perfil/solicitudes";
        }

        @PostMapping("/amigos/eliminar")
        public String eliminarAmigo (@RequestParam Long amigoId, HttpServletRequest request){
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
        public String verSolicitudes(ModelMap modelo, HttpServletRequest request) {
            try {
                String email = (String) request.getSession().getAttribute("EMAIL");
                if (email == null) {
                    return "redirect:/login";
                }

                Usuario usuario = servicioLogin.buscarPorEmail(email);

                List<Amistad> solicitudesAmistadPendientes = servicioAmistad.verSolicitudesPendientes(usuario.getId());
                List<SolicitudUnirse> invitacionesPartidoPendientes = servicioSolicitudUnirse.listarPendientesPorEmailDestino(email);

                modelo.addAttribute("solicitudesAmistadPendientes", solicitudesAmistadPendientes);
                modelo.addAttribute("invitacionesPartidoPendientes", invitacionesPartidoPendientes);

                // La plantilla existente se llama `solicitudes-amistad.html`, retornar ese nombre
                return "solicitudes-amistad";
            } catch (UsuarioNoEncontradoException e) {
                return "redirect:/login";
            }
        }

        @GetMapping("/notificaciones")
        public String verNotificaciones(ModelMap modelo, HttpServletRequest request) throws UsuarioNoEncontradoException {
            String email = (String) request.getSession().getAttribute("EMAIL");
            if (email == null) {
                return "redirect:/login";
            }
            Usuario usuario = servicioLogin.buscarPorEmail(email);
            List<NotificacionDeUsuario> notificaciones = servicioNotificacionDeUsuario.obtenerListaDeNotificaciones(usuario);
            modelo.addAttribute("notificaciones", notificaciones);
            return "notificaciones";
        }

        @PostMapping("/notificaciones/eliminar")
        public String eliminarNotificacion(@RequestParam Long idNotificacion) {
            servicioNotificacionDeUsuario.eliminarNotificacion(idNotificacion);
            return "redirect:/perfil/notificaciones";
        }

        @PostMapping("/notificaciones/marcar-como-leida")
        public String marcarComoLeidaYRedirigir(@RequestParam Long idNotificacion) {

            String username = servicioNotificacionDeUsuario
                .marcarComoLeidaYObtenerUsername(idNotificacion);

            if (username != null) {
                return "redirect:/perfil/ver/username/" + username;
            }

            return "redirect:/perfil/notificaciones";
        }
        }




