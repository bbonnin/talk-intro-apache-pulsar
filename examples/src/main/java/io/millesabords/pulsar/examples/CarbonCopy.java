package io.millesabords.pulsar.examples;

import org.apache.pulsar.client.api.*;

import java.util.concurrent.TimeUnit;

public class CarbonCopy {

    public void createProducer() throws PulsarClientException {

        PulsarClient client = PulsarClient.builder()
                .serviceUrl("pulsar://myhostname:6650")
                .build();

        Producer<byte[]> producer = client.newProducer()
                .topic("demo-topic")
                .producerName("demo-producer")
                .create();

        MessageId msgId = producer.send("Hello Devoxx !".getBytes());

    }

    public void createConsumer() throws PulsarClientException {

        PulsarClient client = PulsarClient.builder()
                .serviceUrl("pulsar://myhostname:6650")
                .build();

        Consumer consumer = client.newConsumer()
                .topic("demo-topic")
                .subscriptionName("demo-exclusive-sub")
                .subscriptionType(SubscriptionType.Exclusive)
                .subscribe();

        // Lecture à partir du premier message non acquitté
        Message msg = consumer.receive(1000, TimeUnit.SECONDS);

        System.out.printf("Message received: %s, from %s with id=%s\n",
                new String(msg.getData()),
                msg.getProducerName(),
                msg.getMessageId());

        consumer.acknowledge(msg);

    }

    public void createReader() throws PulsarClientException {

        PulsarClient client = PulsarClient.builder()
                .serviceUrl("pulsar://myhostname:6650")
                .build();

        Reader reader = client.newReader()
                .topic("demo-topic")
                .startMessageId(MessageId.latest)
                    // Peut aussi être earliest ou un id de message
                .create();

        Message msg = reader.readNext();
    }
}
