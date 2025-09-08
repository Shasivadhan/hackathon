package com.ecommerce.feign;

import com.ecommerce.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", url = "${product.service.url:http://localhost:8080}")
public interface ProductClient {
  @GetMapping("/api/products/{id}")
  ProductDTO getById(@PathVariable("id") String id);
}
