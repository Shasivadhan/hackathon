package com.ecommerce.kafka;

import java.time.OffsetDateTime;
import java.util.UUID;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CartEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(CartEventPublisher.class);

    // Use Object to match Spring Boot's default KafkaTemplate<Object,Object>
    private final KafkaTemplate<String, Object> template;

    @Value("${topics.cartItemAdded}")
    private String topic;

    // Toggle producing on/off without code changes (default true if property absent)
    private final boolean enabled;

    public CartEventPublisher(
            KafkaTemplate<String, Object> template,
            @Value("${kafka.enabled:true}") boolean enabled) {
        this.template = template;
        this.enabled = enabled;
    }

    public void publishItemAdded(String userId, String productId, int qty) {
        if (!enabled) {
            log.debug("Kafka disabled (kafka.enabled=false); skipping publish for userId={}, productId={}", userId, productId);
            return;
        }

        var evt = new CartItemAddedEvent(
                UUID.randomUUID().toString(), userId, productId, qty, OffsetDateTime.now());

        try {
            var future = template.send(topic, userId, evt); // key=userId keeps per-user order

            future.whenComplete((res, ex) -> {
                if (ex != null) {
                    log.warn("Kafka send FAILED to topic {} for userId={}, productId={}, qty={}: {}",
                            topic, userId, productId, qty, ex.toString());
                } else if (res != null) {
                    RecordMetadata md = res.getRecordMetadata();
                    log.info("Kafka send OK: topic={} partition={} offset={} key={}",
                            md.topic(), md.partition(), md.offset(), userId);
                }
            });

        } catch (Exception e) {
            // Keep API responsive even if broker is down or topic missing
            log.warn("Kafka publish exception (non-blocking) for userId={}, productId={}, qty={}: {}",
                    userId, productId, qty, e.toString());
        }
    }
}
