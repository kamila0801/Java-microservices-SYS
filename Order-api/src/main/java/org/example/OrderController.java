package org.example;

import brave.Span;
import brave.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;
    private final Tracer tracer;

    @GetMapping("/getAll")
    public List<Order> getAllOrders() {
        log.info("Getting all orders");
        Span span = tracer.nextSpan().name("controller: get all").start();
        try (Tracer.SpanInScope scope = tracer.withSpanInScope(span)) {
            return service.getAll();
        } finally {
            span.finish();
        }
    }

    @PostMapping("/create")
    public ResponseEntity createOrder(@RequestParam("productId") int productId, @RequestParam("quantity") int quantity) {
        log.info("Creating new order for product id: " + productId);
        Span span = tracer.nextSpan().name("controller: create").start();
        try (Tracer.SpanInScope scope = tracer.withSpanInScope(span)) {
            return service.addOrder(productId, quantity);
        } finally {
            span.finish();
        }

    }

    @PostMapping("/complete/{id}")
    public ResponseEntity completeOrder(@PathVariable("id") int id) {
        log.info("Completing order: id " + id);
        Span span = tracer.nextSpan().name("controller: complete").start();
        try (Tracer.SpanInScope scope = tracer.withSpanInScope(span)) {
            return ResponseEntity.ok(service.completeOrder(id));
        } finally {
            span.finish();
        }
    }
}
