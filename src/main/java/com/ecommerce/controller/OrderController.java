package com.ecommerce.controller;

import com.ecommerce.dto.OrderPreviewResponse;
import com.ecommerce.security.CurrentUserService;
import com.ecommerce.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Order preview only")
public class OrderController {
    private final CartService svc;
    private final CurrentUserService current;

    @Operation(summary = "Preview order (subtotal, discount, total, carbon)")
    @PostMapping("/preview")
    public OrderPreviewResponse preview() {
        return svc.preview(current.id());
    }
}
