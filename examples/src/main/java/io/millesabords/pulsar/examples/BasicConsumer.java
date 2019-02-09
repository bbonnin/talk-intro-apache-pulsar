package io.millesabords.pulsar.examples;

import org.apache.pulsar.client.api.*;

import java.util.concurrent.TimeUnit;

public class BasicConsumer {

    public static void main(String[] args) throws PulsarClientException {

        PulsarClient client = PulsarClient.builder()
                .serviceUrl("pulsar://localhost:6650")
                .build();

        Consumer consumer = client.newConsumer()
                .topic("my-topic")
                .subscriptionName("my-subscription")
                .subscribe();

        consumer.seek(MessageId.earliest);

        boolean stopped = false;

        do {
            Message msg = consumer.receive(1, TimeUnit.SECONDS);

            stopped = msg == null;

            if (!stopped) {
                System.out.printf("Message received: %s", new String(msg.getData()));

                consumer.acknowledge(msg);
            }
        }
        while (!stopped);

        consumer.close();
        client.close();
    }
}
