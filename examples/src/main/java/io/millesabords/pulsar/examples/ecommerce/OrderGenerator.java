package io.millesabords.pulsar.examples.ecommerce;

import com.github.javafaker.Faker;
import io.millesabords.pulsar.examples.common.PulsarClientHelper;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.impl.schema.JSONSchema;

import java.io.IOException;
import java.util.Date;
import java.util.Random;

public class OrderGenerator {

    private static Faker faker = new Faker();

    private static Random r = new Random();

    public static void main(String[] args) throws IOException {

        final Producer<Order> producer = PulsarClientHelper.createBasicProducer(
                Config.get().getPulsarUrl(),
                Config.get().getAllOrdersTopic(),
                "orders-producer",
                JSONSchema.of(Order.class));

        for (int i=0; i<500; i++) {
            final Order order = newOrder();
            final MessageId msgId = producer.send(order);
            System.out.println("Message sent " + msgId + " -> " + order);

            try {
                Thread.sleep(Math.round(Math.random() * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        producer.close();
        PulsarClientHelper.closeClients();
    }

    private static Order newOrder() {

        Order order = new Order(
                faker.idNumber().valid(),
                new Date().getTime(),
                faker.internet().publicIpV4Address(),
                faker.food().ingredient(),
                faker.number().randomDouble(2, 1, 1000),
                r.nextInt(10) + 1,
                faker.internet().emailAddress());

        return order;
    }
}
