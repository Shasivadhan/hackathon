package com.ecommerce.kafka;

import java.time.OffsetDateTime;

public record CartItemAddedEvent(
        String eventId,
        String userId,
        String productId,
        int quantity,
        OffsetDateTime occurredAt
) {}
