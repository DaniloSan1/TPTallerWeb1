package com.tallerwebi.presentacion;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.UsuarioNoEncontradoException;

@Controller
@RequestMapping("/torneos")
@PropertySource("classpath:application.properties")
public class ControladorTorneo {

    private final ServicioTorneo servicioTorneo;
    private final ServicioCancha servicioCancha;
    private final ServicioLogin servicioLogin;
    private final ServicioPago servicioPago;
    private final ServicioInscripcion servicioInscripcion;
    private final ServicioEquipo servicioEquipo;

    @Value("${mercadopago.public.key}")
    private String publicKey;

    @Autowired
    public ControladorTorneo(ServicioTorneo servicioTorneo,
                             ServicioCancha servicioCancha,
                             ServicioLogin servicioLogin,
                             ServicioPago servicioPago,
                             ServicioInscripcion servicioInscripcion,
                             ServicioEquipo servicioEquipo) {
        this.servicioTorneo = servicioTorneo;
        this.servicioCancha = servicioCancha;
        this.servicioLogin = servicioLogin;
        this.servicioPago = servicioPago;
        this.servicioInscripcion = servicioInscripcion;
        this.servicioEquipo = servicioEquipo;
    }

    // Mostrar formulario de creación de torneo
    @GetMapping("/crear/{canchaId}")
    public ModelAndView mostrarFormulario(@PathVariable Long canchaId) {
        ModelAndView mav = new ModelAndView("formulario-torneo");
        Cancha cancha = servicioCancha.obtenerCanchaPorId(canchaId);
        mav.addObject("torneo", new Torneo());
        mav.addObject("cancha", cancha);
        return mav;
    }

    // Crear torneo con pago pendiente
    @PostMapping("/crear")
    public ModelAndView crearTorneo(@RequestParam Long canchaId,
                                    @RequestParam String nombre,
                                    @RequestParam String fecha,
                                    HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        try {
            String email = (String) request.getSession().getAttribute("EMAIL");
            if (email == null) {
                return new ModelAndView("redirect:/login");
            }

            Usuario organizador = servicioLogin.buscarPorEmail(email);
            Cancha cancha = servicioCancha.obtenerCanchaPorId(canchaId);

            LocalDate fechaTorneo = LocalDate.parse(fecha);

            Torneo torneo = new Torneo();
            torneo.setNombre(nombre);
            torneo.setFecha(fechaTorneo);
            torneo.setCancha(cancha);
            torneo.setOrganizador(organizador);

            // Crear torneo en servicio (validaciones de disponibilidad y precio)
            Torneo torneoCreado = servicioTorneo.crearTorneo(torneo);

            // Generar preferencia de pago en MercadoPago
            BigDecimal precioTotal = torneoCreado.getPrecio();
            String preferenceId = servicioPago.crearPago(
                    "Torneo en " + cancha.getNombre(),
                    "Pago de inscripción al torneo",
                    precioTotal,
                    torneoCreado.getId()
            );

            // Guardar pago en estado pendiente
            servicioPago.guardarPagoTorneo(torneoCreado, organizador, preferenceId, precioTotal.doubleValue());

            mav.setViewName("detalleTorneo");
            mav.addObject("torneo", torneoCreado);
            mav.addObject("cancha", cancha);
            mav.addObject("publicKey", publicKey);
            mav.addObject("preferenceId", preferenceId);
            mav.addObject("mensajeExito", "Torneo creado. Completá el pago para confirmarlo.");

        } catch (Exception e) {
            e.printStackTrace();
            mav.setViewName("formulario-torneo");
            mav.addObject("mensajeError", "Error al crear el torneo: " + e.getMessage());
            Cancha cancha = servicioCancha.obtenerCanchaPorId(canchaId);
            mav.addObject("cancha", cancha);
        }
        return mav;
    }

    // Listar torneos disponibles
    @GetMapping("/listar")
    public ModelAndView listarTorneos() {
        List<Torneo> torneos = servicioTorneo.listarTorneosDisponibles();
        ModelAndView mav = new ModelAndView("torneos-disponibles");
        mav.addObject("torneos", torneos);
        return mav;
    }

