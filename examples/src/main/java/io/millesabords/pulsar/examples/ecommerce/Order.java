package io.millesabords.pulsar.examples.ecommerce;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private String id;

    private long orderDate;

    private String ipAddress;

    private String productId;

    private double amount;

    private int quantity;

    private String email;

}
