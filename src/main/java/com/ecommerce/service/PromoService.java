package com.ecommerce.service;

import com.ecommerce.entity.Promo;
import com.ecommerce.repo.PromoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

@Service @RequiredArgsConstructor
public class PromoService {
  private final PromoRepo repo;

  public Optional<Promo> validate(String code, BigDecimal subTotal, Instant now) {
    if (code == null || code.isBlank()) return Optional.empty();
    return repo.findById(code.trim().toUpperCase()).filter(p ->
        p.isActive() &&
        (now.equals(p.getStartDate()) || now.isAfter(p.getStartDate())) &&
        now.isBefore(p.getEndDate()) &&
        subTotal.compareTo(p.getMinOrderAmount()==null?BigDecimal.ZERO:p.getMinOrderAmount()) >= 0
    );
  }

  public BigDecimal discount(Promo p, BigDecimal subTotal) {
    if (p == null) return BigDecimal.ZERO;
    if ("PERCENT".equalsIgnoreCase(p.getType())) {
      return subTotal.multiply(p.getValue()).divide(BigDecimal.valueOf(100));
    }
    if ("FLAT".equalsIgnoreCase(p.getType())) {
      return p.getValue().min(subTotal);
    }
    return BigDecimal.ZERO;
  }
}
