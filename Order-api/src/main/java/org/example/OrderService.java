package org.example;

import brave.Span;
import brave.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.example.rabbitmq.MessageSender;
import org.example.rabbitmq.OrderMessage;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repo;
    private final MessageSender messageSender;
    private final Tracer tracer;
    private final WebClient.Builder webClientBuilder;
    private final String baseUrl = "http://productapi:8081/";

    public List<Order> getAll() {
        Span span = tracer.nextSpan().name("service: get all").start();
        try (Tracer.SpanInScope scope = tracer.withSpanInScope(span)){
            return repo.getAll();
        } finally {
            span.finish();
        }
    }

    public ResponseEntity addOrder(int productId, int quantity) {
        Span span = tracer.nextSpan().name("service: fetch product").start();
        try(Tracer.SpanInScope ws = tracer.withSpanInScope(span)) {
            Product product = fetchProductForOrder(productId);
            if(product.getInStock() - product.getReserved() < quantity)
                return new ResponseEntity("There are only " + (product.getInStock() - product.getReserved()) + " " + product.getName() + " available", HttpStatus.NOT_ACCEPTABLE);
            else {
                int total = product.getPrice() * quantity;
                messageSender.sendOrderCreatedMessage(new OrderMessage(productId, quantity));
                return new ResponseEntity(repo.addOrder(productId, quantity, total), HttpStatus.OK);
            }
        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND)
                return new ResponseEntity("Product does not exist.", HttpStatus.NOT_FOUND);
        } finally {
            span.finish();
        }
        return null;
    }

    public Order completeOrder(int orderId) {
        Span span = tracer.nextSpan().name("service: complete").start();
        try (Tracer.SpanInScope scope = tracer.withSpanInScope(span)) {
            Order order = repo.completeOrder(orderId);
            messageSender.sendOrderCompletedMessage(new OrderMessage(order.getProductId(), order.getQuantity()));
            return order;
        } finally {
            span.finish();
        }
    }

    private Product fetchProductForOrder(int productId) {
        var webClient = webClientBuilder
                .baseUrl(baseUrl)
                .build();

        try {
            Product product = webClient.get()
                    .uri("api/product/" + productId)
                    .retrieve()
                    .bodyToMono(Product.class).single().block();
            return product;
        } catch (WebClientResponseException e) {
            throw e;
        }
    }
}
