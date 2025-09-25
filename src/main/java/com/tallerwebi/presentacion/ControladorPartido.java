package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/partidos")
public class ControladorPartido {

    private final ServicioPartido servicio;

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
}
