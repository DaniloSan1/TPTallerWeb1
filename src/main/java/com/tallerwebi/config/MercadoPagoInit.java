package com.tallerwebi.config;

import org.springframework.context.annotation.Configuration;
import javax.annotation.PostConstruct;
import com.mercadopago.MercadoPagoConfig; 
@Configuration
public class MercadoPagoInit {
    @PostConstruct
    public void init() {
        MercadoPagoConfig.setAccessToken("APP_USR-5362114551262779-102708-db1b598d857e89576dceb45d55c82cc4-2948293150");
    }
}