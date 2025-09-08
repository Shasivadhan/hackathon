package com.ecommerce.entity;

import java.math.BigDecimal;
import lombok.*;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class CartItem {
  private String productId;
  private String name;
  private BigDecimal unitPrice;
  private Double carbonPerUnit;
  private int quantity;
}
