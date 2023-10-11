package org.example.rabbitmq;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

@Builder
@Value
public class OrderMessage implements Serializable {
    private int productId;
    private int quantity;

    @JsonCreator
    public OrderMessage(@JsonProperty("productId") int productId, @JsonProperty("quantity") int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}
