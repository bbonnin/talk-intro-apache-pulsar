package io.millesabords.pulsar.examples;

import org.apache.pulsar.client.api.*;

import java.util.concurrent.TimeUnit;

public class FunctionLogConsumer {

    public static void main(String[] args) throws PulsarClientException {

        PulsarClient client = PulsarClient.builder()
                .serviceUrl("pulsar://localhost:6650")
                .build();

        Consumer consumer = client.newConsumer()
                .topic("talk/demo/logs")
                .subscriptionName("demo-log-subscription")
                .subscriptionType(SubscriptionType.Exclusive)
                .subscribe();

        consumer.seek(MessageId.earliest);

        boolean stopped = false;

        do {
            Message msg = consumer.receive(100, TimeUnit.SECONDS);

            stopped = msg == null;

            if (!stopped) {
                System.out.printf("Message received: %s (from %s, seqid=%d, props=%s)\n",
                        new String(msg.getData()),
                        msg.getProducerName(),
                        msg.getSequenceId(),
                        msg.getProperties().toString());

                consumer.acknowledge(msg);
            }
        }
        while (!stopped);

        consumer.close();
        client.close();
    }
}
