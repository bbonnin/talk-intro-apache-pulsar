package io.millesabords.pulsar.examples.ecommerce;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.Country;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class FilterByCountryFunction implements Function<Order, Order> {

    private static DatabaseReader dbReader;

    private Logger log;



    @Override
    public Order process(Order order, Context context) throws Exception {

        log = context.getLogger();

        loadIpDatabase(context.getUserConfigValue("dbpath").get().toString());

        final InetAddress ipAddress = InetAddress.getByName(order.getIpAddress());
        Country country = null;

        try {
            final CountryResponse response = dbReader.country(ipAddress);
            country = response.getCountry();
        }
        catch (IOException | GeoIp2Exception e) {
            log.error("Get country from IP address", e);
        }

        if (country != null && "US".equals(country.getIsoCode())) {
            // Send this order to US specific topic
            context.publish("orders-us", order);
            //return order;
        }


        context.incrCounter("num-orders", 1);
        long num = context.getCounter("num-orders");
        log.info("#orders: {}", num);

        final double prevAverage = getAvgAmount(context);
        final double newAverage = (prevAverage * (num - 1) + order.getAmount()) / num;
        setAvgAmount(newAverage, context);
        log.info("avg amount: {}", newAverage);

        return null;
    }

    private void loadIpDatabase(String dbPath) {

        if (dbReader == null) {
            log.info("Loading DB... " + dbPath);
            final File database = new File(dbPath);

            try {
                dbReader = new DatabaseReader.Builder(database).build();
            }
            catch (IOException e) {
                log.error("Cannot load database", e);
            }
        }
    }

    private double getAvgAmount(Context context) {
        try {
            ByteBuffer state = context.getState("avg-amount");

            if (state.array().length > 0) {
                return state.getDouble();
            }
        }
        catch (Exception e) {
        }

        return 0;
    }

    private void setAvgAmount(double avg, Context context) {
        ByteBuffer state = ByteBuffer.allocate(8);
        state.putDouble(avg);
        context.putState("avg-amount", state);

        context.putState("average-amount", ByteBuffer.wrap(Double.toString(avg).getBytes()));
    }
}
