package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.ErrorSubiendoImagenException;
import org.springframework.web.multipart.MultipartFile;

public interface ServicioImagenes {

    String subirImagen(MultipartFile insignia) throws ErrorSubiendoImagenException;

}