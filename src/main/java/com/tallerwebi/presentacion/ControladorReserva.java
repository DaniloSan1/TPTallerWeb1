package com.tallerwebi.presentacion;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.tallerwebi.dominio.Horario;
import com.tallerwebi.dominio.Reserva;
import com.tallerwebi.dominio.ServicioReserva;
import com.tallerwebi.dominio.Usuario;

@Controller
@RequestMapping("/reserva")
public class ControladorReserva {
    private final ServicioReserva servicioReserva;
    
    @Autowired
    public ControladorReserva(ServicioReserva servicioReserva) {
        this.servicioReserva = servicioReserva;
    }
    
    @GetMapping("/nueva")
    public String mostrarFormularioReserva(ModelMap model, @RequestParam Long usuarioId){
        model.put("usuarioId", usuarioId);
        return "reservaForm";
    }
    @PostMapping("/crear")
    public String crearReserva(
                                @RequestParam Long horarioId,
                                @RequestParam String fechaReserva,
                                @RequestParam Long usuarioId,
                                ModelMap model){
        try {
         if(usuarioId == null){
            throw new RuntimeException("El usuario es nulo");
         }
         if(horarioId == null){
            throw new RuntimeException("El horario es nulo");
         }
         if(fechaReserva == null){
            throw new RuntimeException("La fecha es nula");
         }
         Horario horario = new Horario();
         horario.setId(horarioId);
         Usuario usuario = new Usuario();
         usuario.setId(usuarioId);
         LocalDateTime fecha = LocalDate.parse(fechaReserva).atStartOfDay();
         Reserva reserva = new Reserva(horario, usuario, fecha);
         this.servicioReserva.crearReserva(reserva);
         return "redirect:/reserva/nueva?usuarioId=" + usuarioId;
        } catch (Exception e) {
            model.put("error", e.getMessage());
            return "reservaForm";
        }
    }
    @PostMapping("/cancelar/{id}")
    public String cancelarReserva(
            @PathVariable Long id,
            @RequestParam Long usuarioId,
            ModelMap model
    ) {
        try {
            servicioReserva.cancelarReserva(id);
            model.put("mensajeExito", "Reserva cancelada correctamente");
        } catch (RuntimeException e) {
            model.put("mensajeError", e.getMessage());
        }
        return "redirect:/reserva/nueva?usuarioId=" + usuarioId;
    }
}