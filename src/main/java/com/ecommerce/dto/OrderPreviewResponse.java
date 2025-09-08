package com.ecommerce.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data @Builder
public class OrderPreviewResponse {
  private BigDecimal subTotal;
  private BigDecimal discount;
  private BigDecimal totalPayable;
  private Double totalCarbon;
  private String appliedCode;
  private List<String> productNames;
  private Instant at;
}
