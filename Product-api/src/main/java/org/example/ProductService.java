package org.example;

import brave.Span;
import brave.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final Tracer tracer;

    public List<Product> getAll() {
        return repository.getAll();
    }

    public Product getById(int id) {
        Span span = tracer.nextSpan().name("service: get product by id").start();
        try(Tracer.SpanInScope ws = tracer.withSpanInScope(span)) {
            return repository.getById(id);
        } finally {
            span.finish();
        }

    }
}
