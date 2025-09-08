package com.ecommerce.dto;

import java.math.BigDecimal;
import java.util.List;
import com.ecommerce.entity.CartItem;
import lombok.Builder;
import lombok.Data;

@Data @Builder
public class CartResponse {
  private String cartId;
  private String userId;
  private List<CartItem> items;
  private String appliedCode;
  private BigDecimal subTotal;
  private BigDecimal discount;
  private BigDecimal total;
  private Double totalCarbon;
}
