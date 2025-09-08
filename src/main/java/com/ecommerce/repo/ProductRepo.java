package com.ecommerce.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.ecommerce.entity.Product;

public interface ProductRepo extends MongoRepository<Product, String> {}
