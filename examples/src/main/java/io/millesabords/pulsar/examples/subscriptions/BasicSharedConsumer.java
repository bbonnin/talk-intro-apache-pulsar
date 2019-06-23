package io.millesabords.pulsar.examples.subscriptions;

import io.millesabords.pulsar.examples.common.PulsarClientHelper;
import org.apache.pulsar.client.api.*;

import java.util.concurrent.TimeUnit;

public class BasicSharedConsumer {

    public static void main(String[] args) throws PulsarClientException {

        Consumer consumer = null;

        try {
            consumer = PulsarClientHelper.createConsumer(
                    "pulsar://localhost:6650",
                    "demo-topic",
                    "demo-shared-subscription",
                    SubscriptionType.Shared);

            boolean stopped = false;

            do {
                Message msg = consumer.receive(1000, TimeUnit.SECONDS);

                stopped = msg == null;

                if (!stopped) {
                    PulsarClientHelper.showMessage(msg);
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
