package io.millesabords.pulsar.examples.common;

import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClientException;

public class BasicProducer {

    public static void main(String[] args) throws PulsarClientException, InterruptedException {

        final Producer<byte[]> producer = PulsarClientHelper.createBasicProducer(
                "pulsar://localhost:6650",
                "public/demo/demo-topic",
                "demo-basic-producer");

        for (int i=0; i<100; i++) {
            final MessageId msgId = producer.send("Hello !".getBytes());
            System.out.println(msgId);
            //Thread.sleep(Math.round(Math.random() * 1000));

//            producer.sendAsync("Hello !".getBytes())
//                    .thenAccept()
        }

        producer.close();
        PulsarClientHelper.closeClients();
    }
}
