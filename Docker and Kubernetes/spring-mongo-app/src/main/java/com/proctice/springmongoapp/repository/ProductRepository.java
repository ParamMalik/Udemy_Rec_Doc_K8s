package com.proctice.springmongoapp.repository;

import com.proctice.springmongoapp.model.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
}
