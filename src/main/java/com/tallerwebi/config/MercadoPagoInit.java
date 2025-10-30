package com.tallerwebi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.mercadopago.MercadoPagoConfig; 
@Configuration
public class MercadoPagoInit {
    @Bean
    public String init() {
        MercadoPagoConfig.setAccessToken("APP_USR-5362114551262779-102708-db1b598d857e89576dceb45d55c82cc4-2948293150");
        return "MercadoPago_Init";
    }
}