package com.ecommerce.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;

@Document("carts")
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class Cart {
  @Id private String id;
  private String userId;
  @Builder.Default
  private List<CartItem> items = new ArrayList<>();
  private String appliedCode;
  private Instant updatedAt;
}
