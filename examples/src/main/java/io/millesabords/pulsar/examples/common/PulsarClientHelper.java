package io.millesabords.pulsar.examples.common;

import org.apache.pulsar.client.api.*;
import org.apache.pulsar.client.impl.schema.JSONSchema;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PulsarClientHelper {

    private static Map<String, PulsarClient> clients = new HashMap<>();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            closeClients();
        }));
    }

    private static PulsarClient createClientIfNeeded(String serviceUrl) throws PulsarClientException {
        PulsarClient client = clients.get(serviceUrl);

        if (client == null) {
            client = PulsarClient.builder()
                    .serviceUrl(serviceUrl)
                    //.authentication(null)
                    .connectionsPerBroker(5)
                    .ioThreads(10)
                    .keepAliveInterval(2, TimeUnit.MINUTES)
                    .maxNumberOfRejectedRequestPerConnection(5)
                    .operationTimeout(10, TimeUnit.SECONDS)
                    .build();
            clients.put(serviceUrl, client);
        }

        return client;
    }

    /*public static PulsarAdmin createAdmin(String serviceUrl) throws PulsarClientException {
        return PulsarAdmin.builder().serviceHttpUrl(serviceUrl).build();
    }*/

    public static Producer<byte[]> createBasicProducer(String serviceUrl, String topic, String producerName)
            throws PulsarClientException {

        final PulsarClient client = createClientIfNeeded(serviceUrl);

        final Producer<byte[]> producer = client.newProducer()
                .topic(topic)
                .producerName(producerName)
                .sendTimeout(0, TimeUnit.SECONDS)
                .create();

        return producer;
    }

    public static <T> Producer<T> createBasicProducer(String serviceUrl, String topic, String producerName, JSONSchema<T> schema)
            throws PulsarClientException {

        final PulsarClient client = createClientIfNeeded(serviceUrl);

        final Producer<T> producer = client.newProducer(schema)
                .topic(topic)
                .producerName(producerName)
                .sendTimeout(0, TimeUnit.SECONDS)
                .create();

        return producer;
    }

    public static Consumer createConsumer(String serviceUrl, String topic, String subsName, SubscriptionType subType)
            throws PulsarClientException {

        final PulsarClient client = createClientIfNeeded(serviceUrl);

        final Consumer consumer = client.newConsumer()
                .topic(topic)
                .subscriptionName(subsName)
                .subscriptionType(subType)
                .subscribe();

        return consumer;
    }

    public static void closeClients() {
        clients.values().forEach(client -> {
            try {
                client.close();
            } catch (PulsarClientException e) {
                // ignore
            }
        });
    }

    public static void showMessage(Message msg) {
        System.out.printf("Message received: %s, from %s with id=%s\n",
                new String(msg.getData()),
                msg.getProducerName(),
                msg.getMessageId());
    }
}
