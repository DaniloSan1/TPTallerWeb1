
package com.tallerwebi.presentacion;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tallerwebi.dominio.Cancha;
import com.tallerwebi.dominio.FotoCancha;
import com.tallerwebi.dominio.Horario;
import com.tallerwebi.dominio.ReseniaCancha;
import com.tallerwebi.dominio.ServicioCancha;
import com.tallerwebi.dominio.ServicioFotoCancha;
import com.tallerwebi.dominio.ServicioHorario;
import com.tallerwebi.dominio.ServicioLogin;
import com.tallerwebi.dominio.ServicioReseniaCancha;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.Zona;

@Controller
public class ControladorCancha {
    private final ServicioCancha servicioCancha;
    private final ServicioHorario servicioHorario;
    private final ServicioLogin servicioLogin;
    private final ServicioFotoCancha servicioFotoCancha;
    private final ServicioReseniaCancha servicioReseniaCancha;

    @Autowired
    public ControladorCancha(ServicioCancha servicioCancha, ServicioHorario servicioHorario,
        ServicioLogin servicioLogin, ServicioFotoCancha servicioFotoCancha, ServicioReseniaCancha servicioReseniaCancha) {
        this.servicioCancha = servicioCancha;
        this.servicioHorario = servicioHorario;
        this.servicioLogin = servicioLogin;
        this.servicioFotoCancha = servicioFotoCancha;
        this.servicioReseniaCancha = servicioReseniaCancha;
    }

    @GetMapping("/canchas-disponibles")
    
    public String listarCanchas(ModelMap model,HttpServletRequest request) {
        String busqueda = request.getParameter("busqueda");
        String zonaParam = request.getParameter("zona");
        String precioParam = request.getParameter("precio");
        model.put("zona", zonaParam);
        model.put("precio", precioParam);
        model.put("busqueda", busqueda);
        Zona zona = null;
        if (zonaParam != null && !zonaParam.isEmpty()) {
            zona = Zona.valueOf(zonaParam);
        }
        Double precio = 0.0;
        try {
            if (precioParam != null && !precioParam.isEmpty()) {
                precio = Double.parseDouble(precioParam);
            }
            List<Cancha> canchas = servicioCancha.obtenerCanchasDisponibles(busqueda, zona, precio);
            List<FotoCancha> fotosCancha = servicioFotoCancha.insertarFotosAModelCanchas(canchas);
            List<Double> calificacionesPromedio = new ArrayList<>();
            for (Cancha cancha : canchas) {
                calificacionesPromedio.add(servicioReseniaCancha.calcularCalificacionPromedioCancha(cancha.getId()));
            }
            // Exponer la lista de calificaciones promedio al modelo para la vista
            model.put("calificacionesPromedio", calificacionesPromedio);
            model.put("fotosCanchas", fotosCancha);
            model.put("canchas", canchas);
            model.put("currentPage", "canchas-disponibles");
        } catch (Exception e) {
            model.put("error", e.getMessage());
        }
        return "canchas";
    }

    @GetMapping("/cancha/{id}")
    public ModelAndView verCancha(@PathVariable Long id, ModelMap model, HttpServletRequest request) {

        try {
            String email = (String) request.getSession().getAttribute("EMAIL");
            if (email == null) {
                return new ModelAndView("redirect:/login");
            }
            Usuario usuario = servicioLogin.buscarPorEmail(email);
            Cancha cancha = servicioCancha.obtenerCanchaPorId(id); // NO usar obtenerCanchaConHorariosPorId para evitar lazy
            // Cargar horarios explícitamente para evitar LazyInitializationException
            List<Horario> horarios = servicioHorario.obtenerPorCancha(cancha);
            String showNew = request.getParameter("showNew");
            if ("true".equalsIgnoreCase(showNew)) {
                horarios = servicioHorario.obtenerDisponiblesPorCancha(cancha);
            }
            List<ReseniaCancha> resniasCancha = servicioReseniaCancha.obtenerReseniasPorCancha(id);
            List<FotoCancha> fotosCancha = servicioFotoCancha.obtenerFotosCancha(id);
            model.put("cancha", cancha);
            model.put("horarios", horarios);
            model.put("horariosCount", horarios != null ? horarios.size() : 0);
            model.put("horariosEnCancha", horarios != null ? horarios.size() : 0);
            model.put("usuarioId", usuario.getId());
            model.put("calificacionPromedio", servicioReseniaCancha.calcularCalificacionPromedioCancha(id));
            model.put("resenias", resniasCancha);
            model.put("fotosCancha", fotosCancha);
        } catch (Exception e) {
            model.put("error", e.getMessage());
        }
        return new ModelAndView("cancha", model);
    }

