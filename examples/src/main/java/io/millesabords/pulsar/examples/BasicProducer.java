package io.millesabords.pulsar.examples;

import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;

public class BasicProducer {

    public static void main(String[] args) throws PulsarClientException {

        PulsarClient client = PulsarClient.builder()
                .serviceUrl("pulsar://localhost:6650")
                .build();

        Producer<byte[]> producer = client.newProducer()
                .topic("my-topic-2")
                .create();

        producer.send("Hello !".getBytes());

        producer.close();
        client.close();

    }
}
