package io.millesabords.pulsar.examples.ecommerce;

import io.millesabords.pulsar.examples.common.PulsarClientHelper;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.SubscriptionType;
import org.apache.pulsar.client.impl.schema.JSONSchema;

import java.util.concurrent.TimeUnit;

public class OrderConsumer {

    public static void main(String[] args) throws PulsarClientException {

        Consumer<Order> consumer = null;

        try {
            consumer = PulsarClientHelper.createConsumer(
                    "pulsar://localhost:6650",
                    "talk/demo/orders-all",
                    "demo-exclusive-subscription",
                    SubscriptionType.Exclusive,
                    JSONSchema.of(Order.class));

            boolean stopped = false;

            do {
                Message<Order> msg = consumer.receive(1000, TimeUnit.SECONDS);

                stopped = msg == null;

                if (!stopped) {
                    Order order = msg.getValue();
                    System.out.println(order);
                    consumer.acknowledge(msg);
                }
            }
            while (!stopped);
        }
        finally {
            if (consumer != null) {
                consumer.close();
            }
            PulsarClientHelper.closeClients();
        }
    }
}
