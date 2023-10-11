package org.example;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Product {
    private int id;
    private String name;
    private int price;
    private int inStock;
    private int reserved;

    @JsonCreator
    public Product(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("price") int price,
                   @JsonProperty("inStock") int inStock, @JsonProperty("reserved") int reserved) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.inStock = inStock;
        this.reserved = reserved;
    }
}
