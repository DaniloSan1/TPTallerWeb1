package com.tallerwebi.dominio;

import org.springframework.web.multipart.MultipartFile;

public interface AdaptadorAWSS3 {
    /**
     * Uploads a file to the configured S3 bucket and returns the public URL.
     * 
     * @param file The multipart file to upload.
     * @return The public URL of the uploaded file, or null if upload fails.
     * @throws Exception If an error occurs during upload.
     */
    String subirArchivo(MultipartFile file) throws Exception;
}