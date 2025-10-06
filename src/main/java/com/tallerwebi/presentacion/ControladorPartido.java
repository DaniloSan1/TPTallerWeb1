package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.PartidoNoEncontrado;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/partidos")
public class ControladorPartido {

    private ServicioPartido servicio;

    @Autowired
    public ControladorPartido(ServicioPartido servicio) {
        this.servicio = servicio;
    }

    @GetMapping
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

    @GetMapping("/{id}")
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
