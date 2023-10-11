package org.example;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Order {
    private int id;
    private int productId;
    private int quantity;
    private int total;
    private OrderStatus status;
}
