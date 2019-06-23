package io.millesabords.pulsar.examples;

import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;
import org.slf4j.Logger;

public class EnhancedHiFunction implements Function<String, String> {

    @Override
    public String process(String input, Context context) throws Exception {
        Logger logger = context.getLogger();

        String functionTenant = context.getTenant();
        String functionNamespace = context.getNamespace();
        String functionName = context.getFunctionName();

        logger.error("Function {}/{}/{}: input={}", functionTenant, functionNamespace, functionName, input);

        return String.format("Hi %s!", input);
    }
}