    // --- Endpoints administrativos (requieren rol ADMIN o ROLE_ADMIN en sesión) ---
    @PostMapping("/admin/cancha/crear")
    public ModelAndView crearCancha(@ModelAttribute("cancha") Cancha cancha, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String rol = (String) request.getSession().getAttribute("ROL");
        if (rol == null || !(rol.equals("ADMIN") || rol.equals("ROLE_ADMIN"))) {
            redirectAttributes.addFlashAttribute("error", "Acceso denegado");
            return new ModelAndView("redirect:/login");
        }
        try {
            // Procesar horarios enviados desde el formulario (con campos indexados)
            int horariosCount = 0;
            try {
                horariosCount = Integer.parseInt(request.getParameter("horariosCount"));
            } catch (Exception e) {
                horariosCount = 0;
            }
            // Eliminar horarios previos si existen (por consistencia)
            cancha.getHorarios().clear();
            List<Horario> horarios = new ArrayList<>();
            java.util.Map<String, String> diasMap = new java.util.HashMap<>();
            diasMap.put("LUNES", "MONDAY");
            diasMap.put("MARTES", "TUESDAY");
            diasMap.put("MIERCOLES", "WEDNESDAY");
            diasMap.put("JUEVES", "THURSDAY");
            diasMap.put("VIERNES", "FRIDAY");
            diasMap.put("SABADO", "SATURDAY");
            diasMap.put("DOMINGO", "SUNDAY");
            for (int i = 0; i < horariosCount; i++) {
                String horaInicio = request.getParameter("horaInicio_" + i);
                String horaFin = request.getParameter("horaFin_" + i);
                String diaSemana = request.getParameter("diaSemana_" + i);
                String[] disponibleArr = request.getParameterValues("disponible_" + i);
                if (horaInicio == null || horaFin == null || diaSemana == null) continue;
                Horario horario = new Horario();
                horario.setCancha(cancha);
                horario.setHoraInicio(java.time.LocalTime.parse(horaInicio));
                horario.setHoraFin(java.time.LocalTime.parse(horaFin));
                String diaEn = diasMap.getOrDefault(diaSemana.toUpperCase(), "MONDAY");
                horario.setDiaSemana(java.time.DayOfWeek.valueOf(diaEn));
                boolean disponible = false;
                if (disponibleArr != null) {
                    // Si alguno de los valores es "on", el checkbox estaba marcado
                    for (String val : disponibleArr) {
                        if ("on".equalsIgnoreCase(val)) {
                            disponible = true;
                            break;
                        }
                    }
                }
                horario.setDisponible(disponible);
                horarios.add(horario);
            }
            // Validación: al menos un horario debe estar disponible
            boolean hayHorarioDisponible = horarios.stream().anyMatch(Horario::getDisponible);
            if (!hayHorarioDisponible) {
                redirectAttributes.addFlashAttribute("error", "Debe agregar al menos un horario disponible.");
                return new ModelAndView("redirect:/admin/cancha/crear-form");
            }
            cancha.setHorarios(horarios);
            servicioCancha.crearCancha(cancha);
            redirectAttributes.addFlashAttribute("mensaje", "Cancha creada correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo crear la cancha: " + e.getMessage());
        }
        return new ModelAndView("redirect:/canchas-disponibles");
    }

