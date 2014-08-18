package org.greencheek.dropwtutorial.usage.client.factory.hystrix;

import io.dropwizard.setup.Environment;
import org.greencheek.dropwtutorial.usage.client.UsageClient;
import org.greencheek.dropwtutorial.usage.client.config.UsageClientConfiguration;
import org.greencheek.dropwtutorial.usage.client.factory.ClientFactory;
import org.greencheek.dropwtutorial.usage.client.factory.apache4.JerseyApacheHttpClientFactory;
import org.greencheek.dropwtutorial.usage.client.impl.Slf4jLoggingUsageClient;
import org.greencheek.dropwtutorial.usage.client.impl.hystrix.HystrixLoggingUsageClient;

/**
 * Created by dominictootell on 17/08/2014.
 */
public class HystrixJerseyApacheWithLoggingClientFactory implements ClientFactory {


    private final UsageClient fallbackUsageClient;

    public HystrixJerseyApacheWithLoggingClientFactory() {
        this(Slf4jLoggingUsageClient.INSTANCE);
    }

    public HystrixJerseyApacheWithLoggingClientFactory(UsageClient fallback) {
        this.fallbackUsageClient = fallback;
    }

    @Override
    public UsageClient createClient(UsageClientConfiguration configuration, Environment environment) {
        UsageClient httpClient = JerseyApacheHttpClientFactory.createUsageClient(configuration,environment);

        return new HystrixLoggingUsageClient(httpClient,configuration,fallbackUsageClient);
    }
}
