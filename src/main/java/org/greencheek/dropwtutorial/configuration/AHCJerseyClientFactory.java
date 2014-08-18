package org.greencheek.dropwtutorial.configuration;

import com.sun.jersey.api.client.Client;
import io.dropwizard.setup.Environment;
import org.greencheek.dropwtutorial.usage.client.UsageClient;
import org.greencheek.dropwtutorial.usage.client.config.UsageClientConfiguration;
import org.greencheek.dropwtutorial.usage.client.factory.ClientFactory;
import org.sonatype.spice.jersey.client.ahc.AhcHttpClient;
import org.sonatype.spice.jersey.client.ahc.config.DefaultAhcConfig;

/**
 * Created by dominictootell on 16/08/2014.
 */
public class AHCJerseyClientFactory implements ClientFactory {
    @Override
    public UsageClient createClient(UsageClientConfiguration configuration,
                               Environment environment) {

//        DefaultAhcConfig config = new DefaultAhcConfig();
//
//        config.getAsyncHttpClientConfigBuilder().setAllowPoolingConnection(true)
//                .setMaximumConnectionsTotal(configuration.getMaxConnections())
//                .setMaximumConnectionsPerHost(configuration.getMaxConnections())
//                .setMaxRequestRetry(configuration.getRetries())
//                .setCompressionEnabled(configuration.isGzipEnabled())
//                .setRequestTimeoutInMs((int) configuration.getTimeout().toMilliseconds())
//                .setMaxConnectionLifeTimeInMs((int) configuration.getTimeToLive().toMilliseconds())
//                .setUserAgent(configuration.getUserAgent().or("default pooling"));
//
//

//        return AhcHttpClient.create(config);
        return null;
    }
}