    @PostMapping("/admin/cancha/{id}/eliminar")
    public Object eliminarCancha(@PathVariable Long id, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String rol = (String) request.getSession().getAttribute("ROL");
        boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
        if (rol == null || !(rol.equals("ADMIN") || rol.equals("ROLE_ADMIN"))) {
            if (isAjax) {
                return "Acceso denegado";
            }
            redirectAttributes.addFlashAttribute("error", "Acceso denegado");
            return new ModelAndView("redirect:/login");
        }
        try {
            servicioCancha.eliminarCancha(id);
            if (isAjax) {
                return "OK";
            }
            redirectAttributes.addFlashAttribute("mensaje", "Cancha eliminada correctamente");
        } catch (Exception e) {
            String errorMsg = "No se pudo eliminar la cancha: " + e.getMessage();
            if (isAjax) {
                return errorMsg;
            }
            redirectAttributes.addFlashAttribute("error", errorMsg);
        }
        return new ModelAndView("redirect:/canchas-disponibles");
    }
        @PostMapping("/admin/cancha/{id}/editar-horarios")
        @Transactional
        public ModelAndView editarHorarios(@PathVariable Long id, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String rol = (String) request.getSession().getAttribute("ROL");
        if (rol == null || !(rol.equals("ADMIN") || rol.equals("ROLE_ADMIN"))) {
            redirectAttributes.addFlashAttribute("error", "Acceso denegado");
            return new ModelAndView("redirect:/login");
        }
        try {
            Cancha cancha = servicioCancha.obtenerCanchaConHorariosPorId(id);
            int count = 0;
            try {
                count = Integer.parseInt(request.getParameter("horariosCount"));
            } catch (Exception e) {
                count = 0;
            }
            List<Horario> horariosOriginales = cancha.getHorarios();
            List<Horario> aEliminar = new ArrayList<>();
            // Solo eliminar los horarios que están explícitamente marcados para eliminar
            for (int i = 0; i < count; i++) {
                String idStr = request.getParameter("horarioId_" + i);
                String[] eliminarParams = request.getParameterValues("eliminar_" + i);
                boolean eliminar = false;
                if (eliminarParams != null) {
                    for (String ep : eliminarParams) {
                        if ("on".equalsIgnoreCase(ep)) {
                            eliminar = true;
                            break;
                        }
                    }
                }
                if (eliminar && idStr != null && !idStr.isEmpty()) {
                    Long horarioId = Long.parseLong(idStr);
                    Horario horario = null;
                    for (Horario h : horariosOriginales) {
                        if (h.getId() != null && h.getId().equals(horarioId)) {
                            horario = h;
                            break;
                        }
                    }
                    if (horario != null) {
                        aEliminar.add(horario);
                    }
                }
            }
            horariosOriginales.removeAll(aEliminar);
            for (Horario h : aEliminar) {
                servicioHorario.eliminarPorId(h.getId());
            }
            // Actualizar o agregar horarios
            for (int i = 0; i < count; i++) {
                String idStr = request.getParameter("horarioId_" + i);
                String dia = request.getParameter("diaSemana_" + i);
                String hi = request.getParameter("horaInicio_" + i);
                String hf = request.getParameter("horaFin_" + i);
                String[] disponiblesParams = request.getParameterValues("disponible_" + i);
                String[] eliminarParams = request.getParameterValues("eliminar_" + i);
                boolean disponible = false;
                boolean eliminar = false;
                if (disponiblesParams != null) {
                    for (String dp : disponiblesParams) {
                        if ("on".equalsIgnoreCase(dp)) {
                            disponible = true;
                            break;
                        }
                    }
                }
                if (eliminarParams != null) {
                    for (String ep : eliminarParams) {
                        if ("on".equalsIgnoreCase(ep)) {
                            eliminar = true;
                            break;
                        }
                    }
                }
                if ((dia == null || dia.isEmpty()) && (hi == null || hi.isEmpty()) && (hf == null || hf.isEmpty()) && (idStr == null || idStr.isEmpty())) {
                    continue;
                }
                java.time.LocalTime horaInicio = hi != null && !hi.isEmpty() ? java.time.LocalTime.parse(hi) : null;
                java.time.LocalTime horaFin = hf != null && !hf.isEmpty() ? java.time.LocalTime.parse(hf) : null;
                if (idStr != null && !idStr.isEmpty()) {
                    Long horarioId = Long.parseLong(idStr);
                    Horario horario = null;
                    for (Horario h : horariosOriginales) {
                        if (h.getId() != null && h.getId().equals(horarioId)) {
                            horario = h;
                            break;
                        }
                    }
                    if (horario != null && !eliminar) {
                        if (dia != null && !dia.isEmpty()) horario.setDiaSemana(java.time.DayOfWeek.valueOf(dia));
                        if (horaInicio != null) horario.setHoraInicio(horaInicio);
                        if (horaFin != null) horario.setHoraFin(horaFin);
                        horario.setDisponible(disponible);
                        servicioHorario.actualizarHorarios(horario);
                    }
                } else {
                    if (!eliminar && dia != null && horaInicio != null && horaFin != null) {
                        Horario nuevo = new Horario();
                        nuevo.setCancha(cancha);
                        nuevo.setDiaSemana(java.time.DayOfWeek.valueOf(dia));
                        nuevo.setHoraInicio(horaInicio);
                        nuevo.setHoraFin(horaFin);
                        nuevo.setDisponible(disponible);
                        horariosOriginales.add(nuevo);
                        servicioHorario.guardar(nuevo);
                    }
                }
            }
            servicioCancha.guardar(cancha);
            redirectAttributes.addFlashAttribute("mensaje", "Horarios actualizados correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudieron actualizar los horarios: " + e.getMessage());
        }
        return new ModelAndView("redirect:/cancha/" + id);
    }

