package com.proctice.springmongoapp.service.impl;

import com.proctice.springmongoapp.model.Product;
import com.proctice.springmongoapp.repository.ProductRepository;
import com.proctice.springmongoapp.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Flux<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Mono<Product> getProduct(String id) {
        return productRepository.findById(id);
    }

    @Override
    public Mono<Product> saveProduct(Product product) {
        return productRepository.save(product);
    }
}
