package com.wirebraincoffee.productapifunctional.handler;

import com.wirebraincoffee.productapifunctional.model.Product;
import com.wirebraincoffee.productapifunctional.model.ProductEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.wirebraincoffee.productapifunctional.repository.ProductRepository;

import java.time.Duration;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
@RequiredArgsConstructor
public class ProductHandler {

    private final ProductRepository repository;

    // create handler function
    public Mono<ServerResponse> getAllProducts(ServerRequest request) {
        Flux<Product> allProducts = repository.findAll();

        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(allProducts, Product.class);


    }

    public Mono<ServerResponse> getProduct(ServerRequest request) {
        // we will extract path variable id from the request
        String id = request.pathVariable("id");
        Mono<Product> productFindById = repository.findById(id);

        Mono<ServerResponse> notFound = ServerResponse.notFound().build();     // building a not found response mono


        return productFindById
                .flatMap(product ->
                        ServerResponse.ok()
                                .contentType(APPLICATION_JSON)
                                .body(fromValue(product)))
                .switchIfEmpty(notFound);                             // fromValue is a static method from BodyInserters class which returns a body inserter to write an object
        // to the response  . Also it is an alternative of passing publisher and its type in the body method

        // here we are using flatmap because here we want to return another response in case of the id is not present in the database


        // we have previously used defaultIfEmpty()
        // both are same but
        // defaultIfEmpty() takes a default value , a simple object
        // whereas switchIfEmpty() takes a publisher implementations


    }

    public Mono<ServerResponse> saveProduct(ServerRequest request) {
        Mono<Product> saveProduct = request.bodyToMono(Product.class);

        return saveProduct
                .flatMap(product ->
                        ServerResponse.status(HttpStatus.CREATED)
                                .contentType(APPLICATION_JSON)
                                .body(repository.save(product), Product.class)    // saving the product in the body method
                );

    }

    // Update a product

    public Mono<ServerResponse> updateProduct(ServerRequest request) {
        String id = request.pathVariable("id");

        // This is form database
        Mono<Product> productById = repository.findById(id);

        // This is from client side
        Mono<Product> productToUpdate = request.bodyToMono(Product.class);

        // This is not found response which we build manually
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();


        // In the below line i am combining two monos and creating one object from them using zipWith mehtod
        return productToUpdate.zipWith(productById,
                        (product, existingProduct) ->
                                new Product(existingProduct.getId(), product.getName(), product.getPrice()))
                .flatMap(product ->
                        ServerResponse.ok()
                                .contentType(APPLICATION_JSON)
                                .body(product, Product.class))
                .switchIfEmpty(notFound);


    }

    public Mono<ServerResponse> deleteProduct(ServerRequest request) {
        String productId = request.pathVariable("id");

        Mono<Product> productById = repository.findById(productId);

        Mono<ServerResponse> notFoundRespose = ServerResponse.notFound().build();

        return productById
                .flatMap(product ->
                        ServerResponse.ok()
                                .build(repository.delete(product)))      // deleting and building response not using body because we are not returning  body here
                .switchIfEmpty(notFoundRespose);


    }

    public Mono<ServerResponse> deleteAllProducts(ServerRequest request) {
        return ServerResponse.ok()
                .build(repository.deleteAll());
    }

    public Mono<ServerResponse> getProductEvents(ServerRequest request) {

        Flux<ProductEvent> productEvent = Flux.interval(Duration.ofSeconds(1)).map(event ->
                new ProductEvent(event, "Product Event"));

        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(productEvent, ProductEvent.class);
    }


}
