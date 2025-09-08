package com.ecommerce.entity;

import java.math.BigDecimal;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;

@Document("promos")
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class Promo {
  @Id private String code;            // UPPERCASE
  private String type;                // PERCENT | FLAT
  private BigDecimal value;
  private BigDecimal minOrderAmount;
  private Instant startDate;
  private Instant endDate;
  private boolean active;
}
