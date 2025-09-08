package com.ecommerce.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ProductDTO {
  private String id;
  private String name;
  private BigDecimal price;
  private Double carbonPerUnit;
}
