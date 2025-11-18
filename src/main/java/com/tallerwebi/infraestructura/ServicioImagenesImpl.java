package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.AdaptadorAWSS3;
import com.tallerwebi.dominio.ServicioImagenes;
import com.tallerwebi.dominio.excepcion.ErrorSubiendoImagenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ServicioImagenesImpl implements ServicioImagenes {

    private final AdaptadorAWSS3 adaptadorAWSS3;

    @Autowired
    public ServicioImagenesImpl(AdaptadorAWSS3 adaptadorAWSS3) {
        this.adaptadorAWSS3 = adaptadorAWSS3;
    }

    @Override
    public String subirImagen(MultipartFile insignia) throws ErrorSubiendoImagenException {
        try {
            return adaptadorAWSS3.subirArchivo(insignia);
        } catch (Exception e) {
            // Log the error and throw a custom exception
            // You could also use a logger here: logger.error("Error uploading image", e);
            throw new ErrorSubiendoImagenException("Error al subir la imagen: " + e.getMessage());
        }
    }

}