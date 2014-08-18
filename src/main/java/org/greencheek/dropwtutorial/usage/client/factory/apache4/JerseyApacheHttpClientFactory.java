package org.greencheek.dropwtutorial.usage.client.factory.apache4;

import com.sun.jersey.api.client.Client;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Environment;
import org.greencheek.dropwtutorial.usage.client.impl.JerseyUsageClient;
import org.greencheek.dropwtutorial.usage.client.UsageClient;
import org.greencheek.dropwtutorial.usage.client.config.UsageClientConfiguration;
import org.greencheek.dropwtutorial.usage.client.factory.ClientFactory;

/**
 * Created by dominictootell on 16/08/2014.
 */
public class JerseyApacheHttpClientFactory implements ClientFactory {


    @Override
    public UsageClient createClient(UsageClientConfiguration configuration, Environment environment) {
        return createUsageClient(configuration,environment);
    }

    public static UsageClient createUsageClient(UsageClientConfiguration configuration, Environment environment) {
        JerseyClientBuilder builder = new JerseyClientBuilder(environment).using(configuration.toJerseyClientConfiguration());
        Client c =  builder.build(environment.getName());

        return new JerseyUsageClient(c,configuration);
    }


}