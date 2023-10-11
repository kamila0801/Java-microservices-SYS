package org.example;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @GetMapping("/getAll")
    public List<Product> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable("id") int id, @RequestHeader HttpHeaders headers) {
        Product product = service.getById(id);
        if(product == null) return new ResponseEntity<>("product not found", HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(product, HttpStatus.OK);
    }
}
