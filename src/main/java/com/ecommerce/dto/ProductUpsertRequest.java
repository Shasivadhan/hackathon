package com.ecommerce.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductUpsertRequest {
  @NotBlank private String id;
  @NotBlank private String name;
  @NotNull @DecimalMin(value="0.0", inclusive=false) private BigDecimal price;
  @NotNull @DecimalMin(value="0.0", inclusive=true) private Double carbonPerUnit;
  private String category;
}
