package com.ecommerce.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.ecommerce.entity.Promo;

public interface PromoRepo extends MongoRepository<Promo, String> {}
