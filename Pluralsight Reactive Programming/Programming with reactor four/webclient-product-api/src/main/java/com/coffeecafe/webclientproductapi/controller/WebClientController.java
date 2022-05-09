package com.coffeecafe.webclientproductapi.controller;

import com.coffeecafe.webclientproductapi.model.Product;
import com.coffeecafe.webclientproductapi.model.ProductEvent;
import com.coffeecafe.webclientproductapi.service.WebClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class WebClientController {

    private final WebClientService webClientService;

    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    private Flux<ProductEvent> getEvents(){
        return webClientService.getEvents();
    }

    @GetMapping
    private Flux<Product> getAllProducts(){
        return webClientService.getAllProduct();
    }

}
