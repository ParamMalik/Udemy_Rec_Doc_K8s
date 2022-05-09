package com.pluralsight.reactiveappwithcontroller.repository;

import com.pluralsight.reactiveappwithcontroller.model.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Service;

public interface ProductRepository extends ReactiveMongoRepository<Product, String> {

}
