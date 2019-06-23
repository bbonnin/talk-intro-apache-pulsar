package io.millesabords.pulsar.examples;

import org.apache.pulsar.client.api.*;

import java.util.concurrent.TimeUnit;

public class FullyConfiguredConsumer {

    public static void main(String[] args) throws PulsarClientException {

        PulsarClient client = PulsarClient.builder()
                .serviceUrl("pulsar://localhost:6650")
                .build();

        Consumer consumer = client.newConsumer()
                .topic("demo-topic")
                .subscriptionName("demo-shared-subscription")
                .subscriptionType(SubscriptionType.Shared)
                //.acknowledgmentGroupTime()
                //.ackTimeout()
                //.autoUpdatePartitions()
                //.consumerName()
                //.deadLetterPolicy(DeadLetterPolicy.builder().deadLetterTopic())
                //.maxTotalReceiverQueueSizeAcrossPartitions()
                //.receiverQueueSize()
                //.priorityLevel()
                //.readCompacted()
                //.cryptoFailureAction()
                //.cryptoKeyReader()
                .subscribe();

        consumer.seek(MessageId.earliest);

        boolean stopped = false;

        do {
            Message msg = consumer.receive(100, TimeUnit.SECONDS);

            stopped = msg == null;

            if (!stopped) {
                System.out.printf("Message received: %s (from %s, seqid=%d)\n",
                        new String(msg.getData()),
                        msg.getProducerName(),
                        msg.getSequenceId());

                consumer.acknowledge(msg);
            }
        }
        while (!stopped);

        consumer.close();
        client.close();
    }
}
