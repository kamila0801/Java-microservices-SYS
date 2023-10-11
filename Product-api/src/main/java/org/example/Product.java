package org.example;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Product {
    private int id;
    private String name;
    private int price;
    private int inStock;
    private int reserved;
}
