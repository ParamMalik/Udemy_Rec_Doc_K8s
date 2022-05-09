package com.wirebraincoffee.productapifunctional;

import com.wirebraincoffee.productapifunctional.handler.ProductHandler;
import com.wirebraincoffee.productapifunctional.model.Product;
import com.wirebraincoffee.productapifunctional.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootApplication
public class ProductApiFunctionalApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductApiFunctionalApplication.class, args);
    }

    // Initialising database using CommandLineRunner
    @Bean
    CommandLineRunner init(ProductRepository repository) {
        // CLR is a functional interface so we this method can return a lambda expression
        return args -> {
            Flux<Product> productFlux = Flux.just(
                            new Product(null, "Latte", 1.26),
                            new Product(null, "Milk", 1.25))
                    .flatMap(repository::save);

            productFlux
                    .thenMany(repository.findAll())
                    .subscribe(System.out::println);
        };
    }

    // we will see two ways to define routes 1  chained routes 2 nested routes
    @Bean
    RouterFunction<ServerResponse> routes(ProductHandler handler) {

        // here we are giving our paths and calling handler methods
//        return route()
//                .GET("/products/events", accept(TEXT_EVENT_STREAM), handler::getProductEvents)
//                .GET("/products/{id}", accept(APPLICATION_JSON), handler::getProduct)
//                .GET("/products", accept(APPLICATION_JSON), handler::getAllProducts)
//                .PUT("/products/{id}", accept(APPLICATION_JSON), handler::updateProduct)
//                .POST("/products", contentType(APPLICATION_JSON), handler::saveProduct)
//                .DELETE("/products/{id}", accept(APPLICATION_JSON), handler::deleteProduct)
//                .DELETE("/products", accept(APPLICATION_JSON), handler::getAllProducts)
//                .build();


        // NESTED WAY OF WRITING ROUTER FUNCTION
        return route()
                .path("/products", builder -> builder.nest(accept(APPLICATION_JSON).or(contentType(APPLICATION_JSON)).or(accept(TEXT_EVENT_STREAM)),
                                        nestedBuilder -> nestedBuilder
                                                .GET("/events", handler::getProductEvents)
                                                .GET("/{id}", handler::getProduct)
                                                .GET(handler::getAllProducts)
                                                .PUT("/{id}", handler::updateProduct)
                                                .POST("/products", handler::saveProduct)

                                )
                                .DELETE("/{id}", handler::deleteProduct)              // Delete do not need content type so we are placing then outside
                                .DELETE(handler::deleteAllProducts)
                ).build();


    }

}


