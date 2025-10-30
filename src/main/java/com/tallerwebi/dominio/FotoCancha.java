package com.tallerwebi.dominio;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class FotoCancha {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    @ManyToOne
    @JoinColumn(name = "cancha_id", nullable = false)
    private Cancha cancha;

    public FotoCancha() {
    }
    public FotoCancha(String url, Cancha cancha) {
        this.url = url;
        this.cancha = cancha;
    }

    public Long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public Cancha getCancha() {
        return cancha;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCancha(Cancha cancha) {
        this.cancha = cancha;
    }
}
