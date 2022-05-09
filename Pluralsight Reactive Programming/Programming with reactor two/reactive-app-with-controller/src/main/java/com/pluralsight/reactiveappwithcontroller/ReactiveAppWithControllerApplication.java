package com.pluralsight.reactiveappwithcontroller;

import com.pluralsight.reactiveappwithcontroller.model.Product;
import com.pluralsight.reactiveappwithcontroller.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class ReactiveAppWithControllerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactiveAppWithControllerApplication.class, args);
    }

    @Bean
    CommandLineRunner init( ProductRepository repository /* , ReactiveMongoOperations operations <- This we only do when we want to perform some operations on mongodb */) {
        return args -> {
            Flux<Product> productFlux = Flux.just(
                    new Product("Big Latte", 2.99),
                    new Product("Big Decaf", 2.49),
                    new Product( "Green Tea", 1.99)
            ).flatMap(repository::save);


            // To Check if products are saved in database
            // Not using flatMap here because i need to be sure that data is getting save before findAll query gets run
            // Then does the same thing - then() allows the first publisher to complete and then execute the publisher it receives as an argument
            // The difference between then and thenMany is that the then takes either no parameter or a mono
            // And thenMany takes any publisher ie flux or mono

            productFlux
                    .thenMany(repository.findAll())    // thenMany let the first publisher run first
                    .subscribe(System.out::println);


            /*
            // This we would be using in real monogdb not in embedded

            operations.collectionExists(Product.class)
                    .flatMap(exists -> exists? operations.dropCollection(Product.class): Mono.just(exists))    // if collection exists drop it
                    .thenMany(v -> operations.createCollection(Product.class))                                  // then create new collection
                    .thenMany(productFlux)                                                                    // then save the product
                    .thenMany(repository.findAll())                                                           // then find the products is saved or not
                    .subscribe(System.out::println);                                                            // and subscribe to this workflow

             */
        };
    }
}
