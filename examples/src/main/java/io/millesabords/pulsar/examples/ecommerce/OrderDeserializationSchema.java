package io.millesabords.pulsar.examples.ecommerce;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

import java.io.IOException;

public class OrderDeserializationSchema implements DeserializationSchema<Order> {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Order deserialize(byte[] bytes) throws IOException {
        return objectMapper.readValue(bytes, Order.class);
    }

    @Override
    public boolean isEndOfStream(Order order) {
        return false;
    }

    @Override
    public TypeInformation<Order> getProducedType() {
        return TypeInformation.of(Order.class);
    }
}
