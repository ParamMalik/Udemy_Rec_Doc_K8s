package com.proctice.springmongoapp.service;

import com.proctice.springmongoapp.model.Product;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface ProductService {

    Flux<Product> getAllProducts();

    Mono<Product> getProduct(String id);

    Mono<Product> saveProduct(Product product);
}
