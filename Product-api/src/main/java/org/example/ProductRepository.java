package org.example;

import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public class ProductRepository {

    private List<Product> products = Arrays.asList(
            new Product(1, "socks", 25, 10, 0),
            new Product(2, "t-shirt", 100, 8, 0),
            new Product(3, "pants", 120, 12, 0)
    );

    public List<Product> getAll() {
        return products;
    }

    public Product getById(int id) {
        return products.stream().filter(product -> product.getId() == id)
                .findAny()
                .orElse(null);
    }

    public void modifyProduct(int id, Product newProduct) {
        Product product = products.stream().filter(p -> p.getId() == id)
                .findFirst().get();
        product.setReserved(newProduct.getReserved());
        product.setInStock(newProduct.getInStock());
    }
}
