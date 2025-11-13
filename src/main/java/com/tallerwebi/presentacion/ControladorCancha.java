package com.tallerwebi.presentacion;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
            
            Usuario usuario = servicioLogin.buscarPorEmail(request.getSession().getAttribute("EMAIL").toString());
            Cancha cancha = servicioCancha.obtenerCanchaPorId(id);
            List<Horario> horarios = servicioHorario.obtenerPorCancha(cancha);
            List<ReseniaCancha> resniasCancha= servicioReseniaCancha.obtenerReseniasPorCancha(id);
            List<FotoCancha> fotosCancha=servicioFotoCancha.obtenerFotosCancha(id);
            model.put("cancha", cancha);
            model.put("horarios", horarios);
            model.put("usuarioId", usuario.getId());
            model.put("calificacionPromedio", servicioReseniaCancha.calcularCalificacionPromedioCancha(id));
            model.put("resenias", resniasCancha);
            model.put("fotosCancha", fotosCancha);
        } catch (Exception e) {
            model.put("error", e.getMessage());
        }
        return new ModelAndView("cancha", model);
    }

}

