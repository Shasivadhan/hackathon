package com.ecommerce.controller;

import com.ecommerce.dto.ProductDTO;
import com.ecommerce.dto.ProductUpsertRequest;
import com.ecommerce.entity.Product;
import com.ecommerce.exception.BadRequestException;
import com.ecommerce.exception.NotFoundException;
import com.ecommerce.repo.ProductRepo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Minimal product endpoints")
public class ProductController {
  private final ProductRepo repo;

  @Operation(summary = "Create product")
  @PostMapping @ResponseStatus(HttpStatus.CREATED)
  public Product create(@Valid @RequestBody ProductUpsertRequest r) {
    if (repo.existsById(r.getId())) throw new BadRequestException("Product exists: " + r.getId());
    Product p = Product.builder().id(r.getId()).name(r.getName())
        .price(r.getPrice()).carbonPerUnit(r.getCarbonPerUnit()).category(r.getCategory()).build();
    return repo.save(p);
  }

  @Operation(summary = "Get product by id")
  @GetMapping("/{id}")
  public ProductDTO get(@PathVariable String id) {
    Product p = repo.findById(id).orElseThrow(() -> new NotFoundException("Product not found: " + id));
    ProductDTO dto = new ProductDTO();
    dto.setId(p.getId()); dto.setName(p.getName()); dto.setPrice(p.getPrice()); dto.setCarbonPerUnit(p.getCarbonPerUnit());
    return dto;
  }
}
