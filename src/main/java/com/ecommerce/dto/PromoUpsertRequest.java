package com.ecommerce.dto;

import java.math.BigDecimal;
import java.time.Instant;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PromoUpsertRequest {
  @NotBlank private String code;
  @NotBlank @Pattern(regexp = "PERCENT|FLAT", flags = Pattern.Flag.CASE_INSENSITIVE,
           message = "type must be PERCENT or FLAT")
  private String type;
  @NotNull @DecimalMin(value="0.0", inclusive=false) private BigDecimal value;
  @DecimalMin(value="0.0", inclusive=true) private BigDecimal minOrderAmount = BigDecimal.ZERO;
  @NotNull private Instant startDate;
  @NotNull private Instant endDate;
  private boolean active = true;
}
