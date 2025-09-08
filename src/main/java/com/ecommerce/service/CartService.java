package com.ecommerce.service;

import com.ecommerce.dto.AddToCartRequest;
import com.ecommerce.dto.CartResponse;
import com.ecommerce.dto.OrderPreviewResponse;
import com.ecommerce.dto.ProductDTO;
import com.ecommerce.entity.Cart;
import com.ecommerce.entity.CartItem;
import com.ecommerce.entity.Promo;
import com.ecommerce.exception.BadRequestException;
import com.ecommerce.exception.NotFoundException;
import com.ecommerce.feign.ProductClient;
import com.ecommerce.repo.CartRepo;
import com.ecommerce.repo.PromoRepo;
import com.ecommerce.kafka.CartEventPublisher;   // <-- added

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepo carts;
    private final PromoRepo promos;
    private final ProductClient products;
    private final PromoService promoService;
    private final CartEventPublisher eventPublisher;   // <-- added

    public Cart getOrCreate(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new BadRequestException("userId is required");
        }
        return carts.findByUserId(userId).orElseGet(() -> carts.save(
                Cart.builder().userId(userId).updatedAt(Instant.now()).build()));
    }

    public CartResponse addItem(String userId, AddToCartRequest req) {
        if (req.getQuantity() <= 0) throw new BadRequestException("quantity must be > 0");
        Cart cart = getOrCreate(userId);

        ProductDTO p = products.getById(req.getProductId());
        if (p == null || p.getId() == null) throw new NotFoundException("product not found: " + req.getProductId());

        Optional<CartItem> existing = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(p.getId())).findFirst();

        if (existing.isPresent()) {
            CartItem it = existing.get();
            it.setQuantity(it.getQuantity() + req.getQuantity());
            it.setUnitPrice(p.getPrice());
            it.setCarbonPerUnit(p.getCarbonPerUnit());
        } else {
            cart.getItems().add(CartItem.builder()
                    .productId(p.getId()).name(p.getName())
                    .unitPrice(p.getPrice()).carbonPerUnit(p.getCarbonPerUnit())
                    .quantity(req.getQuantity()).build());
        }

        cart.setUpdatedAt(Instant.now());
        carts.save(cart);

        // --- publish Kafka event (non-blocking; won't break API if broker down)
        try {
            eventPublisher.publishItemAdded(userId, p.getId(), req.getQuantity());
        } catch (Exception ignore) {
            // optionally log: System.err.println("Kafka publish failed: " + ignore.getMessage());
        }
        // ---

        return toResponse(cart, resolveAppliedPromo(cart));
    }

    public CartResponse applyCode(String userId, String code) {
        Cart cart = getOrCreate(userId);
        Promo promo = promoService.validate(code, subTotal(cart), Instant.now())
                .orElseThrow(() -> new BadRequestException("Invalid or ineligible promo code"));
        cart.setAppliedCode(promo.getCode());
        cart.setUpdatedAt(Instant.now());
        carts.save(cart);
        return toResponse(cart, promo);
    }

    public OrderPreviewResponse preview(String userId) {
        Cart cart = getOrCreate(userId);
        Promo promo = resolveAppliedPromo(cart);
        BigDecimal sub = subTotal(cart);
        BigDecimal disc = promoService.discount(promo, sub);
        BigDecimal tot = sub.subtract(disc).max(BigDecimal.ZERO);
        return OrderPreviewResponse.builder()
                .subTotal(sub).discount(disc).totalPayable(tot)
                .totalCarbon(totalCarbon(cart)).appliedCode(promo == null ? null : promo.getCode())
                .productNames(cart.getItems().stream().map(CartItem::getName).toList())
                .at(Instant.now()).build();
    }

    public CartResponse view(String userId) {
        Cart cart = getOrCreate(userId);
        return toResponse(cart, resolveAppliedPromo(cart));
    }

    private BigDecimal subTotal(Cart cart) {
        return cart.getItems().stream()
                .map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private double totalCarbon(Cart cart) {
        return cart.getItems().stream()
                .mapToDouble(i -> (i.getCarbonPerUnit() == null ? 0.0 : i.getCarbonPerUnit()) * i.getQuantity())
                .sum();
    }

    private Promo resolveAppliedPromo(Cart cart) {
        if (cart.getAppliedCode() == null) return null;
        return promos.findById(cart.getAppliedCode()).orElse(null);
    }

    private CartResponse toResponse(Cart cart, Promo promo) {
        BigDecimal sub = subTotal(cart);
        BigDecimal disc = promoService.discount(promo, sub);
        BigDecimal tot = sub.subtract(disc).max(BigDecimal.ZERO);
        return CartResponse.builder()
                .cartId(cart.getId()).userId(cart.getUserId()).items(cart.getItems())
                .appliedCode(cart.getAppliedCode()).subTotal(sub).discount(disc)
                .total(tot).totalCarbon(totalCarbon(cart)).build();
    }
}
