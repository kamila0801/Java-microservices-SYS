package org.example;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderRepository {
    private List<Order> orders = new ArrayList<>();

    public List<Order> getAll() {
        return orders;
    }

    public Order addOrder(int productId, int quantity, int total) {
        Order newOrder =
                Order.builder()
                        .id(orders.size() + 1)
                        .productId(productId)
                        .quantity(quantity)
                        .status(OrderStatus.CREATED)
                        .total(total).build();
        orders.add(newOrder);
        return newOrder;
    }

    public Order completeOrder(int orderId) {
        Order order = orders.stream().filter(o -> o.getId() == orderId)
                .findFirst().get();
        order.setStatus(OrderStatus.COMPLETED);
        return order;
    }
}
