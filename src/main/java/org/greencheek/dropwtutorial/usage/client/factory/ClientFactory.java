package org.greencheek.dropwtutorial.usage.client.factory;

import com.sun.jersey.api.client.Client;
import io.dropwizard.setup.Environment;
import org.greencheek.dropwtutorial.usage.client.UsageClient;
import org.greencheek.dropwtutorial.usage.client.config.UsageClientConfiguration;

/**
 * Created by dominictootell on 16/08/2014.
 */
public interface ClientFactory {
    UsageClient createClient(UsageClientConfiguration configuration, Environment environment);
}
