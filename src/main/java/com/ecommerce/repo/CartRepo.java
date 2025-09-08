package com.ecommerce.repo;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.ecommerce.entity.Cart;

public interface CartRepo extends MongoRepository<Cart, String> {
  Optional<Cart> findByUserId(String userId);
}
