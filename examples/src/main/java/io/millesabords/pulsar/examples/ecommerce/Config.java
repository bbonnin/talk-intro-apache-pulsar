package io.millesabords.pulsar.examples.ecommerce;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Config {

    private String pulsarUrl;

    private String allOrdersTopic;

    private String usOrdersTopic;

    private String ordersButUsTopic;


    public static Config get() {
        return Config.builder()
                .pulsarUrl("pulsar://pulsar:6650")
                .allOrdersTopic("demo/ecommerce/orders-all")
                .usOrdersTopic("demo/ecommerce/orders-us")
                .ordersButUsTopic("demo/ecommerce/orders-but-us")
                .build();
    }
}
