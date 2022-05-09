package com.pluralsight.reactiveappwithcontroller.controller;

import com.pluralsight.reactiveappwithcontroller.model.Product;
import com.pluralsight.reactiveappwithcontroller.model.ProductEvent;
import com.pluralsight.reactiveappwithcontroller.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductControllerTest {

    private WebTestClient webTestClient;

    private List<Product> expectedList;

    // I have to autowire the repository
    @Autowired
    private ProductRepository repository;

    @BeforeEach
    void setUp() {
        // I can also autowire the controller and pass the repo to it
        this.webTestClient = WebTestClient
                .bindToController(new ProductController(repository))
                .configureClient()
                .baseUrl("/products")
                .build();

        this.expectedList = repository.findAll().collectList().block();
    }

    @Test
    void getAllProducts() {
        webTestClient.get()
                .uri("/")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Product.class)
                .isEqualTo(expectedList);
    }

    @Test
    void testProductByIdIfNotFound() {
        webTestClient.get()
                .uri("/abc")
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void getProductById() {
        Product productAtFirstIndex = expectedList.get(0);

        webTestClient.get()
                .uri("/{id}", productAtFirstIndex.getId())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Product.class)
                .isEqualTo(productAtFirstIndex);

    }

    @Test
    void saveProduct() {
        Mono<Product> productChai = Mono.just(new Product(null, "chai", 1.25));

        webTestClient.post()
                .uri("/")
                .body(productChai, Product.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(Product.class);

    }


    @Test
    void getProductEvents() {

        ProductEvent expectedEvent = new ProductEvent(0L, "Product Event");

        FluxExchangeResult<ProductEvent> productEventFluxExchangeResult = webTestClient.get().uri("/events")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .returnResult(ProductEvent.class);

        StepVerifier.create(productEventFluxExchangeResult.getResponseBody())
                .expectNext(expectedEvent)
                .expectNextCount(2)
                .consumeNextWith(productEvent -> assertEquals(Long.valueOf(3), productEvent.getEventId()))
                .thenCancel()
                .verify();


    }
}