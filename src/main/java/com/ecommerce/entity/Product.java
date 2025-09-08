package com.ecommerce.entity;

import java.math.BigDecimal;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;

@Document("products")
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class Product {
  @Id private String id;
  private String name;
  private BigDecimal price;
  private Double carbonPerUnit; // grams CO2e
  private String category;
}
