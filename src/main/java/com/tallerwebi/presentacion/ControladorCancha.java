package com.tallerwebi.presentacion;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tallerwebi.dominio.Cancha;
import com.tallerwebi.dominio.ServicioCancha;

@Controller
public class ControladorCancha {
    private final ServicioCancha servicioCancha;

    @Autowired
    public ControladorCancha(ServicioCancha servicioCancha) {
        this.servicioCancha = servicioCancha;
    }

    @GetMapping("/canchas-disponibles")
    public String listarCanchas(ModelMap model) {
        List<Cancha> canchas = servicioCancha.obtenerCanchasDisponibles();
        if (canchas.isEmpty()) {
            model.put("error", "No hay canchas disponibles en este momento");
            model.put("currentPage", "canchas");
            return "canchas";
        }
        model.put("canchas", canchas);
        model.put("currentPage", "canchas");
        return "canchas";
    }

    @GetMapping("/cancha/{id}")
    public String verCancha(@PathVariable Long id, ModelMap model) {
        Cancha cancha = servicioCancha.obtenerCanchaPorId(id);
        if (cancha == null) {
            model.put("error", "Cancha no encontrada");
            return "cancha";
        }
        model.put("cancha", cancha);
        return "cancha";
    }

}