    @PostMapping("/admin/horario/{id}/toggle")
    public ModelAndView toggleHorario(@PathVariable Long id, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String rol = (String) request.getSession().getAttribute("ROL");
        if (rol == null || !(rol.equals("ADMIN") || rol.equals("ROLE_ADMIN"))) {
            redirectAttributes.addFlashAttribute("error", "Acceso denegado");
            return new ModelAndView("redirect:/login");
        }
        try {
            Horario h = servicioHorario.obtenerPorId(id);
            boolean nuevoEstado = !Boolean.TRUE.equals(h.getDisponible());
            servicioHorario.cambiarDisponibilidad(id, nuevoEstado);
            redirectAttributes.addFlashAttribute("mensaje", "Disponibilidad actualizada");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo actualizar el horario: " + e.getMessage());
        }
        return new ModelAndView("redirect:/canchas-disponibles");
    }

    @GetMapping("/admin/cancha/agregar")
    public ModelAndView mostrarFormularioAgregarCancha(HttpServletRequest request, ModelMap model) {
        String rol = (String) request.getSession().getAttribute("ROL");
        if (rol == null || !(rol.equals("ADMIN") || rol.equals("ROLE_ADMIN"))) {
            return new ModelAndView("redirect:/login");
        }
        model.put("cancha", new Cancha());
        return new ModelAndView("agregar-cancha", model);
    }

    @GetMapping("/admin/cancha/{id}/editar-horarios")
    public ModelAndView mostrarEditarHorarios(@PathVariable Long id, HttpServletRequest request, ModelMap model) {
        String rol = (String) request.getSession().getAttribute("ROL");
        if (rol == null || !(rol.equals("ADMIN") || rol.equals("ROLE_ADMIN"))) {
            return new ModelAndView("redirect:/login");
        }
    Cancha cancha = servicioCancha.obtenerCanchaConHorariosPorId(id);
        List<Horario> horarios = servicioHorario.obtenerPorCancha(cancha);
        model.put("cancha", cancha);
        model.put("horarios", horarios);
        return new ModelAndView("editar-horarios", model);
    }

    @PostMapping("/cancha/eliminar/{id}")
    public String eliminarCancha(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            servicioCancha.eliminarCancha(id);
            redirectAttributes.addFlashAttribute("mensaje", "Cancha eliminada exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la cancha: " + e.getMessage());
        }
        return "redirect:/canchas-disponibles";
    }

}