    @GetMapping("/disponible/{id}")
public String verTorneoDisponible(@PathVariable Long id,
                                  ModelMap model,
                                  HttpServletRequest request,
                                  RedirectAttributes redirectAttributes) {

    Torneo torneo = servicioTorneo.obtenerPorId(id);
    if (torneo == null) {
        redirectAttributes.addFlashAttribute("mensajeError", "El torneo no existe o fue eliminado.");
        return "redirect:/torneos/listar";
    }

    String email = (String) request.getSession().getAttribute("EMAIL");
    if (email == null) {
        redirectAttributes.addFlashAttribute("mensajeError", "Debes iniciar sesión para inscribirte.");
        return "redirect:/login";
    }

    Usuario usuario;
    try {
        usuario = servicioLogin.buscarPorEmail(email);
        Equipo equipoDelUsuario = servicioEquipo.buscarEquipoDelUsuario(usuario);
        List<Equipo> equiposDisponibles = servicioEquipo.buscarEquiposPorUsuario(usuario);
        model.put("usuarioPerteneceAEquipo", equipoDelUsuario);
        model.put("equiposDisponibles", equiposDisponibles);

    } catch (UsuarioNoEncontradoException e) {
        redirectAttributes.addFlashAttribute("mensajeError", "Tu sesión no es válida. Iniciá sesión nuevamente.");
        return "redirect:/login";
    }
    

    // Listas
    List<InscripcionTorneo> inscripciones = servicioInscripcion.listarInscripcionesPorTorneo(id);

    // Determinar si el usuario pertenece a un equipo inscrito
    boolean yaInscripto = false;
    Long inscripcionId = null;
    if (usuario != null) {
        for (InscripcionTorneo insc : inscripciones) {
            boolean pertenece = insc.getEquipo().getJugadores()
                    .stream()
                    .anyMatch(ej -> ej.getUsuario().getId().equals(usuario.getId()));
            if (pertenece) {
                yaInscripto = true;
                inscripcionId = insc.getId();
                break;
            }
        }
    }

    model.put("torneo", torneo);
    model.put("cancha", torneo.getCancha());
    model.put("inscripciones", inscripciones);
    model.put("yaInscripto", yaInscripto);
    model.put("inscripcionId", inscripcionId);
    return "TorneoDisponibleDetalle";
}



    // Ver detalle de torneo
    @GetMapping("/{id}")
    public String verDetalleTorneo(@PathVariable Long id, ModelMap model) {
        Torneo torneo = servicioTorneo.obtenerPorId(id);
        model.put("torneo", torneo);
        model.put("cancha", torneo.getCancha());
        return "detalleTorneo";
    }

    // Cancelar torneo
    @PostMapping("/cancelar/{id}")
    public String cancelarTorneo(@PathVariable Long id,
                                 RedirectAttributes redirectAttributes) {
        try {
            servicioTorneo.cancelarTorneo(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Torneo cancelado correctamente.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        }
        return "redirect:/home";
    }
    @PostMapping("/disponible/{torneoId}/inscribir")
public String inscribirEquipo(@PathVariable Long torneoId,
                              @RequestParam Long equipoId,
                              HttpServletRequest request,
                              RedirectAttributes redirectAttributes) {

    String email = (String) request.getSession().getAttribute("EMAIL");
    if (email == null) {
        redirectAttributes.addFlashAttribute("mensajeError", "Debes iniciar sesión para inscribirte.");
        return "redirect:/login";
    }

    Usuario usuario;
    try {
        usuario = servicioLogin.buscarPorEmail(email);
    } catch (UsuarioNoEncontradoException e) {
        redirectAttributes.addFlashAttribute("mensajeError", "Tu sesión no es válida. Iniciá sesión nuevamente.");
        return "redirect:/login";
    }

    try {
        servicioInscripcion.inscribirEquipo(torneoId, equipoId);
        redirectAttributes.addFlashAttribute("mensajeExito", "Equipo inscrito correctamente en el torneo.");
    } catch (RuntimeException e) {
        redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
    }

    return "redirect:/torneos/disponible/" + torneoId;
}

@PostMapping("/disponible/{torneoId}/cancelar/{inscripcionId}")
public String cancelarInscripcion(@PathVariable Long torneoId,
                                  @PathVariable Long inscripcionId,
                                  RedirectAttributes redirectAttributes) {
    try {
        servicioInscripcion.cancelarInscripcion(inscripcionId);
        redirectAttributes.addFlashAttribute("mensajeExito", "Inscripción cancelada correctamente.");
    } catch (RuntimeException e) {
        redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
    }

    return "redirect:/torneos/disponible/" + torneoId;
}

}