package io.millesabords.pulsar.examples.ecommerce;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.pulsar.PulsarSourceBuilder;

public class FlinkConsumer {

    public static void main(String[] args) throws Exception {

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        final PulsarSourceBuilder<Order> builder = PulsarSourceBuilder
                .builder(new OrderDeserializationSchema())
                .serviceUrl("pulsar://localhost:6650")
                .topic("talk/demo/orders-all")
                .subscriptionName("flink-orders-all-sub");

        final SourceFunction<Order> source = builder.build();

        final DataStreamSource<Order> orders = env.addSource(source);

        orders
                .setParallelism(1)
                .map((order) -> order.getAmount())
                .timeWindowAll(Time.seconds(5))
                .reduce((amount1, amount2) -> amount1 + amount2)
                .print();

        env.execute();

    }
}
