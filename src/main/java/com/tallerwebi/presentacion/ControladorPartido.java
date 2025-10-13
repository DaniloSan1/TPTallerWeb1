package com.tallerwebi.presentacion;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.Nivel;
import com.tallerwebi.dominio.Partido;
import com.tallerwebi.dominio.ServicioLogin;
import com.tallerwebi.dominio.ServicioPartido;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.Zona;
import com.tallerwebi.dominio.excepcion.NoHayCupoEnPartido;
import com.tallerwebi.dominio.excepcion.PartidoNoEncontrado;
import com.tallerwebi.dominio.excepcion.YaExisteElParticipante;

@Controller
public class ControladorPartido {
    private ServicioPartido servicio;
    private ServicioLogin servicioLogin;

    @Autowired
    public ControladorPartido(ServicioPartido servicio, ServicioLogin servicioLogin) {
        this.servicio = servicio;
        this.servicioLogin = servicioLogin;
    }

    @GetMapping("/partidos")
    public String listar(@RequestParam(required = false) Zona zona,
            @RequestParam(required = false) Nivel nivel,
            @RequestParam(defaultValue = "true") boolean soloConCupo,
            ModelMap model) {
        List<Partido> partidos = servicio.buscar(zona, nivel, soloConCupo);
        model.put("partidos", partidos);
        model.put("zonas", Zona.values());
        model.put("niveles", Nivel.values());
        model.put("zonaSeleccionada", zona);
        model.put("nivelSeleccionado", nivel);
        model.put("soloConCupo", soloConCupo);
        model.put("active", "partidos"); // para el header
        model.put("titulo", "Partidos");
        return "partidos";
    }

    @GetMapping("detalle-partido/{id}")
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

    @PostMapping("partidos/{id}/inscripcion")
    public ResponseEntity<?> inscripcion(@PathVariable long id, HttpServletRequest request) throws Exception {
        try {
            servicio.anotarParticipante(id, request.getSession().getAttribute("EMAIL").toString());
            return ResponseEntity.ok().build();
        } catch (NoHayCupoEnPartido e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        } catch (PartidoNoEncontrado e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (YaExisteElParticipante e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
    @PostMapping("/partido/{id}/abandonar")
    public String abandonarPartido(@PathVariable Long id, HttpServletRequest request) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("USUARIO");
        if (usuario == null) {
            // si no hay usuario logueado, lo mandamos al login
            return "redirect:/login";
        }

        servicio.abandonarPartido(id, usuario.getId());
        return "redirect:/home";
    }

}
