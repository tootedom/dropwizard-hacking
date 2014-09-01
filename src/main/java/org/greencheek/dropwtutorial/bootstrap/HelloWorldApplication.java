package org.greencheek.dropwtutorial.bootstrap;




import com.sun.jersey.api.client.Client;
import com.yammer.tenacity.core.bundle.TenacityBundleBuilder;
import com.yammer.tenacity.core.properties.TenacityPropertyKey;
import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.greencheek.dropwtutorial.clientconfig.ClientLibPropertyKeyFactory;
import org.greencheek.dropwtutorial.clientconfig.ClientLibPropertyKeys;
import org.greencheek.dropwtutorial.configuration.ClientPropertyKeyFactory;
import org.greencheek.dropwtutorial.configuration.ClientPropertyKeys;
import org.greencheek.dropwtutorial.configuration.ClientPropertyRegister;
import org.greencheek.dropwtutorial.configuration.HelloWorldConfiguration;
import org.greencheek.dropwtutorial.usage.client.config.UsageClientConfiguration;
import org.greencheek.dropwtutorial.health.TemplateHealthCheck;
import org.greencheek.dropwtutorial.resources.GetJsonDocResource;
import org.greencheek.dropwtutorial.resources.HelloWorldResource;
import org.greencheek.dropwtutorial.service.JerseyGetJsonDoc;
import org.greencheek.dropwtutorial.usage.client.factory.hystrix.HystrixJerseyApacheWithLoggingClientFactory;
import org.sonatype.spice.jersey.client.ahc.AhcHttpClient;
import org.sonatype.spice.jersey.client.ahc.config.DefaultAhcConfig;

import java.util.ArrayList;
import java.util.List;


public class HelloWorldApplication extends Application<HelloWorldConfiguration> {
    public static void main(String[] args) throws Exception {
        new HelloWorldApplication().run(args);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<HelloWorldConfiguration> bootstrap) {

        List<TenacityPropertyKey> keys = new ArrayList<TenacityPropertyKey>();
        for(TenacityPropertyKey key : ClientPropertyKeys.values()) {
            keys.add(key);
        }
        for(TenacityPropertyKey key : ClientLibPropertyKeys.values()) {
            keys.add(key);
        }

        bootstrap.addBundle(TenacityBundleBuilder
                .newBuilder()
                .propertyKeyFactory(new ClientLibPropertyKeyFactory(new ClientPropertyKeyFactory()))
                .propertyKeys(keys)
                .build());

//        bootstrap.addBundle(TenacityBundleBuilder
//                .newBuilder()
//                .propertyKeyFactory(new ClientLibPropertyKeyFactory())
//                .propertyKeys(ClientLibPropertyKeys.values())
//                .build());
    }

    @Override
    public void run(HelloWorldConfiguration configuration,
                    Environment environment) {
        new ClientPropertyRegister(configuration).register();

        final HelloWorldResource resource = new HelloWorldResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );
        final TemplateHealthCheck healthCheck =
                new TemplateHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);
        environment.jersey().register(resource);

        HystrixJerseyApacheWithLoggingClientFactory factory = new HystrixJerseyApacheWithLoggingClientFactory();

        environment.jersey().register(
                new GetJsonDocResource(
                    new JerseyGetJsonDoc(buildNettyPooledClient(configuration, environment), configuration.getJsonDocUrl()),
                    new JerseyGetJsonDoc(buildNettyPooledClient(configuration, environment), configuration.getJsonDocUrl()),
                    new JerseyGetJsonDoc(buildDefaultClient(),configuration.getJsonDocUrl()),
                    factory.createClient(configuration.getUsageClientConfiguration(),environment)
        )
        );



    }

    private Client buildNettyPooledClient(HelloWorldConfiguration configuration,
                                          Environment environment) {

        JerseyClientConfiguration jconfig = configuration.getJerseyClientConfiguration();


        DefaultAhcConfig config = new DefaultAhcConfig();

        config.getAsyncHttpClientConfigBuilder().setAllowPoolingConnection(true)
                .setMaximumConnectionsTotal(jconfig.getMaxConnections())
                .setMaximumConnectionsPerHost(jconfig.getMaxConnections())
                .setMaxRequestRetry(jconfig.getRetries())
                .setCompressionEnabled(jconfig.isGzipEnabled())
                .setRequestTimeoutInMs((int) jconfig.getTimeout().toMilliseconds())
                .setMaxConnectionLifeTimeInMs((int) jconfig.getTimeToLive().toMilliseconds())
                .setUserAgent(jconfig.getUserAgent().or("default pooling"));

        return AhcHttpClient.create(config);
//
//        return new JerseyClientBuilder(environment).using(configuration.getJerseyClientConfiguration())
//                .build(getName());



    }

    private Client buildJerseyPooledClient(HelloWorldConfiguration configuration,
                                          Environment environment) {

        return new JerseyClientBuilder(environment).using(configuration.getJerseyClientConfiguration())
                .build(getName());



    }

    private Client buildDefaultClient() {
        return Client.create();
    }

}