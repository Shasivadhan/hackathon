package com.ecommerce.controller;

import com.ecommerce.entity.Promo;
import com.ecommerce.dto.PromoUpsertRequest;
import com.ecommerce.exception.BadRequestException;
import com.ecommerce.exception.NotFoundException;
import com.ecommerce.repo.PromoRepo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/promos")
@RequiredArgsConstructor
@Tag(name = "Promos", description = "Create and manage promotion codes")
public class PromoController {
  private final PromoRepo repo;

  @Operation(summary = "Create promo")
  @PostMapping @ResponseStatus(HttpStatus.CREATED)
  public Promo create(@Valid @RequestBody PromoUpsertRequest req) {
    String code = req.getCode().trim().toUpperCase();
    if (repo.existsById(code)) throw new BadRequestException("Promo exists: " + code);
    if (req.getEndDate().isBefore(req.getStartDate())) throw new BadRequestException("endDate must be after startDate");
    Promo p = Promo.builder().code(code).type(req.getType().trim().toUpperCase())
        .value(req.getValue()).minOrderAmount(req.getMinOrderAmount())
        .startDate(req.getStartDate()).endDate(req.getEndDate()).active(req.isActive()).build();
    return repo.save(p);
  }

  @Operation(summary = "Get promo by code")
  @GetMapping("/{code}")
  public Promo get(@PathVariable String code) {
    return repo.findById(code.trim().toUpperCase())
        .orElseThrow(() -> new NotFoundException("Promo not found: " + code));
  }

  @Operation(summary = "Delete promo by code")
  @DeleteMapping("/{code}") @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable String code) {
    String k = code.trim().toUpperCase();
    if (!repo.existsById(k)) throw new NotFoundException("Promo not found: " + code);
    repo.deleteById(k);
  }
}
