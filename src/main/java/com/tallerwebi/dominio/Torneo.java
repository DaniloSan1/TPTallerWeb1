package com.tallerwebi.dominio;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Torneo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private LocalDate fecha; // el torneo ocupa un día completo

    @OneToOne
    private Equipo ganador;

    @OneToOne
    private Usuario goleador;

    private Boolean finalizado = false;
    
    @ManyToOne
    @JoinColumn(name = "cancha_id", nullable = false)
    private Cancha cancha;

   @OneToMany(mappedBy = "torneo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<InscripcionTorneo> inscripciones = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "organizador_id", nullable = false)
    private Usuario organizador;

    private BigDecimal precio;
    private String estado;
    // Getters y setters
    public Long getId() { 
        return id; 
    }
    public void setId(Long id) { 
        this.id = id; 
    }

    public String getNombre() { 
        return nombre; 
    }
    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }

    public LocalDate getFecha() { 
        return fecha; 
    }
    public void setFecha(LocalDate fecha) { 
        this.fecha = fecha; 
    }

    public String getEstado() { 
        return estado; 
    }
    public void setEstado(String estado) { 
        this.estado = estado; 
    }

    public Cancha getCancha() { 
        return cancha; 
    }
    public void setCancha(Cancha cancha) { 
        this.cancha = cancha; 
    }
    public BigDecimal getPrecio() { 
        return precio; 
    }
    public void setPrecio(BigDecimal precio) { 
        this.precio = precio; 
    }

    public Usuario getOrganizador() { 
        return organizador; 
    }
    public void setOrganizador(Usuario organizador) { 
        this.organizador = organizador; 
    }
    public Boolean isFinalizado() { 
        return finalizado; 
    }
    public void setFinalizado(Boolean finalizado) { 
        this.finalizado = finalizado; 
    }
    public Equipo getGanador() { 
        return ganador; 
    }
    public void setGanador(Equipo ganador) { 
        this.ganador = ganador; 
    }
    public Usuario getGoleador() { 
        return goleador; 
    }
    public void setGoleador(Usuario goleador) { 
        this.goleador = goleador; 
    }
}