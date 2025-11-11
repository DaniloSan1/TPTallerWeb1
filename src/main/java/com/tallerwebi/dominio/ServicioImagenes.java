package com.tallerwebi.dominio;

import org.springframework.web.multipart.MultipartFile;

public interface ServicioImagenes {

    String subirImagen(MultipartFile insignia) throws Exception;

}