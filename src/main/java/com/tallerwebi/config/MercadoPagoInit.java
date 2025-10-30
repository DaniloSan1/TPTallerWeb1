package com.tallerwebi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import com.mercadopago.MercadoPagoConfig;

@Configuration
@PropertySource("classpath:application.properties")
public class MercadoPagoInit {

    @Value("${mercadopago.access.token}")
    private String accessToken;

    @Bean
    public String init() {
        MercadoPagoConfig.setAccessToken(accessToken);
        System.out.println("MercadoPago initialized with access token: " + accessToken);
        return "MercadoPago_Init";
    }
}