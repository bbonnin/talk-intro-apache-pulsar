package io.millesabords.pulsar.examples;


import lombok.Getter;
import lombok.Setter;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.impl.schema.AvroSchema;

public class DataGenerator {

    @Setter
    @Getter
    public static class Foo {
        private int field1 = 1;
        private String field2;
        private long field3;
    }

    public static void main(String[] args) throws Exception {
        PulsarClient pulsarClient = PulsarClient.builder()
                .serviceUrl("pulsar://localhost:6650")
                .build();

        /*
        PulsarAdmin admin = PulsarAdmin.builder().serviceHttpUrl("http://localhost:8080").build();
        //admin.namespaces().createNamespace("public/test_ns");
        admin.namespaces().setRetention("public/test_ns", new RetentionPolicies(10, 10));
        */

        Producer<Foo> producer = pulsarClient
                .newProducer(AvroSchema.of(Foo.class))
                .topic("public/test_ns/testfoo")
                .create();

        for (int i = 0; i < 100; i++) {
            Foo foo = new Foo();
            foo.setField1(i);
            foo.setField2("foo" + i);
            foo.setField3(System.currentTimeMillis());
            producer.newMessage().value(foo).send();
            Thread.sleep(200);
        }

        producer.close();
        pulsarClient.close();
    }
}