package org.example.rabbitmq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class MessageSender {

    private final RabbitTemplate rabbitTemplate;

    public void sendOrderCreatedMessage(OrderMessage message) {
        rabbitTemplate.convertAndSend("q.order-created", message);
        log.info("Message sent - order created for product id: " + message.getProductId());
    }

    public void sendOrderCompletedMessage(OrderMessage message) {
        rabbitTemplate.convertAndSend("q.order-completed", message);
        log.info("Message sent - order completed for product id: " + message.getProductId());
    }
}
