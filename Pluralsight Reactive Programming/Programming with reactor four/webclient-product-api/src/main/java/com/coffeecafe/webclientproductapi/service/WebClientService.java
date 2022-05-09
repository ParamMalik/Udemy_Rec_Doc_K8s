package com.coffeecafe.webclientproductapi.service;

import com.coffeecafe.webclientproductapi.config.WebClientConfig;
import com.coffeecafe.webclientproductapi.model.Product;
import com.coffeecafe.webclientproductapi.model.ProductEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WebClientService {

    private final WebClientConfig webClient;

    public Mono<ResponseEntity<Product>> postProduct() {
        return webClient.getWebClient()
                .post()
                .body(Mono.just(new Product(null, "Milk", 1.25)), Product.class)
                .exchangeToMono(clientResponse -> clientResponse.toEntity(Product.class))
                .doOnSuccess(productResponseEntity -> System.out.println("POST : " + productResponseEntity));
    }

    public Flux<Product> getAllProduct() {
        return webClient.getWebClient()
                .get()
                .retrieve()
                .bodyToFlux(Product.class)
                .doOnNext(product -> System.out.println("== GET == :" + product));
    }

    public Mono<Product> getProduct(String id) {
        return webClient.getWebClient()
                .get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnSuccess(product -> System.out.println(" GET ONE : " + product));
    }

    public Mono<Product> updateProduct(String id, String name, Double price) {
        return webClient.getWebClient()
                .put()
                .uri("/{id}", id)
                .body(Mono.just(new Product(null, name, price)), Product.class)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnSuccess(product -> System.out.println("PUT :" + product));
    }


    public Mono<Void> deleteProduct(String id) {
        return webClient.getWebClient()
                .delete()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnSuccess(del -> System.out.println("DELETE :" + del));
    }

    public Flux<ProductEvent> getEvents() {
        return webClient.getWebClient()
                .get()
                .uri("/events")
                .exchangeToFlux(clientResponse -> clientResponse.bodyToFlux(ProductEvent.class));



        /*
        Or Instead Of exchangeToFlux() i can use the below methods

        .retrieve()
        .bodyToFlux(Product.class)
         */
    }

}
