package com.ecommerce.controller;

import com.ecommerce.dto.AddToCartRequest;
import com.ecommerce.dto.ApplyCodeRequest;
import com.ecommerce.dto.CartResponse;
import com.ecommerce.security.CurrentUserService;
import com.ecommerce.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "Few endpoints for UC-02")
public class CartController {
    private final CartService svc;
    private final CurrentUserService current;

    @Operation(summary = "Add item to cart")
    @PostMapping("/items")
    public CartResponse add(@Valid @RequestBody AddToCartRequest req) {
        return svc.addItem(current.id(), req);
    }

    @Operation(summary = "Apply discount code")
    @PostMapping("/apply-code")
    public CartResponse apply(@Valid @RequestBody ApplyCodeRequest req) {
        return svc.applyCode(current.id(), req.getCode());
    }

    @Operation(summary = "View current cart")
    @GetMapping
    public CartResponse get() {
        return svc.view(current.id());
    }
}
