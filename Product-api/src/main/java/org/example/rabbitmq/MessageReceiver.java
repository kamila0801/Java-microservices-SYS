package org.example.rabbitmq;

import brave.Span;
import brave.Tracer;
import io.micrometer.tracing.annotation.NewSpan;
import io.micrometer.tracing.annotation.SpanTag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.Product;
import org.example.ProductRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageReceiver {

    private final ProductRepository repository;

    @RabbitListener(queues = {"q.order-created"})
    public void processMessageOrderCreated(@SpanTag("order.created") OrderMessage message) {
        Product product = repository.getById(message.getProductId());
        product.setReserved(product.getReserved() + message.getQuantity());
        repository.modifyProduct(message.getProductId(), product);
        log.info("Message received - order created for product id: " + message.getProductId());
    }

    @RabbitListener(queues = {"q.order-completed"})
    public void processMessageOrderCompleted(OrderMessage message) {
        Product product = repository.getById(message.getProductId());
        product.setReserved(product.getReserved() - message.getQuantity());
        product.setInStock(product.getInStock() - message.getQuantity());
        repository.modifyProduct(message.getProductId(), product);
        log.info("Message received - order completed for product id: " + message.getProductId());
    }
}
