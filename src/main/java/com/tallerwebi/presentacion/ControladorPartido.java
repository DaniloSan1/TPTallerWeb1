package com.tallerwebi.presentacion;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.Nivel;
import com.tallerwebi.dominio.Partido;
import com.tallerwebi.dominio.ServicioPartido;
import com.tallerwebi.dominio.Zona;

@Controller
public class ControladorPartido {

    private ServicioPartido servicio;

    @Autowired
    public ControladorPartido(ServicioPartido servicio) {
        this.servicio = servicio;
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
    public ModelAndView detalle(@PathVariable long id) {
        ModelMap modelo = new ModelMap();

        try {
            Partido partido = servicio.obtenerPorId(id);
            modelo.put("partido", new DetallePartido(partido));

        } catch (Exception e) {
            modelo.put("error", e.getMessage());
        }

        return new ModelAndView("detalle-partido", modelo);
    }
}
