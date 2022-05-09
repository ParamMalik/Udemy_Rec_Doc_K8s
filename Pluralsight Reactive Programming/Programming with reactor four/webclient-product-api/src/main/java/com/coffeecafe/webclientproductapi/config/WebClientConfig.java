package com.coffeecafe.webclientproductapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class WebClientConfig {

    @Bean
    public WebClient getWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8080/products")
                .build();
    }
}
